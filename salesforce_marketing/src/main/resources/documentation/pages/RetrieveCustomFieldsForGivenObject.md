# Retrieve Custom Fields For Given Object

## Overview

Retrieves custom field values from a specified Salesforce marketing object using the object's ID and a list of custom field names. This request enables access to organization-specific custom data stored in Salesforce marketing objects.

## Request Details

- **Area**: Content
- **Type**: QUERY_SYSTEM
- **Retry Support**: ✅ Yes - Failed requests are automatically retried with exponential backoff

## Input Parameters

| Parameter Name | Type | Required | Description | Example |
|----------------|------|----------|-------------|---------|
| Object Id | Text | Yes | Unique identifier of the Salesforce object | "0013000000AbCdEf" |
| Object Type | Pick List | Yes | Type of Salesforce object to query | "Campaign" |
| Custom Fields | List<Text> | Yes | List of custom field API names to retrieve | ["Competition__c", "Budget_Category__c"] |

### Parameter Details

#### Object Id
- **Format**: 15 or 18-character Salesforce ID
- **Validation**: Must be a valid Salesforce record ID
- **Example**: "0013000000AbCdEf" (15-char) or "0013000000AbCdEfGH" (18-char)

#### Object Type
- **Supported Values**: Currently supports "Campaign" only
- **Future Support**: Additional marketing objects may be added
- **Case Sensitive**: Yes, must match exactly

#### Custom Fields
- **Format**: Each field name must end with "__c" (double underscore + c)
- **Validation**: System validates that field names follow Salesforce custom field naming convention
- **Multiple Fields**: Supports retrieving multiple custom fields in a single request
- **Field Types**: Supports all Salesforce custom field types (Text, Number, Date, Picklist, etc.)

## Output Parameters

| Parameter Name | Type | Description |
|----------------|------|-------------|
| Field Details | FreeForm | Dynamic object containing custom field names and their values |

### Output Structure

The response returns a FreeForm object with custom field names as keys and their values:

```json
{
  "Competition__c": "Q4 Holiday Campaign",
  "Budget_Category__c": "Digital Marketing",
  "Target_Audience__c": "Premium Customers"
}
```

## Validation Rules

| Validation | Error Message | Resolution |
|------------|---------------|------------|
| Object Id is empty | "Object Id is required" | Provide a valid Salesforce record ID |
| Object Type is empty | "Object Type is required" | Select "Campaign" from the dropdown |
| Custom Fields list is empty | "Custom Fields list cannot be empty" | Add at least one custom field name |
| Invalid custom field format | "Field name must end with __c" | Ensure field names follow Salesforce custom field convention |
| Object not found | "Record not found for given Object Id" | Verify the Object Id exists and is accessible |
| Field not found | "Custom field does not exist on object" | Verify custom field exists on the specified object type |

## Error Handling

### Input Errors (INPUT_ERROR)
**Cause**: Invalid or missing required parameters  
**Common Scenarios**:
- Empty Object Id or Object Type
- Custom field names not ending with "__c"
- Invalid Salesforce ID format

**Resolution**: Validate all input parameters and ensure proper formatting

### Logic Errors (LOGIC_ERROR)
**Cause**: Business logic validation failures  
**Common Scenarios**:
- Object Id exists but record is not accessible
- Custom fields don't exist on the specified object
- Insufficient permissions to access the object or fields

**Resolution**: Verify record accessibility and field existence

### System Errors (SYSTEM_ERROR)
**Cause**: Salesforce API or connectivity issues  
**Common Scenarios**:
- Salesforce API rate limits exceeded
- Network connectivity problems
- Authentication token expired

**Resolution**: Check Salesforce connection and retry after a brief delay

### Authorization Errors
**Cause**: Insufficient permissions or authentication failures  
**Common Scenarios**:
- User lacks read access to the object
- Custom field permissions not granted
- OAuth token expired or revoked

**Resolution**: Verify user permissions and re-authenticate if necessary

## Usage Examples

### Example 1: Retrieve Campaign Custom Fields

**Input**:
```
Object Id: "7013000000XyZ123"
Object Type: "Campaign"
Custom Fields: ["Competition__c", "Budget_Category__c", "ROI_Target__c"]
```

**Output**:
```json
{
  "Competition__c": "Black Friday 2024",
  "Budget_Category__c": "Seasonal Promotions",
  "ROI_Target__c": 250.5
}
```

**Result**: Successfully retrieved three custom field values from the specified Campaign record.

### Example 2: Single Custom Field Retrieval

