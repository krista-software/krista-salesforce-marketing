# Troubleshooting

## Overview

This comprehensive troubleshooting guide helps resolve common issues with the Salesforce Marketing Extension. Issues are organized by category with detailed solutions and prevention strategies.

## Authentication Issues

### Issue: "Invalid Client ID or Client Secret"

**Symptoms**:
- Authentication fails during connection setup
- Error message: "Invalid client credentials"
- Unable to complete OAuth flow

**Causes**:
- Incorrect Client ID or Client Secret copied from Salesforce
- Extra spaces or hidden characters in credentials
- Connected App not properly deployed or active
- Using credentials from wrong Salesforce org

**Resolution Steps**:
1. **Verify Credentials**:
   - Go to Salesforce Setup → App Manager
   - Find your Connected App and click "View"
   - Click "Manage Consumer Details"
   - Copy Client ID and Client Secret exactly (no extra spaces)

2. **Check Connected App Status**:
   - Ensure Connected App status is "Active"
   - Verify OAuth settings are enabled
   - Confirm all required scopes are selected

3. **Test in Different Browser**:
   - Clear browser cache and cookies
   - Try authentication in incognito/private mode
   - Disable browser extensions that might interfere

**Prevention**:
- Always copy credentials directly from Salesforce console
- Use a password manager to avoid typing errors
- Document which org the credentials belong to

---

### Issue: "OAuth Flow Selection Problems"

**Symptoms**:
- Wrong authentication method being used
- Client Credentials Flow not working when expected
- Authorization Code Flow triggering unexpectedly

**Causes**:
- Incorrect parameter combination in extension configuration
- Misunderstanding of flow selection logic
- Connected App not configured for desired flow

**Resolution Steps**:
1. **Review Flow Selection Logic**:
   - **Authorization Code**: Client ID + Client Secret (Instance URL blank)
   - **Client Credentials**: Client ID + Client Secret + Instance URL
   - **Password Grant**: Username + Password + Security Token + Client ID + Client Secret

2. **For Client Credentials Flow**:
   - Ensure Instance URL is provided
   - Verify Connected App has "Enable OAuth 2.0 Client Credentials Flow" checked
   - Confirm "Run As" user is set in Connected App policies
   - Wait 2-5 minutes after policy changes

3. **For Authorization Code Flow**:
   - Leave Instance URL blank
   - Ensure callback URL is correctly configured
   - Verify user has permission to authorize the app

**Prevention**:
- Understand flow selection before configuration
- Document intended authentication method
- Test configuration in sandbox first

---

### Issue: "Token Refresh Failures"

**Symptoms**:
- Authentication works initially but fails after time
- Error: "Token expired" or "Invalid refresh token"
- Need to re-authenticate frequently

**Causes**:
- Refresh token expired or revoked
- User changed password
- Connected App policies changed
- Salesforce security events

**Resolution Steps**:
1. **Check Token Policies**:
   - Go to Connected App → Edit Policies
   - Set "Refresh Token Policy" to "Refresh token is valid until revoked"
   - Ensure "IP Relaxation" is appropriate for your use case

2. **Re-authenticate**:
   - Clear stored tokens in extension
   - Complete OAuth flow again
   - Verify new tokens are working

3. **Monitor Token Usage**:
   - Check Salesforce Login History for token usage
   - Review API usage patterns
   - Implement proper error handling for token refresh

**Prevention**:
- Set appropriate refresh token policies
- Implement automatic token refresh in workflows
- Monitor token expiration patterns

## Configuration Issues

### Issue: "Invalid Instance URL"

**Symptoms**:
- Connection fails with Instance URL error
- Error: "Invalid endpoint" or "Host not found"
- Client Credentials Flow not working

**Causes**:
- Incorrect Instance URL format
- Using Lightning domain instead of base domain
- Including trailing slash in URL
- Wrong Salesforce org domain

**Resolution Steps**:
1. **Verify URL Format**:
   - Correct: `https://mydomain.my.salesforce.com`
   - Incorrect: `https://mydomain.lightning.force.com`
   - Incorrect: `https://mydomain.my.salesforce.com/`

2. **Find Correct Domain**:
   - Log into Salesforce and check browser URL
   - Use the base domain (before any path)
   - For sandbox: `https://mydomain--sandbox.my.salesforce.com`

3. **Test Domain**:
   - Open Instance URL in browser
   - Should redirect to Salesforce login page
   - Verify SSL certificate is valid

**Prevention**:
- Always use base domain format
- Document correct Instance URL for each org
- Test URL accessibility before configuration

---

### Issue: "Permission Denied Errors"

**Symptoms**:
- Authentication succeeds but API calls fail
- Error: "Insufficient privileges" or "Access denied"
- Some operations work, others don't

**Causes**:
- User lacks required object permissions
- Missing field-level security permissions
- OAuth scopes too restrictive
- Profile or permission set limitations

**Resolution Steps**:
1. **Check User Permissions**:
   - Verify user has read/write access to Campaign objects
   - Check field-level security for custom fields
   - Ensure API access is enabled for user

2. **Review OAuth Scopes**:
   - Confirm all required scopes are granted
   - Check Connected App scope configuration
   - Re-authorize if scopes were added

3. **For Client Credentials Flow**:
   - Verify "Run As" user has appropriate permissions
   - Check integration user profile settings
   - Ensure user is active and not locked

**Prevention**:
- Use dedicated integration user with proper permissions
- Document required permissions for each operation
- Test with minimal permissions first, then expand

