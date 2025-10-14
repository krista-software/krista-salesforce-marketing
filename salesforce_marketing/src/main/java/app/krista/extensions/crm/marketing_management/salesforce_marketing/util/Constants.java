package app.krista.extensions.crm.marketing_management.salesforce_marketing.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class Constants {
    public static final String CALLBACK_URI = "/salesforcemarketing/callback";
    public static final String CODE = "code";
    public static final String STATE = "state";
    public static final String REFRESH_TOKEN = "refresh_token";
    // Error Messages - Following new format: Problem identification + Requirements + Resolution steps
    public static final String ERROR_OCCURRED_DURING_AUTHORIZATION = "OAuth authorization failed during token exchange. The authorization code or state parameter is invalid. Please retry the authorization process by clicking 'Authorize' in the extension configuration.";
    public static final String INVALID_STATE_PARAMETERS = "OAuth state parameter validation failed. The state parameter is missing, malformed, or doesn't match the expected format. This indicates a potential security issue or session timeout. Please restart the authorization process.";
    public static final String FAILED_TO_CREATE_CAMPAIGN = "Campaign creation failed due to invalid or missing required fields. Salesforce requires specific field values and data types for campaign creation. Please verify all required fields are provided with valid values and try again.";
    public static final String FAILED_TO_UPDATE_CAMPAIGN = "Campaign update failed due to invalid field values or insufficient permissions. The campaign may not exist, or the provided field values don't meet Salesforce validation rules. Please verify the campaign ID exists and all field values are valid.";
    public static final String CAMPAIGN = "Campaign";
    public static final String TYPE = "Type";
    public static final String NAME = "Name";
    public static final String STATUS = "Status";
    public static final String LASTMODIFIEDDATE = "LastModifiedDate";
    public static final String DESC = "DESC";
    public static final String PRIMARY_KEY_IS_EMPTY = "Campaign ID is required but not provided. A valid Salesforce Campaign ID (15 or 18 characters) is required to retrieve campaign data. Please provide a valid Campaign ID and try again.";

    public static final String FAILED_TO_SEARCH_FOR_CAMPAIGN = "Campaign search failed due to invalid search criteria or insufficient permissions. The search conditions may be malformed or reference non-existent fields. Please verify your search criteria use valid field names and operators.";
    public static final String FAILED_TO_DELETE_CAMPAIGN = "Campaign deletion failed because the operation is not supported. Campaign deletion is not currently implemented in this extension. Please use Salesforce directly to delete campaigns.";
    public static final String EMPTY_STRING = "";
    public static final String CONDITIONS_NOT_FOUND = "Search conditions are required but not provided. At least one search condition with field name, operator, and value is required to search for campaigns. Please add valid search conditions and try again.";
    public static final String ENTITY_INPUT_IS_NULL_OR_EMPTY = "Campaign data is required but not provided. A campaign object with at least the required fields (Name, Type) must be provided for creation or update operations. Please provide valid campaign data and try again.";
    public static final String REFRESH_TOKEN_IS_NO_LONGER_VALID = "Authentication token has expired or been revoked. The stored refresh token is no longer valid due to password change, token expiration, or manual revocation in Salesforce.";
    public static final String PLEASE_REAUTHORIZE_YOURSELF = "Please re-authorize the extension by clicking 'Authorize' in the configuration settings to obtain a new authentication token.";
    public static final String USER_ID = "userId";
    public static final String AUTH_CONTEXT_ID = "authContextId";
    public static final String TEXT = "Text";
    public static final String START_DATE = "Start Date";
    public static final String END_DATE = "End Date";
    public static final String SALESFORCE_DIDN_T_RETURN_THE_CORRECT_TOKEN_PLEASE_REAUTHORIZE = "Salesforce authentication response is invalid or incomplete. The token response is missing required fields or contains invalid data. Please re-authorize the extension to obtain a valid authentication token.";
    public static final String CAMPAIGN_ID_CAN_T_BE_EMPTY = "Campaign ID is required for this operation. A valid Salesforce Campaign ID (15 or 18 characters starting with '701') is required to update or retrieve campaign data. Please provide a valid Campaign ID.";
    public static final String SELECT = "Select ";
    public static final String FROM_CAMPAIGN = "  from Campaign ";
    public static final String LIMIT = " LIMIT ";
    public static final String OFFSET = " OFFSET ";
    public static final String ORDER_BY = "ORDER BY ";
    public static final String SPACE = " ";
    public static final String SERVICES_DATA_V_55_0_SOBJECTS = "/services/data/v55.0/sobjects/";
    public static final String FAILED_TO_UPDATE_RECORD = "Record update failed due to invalid data or insufficient permissions. The record may not exist, be locked for editing, or the provided field values don't meet Salesforce validation rules. Please verify the record ID exists, you have edit permissions, and all field values are valid.";
    public static final String FAILED_TO_DELETE_RECORD = "Record deletion failed due to insufficient permissions or record dependencies. The record may be referenced by other records or your user profile lacks delete permissions. Please check for dependencies and verify you have delete permissions for this record type.";
    public static final String SERVICES_DATA_V_55_0_QUERY_Q = "/services/data/v55.0/query?q=";
    public static final String FAILED_TO_CREATE_RECORD = "Record creation failed due to missing required fields or invalid data. Salesforce requires specific field values and data types for record creation. Please verify all required fields are provided with valid values according to the object's field definitions.";
    public static final String FAILED_TO_GET_RECORD = "Record retrieval failed due to invalid ID or insufficient permissions. The record may not exist or you may lack read permissions for this record type. Please verify the record ID is correct and you have appropriate access permissions.";
    public static final String AUTHORIZATION_PROMPT = "Extension authorization is required before configuration can be saved. Please click 'Authorize' to authenticate with Salesforce, then click 'Validate Attributes' to verify the connection before saving your configuration.";

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private Constants() {
    }
}
