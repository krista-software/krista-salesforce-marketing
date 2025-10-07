package app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.connectors;

import app.krista.extension.authorization.MustAuthorizeException;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.SalesforceMarketingAttributes;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.stores.RefreshTokenStore;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.util.Constants;
import app.krista.ksdk.context.AuthorizationContext;
import app.krista.ksdk.context.RequestContext;
import app.krista.model.field.NamedValuedField;
import com.github.scribejava.apis.salesforce.SalesforceToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuth2AccessTokenErrorResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public final class SalesforceTokenProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesforceTokenProvider.class);

    private final RefreshTokenStore refreshTokenStore;
    private final RequestContext requestContext;
    private final SalesforceMarketingAttributes salesforceMarketingAttributes;
    private final String authContextId;
    private final AuthorizationContext authorizationContext;

    @Inject
    public SalesforceTokenProvider(RefreshTokenStore refreshTokenStore, RequestContext requestContext, SalesforceMarketingAttributes salesforceMarketingAttributes, AuthorizationContext authorizationContext) {
        this(refreshTokenStore, requestContext, salesforceMarketingAttributes, authorizationContext, null);
    }

    public SalesforceTokenProvider(RefreshTokenStore refreshTokenStore, RequestContext requestContext, SalesforceMarketingAttributes salesforceMarketingAttributes, AuthorizationContext authorizationContext, String authContextId) {
        this.refreshTokenStore = refreshTokenStore;
        this.requestContext = requestContext;
        this.salesforceMarketingAttributes = salesforceMarketingAttributes;
        this.authorizationContext = authorizationContext;
        this.authContextId = authContextId;
    }

    /**
     * @return {@link SalesforceMarketingAttributes}
     */
    public SalesforceMarketingAttributes getSalesforceMarketingAttributes() {
        return salesforceMarketingAttributes;
    }

    /**
     * Retrieves a Salesforce token for the current user.
     *
     * @return A valid Salesforce token for the current user
     */
    public SalesforceToken getSalesforceToken() throws IOException {
        return getSalesforceToken(requestContext.invokeAsUser(), null);
    }

    /**
     * Retrieves a Salesforce token for admin operations.
     */
    public void getSalesforceTokenForAdmin() throws IOException {
        getSalesforceToken(false, null);
    }

    /**
     * Backward-compatible delegate preserving original signature.
     */
    private SalesforceToken getSalesforceToken(boolean isAdmin) throws IOException {
        return getSalesforceToken(isAdmin, null);
    }

    /**
     * Retrieves a Salesforce token based on the specified parameters.
     *
     * <p>This method automatically selects the appropriate authentication flow based on
     * the configuration and parameters:</p>
     * <ul>
     *   <li>Client Credentials flow if Instance URL is provided</li>
     *   <li>Authorization Code flow if no Instance URL and incomplete password credentials</li>
     *   <li>Password Grant flow if complete username/password/token credentials are provided</li>
     * </ul>
     *
     * @param isAdmin   Whether the token is for admin operations (forces OAuth flow)
     * @param accountId The account ID to use, or null to use the current authorized account
     * @return          A valid Salesforce token for API access
     * @throws IllegalArgumentException If the provided credentials are invalid
     * @throws MustAuthorizeException   If authorization is required to obtain a new token
     */
    private SalesforceToken getSalesforceToken(boolean isAdmin, String accountId) throws IllegalArgumentException {
        if (validateUsingOAuth(isAdmin, salesforceMarketingAttributes)) {
            String domain = salesforceMarketingAttributes.getInstanceUrl();
            if (domain != null && !domain.isEmpty()) {
                LOGGER.debug("Using Client Credentials flow for domain: {}", domain);
                return getTokenUsingClientCredentials(domain);
            } else {
                LOGGER.debug("Using Authorization Code flow with refresh token");
                String effectiveAccountId = accountId == null ? authorizationContext.getAuthorizedAccount().getAccountId() : accountId;
                String refreshToken = refreshTokenStore.get(effectiveAccountId + "&" + salesforceMarketingAttributes.getClientId() + salesforceMarketingAttributes.getClientSecret());
                if (refreshToken == null || refreshToken.isBlank()) {
                    LOGGER.debug("No refresh token found for account: {}", effectiveAccountId);
                    throw createMustAuthorizationException(false);
                }
                try {
                    LOGGER.debug("Attempting to refresh access token for account: {}", effectiveAccountId);
                    return getSalesforceTokenForUser(salesforceMarketingAttributes.getOauthService().refreshAccessToken(refreshToken));
                } catch (MustAuthorizeException cause) {
                    LOGGER.debug("Authorization required after refresh token attempt");
                    throw createMustAuthorizationException(true);
                } catch (OAuth2AccessTokenErrorResponse cause) {
                    if (cause.getErrorDescription() != null && cause.getErrorDescription().equals("expired access/refresh token")) {
                        LOGGER.debug("Refresh token expired, re-authorization required");
                        throw createMustAuthorizationException(true);
                    }
                    LOGGER.error("OAuth token refresh failed: {} - {}", cause.getError(), cause.getErrorDescription());
                    throw new RuntimeException("Something went wrong, please try again later. " + cause.getErrorDescription(), cause);
                } catch (InterruptedException | ExecutionException | IOException cause) {
                    Thread.currentThread().interrupt();
                    LOGGER.error("Token refresh interrupted or failed: {}", cause.getMessage(), cause);
                    throw new RuntimeException("Refresh Token not found. Please re authorize yourself. " + cause.getMessage(), cause);
                }
            }
        } else {
            LOGGER.debug("Using Password Grant flow");
            Object username = salesforceMarketingAttributes.getUserName();
            Object password = salesforceMarketingAttributes.getPassword();
            Object securityToken = salesforceMarketingAttributes.getSecurityToken();

            try {
                LOGGER.debug("Attempting OAuth 2.0 Password Grant flow for username: {}", username);
                return getSalesforceTokenForUser(
                        salesforceMarketingAttributes.getOauthService().getAccessTokenPasswordGrant(String.valueOf(username),
                                password + String.valueOf(securityToken)));
            } catch (OAuth2AccessTokenErrorResponse cause) {
                LOGGER.error("OAuth 2.0 Password Grant failed with error: {} - {}", cause.getError(), cause.getErrorDescription());
                String message = this.getErrorMessage(cause);
                throw new IllegalArgumentException(message, cause);
            } catch (InterruptedException cause) {
                Thread.currentThread().interrupt();
                LOGGER.error("Password Grant authentication was interrupted: {}", cause.getMessage());
                throw new RuntimeException("Authentication was interrupted. Please try again.", cause);
            } catch (ExecutionException cause) {
                LOGGER.error("Password Grant authentication execution failed: {}", cause.getMessage(), cause);
                String message = this.getErrorMessage(cause);
                throw new IllegalArgumentException(message, cause);
            } catch (IOException cause) {
                LOGGER.error("Network error during Password Grant authentication: {}", cause.getMessage(), cause);
                throw new RuntimeException("Network error during authentication. Please check your connection and try again.", cause);
            }
        }
    }

    /**
     * Retrieves a Salesforce token using OAuth 2.0 Client Credentials flow.
     *
     * @param domain The Salesforce instance domain
     * @return A valid Salesforce token
     * @throws IllegalArgumentException If the client credentials are invalid or the request fails
     */
    private SalesforceToken getTokenUsingClientCredentials(String domain) throws IllegalArgumentException {
        try {
            LOGGER.debug("Attempting OAuth 2.0 Client Credentials flow for domain: {}", domain);
            return getSalesforceTokenForUser(salesforceMarketingAttributes.getOauthService(domain).getAccessTokenClientCredentialsGrant());
        } catch (OAuth2AccessTokenErrorResponse cause) {
            LOGGER.error("OAuth 2.0 Client Credentials failed with error: {} - {} for domain: {}",
                        cause.getError(), cause.getErrorDescription(), domain);
            String message = this.getErrorMessage(cause);
            throw new IllegalArgumentException(message, cause);
        } catch (InterruptedException cause) {
            Thread.currentThread().interrupt();
            LOGGER.error("Client Credentials authentication was interrupted for domain: {}", domain);
            throw new RuntimeException("Authentication was interrupted. Please try again.", cause);
        } catch (ExecutionException cause) {
            LOGGER.error("Client Credentials authentication execution failed for domain: {} - {}", domain, cause.getMessage(), cause);
            String message = this.getErrorMessage(cause);
            throw new IllegalArgumentException(message, cause);
        } catch (IOException cause) {
            LOGGER.error("Network error during Client Credentials authentication for domain: {} - {}", domain, cause.getMessage(), cause);
            throw new RuntimeException("Network error during authentication. Please check your connection and try again.", cause);
        }
    }

    /**
     * Returns a user-friendly error message parsed from exception content.
     */
    private String getErrorMessage(Exception cause) {
        String message = cause.getMessage();
        if (message != null) {
            try {
                JSONObject json = new JSONObject(message);
                if (json.has("error")) {
                    String errorMessage = json.getString("error");
                    return switch (errorMessage) {
                        case "invalid_client" -> "Authentication failed. Please check your Salesforce client credentials.";
                        case "invalid_grant" -> "Authentication failed. Please check your Salesforce username, password, and security token.";
                        default -> "Authentication failed: " + errorMessage;
                    };
                }
            } catch (Exception exception) {
                LOGGER.debug("Failed to parse error message as JSON: {}", exception.getMessage(), exception);
                return "Provided attributes are not valid.";
            }
        }
        return "An unexpected error occurred. Please contact support.";
    }

    /**
     * This function will generate salesforce token for the given access token.
     *
     * @param accessToken {@link OAuth2AccessToken} value
     * @return {@link SalesforceToken} object
     */
    private SalesforceToken getSalesforceTokenForUser(OAuth2AccessToken accessToken) {
        final SalesforceToken salesforceAccessToken;
        if (accessToken instanceof SalesforceToken) {
            salesforceAccessToken = (SalesforceToken) accessToken;
        } else if (accessToken.getAccessToken() != null) {
            salesforceAccessToken = new SalesforceToken(accessToken.getAccessToken(), salesforceMarketingAttributes.getInstanceUrl(), accessToken.getRawResponse());
        } else {
            throw new IllegalArgumentException(Constants.SALESFORCE_DIDN_T_RETURN_THE_CORRECT_TOKEN_PLEASE_REAUTHORIZE);
        }
        return salesforceAccessToken;
    }

    /**
     * Determines whether OAuth-based authentication should be used based on the context and attributes.
     *
     * @param isAdmin Whether this is an admin operation
     * @param attributes The Salesforce marketing attributes containing authentication configuration
     * @return true if OAuth should be used, false if Password Grant should be used
     */
    public static boolean validateUsingOAuth(boolean isAdmin, SalesforceMarketingAttributes attributes) {
        boolean hasInstanceUrl = attributes.getInstanceUrl() != null && !attributes.getInstanceUrl().trim().isEmpty();
        boolean hasCompletePasswordCredentials = attributes.getUserName() != null && !attributes.getUserName().isBlank() &&
                attributes.getPassword() != null && !attributes.getPassword().isBlank() &&
                attributes.getSecurityToken() != null && !attributes.getSecurityToken().isBlank();

        boolean useOAuth = isAdmin || !hasCompletePasswordCredentials;

        // Log the authentication decision
        if (LOGGER.isDebugEnabled()) {
            if (isAdmin) {
                LOGGER.debug("Using OAuth flow because admin operation is requested");
            } else if (hasInstanceUrl) {
                LOGGER.debug("Using OAuth Client Credentials flow because Instance URL is configured: {}", attributes.getInstanceUrl());
            } else if (!hasCompletePasswordCredentials) {
                LOGGER.debug("Using OAuth Authorization Code flow because password credentials are incomplete (username: {}, password: {}, token: {})",
                           attributes.getUserName() != null && !attributes.getUserName().isBlank() ? "present" : "missing",
                           attributes.getPassword() != null && !attributes.getPassword().isBlank() ? "present" : "missing",
                           attributes.getSecurityToken() != null && !attributes.getSecurityToken().isBlank() ? "present" : "missing");
            } else {
                LOGGER.debug("Using OAuth Password Grant flow with username: {}", attributes.getUserName());
            }
        }

        return useOAuth;
    }

    /**
     * This function will throw {@link MustAuthorizeException} if the authentication is not successful
     *
     * @param reAuthentication {@link Boolean} value to throw exception
     * @return {@link MustAuthorizeException}
     */
    private MustAuthorizeException createMustAuthorizationException(boolean reAuthentication) {
        List<NamedValuedField> details = new ArrayList<>();
        NamedValuedField userIdField = new NamedValuedField(Constants.USER_ID, Constants.TEXT, authorizationContext.getAuthorizedAccount().getAccountId(), new HashMap<>(), new HashMap<>());
        details.add(userIdField);
        if (authContextId != null) {
            NamedValuedField contextIdField = new NamedValuedField(Constants.AUTH_CONTEXT_ID, Constants.TEXT, authContextId, new HashMap<>(), new HashMap<>());
            details.add(contextIdField);
        }
        return new MustAuthorizeException(reAuthentication ? Constants.REFRESH_TOKEN_IS_NO_LONGER_VALID + Constants.PLEASE_REAUTHORIZE_YOURSELF : Constants.AUTHORIZATION_PROMPT, details);
    }

}
