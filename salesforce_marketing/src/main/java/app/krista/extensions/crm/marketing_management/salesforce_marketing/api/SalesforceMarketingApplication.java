package app.krista.extensions.crm.marketing_management.salesforce_marketing.api;

import org.jvnet.hk2.annotations.ContractsProvided;
import org.jvnet.hk2.annotations.Service;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

@Service
@ApplicationPath("salesforcemarketing")
@ContractsProvided(Application.class)
public class SalesforceMarketingApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return Set.of(AuthenticationResource.class);
    }
}
