# Release Notes

## Version 2.0.6 (Current)

- **Release Date**: December 2024
- **Developer**: Krista Extensions Team
- **Krista Service APIs Java**: 1.0.117
- **Global Catalog Version**: GC-2025.10.1
- **Compatibility**: Salesforce API v58.0+, Krista Platform 2024.x, Java 21+

### 🆕 New Features

#### Technical Modernization
- **Java 21 Upgrade**: Full compatibility with Java 21 LTS with enhanced performance and security
- **Gradle 8.5**: Updated build system with improved dependency management and build performance
- **Modern Testing Framework**: Comprehensive test suite with 100% coverage for utility classes
- **Enhanced Error Messages**: Completely rewritten error messages following new format standards

#### Documentation Excellence
- **Complete Authentication Guide**: New comprehensive OAuth 2.0 documentation with all flows, scopes, and security best practices
- **Image Standardization**: All documentation images renamed following `<MD_FILE_NAME>_<IMAGE_NAME>.png` convention
- **Enhanced Catalog Request Documentation**: Detailed technical implementation, validation rules, and telemetry metrics
- **Improved Cross-References**: Fixed all internal links and improved navigation structure

#### Code Quality Improvements
- **Comprehensive Test Coverage**: Added real-world test cases for DatePlus utility class and SalesforceManager.getDifference method
- **Error Message Refactoring**: All error messages now clearly identify problems, explain requirements, and provide actionable resolution steps
- **Enhanced Validation**: Improved input validation with detailed error scenarios and logging

### 🔧 Improvements

#### Developer Experience
- **Better Error Diagnostics**: Error messages now include specific causes, requirements, and step-by-step resolution instructions
- **Enhanced Logging**: Improved telemetry metrics and logging for better debugging and monitoring
- **Code Documentation**: Updated inline documentation and technical implementation details

#### Build & Deployment
- **Gradle Wrapper**: Added Gradle 8.5 wrapper for consistent build environment across all platforms
- **Java 21 Compatibility**: Full support for Java 21 features and optimizations
- **Dependency Updates**: Updated all dependencies to latest compatible versions

### 🐛 Resolved Issues

#### Documentation
- **Fixed Image References**: Corrected all broken image links in documentation
- **Link Consistency**: Fixed case-sensitive file references (SupportedRequests.md → supportedRequests.md)
- **Navigation Improvements**: Updated _sidebar.md with correct file references

#### Code Quality
- **Error Message Clarity**: Replaced generic error messages with specific, actionable guidance
- **Test Coverage**: Added comprehensive edge case testing for utility methods
- **Validation Logic**: Enhanced field validation with proper error handling and logging

### 📋 Technical Details

#### New Test Classes
- **DatePlusTest**: Comprehensive testing of date utility methods with edge cases, boundary values, and error conditions
- **SalesforceManagerTest**: Complete testing of getDifference method with various object types and scenarios

#### Updated Error Messages
- All error messages in Constants.java now follow the format: Problem identification + Requirements + Resolution steps
- Enhanced error handling in CampaignStore, CampaignDaoImpl, and SalesforceManager classes

#### Documentation Structure
- Authentication.md: New comprehensive OAuth 2.0 guide with all flows, scopes, and security practices
- Image naming: All images follow `<MD_FILE_NAME>_<IMAGE_NAME>.png` convention
- Cross-references: All internal links verified and corrected

### 🔄 Migration Notes

#### For Developers
- **Java 21 Required**: Ensure development environment uses Java 21 LTS
- **Gradle 8.5**: Use included Gradle wrapper for consistent builds
- **Updated Dependencies**: Review dependency updates for any compatibility impacts

#### For Users
- **No Breaking Changes**: All existing configurations and integrations remain compatible
- **Enhanced Error Messages**: Users will see more helpful error messages with specific resolution steps
- **Improved Documentation**: Updated documentation provides better guidance for setup and troubleshooting

### 🔗 Related Resources

- [Extension Configuration](pages/ExtensionConfiguration.md) - Updated setup guide
- [Authentication](pages/Authentication.md) - New comprehensive OAuth guide
- [Troubleshooting](pages/Troubleshooting.md) - Enhanced error resolution guide

---

## Version 2.0.5

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
- [Supported Requests](pages/supportedRequests.md) - Complete functionality reference
