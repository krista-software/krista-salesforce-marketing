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

import com.github.scribejava.core.builder.api.DefaultApi20;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Custom Salesforce OAuth 2.0 API implementation that supports configurable Salesforce domains.
 * This class extends DefaultApi20 to provide OAuth 2.0 Client Credentials flow support
 * for Salesforce instances with custom domains or sandbox environments.
 *
 * <p>This implementation is used when an Instance URL is provided in the configuration,
 * enabling server-to-server authentication without user interaction.</p>
 *
 * @author Krista Extensions Team
 * @version 2.0.5
 * @since 2.0.4
 */
public class SalesforceCustomApi extends DefaultApi20 {

    private final String domain;

    /**
     * Constructs a new SalesforceCustomApi with the specified domain.
     *
     * @param domain The Salesforce instance domain (e.g., "https://mycompany.my.salesforce.com")
     * @throws IllegalArgumentException if the domain is null, empty, or not a valid URL
     */
    public SalesforceCustomApi(String domain) {
        if (domain == null || domain.trim().isEmpty()) {
            throw new IllegalArgumentException("Domain cannot be null or empty");
        }

        // Validate that the domain is a valid URL
        try {
            URI uri = new URI(domain.trim());

            // Validate that the URI is absolute (has a scheme)
            if (!uri.isAbsolute()) {
                throw new MalformedURLException("URI must be absolute with a scheme (e.g., https://)");
            }

            uri.toURL(); // Validate URL format
        } catch (URISyntaxException | MalformedURLException e) {
            throw new IllegalArgumentException("Invalid domain URL format: " + domain, e);
        }

        this.domain = domain.trim();
    }

    /**
     * Returns the OAuth 2.0 access token endpoint for the configured Salesforce domain.
     *
     * @return The complete URL for the OAuth 2.0 token endpoint
     */
    @Override
    public String getAccessTokenEndpoint() {
        return new StringBuilder(domain)
                .append("/services/oauth2/token")
                .toString();
    }

    /**
     * Returns the OAuth 2.0 authorization base URL for the configured Salesforce domain.
     *
     * @return The complete URL for the OAuth 2.0 authorization endpoint
     */
    @Override
    protected String getAuthorizationBaseUrl() {
        return new StringBuilder(domain)
                .append("/services/oauth2/authorize")
                .toString();
    }

    /**
     * Returns the configured domain for this API instance.
     *
     * @return The Salesforce instance domain
     */
    public String getDomain() {
        return domain;
    }
}
