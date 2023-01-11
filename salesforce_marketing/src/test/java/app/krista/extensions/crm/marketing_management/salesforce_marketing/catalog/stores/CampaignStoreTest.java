package app.krista.extensions.crm.marketing_management.salesforce_marketing.catalog.stores;

import app.krista.extensions.crm.marketing_management.salesforce_marketing.SalesforceMarketingAttributes;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.catalog.entity.Campaign;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.CampaignDaoImpl;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.CampaignImpl;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.SalesforceManager;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.connectors.SalesforceTokenProvider;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.model.SFCampaign;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.util.SalesforceListResponse;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.marketing_management.salesforce_marketing.util.Constants.GSON;

@RunWith(MockitoJUnitRunner.class)
public class CampaignStoreTest {

    SalesforceManager salesforceManager;
    @InjectMocks
    CampaignStore campaignStore;
    @Mock
    SalesforceTokenProvider tokenProvider;
    @Mock
    SalesforceMarketingAttributes salesforceMarketingAttributes;
    @Mock
    CampaignDaoImpl campaignDaoImpl;
    private String validCampaignResponse;
    private String validId;

    @Before
    public void setup() {
        validId = "7015g000000UG1RAAW";
        validCampaignResponse = "{\"totalSize\":1,\"done\":true,\"records\":[{\"attributes\":{\"type\":\"Campaign\",\"url\":\"/services/data/v55.0/sobjects/Campaign/7015g000000UG1RAAW\"},\"Status\":\"Planned\",\"Name\":\"test response title\",\"Type\":\"Conference\",\"StartDate\":null,\"EndDate\":null,\"ExpectedRevenue\":null,\"BudgetedCost\":null,\"ActualCost\":null,\"ExpectedResponse\":0.0,\"NumberSent\":12.0,\"NumberOfLeads\":0,\"NumberOfContacts\":0,\"NumberOfOpportunities\":0,\"NumberOfConvertedLeads\":0,\"CreatedDate\":\"2023-01-02T16:04:47.000+0000\",\"LastModifiedDate\":\"2023-01-02T16:04:47.000+0000\",\"Id\":\"7015g000000UG1RAAW\"}]}";

        salesforceManager = new SalesforceManager(tokenProvider, salesforceMarketingAttributes);
        campaignStore = new CampaignStore(campaignDaoImpl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCampaignByBlankOrNullId() {
        String campaignId = "";
        campaignStore.get(campaignId);
    }

    @Test
    public void testGetCampaignByValidId() {

        // Given
        List<app.krista.extensions.crm.marketing_management.salesforce_marketing.service.Campaign> campaigns = getCampaigns(validCampaignResponse);

        // Stub
        Campaign campaignEntity = getMockedCampaignEntity(campaigns);
        Assert.assertNotNull(campaignEntity);
        Assert.assertEquals(campaignEntity.id, validId);

    }

    private Campaign getMockedCampaignEntity(List<app.krista.extensions.crm.marketing_management.salesforce_marketing.service.Campaign> campaigns) {
        Mockito.when(campaignDaoImpl.getCampaignById(validId)).thenReturn(campaigns.get(0));
        return campaignStore.get(validId);
    }

    @Test
    public void testCreateCampaign_validData() {
        Map<String, Object> campaignEntityDefinition = new HashMap<>();
        campaignEntityDefinition.put("Name", "Vara");

        Mockito.when(campaignDaoImpl.addCampaign(campaignEntityDefinition)).thenReturn(validId);
        List<app.krista.extensions.crm.marketing_management.salesforce_marketing.service.Campaign> campaigns = getCampaigns(validCampaignResponse);
        getMockedCampaignEntity(campaigns);
        Assert.assertNotNull(campaignStore.create(campaignEntityDefinition));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCampaign_EmptyInput() {
        Map<String, Object> campaignData = new HashMap<>();
        campaignStore.create(campaignData);
    }

    @Test
    public void testUpdateCampaign() {
        List<app.krista.extensions.crm.marketing_management.salesforce_marketing.service.Campaign> campaigns = getCampaigns(validCampaignResponse);
        Campaign campaign = getMockedCampaignEntity(campaigns);
        Assert.assertNotNull(campaignStore.update(campaign));

    }

    private List<app.krista.extensions.crm.marketing_management.salesforce_marketing.service.Campaign> getCampaigns(String campaignResponse) {
        SalesforceListResponse<SFCampaign> oppList =
                GSON.fromJson(campaignResponse, new TypeToken<SalesforceListResponse<SFCampaign>>() {
                }.getType());
        return oppList.records.stream().map(CampaignImpl::new).collect(Collectors.toList());
    }

}
