package app.krista.extensions.crm.marketing_management.salesforce_marketing.service;

public interface Campaign {

    /**
     * This function will return the name of the campaign
     *
     * @return name of campaign
     */
    String getName();

    /**
     * This function will return Id of the campaign
     *
     * @return Id of the campaign
     */
    String getId();

    /**
     * This function will return Number Sent attribute value
     *
     * @return Number sent in the campaign
     */
    Double getNumberSent();

    /**
     * This function will return number of leads attribute value
     *
     * @return number of leads in campaign
     */
    Double getNumberOfLeads();

    /**
     * This function will return number of converted leads attribute value
     *
     * @return number of converted leads in campaign
     */
    Double getNumberOfConvertedLeads();

    /**
     * This function will return number of contacts attribute value
     *
     * @return number of contacts in campaign
     */
    Double getNumberOfContacts();

    /**
     * This function will return number of responses attribute value
     *
     * @return number of responses in campaign
     */
    Double getNumberOfResponses();

    /**
     * This function will return number of opportunities attribute value
     *
     * @return number of opportunities in campaign
     */
    Double getNumberOfOpportunities();

    /**
     * This function will return number of won opportunities attribute value
     *
     * @return number of won opportunities in campaign
     */
    Double getNumberOfWonOpportunities();

    /**
     * This function will return created date attribute value
     *
     * @return created date of the campaign
     */
    Long getCreatedDate();

    /**
     * This function will return last modified date attribute value
     *
     * @return last modified date of the campaign
     */
    Long getLastModifiedDate();

    /**
     * This function will return owner Id attribute value
     *
     * @return owner If of the campaign
     */
    String getOwnerId();

    /**
     * This function will return created by Id attribute value
     *
     * @return created by Id of the campaign
     */
    String getCreatedById();

    /**
     * This function will return type attribute value
     *
     * @return type of the campaign
     */
    String getType();

    /**
     * This function will return status attribute value
     *
     * @return status of the campaign
     */
    String getStatus();

    /**
     * This function will return description attribute value
     *
     * @return description of the campaign
     */
    String getDescription();

    /**
     * This function will return start date attribute value
     *
     * @return start date of the campaign
     */
    Long getStartDate();

    /**
     * This function will return end date attribute value
     *
     * @return end date of the campaign
     */
    Long getEndDate();

    /**
     * This function will return budgeted cost attribute value
     *
     * @return budgeted cost of the campaign
     */
    Double getBudgetedCost();

    /**
     * This function will return expected revenue attribute value
     *
     * @return expected revenue of the campaign
     */
    Double getExpectedRevenue();

    /**
     * This function will return actual cost attribute value
     *
     * @return actual cost of the campaign
     */
    Double getActualCost();

    /**
     * This function will return expected response attribute value
     *
     * @return expected response of the campaign
     */
    Double getExpectedResponse();

    /**
     * This function will return amount all opportunities attribute value
     *
     * @return amount of all opportunities of the campaign
     */
    Double getAmountAllOpportunities();

    /**
     * This function will return amount of won opportunities attribute value
     *
     * @return amount of won opportunities of the campaign
     */
    Double getAmountWonOpportunities();
}
