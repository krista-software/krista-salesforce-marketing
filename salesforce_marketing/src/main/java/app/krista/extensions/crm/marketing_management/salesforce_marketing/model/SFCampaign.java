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

package app.krista.extensions.crm.marketing_management.salesforce_marketing.model;

import app.krista.extensions.crm.marketing_management.salesforce_marketing.catalog.entity.Campaign;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.util.DatePlus;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Map;

public final class SFCampaign {

    @SerializedName("Id")
    public String Id;

    @SerializedName("Name")
    public String Name;

    @SerializedName("Type")
    public String Type;

    @SerializedName("Status")
    public String Status;

    @SerializedName("Description")
    public String Description;

    @SerializedName("NumberOfOpportunities")
    public Double NumberOfOpportunities;

    @SerializedName("NumberOfWonOpportunities")
    public Double NumberOfWonOpportunities;

    @SerializedName("AmountWonOpportunities")
    public Double AmountWonOpportunities;

    @SerializedName("NumberOfContacts")
    public Double NumberOfContacts;

    @SerializedName("ExpectedRevenue")
    public Double ExpectedRevenue;

    @SerializedName("NumberOfConvertedLeads")
    public Double NumberOfConvertedLeads;

    @SerializedName("ActualCost")
    public Double ActualCost;

    @SerializedName("NumberSent")
    public Double NumberSent;

    @SerializedName("NumberOfLeads")
    public Double NumberOfLeads;
    @SerializedName("NumberOfResponses")
    public Double NumberOfResponses;
    @SerializedName("AmountAllOpportunities")
    public Double AmountAllOpportunities;
    @SerializedName("BudgetedCost")
    public Double BudgetedCost;
    @SerializedName("ExpectedResponse")
    public Double ExpectedResponse;

    @SerializedName("OwnerId")
    public String OwnerId;

    @SerializedName("CreatedById")
    public String CreatedById;

    @SerializedName("LastModifiedById")
    public String LastModifiedById;

    @SerializedName("CampaignMemberRecordTypeId")
    public String CampaignMemberRecordTypeId;

    @SerializedName("CreatedDate")
    public Date CreatedDate;

    @SerializedName("LastModifiedDate")
    public Date LastModifiedDate;

    @SerializedName("StartDate")
    public String StartDate;

    @SerializedName("EndDate")
    public String EndDate;


    /**
     * This function will convert {@link Map} with Campaign fields into {@link SFCampaign} object
     *
     * @param entityAttributesField {@link Map} of fields
     * @return {@link SFCampaign} object with entity attributes
     */
    public static SFCampaign from(Map<String, Object> entityAttributesField) {
        SFCampaign campaign = new SFCampaign();
        campaign.Id = (String) entityAttributesField.get("Id");
        campaign.Name = (String) entityAttributesField.get("Name");
        if (entityAttributesField.get("Created Date") != null)
            campaign.CreatedDate = new Date((Long) entityAttributesField.get("Created Date"));
        if (entityAttributesField.get("Last Modified Date") != null)
            campaign.LastModifiedDate = new Date((Long) entityAttributesField.get("Last Modified Date"));
        Long StartDate = (Long) entityAttributesField.get("Start Date");
        campaign.StartDate = StartDate != null && StartDate > 0 ? DatePlus.getYYYYMMDD(StartDate) : null;
        Long EndDate = (Long) entityAttributesField.get("End Date");
        campaign.EndDate = EndDate != null && EndDate > 0 ? DatePlus.getYYYYMMDD(EndDate) : null;
        campaign.Description = (String) entityAttributesField.get("Description");
        campaign.OwnerId = (String) entityAttributesField.get("Owner Id");
        campaign.Status = (String) entityAttributesField.get("Status");
        campaign.NumberOfOpportunities = (Double) entityAttributesField.get("Number Of Opportunities");
        campaign.NumberOfWonOpportunities = (Double) entityAttributesField.get("Number Of Won Opportunities");
        campaign.AmountWonOpportunities = (Double) entityAttributesField.get("Amount Won Opportunities");
        campaign.NumberOfContacts = (Double) entityAttributesField.get("Number Of Contacts");
        campaign.ExpectedRevenue = (Double) entityAttributesField.get("Expected Revenue");
        campaign.NumberOfConvertedLeads = (Double) entityAttributesField.get("Number Of Converted Leads");
        campaign.ActualCost = (Double) entityAttributesField.get("Actual Cost");
        campaign.Type = (String) entityAttributesField.get("Type");
        campaign.NumberSent = (Double) entityAttributesField.get("Number Sent");
        campaign.NumberOfResponses = (Double) entityAttributesField.get("Number Of Responses");
        campaign.NumberOfLeads = (Double) entityAttributesField.get("Number Of Leads");
        campaign.ExpectedResponse = (Double) entityAttributesField.get("Expected Response");
        campaign.BudgetedCost = (Double) entityAttributesField.get("Budgeted Cost");
        campaign.AmountAllOpportunities = (Double) entityAttributesField.get("Amount All Opportunities");
        campaign.CreatedById = (String) entityAttributesField.get("Created By Id");

        return campaign;
    }

    /**
     * This function will convert {@link Campaign} entity fields into {@link SFCampaign} model object
     *
     * @param campaign {@link Campaign} entity
     * @return {@link SFCampaign} model object
     */
    public static SFCampaign from(Campaign campaign) {
        SFCampaign sfCampaign = new SFCampaign();
        sfCampaign.Id = campaign.id;
        sfCampaign.CreatedDate = new Date(campaign.createdDate);
        if (campaign.startDate != null && campaign.startDate > 0)
            sfCampaign.StartDate = DatePlus.getYYYYMMDD(campaign.startDate);
        sfCampaign.LastModifiedDate = new Date(campaign.lastModifiedDate);
        if (campaign.endDate != null && campaign.endDate > 0)
            sfCampaign.EndDate = DatePlus.getYYYYMMDD(campaign.endDate);
        sfCampaign.Description = campaign.description;
        sfCampaign.OwnerId = campaign.ownerId;
        sfCampaign.Name = campaign.name;
        sfCampaign.Status = campaign.status;
        sfCampaign.NumberOfOpportunities = campaign.numberOfOpportunities;
        sfCampaign.NumberOfWonOpportunities = campaign.numberOfWonOpportunities;
        sfCampaign.AmountWonOpportunities = campaign.amountWonOpportunities;
        sfCampaign.NumberOfContacts = campaign.numberOfContacts;
        sfCampaign.ExpectedRevenue = campaign.expectedRevenue;
        sfCampaign.NumberOfConvertedLeads = campaign.numberOfConvertedLeads;
        sfCampaign.ActualCost = campaign.actualCost;
        sfCampaign.Type = campaign.type;
        sfCampaign.NumberSent = campaign.numberSent;
        sfCampaign.NumberOfResponses = campaign.numberOfResponses;
        sfCampaign.NumberOfLeads = campaign.numberOfLeads;
        sfCampaign.ExpectedResponse = campaign.expectedResponse;
        sfCampaign.BudgetedCost = campaign.budgetedCost;
        sfCampaign.AmountAllOpportunities = campaign.amountAllOpportunities;
        sfCampaign.CreatedById = campaign.createdById;
        return sfCampaign;
    }
}
