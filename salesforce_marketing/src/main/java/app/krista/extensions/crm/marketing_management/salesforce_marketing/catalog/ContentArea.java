package app.krista.extensions.crm.marketing_management.salesforce_marketing.catalog;

import app.krista.extension.impl.anno.Attribute;
import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.ContentDaoImpl;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.SalesforceManager;
import app.krista.model.base.FreeForm;
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
    private final ContentDaoImpl contentDao;

    @Inject
    public ContentArea(SalesforceManager salesforceManager, ContentDaoImpl contentDao) {
        this.salesforceManager = salesforceManager;
        this.contentDao = contentDao;
    }

    @CatalogRequest(
            id = "localDomainRequest_334724ad-bc47-4eba-a03f-60aa88a4d5c2",
            name = "Retrieve Custom Fields For Given Object",
            description = "Retrieve Custom Fields For Given Object",
            area = "Content",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field(name = "Field Details", type = "FreeForm", required = false)
    public FreeForm retrieveCustomFieldsForGivenObject(
            @Field.Text(name = "Object Id", attributes = {@Attribute(name = "visualWidth", value = "S")}) String objectId,
            @Field.PickOne(name = "Object Type", values = {"Campaign"}, attributes = {@Attribute(name = "visualWidth", value = "S")}) String objectType,
            @Field.Desc(name = "Custom Fields", type = "[ Text ]") List<String> customFields) {
        StringBuilder customField = new StringBuilder();
        for (String str : customFields) {
            if (!str.endsWith("__c")) { // custom fields ends with __c
                LOGGER.info("{} is not a custom field", str);
                return new FreeForm();
            }
            customField.append(str).append(",");
        }
        if (!customFields.isEmpty()) {
            customField.deleteCharAt(customField.length() - 1);
        }
        String queryFields = customField.toString();
        String query = "Select " + queryFields + " from " + objectType + " where id = '" + objectId + "'";
        String recordByQuery = salesforceManager.getRecordByQuery(query);
        if (recordByQuery != null) {
            return contentDao.parseJSONToMap(recordByQuery, customFields);
        }
        return new FreeForm();
    }

}