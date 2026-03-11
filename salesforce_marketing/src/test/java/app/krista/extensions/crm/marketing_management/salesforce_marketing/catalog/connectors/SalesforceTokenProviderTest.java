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

package app.krista.extensions.crm.marketing_management.salesforce_marketing.catalog.connectors;

import app.krista.extensions.crm.marketing_management.salesforce_marketing.SalesforceMarketingAttributes;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.connectors.SalesforceTokenProvider;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for SalesforceTokenProvider covering all three authentication methods:
 * 1. OAuth 2.0 Client Credentials Flow (Instance URL provided)
 * 2. OAuth 2.0 Authorization Code Flow (Instance URL blank, uses refresh token)
 * 3. OAuth 2.0 Password Grant Flow (Username/Password/Security Token provided)
 *
 * Test credentials provided:
 * - Client ID: REDACTED_SALESFORCE_CONSUMER_KEY
 * - Client Secret: REDACTED_SALESFORCE_CONSUMER_SECRET
 * - Security Token: h63mvaB1DlqplIMAo057WMKMH
 * - Instance URL: https://ankit-ab-dev-ed.my.salesforce.com
 * - Username: hiren.panchal@kristasoft.com
 * - Password: Krista@602
 */
public class SalesforceTokenProviderTest {

    // Test credentials from the provided data
    private static final String CLIENT_ID = "REDACTED_SALESFORCE_CONSUMER_KEY";
    private static final String CLIENT_SECRET = "REDACTED_SALESFORCE_CONSUMER_SECRET";
    private static final String SECURITY_TOKEN = "h63mvaB1DlqplIMAo057WMKMH";
    private static final String INSTANCE_URL = "https://ankit-ab-dev-ed.my.salesforce.com";
    private static final String USERNAME = "hiren.panchal@kristasoft.com";
    private static final String PASSWORD = "Krista@602";
    private static final String ROUTING_URL = "https://test.example.com/callback";

    /**
     * Test OAuth 2.0 Client Credentials Flow authentication method selection.
     * This flow is triggered when Instance URL is provided.
     */
    @Test
    public void testClientCredentialsFlow_AuthenticationMethodSelection() {
        // Arrange - Client Credentials Flow (Instance URL provided)
        SalesforceMarketingAttributes attributes = new SalesforceMarketingAttributes(
            CLIENT_ID, CLIENT_SECRET, INSTANCE_URL, null, null, null, ROUTING_URL);

        // Act & Assert - Should use OAuth when Instance URL is provided
        assertTrue("Should use OAuth when Instance URL is provided",
                  SalesforceTokenProvider.validateUsingOAuth(false, attributes));


    }

    /**
     * Test OAuth 2.0 Authorization Code Flow authentication method selection.
     * This flow is triggered when Instance URL is blank and credentials are missing.
     */
    @Test
    public void testAuthorizationCodeFlow_AuthenticationMethodSelection() {
        // Arrange - Authorization Code Flow (Instance URL blank, missing credentials)
        SalesforceMarketingAttributes attributes = new SalesforceMarketingAttributes(
            CLIENT_ID, CLIENT_SECRET, "", null, null, null, ROUTING_URL);

        // Act & Assert - Should use OAuth when credentials are missing
        assertTrue("Should use OAuth when Instance URL is blank and credentials missing",
                  SalesforceTokenProvider.validateUsingOAuth(false, attributes));


    }

    /**
     * Test OAuth 2.0 Password Grant Flow authentication method selection.
     * This flow is triggered when all credentials (username, password, security token) are provided.
     */
    @Test
    public void testPasswordGrantFlow_AuthenticationMethodSelection() {
        // Arrange - Password Grant Flow (All credentials provided)
        SalesforceMarketingAttributes attributes = new SalesforceMarketingAttributes(
            CLIENT_ID, CLIENT_SECRET, null, USERNAME, PASSWORD, SECURITY_TOKEN, ROUTING_URL);

        // Act & Assert - Should NOT use OAuth when all credentials are provided
        assertFalse("Should use Password Grant when all credentials are provided",
                   SalesforceTokenProvider.validateUsingOAuth(false, attributes));


    }

    /**
     * Test authentication method selection in admin context.
     * Admin context should always use OAuth regardless of credentials.
     */
    @Test
    public void testValidateUsingOAuth_AdminContext() {
        // Arrange
        SalesforceMarketingAttributes attributes = new SalesforceMarketingAttributes(
            CLIENT_ID, CLIENT_SECRET, null, USERNAME, PASSWORD, SECURITY_TOKEN, ROUTING_URL);

        // Act & Assert - Admin context should always use OAuth
        assertTrue("Admin context should always use OAuth",
                  SalesforceTokenProvider.validateUsingOAuth(true, attributes));


    }

