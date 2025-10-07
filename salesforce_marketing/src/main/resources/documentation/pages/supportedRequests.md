# Supported Requests

## Overview

The Salesforce Marketing Extension provides comprehensive functionality for managing Salesforce marketing operations through catalog requests and entity operations. This page outlines all available requests, their capabilities, and usage scenarios.

## Catalog Requests

Catalog requests are specific operations that can be invoked through Krista's automation platform.

### Content Management

#### 🔧 Retrieve Custom Fields For Given Object

Extracts custom field data from Salesforce marketing objects for analysis and reporting.

- **Area**: Content
- **Type**: QUERY_SYSTEM
- **Retry Support**: ✅ Yes
- **[Detailed Documentation](pages/RetrieveCustomFieldsForGivenObject.md)**

**Quick Reference**:
```
Input: Object ID + Object Type + Custom Field Names
Output: FreeForm object with field values
Use Case: Campaign analysis, custom reporting, data extraction
```

**Input Parameters**:

| Parameter Name | Type | Required | Description | Example |
|----------------|------|----------|-------------|---------|
| Object Id | Text | Yes | Salesforce record identifier | "7013000000XyZ123" |
| Object Type | Pick List | Yes | Object type (Campaign) | "Campaign" |
| Custom Fields | List<Text> | Yes | Custom field API names | ["Competition__c", "Budget_Category__c"] |

**Output Parameters**:

| Parameter Name | Type | Description |
|----------------|------|-------------|
| Field Details | FreeForm | Dynamic object containing custom field values |

---

## Entity Operations

Entity operations provide full CRUD (Create, Read, Update, Delete) functionality for Salesforce marketing objects through Krista's entity management system.

### 📊 Campaigns {#campaigns}

The Campaign entity enables comprehensive management of Salesforce marketing campaigns with full lifecycle support.

**[📖 Complete Campaign Entity Documentation](pages/CampaignEntity.md)**

#### Entity Overview

- **Entity Name**: Campaign
- **Primary Key**: Id
- **Searchable Fields**: Name, Type, Status
- **Support Level**: Full CRUD operations
- **Store Support**: Yes - enables entity storage and retrieval

#### Supported Operations

##### 🔍 Search Campaigns

Find campaigns based on various criteria with flexible filtering and sorting.

**Search Capabilities**:
- **By Name**: Find campaigns matching name patterns
- **By Status**: Filter campaigns by current status
- **By Type**: Filter campaigns by campaign type
- **Combined Search**: Use multiple criteria simultaneously
- **Pagination**: Support for large result sets
- **Sorting**: Order results by any field

**Search Parameters**:

| Field | Operators | Description | Example |
|-------|-----------|-------------|---------|
| Name | Contains, Equals | Campaign name matching | "Holiday 2024" |
| Status | Equals | Campaign status | "Active", "Completed" |
| Type | Equals | Campaign type | "Email", "Social Media" |

**Example Search Queries**:
```
Find active email campaigns: Status = "Active" AND Type = "Email"
Find campaigns by name: Name CONTAINS "Holiday"
Find completed campaigns: Status = "Completed"
```

##### ➕ Create Campaign

Create new marketing campaigns with comprehensive field support.

**Supported Fields**:
- **Basic Information**: Name, Description, Type, Status
- **Dates**: Start Date, End Date, Created Date
- **Financial**: Budgeted Cost, Expected Revenue, Actual Cost
- **Metrics**: Expected Response, Number Sent
- **Relationships**: Owner ID, Created By ID
- **Performance**: Number of Leads, Opportunities, Responses

##### 📖 Read Campaign

Retrieve complete campaign information by ID.

**Retrieved Data**:
- All standard campaign fields
- Custom fields (if accessible)
- Related metrics and performance data
- Audit information (created/modified dates and users)

##### ✏️ Update Campaign

Modify existing campaign information with field-level updates.

**Updatable Fields**:
- Campaign details (name, description, type)
- Status and dates
- Financial information
- Performance metrics
- Custom fields

##### 🗑️ Delete Campaign

Remove campaigns from Salesforce (subject to org deletion policies).

**Deletion Behavior**:
- Soft delete (moves to Recycle Bin)
- Respects Salesforce deletion restrictions
- Maintains audit trail

#### Campaign Entity Fields

