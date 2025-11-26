package app.krista.extensions.crm.marketing_management.salesforce_marketing.catalog;

import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.ContentDaoImpl;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.SalesforceManager;
import app.krista.model.base.FreeForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ContentArea class.
 * Tests the retrieveCustomFieldsForGivenObject catalog request with various scenarios.
 */
@DisplayName("ContentArea Tests")
class ContentAreaTest {

    @Mock
    private SalesforceManager mockSalesforceManager;

    @Mock
    private ContentDaoImpl mockContentDao;

    private ContentArea contentArea;

    private static final String VALID_OBJECT_ID = "7013000000XyZ123";
    private static final String OBJECT_TYPE_CAMPAIGN = "Campaign";
    private static final String VALID_CUSTOM_FIELD_1 = "Budget_Category__c";
    private static final String VALID_CUSTOM_FIELD_2 = "Competition__c";
    private static final String INVALID_CUSTOM_FIELD = "InvalidField";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        contentArea = new ContentArea(mockSalesforceManager, mockContentDao);
    }

    @Test
    @DisplayName("Should create ContentArea with injected dependencies")
    void shouldCreateContentAreaWithInjectedDependencies() {
        // Given & When
        ContentArea area = new ContentArea(mockSalesforceManager, mockContentDao);

        // Then
        assertNotNull(area);
    }

    @Test
    @DisplayName("Should retrieve custom fields successfully with valid inputs")
    void shouldRetrieveCustomFieldsSuccessfullyWithValidInputs() {
        // Given
        List<String> customFields = Arrays.asList(VALID_CUSTOM_FIELD_1, VALID_CUSTOM_FIELD_2);
        String expectedQuery = "Select Budget_Category__c,Competition__c from Campaign where id = '7013000000XyZ123'";
        String mockJsonResponse = "{\"records\":[{\"Budget_Category__c\":\"Marketing\",\"Competition__c\":\"High\"}]}";
        
        FreeForm expectedResult = new FreeForm();
        expectedResult.put("Budget_Category__c", "Text", "Marketing");
        expectedResult.put("Competition__c", "Text", "High");

        when(mockSalesforceManager.getRecordByQuery(expectedQuery)).thenReturn(mockJsonResponse);
        when(mockContentDao.parseJSONToMap(mockJsonResponse, customFields)).thenReturn(expectedResult);

        // When
        FreeForm result = contentArea.retrieveCustomFieldsForGivenObject(VALID_OBJECT_ID, OBJECT_TYPE_CAMPAIGN, customFields);

        // Then
        assertNotNull(result);
        assertEquals(expectedResult, result);
        verify(mockSalesforceManager, times(1)).getRecordByQuery(expectedQuery);
        verify(mockContentDao, times(1)).parseJSONToMap(mockJsonResponse, customFields);
    }

    @Test
    @DisplayName("Should return empty FreeForm when custom field does not end with __c")
    void shouldReturnEmptyFreeFormWhenCustomFieldDoesNotEndWithUnderscoreC() {
        // Given
        List<String> customFields = Arrays.asList(VALID_CUSTOM_FIELD_1, INVALID_CUSTOM_FIELD);

        // When
        FreeForm result = contentArea.retrieveCustomFieldsForGivenObject(VALID_OBJECT_ID, OBJECT_TYPE_CAMPAIGN, customFields);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mockSalesforceManager, never()).getRecordByQuery(anyString());
        verify(mockContentDao, never()).parseJSONToMap(anyString(), anyList());
    }

    @Test
    @DisplayName("Should return empty FreeForm when all custom fields are invalid")
    void shouldReturnEmptyFreeFormWhenAllCustomFieldsAreInvalid() {
        // Given
        List<String> customFields = Arrays.asList("Field1", "Field2", "Field3");

        // When
        FreeForm result = contentArea.retrieveCustomFieldsForGivenObject(VALID_OBJECT_ID, OBJECT_TYPE_CAMPAIGN, customFields);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mockSalesforceManager, never()).getRecordByQuery(anyString());
        verify(mockContentDao, never()).parseJSONToMap(anyString(), anyList());
    }

    @Test
    @DisplayName("Should handle single valid custom field")
    void shouldHandleSingleValidCustomField() {
        // Given
        List<String> customFields = Collections.singletonList(VALID_CUSTOM_FIELD_1);
        String expectedQuery = "Select Budget_Category__c from Campaign where id = '7013000000XyZ123'";
        String mockJsonResponse = "{\"records\":[{\"Budget_Category__c\":\"Marketing\"}]}";
        
        FreeForm expectedResult = new FreeForm();
        expectedResult.put("Budget_Category__c", "Text", "Marketing");

        when(mockSalesforceManager.getRecordByQuery(expectedQuery)).thenReturn(mockJsonResponse);
        when(mockContentDao.parseJSONToMap(mockJsonResponse, customFields)).thenReturn(expectedResult);

        // When
        FreeForm result = contentArea.retrieveCustomFieldsForGivenObject(VALID_OBJECT_ID, OBJECT_TYPE_CAMPAIGN, customFields);

        // Then
        assertNotNull(result);
        assertEquals(expectedResult, result);
        verify(mockSalesforceManager, times(1)).getRecordByQuery(expectedQuery);
    }

    @Test
    @DisplayName("Should return empty FreeForm when SalesforceManager returns null")
    void shouldReturnEmptyFreeFormWhenSalesforceManagerReturnsNull() {
        // Given
        List<String> customFields = Arrays.asList(VALID_CUSTOM_FIELD_1, VALID_CUSTOM_FIELD_2);
        String expectedQuery = "Select Budget_Category__c,Competition__c from Campaign where id = '7013000000XyZ123'";

        when(mockSalesforceManager.getRecordByQuery(expectedQuery)).thenReturn(null);

        // When
        FreeForm result = contentArea.retrieveCustomFieldsForGivenObject(VALID_OBJECT_ID, OBJECT_TYPE_CAMPAIGN, customFields);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mockSalesforceManager, times(1)).getRecordByQuery(expectedQuery);
        verify(mockContentDao, never()).parseJSONToMap(anyString(), anyList());
    }

    @Test
    @DisplayName("Should handle empty custom fields list")
    void shouldHandleEmptyCustomFieldsList() {
        // Given
        List<String> customFields = Collections.emptyList();
        String expectedQuery = "Select  from Campaign where id = '7013000000XyZ123'";
        String mockJsonResponse = "{\"records\":[]}";
        
        FreeForm expectedResult = new FreeForm();

        when(mockSalesforceManager.getRecordByQuery(expectedQuery)).thenReturn(mockJsonResponse);
        when(mockContentDao.parseJSONToMap(mockJsonResponse, customFields)).thenReturn(expectedResult);

        // When
        FreeForm result = contentArea.retrieveCustomFieldsForGivenObject(VALID_OBJECT_ID, OBJECT_TYPE_CAMPAIGN, customFields);

        // Then
        assertNotNull(result);
        verify(mockSalesforceManager, times(1)).getRecordByQuery(expectedQuery);
    }

    @Test
    @DisplayName("Should handle multiple valid custom fields")
    void shouldHandleMultipleValidCustomFields() {
        // Given
        List<String> customFields = Arrays.asList(
            "Field1__c", 
            "Field2__c", 
            "Field3__c", 
            "Field4__c"
        );
        String expectedQuery = "Select Field1__c,Field2__c,Field3__c,Field4__c from Campaign where id = '7013000000XyZ123'";
        String mockJsonResponse = "{\"records\":[{\"Field1__c\":\"Value1\"}]}";
        
        FreeForm expectedResult = new FreeForm();

        when(mockSalesforceManager.getRecordByQuery(expectedQuery)).thenReturn(mockJsonResponse);
        when(mockContentDao.parseJSONToMap(mockJsonResponse, customFields)).thenReturn(expectedResult);

        // When
        FreeForm result = contentArea.retrieveCustomFieldsForGivenObject(VALID_OBJECT_ID, OBJECT_TYPE_CAMPAIGN, customFields);

        // Then
        assertNotNull(result);
        verify(mockSalesforceManager, times(1)).getRecordByQuery(expectedQuery);
        verify(mockContentDao, times(1)).parseJSONToMap(mockJsonResponse, customFields);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "Custom_Field__c",
        "Another_Field__c",
        "Test_Field__c",
        "Budget__c",
        "Revenue__c"
    })
    @DisplayName("Should accept various valid custom field names ending with __c")
    void shouldAcceptVariousValidCustomFieldNames(String validFieldName) {
        // Given
        List<String> customFields = Collections.singletonList(validFieldName);
        String mockJsonResponse = "{\"records\":[]}";
        FreeForm expectedResult = new FreeForm();

        when(mockSalesforceManager.getRecordByQuery(anyString())).thenReturn(mockJsonResponse);
        when(mockContentDao.parseJSONToMap(anyString(), anyList())).thenReturn(expectedResult);

        // When
        FreeForm result = contentArea.retrieveCustomFieldsForGivenObject(VALID_OBJECT_ID, OBJECT_TYPE_CAMPAIGN, customFields);

        // Then
        assertNotNull(result);
        verify(mockSalesforceManager, times(1)).getRecordByQuery(anyString());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "InvalidField",
        "Field__",
        "Field_c",
        "Field__C"
    })
    @DisplayName("Should reject invalid custom field names")
    void shouldRejectInvalidCustomFieldNames(String invalidFieldName) {
        // Given
        List<String> customFields = Collections.singletonList(invalidFieldName);

        // When
        FreeForm result = contentArea.retrieveCustomFieldsForGivenObject(VALID_OBJECT_ID, OBJECT_TYPE_CAMPAIGN, customFields);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mockSalesforceManager, never()).getRecordByQuery(anyString());
    }

    @Test
    @DisplayName("Should construct correct SOQL query with valid fields")
    void shouldConstructCorrectSoqlQueryWithValidFields() {
        // Given
        List<String> customFields = Arrays.asList("Field1__c", "Field2__c");
        String expectedQuery = "Select Field1__c,Field2__c from Campaign where id = '7013000000XyZ123'";
        
        when(mockSalesforceManager.getRecordByQuery(expectedQuery)).thenReturn("{}");
        when(mockContentDao.parseJSONToMap(anyString(), anyList())).thenReturn(new FreeForm());

        // When
        contentArea.retrieveCustomFieldsForGivenObject(VALID_OBJECT_ID, OBJECT_TYPE_CAMPAIGN, customFields);

        // Then
        verify(mockSalesforceManager, times(1)).getRecordByQuery(expectedQuery);
    }

    @Test
    @DisplayName("Should handle ContentDao returning empty FreeForm")
    void shouldHandleContentDaoReturningEmptyFreeForm() {
        // Given
        List<String> customFields = Collections.singletonList(VALID_CUSTOM_FIELD_1);
        String mockJsonResponse = "{\"records\":[]}";
        FreeForm emptyResult = new FreeForm();

        when(mockSalesforceManager.getRecordByQuery(anyString())).thenReturn(mockJsonResponse);
        when(mockContentDao.parseJSONToMap(mockJsonResponse, customFields)).thenReturn(emptyResult);

        // When
        FreeForm result = contentArea.retrieveCustomFieldsForGivenObject(VALID_OBJECT_ID, OBJECT_TYPE_CAMPAIGN, customFields);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should validate first invalid field and return immediately")
    void shouldValidateFirstInvalidFieldAndReturnImmediately() {
        // Given - First field is valid, second is invalid
        List<String> customFields = Arrays.asList(VALID_CUSTOM_FIELD_1, INVALID_CUSTOM_FIELD, VALID_CUSTOM_FIELD_2);

        // When
        FreeForm result = contentArea.retrieveCustomFieldsForGivenObject(VALID_OBJECT_ID, OBJECT_TYPE_CAMPAIGN, customFields);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mockSalesforceManager, never()).getRecordByQuery(anyString());
        verify(mockContentDao, never()).parseJSONToMap(anyString(), anyList());
    }
}