    /**
     * Test authentication method selection with missing credentials.
     * Missing any credential should trigger OAuth.
     */
    @Test
    public void testValidateUsingOAuth_MissingCredentials() {
        // Test missing username
        SalesforceMarketingAttributes attributes1 = new SalesforceMarketingAttributes(
            CLIENT_ID, CLIENT_SECRET, null, null, PASSWORD, SECURITY_TOKEN, ROUTING_URL);
        assertTrue("Missing username should trigger OAuth",
                  SalesforceTokenProvider.validateUsingOAuth(false, attributes1));

        // Test missing password
        SalesforceMarketingAttributes attributes2 = new SalesforceMarketingAttributes(
            CLIENT_ID, CLIENT_SECRET, null, USERNAME, null, SECURITY_TOKEN, ROUTING_URL);
        assertTrue("Missing password should trigger OAuth",
                  SalesforceTokenProvider.validateUsingOAuth(false, attributes2));

        // Test missing security token
        SalesforceMarketingAttributes attributes3 = new SalesforceMarketingAttributes(
            CLIENT_ID, CLIENT_SECRET, null, USERNAME, PASSWORD, null, ROUTING_URL);
        assertTrue("Missing security token should trigger OAuth",
                  SalesforceTokenProvider.validateUsingOAuth(false, attributes3));


    }

    /**
     * Test comprehensive authentication method selection scenarios.
     * This test covers all three authentication flows in one comprehensive test.
     */
    @Test
    public void testComprehensiveAuthenticationMethodSelection() {


        // Scenario 1: Client Credentials Flow (Instance URL provided)
        SalesforceMarketingAttributes clientCredentialsAttrs = new SalesforceMarketingAttributes(
            CLIENT_ID, CLIENT_SECRET, INSTANCE_URL, null, null, null, ROUTING_URL);

        assertTrue("Client Credentials Flow should use OAuth",
                  SalesforceTokenProvider.validateUsingOAuth(false, clientCredentialsAttrs));


        // Scenario 2: Authorization Code Flow (Instance URL blank, missing credentials)
        SalesforceMarketingAttributes authCodeAttrs = new SalesforceMarketingAttributes(
            CLIENT_ID, CLIENT_SECRET, "", null, null, null, ROUTING_URL);

        assertTrue("Authorization Code Flow should use OAuth",
                  SalesforceTokenProvider.validateUsingOAuth(false, authCodeAttrs));


        // Scenario 3: Password Grant Flow (All credentials provided, no Instance URL)
        SalesforceMarketingAttributes passwordGrantAttrs = new SalesforceMarketingAttributes(
            CLIENT_ID, CLIENT_SECRET, null, USERNAME, PASSWORD, SECURITY_TOKEN, ROUTING_URL);

        assertFalse("Password Grant Flow should NOT use OAuth",
                   SalesforceTokenProvider.validateUsingOAuth(false, passwordGrantAttrs));



    }

    /**
     * Test edge cases and boundary conditions for authentication method selection.
     */
    @Test
    public void testAuthenticationMethodEdgeCases() {


        // Edge Case 1: Empty strings vs null
        SalesforceMarketingAttributes attributes1 = new SalesforceMarketingAttributes(
            CLIENT_ID, CLIENT_SECRET, null, "", "", "", ROUTING_URL);
        assertTrue("Empty strings should trigger OAuth",
                  SalesforceTokenProvider.validateUsingOAuth(false, attributes1));


        // Edge Case 2: Whitespace-only strings
        SalesforceMarketingAttributes attributes2 = new SalesforceMarketingAttributes(
            CLIENT_ID, CLIENT_SECRET, null, "   ", "   ", "   ", ROUTING_URL);
        assertTrue("Whitespace-only strings should trigger OAuth",
                  SalesforceTokenProvider.validateUsingOAuth(false, attributes2));


        // Edge Case 3: Mixed valid and invalid credentials
        SalesforceMarketingAttributes attributes3 = new SalesforceMarketingAttributes(
            CLIENT_ID, CLIENT_SECRET, null, USERNAME, "", SECURITY_TOKEN, ROUTING_URL);
        assertTrue("Mixed credentials should trigger OAuth",
                  SalesforceTokenProvider.validateUsingOAuth(false, attributes3));
    }

