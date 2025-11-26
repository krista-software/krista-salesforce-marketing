# Campaign Entity

## Overview

The Campaign entity provides comprehensive access to Salesforce marketing campaigns through Krista's entity management system. This entity supports full CRUD operations and enables automated campaign lifecycle management, performance tracking, and data synchronization.

## Entity Details

- **Entity Name**: Campaign
- **Primary Key**: Id
- **Salesforce Object**: Campaign
- **Store Support**: ✅ Yes - Full entity storage and retrieval capabilities
- **Search Support**: ✅ Yes - Multiple search criteria supported
- **CRUD Operations**: ✅ Create, Read, Update, Delete all supported

## Entity Fields

### Core Campaign Information

| Field Name | Type | Salesforce API Name | Required | Searchable | Description |
|------------|------|-------------------|----------|------------|-------------|
| Name | Text | Name | ✅ Yes | ✅ Yes | Campaign name |
| Id | Text | Id | ❌ No | ❌ No | Unique campaign identifier |
| Type | Text | Type | ❌ No | ✅ Yes | Campaign type (Email, Social, etc.) |
| Status | Text | Status | ❌ No | ✅ Yes | Campaign status (Active, Completed, etc.) |
| Description | Paragraph | Description | ❌ No | ❌ No | Campaign description |

### Campaign Dates

| Field Name | Type | Salesforce API Name | Required | Description |
|------------|------|-------------------|----------|-------------|
| Start Date | Date | StartDate | ❌ No | Campaign start date |
| End Date | Date | EndDate | ❌ No | Campaign end date |
| Created Date | Date | CreatedDate | ❌ No | Record creation timestamp |
| Last Modified Date | Date | LastModifiedDate | ❌ No | Last update timestamp |

### Financial Information

| Field Name | Type | Salesforce API Name | Required | Description |
|------------|------|-------------------|----------|-------------|
| Budgeted Cost | Currency | BudgetedCost | ❌ No | Planned campaign budget |
| Expected Revenue | Currency | ExpectedRevenue | ❌ No | Revenue target |
| Actual Cost | Currency | ActualCost | ❌ No | Actual campaign spend |
| Expected Response | Percentage | ExpectedResponse | ❌ No | Expected response rate |

### Performance Metrics

| Field Name | Type | Salesforce API Name | Required | Description |
|------------|------|-------------------|----------|-------------|
| Number Sent | Number | NumberSent | ❌ No | Total messages/materials sent |
| Number Of Leads | Number | NumberOfLeads | ❌ No | Generated leads |
| Number Of Converted Leads | Number | NumberOfConvertedLeads | ❌ No | Converted leads |
| Number Of Contacts | Number | NumberOfContacts | ❌ No | Associated contacts |
| Number Of Responses | Number | NumberOfResponses | ❌ No | Total responses received |
| Number Of Opportunities | Number | NumberOfOpportunities | ❌ No | Created opportunities |
| Number Of Won Opportunities | Number | NumberOfWonOpportunities | ❌ No | Won opportunities |

### Opportunity Values

| Field Name | Type | Salesforce API Name | Required | Description |
|------------|------|-------------------|----------|-------------|
| Amount All Opportunities | Currency | AmountAllOpportunities | ❌ No | Total opportunity value |
| Amount Won Opportunities | Currency | AmountWonOpportunities | ❌ No | Won opportunity value |

### Relationship Fields

| Field Name | Type | Salesforce API Name | Required | Description |
|------------|------|-------------------|----------|-------------|
| Owner Id | Text | OwnerId | ❌ No | Campaign owner user ID |
| Created By Id | Text | CreatedById | ❌ No | Creator user ID |

## CRUD Operations

### Create Campaign

Create new marketing campaigns with comprehensive field support.

**Required Fields**:
- Name (Campaign name is mandatory)

**Optional Fields**:
- All other fields can be set during creation
- Custom fields (if accessible by user)
- Financial and performance metrics

**Example Creation**:
```json
{
  "name": "Holiday 2024 Email Campaign",
  "type": "Email",
  "status": "Planning",
  "description": "Year-end holiday promotion campaign",
  "startDate": "2024-12-01",
  "endDate": "2024-12-31",
  "budgetedCost": 25000.00,
  "expectedRevenue": 150000.00,
  "expectedResponse": 12.5
}
```

### Read Campaign

Retrieve complete campaign information by ID.

**Input**: Campaign ID (15 or 18 character Salesforce ID)
**Output**: Complete Campaign entity with all accessible fields

**Retrieved Data Includes**:
- All standard campaign fields
- Custom fields (if user has access)
- Performance metrics and financial data
- Audit information (created/modified dates and users)

### Update Campaign

Modify existing campaign information with field-level updates.

