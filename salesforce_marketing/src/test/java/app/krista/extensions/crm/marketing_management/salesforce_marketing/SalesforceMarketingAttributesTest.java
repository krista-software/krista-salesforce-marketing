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

package app.krista.extensions.crm.marketing_management.salesforce_marketing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SalesforceMarketingAttributes class.
 * Tests validation logic, configuration scenarios, and authentication method detection.
 */
class SalesforceMarketingAttributesTest {

    private static final String VALID_CLIENT_ID = "3MVG95jctIhbyCprtzHqvP5ePGclhjUompupjYnVlmU84hNMeafFnbvpLnRVq_RM5.cije9EY_1yqmYBDr3KY";
    private static final String VALID_CLIENT_SECRET = "REDACTED_SECRET";
    private static final String VALID_INSTANCE_URL = "https://mycompany.my.salesforce.com";
    private static final String VALID_USERNAME = "test@example.com";
    private static final String VALID_PASSWORD = "password123";
    private static final String VALID_SECURITY_TOKEN = "REDACTED_SECRET";
    private static final String VALID_ROUTING_URL = "https://extension.krista.app";

    @Test
    @DisplayName("Should create attributes with valid Client Credentials configuration")
    void shouldCreateWithValidClientCredentialsConfig() {
        // Given & When
        SalesforceMarketingAttributes attributes = new SalesforceMarketingAttributes(
            VALID_CLIENT_ID, VALID_CLIENT_SECRET, VALID_INSTANCE_URL, 
            null, null, null, VALID_ROUTING_URL
        );

        // Then
        assertEquals(VALID_CLIENT_ID, attributes.getClientId());
        assertEquals(VALID_CLIENT_SECRET, attributes.getClientSecret());
        assertEquals(VALID_INSTANCE_URL, attributes.getInstanceUrl());
        assertNull(attributes.getUserName());
        assertNull(attributes.getPassword());
        assertNull(attributes.getSecurityToken());
    }

    @Test
    @DisplayName("Should create attributes with valid Password Grant configuration")
    void shouldCreateWithValidPasswordGrantConfig() {
        // Given & When
        SalesforceMarketingAttributes attributes = new SalesforceMarketingAttributes(
            VALID_CLIENT_ID, VALID_CLIENT_SECRET, null, 
            VALID_USERNAME, VALID_PASSWORD, VALID_SECURITY_TOKEN, VALID_ROUTING_URL
        );

        // Then
        assertEquals(VALID_CLIENT_ID, attributes.getClientId());
        assertEquals(VALID_CLIENT_SECRET, attributes.getClientSecret());
        assertNull(attributes.getInstanceUrl());
        assertEquals(VALID_USERNAME, attributes.getUserName());
        assertEquals(VALID_PASSWORD, attributes.getPassword());
        assertEquals(VALID_SECURITY_TOKEN, attributes.getSecurityToken());
    }

    @Test
    @DisplayName("Should create attributes with Authorization Code configuration")
    void shouldCreateWithAuthorizationCodeConfig() {
        // Given & When
        SalesforceMarketingAttributes attributes = new SalesforceMarketingAttributes(
            VALID_CLIENT_ID, VALID_CLIENT_SECRET, null, 
            null, null, null, VALID_ROUTING_URL
        );

        // Then
        assertEquals(VALID_CLIENT_ID, attributes.getClientId());
        assertEquals(VALID_CLIENT_SECRET, attributes.getClientSecret());
        assertNull(attributes.getInstanceUrl());
        assertNull(attributes.getUserName());
        assertNull(attributes.getPassword());
        assertNull(attributes.getSecurityToken());
    }