    /**
     * Test admin operations always use OAuth regardless of other credentials.
     */
    @Test
    public void testAdminOperationsAlwaysUseOAuth() {
        // Admin with complete password credentials should still use OAuth
        SalesforceMarketingAttributes completePasswordAttrs = new SalesforceMarketingAttributes(
            CLIENT_ID, CLIENT_SECRET, null, USERNAME, PASSWORD, SECURITY_TOKEN, ROUTING_URL);

        assertTrue("Admin operations should always use OAuth even with complete password credentials",
                  SalesforceTokenProvider.validateUsingOAuth(true, completePasswordAttrs));

        // Admin with instance URL should use OAuth
        SalesforceMarketingAttributes instanceUrlAttrs = new SalesforceMarketingAttributes(
            CLIENT_ID, CLIENT_SECRET, INSTANCE_URL, null, null, null, ROUTING_URL);

        assertTrue("Admin operations should always use OAuth with instance URL",
                  SalesforceTokenProvider.validateUsingOAuth(true, instanceUrlAttrs));

        // Admin with no credentials should use OAuth
        SalesforceMarketingAttributes noCredsAttrs = new SalesforceMarketingAttributes(
            CLIENT_ID, CLIENT_SECRET, null, null, null, null, ROUTING_URL);

        assertTrue("Admin operations should always use OAuth with no credentials",
                  SalesforceTokenProvider.validateUsingOAuth(true, noCredsAttrs));
    }

    /**
     * Test instance URL precedence over password credentials.
     */
    @Test
    public void testInstanceUrlPrecedence() {
        // Instance URL with complete password credentials should use OAuth (Client Credentials)
        SalesforceMarketingAttributes mixedAttrs = new SalesforceMarketingAttributes(
            CLIENT_ID, CLIENT_SECRET, INSTANCE_URL, USERNAME, PASSWORD, SECURITY_TOKEN, ROUTING_URL);

        assertTrue("Instance URL should take precedence over password credentials",
                  SalesforceTokenProvider.validateUsingOAuth(false, mixedAttrs));

        // Instance URL with incomplete password credentials should use OAuth
        SalesforceMarketingAttributes incompleteWithUrlAttrs = new SalesforceMarketingAttributes(
            CLIENT_ID, CLIENT_SECRET, INSTANCE_URL, USERNAME, null, SECURITY_TOKEN, ROUTING_URL);

        assertTrue("Instance URL should take precedence over incomplete password credentials",
                  SalesforceTokenProvider.validateUsingOAuth(false, incompleteWithUrlAttrs));

        // Whitespace instance URL should not take precedence
        SalesforceMarketingAttributes whitespaceUrlAttrs = new SalesforceMarketingAttributes(
            CLIENT_ID, CLIENT_SECRET, "   ", USERNAME, PASSWORD, SECURITY_TOKEN, ROUTING_URL);

        assertFalse("Whitespace instance URL should not take precedence",
                   SalesforceTokenProvider.validateUsingOAuth(false, whitespaceUrlAttrs));
    }

    /**
     * Test various incomplete credential combinations.
     */
    @Test
    public void testIncompleteCredentialCombinations() {
        // Only username provided
        SalesforceMarketingAttributes usernameOnlyAttrs = new SalesforceMarketingAttributes(
            CLIENT_ID, CLIENT_SECRET, null, USERNAME, null, null, ROUTING_URL);

        assertTrue("Username-only should use OAuth",
                  SalesforceTokenProvider.validateUsingOAuth(false, usernameOnlyAttrs));

        // Only password provided
        SalesforceMarketingAttributes passwordOnlyAttrs = new SalesforceMarketingAttributes(
            CLIENT_ID, CLIENT_SECRET, null, null, PASSWORD, null, ROUTING_URL);

        assertTrue("Password-only should use OAuth",
                  SalesforceTokenProvider.validateUsingOAuth(false, passwordOnlyAttrs));

        // Only security token provided
        SalesforceMarketingAttributes tokenOnlyAttrs = new SalesforceMarketingAttributes(
            CLIENT_ID, CLIENT_SECRET, null, null, null, SECURITY_TOKEN, ROUTING_URL);

        assertTrue("Security token-only should use OAuth",
                  SalesforceTokenProvider.validateUsingOAuth(false, tokenOnlyAttrs));

        // Username and password but no security token
        SalesforceMarketingAttributes noTokenAttrs = new SalesforceMarketingAttributes(
            CLIENT_ID, CLIENT_SECRET, null, USERNAME, PASSWORD, null, ROUTING_URL);

        assertTrue("Missing security token should use OAuth",
                  SalesforceTokenProvider.validateUsingOAuth(false, noTokenAttrs));

        // Username and security token but no password
        SalesforceMarketingAttributes noPasswordAttrs = new SalesforceMarketingAttributes(
            CLIENT_ID, CLIENT_SECRET, null, USERNAME, null, SECURITY_TOKEN, ROUTING_URL);

        assertTrue("Missing password should use OAuth",
                  SalesforceTokenProvider.validateUsingOAuth(false, noPasswordAttrs));

        // Password and security token but no username
        SalesforceMarketingAttributes noUsernameAttrs = new SalesforceMarketingAttributes(
            CLIENT_ID, CLIENT_SECRET, null, null, PASSWORD, SECURITY_TOKEN, ROUTING_URL);

        assertTrue("Missing username should use OAuth",
                  SalesforceTokenProvider.validateUsingOAuth(false, noUsernameAttrs));
    }
}
