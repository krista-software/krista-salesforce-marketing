package app.krista.extensions.crm.marketing_management.salesforce_marketing;

import app.krista.extension.authorization.RequestAuthenticator;
import app.krista.extension.executor.Invoker;
import app.krista.extension.impl.anno.*;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.connectors.SalesforceTokenProviderFactory;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.stores.SalesforceMarketingAttributesStore;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Map;

@Field.Text(value = SalesforceMarketingAttributes.CLIENT_ID)
@Field.Text(value = SalesforceMarketingAttributes.CLIENT_SECRET, isSecured = true)
@Field.Text(value = SalesforceMarketingAttributes.INSTANCE_URL, required = false)
@Field.Text(value = SalesforceMarketingAttributes.USER_NAME, required = false)
@Field.Text(value = SalesforceMarketingAttributes.PASSWORD, isSecured = true, required = false)
@Field.Text(value = SalesforceMarketingAttributes.SECURITY_TOKEN, isSecured = true, required = false)
@Extension(version = "2.0.6", name = "Salesforce Marketing", description = "Integrating Salesforce with Krista")
@Java(version = Java.Version.JAVA_21)
@StaticResource(path = "docs", file = "documentation")
public final class SalesforceMarketingExtension {

    private final SalesforceTokenProviderFactory providerFactory;
    private final Invoker invoker;
    private final SalesforceMarketingAttributes salesforceMarketingAttributes;
    private final SalesforceMarketingRequestAuthenticator requestAuthenticator;

    @Inject
    public SalesforceMarketingExtension(SalesforceTokenProviderFactory providerFactory, Invoker invoker, SalesforceMarketingAttributes salesforceMarketingAttributes, SalesforceMarketingAttributesStore attributesStore) {
        this(providerFactory, invoker, salesforceMarketingAttributes, new SalesforceMarketingRequestAuthenticator(salesforceMarketingAttributes, attributesStore));
    }

    public SalesforceMarketingExtension(SalesforceTokenProviderFactory providerFactory, Invoker invoker, SalesforceMarketingAttributes salesforceMarketingAttributes, SalesforceMarketingRequestAuthenticator requestAuthenticator) {
        this.providerFactory = providerFactory;
        this.invoker = invoker;
        this.salesforceMarketingAttributes = salesforceMarketingAttributes;
        this.requestAuthenticator = requestAuthenticator;
    }

    @InvokerRequest(InvokerRequest.Type.AUTHENTICATOR)
    /*
     * This method is used for redirecting the MustAuthenticateException to
     * RequestAuthenticator Interface.
     */
    public RequestAuthenticator getRequestAuthenticator() {
        return requestAuthenticator;
    }

    @InvokerRequest(InvokerRequest.Type.CUSTOM_TABS)
    public Map<String, String> customTabs() {
        return Map.of("Documentation", "static/docs");
    }

    @InvokerRequest(InvokerRequest.Type.VALIDATE_ATTRIBUTES)
    public void validateConnection(Map<String, Object> connectionAttributes) throws IOException {
        SalesforceMarketingAttributes marketingAttributes = SalesforceMarketingAttributes.create(invoker, connectionAttributes);
        attributesValidator(marketingAttributes);
    }

    @InvokerRequest(InvokerRequest.Type.TEST_CONNECTION)
    public void testConnection() throws IOException {
        attributesValidator(salesforceMarketingAttributes);
    }

    private void attributesValidator(SalesforceMarketingAttributes salesforceMarketingAttributes) throws IOException {
        if (salesforceMarketingAttributes.getUserName() == null || salesforceMarketingAttributes.getUserName().isBlank() ||
                salesforceMarketingAttributes.getPassword() == null || salesforceMarketingAttributes.getPassword().isBlank() ||
                salesforceMarketingAttributes.getSecurityToken() == null || salesforceMarketingAttributes.getSecurityToken().isBlank()) {
            providerFactory.create(salesforceMarketingAttributes).getSalesforceToken();
        } else {
            providerFactory.create(salesforceMarketingAttributes).getSalesforceTokenForAdmin();
        }
    }

    @InvokerRequest(InvokerRequest.Type.INVOKER_UPDATED)
    public void attributesUpdated(Map<String, Object> oldAttributes, Map<String, Object> newAttributes) {
        salesforceMarketingAttributes.update(newAttributes);
    }
}
