[<span style = "color : blue ; text-decoration: none">< Back<span>](/)

# Supported Requests

## Catalog Requests

### Content Area

#### Retrieve Custom Fields For Given Object

- **Description**: Retrieve Custom Fields For Given Object
- **Input Parameters**:

| **Parameter Name** | **Parameter Type** | **Mandatory** | **Example**    |
|--------------------|--------------------|---------------|----------------|
| Object Id          | Text               | Yes           | 123456         |
| Object Type        | Pick List          | Yes           | Campaign       |
| Custom Fields      | List<Text>         | Yes           | Competition__c |

- **Output Parameters**:

| **Parameter Name** | **Parameter Type** |
|--------------------|--------------------|
| Field Details      | FreeForm           |



## Entity Requests

The following Entity Request is supported

#### Campaigns

* Search Campaign
    * Description: This will return the list of Campaigns.
    * Input Parameters:

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
