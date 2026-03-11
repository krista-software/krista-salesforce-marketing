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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for CampaignImpl class.
 * Tests all getter methods and data transformations.
 */
@DisplayName("CampaignImpl Tests")
class CampaignImplTest {

    private SFCampaign sfCampaign;
    private CampaignImpl campaignImpl;

    @BeforeEach
    void setUp() {
        sfCampaign = new SFCampaign();
        sfCampaign.Id = "7013000000XyZ123";
        sfCampaign.Name = "Test Campaign";
        sfCampaign.Type = "Email";
        sfCampaign.Status = "In Progress";
        sfCampaign.Description = "Test campaign description";
        sfCampaign.StartDate = "2024-01-01";
        sfCampaign.EndDate = "2024-12-31";
        sfCampaign.ExpectedRevenue = 100000.0;
        sfCampaign.BudgetedCost = 50000.0;
        sfCampaign.ActualCost = 45000.0;
        sfCampaign.ExpectedResponse = 1000.0;
        sfCampaign.NumberSent = 5000.0;
        sfCampaign.NumberOfLeads = 500.0;
        sfCampaign.NumberOfContacts = 300.0;
        sfCampaign.NumberOfOpportunities = 50.0;
        sfCampaign.NumberOfConvertedLeads = 100.0;
        sfCampaign.NumberOfResponses = 250.0;
        sfCampaign.NumberOfWonOpportunities = 25.0;
        sfCampaign.AmountAllOpportunities = 500000.0;
        sfCampaign.AmountWonOpportunities = 250000.0;
        sfCampaign.OwnerId = "0053000000ABC123";
        sfCampaign.CreatedById = "0053000000DEF456";
        sfCampaign.CreatedDate = new Date(1704110400000L); // 2024-01-01 10:00:00
        sfCampaign.LastModifiedDate = new Date(1705320000000L); // 2024-01-15 10:00:00

        campaignImpl = new CampaignImpl(sfCampaign);
    }

    @Test
    @DisplayName("Should create CampaignImpl with SFCampaign")
    void shouldCreateCampaignImplWithSfCampaign() {
        // Given & When
        Campaign campaign = new CampaignImpl(sfCampaign);

        // Then
        assertNotNull(campaign);
    }

    @Test
    @DisplayName("Should return correct campaign ID")
    void shouldReturnCorrectCampaignId() {
        // When
        String id = campaignImpl.getId();

        // Then
        assertEquals("7013000000XyZ123", id);
    }

    @Test
    @DisplayName("Should return correct campaign name")
    void shouldReturnCorrectCampaignName() {
        // When
        String name = campaignImpl.getName();

        // Then
        assertEquals("Test Campaign", name);
    }

    @Test
    @DisplayName("Should return correct campaign type")
    void shouldReturnCorrectCampaignType() {
        // When
        String type = campaignImpl.getType();

        // Then
        assertEquals("Email", type);
    }

    @Test
    @DisplayName("Should return correct campaign status")
    void shouldReturnCorrectCampaignStatus() {
        // When
        String status = campaignImpl.getStatus();

        // Then
        assertEquals("In Progress", status);
    }

    @Test
    @DisplayName("Should return correct campaign description")
    void shouldReturnCorrectCampaignDescription() {
        // When
        String description = campaignImpl.getDescription();

        // Then
        assertEquals("Test campaign description", description);
    }

    @Test
    @DisplayName("Should return correct number sent")
    void shouldReturnCorrectNumberSent() {
        // When
        Double numberSent = campaignImpl.getNumberSent();

        // Then
        assertEquals(5000.0, numberSent);
    }

    @Test
    @DisplayName("Should return correct number of leads")
    void shouldReturnCorrectNumberOfLeads() {
        // When
        Double numberOfLeads = campaignImpl.getNumberOfLeads();

        // Then
        assertEquals(500.0, numberOfLeads);
    }

    @Test
    @DisplayName("Should return correct number of converted leads")
    void shouldReturnCorrectNumberOfConvertedLeads() {
        // When
        Double numberOfConvertedLeads = campaignImpl.getNumberOfConvertedLeads();

        // Then
        assertEquals(100.0, numberOfConvertedLeads);
    }

    @Test
    @DisplayName("Should return correct number of contacts")
    void shouldReturnCorrectNumberOfContacts() {
        // When
        Double numberOfContacts = campaignImpl.getNumberOfContacts();

        // Then
        assertEquals(300.0, numberOfContacts);
    }

    @Test
    @DisplayName("Should return correct number of responses")
    void shouldReturnCorrectNumberOfResponses() {
        // When
        Double numberOfResponses = campaignImpl.getNumberOfResponses();

        // Then
        assertEquals(250.0, numberOfResponses);
    }

    @Test
    @DisplayName("Should return correct number of opportunities")
    void shouldReturnCorrectNumberOfOpportunities() {
        // When
        Double numberOfOpportunities = campaignImpl.getNumberOfOpportunities();

        // Then
        assertEquals(50.0, numberOfOpportunities);
    }

