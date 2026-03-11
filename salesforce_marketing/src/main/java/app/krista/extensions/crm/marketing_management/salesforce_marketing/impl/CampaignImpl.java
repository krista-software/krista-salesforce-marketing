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

package app.krista.extensions.crm.marketing_management.salesforce_marketing.impl;

import app.krista.extensions.crm.marketing_management.salesforce_marketing.model.SFCampaign;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.service.Campaign;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.util.DatePlus;

import java.util.Objects;

public class CampaignImpl implements Campaign {
    private final SFCampaign sfCampaign;

    public CampaignImpl(SFCampaign sfCampaign) {
        this.sfCampaign = sfCampaign;
    }

    @Override
    public String getName() {
        return sfCampaign.Name;
    }

    @Override
    public String getId() {
        return sfCampaign.Id;
    }

    @Override
    public Double getNumberSent() {
        return sfCampaign.NumberSent;
    }

    @Override
    public Double getNumberOfLeads() {
        return sfCampaign.NumberOfLeads;
    }

    @Override
    public Double getNumberOfConvertedLeads() {
        return sfCampaign.NumberOfConvertedLeads;
    }

    @Override
    public Double getNumberOfContacts() {
        return sfCampaign.NumberOfContacts;
    }

    @Override
    public Double getNumberOfResponses() {
        return sfCampaign.NumberOfResponses;
    }

    @Override
    public Double getNumberOfOpportunities() {
        return sfCampaign.NumberOfOpportunities;
    }

    @Override
    public Double getNumberOfWonOpportunities() {
        return sfCampaign.NumberOfWonOpportunities;
    }

    @Override
    public Long getCreatedDate() {
        return sfCampaign.CreatedDate != null ? sfCampaign.CreatedDate.getTime() : null;
    }

    @Override
    public Long getLastModifiedDate() {
        return sfCampaign.LastModifiedDate != null ? sfCampaign.LastModifiedDate.getTime() : null;
    }

    @Override
    public String getOwnerId() {
        return sfCampaign.OwnerId;
    }

    @Override
    public String getCreatedById() {
        return sfCampaign.CreatedById;
    }

    @Override
    public String getType() {
        return sfCampaign.Type;
    }

    @Override
    public String getStatus() {
        return sfCampaign.Status;
    }

    @Override
    public String getDescription() {
        return sfCampaign.Description;
    }

    @Override
    public Long getStartDate() {
        return sfCampaign.StartDate != null ? Objects.requireNonNull(DatePlus.fromYYYYMMDD(sfCampaign.StartDate)).getTime() : null;
    }

    @Override
    public Long getEndDate() {
        return sfCampaign.EndDate != null ? Objects.requireNonNull(DatePlus.fromYYYYMMDD(sfCampaign.EndDate)).getTime() : null;
    }

    @Override
    public Double getBudgetedCost() {
        return sfCampaign.BudgetedCost;
    }

    @Override
    public Double getExpectedRevenue() {
        return sfCampaign.ExpectedRevenue;
    }

    @Override
    public Double getActualCost() {
        return sfCampaign.ActualCost;
    }

    @Override
    public Double getExpectedResponse() {
        return sfCampaign.ExpectedResponse;
    }

    @Override
    public Double getAmountAllOpportunities() {
        return sfCampaign.AmountAllOpportunities;
    }

    @Override
    public Double getAmountWonOpportunities() {
        return sfCampaign.AmountWonOpportunities;
    }
}
