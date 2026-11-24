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

import app.krista.extension.authorization.RequestAuthenticator;
import app.krista.extension.executor.Invoker;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.connectors.SalesforceTokenProvider;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.connectors.SalesforceTokenProviderFactory;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.stores.SalesforceMarketingAttributesStore;
import com.github.scribejava.apis.salesforce.SalesforceToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for SalesforceMarketingExtension class.
 * Tests all public methods including connection validation, authentication, and configuration management.
 *
 * @author Krista Extensions Team
 * @version 2.0.5
 */
@ExtendWith(MockitoExtension.class)
class SalesforceMarketingExtensionTest {

    // Test constants
    private static final String VALID_CLIENT_ID = "3MVG95jctIhbyCprtzHqvP5ePGclhjUompupjYnVlmU84hNMeafFnbvpLnRVq_RM5.cije9EY_1yqmYBDr3KY";
    private static final String VALID_CLIENT_SECRET = "REDACTED_SECRET";
    private static final String VALID_INSTANCE_URL = "https://mycompany.my.salesforce.com";
    private static final String VALID_USERNAME = "test@example.com";
    private static final String VALID_PASSWORD = "password123";
    private static final String VALID_SECURITY_TOKEN = "REDACTED_SECRET";

    @Mock
    private SalesforceTokenProviderFactory mockProviderFactory;

    @Mock
    private Invoker mockInvoker;

    @Mock
    private SalesforceMarketingAttributes mockSalesforceMarketingAttributes;

    @Mock
    private SalesforceMarketingAttributesStore mockAttributesStore;

    @Mock
    private SalesforceMarketingRequestAuthenticator mockRequestAuthenticator;

    @Mock
    private SalesforceTokenProvider mockTokenProvider;

    @InjectMocks
    private SalesforceMarketingExtension salesforceMarketingExtension;

    @BeforeEach
    void setUp() {
        // Create extension instance with mocked dependencies
        salesforceMarketingExtension = new SalesforceMarketingExtension(
            mockProviderFactory,
            mockInvoker,
            mockSalesforceMarketingAttributes,
            mockRequestAuthenticator
        );
    }

    @Test
    @DisplayName("Should create extension with injected constructor")
    void shouldCreateExtensionWithInjectedConstructor() {
        // Given & When
        SalesforceMarketingExtension extension = new SalesforceMarketingExtension(
            mockProviderFactory,
            mockInvoker,
            mockSalesforceMarketingAttributes,
            mockAttributesStore
        );

        // Then
        assertNotNull(extension);
        assertNotNull(extension.getRequestAuthenticator());
    }

    @Test
    @DisplayName("Should return correct custom tabs configuration")
    void shouldReturnCorrectCustomTabs() {
        // Given & When
        Map<String, String> customTabs = salesforceMarketingExtension.customTabs();

        // Then
        assertNotNull(customTabs);
        assertEquals(1, customTabs.size());
        assertEquals("static/docs", customTabs.get("Documentation"));
    }

    @Test
    @DisplayName("Should return request authenticator")
    void shouldReturnRequestAuthenticator() {
        // Given & When
        RequestAuthenticator authenticator = salesforceMarketingExtension.getRequestAuthenticator();

        // Then
        assertNotNull(authenticator);
        assertEquals(mockRequestAuthenticator, authenticator);
    }

    @Test
    @DisplayName("Should test connection with OAuth credentials successfully")
    void shouldTestConnectionWithOAuthCredentials() throws IOException {
        // Given - Only stub what's actually called (first null check triggers OAuth flow)
        SalesforceToken mockToken = mock(SalesforceToken.class);
        when(mockSalesforceMarketingAttributes.getUserName()).thenReturn(null);
        when(mockProviderFactory.create(mockSalesforceMarketingAttributes)).thenReturn(mockTokenProvider);
        when(mockTokenProvider.getSalesforceToken()).thenReturn(mockToken);

        // When
        salesforceMarketingExtension.testConnection();

        // Then
        verify(mockProviderFactory).create(mockSalesforceMarketingAttributes);
        verify(mockTokenProvider).getSalesforceToken();
        verify(mockTokenProvider, never()).getSalesforceTokenForAdmin();
    }

