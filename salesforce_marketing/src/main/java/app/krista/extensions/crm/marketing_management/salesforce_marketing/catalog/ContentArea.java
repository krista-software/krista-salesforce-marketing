package app.krista.extensions.crm.marketing_management.salesforce_marketing.catalog;

import app.krista.extension.impl.anno.Attribute;
import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.SalesforceManager;
import app.krista.model.base.FreeForm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

@Domain(id = "catEntryDomain_44aee5a3-7112-45a1-a751-7127535d9ec6",
        name = "Marketing Management",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "717f1f7e-c727-4fa5-9df7-0993b48f04de")
public class ContentArea {
    private final SalesforceManager salesforceManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentArea.class);

    @Inject
    public ContentArea(SalesforceManager salesforceManager) {
        this.salesforceManager = salesforceManager;
    }

    @CatalogRequest(
            id = "localDomainRequest_334724ad-bc47-4eba-a03f-60aa88a4d5c2",
            name = "Retrieve Custom Fields For Given Object",
            description = "Retrieve Custom Fields For Given Object",
            area = "Content",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field(name = "Field Details", type = "FreeForm", required = false, attributes = {}, options = {})
    public FreeForm retrieveCustomFieldsForGivenObject(
            @Field.Text(name = "Object Id", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}) String objectId,
            @Field.PickOne(name = "Object Type", values = {"Campaign"}, required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}) String objectType,
            @Field.Desc(name = "Custom Fields", type = "[ Text ]", required = true) List<String> customFields) {
        StringBuilder stringBuilderForCustomFields = new StringBuilder();
        for (String str : customFields) {
            if (!str.endsWith("__c")) { // custom fields ends with __c
                LOGGER.info("{} is not a custom field", str);
                return new FreeForm();
            }
            stringBuilderForCustomFields.append(str).append(",");
        }
        if (!customFields.isEmpty()) {
            stringBuilderForCustomFields.deleteCharAt(stringBuilderForCustomFields.length() - 1);
        }
        String queryFields = stringBuilderForCustomFields.toString();
        String query = "Select " + queryFields + " from " + objectType + " where id = '" + objectId + "'";
        String recordByQuery = salesforceManager.getRecordByQuery(query);
        if (recordByQuery != null) {
            return parseJSONToMap(recordByQuery, customFields);
        }
        return new FreeForm();
    }

    private static FreeForm parseJSONToMap(String jsonResponse, List<String> customFields) {
        FreeForm resultMap = new FreeForm();
        try {
            processJSONResponse(jsonResponse, customFields, resultMap);
        } catch (JSONException exception) {
            LOGGER.info("Exception occurred due to :" + exception.getMessage(), exception);
        }
        return resultMap;
    }

    private static void processJSONResponse(String jsonResponse, List<String> customFields, FreeForm resultMap) {
        if (jsonResponse != null) {
            JSONObject jsonObject = new JSONObject(jsonResponse);

            if (jsonObject.has("records")) {
                JSONArray records = jsonObject.getJSONArray("records");

                if (records.length() > 0) {
                    JSONObject record = records.getJSONObject(0);

                    if (record != null) {
                        for (String key : customFields) {
                            if (record.has(key)) {
                                Object value = record.get(key);

                                if (value.equals(null)) {
                                    resultMap.put(key, "Text", "");
                                } else {
                                    resultMap.put(key, "Text", value.toString());
                                }
                            }
                        }
                    } else {
                        LOGGER.info("First record is null.");
                    }
                } else {
                    LOGGER.info("No records found in the JSON response.");
                }
            } else {
                LOGGER.info("No 'records' array found in the JSON response.");
            }
        } else {
            LOGGER.info("JSON response is null.");
        }
    }

}