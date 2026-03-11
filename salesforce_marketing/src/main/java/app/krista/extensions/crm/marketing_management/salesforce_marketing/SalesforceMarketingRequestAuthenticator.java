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

package app.krista.extensions.crm.marketing_management.salesforce_marketing;

import app.krista.extension.authorization.MustAuthenticateException;
import app.krista.extension.authorization.MustAuthorizeException;
import app.krista.extension.authorization.RequestAuthenticator;
import app.krista.extension.request.ProtoRequest;
import app.krista.extension.request.ProtoResponse;
import app.krista.extension.request.protos.http.HttpRequest;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.stores.SalesforceMarketingAttributesStore;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.util.Constants;
import app.krista.model.field.NamedField;
import app.krista.model.field.NamedValuedField;
import com.github.scribejava.core.oauth.OAuth20Service;

import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.util.*;

public class SalesforceMarketingRequestAuthenticator implements RequestAuthenticator {


    private final SalesforceMarketingAttributes salesforceMarketingAttributes;
    private final SalesforceMarketingAttributesStore attributesStore;

    public SalesforceMarketingRequestAuthenticator(SalesforceMarketingAttributes salesforceMarketingAttributes, SalesforceMarketingAttributesStore attributesStore) {
        this.salesforceMarketingAttributes = salesforceMarketingAttributes;
        this.attributesStore = attributesStore;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public Set<String> getSupportedProtocols() {
        return Set.of();
    }

    @Override
    public String getAuthenticatedAccountId(ProtoRequest protoRequest) {
        try {
            HttpRequest httpRequest = (HttpRequest) protoRequest;
            if (httpRequest.getUri().getPath().equals(Constants.CALLBACK_URI)) {
                httpRequest.bufferBody();
                MultivaluedMap<String, String> queryParameters = httpRequest.getQueryParameters();
                if (queryParameters == null ||
                        queryParameters.get(Constants.STATE) == null ||
                        queryParameters.get(Constants.STATE).get(0) == null) {
                    return null;
                }
                String state = String.valueOf(queryParameters.get(Constants.STATE).get(0));
                String[] parts = state.split("&");
                return parts[0];
            } else {
                return null;
            }
        } catch (RuntimeException | IOException cause) {
            return null;
        }
    }

    @Override
    public boolean setServiceAuthorization(String s) {
        return false;
    }

    @Override
    public Map<String, NamedField> getAttributeFields() {
        return Map.of();
    }

    @Override
    public ProtoResponse getMustAuthenticateResponse(MustAuthenticateException cause, ProtoRequest request) {
        return null;
    }

    @Override
    public AuthorizationResponse getMustAuthenticateResponse(MustAuthenticateException cause) {
        return null;
    }

    @Override
    public ProtoResponse getMustAuthorizeResponse(MustAuthorizeException cause, ProtoRequest request) {
        return null;
    }

    @Override
    public AuthorizationResponse getMustAuthorizeResponse(MustAuthorizeException cause) {
        String userId = (String) cause.getDetails().get(0).getValue();
        Optional<NamedValuedField> authContextIdField = cause.getDetails().stream().filter(f -> Objects.equals(f.getName(), "authContextId")).findFirst();
        SalesforceMarketingAttributes effectiveSalesforceMarketingAttributes;
        String state;
        if (authContextIdField.isPresent()) {
            String authContextId = (String) authContextIdField.get().getValue();
            effectiveSalesforceMarketingAttributes = attributesStore.load(authContextId);
            state = userId + "#" + authContextId;
        } else {
            effectiveSalesforceMarketingAttributes = salesforceMarketingAttributes;
            state = userId;
        }
        OAuth20Service salesforceMarketingService = effectiveSalesforceMarketingAttributes.getOauthService();
        String url = salesforceMarketingService.getAuthorizationUrl(state);
        return new AuthorizationResponse(url, List.of());
    }
}
