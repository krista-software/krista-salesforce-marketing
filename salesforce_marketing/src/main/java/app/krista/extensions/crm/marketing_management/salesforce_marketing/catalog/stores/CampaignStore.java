/*
 * Salesforce Marketing Extension for Krista
 * Copyright (C) 2025 Krista Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package app.krista.extensions.crm.marketing_management.salesforce_marketing.catalog.stores;

import app.krista.extension.executor.QueryClause;
import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extension.util.EntityStore;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.catalog.CatalogTypes;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.catalog.entity.Campaign;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.CampaignDaoImpl;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.impl.CampaignImpl;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.model.SFCampaign;
import app.krista.extensions.crm.marketing_management.salesforce_marketing.util.Constants;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CampaignStore implements EntityStore<Campaign> {

    private final CampaignDaoImpl campaignDaoImpl;

    @Inject
    public CampaignStore(CampaignDaoImpl campaignDao) {
        this.campaignDaoImpl = campaignDao;
    }

    @Override
    public Campaign create(Map<String, Object> entityAttributesField) {
        if (entityAttributesField == null || entityAttributesField.isEmpty()) {
            throw new IllegalArgumentException(Constants.FAILED_TO_CREATE_CAMPAIGN + Constants.ENTITY_INPUT_IS_NULL_OR_EMPTY);
        }
        try {
            String campaignId = campaignDaoImpl.addCampaign(entityAttributesField);
            return get(campaignId);
        } catch (IllegalArgumentException cause) {
            throw new IllegalArgumentException(Constants.FAILED_TO_CREATE_CAMPAIGN);
        }
    }

    @Override
    public Campaign get(String primaryKey) {
        if (primaryKey == null || primaryKey.isBlank()) {
            throw new IllegalArgumentException("Failed to retrieve campaign. " + Constants.PRIMARY_KEY_IS_EMPTY);
        }
        try {
            app.krista.extensions.crm.marketing_management.salesforce_marketing.service.Campaign campaign = campaignDaoImpl.getCampaignById(primaryKey);
            return CatalogTypes.fromSFCampaign(campaign);
        } catch (IllegalArgumentException cause) {
            throw new IllegalArgumentException(cause.getMessage());
        }
    }

    @Override
    public Campaign update(Campaign campaign) {
        try {
            campaignDaoImpl.updateCampaign(new CampaignImpl(SFCampaign.from(campaign)));
            return get(campaign.id);
        } catch (IllegalArgumentException cause) {
            throw new IllegalArgumentException(Constants.FAILED_TO_UPDATE_CAMPAIGN + cause.getMessage());
        }
    }

    @Override
    public void delete(String primaryKey) {
        try {
            campaignDaoImpl.deleteCampaign(primaryKey);
        } catch (IllegalArgumentException cause) {
            throw new IllegalArgumentException(Constants.FAILED_TO_DELETE_CAMPAIGN + cause.getMessage());
        }
    }

    @Override
    public boolean contains(String s) {
        return false;
    }


    @Override
    @SuppressWarnings({"deprecation", "RedundantSuppression"})
    public List<Campaign> search(List<SearchCondition> conditions, long pageIndex, int pageSize) {
        return Collections.emptyList();
    }

    @Override
    public List<Campaign> search(SearchQuery searchQuery, long pageIndex, int pageSize) {
        final List<SearchCondition> conditions = searchQuery.getSearchConditions();
        final List<QueryClause> queryClauses = searchQuery.getQueryClauses();
        if (conditions == null || conditions.isEmpty()) {
            throw new IllegalArgumentException(Constants.FAILED_TO_SEARCH_FOR_CAMPAIGN + Constants.CONDITIONS_NOT_FOUND);
        }
        try {
            List<Campaign> campaigns = new ArrayList<>();
            String type = getConditionValue(conditions);
            String orderByField = queryClauses.isEmpty() ? Constants.LASTMODIFIEDDATE : queryClauses.get(0).getFieldName();
            String orderByOperand = queryClauses.isEmpty() ? Constants.DESC : queryClauses.get(0).getOperand().toString();

            for (SearchCondition con : conditions) {
                String matchPattern = con.getOperand() != null ? String.valueOf(con.getOperand()).trim() : "";
                String selector = con.getFieldName();
                if (Constants.NAME.equalsIgnoreCase(selector)) {
                    campaigns = campaignDaoImpl.getCampaignByName(matchPattern, type, pageSize, pageIndex, orderByField, orderByOperand).stream().map(CatalogTypes::fromSFCampaign).collect(Collectors.toList());
                } else if (Constants.STATUS.equalsIgnoreCase(selector)) {
                    campaigns = campaignDaoImpl.getCampaignByStatus(matchPattern, pageSize, pageIndex, orderByField, orderByOperand).stream().map(CatalogTypes::fromSFCampaign).collect(Collectors.toList());
                } else if (Constants.TYPE.equalsIgnoreCase(selector)) {
                    campaigns = campaignDaoImpl.getCampaignByType(matchPattern, pageSize, pageIndex, orderByField, orderByOperand).stream().map(CatalogTypes::fromSFCampaign).collect(Collectors.toList());
                }
            }
            if (conditions.size() == 1 && campaigns.isEmpty()) {
                campaigns = campaignDaoImpl.getCampaigns(pageSize, pageIndex, orderByField, orderByOperand).stream().map(CatalogTypes::fromSFCampaign).collect(Collectors.toList());
            }
            return campaigns;
        } catch (IllegalArgumentException cause) {
            throw new IllegalArgumentException(Constants.FAILED_TO_SEARCH_FOR_CAMPAIGN + cause.getMessage());
        }
    }

    @Override
    public long count(List<SearchCondition> list) {
        return 0;
    }

    @Override
    public List<String> lookup(Map<String, Object> map, List<String> list) {
        return Collections.emptyList();
    }

    String getConditionValue(List<SearchCondition> conditions) {
        for (SearchCondition con : conditions) {
            if (Constants.TYPE.equalsIgnoreCase(con.getFieldName())) {
                return con.getOperand() != null ? String.valueOf(con.getOperand()).trim() : Constants.EMPTY_STRING;
            }
        }
        return Constants.EMPTY_STRING;
    }
}