**Updatable Fields**:
- Campaign details (name, description, type, status)
- Dates (start date, end date)
- Financial information (costs, revenue, response rates)
- Performance metrics (leads, opportunities, responses)
- Custom fields (if user has edit access)

**Non-Updatable Fields**:
- Id (primary key)
- Created Date, Created By Id (audit fields)
- System-calculated fields

### Delete Campaign

Remove campaigns from Salesforce (subject to org deletion policies).

**Deletion Behavior**:
- Soft delete (moves to Recycle Bin)
- Respects Salesforce deletion restrictions
- Maintains audit trail
- May be restricted by org policies

## Search Operations

### Search Capabilities

The Campaign entity supports flexible search with multiple criteria:

#### By Name
- **Operator**: Contains, Equals
- **Use Case**: Find campaigns by name pattern
- **Example**: `Name CONTAINS "Holiday"` finds all holiday campaigns

#### By Status
- **Operator**: Equals
- **Use Case**: Filter campaigns by current status
- **Example**: `Status = "Active"` finds all active campaigns

#### By Type
- **Operator**: Equals
- **Use Case**: Filter campaigns by campaign type
- **Example**: `Type = "Email"` finds all email campaigns

#### Combined Search
- **Multiple Criteria**: Use AND/OR operators
- **Example**: `Status = "Active" AND Type = "Email"`

### Search Parameters

| Parameter | Description | Example Values |
|-----------|-------------|----------------|
| Search Conditions | Field-based search criteria | Name, Status, Type |
| Page Index | Page number for pagination | 0, 1, 2, ... |
| Page Size | Number of results per page | 10, 25, 50, 100 |
| Sort Field | Field to sort results by | Name, CreatedDate, Status |
| Sort Order | Ascending or descending | ASC, DESC |

### Search Examples

#### Example 1: Find Active Email Campaigns
```
Search Criteria: Status = "Active" AND Type = "Email"
Page Size: 10
Sort: Name ASC
Result: List of active email campaigns ordered by name
```

#### Example 2: Find Recent Campaigns
```
Search Criteria: CreatedDate >= "2024-01-01"
Page Size: 25
Sort: CreatedDate DESC
Result: Campaigns created this year, newest first
```

#### Example 3: Find High-Budget Campaigns
```
Search Criteria: BudgetedCost >= 50000
Page Size: 20
Sort: BudgetedCost DESC
Result: Campaigns with budget over $50K, highest first
```

## Entity Relationships

### Parent Relationships
- **Owner**: User who owns the campaign
- **Created By**: User who created the campaign

### Child Relationships
- **Campaign Members**: Leads and Contacts associated with campaign
- **Campaign Opportunities**: Opportunities influenced by campaign
- **Campaign Activities**: Tasks and events related to campaign

### Custom Relationships
- Organization-specific custom lookup fields
- Custom junction objects for many-to-many relationships

## Business Rules

### Validation Rules
1. **Name Required**: Campaign name cannot be empty
2. **Date Logic**: End date must be after start date (if both provided)
3. **Financial Validation**: Costs and revenue must be non-negative
4. **Status Transitions**: Some status changes may be restricted by org rules
5. **Type Constraints**: Campaign type must match org-defined picklist values

### Field Dependencies
1. **Performance Metrics**: Some metrics auto-calculate based on related records
2. **Financial Rollups**: Opportunity amounts may auto-update from related opportunities
3. **Status Impact**: Status changes may trigger workflow rules or process builder

### Security Rules
1. **Field-Level Security**: User must have read/write access to fields
2. **Object Permissions**: User must have appropriate Campaign object permissions
3. **Sharing Rules**: Campaign visibility subject to org sharing settings
4. **Custom Permissions**: May require additional custom permissions

## Performance Considerations

### Search Optimization
- **Indexed Fields**: Name, Status, Type are typically indexed
- **Filter Early**: Use specific criteria to limit result sets
- **Pagination**: Use appropriate page sizes for large datasets
- **Sort Efficiently**: Sort by indexed fields when possible

### Data Volume
- **Large Campaigns**: Handle campaigns with many members efficiently
- **Bulk Operations**: Use bulk patterns for multiple campaign operations
- **Rate Limiting**: Respect Salesforce API rate limits
- **Caching**: Cache frequently accessed campaign data

## Common Use Cases

### Campaign Lifecycle Management
```
Scenario: Automate campaign status progression
Actions:
1. Create campaign in "Planning" status
2. Update to "Active" when launch date arrives
3. Track performance metrics during execution
4. Update to "Completed" when campaign ends
5. Calculate ROI and update financial fields
```

### Performance Monitoring
```
Scenario: Track campaign effectiveness
Actions:
1. Search for active campaigns
2. Retrieve performance metrics
3. Calculate conversion rates
4. Update expected vs actual performance
5. Generate performance reports
```