**Input**:
```
Object Id: "7013000000AbC456"
Object Type: "Campaign"
Custom Fields: ["Target_Demographics__c"]
```

**Output**:
```json
{
  "Target_Demographics__c": "Ages 25-45, Urban, High Income"
}
```

**Result**: Retrieved a single custom field containing demographic targeting information.

### Example 3: Retry Scenario

**Input**:
```
Object Id: "7013000000DeF789"
Object Type: "Campaign"
Custom Fields: ["Campaign_Score__c", "Effectiveness__c"]
```

**Initial Response**: SYSTEM_ERROR (API rate limit)  
**Retry Response**: Success after 2-second delay  
**Final Output**:
```json
{
  "Campaign_Score__c": 87.5,
  "Effectiveness__c": "High"
}
```

## Business Rules

1. **Custom Field Validation**: All field names must end with "__c" to be considered valid custom fields
2. **Object Type Restriction**: Currently limited to "Campaign" objects only
3. **Field Accessibility**: Only returns fields that the authenticated user has permission to read
4. **Empty Response**: Returns empty FreeForm object if no valid custom fields are found
5. **Data Type Preservation**: Field values maintain their original Salesforce data types in the response

## Limitations

1. **Object Type Support**: Currently supports Campaign objects only
2. **Field Name Convention**: Only retrieves fields following Salesforce custom field naming (ending with "__c")
3. **Permission Dependent**: Results limited by user's field-level security permissions
4. **API Rate Limits**: Subject to Salesforce API rate limiting policies
5. **Record Accessibility**: Can only access records visible to the authenticated user

## Best Practices

### 1. Efficient Field Selection
Only request custom fields that are actually needed to minimize API response size and improve performance.

### 2. Error Handling
Always implement proper error handling to gracefully manage scenarios where records or fields are not accessible.

### 3. Permission Verification
Ensure the integration user has appropriate read permissions for both the object and custom fields before making requests.

### 4. Field Name Validation
Validate custom field names client-side before sending requests to avoid unnecessary API calls.

## Common Use Cases

### 1. Campaign Analysis Dashboard
```
Scenario: Marketing team needs custom campaign metrics for reporting
Action: Retrieve custom fields like "ROI_Target__c", "Actual_ROI__c", "Campaign_Score__c"
Result: Dashboard displays custom KPIs alongside standard campaign data
```

### 2. Campaign Segmentation
```
Scenario: Need to categorize campaigns based on custom attributes
Action: Retrieve fields like "Target_Audience__c", "Channel_Mix__c", "Budget_Category__c"
Result: Campaigns are automatically segmented for analysis and reporting
```

### 3. Compliance Reporting
```
Scenario: Generate compliance reports requiring custom tracking fields
Action: Retrieve fields like "Compliance_Status__c", "Review_Date__c", "Approval_Level__c"
Result: Automated compliance reporting with custom organizational requirements
```

## Related Catalog Requests

Currently, this is the only catalog request available in the Salesforce Marketing Extension. Future releases may include:
- Create Campaign with Custom Fields
- Update Custom Field Values
- Search Campaigns by Custom Criteria

## Technical Implementation

### Helper Class
- **Class**: ContentDaoImpl
- **Package**: app.krista.extensions.crm.marketing_management.salesforce_marketing.impl
- **Validation**: Custom field name format validation (must end with "__c")
- **Service**: SalesforceManager.getRecordByQuery() for data retrieval

### Query Construction
The request builds a SOQL query dynamically:
```sql
SELECT Competition__c, Budget_Category__c FROM Campaign WHERE Id = '7013000000XyZ123'
```

### Response Processing
- Parses JSON response from Salesforce API
- Maps field names to values in FreeForm object
- Handles null values and data type conversion

## Troubleshooting

### Issue: "Field name must end with __c"
**Cause**: Provided field name doesn't follow Salesforce custom field convention  
**Solution**: Ensure all custom field names end with "__c" (double underscore + c)

### Issue: "Record not found for given Object Id"
**Cause**: Object Id doesn't exist or user lacks access  
**Solution**: Verify the record exists and user has read permissions

### Issue: Empty response despite valid inputs
**Cause**: Custom fields don't exist on the object or user lacks field permissions  
**Solution**: Verify custom fields exist and user has field-level read access

## See Also

- [Extension Configuration](pages/ExtensionConfiguration.md) - Setup and connection configuration
- [Authentication](pages/Authentication.md) - OAuth setup and troubleshooting
- [Supported Requests](pages/SupportedRequests.md) - Complete functionality overview
