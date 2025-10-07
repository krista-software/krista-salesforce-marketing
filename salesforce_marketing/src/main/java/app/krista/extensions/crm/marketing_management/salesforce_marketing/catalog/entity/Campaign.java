package app.krista.extensions.crm.marketing_management.salesforce_marketing.catalog.entity;


import app.krista.extension.impl.anno.*;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.util.Constants;

import java.util.Map;

@Domain(id = "catEntryDomain_44aee5a3-7112-45a1-a751-7127535d9ec6",
        name = "Marketing Management",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "6527e782-7393-41b8-93f3-8a9e75e29716")
@Entity(name = "Campaign", id = "localDomainEntity_5ceca9e0-dd7a-4263-9b5a-e999d6e2f915", primaryKey = "Id", supportStore = false)
public class Campaign {

    @Searchable
    @ToString
    @Field(name = "Name", type = "Text")
    public String name;

    @Field(name = "Id", type = "Text", required = false)
    public String id;

    @Field(name = "Number Sent", type = "Number", required = false)
    public Double numberSent;

    @Field(name = "Number Of Leads", type = "Number", required = false)
    public Double numberOfLeads;

    @Field(name = "Number Of Converted Leads", type = "Number", required = false)
    public Double numberOfConvertedLeads;

    @Field(name = "Number Of Contacts", type = "Number", required = false)
    public Double numberOfContacts;

    @Field(name = "Number Of Responses", type = "Number", required = false)
    public Double numberOfResponses;

    @Field(name = "Number Of Opportunities", type = "Number", required = false)
    public Double numberOfOpportunities;

    @Field(name = "Number Of Won Opportunities", type = "Number", required = false)
    public Double numberOfWonOpportunities;

    @Field(name = "Created Date", type = "Date", required = false, attributes = @Attribute(name = "includeTimeOfDay", value = "true"))
    public Long createdDate;

    @Field(name = "Last Modified Date", type = "Date", required = false, attributes = @Attribute(name = "includeTimeOfDay", value = "true"))
    public Long lastModifiedDate;

    @Field(name = "Owner Id", type = "Text", required = false)
    public String ownerId;

    @Field(name = "Created By Id", type = "Text", required = false)
    public String createdById;

    @Searchable
    @ToString
    @Field(name = "Type", type = "Text", required = false)
    public String type;

    @Searchable
    @ToString
    @Field(name = "Status", type = "Text", required = false)
    public String status;

    @Field(name = "Description", type = "Paragraph", required = false)
    public String description;

    @Field(name = "Start Date", type = "Date", required = false)
    public Long startDate;

    @Field(name = "End Date", type = "Date", required = false)
    public Long endDate;

    @Field(name = "Budgeted Cost", type = "Currency", required = false)
    public Double budgetedCost;

    @Field(name = "Expected Revenue", type = "Currency", required = false)
    public Double expectedRevenue;

    @Field(name = "Actual Cost", type = "Currency", required = false)
    public Double actualCost;

    @Field(name = "Expected Response", type = "Percentage", required = false)
    public Double expectedResponse;

    @Field(name = "Amount All Opportunities", type = "Currency", required = false)
    public Double amountAllOpportunities;

    @Field(name = "Amount Won Opportunities", type = "Currency", required = false)
    public Double amountWonOpportunities;

    public static Campaign fromFields(Map<String, Object> campaign) {
        Campaign newCampaign = new Campaign();
        newCampaign.name = (String) campaign.get("Name");
        newCampaign.id = (String) campaign.get("Id");
        newCampaign.numberSent = (Double) campaign.get("Number Sent");
        newCampaign.numberOfLeads = (Double) campaign.get("Number Of Leads");
        newCampaign.numberOfConvertedLeads = (Double) campaign.get("Number Of Converted Leads");
        newCampaign.numberOfContacts = (Double) campaign.get("Number Of Contacts");
        newCampaign.numberOfResponses = (Double) campaign.get("Number Of Responses");
        newCampaign.numberOfOpportunities = (Double) campaign.get("Number Of Opportunities");
        newCampaign.numberOfWonOpportunities = (Double) campaign.get("Number Of Won Opportunities");
        newCampaign.createdDate = (Long) campaign.get("Created Date");
        newCampaign.lastModifiedDate = (Long) campaign.get("Last Modified Date");
        newCampaign.ownerId = (String) campaign.get("Owner Id");
        newCampaign.createdById = (String) campaign.get("Created By Id");
        newCampaign.type = (String) campaign.get("Type");
        newCampaign.status = (String) campaign.get("Status");
        newCampaign.description = (String) campaign.get("Description");
        if (campaign.get(Constants.START_DATE) != null && (Long) campaign.get(Constants.START_DATE) > 0)
            newCampaign.startDate = (Long) campaign.get(Constants.START_DATE);
        newCampaign.startDate = (Long) campaign.get(Constants.START_DATE);
        if (campaign.get(Constants.END_DATE) != null && (Long) campaign.get(Constants.END_DATE) > 0)
            newCampaign.endDate = (Long) campaign.get(Constants.END_DATE);
        newCampaign.budgetedCost = (Double) campaign.get("Budgeted Cost");
        newCampaign.expectedRevenue = (Double) campaign.get("Expected Revenue");
        newCampaign.actualCost = (Double) campaign.get("Actual Cost");
        newCampaign.expectedResponse = (Double) campaign.get("Expected Response");
        newCampaign.amountAllOpportunities = (Double) campaign.get("Amount All Opportunities");
        newCampaign.amountWonOpportunities = (Double) campaign.get("Amount Won Opportunities");
        return newCampaign;
    }
}
