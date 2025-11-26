package app.krista.extensions.crm.marketing_management.salesforce_marketing.impl;

import app.krista.extensions.crm.marketing_management.salesforce_marketing.service.Campaign;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.argThat;

/**
 * Comprehensive unit tests for CampaignDaoImpl class.
 * Tests all CRUD operations and query methods with various scenarios.
 */
@DisplayName("CampaignDaoImpl Tests")
class CampaignDaoImplTest {

    @Mock
    private SalesforceManager mockSalesforceManager;

    private CampaignDaoImpl campaignDao;

    private static final String SAMPLE_CAMPAIGN_JSON = """
            {
                "totalSize": 1,
                "done": true,
                "records": [
                    {
                        "Id": "7013000000XyZ123",
                        "Name": "Test Campaign",
                        "Type": "Email",
                        "Status": "In Progress",
                        "StartDate": "2024-01-01",
                        "EndDate": "2024-12-31",
                        "ExpectedRevenue": 100000.0,
                        "BudgetedCost": 50000.0,
                        "ActualCost": 45000.0,
                        "ExpectedResponse": 1000.0,
                        "NumberSent": 5000.0,
                        "NumberOfLeads": 500.0,
                        "NumberOfContacts": 300.0,
                        "NumberOfOpportunities": 50.0,
                        "NumberOfConvertedLeads": 100.0,
                        "CreatedDate": "2024-01-01T10:00:00.000+0000",
                        "LastModifiedDate": "2024-01-15T10:00:00.000+0000"
                    }
                ]
            }
            """;