    @Test
    @DisplayName("Should test connection with password credentials successfully")
    void shouldTestConnectionWithPasswordCredentials() throws IOException {
        // Given
        when(mockSalesforceMarketingAttributes.getUserName()).thenReturn(VALID_USERNAME);
        when(mockSalesforceMarketingAttributes.getPassword()).thenReturn(VALID_PASSWORD);
        when(mockSalesforceMarketingAttributes.getSecurityToken()).thenReturn(VALID_SECURITY_TOKEN);
        when(mockProviderFactory.create(mockSalesforceMarketingAttributes)).thenReturn(mockTokenProvider);
        doNothing().when(mockTokenProvider).getSalesforceTokenForAdmin();

        // When
        salesforceMarketingExtension.testConnection();

        // Then
        verify(mockProviderFactory).create(mockSalesforceMarketingAttributes);
        verify(mockTokenProvider).getSalesforceTokenForAdmin();
        verify(mockTokenProvider, never()).getSalesforceToken();
    }

    @Test
    @DisplayName("Should handle IOException during test connection")
    void shouldHandleIOExceptionDuringTestConnection() throws IOException {
        // Given
        when(mockProviderFactory.create(mockSalesforceMarketingAttributes)).thenReturn(mockTokenProvider);
        when(mockTokenProvider.getSalesforceToken()).thenThrow(new IOException("Connection failed"));

        // When & Then
        assertThrows(IOException.class, () -> salesforceMarketingExtension.testConnection());
        verify(mockProviderFactory).create(mockSalesforceMarketingAttributes);
        verify(mockTokenProvider).getSalesforceToken();
    }

    @Test
    @DisplayName("Should test connection with empty credentials")
    void shouldTestConnectionWithEmptyCredentials() throws IOException {
        // Given - Empty string triggers OAuth flow (first check: username.isBlank())
        SalesforceToken mockToken = mock(SalesforceToken.class);
        when(mockSalesforceMarketingAttributes.getUserName()).thenReturn("");
        when(mockProviderFactory.create(mockSalesforceMarketingAttributes)).thenReturn(mockTokenProvider);
        when(mockTokenProvider.getSalesforceToken()).thenReturn(mockToken);

        // When
        salesforceMarketingExtension.testConnection();

        // Then
        verify(mockProviderFactory).create(mockSalesforceMarketingAttributes);
        verify(mockTokenProvider).getSalesforceToken();
        verify(mockTokenProvider, never()).getSalesforceTokenForAdmin();
    }

    @Test
    @DisplayName("Should test connection with whitespace credentials")
    void shouldTestConnectionWithWhitespaceCredentials() throws IOException {
        // Given - Whitespace triggers OAuth flow (first check: username.isBlank())
        SalesforceToken mockToken = mock(SalesforceToken.class);
        when(mockSalesforceMarketingAttributes.getUserName()).thenReturn("   ");
        when(mockProviderFactory.create(mockSalesforceMarketingAttributes)).thenReturn(mockTokenProvider);
        when(mockTokenProvider.getSalesforceToken()).thenReturn(mockToken);

        // When
        salesforceMarketingExtension.testConnection();

        // Then
        verify(mockProviderFactory).create(mockSalesforceMarketingAttributes);
        verify(mockTokenProvider).getSalesforceToken();
        verify(mockTokenProvider, never()).getSalesforceTokenForAdmin();
    }

    @Test
    @DisplayName("Should update attributes successfully")
    void shouldUpdateAttributesSuccessfully() {
        // Given
        Map<String, Object> oldAttributes = createOAuthConnectionAttributes();
        Map<String, Object> newAttributes = createPasswordConnectionAttributes();

        doNothing().when(mockSalesforceMarketingAttributes).update(newAttributes);

        // When
        salesforceMarketingExtension.attributesUpdated(oldAttributes, newAttributes);

        // Then
        verify(mockSalesforceMarketingAttributes).update(newAttributes);
    }

