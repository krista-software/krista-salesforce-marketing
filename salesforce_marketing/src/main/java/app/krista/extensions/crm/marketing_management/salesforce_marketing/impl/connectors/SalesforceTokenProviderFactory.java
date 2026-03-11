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

package app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.connectors;

import app.krista.extensions.crm.marketing_management.salesforce_marketing.SalesforceMarketingAttributes;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.stores.RefreshTokenStore;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.stores.SalesforceMarketingAttributesStore;
import app.krista.ksdk.context.AuthorizationContext;
import app.krista.ksdk.context.RequestContext;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;

@Service
public final class SalesforceTokenProviderFactory {

    private final RequestContext requestContext;
    private final RefreshTokenStore refreshTokenStore;
    private final SalesforceTokenProvider defaultSalesforceTokenProvider;
    private final SalesforceMarketingAttributesStore attributesStore;
    private final AuthorizationContext authorizationContext;

    @Inject
    public SalesforceTokenProviderFactory(RefreshTokenStore refreshTokenStore, RequestContext requestContext, SalesforceMarketingAttributes salesforceMarketingAttributes, SalesforceMarketingAttributesStore attributesStore, AuthorizationContext authorizationContext) {
        this(refreshTokenStore, requestContext, authorizationContext,
                new SalesforceTokenProvider(refreshTokenStore, requestContext, salesforceMarketingAttributes, authorizationContext, null), attributesStore);
    }

    public SalesforceTokenProviderFactory(RefreshTokenStore refreshTokenStore, RequestContext requestContext, AuthorizationContext authorizationContext, SalesforceTokenProvider salesforceTokenProvider, SalesforceMarketingAttributesStore attributesStore) {
        this.refreshTokenStore = refreshTokenStore;
        this.requestContext = requestContext;
        this.authorizationContext = authorizationContext;
        this.defaultSalesforceTokenProvider = salesforceTokenProvider;
        this.attributesStore = attributesStore;
    }

    /**
     * This function will create {@link SalesforceTokenProvider} object to get salesforce token for the given salesforce credentials
     *
     * @param salesforceMarketingAttributes Salesforce portal credentials
     * @return {@link SalesforceTokenProvider} object
     */
    public SalesforceTokenProvider create(SalesforceMarketingAttributes salesforceMarketingAttributes) {
        String authContextId = attributesStore.save(salesforceMarketingAttributes);
        return new SalesforceTokenProvider(refreshTokenStore, requestContext, salesforceMarketingAttributes, authorizationContext, authContextId);
    }

    public SalesforceTokenProvider create() {
        return defaultSalesforceTokenProvider;
    }
}
