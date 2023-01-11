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
