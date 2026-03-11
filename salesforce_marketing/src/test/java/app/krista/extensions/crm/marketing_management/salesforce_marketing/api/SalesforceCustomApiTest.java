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

package app.krista.extensions.crm.marketing_management.salesforce_marketing.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SalesforceCustomApi class.
 * Tests domain validation, URL construction, and error handling.
 */
class SalesforceCustomApiTest {

    private static final String VALID_PRODUCTION_DOMAIN = "https://mycompany.my.salesforce.com";
    private static final String VALID_SANDBOX_DOMAIN = "https://mycompany--sandbox.sandbox.my.salesforce.com";
    private static final String VALID_DEVELOPER_DOMAIN = "https://mycompany-dev-ed.develop.my.salesforce.com";

    @Test
    @DisplayName("Should create SalesforceCustomApi with valid production domain")
    void shouldCreateWithValidProductionDomain() {
        // Given & When
        SalesforceCustomApi api = new SalesforceCustomApi(VALID_PRODUCTION_DOMAIN);

        // Then
        assertNotNull(api);
        assertEquals(VALID_PRODUCTION_DOMAIN, api.getDomain());
        assertEquals(VALID_PRODUCTION_DOMAIN + "/services/oauth2/token", api.getAccessTokenEndpoint());
        assertEquals(VALID_PRODUCTION_DOMAIN + "/services/oauth2/authorize", api.getAuthorizationBaseUrl());
    }

    @Test
    @DisplayName("Should create SalesforceCustomApi with valid sandbox domain")
    void shouldCreateWithValidSandboxDomain() {
        // Given & When
        SalesforceCustomApi api = new SalesforceCustomApi(VALID_SANDBOX_DOMAIN);

        // Then
        assertNotNull(api);
        assertEquals(VALID_SANDBOX_DOMAIN, api.getDomain());
        assertEquals(VALID_SANDBOX_DOMAIN + "/services/oauth2/token", api.getAccessTokenEndpoint());
        assertEquals(VALID_SANDBOX_DOMAIN + "/services/oauth2/authorize", api.getAuthorizationBaseUrl());
    }

    @Test
    @DisplayName("Should create SalesforceCustomApi with valid developer domain")
    void shouldCreateWithValidDeveloperDomain() {
        // Given & When
        SalesforceCustomApi api = new SalesforceCustomApi(VALID_DEVELOPER_DOMAIN);

        // Then
        assertNotNull(api);
        assertEquals(VALID_DEVELOPER_DOMAIN, api.getDomain());
        assertEquals(VALID_DEVELOPER_DOMAIN + "/services/oauth2/token", api.getAccessTokenEndpoint());
        assertEquals(VALID_DEVELOPER_DOMAIN + "/services/oauth2/authorize", api.getAuthorizationBaseUrl());
    }

