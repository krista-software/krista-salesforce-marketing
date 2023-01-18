package app.krista.extensions.crm.marketing_management.salesforce_marketing.util;

import java.util.List;

public final class SalesforceListResponse<T> {

    public int totalSize;
    public boolean done;
    public List<T> records;
}
