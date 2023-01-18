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

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public final class SalesforceTokenProvider {

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
     * This function will return Salesforce Token for the requested user
     *
     * @return {@link SalesforceToken}
     * @throws IOException if there is error in user input
     */
    public SalesforceToken getSalesforceToken() throws IOException {
        return getSalesforceToken(requestContext.invokeAsUser());
    }

    /**
     * This function will create salesforce token for admin user
     *
     * @throws IOException if there is error in user input
     */
    public void getSalesforceTokenForAdmin() throws IOException {
        getSalesforceToken(false);
    }

    /**
     * This function will return the {@link SalesforceToken} for both admin and user
     *
     * @param isAdmin is the salesforce token for admin or user
     * @return {@link SalesforceToken} object
     * @throws IOException if there is error in user input
     */
    private SalesforceToken getSalesforceToken(boolean isAdmin) throws IOException {
        if (isAdmin || (salesforceMarketingAttributes.getUserName() == null || salesforceMarketingAttributes.getUserName().isBlank() ||
                salesforceMarketingAttributes.getPassword() == null || salesforceMarketingAttributes.getPassword().isBlank() ||
                salesforceMarketingAttributes.getSecurityToken() == null || salesforceMarketingAttributes.getSecurityToken().isBlank())) {
            String refreshToken = refreshTokenStore.get(authorizationContext.getAuthorizedAccount().getAccountId() + "&" + salesforceMarketingAttributes.getClientId() + salesforceMarketingAttributes.getClientSecret());
            if (refreshToken == null || refreshToken.isBlank()) {
                throw createMustAuthorizationException(false);
            }
            try {
                return getSalesforceToken(salesforceMarketingAttributes.getOauthService().refreshAccessToken(refreshToken));
            } catch (IOException | ExecutionException cause) {
                throw new IllegalStateException("Refresh Token not found. Please re authorize yourself. " + cause.getMessage(), cause);
            } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(interruptedException.getMessage());
            }
        } else {
            Object username = salesforceMarketingAttributes.getUserName();
            Object password = salesforceMarketingAttributes.getPassword();
            Object securityToken = salesforceMarketingAttributes.getSecurityToken();

            try {
                return getSalesforceToken(
                        salesforceMarketingAttributes.getOauthService().getAccessTokenPasswordGrant(String.valueOf(username),
                                password + String.valueOf(securityToken)));
            } catch (IOException | ExecutionException cause) {
                throw new MustAuthorizeException(cause.getMessage());
            } catch (InterruptedException cause) {
                Thread.currentThread().interrupt();
                throw new MustAuthorizeException(cause.getMessage());
            }
        }
    }

    /**
     * This function will generate salesforce token for the given access token.
     *
     * @param accessToken {@link OAuth2AccessToken} value
     * @return {@link SalesforceToken} object
     */
    private SalesforceToken getSalesforceToken(OAuth2AccessToken accessToken) {
        final SalesforceToken salesforceAccessToken;
        if (accessToken instanceof SalesforceToken) {
            salesforceAccessToken = (SalesforceToken) accessToken;
        } else {
            throw new MustAuthorizeException(Constants.SALESFORCE_DIDN_T_RETURN_THE_CORRECT_TOKEN_PLEASE_REAUTHORIZE);
        }
        return salesforceAccessToken;
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
        return new MustAuthorizeException(reAuthentication ? Constants.REFRESH_TOKEN_IS_NO_LONGER_VALID + Constants.PLEASE_REAUTHORIZE_YOURSELF : Constants.YOU_ARE_NOT_AUTHORIZED + Constants.PLEASE_AUTHORIZE_YOURSELF, details);
    }

}
