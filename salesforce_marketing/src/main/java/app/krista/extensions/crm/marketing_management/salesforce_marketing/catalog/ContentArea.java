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

import javax.inject.Inject;
import java.util.List;

@Domain(id = "catEntryDomain_44aee5a3-7112-45a1-a751-7127535d9ec6",
        name = "Marketing Management",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "717f1f7e-c727-4fa5-9df7-0993b48f04de")
public class ContentArea {
    private final SalesforceManager salesforceManager;

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
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : customFields) {
            if (!str.endsWith("__c")) {
                System.out.println(str + " is not custom field");
                return new FreeForm();
            }
            stringBuilder.append(str).append(",");
        }
        if (!customFields.isEmpty()) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        String queryFields = stringBuilder.toString();
        String query = "Select " + queryFields + " from " + objectType + " where id = '" + objectId + "'";
        String recordByQuery = salesforceManager.getRecordByQuery(query);
        if (recordByQuery != null) {
            return parseJSONToMap(recordByQuery, customFields);
        }
        return new FreeForm();
    }

    public static FreeForm parseJSONToMap(String jsonResponse, List<String> customFields) {
        FreeForm resultMap = new FreeForm();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray records = jsonObject.getJSONArray("records");
            JSONObject record = records.getJSONObject(0);

            for (String key : customFields) {
                if (record.has(key)) {
                    Object value = record.get(key);
                    if (value.equals(null)) {
                        resultMap.put(key, "Text", "");
                    } else {
                        resultMap.put(key, "Text", value);
                    }

                }
            }

        } catch (JSONException exception) {
            System.out.println("Exception:" + exception.getMessage());
        }
        return resultMap;
    }

}