    @Test
    @DisplayName("Should trim whitespace from domain")
    void shouldTrimWhitespaceFromDomain() {
        // Given
        String domainWithWhitespace = "  " + VALID_PRODUCTION_DOMAIN + "  ";

        // When
        SalesforceCustomApi api = new SalesforceCustomApi(domainWithWhitespace);

        // Then
        assertEquals(VALID_PRODUCTION_DOMAIN, api.getDomain());
        assertEquals(VALID_PRODUCTION_DOMAIN + "/services/oauth2/token", api.getAccessTokenEndpoint());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Should throw IllegalArgumentException for null, empty, or whitespace-only domains")
    void shouldThrowExceptionForInvalidDomains(String invalidDomain) {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new SalesforceCustomApi(invalidDomain)
        );
        assertEquals("Domain cannot be null or empty", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "not-a-url",
        "://missing-protocol.com"
    })
    @DisplayName("Should throw IllegalArgumentException for malformed URLs")
    void shouldThrowExceptionForMalformedUrls(String malformedUrl) {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new SalesforceCustomApi(malformedUrl)
        );
        assertTrue(exception.getMessage().startsWith("Invalid domain URL format:"));
        assertNotNull(exception.getCause());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "https://example.com",
        "https://google.com",
        "https://not-salesforce.org"
    })
    @DisplayName("Should accept non-Salesforce domains but log warning")
    void shouldAcceptNonSalesforceDomains(String nonSalesforceDomain) {
        // Given & When
        // Note: This test verifies that non-Salesforce domains are accepted
        // The warning is logged but doesn't prevent object creation
        SalesforceCustomApi api = new SalesforceCustomApi(nonSalesforceDomain);

        // Then
        assertNotNull(api);
        assertEquals(nonSalesforceDomain, api.getDomain());
        assertEquals(nonSalesforceDomain + "/services/oauth2/token", api.getAccessTokenEndpoint());
        assertEquals(nonSalesforceDomain + "/services/oauth2/authorize", api.getAuthorizationBaseUrl());
    }

    @Test
    @DisplayName("Should handle HTTPS URLs correctly")
    void shouldHandleHttpsUrls() {
        // Given
        String httpsUrl = "https://secure.salesforce.com";

        // When
        SalesforceCustomApi api = new SalesforceCustomApi(httpsUrl);

        // Then
        assertEquals(httpsUrl, api.getDomain());
        assertEquals("https://secure.salesforce.com/services/oauth2/token", api.getAccessTokenEndpoint());
        assertEquals("https://secure.salesforce.com/services/oauth2/authorize", api.getAuthorizationBaseUrl());
    }

    @Test
    @DisplayName("Should handle HTTP URLs correctly")
    void shouldHandleHttpUrls() {
        // Given
        String httpUrl = "http://localhost:8080";

        // When
        SalesforceCustomApi api = new SalesforceCustomApi(httpUrl);

        // Then
        assertEquals(httpUrl, api.getDomain());
        assertEquals("http://localhost:8080/services/oauth2/token", api.getAccessTokenEndpoint());
        assertEquals("http://localhost:8080/services/oauth2/authorize", api.getAuthorizationBaseUrl());
    }

    @Test
    @DisplayName("Should handle domains with ports")
    void shouldHandleDomainsWithPorts() {
        // Given
        String domainWithPort = "https://mycompany.my.salesforce.com:8443";

        // When
        SalesforceCustomApi api = new SalesforceCustomApi(domainWithPort);

        // Then
        assertEquals(domainWithPort, api.getDomain());
        assertEquals(domainWithPort + "/services/oauth2/token", api.getAccessTokenEndpoint());
        assertEquals(domainWithPort + "/services/oauth2/authorize", api.getAuthorizationBaseUrl());
    }

    @Test
    @DisplayName("Should handle domains with paths (though not typical for Salesforce)")
    void shouldHandleDomainsWithPaths() {
        // Given
        String domainWithPath = "https://mycompany.my.salesforce.com/path";

        // When
        SalesforceCustomApi api = new SalesforceCustomApi(domainWithPath);

        // Then
        assertEquals(domainWithPath, api.getDomain());
        assertEquals(domainWithPath + "/services/oauth2/token", api.getAccessTokenEndpoint());
        assertEquals(domainWithPath + "/services/oauth2/authorize", api.getAuthorizationBaseUrl());
    }

    @Test
    @DisplayName("Should use StringBuilder for URL construction (performance optimization)")
    void shouldUseStringBuilderForUrlConstruction() {
        // Given
        SalesforceCustomApi api = new SalesforceCustomApi(VALID_PRODUCTION_DOMAIN);

        // When
        String tokenEndpoint = api.getAccessTokenEndpoint();
        String authEndpoint = api.getAuthorizationBaseUrl();

        // Then
        // Verify that the URLs are constructed correctly (StringBuilder usage is internal)
        assertEquals(VALID_PRODUCTION_DOMAIN + "/services/oauth2/token", tokenEndpoint);
        assertEquals(VALID_PRODUCTION_DOMAIN + "/services/oauth2/authorize", authEndpoint);
        
        // Verify multiple calls return consistent results
        assertEquals(tokenEndpoint, api.getAccessTokenEndpoint());
        assertEquals(authEndpoint, api.getAuthorizationBaseUrl());
    }
}
