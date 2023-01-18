package app.krista.extensions.crm.marketing_management.salesforce_marketing;

import app.krista.extension.executor.Invoker;
import app.krista.extension.request.RoutingInfo;
import app.krista.extension.request.protos.http.HttpProtocol;
import app.krista.extension.util.InvokerAttributes;
import com.github.scribejava.apis.SalesforceApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.Map;


@Service
public final class SalesforceMarketingAttributes {
    public static final String CLIENT_ID = "clientId";
    public static final String CLIENT_SECRET = "clientSecret";
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";
    public static final String SECURITY_TOKEN = "securityToken";
    private String clientId;
    private String clientSecret;
    private String userName;
    private String password;
    private String securityToken;
    private final String routingUrl;
    private OAuth20Service oAuthService;

    @Inject
    public SalesforceMarketingAttributes(Invoker invoker) {
        this(invoker.getRoutingInfo().getRoutingURL(HttpProtocol.PROTOCOL_NAME, RoutingInfo.Type.APPLIANCE),
                invoker.getAttributes());
    }

    public SalesforceMarketingAttributes(String routingURL, Map<String, Object> attributes) {
        this((String) attributes.get(CLIENT_ID),
                (String) attributes.get(CLIENT_SECRET),
                (String) attributes.get(USER_NAME),
                (String) attributes.get(PASSWORD),
                (String) attributes.get(SECURITY_TOKEN),
                routingURL);
    }

    public SalesforceMarketingAttributes(String clientId, String clientSecret, String userName, String password, String securityToken, String routingUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.userName = userName;
        this.password = password;
        this.securityToken = securityToken;
        this.routingUrl = routingUrl;
    }

    public static SalesforceMarketingAttributes create(Invoker invoker, Map<String, Object> attributes) {
        return new SalesforceMarketingAttributes(invoker.getRoutingInfo().getRoutingURL(HttpProtocol.PROTOCOL_NAME, RoutingInfo.Type.APPLIANCE), attributes);
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    private String getCallbackUrl() {

        return (routingUrl.contains("extension.local.eng.krista.app") ? routingUrl.replace("extension.local.eng.krista.app", "localhost:8765") : routingUrl) + "/rest/salesforcemarketing/callback";
    }

    public synchronized OAuth20Service getOauthService() {
        if (oAuthService == null) {
            oAuthService = new ServiceBuilder(getClientId())
                    .apiSecret(getClientSecret())
                    .callback(getCallbackUrl())
                    .build(SalesforceApi.instance());
        }
        return oAuthService;
    }

    public void update(Map<String, Object> newAttributes) {
        clientId = InvokerAttributes.getStringOrNull(newAttributes, CLIENT_ID);
        clientSecret = InvokerAttributes.getStringOrNull(newAttributes, CLIENT_SECRET);
        userName = InvokerAttributes.getStringOrNull(newAttributes, USER_NAME);
        password = InvokerAttributes.getStringOrNull(newAttributes, PASSWORD);
        securityToken = InvokerAttributes.getStringOrNull(newAttributes, SECURITY_TOKEN);
    }

}
