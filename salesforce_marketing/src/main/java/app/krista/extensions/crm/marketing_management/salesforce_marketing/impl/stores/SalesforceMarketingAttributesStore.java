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

package app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.stores;

import app.krista.extension.executor.Invoker;
import app.krista.extension.request.RoutingInfo;
import app.krista.extension.request.protos.http.HttpProtocol;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.SalesforceMarketingAttributes;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.util.Constants;
import app.krista.extensions.util.KeyValueStore;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public final class SalesforceMarketingAttributesStore {

    private final KeyValueStore store;
    private final Invoker invoker;

    @Inject
    public SalesforceMarketingAttributesStore(KeyValueStore store, Invoker invoker) {
        this.store = store;
        this.invoker = invoker;
    }

    /**
     * This function will return the authorization context Id.
     *
     * @param salesforceMarketingAttributes salesforce portal credentials
     * @return authorization code for the stored credentials
     */
    public String save(SalesforceMarketingAttributes salesforceMarketingAttributes) {
        String authContextId = UUID.randomUUID().toString();
        store.put(authContextId, getAttributesJson(salesforceMarketingAttributes));
        return authContextId;
    }

    private static String getAttributesJson(SalesforceMarketingAttributes salesforceMarketingAttributes) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(salesforceMarketingAttributes.CLIENT_ID,
                salesforceMarketingAttributes.getClientId());
        attributes.put(salesforceMarketingAttributes.CLIENT_SECRET, salesforceMarketingAttributes.getClientSecret());
        if (salesforceMarketingAttributes.getInstanceUrl() != null) {
            attributes.put(salesforceMarketingAttributes.INSTANCE_URL, salesforceMarketingAttributes.getInstanceUrl());
        }
        return Constants.GSON.toJson(attributes);
    }

    /**
     * This function will return the salesforce portal credentials for the given authorization code.
     *
     * @param authContextId authorization code of saved credentials
     * @return {@link SalesforceMarketingAttributes}
     */
    public SalesforceMarketingAttributes load(String authContextId) {
        var attributes = Constants.GSON.fromJson(store.get(authContextId).toString(), Map.class);
        return new SalesforceMarketingAttributes((String) attributes.get(SalesforceMarketingAttributes.CLIENT_ID),
                (String) attributes.get(SalesforceMarketingAttributes.CLIENT_SECRET),
                (String) attributes.get(SalesforceMarketingAttributes.INSTANCE_URL),
                (String) attributes.get(SalesforceMarketingAttributes.USER_NAME),
                (String) attributes.get(SalesforceMarketingAttributes.PASSWORD),
                (String) attributes.get(SalesforceMarketingAttributes.SECURITY_TOKEN),
                invoker.getRoutingInfo().getRoutingURL(HttpProtocol.PROTOCOL_NAME, RoutingInfo.Type.APPLIANCE));
    }

    /**
     * This function will remove the salesforce credentials for the given authorization code.
     *
     * @param authContextId input key
     */
    public void remove(String authContextId) {
        store.remove(authContextId);
    }
}
