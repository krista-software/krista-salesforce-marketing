# Authentication

## Overview

The Salesforce Marketing Extension supports multiple OAuth 2.0 authentication modes to accommodate different integration scenarios. The extension automatically selects the appropriate authentication flow based on your configuration parameters, providing flexibility for various deployment environments and security requirements.

## Authentication Modes

### User Account Authentication (Authorization Code Flow)

**When to Use**: Interactive scenarios where user consent is required, or when accessing user-specific data.

**Configuration**: Provide only `Client ID` and `Client Secret`, leave `Instance URL` blank.

**Benefits**:
- User-specific permissions and data access
- Supports interactive consent flows
- Ideal for user-facing applications
- Automatic token refresh capabilities

### Service Account Authentication (Client Credentials Flow)

**When to Use**: Server-to-server integrations, automated processes, or when Instance URL is known.

**Configuration**: Provide `Client ID`, `Client Secret`, and `Instance URL`.

**Benefits**:
- No user interaction required
- Suitable for background processes
- Direct server-to-server communication
- Consistent authentication for automation

### Password Grant Authentication

**When to Use**: Development, testing, or legacy integrations where OAuth flows are not feasible.

**Configuration**: Provide `Client ID`, `Client Secret`, `Username`, `Password`, and `Security Token`.

**Benefits**:
- Simple credential-based authentication
- No redirect flows required
- Suitable for testing environments

⚠️ **Security Note**: Password Grant is less secure and should only be used in controlled environments.

## OAuth 2.0 Scopes and Permissions

### Required Scopes

The Salesforce Marketing Extension requires the following OAuth scopes for full functionality:

| Scope | URL | Permission Level | Description |
|-------|-----|------------------|-------------|
| **API Access** | `https://[instance].salesforce.com/services/oauth2/scope/api` | Read/Write | Access to Salesforce APIs for data operations |
| **Web Access** | `https://[instance].salesforce.com/services/oauth2/scope/web` | Read/Write | Web-based access to Salesforce resources |
| **Refresh Token** | `https://[instance].salesforce.com/services/oauth2/scope/refresh_token` | Token Management | Ability to refresh access tokens |

### Scope Permissions Explained

#### API Access (`/scope/api`)
- **Allows**: Access to Salesforce REST and SOAP APIs
- **Operations**: CRUD operations on Campaign objects, custom field queries
- **Data Access**: Marketing objects, custom fields, standard objects
- **Required For**: All catalog requests and entity operations

#### Web Access (`/scope/web`)
- **Allows**: Web-based access to Salesforce resources
- **Operations**: Browser-based authentication flows
- **Integration**: Required for OAuth authorization flows
- **Security**: Enables secure web-based token exchange

#### Refresh Token (`/scope/refresh_token`)
- **Allows**: Automatic token refresh without user re-authentication
- **Lifecycle**: Long-lived tokens for continuous access
- **Security**: Encrypted storage and automatic rotation
- **Required For**: Unattended operations and background processes

### Alternative Scopes (Not Used)

| Scope | URL | Why Not Used |
|-------|-----|--------------|
| Full Access | `/scope/full` | Excessive permissions beyond extension needs |
| Chatter API | `/scope/chatter_api` | Not required for marketing operations |
| Visualforce | `/scope/visualforce` | Extension doesn't use Visualforce pages |

## OAuth 2.0 Grant Types

### Authorization Code Grant

**Grant Type**: `authorization_code`  
**Used For**: Initial user authentication and obtaining refresh token

**Flow Overview**:
1. User is redirected to Salesforce authorization URL
2. User grants permissions to the Connected App
3. Salesforce redirects back with authorization code
4. Extension exchanges code for access and refresh tokens

**Request Example**:
```http
POST https://[instance].salesforce.com/services/oauth2/token
Content-Type: application/x-www-form-urlencoded

grant_type=authorization_code&
client_id=3MVG9A2kN3Bn17hs...&
client_secret=1234567890123456789&
redirect_uri=https://your-krista-instance.com/rest/salesforcemarketing/callback&
code=aPrx...
```