    private static final String EMPTY_CAMPAIGN_JSON = """
            {
                "totalSize": 0,
                "done": true,
                "records": []
            }
            """;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        campaignDao = new CampaignDaoImpl(mockSalesforceManager);
        Gson gson = new Gson();
    }

    @Test
    @DisplayName("Should create CampaignDaoImpl with injected SalesforceManager")
    void shouldCreateCampaignDaoImplWithInjectedDependencies() {
        // Given & When
        CampaignDaoImpl dao = new CampaignDaoImpl(mockSalesforceManager);

        // Then
        assertNotNull(dao);
    }

    @Test
    @DisplayName("Should get campaigns with pagination and ordering")
    void shouldGetCampaignsWithPaginationAndOrdering() {
        // Given
        long size = 10;
        long page = 1;
        String orderByField = "Name";
        String orderByOperand = "ASC";

        when(mockSalesforceManager.getRecordByQuery(anyString())).thenReturn(SAMPLE_CAMPAIGN_JSON);

        // When
        List<Campaign> campaigns = campaignDao.getCampaigns(size, page, orderByField, orderByOperand);

        // Then
        assertNotNull(campaigns);
        assertEquals(1, campaigns.size());
        assertEquals("Test Campaign", campaigns.get(0).getName());
        verify(mockSalesforceManager, times(1)).getRecordByQuery(contains("ORDER BY"));
        verify(mockSalesforceManager, times(1)).getRecordByQuery(contains("LIMIT 10"));
        verify(mockSalesforceManager, times(1)).getRecordByQuery(contains("OFFSET 0"));
    }

    @Test
    @DisplayName("Should get campaigns with correct offset for page 2")
    void shouldGetCampaignsWithCorrectOffsetForPage2() {
        // Given
        long size = 10;
        long page = 2;
        String orderByField = "CreatedDate";
        String orderByOperand = "DESC";

        when(mockSalesforceManager.getRecordByQuery(anyString())).thenReturn(SAMPLE_CAMPAIGN_JSON);

        // When
        List<Campaign> campaigns = campaignDao.getCampaigns(size, page, orderByField, orderByOperand);

        // Then
        assertNotNull(campaigns);
        verify(mockSalesforceManager, times(1)).getRecordByQuery(contains("OFFSET 10"));
    }

    @Test
    @DisplayName("Should return empty list when no campaigns found")
    void shouldReturnEmptyListWhenNoCampaignsFound() {
        // Given
        when(mockSalesforceManager.getRecordByQuery(anyString())).thenReturn(EMPTY_CAMPAIGN_JSON);

        // When
        List<Campaign> campaigns = campaignDao.getCampaigns(10, 1, "Name", "ASC");

        // Then
        assertNotNull(campaigns);
        assertTrue(campaigns.isEmpty());
    }

    @Test
    @DisplayName("Should get campaign by name with SOQL injection prevention")
    void shouldGetCampaignByNameWithSoqlInjectionPrevention() {
        // Given
        String maliciousName = "Test' OR '1'='1";
        when(mockSalesforceManager.getRecordByQuery(anyString())).thenReturn(SAMPLE_CAMPAIGN_JSON);

        // When
        List<Campaign> campaigns = campaignDao.getCampaignByName(maliciousName, 10, 1, "Name", "ASC");

        // Then
        assertNotNull(campaigns);
        verify(mockSalesforceManager, times(1)).getRecordByQuery(contains("\\'"));
    }

    @Test
    @DisplayName("Should escape SOQL wildcards in campaign name search")
    void shouldEscapeSoqlWildcardsInCampaignNameSearch() {
        // Given
        String nameWithWildcards = "Test%Campaign_Name";
        when(mockSalesforceManager.getRecordByQuery(anyString())).thenReturn(SAMPLE_CAMPAIGN_JSON);

        // When
        List<Campaign> campaigns = campaignDao.getCampaignByName(nameWithWildcards, 10, 1, "Name", "ASC");

        // Then
        assertNotNull(campaigns);
        verify(mockSalesforceManager, times(1)).getRecordByQuery(contains("\\%"));
        verify(mockSalesforceManager, times(1)).getRecordByQuery(contains("\\_"));
    }

    @Test
    @DisplayName("Should get campaign by name and type")
    void shouldGetCampaignByNameAndType() {
        // Given
        String name = "Test Campaign";
        String type = "Email";
        when(mockSalesforceManager.getRecordByQuery(anyString())).thenReturn(SAMPLE_CAMPAIGN_JSON);

        // When
        List<Campaign> campaigns = campaignDao.getCampaignByName(name, type, 10, 1, "Name", "ASC");

        // Then
        assertNotNull(campaigns);
        assertEquals(1, campaigns.size());
        verify(mockSalesforceManager, times(1)).getRecordByQuery(contains("name LIKE"));
        verify(mockSalesforceManager, times(1)).getRecordByQuery(contains("Type LIKE"));
    }

    @Test
    @DisplayName("Should get campaign by name only when type is null")
    void shouldGetCampaignByNameOnlyWhenTypeIsNull() {
        // Given
        String name = "Test Campaign";
        String type = null;
        when(mockSalesforceManager.getRecordByQuery(anyString())).thenReturn(SAMPLE_CAMPAIGN_JSON);

        // When
        List<Campaign> campaigns = campaignDao.getCampaignByName(name, type, 10, 1, "Name", "ASC");

        // Then
        assertNotNull(campaigns);
        verify(mockSalesforceManager, times(1)).getRecordByQuery(contains("name LIKE"));
        verify(mockSalesforceManager, times(1)).getRecordByQuery(argThat(query -> !query.contains("Type LIKE")));
    }

    @Test
    @DisplayName("Should get campaign by name only when type is empty")
    void shouldGetCampaignByNameOnlyWhenTypeIsEmpty() {
        // Given
        String name = "Test Campaign";
        String type = "";
        when(mockSalesforceManager.getRecordByQuery(anyString())).thenReturn(SAMPLE_CAMPAIGN_JSON);

        // When
        List<Campaign> campaigns = campaignDao.getCampaignByName(name, type, 10, 1, "Name", "ASC");

        // Then
        assertNotNull(campaigns);
        verify(mockSalesforceManager, times(1)).getRecordByQuery(argThat(query -> !query.contains("Type LIKE")));
    }

    @Test
    @DisplayName("Should get campaign by type")
    void shouldGetCampaignByType() {
        // Given
        String type = "Email";
        when(mockSalesforceManager.getRecordByQuery(anyString())).thenReturn(SAMPLE_CAMPAIGN_JSON);

        // When
        List<Campaign> campaigns = campaignDao.getCampaignByType(type, 10, 1, "Name", "ASC");

        // Then
        assertNotNull(campaigns);
        assertEquals(1, campaigns.size());
        verify(mockSalesforceManager, times(1)).getRecordByQuery(contains("type LIKE"));
    }

    @Test
    @DisplayName("Should escape SOQL injection in type search")
    void shouldEscapeSoqlInjectionInTypeSearch() {
        // Given
        String maliciousType = "Email' OR '1'='1";
        when(mockSalesforceManager.getRecordByQuery(anyString())).thenReturn(SAMPLE_CAMPAIGN_JSON);

        // When
        List<Campaign> campaigns = campaignDao.getCampaignByType(maliciousType, 10, 1, "Name", "ASC");

        // Then
        assertNotNull(campaigns);
        verify(mockSalesforceManager, times(1)).getRecordByQuery(contains("\\'"));
    }

    @Test
    @DisplayName("Should get campaign by status")
    void shouldGetCampaignByStatus() {
        // Given
        String status = "In Progress";
        when(mockSalesforceManager.getRecordByQuery(anyString())).thenReturn(SAMPLE_CAMPAIGN_JSON);

        // When
        List<Campaign> campaigns = campaignDao.getCampaignByStatus(status, 10, 1, "Name", "ASC");

        // Then
        assertNotNull(campaigns);
        assertEquals(1, campaigns.size());
        verify(mockSalesforceManager, times(1)).getRecordByQuery(contains("status LIKE"));
    }

    @Test
    @DisplayName("Should escape SOQL injection in status search")
    void shouldEscapeSoqlInjectionInStatusSearch() {
        // Given
        String maliciousStatus = "Active' OR '1'='1";
        when(mockSalesforceManager.getRecordByQuery(anyString())).thenReturn(SAMPLE_CAMPAIGN_JSON);

        // When
        List<Campaign> campaigns = campaignDao.getCampaignByStatus(maliciousStatus, 10, 1, "Name", "ASC");

        // Then
        assertNotNull(campaigns);
        verify(mockSalesforceManager, times(1)).getRecordByQuery(contains("\\'"));
    }

    @Test
    @DisplayName("Should get campaign by ID")
    void shouldGetCampaignById() {
        // Given
        String campaignId = "7013000000XyZ123";
        when(mockSalesforceManager.getRecordByQuery(anyString())).thenReturn(SAMPLE_CAMPAIGN_JSON);

        // When
        Campaign campaign = campaignDao.getCampaignById(campaignId);

        // Then
        assertNotNull(campaign);
        assertEquals("Test Campaign", campaign.getName());
        verify(mockSalesforceManager, times(1)).getRecordByQuery(contains("where id = '" + campaignId + "'"));
    }

    @Test
    @DisplayName("Should throw exception when campaign not found by ID")
    void shouldThrowExceptionWhenCampaignNotFoundById() {
        // Given
        String campaignId = "nonexistent";
        when(mockSalesforceManager.getRecordByQuery(anyString())).thenReturn(EMPTY_CAMPAIGN_JSON);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> campaignDao.getCampaignById(campaignId));
    }

    @Test
    @DisplayName("Should add campaign successfully")
    void shouldAddCampaignSuccessfully() {
        // Given
        Map<String, Object> campaignAttributes = new HashMap<>();
        campaignAttributes.put("Name", "New Campaign");
        campaignAttributes.put("Type", "Email");
        campaignAttributes.put("Status", "Planned");

        String createResponse = "{\"id\":\"7013000000NewCamp\",\"success\":true}";
        when(mockSalesforceManager.createRecord(anyString(), eq("Campaign"))).thenReturn(createResponse);

        // When
        String campaignId = campaignDao.addCampaign(campaignAttributes);

        // Then
        assertNotNull(campaignId);
        assertEquals("7013000000NewCamp", campaignId);
        verify(mockSalesforceManager, times(1)).createRecord(anyString(), eq("Campaign"));
    }

    @Test
    @DisplayName("Should throw UnsupportedOperationException when deleting campaign")
    void shouldThrowUnsupportedOperationExceptionWhenDeletingCampaign() {
        // Given
        String campaignId = "7013000000XyZ123";

        // When & Then
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> campaignDao.deleteCampaign(campaignId)
        );

        assertTrue(exception.getMessage().contains("Campaign deletion is not available"));
    }

    @Test
    @DisplayName("Should handle negative page number by using offset 0")
    void shouldHandleNegativePageNumberByUsingOffset0() {
        // Given
        long size = 10;
        long page = -1;
        when(mockSalesforceManager.getRecordByQuery(anyString())).thenReturn(SAMPLE_CAMPAIGN_JSON);

        // When
        List<Campaign> campaigns = campaignDao.getCampaigns(size, page, "Name", "ASC");

        // Then
        assertNotNull(campaigns);
        verify(mockSalesforceManager, times(1)).getRecordByQuery(contains("OFFSET 0"));
    }

    @Test
    @DisplayName("Should handle page 0 by using offset 0")
    void shouldHandlePage0ByUsingOffset0() {
        // Given
        long size = 10;
        long page = 0;
        when(mockSalesforceManager.getRecordByQuery(anyString())).thenReturn(SAMPLE_CAMPAIGN_JSON);

        // When
        List<Campaign> campaigns = campaignDao.getCampaigns(size, page, "Name", "ASC");

        // Then
        assertNotNull(campaigns);
        verify(mockSalesforceManager, times(1)).getRecordByQuery(contains("OFFSET 0"));
    }

    @Test
    @DisplayName("Should construct correct query with all required fields")
    void shouldConstructCorrectQueryWithAllRequiredFields() {
        // Given
        when(mockSalesforceManager.getRecordByQuery(anyString())).thenReturn(SAMPLE_CAMPAIGN_JSON);

        // When
        campaignDao.getCampaigns(10, 1, "Name", "ASC");

        // Then
        verify(mockSalesforceManager, times(1)).getRecordByQuery(contains("Status"));
        verify(mockSalesforceManager, times(1)).getRecordByQuery(contains("Name"));
        verify(mockSalesforceManager, times(1)).getRecordByQuery(contains("Type"));
        verify(mockSalesforceManager, times(1)).getRecordByQuery(contains("StartDate"));
        verify(mockSalesforceManager, times(1)).getRecordByQuery(contains("EndDate"));
        verify(mockSalesforceManager, times(1)).getRecordByQuery(contains("ExpectedRevenue"));
        verify(mockSalesforceManager, times(1)).getRecordByQuery(contains("BudgetedCost"));
        verify(mockSalesforceManager, times(1)).getRecordByQuery(contains("ActualCost"));
    }
}
