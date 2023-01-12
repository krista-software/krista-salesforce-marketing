package app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.stores;

import app.krista.extensions.util.KeyValueStore;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;

@Service
public final class RefreshTokenStore {

    private final KeyValueStore keyValueStore;

    @Inject
    public RefreshTokenStore(KeyValueStore keyValueStore) {
        this.keyValueStore = keyValueStore;
    }

    /**
     * This function will add refresh token to the store
     *
     * @param key      Authorization code, client Id, client secret
     * @param refToken refresh token
     */
    public void put(String key, String refToken) {
        keyValueStore.put(key, refToken);
    }

    /**
     * This function will return refresh token for the given key
     *
     * @param key authorization code, client id and client secret
     * @return Refresh token value
     */
    public String get(String key) {
        return keyValueStore.get(key, String.class);
    }
}