**Response Example**:
```json
{
  "access_token": "REDACTED_SALESFORCE_ACCESS_TOKEN",
  "refresh_token": "REDACTED_SALESFORCE_REFRESH_TOKEN",
  "signature": "0CcxinCY4uQJEsxOiQJhO82J4A3BQESLtqMelf5chU=",
  "scope": "api web refresh_token",
  "id_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "instance_url": "https://yourinstance.salesforce.com",
  "id": "https://login.salesforce.com/id/00D3h000007R1LuEAK/0053h000002xQ5YAAU",
  "token_type": "Bearer",
  "issued_at": "1278448832702",
  "expires_in": 7200
}
```

### Client Credentials Grant

**Grant Type**: `client_credentials`  
**Used For**: Server-to-server authentication with Instance URL

**Request Example**:
```http
POST https://[instance].salesforce.com/services/oauth2/token
Content-Type: application/x-www-form-urlencoded

grant_type=client_credentials&
client_id=3MVG9A2kN3Bn17hs...&
client_secret=1234567890123456789
```

### Refresh Token Grant

**Grant Type**: `refresh_token`  
**Used For**: Obtaining new access tokens without user interaction

**Request Example**:
```http
POST https://[instance].salesforce.com/services/oauth2/token
Content-Type: application/x-www-form-urlencoded

grant_type=refresh_token&
client_id=3MVG9A2kN3Bn17hs...&
client_secret=1234567890123456789&
refresh_token=REDACTED_SALESFORCE_REFRESH_TOKEN
```

### Password Grant

**Grant Type**: `password`  
**Used For**: Username/password authentication (development/testing)

**Request Example**:
```http
POST https://[instance].salesforce.com/services/oauth2/token
Content-Type: application/x-www-form-urlencoded

grant_type=password&
client_id=3MVG9A2kN3Bn17hs...&
client_secret=1234567890123456789&
username=user@company.com&
password=mypasswordAbCdEfGhIjKlMnOpQrSt
```

## Authentication Flow

### Step-by-Step OAuth Flow

#### Authorization Code Flow (User Authentication)

1. **Initiate Authentication**
   - Extension generates authorization URL with state parameter
   - User is redirected to Salesforce login page
   - State parameter includes user ID and optional auth context

2. **User Authorization**
   - User logs into Salesforce
   - User grants permissions to Connected App
   - Salesforce validates user credentials and permissions

3. **Authorization Code Exchange**
   - Salesforce redirects to callback URL with authorization code
   - Extension validates state parameter for security
   - Extension exchanges authorization code for tokens

4. **Token Storage**
   - Access token and refresh token are securely stored
   - Tokens are encrypted and associated with user account
   - Extension is ready for API operations

#### Client Credentials Flow (Server-to-Server)

1. **Direct Token Request**
   - Extension makes direct request to token endpoint
   - Uses Client ID, Client Secret, and Instance URL
   - No user interaction required

2. **Token Response**
   - Salesforce validates client credentials
   - Returns access token for API operations
   - No refresh token provided (tokens are shorter-lived)

### Authorization Code Exchange Details

The authorization code exchange process involves:

1. **Receive Authorization Code**: From Salesforce OAuth redirect
2. **Validate State Parameter**: Ensure request authenticity and prevent CSRF
3. **Extract User Context**: Parse user ID and auth context from state
4. **Exchange Code for Tokens**: POST request to Salesforce token endpoint
5. **Store Tokens Securely**: Encrypt and store access and refresh tokens
6. **Verify Token Scope**: Ensure granted scopes match requirements

### Token Storage Mechanism

- **Encryption**: All tokens encrypted using platform security
- **Association**: Tokens linked to specific user accounts
- **Isolation**: Each user's tokens stored separately
- **Cleanup**: Expired tokens automatically removed

## Token Management

### Access Token Lifecycle