## API and Data Issues

### Issue: "Custom Field Not Found"

**Symptoms**:
- Custom field retrieval returns empty results
- Error: "Field does not exist on object"
- Some custom fields work, others don't

**Causes**:
- Custom field doesn't exist on the object
- Field name misspelled or incorrect format
- User lacks field-level read permissions
- Field is not accessible via API

**Resolution Steps**:
1. **Verify Field Existence**:
   - Go to Salesforce Setup → Object Manager
   - Select Campaign object
   - Check Fields & Relationships for the custom field
   - Verify field API name ends with "__c"

2. **Check Field Permissions**:
   - Review field-level security settings
   - Ensure user profile has read access
   - Check permission sets if applicable

3. **Test Field Access**:
   - Use Salesforce Developer Console to query field
   - Try: `SELECT Custom_Field__c FROM Campaign LIMIT 1`
   - Verify field returns data

**Prevention**:
- Always verify custom field API names
- Test field access before automation
- Document custom field requirements

---

### Issue: "Rate Limit Exceeded"

**Symptoms**:
- API calls fail intermittently
- Error: "Request limit exceeded" or "Too many requests"
- Performance degrades over time

**Causes**:
- Too many API calls in short time period
- Salesforce org API limits reached
- Concurrent processes using API
- Inefficient query patterns

**Resolution Steps**:
1. **Check API Usage**:
   - Go to Salesforce Setup → System Overview
   - Review API usage statistics
   - Identify peak usage patterns

2. **Implement Rate Limiting**:
   - Add delays between API calls
   - Use bulk operations when possible
   - Implement exponential backoff for retries

3. **Optimize Queries**:
   - Reduce number of API calls
   - Use selective queries (only needed fields)
   - Implement caching where appropriate

**Prevention**:
- Monitor API usage regularly
- Design efficient integration patterns
- Use bulk operations for large datasets

## Network and Connectivity Issues

### Issue: "Connection Timeout"

**Symptoms**:
- Extension fails to connect to Salesforce
- Timeout errors during API calls
- Intermittent connectivity issues

**Causes**:
- Network connectivity problems
- Firewall blocking HTTPS traffic
- DNS resolution issues
- Salesforce service outages

**Resolution Steps**:
1. **Test Network Connectivity**:
   - Verify internet connection is working
   - Test access to `https://login.salesforce.com`
   - Check if other Salesforce integrations work

2. **Check Firewall Settings**:
   - Ensure outbound HTTPS (port 443) is allowed
   - Verify no proxy interference
   - Check corporate firewall rules

3. **Verify Salesforce Status**:
   - Check Salesforce Trust status page
   - Verify your instance is operational
   - Look for maintenance windows

**Prevention**:
- Monitor network connectivity
- Implement retry logic for transient failures
- Set up alerts for connectivity issues

## Data Quality Issues

### Issue: "Unexpected Data Types"

**Symptoms**:
- Data appears corrupted or wrong format
- Type conversion errors
- Null values where data expected

**Causes**:
- Salesforce field type changes
- Data migration issues
- Custom field configuration problems
- Regional formatting differences

**Resolution Steps**:
1. **Verify Field Types**:
   - Check Salesforce field definitions
   - Confirm data types match expectations
   - Review recent field changes

2. **Test Data Samples**:
   - Query small data samples manually
   - Verify data format and content
   - Check for null or empty values

3. **Handle Data Variations**:
   - Implement robust data type handling
   - Add validation for expected formats
   - Handle null values gracefully

**Prevention**:
- Document expected data formats
- Implement data validation
- Monitor for field definition changes

## Performance Issues

### Issue: "Slow Response Times"

**Symptoms**:
- API calls take longer than expected
- Timeouts on large data requests
- Poor user experience

**Causes**:
- Large result sets
- Complex queries
- Network latency
- Salesforce org performance

**Resolution Steps**:
1. **Optimize Queries**:
   - Use selective field lists
   - Add appropriate filters
   - Implement pagination for large results

2. **Monitor Performance**:
   - Track API response times
   - Identify slow operations
   - Optimize critical paths

3. **Implement Caching**:
   - Cache frequently accessed data
   - Use appropriate cache expiration
   - Implement cache invalidation

**Prevention**:
- Design for performance from start
- Regular performance testing
- Monitor and alert on performance degradation

## Getting Additional Help

### Diagnostic Information to Collect

When reporting issues, include:
- Extension version number
- Salesforce org type (Production/Sandbox)
- Authentication method being used
- Complete error messages
- Steps to reproduce the issue
- Network and browser information

### Escalation Process

1. **Check Documentation**: Review relevant sections first
2. **Search Known Issues**: Check release notes and troubleshooting
3. **Test in Sandbox**: Reproduce issue in test environment
4. **Collect Diagnostics**: Gather all relevant information
5. **Contact Support**: Provide detailed issue description

### Useful Salesforce Tools

- **Developer Console**: For testing SOQL queries
- **Setup Audit Trail**: For tracking configuration changes
- **Login History**: For authentication troubleshooting
- **API Usage**: For monitoring rate limits
- **Trust Status**: For service availability

## See Also

- [Extension Configuration](pages/ExtensionConfiguration.md) - Setup and configuration
- [Authentication](pages/Authentication.md) - OAuth setup and security
- [Creating Salesforce App](pages/CreatingSalesforceApp.md) - Connected App setup
- [Supported Requests](pages/supportedRequests.md) - API reference and examples