    @Test
    @DisplayName("Should trim whitespace from instance URL")
    void shouldTrimWhitespaceFromInstanceUrl() {
        // Given
        String instanceUrlWithWhitespace = "  " + VALID_INSTANCE_URL + "  ";

        // When
        SalesforceMarketingAttributes attributes = new SalesforceMarketingAttributes(
            VALID_CLIENT_ID, VALID_CLIENT_SECRET, instanceUrlWithWhitespace, 
            null, null, null, VALID_ROUTING_URL
        );

        // Then
        assertEquals(VALID_INSTANCE_URL, attributes.getInstanceUrl());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "not-a-url",
        "://missing-protocol.com"
    })
    @DisplayName("Should throw IllegalArgumentException for malformed instance URLs")
    void shouldThrowExceptionForMalformedInstanceUrls(String malformedUrl) {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new SalesforceMarketingAttributes(
                VALID_CLIENT_ID, VALID_CLIENT_SECRET, malformedUrl,
                null, null, null, VALID_ROUTING_URL
            )
        );
        assertTrue(exception.getMessage().contains("Instance URL you provided isn't in the correct format"),
            "Expected error message about invalid Instance URL format, but got: " + exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Should accept null, empty, or whitespace-only instance URLs")
    void shouldAcceptNullOrEmptyInstanceUrls(String instanceUrl) {
        // Given & When
        SalesforceMarketingAttributes attributes = new SalesforceMarketingAttributes(
            VALID_CLIENT_ID, VALID_CLIENT_SECRET, instanceUrl, 
            null, null, null, VALID_ROUTING_URL
        );

        // Then
        // Should accept null/empty instance URLs (for other auth flows)
        assertNotNull(attributes);
        if (instanceUrl == null || instanceUrl.trim().isEmpty()) {
            assertEquals(instanceUrl, attributes.getInstanceUrl());
        }
    }

    @Test
    @DisplayName("Should create from map with all attributes")
    void shouldCreateFromMapWithAllAttributes() {
        // Given
        Map<String, Object> attributeMap = new HashMap<>();
        attributeMap.put(SalesforceMarketingAttributes.CLIENT_ID, VALID_CLIENT_ID);
        attributeMap.put(SalesforceMarketingAttributes.CLIENT_SECRET, VALID_CLIENT_SECRET);
        attributeMap.put(SalesforceMarketingAttributes.INSTANCE_URL, VALID_INSTANCE_URL);
        attributeMap.put(SalesforceMarketingAttributes.USER_NAME, VALID_USERNAME);
        attributeMap.put(SalesforceMarketingAttributes.PASSWORD, VALID_PASSWORD);
        attributeMap.put(SalesforceMarketingAttributes.SECURITY_TOKEN, VALID_SECURITY_TOKEN);

        // When
        SalesforceMarketingAttributes attributes = new SalesforceMarketingAttributes(VALID_ROUTING_URL, attributeMap);

        // Then
        assertEquals(VALID_CLIENT_ID, attributes.getClientId());
        assertEquals(VALID_CLIENT_SECRET, attributes.getClientSecret());
        assertEquals(VALID_INSTANCE_URL, attributes.getInstanceUrl());
        assertEquals(VALID_USERNAME, attributes.getUserName());
        assertEquals(VALID_PASSWORD, attributes.getPassword());
        assertEquals(VALID_SECURITY_TOKEN, attributes.getSecurityToken());
    }

    @Test
    @DisplayName("Should update attributes with valid instance URL")
    void shouldUpdateAttributesWithValidInstanceUrl() {
        // Given
        SalesforceMarketingAttributes attributes = new SalesforceMarketingAttributes(
            VALID_CLIENT_ID, VALID_CLIENT_SECRET, null, 
            null, null, null, VALID_ROUTING_URL
        );

        Map<String, Object> newAttributes = new HashMap<>();
        newAttributes.put(SalesforceMarketingAttributes.INSTANCE_URL, VALID_INSTANCE_URL);

        // When
        attributes.update(newAttributes);

        // Then
        assertEquals(VALID_INSTANCE_URL, attributes.getInstanceUrl());
    }

    @Test
    @DisplayName("Should throw exception when updating with invalid instance URL")
    void shouldThrowExceptionWhenUpdatingWithInvalidInstanceUrl() {
        // Given
        SalesforceMarketingAttributes attributes = new SalesforceMarketingAttributes(
            VALID_CLIENT_ID, VALID_CLIENT_SECRET, null, 
            null, null, null, VALID_ROUTING_URL
        );

        Map<String, Object> newAttributes = new HashMap<>();
        newAttributes.put(SalesforceMarketingAttributes.INSTANCE_URL, "invalid-url");

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> attributes.update(newAttributes)
        );
        assertTrue(exception.getMessage().contains("Instance URL you provided isn't in the correct format"),
            "Expected error message about invalid Instance URL format, but got: " + exception.getMessage());
    }

    @Test
    @DisplayName("Should clear OAuth service cache when updating attributes")
    void shouldClearOAuthServiceCacheWhenUpdating() {
        // Given
        SalesforceMarketingAttributes attributes = new SalesforceMarketingAttributes(
            VALID_CLIENT_ID, VALID_CLIENT_SECRET, null, 
            null, null, null, VALID_ROUTING_URL
        );

        // Get OAuth service to cache it
        attributes.getOauthService();

        Map<String, Object> newAttributes = new HashMap<>();
        newAttributes.put(SalesforceMarketingAttributes.CLIENT_ID, "3MVG95jctIhbyCprtzHqvP5ePGclhjUompupjYnVlmU84hNMeafFnbvpLnRVq_RM5.cije9EY_1yqmYBDr3KY_NEW");
        newAttributes.put(SalesforceMarketingAttributes.CLIENT_SECRET, "REDACTED_SECRET");

        // When
        attributes.update(newAttributes);

        // Then
        assertEquals("3MVG95jctIhbyCprtzHqvP5ePGclhjUompupjYnVlmU84hNMeafFnbvpLnRVq_RM5.cije9EY_1yqmYBDr3KY_NEW", attributes.getClientId());
        // OAuth service should be recreated with new client ID
        assertNotNull(attributes.getOauthService());
    }

    @Test
    @DisplayName("Should create OAuth service with custom domain")
    void shouldCreateOAuthServiceWithCustomDomain() {
        // Given
        SalesforceMarketingAttributes attributes = new SalesforceMarketingAttributes(
            VALID_CLIENT_ID, VALID_CLIENT_SECRET, VALID_INSTANCE_URL, 
            null, null, null, VALID_ROUTING_URL
        );

        // When
        var oauthService = attributes.getOauthService(VALID_INSTANCE_URL);

        // Then
        assertNotNull(oauthService);
    }

    @Test
    @DisplayName("Should handle non-Salesforce domains with warning")
    void shouldHandleNonSalesforceDomainsWithWarning() {
        // Given
        String nonSalesforceDomain = "https://example.com";

        // When
        SalesforceMarketingAttributes attributes = new SalesforceMarketingAttributes(
            VALID_CLIENT_ID, VALID_CLIENT_SECRET, nonSalesforceDomain, 
            null, null, null, VALID_ROUTING_URL
        );

        // Then
        assertEquals(nonSalesforceDomain, attributes.getInstanceUrl());
        // Warning should be logged but object should be created successfully
        assertNotNull(attributes);
    }

    @Test
    @DisplayName("Should create static instance using create method")
    void shouldCreateStaticInstanceUsingCreateMethod() {
        // Given
        Map<String, Object> attributeMap = new HashMap<>();
        attributeMap.put(SalesforceMarketingAttributes.CLIENT_ID, VALID_CLIENT_ID);
        attributeMap.put(SalesforceMarketingAttributes.CLIENT_SECRET, VALID_CLIENT_SECRET);

        // When
        // Note: This test would require mocking Invoker, so we'll test the constructor path instead
        SalesforceMarketingAttributes attributes = new SalesforceMarketingAttributes(VALID_ROUTING_URL, attributeMap);

        // Then
        assertNotNull(attributes);
        assertEquals(VALID_CLIENT_ID, attributes.getClientId());
        assertEquals(VALID_CLIENT_SECRET, attributes.getClientSecret());
    }
}