| Field Name | Type | Description | Searchable |
|------------|------|-------------|------------|
| Name | Text | Campaign name | ✅ |
| Id | Text | Unique identifier | ❌ |
| Type | Text | Campaign type | ✅ |
| Status | Text | Current status | ✅ |
| Description | Paragraph | Campaign description | ❌ |
| Start Date | Date | Campaign start date | ❌ |
| End Date | Date | Campaign end date | ❌ |
| Budgeted Cost | Currency | Planned budget | ❌ |
| Expected Revenue | Currency | Revenue target | ❌ |
| Actual Cost | Currency | Actual spend | ❌ |
| Expected Response | Percentage | Response rate target | ❌ |
| Number Sent | Number | Messages/materials sent | ❌ |
| Number Of Leads | Number | Generated leads | ❌ |
| Number Of Converted Leads | Number | Converted leads | ❌ |
| Number Of Contacts | Number | Associated contacts | ❌ |
| Number Of Responses | Number | Total responses | ❌ |
| Number Of Opportunities | Number | Created opportunities | ❌ |
| Number Of Won Opportunities | Number | Won opportunities | ❌ |
| Amount All Opportunities | Currency | Total opportunity value | ❌ |
| Amount Won Opportunities | Currency | Won opportunity value | ❌ |
| Created Date | Date | Creation timestamp | ❌ |
| Last Modified Date | Date | Last update timestamp | ❌ |
| Owner Id | Text | Campaign owner | ❌ |
| Created By Id | Text | Creator user ID | ❌ |

#### Usage Examples

##### Example 1: Search Active Email Campaigns
```
Search Criteria: Status = "Active" AND Type = "Email"
Page Size: 10
Sort: Name ASC
Result: List of active email campaigns ordered by name
```

##### Example 2: Create Holiday Campaign
```
Name: "Holiday 2024 Promotion"
Type: "Email"
Status: "Planning"
Start Date: "2024-12-01"
End Date: "2024-12-31"
Budgeted Cost: 50000.00
Expected Response: 15.5%
```

##### Example 3: Update Campaign Status
```
Campaign ID: "7013000000XyZ123"
Update: Status = "Active", Start Date = "2024-11-15"
Result: Campaign status updated and activated
```

## Request Categories

### By Functionality

**Content Management**:
- Retrieve Custom Fields For Given Object

**Entity Management**:
- Campaign CRUD operations
- Campaign search and filtering

### By Use Case

**📊 Reporting & Analytics**:
- Extract custom field data for analysis
- Search campaigns by performance metrics
- Retrieve campaign financial data

**🔧 Campaign Management**:
- Create and update marketing campaigns
- Manage campaign lifecycle
- Track campaign performance

**🔍 Data Discovery**:
- Search campaigns by various criteria
- Find campaigns by name, status, or type
- Explore campaign relationships

## Integration Patterns

### Common Workflows

1. **Campaign Analysis Pipeline**:
   - Search campaigns by criteria
   - Retrieve custom fields for each campaign
   - Aggregate data for reporting

2. **Campaign Lifecycle Management**:
   - Create new campaigns
   - Update status as campaigns progress
   - Track performance metrics

3. **Data Synchronization**:
   - Search for updated campaigns
   - Extract custom field data
   - Synchronize with external systems

## Performance Considerations

### Best Practices

1. **Search Optimization**:
   - Use specific search criteria to limit results
   - Implement pagination for large datasets
   - Sort by indexed fields when possible

2. **Custom Field Access**:
   - Request only needed custom fields
   - Batch multiple field requests when possible
   - Cache frequently accessed data

3. **Rate Limiting**:
   - Implement retry logic for rate-limited requests
   - Use bulk operations when available
   - Monitor API usage patterns

## Limitations

### Current Limitations

1. **Object Support**: Custom field retrieval currently limited to Campaign objects
2. **Field Types**: Some complex field types may have limited support
3. **Permissions**: All operations subject to user permissions in Salesforce
4. **API Limits**: Subject to Salesforce API rate limits and org limits

### Future Enhancements

- Support for additional marketing objects (Leads, Opportunities)
- Bulk custom field operations
- Advanced search capabilities
- Real-time data synchronization

## See Also

- [Retrieve Custom Fields For Given Object](pages/RetrieveCustomFieldsForGivenObject.md) - Detailed catalog request documentation
- [Extension Configuration](pages/ExtensionConfiguration.md) - Setup and configuration
- [Authentication](pages/Authentication.md) - OAuth and security setup

| **Parameter Name** | **Parameter Type** | **Mandatory** | **Example**   |
|--------------------|--------------------|---------------|---------------|
| Name               | Text               | No            | Demo Campaign |
| Type               | Text               | No            | Conference    |
| Status             | Text               | No            | Planned       |

