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

package app.krista.extensions.crm.marketing_management.salesforce_marketing.catalog.entity;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CampaignTest {

    @Test
    public void testEntityFromFields() {
        Map<String, Object> campaignInput = new HashMap<>();
        campaignInput.put("Name", "testCampaign");
        campaignInput.put("id", "7015g000000UG1RAAW");
        campaignInput.put("Number Of Leads", 123d);
        campaignInput.put("Status", "Planned");

        Campaign createdCampaign = Campaign.fromFields(campaignInput);
        Assert.assertNotNull(createdCampaign);
        Assert.assertEquals("Planned", createdCampaign.status);
        Assert.assertNull(createdCampaign.id);

    }
}
