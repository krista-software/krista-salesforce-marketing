# Release Notes

## Version 2.0.5 (Current)

- **Release Date**: Current Version
- **Developer**: Vaibhav Choudhary
- **Krista Service APIs Java**: 1.0.117
- **Global Catalog Version**: GC-2025.10.1
- **Compatibility**: Salesforce API v58.0+, Krista Platform 2024.x

### 🆕 New Features

#### Enhanced Documentation
- **Comprehensive Documentation Suite**: Complete rewrite of all documentation following industry best practices
- **Interactive Guides**: Step-by-step setup guides with screenshots and examples
- **Detailed API Reference**: In-depth catalog request and entity operation documentation
- **Troubleshooting Guides**: Comprehensive error resolution and debugging information

#### Authentication Improvements
- **Multiple OAuth Flows**: Support for Authorization Code, Client Credentials, and Password Grant flows
- **Automatic Flow Detection**: Extension automatically selects appropriate OAuth flow based on configuration
- **Enhanced Security**: Improved token management and credential protection
- **Better Error Messages**: More descriptive authentication error messages and resolution steps

#### Functionality Enhancements
- **Custom Field Retrieval**: Robust custom field extraction with validation and error handling
- **Campaign Entity Management**: Full CRUD operations for Salesforce Campaign objects
- **Improved Search**: Enhanced campaign search with multiple criteria and sorting options
- **Retry Mechanism**: Automatic retry for transient failures with exponential backoff

### 🔧 Improvements

#### User Experience
- **Streamlined Configuration**: Simplified setup process with clear parameter descriptions
- **Better Validation**: Enhanced input validation with helpful error messages
- **Improved Diagnostics**: Better connection testing and troubleshooting tools
- **Documentation Navigation**: Organized documentation with logical categorization

#### Performance & Reliability
- **Optimized API Calls**: Reduced API usage through efficient query construction
- **Enhanced Error Handling**: Comprehensive error categorization and recovery strategies
- **Connection Stability**: Improved connection management and token refresh handling
- **Resource Management**: Better memory and connection resource management

### 🐛 Resolved Issues

#### Authentication
- **[KE-2428](https://antbrains.atlassian.net/browse/KE-2428)**: Added support for OAuth 2.0 Client Credentials Flow
- **Token Refresh**: Fixed automatic token refresh for long-running operations
- **Credential Validation**: Resolved issues with credential validation during setup
- **Session Management**: Improved session handling for concurrent operations

#### Data Handling
- **Custom Field Validation**: Fixed validation of custom field names and formats
- **Entity Mapping**: Resolved inconsistencies in Campaign entity field mapping
- **Data Type Handling**: Fixed handling of various Salesforce data types
- **Null Value Processing**: Improved handling of null and empty values

### 🔧 Technical Changes

#### Architecture Improvements
- **OAuth 2.0 Implementation**: Complete OAuth 2.0 flow implementation with multiple grant types
- **Code Refactoring**: Improved code organization and maintainability
- **API Integration**: Enhanced Salesforce API integration patterns
- **Configuration Management**: Improved configuration handling and validation

#### Security Enhancements
- **OAuth 2.0 Best Practices**: Implementation follows OAuth 2.0 security recommendations
- **Credential Protection**: Enhanced protection of sensitive authentication data
- **Audit Logging**: Improved logging for security monitoring and compliance
- **Permission Validation**: Better validation of user permissions and access rights

### 📋 Known Issues

#### Current Limitations
- **Object Support**: Custom field retrieval currently limited to Campaign objects only
- **Bulk Operations**: Limited support for bulk data operations
- **Real-time Sync**: No real-time data synchronization capabilities

#### Workarounds
- **Multiple Objects**: Use separate requests for different object types
- **Large Datasets**: Implement pagination for large result sets
- **Performance**: Monitor API usage to avoid rate limits

---

## Version 2.0.4

**Release Date**: Previous Version
**Compatibility**: Salesforce API v57.0+

### Features
- Initial Salesforce Marketing Cloud integration
- Basic Campaign entity support with CRUD operations
- OAuth 2.0 authentication implementation
- Custom field retrieval functionality
- Entity search and filtering capabilities

### Known Issues
- Limited OAuth flow support (Authorization Code only)
- Basic error messages
- Manual authentication flow selection
- Limited custom field validation

---

## Version 2.0.3

**Release Date**: Foundation Release
**Compatibility**: Salesforce API v56.0+

### Features
- Foundation extension framework
- Core connectivity features
- Basic Salesforce API integration
- Initial OAuth 2.0 support
- Basic entity framework implementation

---

## Migration Guide

### Upgrading from 2.0.4 to 2.0.5

#### New Features Available
- **Client Credentials Flow**: Now supports server-to-server authentication
- **Enhanced Documentation**: Complete documentation rewrite with detailed guides
- **Improved Error Handling**: Better error messages and troubleshooting

#### Configuration Changes
- **No Breaking Changes**: Existing configurations remain compatible
- **New Options**: Additional authentication methods available
- **Enhanced Validation**: New validation may catch previously undetected issues

#### Recommended Actions
1. **Review Configuration**: Use new validation features to verify setup
2. **Explore Client Credentials**: Consider using Client Credentials Flow for automated processes
3. **Update Documentation**: Review new comprehensive documentation
4. **Test Functionality**: Verify all existing integrations work correctly

## See Also

- [Extension Configuration](pages/ExtensionConfiguration.md) - Setup and configuration guide
- [Authentication](pages/Authentication.md) - OAuth setup and troubleshooting
- [Supported Requests](pages/SupportedRequests.md) - Complete functionality reference
