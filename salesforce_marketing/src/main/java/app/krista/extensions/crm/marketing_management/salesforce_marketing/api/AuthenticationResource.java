/*
 * Salesforce Marketing Extension for Krista
 * Copyright (C) 2025 Krista Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package app.krista.extensions.crm.marketing_management.salesforce_marketing.api;

import app.krista.extensions.crm.marketing_management.salesforce_marketing.SalesforceMarketingAttributes;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.connectors.SalesforceTokenProvider;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.connectors.SalesforceTokenProviderFactory;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.stores.RefreshTokenStore;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.stores.SalesforceMarketingAttributesStore;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.util.Constants;
import app.krista.ksdk.authentication.AuthorizationListener;
import app.krista.ksdk.context.AuthorizationContext;
import com.github.scribejava.core.model.OAuth2AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Path("/")
public final class AuthenticationResource {
    private final RefreshTokenStore refreshTokenStore;
    private final SalesforceMarketingAttributesStore attributesStore;
    private final SalesforceTokenProviderFactory providerFactory;
    private final AuthorizationContext authorizationContext;
    private final AuthorizationListener authorizationListener;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationResource.class);

    @Inject
    public AuthenticationResource(RefreshTokenStore refreshTokenStore, SalesforceMarketingAttributesStore attributesStore, SalesforceTokenProviderFactory providerFactory, AuthorizationContext authorizationContext, AuthorizationListener authorizationListener) {
        this.refreshTokenStore = refreshTokenStore;
        this.attributesStore = attributesStore;
        this.providerFactory = providerFactory;
        this.authorizationListener = authorizationListener;
        this.authorizationContext = authorizationContext;
    }

    /**
     * This function will check if the state parameter of OAuth is valid
     *
     * @param parts will contain user ID and authorization context ID
     * @return {@link Boolean} value
     */
    private static boolean isStateInValid(String[] parts) {
        return parts[0].isBlank() || parts.length > 2;
    }

    /**
     * This function will return the authorization successful string
     *
     * @param code  authorization code to get the access token
     * @param state user Id and authorization context id
     * @return String with successful validation if authentication is successful
     */
    @GET
    @Path("/callback")
    public String getCallBack(@QueryParam(Constants.CODE) String code, @QueryParam(Constants.STATE) String state) {
        String[] parts = state.split("#");
        if (isStateInValid(parts)) {
            throw new BadRequestException(Constants.INVALID_STATE_PARAMETERS);
        }
        String key = parts[0];
        String authContextId = parts.length == 2 ? parts[1] : null;
        try {
            SalesforceTokenProvider tokenProvider = getSalesforceTokenProvider(authContextId);
            OAuth2AccessToken accessToken = tokenProvider.getSalesforceMarketingAttributes().getOauthService().getAccessToken(Objects.requireNonNull(code));
            refreshTokenStore.put(key +
                            "&" +
                            tokenProvider.getSalesforceMarketingAttributes().getClientId() +
                            tokenProvider.getSalesforceMarketingAttributes().getClientSecret(),
                    accessToken.getRefreshToken());
            if (authorizationContext.isAuthenticated()) {
                authorizationListener.authorized();
                return "User authenticated successfully. Please close the dialog.";
            }
            return "User authenticated successfully. Save the changes.";
        } catch (IOException | ExecutionException cause) {
            LOGGER.error("Exception occurred due to :" + cause.getMessage(), cause);
            throw new IllegalStateException(Constants.ERROR_OCCURRED_DURING_AUTHORIZATION, cause.getCause());
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            LOGGER.error("Exception occurred due to :" + interruptedException.getMessage(), interruptedException);
            throw new IllegalStateException(interruptedException.getMessage());
        } finally {
            if (authContextId != null) {
                attributesStore.remove(authContextId);
            }
        }
    }

    /**
     * This function will return {@link SalesforceTokenProvider} object for the given authorization context Id.
     *
     * @param authContextId authorization context Id
     * @return {@link SalesforceTokenProvider} object
     */
    private SalesforceTokenProvider getSalesforceTokenProvider(String authContextId) {
        SalesforceTokenProvider tokenProvider;
        if (authContextId == null) {
            tokenProvider = providerFactory.create();
        } else if (authContextId.isBlank()) {
            throw new BadRequestException(Constants.INVALID_STATE_PARAMETERS);
        } else {
            SalesforceMarketingAttributes salesforceMarketingAttributes = attributesStore.load(authContextId);
            tokenProvider = providerFactory.create(salesforceMarketingAttributes);
        }
        return tokenProvider;
    }

    /**
     * This function will get the Refresh Token for the given authorization context Id and authorization code
     *
     * @param code          authorization code
     * @param authContextId authorization context Id
     * @return {@link Map} of refresh token value
     */
    public Map<String, Object> getAuthTokenResponse(String code, String authContextId) {
        Map<String, Object> response = new HashMap<>();
        try {
            SalesforceTokenProvider tokenProvider = getSalesforceTokenProvider(authContextId);
            OAuth2AccessToken accessToken = tokenProvider.getSalesforceMarketingAttributes().getOauthService().getAccessToken(Objects.requireNonNull(code));
            response.put(Constants.REFRESH_TOKEN, accessToken.getRefreshToken());
        } catch (IOException | ExecutionException cause) {
            throw new IllegalStateException(Constants.ERROR_OCCURRED_DURING_AUTHORIZATION, cause.getCause());
        } catch (InterruptedException cause) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(Constants.ERROR_OCCURRED_DURING_AUTHORIZATION, cause.getCause());
        } finally {
            if (authContextId != null) {
                attributesStore.remove(authContextId);
            }
        }
        return response;
    }
}