### Budget Management
```
Scenario: Monitor campaign spending
Actions:
1. Search campaigns by budget range
2. Compare budgeted vs actual costs
3. Alert when spending exceeds budget
4. Update financial projections
5. Optimize budget allocation
```

## Limitations

### Current Limitations
1. **Bulk Operations**: Limited bulk create/update support
2. **Complex Queries**: Some complex search criteria not supported
3. **Real-time Sync**: No real-time synchronization with Salesforce
4. **Custom Objects**: Limited support for custom campaign-related objects

### Salesforce Limitations
1. **API Limits**: Subject to Salesforce API rate limits
2. **Field Limits**: Limited by Salesforce field limits per object
3. **Sharing Rules**: Subject to org sharing and security settings
4. **Validation Rules**: Must comply with org-defined validation rules

## Best Practices

### 1. Efficient Data Access
- Request only needed fields to minimize API usage
- Use specific search criteria to limit result sets
- Implement pagination for large datasets
- Cache frequently accessed campaign data

### 2. Error Handling
- Implement retry logic for transient failures
- Handle validation errors gracefully
- Provide meaningful error messages to users
- Log errors for troubleshooting

### 3. Security
- Validate user permissions before operations
- Use field-level security appropriately
- Implement audit logging for sensitive operations
- Follow principle of least privilege

### 4. Performance
- Monitor API usage and rate limits
- Use bulk operations when available
- Optimize search queries for performance
- Implement appropriate caching strategies

## Troubleshooting

### Common Issues and Error Messages

#### "We couldn't create the campaign in Salesforce"
**What happened**: Campaign creation failed due to missing or invalid data.

**Common causes**:
- Campaign Name is missing (required field)
- Field values don't match expected data types
- Validation rules are not met

**How to fix**:
- Ensure Campaign Name is provided
- Verify all field values match their data types
- Check validation rules on Campaign object
- See [Troubleshooting Guide](pages/Troubleshooting.md#issue-we-couldnt-create-the-campaign-in-salesforce) for detailed steps

---

#### "We couldn't update the campaign in Salesforce"
**What happened**: Campaign update failed due to invalid data or permissions.

**Common causes**:
- Campaign ID is incorrect or campaign doesn't exist
- Field values violate validation rules
- Insufficient edit permissions

**How to fix**:
- Verify Campaign ID is correct (15-18 chars, starts with '701')
- Check all field values meet validation requirements
- Ensure you have edit permission on campaigns
- See [Troubleshooting Guide](pages/Troubleshooting.md#issue-we-couldnt-update-the-campaign-in-salesforce) for detailed steps

---

#### "A Campaign ID is required to find the campaign"
**What happened**: Attempted to retrieve or update a campaign without providing the ID.

**Common causes**:
- Campaign ID field is empty or null
- ID wasn't captured from previous operation

**How to fix**:
- Provide a valid Campaign ID (15-18 characters, starts with '701')
- Ensure ID is captured and stored after campaign creation
- Verify ID is passed correctly between workflow steps
- See [Troubleshooting Guide](pages/Troubleshooting.md#issue-a-campaign-id-is-required-to-find-the-campaign) for detailed steps

---

#### "We couldn't search for campaigns with the criteria you provided"
**What happened**: Campaign search failed due to invalid criteria or permissions.

**Common causes**:
- Search field name doesn't exist
- Insufficient read permissions
- Invalid search operators or values

**How to fix**:
- Use supported search fields: Name, Status, or Type
- Verify you have read access to Campaign object
- Ensure search values are not empty
- See [Troubleshooting Guide](pages/Troubleshooting.md#issue-we-couldnt-search-for-campaigns-with-the-criteria-you-provided) for detailed steps

---

#### "Campaign deletion is not available in this extension"
**What happened**: Attempted to delete a campaign, which is not supported.

**Why**: This feature requires additional safety checks to prevent accidental data loss.

**How to delete campaigns**:
- Log in to Salesforce directly
- Navigate to the campaign and use the Delete button
- Or mark campaigns as inactive instead of deleting
- See [Troubleshooting Guide](pages/Troubleshooting.md#issue-campaign-deletion-is-not-available-in-this-extension) for alternatives

---

### For More Help

For comprehensive troubleshooting information, including authentication issues, network problems, and detailed resolution steps, see the [Troubleshooting Guide](pages/Troubleshooting.md).

## See Also

- [Supported Requests](pages/supportedRequests.md) - Complete API reference
- [Retrieve Custom Fields For Given Object](pages/RetrieveCustomFieldsForGivenObject.md) - Custom field access
- [Extension Configuration](pages/ExtensionConfiguration.md) - Setup and permissions
- [Troubleshooting](pages/Troubleshooting.md) - Issue resolution guide
