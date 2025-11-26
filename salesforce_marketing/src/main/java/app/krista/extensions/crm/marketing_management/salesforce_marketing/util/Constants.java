package app.krista.extensions.crm.marketing_management.salesforce_marketing.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class Constants {
    public static final String CALLBACK_URI = "/salesforcemarketing/callback";
    public static final String CODE = "code";
    public static final String STATE = "state";
    public static final String REFRESH_TOKEN = "refresh_token";
    // ========================================
    // ERROR MESSAGES - User-Friendly & Actionable
    // Format: Clear problem + Why it happened + What to do next
    // ========================================

    // Authentication & Authorization Errors
    public static final String ERROR_OCCURRED_DURING_AUTHORIZATION =
        "We couldn't complete the authorization process. " +
        "This usually happens when the authorization code expired or the connection was interrupted. " +
        "What to do: Click the 'Authorize' button in your extension settings and complete the Salesforce login again.";

    public static final String INVALID_STATE_PARAMETERS =
        "The authorization session has expired or is invalid. " +
        "This is a security measure to protect your account. " +
        "What to do: Please start the authorization process again from the beginning.";

    public static final String REFRESH_TOKEN_IS_NO_LONGER_VALID =
        "Your Salesforce connection has expired. " +
        "This can happen if you changed your Salesforce password, revoked access, or the token expired. " +
        "What to do: ";

    public static final String PLEASE_REAUTHORIZE_YOURSELF =
        "Click 'Authorize' in the extension settings and log in to Salesforce again to restore the connection.";

    public static final String SALESFORCE_DIDN_T_RETURN_THE_CORRECT_TOKEN_PLEASE_REAUTHORIZE =
        "Salesforce didn't provide the expected authentication information. " +
        "This might be a temporary issue with Salesforce's authentication service. " +
        "What to do: Wait a moment and try authorizing again. If the problem persists, check Salesforce's service status.";

    public static final String AUTHORIZATION_PROMPT =
        "Before saving, you need to authorize this extension with Salesforce. " +
        "What to do: 1) Click 'Authorize' and log in to Salesforce, 2) Click 'Validate Attributes' to test the connection, 3) Save your configuration.";

    // Campaign Operation Errors
    public static final String FAILED_TO_CREATE_CAMPAIGN =
        "We couldn't create the campaign in Salesforce. " +
        "This usually means required information is missing or doesn't match Salesforce's requirements. " +
        "What to do: Check that you've provided the Campaign Name and all required fields with valid values, then try again.";

    public static final String FAILED_TO_UPDATE_CAMPAIGN =
        "We couldn't update the campaign in Salesforce. " +
        "The campaign might not exist, or the information you provided doesn't meet Salesforce's validation rules. " +
        "What to do: Verify the Campaign ID is correct and all field values are valid for this campaign type.";

    public static final String FAILED_TO_SEARCH_FOR_CAMPAIGN =
        "We couldn't search for campaigns with the criteria you provided. " +
        "Your search might reference fields that don't exist or you may not have permission to search campaigns. " +
        "What to do: Check that your search uses valid field names (Name, Status, or Type) and that you have read access to campaigns.";

    public static final String FAILED_TO_DELETE_CAMPAIGN =
        "Campaign deletion is not available in this extension. " +
        "This feature requires additional safety checks that aren't implemented yet. " +
        "What to do: Log in to Salesforce directly to delete campaigns, or contact your administrator for assistance.";

    // Data Validation Errors
    public static final String PRIMARY_KEY_IS_EMPTY =
        "A Campaign ID is required to find the campaign. " +
        "Campaign IDs are 15 or 18 characters long and start with '701'. " +
        "What to do: Provide a valid Campaign ID from Salesforce and try again.";

    public static final String CAMPAIGN_ID_CAN_T_BE_EMPTY =
        "A Campaign ID is required for this action. " +
        "Campaign IDs are 15 or 18 characters long and start with '701'. " +
        "What to do: Make sure you've provided a valid Campaign ID before updating or retrieving campaign data.";

    public static final String CONDITIONS_NOT_FOUND =
        "No search criteria were provided. " +
        "To search for campaigns, you need to specify what you're looking for. " +
        "What to do: Add at least one search condition with a field name (like 'Name' or 'Status'), an operator, and a value.";

    public static final String ENTITY_INPUT_IS_NULL_OR_EMPTY =
        "No campaign information was provided. " +
        "To create or update a campaign, you need to provide campaign details. " +
        "What to do: Include at least the Campaign Name and any other required fields, then try again.";

    // Record Operation Errors (Generic)
    public static final String FAILED_TO_CREATE_RECORD =
        "We couldn't create the record in Salesforce. " +
        "Required information might be missing or doesn't match Salesforce's field requirements. " +
        "What to do: Check that all required fields are filled in with the correct data types and try again.";

    public static final String FAILED_TO_UPDATE_RECORD =
        "We couldn't update the record in Salesforce. " +
        "The record might not exist, be locked by another user, or the values don't meet validation rules. " +
        "What to do: Verify the record ID is correct, check that you have edit permission, and ensure all values are valid.";

    public static final String FAILED_TO_DELETE_RECORD =
        "We couldn't delete the record from Salesforce. " +
        "The record might be referenced by other records, or you may not have delete permission. " +
        "What to do: Check if other records depend on this one, and verify you have delete permission for this record type.";

    public static final String FAILED_TO_GET_RECORD =
        "We couldn't find or access the record in Salesforce. " +
        "The record might not exist, or you may not have permission to view it. " +
        "What to do: Double-check the record ID is correct and that you have read access to this type of record.";

    // Field and Constant Names
    public static final String CAMPAIGN = "Campaign";
    public static final String TYPE = "Type";
    public static final String NAME = "Name";
    public static final String STATUS = "Status";
    public static final String LASTMODIFIEDDATE = "LastModifiedDate";
    public static final String DESC = "DESC";
    public static final String EMPTY_STRING = "";
    public static final String USER_ID = "userId";
    public static final String AUTH_CONTEXT_ID = "authContextId";
    public static final String TEXT = "Text";
    public static final String START_DATE = "Start Date";
    public static final String END_DATE = "End Date";
    public static final String SELECT = "Select ";
    public static final String FROM_CAMPAIGN = "  from Campaign ";
    public static final String LIMIT = " LIMIT ";
    public static final String OFFSET = " OFFSET ";
    public static final String ORDER_BY = "ORDER BY ";
    public static final String SPACE = " ";

    // API Endpoints
    public static final String SERVICES_DATA_V_55_0_SOBJECTS = "/services/data/v55.0/sobjects/";
    public static final String SERVICES_DATA_V_55_0_QUERY_Q = "/services/data/v55.0/query?q=";

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private Constants() {
    }
}
