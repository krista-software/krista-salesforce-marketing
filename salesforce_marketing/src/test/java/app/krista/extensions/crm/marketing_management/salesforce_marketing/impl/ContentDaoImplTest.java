/*
 * Salesforce Marketing Extension for Krista
 * Copyright (C) 2025 Krista Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package app.krista.extensions.crm.marketing_management.salesforce_marketing.impl;

import app.krista.model.base.FreeForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for ContentDaoImpl class.
 * Tests JSON parsing logic with various scenarios.
 */
@DisplayName("ContentDaoImpl Tests")
class ContentDaoImplTest {

    private ContentDaoImpl contentDao;

    @BeforeEach
    void setUp() {
        contentDao = new ContentDaoImpl();
    }

    @Test
    @DisplayName("Should create ContentDaoImpl successfully")
    void shouldCreateContentDaoImplSuccessfully() {
        // Given & When
        ContentDaoImpl dao = new ContentDaoImpl();

        // Then
        assertNotNull(dao);
    }

    @Test
    @DisplayName("Should parse valid JSON with custom fields successfully")
    void shouldParseValidJsonWithCustomFieldsSuccessfully() {
        // Given
        String jsonResponse = """
                {
                    "totalSize": 1,
                    "done": true,
                    "records": [
                        {
                            "CustomField1__c": "Value1",
                            "CustomField2__c": "Value2",
                            "CustomField3__c": "Value3"
                        }
                    ]
                }
                """;
        List<String> customFields = Arrays.asList("CustomField1__c", "CustomField2__c", "CustomField3__c");

        // When
        FreeForm result = contentDao.parseJSONToMap(jsonResponse, customFields);

        // Then
        assertNotNull(result);
        assertEquals("Value1", result.get("CustomField1__c"));
        assertEquals("Value2", result.get("CustomField2__c"));
        assertEquals("Value3", result.get("CustomField3__c"));
    }

    @Test
    @DisplayName("Should parse JSON with single custom field")
    void shouldParseJsonWithSingleCustomField() {
        // Given
        String jsonResponse = """
                {
                    "records": [
                        {
                            "Email__c": "test@example.com"
                        }
                    ]
                }
                """;
        List<String> customFields = Collections.singletonList("Email__c");

        // When
        FreeForm result = contentDao.parseJSONToMap(jsonResponse, customFields);

        // Then
        assertNotNull(result);
        assertEquals("test@example.com", result.get("Email__c"));
    }

    @Test
    @DisplayName("Should return empty FreeForm when JSON response is null")
    void shouldReturnEmptyFreeFormWhenJsonResponseIsNull() {
        // Given
        String jsonResponse = null;
        List<String> customFields = Arrays.asList("Field1__c", "Field2__c");

        // When
        FreeForm result = contentDao.parseJSONToMap(jsonResponse, customFields);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return empty FreeForm when records array is missing")
    void shouldReturnEmptyFreeFormWhenRecordsArrayIsMissing() {
        // Given
        String jsonResponse = """
                {
                    "totalSize": 0,
                    "done": true
                }
                """;
        List<String> customFields = Arrays.asList("Field1__c", "Field2__c");

        // When
        FreeForm result = contentDao.parseJSONToMap(jsonResponse, customFields);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return empty FreeForm when records array is empty")
    void shouldReturnEmptyFreeFormWhenRecordsArrayIsEmpty() {
        // Given
        String jsonResponse = """
                {
                    "totalSize": 0,
                    "done": true,
                    "records": []
                }
                """;
        List<String> customFields = Arrays.asList("Field1__c", "Field2__c");

        // When
        FreeForm result = contentDao.parseJSONToMap(jsonResponse, customFields);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should handle custom field not present in record")
    void shouldHandleCustomFieldNotPresentInRecord() {
        // Given
        String jsonResponse = """
                {
                    "records": [
                        {
                            "Field1__c": "Value1"
                        }
                    ]
                }
                """;
        List<String> customFields = Arrays.asList("Field1__c", "Field2__c", "Field3__c");

        // When
        FreeForm result = contentDao.parseJSONToMap(jsonResponse, customFields);

        // Then
        assertNotNull(result);
        assertEquals("Value1", result.get("Field1__c"));
        assertNull(result.get("Field2__c"));
        assertNull(result.get("Field3__c"));
    }

    @Test
    @DisplayName("Should handle empty custom fields list")
    void shouldHandleEmptyCustomFieldsList() {
        // Given
        String jsonResponse = """
                {
                    "records": [
                        {
                            "Field1__c": "Value1",
                            "Field2__c": "Value2"
                        }
                    ]
                }
                """;
        List<String> customFields = Collections.emptyList();

        // When
        FreeForm result = contentDao.parseJSONToMap(jsonResponse, customFields);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should handle numeric values in custom fields")
    void shouldHandleNumericValuesInCustomFields() {
        // Given
        String jsonResponse = """
                {
                    "records": [
                        {
                            "Amount__c": 12345.67,
                            "Count__c": 100
                        }
                    ]
                }
                """;
        List<String> customFields = Arrays.asList("Amount__c", "Count__c");

        // When
        FreeForm result = contentDao.parseJSONToMap(jsonResponse, customFields);

        // Then
        assertNotNull(result);
        assertEquals("12345.67", result.get("Amount__c"));
        assertEquals("100", result.get("Count__c"));
    }

    @Test
    @DisplayName("Should handle boolean values in custom fields")
    void shouldHandleBooleanValuesInCustomFields() {
        // Given
        String jsonResponse = """
                {
                    "records": [
                        {
                            "IsActive__c": true,
                            "IsDeleted__c": false
                        }
                    ]
                }
                """;
        List<String> customFields = Arrays.asList("IsActive__c", "IsDeleted__c");

        // When
        FreeForm result = contentDao.parseJSONToMap(jsonResponse, customFields);

        // Then
        assertNotNull(result);
        assertEquals("true", result.get("IsActive__c"));
        assertEquals("false", result.get("IsDeleted__c"));
    }

    @Test
    @DisplayName("Should handle special characters in field values")
    void shouldHandleSpecialCharactersInFieldValues() {
        // Given
        String jsonResponse = """
                {
                    "records": [
                        {
                            "Description__c": "Test with special chars: @#$%^&*()",
                            "Email__c": "test+user@example.com"
                        }
                    ]
                }
                """;
        List<String> customFields = Arrays.asList("Description__c", "Email__c");

        // When
        FreeForm result = contentDao.parseJSONToMap(jsonResponse, customFields);

        // Then
        assertNotNull(result);
        assertEquals("Test with special chars: @#$%^&*()", result.get("Description__c"));
        assertEquals("test+user@example.com", result.get("Email__c"));
    }

    @Test
    @DisplayName("Should handle malformed JSON gracefully")
    void shouldHandleMalformedJsonGracefully() {
        // Given
        String jsonResponse = "{ invalid json }";
        List<String> customFields = Arrays.asList("Field1__c");

        // When
        FreeForm result = contentDao.parseJSONToMap(jsonResponse, customFields);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should handle empty string values")
    void shouldHandleEmptyStringValues() {
        // Given
        String jsonResponse = """
                {
                    "records": [
                        {
                            "Field1__c": "",
                            "Field2__c": "Value2"
                        }
                    ]
                }
                """;
        List<String> customFields = Arrays.asList("Field1__c", "Field2__c");

        // When
        FreeForm result = contentDao.parseJSONToMap(jsonResponse, customFields);

        // Then
        assertNotNull(result);
        assertEquals("", result.get("Field1__c"));
        assertEquals("Value2", result.get("Field2__c"));
    }
}

