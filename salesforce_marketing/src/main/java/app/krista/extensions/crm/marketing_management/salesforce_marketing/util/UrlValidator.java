package app.krista.extensions.crm.marketing_management.salesforce_marketing.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Utility class for URL validation operations.
 * Provides centralized URL validation logic to avoid code duplication.
 *
 * @since 1.0
 */
public final class UrlValidator {

    /**
     * Validates that a URL string is properly formatted and absolute.
     * 
     * <p>This method performs the following validations:
     * <ul>
     *   <li>Parses the URL string as a URI</li>
     *   <li>Ensures the URI is absolute (has a scheme like https://)</li>
     *   <li>Converts to URL and validates format</li>
     *   <li>Ensures the URL has a valid host</li>
     * </ul>
     *
     * @param urlString The URL string to validate
     * @return A validated URL object
     * @throws URISyntaxException if the URL string violates RFC 2396
     * @throws MalformedURLException if the URL is not absolute or lacks a valid host
     */
    public static URL validateAbsoluteUrl(String urlString) throws URISyntaxException, MalformedURLException {
        URI uri = new URI(urlString);
        
        // Validate that the URI is absolute (has a scheme)
        if (!uri.isAbsolute()) {
            throw new MalformedURLException("URI must be absolute with a scheme (e.g., https://)");
        }
        
        URL url = uri.toURL();
        
        // Validate that the URL has a valid host
        if (url.getHost() == null || url.getHost().isEmpty()) {
            throw new MalformedURLException("URL must have a valid host");
        }
        
        return url;
    }

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with only static methods.
     */
    private UrlValidator() {
        throw new AssertionError("Utility class should not be instantiated");
    }
}