    @Test
    @DisplayName("Should return correct number of won opportunities")
    void shouldReturnCorrectNumberOfWonOpportunities() {
        // When
        Double numberOfWonOpportunities = campaignImpl.getNumberOfWonOpportunities();

        // Then
        assertEquals(25.0, numberOfWonOpportunities);
    }

    @Test
    @DisplayName("Should return correct created date as timestamp")
    void shouldReturnCorrectCreatedDateAsTimestamp() {
        // When
        Long createdDate = campaignImpl.getCreatedDate();

        // Then
        assertNotNull(createdDate);
        assertEquals(1704110400000L, createdDate);
    }

    @Test
    @DisplayName("Should return correct last modified date as timestamp")
    void shouldReturnCorrectLastModifiedDateAsTimestamp() {
        // When
        Long lastModifiedDate = campaignImpl.getLastModifiedDate();

        // Then
        assertNotNull(lastModifiedDate);
        assertEquals(1705320000000L, lastModifiedDate);
    }

    @Test
    @DisplayName("Should return null when created date is null")
    void shouldReturnNullWhenCreatedDateIsNull() {
        // Given
        sfCampaign.CreatedDate = null;
        CampaignImpl campaign = new CampaignImpl(sfCampaign);

        // When
        Long createdDate = campaign.getCreatedDate();

        // Then
        assertNull(createdDate);
    }

    @Test
    @DisplayName("Should return null when last modified date is null")
    void shouldReturnNullWhenLastModifiedDateIsNull() {
        // Given
        sfCampaign.LastModifiedDate = null;
        CampaignImpl campaign = new CampaignImpl(sfCampaign);

        // When
        Long lastModifiedDate = campaign.getLastModifiedDate();

        // Then
        assertNull(lastModifiedDate);
    }

    @Test
    @DisplayName("Should return correct owner ID")
    void shouldReturnCorrectOwnerId() {
        // When
        String ownerId = campaignImpl.getOwnerId();

        // Then
        assertEquals("0053000000ABC123", ownerId);
    }

    @Test
    @DisplayName("Should return correct created by ID")
    void shouldReturnCorrectCreatedById() {
        // When
        String createdById = campaignImpl.getCreatedById();

        // Then
        assertEquals("0053000000DEF456", createdById);
    }

    @Test
    @DisplayName("Should return correct start date as timestamp")
    void shouldReturnCorrectStartDateAsTimestamp() {
        // When
        Long startDate = campaignImpl.getStartDate();

        // Then
        assertNotNull(startDate);
    }

    @Test
    @DisplayName("Should return correct end date as timestamp")
    void shouldReturnCorrectEndDateAsTimestamp() {
        // When
        Long endDate = campaignImpl.getEndDate();

        // Then
        assertNotNull(endDate);
    }

    @Test
    @DisplayName("Should return null when start date is null")
    void shouldReturnNullWhenStartDateIsNull() {
        // Given
        sfCampaign.StartDate = null;
        CampaignImpl campaign = new CampaignImpl(sfCampaign);

        // When
        Long startDate = campaign.getStartDate();

        // Then
        assertNull(startDate);
    }

    @Test
    @DisplayName("Should return null when end date is null")
    void shouldReturnNullWhenEndDateIsNull() {
        // Given
        sfCampaign.EndDate = null;
        CampaignImpl campaign = new CampaignImpl(sfCampaign);

        // When
        Long endDate = campaign.getEndDate();

        // Then
        assertNull(endDate);
    }

    @Test
    @DisplayName("Should return correct budgeted cost")
    void shouldReturnCorrectBudgetedCost() {
        // When
        Double budgetedCost = campaignImpl.getBudgetedCost();

        // Then
        assertEquals(50000.0, budgetedCost);
    }

    @Test
    @DisplayName("Should return correct expected revenue")
    void shouldReturnCorrectExpectedRevenue() {
        // When
        Double expectedRevenue = campaignImpl.getExpectedRevenue();

        // Then
        assertEquals(100000.0, expectedRevenue);
    }

    @Test
    @DisplayName("Should return correct actual cost")
    void shouldReturnCorrectActualCost() {
        // When
        Double actualCost = campaignImpl.getActualCost();

        // Then
        assertEquals(45000.0, actualCost);
    }

    @Test
    @DisplayName("Should return correct expected response")
    void shouldReturnCorrectExpectedResponse() {
        // When
        Double expectedResponse = campaignImpl.getExpectedResponse();

        // Then
        assertEquals(1000.0, expectedResponse);
    }

    @Test
    @DisplayName("Should return correct amount all opportunities")
    void shouldReturnCorrectAmountAllOpportunities() {
        // When
        Double amountAllOpportunities = campaignImpl.getAmountAllOpportunities();

        // Then
        assertEquals(500000.0, amountAllOpportunities);
    }

    @Test
    @DisplayName("Should return correct amount won opportunities")
    void shouldReturnCorrectAmountWonOpportunities() {
        // When
        Double amountWonOpportunities = campaignImpl.getAmountWonOpportunities();

        // Then
        assertEquals(250000.0, amountWonOpportunities);
    }
}