- **Lifetime**: 2 hours (7200 seconds) for most Salesforce orgs
- **Usage**: Authenticate all API requests to Salesforce
- **Refresh**: Automatically refreshed using refresh token before expiration
- **Storage**: Encrypted in Krista platform secure storage
- **Scope**: Limited to granted OAuth scopes

### Refresh Token Lifecycle

#### For Verified Connected Apps
- **Lifetime**: Indefinite (until explicitly revoked)
- **Refresh Policy**: Tokens remain valid until revoked
- **Usage**: Automatic access token refresh

#### For Unverified Connected Apps
- **Lifetime**: 90 days of inactivity
- **Refresh Policy**: Must be used within 90 days or expires
- **Renewal**: Requires re-authentication if expired

### Token Expiration Scenarios

#### Access Token Expiration
- **Detection**: API returns 401 Unauthorized
- **Action**: Automatic refresh using refresh token
- **Fallback**: Re-authentication if refresh fails
- **User Impact**: Transparent to end users

#### Refresh Token Expiration
- **Causes**: 
  - 90-day inactivity (unverified apps)
  - User password change
  - User deactivation
  - Manual token revocation
  - Security policy changes
- **Detection**: Refresh attempt returns invalid_grant error
- **Action**: Trigger re-authentication flow
- **User Impact**: User must re-authorize the application

#### Token Revocation Scenarios
- **User Action**: User revokes app access in Salesforce
- **Password Change**: User changes Salesforce password
- **Security Events**: Suspicious activity detected
- **Admin Action**: Administrator revokes Connected App access
- **Token Limits**: Salesforce token limits exceeded

### Automatic Refresh Mechanism

The extension implements automatic token refresh:

1. **Pre-emptive Refresh**: Tokens refreshed before expiration
2. **Error Handling**: 401 errors trigger immediate refresh attempt
3. **Retry Logic**: Failed requests retried after successful refresh
4. **Fallback**: Re-authentication triggered if refresh fails

## Security Best Practices

### Credential Protection
- Store Client Secret securely and never expose in logs
- Use HTTPS for all OAuth communications
- Implement proper state parameter validation
- Rotate credentials regularly

### Token Security
- Encrypt tokens at rest and in transit
- Implement token expiration monitoring
- Use minimum required OAuth scopes
- Monitor for suspicious token usage

### Network Security
- Validate SSL certificates for all Salesforce connections
- Use secure callback URLs (HTTPS only)
- Implement proper CORS policies
- Monitor network traffic for anomalies

## Troubleshooting Authentication

### Common Issues

#### Invalid Client Credentials
**Symptoms**: Authentication fails with "invalid_client_id" or "invalid_client"
**Causes**: 
- Incorrect Client ID or Client Secret
- Connected App not deployed or active
- Callback URL mismatch

**Solutions**:
1. Verify Client ID and Client Secret in Connected App
2. Ensure Connected App is deployed and active
3. Check callback URL configuration matches exactly

#### Insufficient Permissions
**Symptoms**: API calls fail with permission errors
**Causes**:
- Missing OAuth scopes in Connected App
- User lacks required Salesforce permissions
- Profile restrictions

**Solutions**:
1. Add required OAuth scopes to Connected App
2. Verify user has appropriate Salesforce permissions
3. Check profile and permission set assignments

#### Token Refresh Failures
**Symptoms**: Automatic refresh fails, requires re-authentication
**Causes**:
- Refresh token expired or revoked
- Connected App configuration changed
- User account deactivated

**Solutions**:
1. Check refresh token validity in Salesforce
2. Verify Connected App configuration unchanged
3. Confirm user account is active

### Error Handling Flow

```
API Request → 401 Unauthorized → Refresh Token → Success/Failure
                                      ↓
                                 Re-authenticate User
```

## See Also

- [Extension Configuration](ExtensionConfiguration.md) - Setup and configuration guide
- [Creating Salesforce App](CreatingSalesforceApp.md) - Connected App setup
- [Troubleshooting](Troubleshooting.md) - Additional troubleshooting guidance
