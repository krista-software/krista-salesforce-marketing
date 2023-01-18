package app.krista.extensions.crm.marketing_management.salesforce_marketing.catalog;

import app.krista.extensions.crm.marketing_management.salesforce_marketing.catalog.entity.Campaign;

public final class CatalogTypes {

    private CatalogTypes() {
    }

    /**
     * This function will Salesforce Campaign Model object to Krista Campaign Entity object
     *
     * @param campaign {@link app.krista.extensions.crm.marketing_management.salesforce_marketing.service.Campaign} object
     * @return {@link Campaign} object
     */
    public static Campaign fromSFCampaign(app.krista.extensions.crm.marketing_management.salesforce_marketing.service.Campaign campaign) {
        Campaign newCampaign = new Campaign();
        newCampaign.name = campaign.getName();
        newCampaign.id = campaign.getId();
        newCampaign.type = campaign.getType();
        newCampaign.numberSent = campaign.getNumberSent();
        newCampaign.numberOfLeads = campaign.getNumberOfLeads();
        newCampaign.numberOfConvertedLeads = campaign.getNumberOfConvertedLeads();
        newCampaign.numberOfContacts = campaign.getNumberOfContacts();
        newCampaign.numberOfResponses = campaign.getNumberOfResponses();
        newCampaign.numberOfOpportunities = campaign.getNumberOfOpportunities();
        newCampaign.numberOfWonOpportunities = campaign.getNumberOfWonOpportunities();
        newCampaign.createdDate = campaign.getCreatedDate();
        newCampaign.lastModifiedDate = campaign.getLastModifiedDate();
        newCampaign.ownerId = campaign.getOwnerId();
        newCampaign.createdById = campaign.getCreatedById();
        newCampaign.status = campaign.getStatus();
        newCampaign.description = campaign.getDescription();
        newCampaign.startDate = campaign.getStartDate();
        newCampaign.endDate = campaign.getEndDate();
        newCampaign.budgetedCost = campaign.getBudgetedCost();
        newCampaign.expectedRevenue = campaign.getExpectedRevenue();
        newCampaign.actualCost = campaign.getActualCost();
        newCampaign.expectedResponse = campaign.getExpectedResponse();
        newCampaign.amountAllOpportunities = campaign.getAmountAllOpportunities();
        newCampaign.amountWonOpportunities = campaign.getAmountWonOpportunities();
        return newCampaign;
    }
}
