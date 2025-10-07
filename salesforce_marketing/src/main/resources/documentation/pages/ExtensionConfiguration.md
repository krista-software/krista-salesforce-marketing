# Extension Configuration

## Overview

The Salesforce Marketing Extension requires proper configuration to establish secure connections with your Salesforce org. This guide covers all configuration parameters, setup procedures, and troubleshooting steps to ensure successful integration.

## Configuration Parameters

The extension supports multiple authentication methods, automatically determined by the parameters you provide:

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| Client ID | Text | Yes | OAuth 2.0 client identifier from Connected App | "3MVG9A2kN3Bn17hs..." |
| Client Secret | Text | Yes | OAuth 2.0 client secret from Connected App | "1234567890123456789" |
| Instance URL | Text | Conditional | Salesforce org base domain (triggers Client Credentials Flow) | "https://mydomain.my.salesforce.com" |
| Username | Text | Conditional | Salesforce username (for Password Grant Flow) | "user@company.com" |
| Password | Text | Conditional | Salesforce password (for Password Grant Flow) | "mypassword" |
| Security Token | Text | Conditional | Salesforce security token (for Password Grant Flow) | "AbCdEfGhIjKlMnOpQrSt" |
| Sandbox | Boolean | No | Set to true if connecting to Salesforce Sandbox | true/false |
| Allow Retry | Boolean | No | Enable automatic retry for failed requests | true/false |

![Salesforce Marketing Extension Setup Interface](../_media/ExtensionConfiguration_SalesforceMarketingSetup.png)

## Step-by-Step Setup

### Step 1: Determine Authentication Method

The extension automatically selects the authentication method based on your configuration:

**Option A: OAuth 2.0 Authorization Code Flow** (Recommended for user interactions)
- Provide: `Client ID` + `Client Secret`
- Leave blank: `Instance URL`

**Option B: OAuth 2.0 Client Credentials Flow** (Recommended for server-to-server)
- Provide: `Client ID` + `Client Secret` + `Instance URL`

**Option C: OAuth 2.0 Password Grant Flow** (For development/testing)
- Provide: `Client ID` + `Client Secret` + `Username` + `Password` + `Security Token`

### Step 2: Configure Connection Attributes

1. Navigate to your Krista platform
2. Go to the Salesforce Marketing Extension configuration
3. Enter the required parameters based on your chosen authentication method
4. Configure optional settings:
   - **Sandbox**: Check if connecting to a Salesforce Sandbox environment
   - **Allow Retry**: Enable to automatically retry failed requests

![Input Attributes](../_media/ExtensionConfiguration_InputAttributes.png)

### Step 3: Authentication Type Selection

#### Service Account Authentication (Client Credentials Flow)
- **When to use**: Automated processes, server-to-server integration
- **Required**: `Client ID` + `Client Secret` + `Instance URL`
- **Benefits**: No user interaction required, runs as designated service user

![Input Attributes for Service Account Authentication](../_media/ExtensionConfiguration_InputAttributes2.png)

#### User Account Authentication (Authorization Code Flow)
- **When to use**: Interactive applications, user-specific operations
- **Required**: `Client ID` + `Client Secret` (Instance URL blank)
- **Benefits**: User-specific permissions, secure OAuth flow

![Input Attributes for User Authentication](../_media/ExtensionConfiguration_InputAttributes1.png)

### Step 4: Redirect URI Configuration

For OAuth flows, configure the callback URL in your Salesforce Connected App:

1. Copy your extension's base URL from the Details tab
2. Append `/rest/salesforcemarketing/callback`
3. Example: `https://your-krista-instance.com/rest/salesforcemarketing/callback`
4. Add this URL to your Connected App's callback URLs

### Step 5: Allow Retry Flag

The retry mechanism automatically handles transient failures:

- **Enabled**: Failed requests are automatically retried with exponential backoff
- **Disabled**: Failed requests return immediately without retry
- **Recommended**: Enable for production environments

## Security Considerations

### OAuth 2.0 Best Practices

1. **Client Secret Protection**: Store Client Secret securely, never expose in logs
2. **Scope Limitation**: Use minimum required OAuth scopes
3. **Token Management**: Tokens are automatically refreshed by the extension
4. **Instance URL Validation**: Ensure Instance URL uses HTTPS and correct domain

### Password Grant Security

> **⚠️ Warning**: Password Grant Flow stores credentials directly. Use only for development/testing.

1. **Credential Storage**: Passwords are encrypted but stored in configuration
2. **Security Token**: Required for API access, obtained from Salesforce
3. **Regular Rotation**: Change passwords and security tokens regularly

### Network Security

1. **HTTPS Only**: All communication uses encrypted HTTPS connections
2. **Certificate Validation**: SSL certificates are validated for all requests
3. **Firewall Configuration**: Ensure outbound HTTPS (443) access to Salesforce

## Troubleshooting

### Common Configuration Issues

#### Issue: "Invalid Client ID or Client Secret"
**Cause**: Incorrect OAuth credentials  
**Resolution**: 
1. Verify Client ID and Client Secret from Connected App
2. Ensure Connected App is deployed and active
3. Check for extra spaces or characters in credentials

#### Issue: "Invalid Instance URL"
**Cause**: Incorrect Salesforce domain format  
**Resolution**:
1. Use base domain format: `https://mydomain.my.salesforce.com`
2. Do NOT use Lightning URLs (`lightning.force.com`)
3. Do NOT include trailing slash
4. Verify domain matches your Salesforce org

#### Issue: "Authentication Failed"
**Cause**: Various authentication problems  
**Resolution**:
1. Check OAuth scopes in Connected App
2. Verify user permissions for Client Credentials Flow
3. Ensure Connected App policies allow access
4. For Password Grant: verify username, password, and security token

#### Issue: "Connection Timeout"
**Cause**: Network connectivity problems  
**Resolution**:
1. Verify internet connectivity
2. Check firewall settings for HTTPS outbound
3. Confirm Salesforce service status
4. Test with different Instance URL if applicable

### Validation Steps

After configuration, validate your setup:

1. **Click "Validate Attributes"** - Tests credential validity
2. **Complete OAuth Flow** - Authorize if prompted
3. **Save Configuration** - Store validated settings
4. **Test Connection** - Use Diagnostics → Test Connection
5. **Verify Functionality** - Try a simple catalog request

### Advanced Configuration

#### Custom Domain Configuration
If your Salesforce org uses a custom domain:
1. Use your custom domain in Instance URL
2. Format: `https://yourcustomdomain.my.salesforce.com`
3. Ensure SSL certificate is valid

#### Sandbox Configuration
For Salesforce Sandbox environments:
1. Check the "Sandbox" flag
2. Use sandbox-specific Instance URL if required
3. Ensure Connected App exists in sandbox org

## Next Steps

After successful configuration:

1. **[Test Authentication](pages/Authentication.md)** - Verify OAuth flows work correctly
2. **[Explore Catalog Requests](pages/SupportedRequests.md)** - Try available operations
3. **[Review Entity Operations](pages/SupportedRequests.md#campaigns)** - Understand Campaign management

## See Also

- [Authentication Guide](pages/Authentication.md) - Detailed OAuth flow information
- [Creating Salesforce App](pages/CreatingSalesforceApp.md) - Connected App setup
- [Supported Requests](pages/SupportedRequests.md) - Available functionality
