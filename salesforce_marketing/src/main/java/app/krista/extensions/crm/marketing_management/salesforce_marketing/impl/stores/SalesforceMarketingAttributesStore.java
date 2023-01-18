package app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.stores;

import app.krista.extension.executor.Invoker;
import app.krista.extension.request.RoutingInfo;
import app.krista.extension.request.protos.http.HttpProtocol;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.SalesforceMarketingAttributes;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.util.Constants;
import app.krista.extensions.util.KeyValueStore;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
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
        store.put(authContextId, Constants.GSON.toJson(Map.of(SalesforceMarketingAttributes.CLIENT_ID, salesforceMarketingAttributes.getClientId(),
                SalesforceMarketingAttributes.CLIENT_SECRET, salesforceMarketingAttributes.getClientSecret())));
        return authContextId;
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
