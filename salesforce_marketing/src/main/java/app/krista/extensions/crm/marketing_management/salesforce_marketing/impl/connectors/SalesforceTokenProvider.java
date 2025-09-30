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
     * @param isAdmin   Whether the token is for admin operations
     * @param accountId The account ID to use, or null to use the current authorized account
     * @return A valid Salesforce token
     * @throws IllegalArgumentException If the provided credentials are invalid
     * @throws MustAuthorizeException   If authorization is required to obtain a new token
     */
    private SalesforceToken getSalesforceToken(boolean isAdmin, String accountId) throws IllegalArgumentException {
        if (validateUsingOAuth(isAdmin, salesforceMarketingAttributes)) {
            String domain = salesforceMarketingAttributes.getInstanceUrl();
            if (domain != null && !domain.isEmpty()) {
                return getTokenUsingClientCredentials(domain);
            } else {
                String effectiveAccountId = accountId == null ? authorizationContext.getAuthorizedAccount().getAccountId() : accountId;
                String refreshToken = refreshTokenStore.get(effectiveAccountId + "&" + salesforceMarketingAttributes.getClientId() + salesforceMarketingAttributes.getClientSecret());
                if (refreshToken == null || refreshToken.isBlank()) {
                    throw createMustAuthorizationException(false);
                }
                try {
                    return getSalesforceTokenForUser(salesforceMarketingAttributes.getOauthService().refreshAccessToken(refreshToken));
                } catch (MustAuthorizeException cause) {
                    throw createMustAuthorizationException(true);
                } catch (OAuth2AccessTokenErrorResponse cause) {
                    if (cause.getErrorDescription() != null && cause.getErrorDescription().equals("expired access/refresh token")) {
                        throw createMustAuthorizationException(true);
                    }
                    throw new RuntimeException("Something went wrong, please try again later. " + cause.getErrorDescription(), cause);
                } catch (InterruptedException | ExecutionException | IOException cause) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Refresh Token not found. Please re authorize yourself. " + cause.getMessage(), cause);
                }
            }
        } else {
            Object username = salesforceMarketingAttributes.getUserName();
            Object password = salesforceMarketingAttributes.getPassword();
            Object securityToken = salesforceMarketingAttributes.getSecurityToken();

            try {
                return getSalesforceTokenForUser(
                        salesforceMarketingAttributes.getOauthService().getAccessTokenPasswordGrant(String.valueOf(username),
                                password + String.valueOf(securityToken)));
            } catch (Exception cause) {
                String message = this.getErrorMessage(cause);
                throw new IllegalArgumentException(message, cause);
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
            return getSalesforceTokenForUser(salesforceMarketingAttributes.getOauthService(domain).getAccessTokenClientCredentialsGrant());
        } catch (Exception cause) {
            LOGGER.error("Failed to get access token for user. - {}", cause.getMessage(), cause);
            String message = this.getErrorMessage(cause);
            throw new IllegalArgumentException(message, cause);
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
     */
    public static boolean validateUsingOAuth(boolean isAdmin, SalesforceMarketingAttributes attributes) {
        return isAdmin || (attributes.getUserName() == null || attributes.getUserName().isBlank() ||
                attributes.getPassword() == null || attributes.getPassword().isBlank() ||
                attributes.getSecurityToken() == null || attributes.getSecurityToken().isBlank());
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
