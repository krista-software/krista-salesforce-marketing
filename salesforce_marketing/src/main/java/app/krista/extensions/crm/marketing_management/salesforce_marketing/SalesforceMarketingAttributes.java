package app.krista.extensions.crm.marketing_management.salesforce_marketing;

import app.krista.extension.executor.Invoker;
import app.krista.extension.request.RoutingInfo;
import app.krista.extension.request.protos.http.HttpProtocol;
import app.krista.extension.util.InvokerAttributes;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.api.SalesforceCustomApi;
import com.github.scribejava.apis.SalesforceApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;


/**
 * Configuration attributes for Salesforce Marketing Extension.
 * This class manages authentication credentials and configuration for connecting to Salesforce
 * using various authentication flows including OAuth 2.0 Client Credentials, Authorization Code,
 * and Password Grant flows.
 *
 * @author Krista Extensions Team
 * @version 2.0.5
 * @since 1.0.0
 */
@Service
public final class SalesforceMarketingAttributes {
    private static final Logger LOGGER = LoggerFactory.getLogger(SalesforceMarketingAttributes.class);

    public static final String CLIENT_ID = "clientId";
    public static final String CLIENT_SECRET = "clientSecret";
    public static final String INSTANCE_URL = "Instance URL";
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";
    public static final String SECURITY_TOKEN = "securityToken";

    private String clientId;
    private String clientSecret;
    private String instanceUrl;
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
                (String) attributes.get(INSTANCE_URL),
                (String) attributes.get(USER_NAME),
                (String) attributes.get(PASSWORD),
                (String) attributes.get(SECURITY_TOKEN),
                routingURL);
    }

    /**
     * Constructs SalesforceMarketingAttributes with all authentication parameters.
     *
     * @param clientId The OAuth 2.0 client ID
     * @param clientSecret The OAuth 2.0 client secret
     * @param instanceUrl The Salesforce instance URL (optional, used for Client Credentials flow)
     * @param userName The Salesforce username (optional, used for Password Grant flow)
     * @param password The Salesforce password (optional, used for Password Grant flow)
     * @param securityToken The Salesforce security token (optional, used for Password Grant flow)
     * @param routingUrl The routing URL for OAuth callbacks
     * @throws IllegalArgumentException if instanceUrl is provided but not a valid URL format
     */
    public SalesforceMarketingAttributes(String clientId, String clientSecret, String instanceUrl, String userName, String password, String securityToken, String routingUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.instanceUrl = validateAndSetInstanceUrl(instanceUrl);
        this.userName = userName;
        this.password = password;
        this.securityToken = securityToken;
        this.routingUrl = routingUrl;

        // Log the authentication method that will be used
        logAuthenticationMethod();
    }

    /**
     * Validates the instance URL format if provided and checks for consistency.
     *
     * @param instanceUrl The instance URL to validate
     * @return The validated instance URL or null if not provided
     * @throws IllegalArgumentException if the URL format is invalid
     */
    private String validateAndSetInstanceUrl(String instanceUrl) {
        if (instanceUrl != null && !instanceUrl.trim().isEmpty()) {
            try {
                URI uri = new URI(instanceUrl.trim());

                // Validate that the URI is absolute (has a scheme)
                if (!uri.isAbsolute()) {
                    throw new MalformedURLException("URI must be absolute with a scheme (e.g., https://)");
                }

                URL url = uri.toURL();
                String validatedUrl = instanceUrl.trim();
                String host = url.getHost();

                if (host == null || host.isEmpty()) {
                    throw new MalformedURLException("URL must have a valid host");
                }

                String hostLower = host.toLowerCase();

                // Validate that it's a Salesforce domain
                if (!hostLower.contains("salesforce.com") && !hostLower.contains("force.com")) {
                    LOGGER.warn("Instance URL does not appear to be a Salesforce domain: {}", host);
                }

                // Check for sandbox consistency
                validateSandboxConsistency(hostLower, validatedUrl);

                return validatedUrl;
            } catch (URISyntaxException | MalformedURLException e) {
                throw new IllegalArgumentException(
                    "The Instance URL you provided isn't in the correct format. " +
                    "What to do: Make sure it looks like 'https://yourinstance.salesforce.com' or 'https://yourinstance.my.salesforce.com'. " +
                    "You provided: " + instanceUrl, e);
            }
        }
        return instanceUrl;
    }

    /**
     * Validates that the instance URL is consistent with sandbox expectations.
     * Provides warnings for potential configuration mismatches.
     *
     * @param host The hostname from the instance URL
     * @param instanceUrl The full instance URL for logging
     */
    private void validateSandboxConsistency(String host, String instanceUrl) {
        boolean isSandboxUrl = host.contains("sandbox") || host.contains("--") || host.contains("develop");

        if (isSandboxUrl) {
            LOGGER.debug("Detected sandbox instance URL: {}", instanceUrl);
            // Additional sandbox-specific validations could be added here
            if (host.contains("--") && !host.contains("sandbox")) {
                LOGGER.warn("URL appears to be a sandbox (contains '--') but doesn't contain 'sandbox': {}", instanceUrl);
            }
        } else if (host.contains("my.salesforce.com")) {
            LOGGER.debug("Detected production instance URL: {}", instanceUrl);
        } else if (host.contains("develop") || host.contains("-dev-ed")) {
            LOGGER.debug("Detected developer edition instance URL: {}", instanceUrl);
        }

        // Validate HTTPS usage for production-like environments
        if (!instanceUrl.startsWith("https://") && !host.contains("localhost") && !host.contains("127.0.0.1")) {
            LOGGER.warn("Instance URL should use HTTPS for security: {}", instanceUrl);
        }
    }

    /**
     * Logs the authentication method that will be used based on current configuration.
     */
    private void logAuthenticationMethod() {
        if (instanceUrl != null && !instanceUrl.trim().isEmpty()) {
            LOGGER.debug("Configured for OAuth 2.0 Client Credentials flow with Instance URL: {}", instanceUrl);
        } else if (userName != null && !userName.trim().isEmpty()) {
            LOGGER.debug("Configured for OAuth 2.0 Password Grant flow with username: {}", userName);
        } else {
            LOGGER.debug("Configured for OAuth 2.0 Authorization Code flow (requires user authorization)");
        }
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

    public String getInstanceUrl() {
        return instanceUrl;
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

    public synchronized OAuth20Service getOauthService(String domain) {
        return new ServiceBuilder(getClientId())
                .apiSecret(getClientSecret())
                .build(new SalesforceCustomApi(domain));
    }

    /**
     * Updates the attributes with new values from the provided map.
     * Validates the instance URL if provided.
     *
     * @param newAttributes Map containing the new attribute values
     * @throws IllegalArgumentException if the new instance URL format is invalid
     */
    public void update(Map<String, Object> newAttributes) {
        clientId = InvokerAttributes.getStringOrNull(newAttributes, CLIENT_ID);
        clientSecret = InvokerAttributes.getStringOrNull(newAttributes, CLIENT_SECRET);
        instanceUrl = validateAndSetInstanceUrl(InvokerAttributes.getStringOrNull(newAttributes, INSTANCE_URL));
        userName = InvokerAttributes.getStringOrNull(newAttributes, USER_NAME);
        password = InvokerAttributes.getStringOrNull(newAttributes, PASSWORD);
        securityToken = InvokerAttributes.getStringOrNull(newAttributes, SECURITY_TOKEN);

        // Clear cached OAuth service since configuration changed
        oAuthService = null;

        // Log the new authentication method
        logAuthenticationMethod();
    }

}
