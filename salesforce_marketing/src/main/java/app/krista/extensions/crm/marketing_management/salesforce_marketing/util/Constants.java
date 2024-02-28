package app.krista.extensions.crm.marketing_management.salesforce_marketing.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class Constants {
    public static final String CALLBACK_URI = "/salesforcemarketing/callback";
    public static final String CODE = "code";
    public static final String STATE = "state";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String ERROR_OCCURRED_DURING_AUTHORIZATION = "Error occurred during authorization ";
    public static final String INVALID_STATE_PARAMETERS = "Invalid state parameters!";
    public static final String FAILED_TO_CREATE_CAMPAIGN = "Failed to create Campaign.";
    public static final String FAILED_TO_UPDATE_CAMPAIGN = "Failed to update Campaign. ";
    public static final String CAMPAIGN = "Campaign";
    public static final String TYPE = "Type";
    public static final String NAME = "Name";
    public static final String STATUS = "Status";
    public static final String LASTMODIFIEDDATE = "LastModifiedDate";
    public static final String DESC = "DESC";
    public static final String PRIMARY_KEY_IS_EMPTY = "Primary key is empty";

    public static final String FAILED_TO_SEARCH_FOR_CAMPAIGN = "Failed to search for Campaign. ";
    public static final String FAILED_TO_DELETE_CAMPAIGN = "Failed to delete campaign. ";
    public static final String EMPTY_STRING = "";
    public static final String CONDITIONS_NOT_FOUND = "Conditions not found.";
    public static final String ENTITY_INPUT_IS_NULL_OR_EMPTY = "Entity input is null or empty.";
    public static final String REFRESH_TOKEN_IS_NO_LONGER_VALID = "Refresh token is no longer valid. ";
    public static final String PLEASE_REAUTHORIZE_YOURSELF = "Please reauthorize yourself";
    public static final String USER_ID = "userId";
    public static final String AUTH_CONTEXT_ID = "authContextId";
    public static final String TEXT = "Text";
    public static final String START_DATE = "Start Date";
    public static final String END_DATE = "End Date";
    public static final String SALESFORCE_DIDN_T_RETURN_THE_CORRECT_TOKEN_PLEASE_REAUTHORIZE = "Salesforce didn't return the correct token. Please Reauthorize.";
    public static final String CAMPAIGN_ID_CAN_T_BE_EMPTY = "Campaign Id can't be empty";
    public static final String SELECT = "Select ";
    public static final String FROM_CAMPAIGN = "  from Campaign ";
    public static final String LIMIT = " LIMIT ";
    public static final String OFFSET = " OFFSET ";
    public static final String ORDER_BY = "ORDER BY ";
    public static final String SPACE = " ";
    public static final String SERVICES_DATA_V_55_0_SOBJECTS = "/services/data/v55.0/sobjects/";
    public static final String FAILED_TO_UPDATE_RECORD = "Failed to update record ";
    public static final String FAILED_TO_DELETE_RECORD = "Failed to delete record ";
    public static final String SERVICES_DATA_V_55_0_QUERY_Q = "/services/data/v55.0/query?q=";
    public static final String FAILED_TO_CREATE_RECORD = "Failed to create record ";
    public static final String FAILED_TO_GET_RECORD = "Failed to get record ";
    public static final String AUTHORIZATION_PROMPT = "Authorize yourself and click 'Validate Attributes' before saving changes to proceed further";

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private Constants() {
    }
}