* Output Parameters:

| **Parameter Name** | **Parameter Type** | 
|--------------------|--------------------|
| Campaign           | List&lt;Campaign>  |


* Get Campaign
    * Description: Returns selected Campaign.
    * Input Parameters: NA
    * Output Parameters:

| **Parameter Name** | **Parameter Type** | 
|--------------------|--------------------|
| Campaign           | Campaign           |


* Create Campaign
    * Description: This will create a Campaign and returns it.
    * Input Parameters:

| **Parameter Name**          | **Parameter Type** | **Mandatory** | **Example**             |
|-----------------------------|--------------------|---------------|-------------------------|
| Name                        | Text               | Yes           | Create Campaign         |
| ID                          | Text               | No            | 0065w00002AlDtDAAM      |
| Number Sent                 | Number             | No            | 10000                   |
| Number Of Leads             | Number             | No            | 1000                    |
| Number of Converted Leads   | Number             | No            | 500                     |
| Number Of Contacts          | Number             | No            | 100000                  |
| Number Of Responses         | Number             | No            | 50000                   |
| Number Of Opportunities     | Number             | No            | 20000                   |
| Number Of Won Opportunities | Number             | No            | 200                     |
| Created Date                | Date               | No            | Entity Description      |
| Last Modified Date          | Date               | No            | 2022-07-22T07&#58;35:12 |
| Owner Id                    | Text               | No            | 0055w00000D9a8iAAC      |
| Created By Id               | Text               | No            | 0015w00002inu5PAAP      |
| Type                        | Text               | No            | Webinar                 |
| Status                      | Text               | No            | Planned                 |
| Description                 | Paragraph          | No            | Entity Description      |
| Start Date                  | Date               | No            | 2022-07-25T10&#58;10:30 |
| End Date                    | Date               | No            | 2022-08-25T05&#58;27:43 |
| Budgeted Cost               | Currency           | No            | 30000000                |
| Expected Revenue            | Currency           | No            | 40000000                |
| Actual cost                 | Currency           | No            | 25000000                |
| Expected Response           | Percentage         | No            | 7                       |
| Amount All Opportunities    | Currency           | No            | 65000000                |
| Amount Won Opportunities    | Currency           | No            | 45000000                |




* Output Parameters:

| **Parameter Name** | **Parameter Type** | 
|--------------------|--------------------|
| Campaign           | Campaign           |



* Update Campaign
    * Description: Returns updated Campaign.
    * Input Parameters:

| **Parameter Name**          | **Parameter Type** | **Mandatory** | **Example**             |
|-----------------------------|--------------------|---------------|-------------------------|
| Name                        | Text               | Yes           | Update Campaign         |
| ID                          | Text               | No            | 0065w00002AlDtDAAM      |
| Number Sent                 | Number             | No            | 10000                   |
| Number Of Leads             | Number             | No            | 1000                    |
| Number of Converted Leads   | Number             | No            | 500                     |
| Number Of Contacts          | Number             | No            | 100000                  |
| Number Of Responses         | Number             | No            | 50000                   |
| Number Of Opportunities     | Number             | No            | 20000                   |
| Number Of Won Opportunities | Number             | No            | 200                     |
| Created Date                | Date               | No            | Entity Description      |
| Last Modified Date          | Date               | No            | 2022-07-22T07&#58;35:12 |
| Owner Id                    | Text               | No            | 0055w00000D9a8iAAC      |
| Created By Id               | Text               | No            | 0015w00002inu5PAAP      |
| Type                        | Text               | No            | Webinar                 |
| Status                      | Text               | No            | Aborted                 |
| Description                 | Paragraph          | No            | Entity Description      |
| Start Date                  | Date               | No            | 2022-07-25T10&#58;10:30 |
| End Date                    | Date               | No            | 2022-08-25T10&#58;10:30 |
| Budgeted Cost               | Currency           | No            | 30000000                |
| Expected Revenue            | Currency           | No            | 40000000                |
| Actual cost                 | Currency           | No            | 25000000                |
| Expected Response           | Percentage         | No            | 7                       |
| Amount All Opportunities    | Currency           | No            | 65000000                |
| Amount Won Opportunities    | Currency           | No            | 45000000                |


* Output Parameters:

| **Parameter Name** | **Parameter Type** | 
|--------------------|--------------------|
| Campaign           | Campaign           |
