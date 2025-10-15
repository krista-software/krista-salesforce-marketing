package app.krista.extensions.crm.marketing_management.salesforce_marketing.impl;

import app.krista.extensions.crm.marketing_management.salesforce_marketing.SalesforceMarketingAttributes;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.connectors.SalesforceTokenProvider;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.connectors.SalesforceTokenProviderFactory;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.util.Constants;
import com.github.scribejava.apis.salesforce.SalesforceToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
public final class SalesforceManager {

    private final SalesforceMarketingAttributes salesforceMarketingAttributes;
    private final SalesforceTokenProvider tokenProvider;
    private static final Logger LOGGER = LoggerFactory.getLogger(SalesforceManager.class);


    @Inject
    public SalesforceManager(SalesforceTokenProviderFactory providerFactory, SalesforceMarketingAttributes salesforceMarketingAttributes) {
        this(providerFactory.create(), salesforceMarketingAttributes);
    }

    public SalesforceManager(SalesforceTokenProvider tokenProvider, SalesforceMarketingAttributes salesforceMarketingAttributes) {
        this.tokenProvider = tokenProvider;
        this.salesforceMarketingAttributes = salesforceMarketingAttributes;
    }

    /**
     * This function will get the changed fields for updating a campaign
     *
     * @param old    old campaign object values
     * @param newObj new campaign object values
     * @return {@link Map} of objects with changed fields in the campaign
     */
    public static Map<String, Object> getDifference(Object old, Object newObj) {
        try {
            Map<String, Object> values = new HashMap<>();
            Class<?> clazz = old.getClass();

            // Process all fields including inherited ones
            while (clazz != null && clazz != Object.class) {
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    Object value1 = field.get(old);
                    Object value2 = field.get(newObj);

                    // Check if field name contains "id" (case-insensitive)
                    String fieldNameLower = field.getName().toLowerCase();
                    boolean isIdField = fieldNameLower.contains("id");

                    if (value1 != null && value2 != null && !Objects.equals(value1, value2) && !isIdField) {
                        values.put(field.getName(), value2);
                    }
                }
                clazz = clazz.getSuperclass();
            }

            return values;
        } catch (IllegalAccessException cause) {
            throw new IllegalArgumentException(cause.getMessage());
        }
    }

    /**
     * This function will make api call with the changed attribute fields
     *
     * @param id      Campaign Id
     * @param body    Changed values in the campaign
     * @param sobject String CAMPAIGN
     * @return response body of the api call
     */
    @SuppressWarnings("UnusedReturnValue")
    public String updateRecord(String id, String body, String sobject) {
        try {
            SalesforceToken salesforceToken = tokenProvider.getSalesforceToken();
            String url = salesforceToken.getInstanceUrl() + Constants.SERVICES_DATA_V_55_0_SOBJECTS + sobject + "/" + id + "?_HttpMethod=PATCH";
            final OAuthRequest request = getOAuthRequest(Verb.POST, body, salesforceToken, url);
            return getRequestResponse(salesforceToken, request);
        } catch (IOException cause) {
            throw new IllegalArgumentException(Constants.FAILED_TO_UPDATE_RECORD + cause.getCause());
        }
    }

    /**
     * This function will make api call to delete the salesforce record
     *
     * @param id      Campaign Id
     * @param sobject String CAMPAIGN
     * @return response body of the api call
     */
    @SuppressWarnings("UnusedReturnValue")
    public String deleteRecord(String id, String sobject) {
        try {
            SalesforceToken salesforceToken = tokenProvider.getSalesforceToken();
            String url = salesforceToken.getInstanceUrl() + Constants.SERVICES_DATA_V_55_0_SOBJECTS + sobject + "/" + id + "?_HttpMethod=DELETE";
            final OAuthRequest request = getOAuthRequest(Verb.DELETE, null, salesforceToken, url);
            return getRequestResponse(salesforceToken, request);
        } catch (IOException cause) {
            throw new IllegalArgumentException(Constants.FAILED_TO_DELETE_RECORD + cause.getCause());
        }
    }

    /**
     * This function will return the salesforce record by query
     *
     * @param query query to retrieve salesforce records
     * @return response body of api call
     */
    public String getRecordByQuery(String query) {
        try {
            SalesforceToken salesforceToken = tokenProvider.getSalesforceToken();
            String url = salesforceToken.getInstanceUrl() + Constants.SERVICES_DATA_V_55_0_QUERY_Q + URLEncoder.encode(query, StandardCharsets.UTF_8);
            final OAuthRequest request = getOAuthRequest(Verb.GET, null, salesforceToken, url);
            return getRequestResponse(salesforceToken, request);
        } catch (IOException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }
    }

    /**
     * This function will create a record in the salesforce
     *
     * @param body    values of the fields in the campaign
     * @param sobject String CAMPAIGN
     * @return response body of api call
     */
    public String createRecord(String body, String sobject) {
        try {
            SalesforceToken salesforceToken = tokenProvider.getSalesforceToken();
            String url = salesforceToken.getInstanceUrl() + Constants.SERVICES_DATA_V_55_0_SOBJECTS + sobject + "/";
            final OAuthRequest request = getOAuthRequest(Verb.POST, body, salesforceToken, url);
            return getRequestResponse(salesforceToken, request);
        } catch (IOException cause) {
            throw new IllegalArgumentException(Constants.FAILED_TO_CREATE_RECORD + cause.getMessage());
        }
    }

    /**
     * This function will get the response of the salesforce api call
     *
     * @param salesforceToken {@link SalesforceToken} object
     * @param request         {@link OAuthRequest} object
     * @return Salesforce api call response
     */
    private String getRequestResponse(SalesforceToken salesforceToken, OAuthRequest request) {
        salesforceMarketingAttributes.getOauthService().signRequest(salesforceToken, request);
        try (com.github.scribejava.core.model.Response response = salesforceMarketingAttributes.getOauthService().execute(request)) {
            if (response.getCode() >= 400) {
                String errorDetails = getErrorMessage(response.getBody());
                throw new IllegalArgumentException("Salesforce API request failed with HTTP " + response.getCode() + ". " +
                    "The request contains invalid data or violates Salesforce validation rules. " +
                    "Please verify all field values meet the required format and constraints. " +
                    "Error details: " + (errorDetails != null ? errorDetails : "No additional details available."));
            }
            return response.getBody();
        } catch (IOException | ExecutionException cause) {
            throw new IllegalStateException(Constants.FAILED_TO_GET_RECORD + cause.getMessage());
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(interruptedException.getMessage());
        }
    }

    /**
     * This function will create a new {@link OAuthRequest} for the salesforce api
     *
     * @param verb            type of API call
     * @param body            parameters of the api
     * @param salesforceToken salesforce token for the request
     * @param url             url of the api
     * @return {@link OAuthRequest} object of request to make api call
     */
    private OAuthRequest getOAuthRequest(Verb verb, String body, SalesforceToken salesforceToken, String url) {
        final OAuthRequest request = new OAuthRequest(verb, url);
        if (body != null) {
            request.setPayload(body);
        }
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "Bearer " + salesforceToken.getAccessToken());
        return request;
    }

    private String getErrorMessage(String exceptionMessage) {
        if (exceptionMessage == null || exceptionMessage.isEmpty()) {
            LOGGER.error("Exception message is null or empty");
            return null;
        }

        try {
            JSONArray jsonArray = new JSONArray(exceptionMessage);

            if (jsonArray.length() == 0) {
                LOGGER.error("JSON array is empty");
                return null;
            }

            JSONObject jsonObject = jsonArray.getJSONObject(0);

            if (jsonObject == null) {
                LOGGER.error("First JSON object is null");
                return null;
            }
            if (!jsonObject.has("message")) {
                LOGGER.error("JSON object does not have message");
                return null;
            }

            String message = jsonObject.getString("message");

            if (message == null || message.isEmpty()) {
                LOGGER.error("Message in JSON object is null or empty");
                return null;
            }

            return message;
        } catch (JSONException exception) {
            LOGGER.error("Exception occurred due to : {}", exception.getMessage(), exception);
            return null;
        }
    }

}
