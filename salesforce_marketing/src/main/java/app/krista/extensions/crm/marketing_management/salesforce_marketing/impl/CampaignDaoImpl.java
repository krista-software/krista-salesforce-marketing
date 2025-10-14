package app.krista.extensions.crm.marketing_management.salesforce_marketing.impl;

import app.krista.extensions.crm.marketing_management.salesforce_marketing.catalog.CatalogTypes;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.model.SFCampaign;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.service.Campaign;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.service.CampaignDao;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.util.Constants;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.util.SalesforceListResponse;
import com.google.gson.reflect.TypeToken;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.marketing_management.salesforce_marketing.util.Constants.GSON;

@Service
public class CampaignDaoImpl implements CampaignDao {
    public static final String QUERY_FIELDS = " Status,Name,Type,StartDate,EndDate,ExpectedRevenue,BudgetedCost,ActualCost,ExpectedResponse,NumberSent,NumberOfLeads,NumberOfContacts,NumberOfOpportunities,NumberOfConvertedLeads,CreatedDate,LastModifiedDate,Id ";
    public static final String ID = "id";
    public static final String THIS_REQUEST_IS_NOT_YET_IMPLEMENTED = "Campaign deletion is not supported in this extension version. This operation requires additional validation and dependency checking that is not currently implemented. Please use Salesforce directly to delete campaigns or contact support for assistance.";


    private final SalesforceManager salesforceManager;

    @Inject
    public CampaignDaoImpl(SalesforceManager salesforceManager) {
        this.salesforceManager = salesforceManager;
    }

    @Override
    public List<Campaign> getCampaigns(long size, long page, String orderByField, String orderByOperand) {
        return getCampaignByQuery(Constants.SELECT + QUERY_FIELDS + Constants.FROM_CAMPAIGN + Constants.ORDER_BY + orderByField + " " + orderByOperand + Constants.LIMIT + size + Constants.OFFSET + Math.max((page - 1) * size, 0));
    }

    @Override
    public List<Campaign> getCampaignByName(String name, long size, long page, String orderByField, String orderByOperand) {
        // Escape single quotes and SOQL wildcards to prevent SOQL injection
        String escapedName = name.replace("'", "\\'").replace("%", "\\%").replace("_", "\\_");

        return getCampaignByQuery(Constants.SELECT + QUERY_FIELDS + Constants.FROM_CAMPAIGN + "where name LIKE '%" +
                escapedName + "%' " + Constants.ORDER_BY +
                orderByField + Constants.SPACE +
                orderByOperand + Constants.LIMIT +
                size + Constants.OFFSET + Math.max((page - 1) * size, 0));
    }

    @Override
    public List<Campaign> getCampaignByName(String name, String type, long size, long page, String orderByField, String orderByOperand) {
        if (type == null || type.isEmpty()) {
            return getCampaignByName(name, size, page, orderByField, orderByOperand);
        }

        // Escape single quotes and SOQL wildcards to prevent SOQL injection
        String escapedName = name.replace("'", "\\'").replace("%", "\\%").replace("_", "\\_");
        String escapedType = type.replace("'", "\\'").replace("%", "\\%").replace("_", "\\_");

        return getCampaignByQuery(Constants.SELECT + QUERY_FIELDS + Constants.FROM_CAMPAIGN + "where name LIKE '%" +
                escapedName + "%' AND Type LIKE '%" + escapedType + "%' " + Constants.ORDER_BY +
                orderByField + Constants.SPACE +
                orderByOperand + Constants.LIMIT + size + Constants.OFFSET + Math.max((page - 1) * size, 0));
    }

    @Override
    public List<Campaign> getCampaignByType(String type, long size, long page, String orderByField, String orderByOperand) {
        // Escape single quotes and SOQL wildcards to prevent SOQL injection
        String escapedType = type.replace("'", "\\'").replace("%", "\\%").replace("_", "\\_");

        return getCampaignByQuery(
                Constants.SELECT + QUERY_FIELDS + Constants.FROM_CAMPAIGN +
                        "where type LIKE '%" + escapedType + "%' " +
                        Constants.ORDER_BY +
                        orderByField + Constants.SPACE +
                        orderByOperand +
                        Constants.LIMIT + size +
                        Constants.OFFSET + Math.max((page - 1) * size, 0));
    }

    @Override
    public List<Campaign> getCampaignByStatus(String status, long size, long page, String orderByField, String orderByOperand) {
        // Escape single quotes and SOQL wildcards to prevent SOQL injection
        String escapedStatus = status.replace("'", "\\'").replace("%", "\\%").replace("_", "\\_");

        return getCampaignByQuery(
                Constants.SELECT + QUERY_FIELDS + Constants.FROM_CAMPAIGN +
                        "where status LIKE '%" + escapedStatus + "%' " +
                        Constants.ORDER_BY + orderByField + Constants.SPACE + orderByOperand +
                        Constants.LIMIT + size +
                        Constants.OFFSET + Math.max((page - 1) * size, 0));
    }

    @Override
    public Campaign getCampaignById(String campaignId) {
        return getCampaignByQuery(Constants.SELECT + QUERY_FIELDS + Constants.FROM_CAMPAIGN + "where id = '" + campaignId + "'").stream().findFirst().orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public String addCampaign(Map<String, Object> entityAttributesField) {
        var response = Constants.GSON.fromJson(salesforceManager.createRecord(Constants.GSON.toJson(SFCampaign.from(entityAttributesField)), Constants.CAMPAIGN), Map.class);
        return String.valueOf(response.get(ID));
    }

    @Override
    public void updateCampaign(Campaign campaign) {
        if (campaign.getId() == null || campaign.getId().isEmpty())
            throw new IllegalStateException(Constants.CAMPAIGN_ID_CAN_T_BE_EMPTY);
        SFCampaign updatedCampaign = SFCampaign.from(CatalogTypes.fromSFCampaign(campaign));
        SFCampaign oldCampaign = SFCampaign.from(CatalogTypes.fromSFCampaign(getCampaignById(updatedCampaign.Id)));
        Map<String, Object> diff = SalesforceManager.getDifference(oldCampaign, updatedCampaign);
        salesforceManager.updateRecord(updatedCampaign.Id, GSON.toJson(diff), Constants.CAMPAIGN);
    }

    @Override
    public void deleteCampaign(String campaignId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(THIS_REQUEST_IS_NOT_YET_IMPLEMENTED);
    }

    private List<Campaign> getCampaignByQuery(String query) {
        String res = salesforceManager.getRecordByQuery(query);
        SalesforceListResponse<SFCampaign> oppList =
                GSON.fromJson(res, new TypeToken<SalesforceListResponse<SFCampaign>>() {
                }.getType());
        if (!oppList.records.isEmpty()) {
            return oppList.records.stream().map(CampaignImpl::new).collect(Collectors.toList());
        } else {
            return List.of();
        }
    }
}