    @Test
    @DisplayName("Should handle partial password credentials - missing username")
    void shouldHandlePartialPasswordCredentialsMissingUsername() throws IOException {
        // Given - Null username triggers OAuth flow (first check fails)
        SalesforceToken mockToken = mock(SalesforceToken.class);
        when(mockSalesforceMarketingAttributes.getUserName()).thenReturn(null);
        when(mockProviderFactory.create(mockSalesforceMarketingAttributes)).thenReturn(mockTokenProvider);
        when(mockTokenProvider.getSalesforceToken()).thenReturn(mockToken);

        // When
        salesforceMarketingExtension.testConnection();

        // Then
        verify(mockProviderFactory).create(mockSalesforceMarketingAttributes);
        verify(mockTokenProvider).getSalesforceToken();
        verify(mockTokenProvider, never()).getSalesforceTokenForAdmin();
    }

    @Test
    @DisplayName("Should handle partial password credentials - missing password")
    void shouldHandlePartialPasswordCredentialsMissingPassword() throws IOException {
        // Given - Valid username but null password triggers OAuth flow (second check fails)
        SalesforceToken mockToken = mock(SalesforceToken.class);
        when(mockSalesforceMarketingAttributes.getUserName()).thenReturn(VALID_USERNAME);
        when(mockSalesforceMarketingAttributes.getPassword()).thenReturn(null);
        when(mockProviderFactory.create(mockSalesforceMarketingAttributes)).thenReturn(mockTokenProvider);
        when(mockTokenProvider.getSalesforceToken()).thenReturn(mockToken);

        // When
        salesforceMarketingExtension.testConnection();

        // Then
        verify(mockProviderFactory).create(mockSalesforceMarketingAttributes);
        verify(mockTokenProvider).getSalesforceToken();
        verify(mockTokenProvider, never()).getSalesforceTokenForAdmin();
    }

    @Test
    @DisplayName("Should handle partial password credentials - missing security token")
    void shouldHandlePartialPasswordCredentialsMissingSecurityToken() throws IOException {
        // Given
        SalesforceToken mockToken = mock(SalesforceToken.class);
        when(mockSalesforceMarketingAttributes.getUserName()).thenReturn(VALID_USERNAME);
        when(mockSalesforceMarketingAttributes.getPassword()).thenReturn(VALID_PASSWORD);
        when(mockSalesforceMarketingAttributes.getSecurityToken()).thenReturn(null);
        when(mockProviderFactory.create(mockSalesforceMarketingAttributes)).thenReturn(mockTokenProvider);
        when(mockTokenProvider.getSalesforceToken()).thenReturn(mockToken);

        // When
        salesforceMarketingExtension.testConnection();

        // Then
        verify(mockProviderFactory).create(mockSalesforceMarketingAttributes);
        verify(mockTokenProvider).getSalesforceToken();
        verify(mockTokenProvider, never()).getSalesforceTokenForAdmin();
    }

    // Helper methods for creating test data
    private Map<String, Object> createOAuthConnectionAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(SalesforceMarketingAttributes.CLIENT_ID, VALID_CLIENT_ID);
        attributes.put(SalesforceMarketingAttributes.CLIENT_SECRET, VALID_CLIENT_SECRET);
        attributes.put(SalesforceMarketingAttributes.INSTANCE_URL, VALID_INSTANCE_URL);
        return attributes;
    }

    private Map<String, Object> createPasswordConnectionAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(SalesforceMarketingAttributes.CLIENT_ID, VALID_CLIENT_ID);
        attributes.put(SalesforceMarketingAttributes.CLIENT_SECRET, VALID_CLIENT_SECRET);
        attributes.put(SalesforceMarketingAttributes.USER_NAME, VALID_USERNAME);
        attributes.put(SalesforceMarketingAttributes.PASSWORD, VALID_PASSWORD);
        attributes.put(SalesforceMarketingAttributes.SECURITY_TOKEN, VALID_SECURITY_TOKEN);
        return attributes;
    }
}
