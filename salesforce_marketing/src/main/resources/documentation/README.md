# Salesforce Marketing Extension

## Overview

The Krista Salesforce Marketing Extension connects to Salesforce's Marketing Cloud and Campaign Management functionality, enabling automated marketing operations and campaign data management through Krista's intelligent automation platform.

## Key Features

- **Campaign Management**: Access and manage Salesforce marketing campaigns
- **Custom Field Retrieval**: Extract custom field data from marketing objects
- **Entity Operations**: Full CRUD operations on Campaign entities 
- **Multiple Authentication Methods**: Support for OAuth 2.0 flows and password authentication 
- **Real-time Integration**: Direct API connectivity with Salesforce Marketing Cloud 
- **Flexible Configuration**: Adaptable to various Salesforce org configurations

## Quick Start Guide

### 1. Prerequisites
- Salesforce Developer Account or admin access to your Salesforce org
- Access to create Connected Apps in Salesforce
- Krista platform with extension deployment capabilities

### 2. Setup Process
1. **[Create Salesforce Connected App](pages/CreatingSalesforceApp.md)** - Set up OAuth credentials
2. **[Configure Extension](pages/ExtensionConfiguration.md)** - Set connection parameters
3. **[Authenticate Connection](pages/Authentication.md)** - Establish secure connection
4. **[Test Integration](pages/SupportedRequests.md)** - Verify functionality

![Salesforce Marketing Extension Setup Interface](_media/ExtensionConfiguration_SalesforceMarketingSetup.png)

### 3. First Steps
- Configure your Salesforce Connected App with proper OAuth scopes
- Set up extension attributes with Client ID and Client Secret
- Test connection using the Diagnostics panel
- Explore available catalog requests and entity operations

## Documentation Structure

### 📚 **Getting Started**
- **[Extension Configuration](pages/ExtensionConfiguration.md)** - Complete setup guide
- **[Authentication](pages/Authentication.md)** - OAuth flows and security
- **[Creating Salesforce App](pages/CreatingSalesforceApp.md)** - Connected App setup

### 🔧 **Catalog Requests**
- **[Retrieve Custom Fields For Given Object](pages/RetrieveCustomFieldsForGivenObject.md)** - Extract custom field data

### 📊 **Entity Operations**
- **[Campaign Entity](pages/CampaignEntity.md)** - Complete campaign management documentation

### 🔧 **Troubleshooting**
- **[Troubleshooting Guide](pages/Troubleshooting.md)** - Issue resolution and debugging

### 📋 **Reference**
- **[Supported Requests](pages/SupportedRequests.md)** - Complete functionality overview
- **[Release Notes](pages/ReleaseNotes.md)** - Version history and updates

## Support & Resources

- **Extension Version**: 2.0.5
- **Salesforce API Version**: Latest supported
- **Documentation**: Comprehensive guides and examples
- **Authentication**: OAuth 2.0 and Password Grant flows supported

## Need Help?

- Review the **[Comprehensive Troubleshooting Guide](pages/Troubleshooting.md)** for detailed issue resolution
- Check **[Authentication Guide](pages/Authentication.md)** for connection issues
- Verify your **[Salesforce App Configuration](pages/CreatingSalesforceApp.md)**
- Consult **[Extension Configuration](pages/ExtensionConfiguration.md#troubleshooting)** for setup issues

