package app.krista.extensions.crm.marketing_management.salesforce_marketing.service;

import java.util.List;
import java.util.Map;

public interface CampaignDao {


    /**
     * This function will return {@link List} of {@link Campaign} objects
     *
     * @param size           number of campaigns to be fetched
     * @param page           page number to be fetched
     * @param orderByField   field of campaign to sort the list
     * @param orderByOperand type of sorting order
     * @return {@link List<Campaign>} list of salesforce campaign objects
     */
    List<Campaign> getCampaigns(long size, long page, String orderByField, String orderByOperand);

    /**
     * This function will return {@link List} of {@link Campaign} objects
     *
     * @param name           name of the campaign
     * @param size           number of campaigns to be fetched
     * @param page           page number to be fetched
     * @param orderByField   field of campaign to sort the list
     * @param orderByOperand type of sorting order
     * @return {@link List<Campaign>} list of salesforce campaign objects
     */
    List<Campaign> getCampaignByName(String name, long size, long page, String orderByField, String orderByOperand);

    /**
     * Returns {@link List<Campaign>} list of salesforce campaign objects
     *
     * @param name           name of the campaign
     * @param type           type of the campaign
     * @param size           number of campaigns to be fetched
     * @param page           page number to be fetched
     * @param orderByField   field of campaign to sort the list
     * @param orderByOperand type of sorting order
     * @return {@link List<Campaign>} list of salesforce campaign objects
     */
    List<Campaign> getCampaignByName(String name, String type, long size, long page, String orderByField, String orderByOperand);

    /**
     * Returns {@link List<Campaign>} list of salesforce campaign objects
     *
     * @param type           type of the campaign
     * @param size           number of campaigns to be fetched
     * @param page           page number to be fetched
     * @param orderByField   field of campaign to sort the list
     * @param orderByOperand type of sorting order
     * @return {@link List<Campaign>} list of salesforce campaign objects
     */
    List<Campaign> getCampaignByType(String type, long size, long page, String orderByField, String orderByOperand);

    /**
     * Returns {@link List<Campaign>} list of salesforce campaign objects
     *
     * @param status         status of the campaign
     * @param size           number of campaigns to be fetched
     * @param page           page number to be fetched
     * @param orderByField   field of campaign to sort the list
     * @param orderByOperand type of sorting order
     * @return {@link List<Campaign>} list of salesforce campaign objects
     */
    List<Campaign> getCampaignByStatus(String status, long size, long page, String orderByField, String orderByOperand);

    /**
     * Returns {@link Campaign} object of salesforce campaign
     *
     * @param campaignId opportunity id of the campaign
     * @return {@link Campaign} salesforce campaign object
     */
    Campaign getCampaignById(String campaignId);

    /**
     * Add {@link Campaign} to salesforce from provided input fields
     * Returns id of the new salesforce campaign
     *
     * @param entityAttributeField fields which are added in the {@link Campaign} object of salesforce to create
     * @return id of the new salesforce campaign
     */
    String addCampaign(Map<String, Object> entityAttributeField);

    /**
     * Update {@link Campaign} to salesforce from provided input fields
     *
     * @param campaign {@link Campaign} object of salesforce to be updated
     */
    void updateCampaign(Campaign campaign);

    /**
     * This function will delete campaign from salesforce
     *
     * @param campaignId id of the salesforce campaign
     */
    void deleteCampaign(String campaignId) throws UnsupportedOperationException;

}
