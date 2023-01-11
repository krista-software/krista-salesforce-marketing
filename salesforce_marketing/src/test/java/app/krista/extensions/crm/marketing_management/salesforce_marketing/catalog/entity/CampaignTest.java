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
