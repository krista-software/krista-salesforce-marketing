package app.krista.extensions.crm.marketing_management.salesforce_marketing.catalog;

import java.util.Map;
import java.util.List;
import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.*;
import app.krista.ksdk.entities.EntityReference;
import app.krista.model.base.*;

@Domain(id = "catEntryDomain_44aee5a3-7112-45a1-a751-7127535d9ec6",
        name = "Marketing Management",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "717f1f7e-c727-4fa5-9df7-0993b48f04de")
public class ContentArea {

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
            @Field.Desc(name = "Custom Fields", type = "[ Text ]",required = true) List<String> customFields) {
        throw new UnsupportedOperationException("This method is not yet implemented! If you don't want to support this method, please remove the complete method");
    }

}