package org.smartregister.chw.cdp.model;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.smartregister.chw.cdp.contract.BaseOrdersRegisterFragmentContract;
import org.smartregister.chw.cdp.util.CdpJsonFormUtils;
import org.smartregister.chw.cdp.util.Constants;
import org.smartregister.chw.cdp.util.DBConstants;
import org.smartregister.cursoradapter.SmartRegisterQueryBuilder;

import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;

public class BaseOrdersRegisterFragmentModel implements BaseOrdersRegisterFragmentContract.Model {
    @Override
    public String countSelect(String tableName, String mainCondition) {
        SmartRegisterQueryBuilder countQueryBuilder = new SmartRegisterQueryBuilder();
        countQueryBuilder.selectInitiateMainTableCounts(tableName);
        return countQueryBuilder.mainCondition(mainCondition);
    }

    @NonNull
    @Override
    public String mainSelect(@NonNull String tableName, @NonNull String mainCondition) {
        SmartRegisterQueryBuilder queryBuilder = new SmartRegisterQueryBuilder();
        queryBuilder.selectInitiateMainTable(tableName, mainColumns(tableName), DBConstants.KEY.LOCATION_ID);
        queryBuilder.selectInitiateMainTable(tableName, mainColumns(tableName));
        queryBuilder.customJoin("INNER JOIN " + Constants.TABLES.TASK + " ON  " + tableName + "." + DBConstants.KEY.BASE_ENTITY_ID + " = " + Constants.TABLES.TASK + "." + DBConstants.KEY.FOR + " COLLATE NOCASE ");
        return queryBuilder.mainCondition(mainCondition);
    }

    @Override
    public JSONObject getOrderFormAsJson(String formName) throws Exception {
        if (formName.equalsIgnoreCase(Constants.FORMS.CDP_CONDOM_ORDER_FACILITY)) {
            JSONObject form = CdpJsonFormUtils.getFormAsJson(formName);
            CdpJsonFormUtils.initializeHealthFacilitiesList(form);
            return form;
        }
        return CdpJsonFormUtils.getFormAsJson(formName);
    }

    @NotNull
    public String[] mainColumns(String tableName) {
        Set<String> columnList = new HashSet<>();
        columnList.add(tableName + "." + DBConstants.KEY.LOCATION_ID);
        columnList.add(tableName + "." + DBConstants.KEY.LOCATION_ID + " AS " + DBConstants.KEY.RELATIONAL_ID);
        columnList.add(tableName + "." + DBConstants.KEY.CONDOM_TYPE);
        columnList.add(tableName + "." + DBConstants.KEY.CONDOM_BRAND);
        columnList.add(tableName + "." + DBConstants.KEY.QUANTITY_REQ);
        columnList.add(tableName + "." + DBConstants.KEY.REQUEST_TYPE);
        columnList.add(Constants.TABLES.TASK + "." + DBConstants.KEY.STATUS);
        columnList.add(Constants.TABLES.TASK + "." + DBConstants.KEY.AUTHORED_ON + " AS " + DBConstants.KEY.REQUESTED_AT);
        columnList.add(Constants.TABLES.TASK + "." + DBConstants.KEY.ID + " AS " + DBConstants.KEY.TASK_ID);
        columnList.add(Constants.TABLES.TASK + "." + DBConstants.KEY.REQUESTER);

        return columnList.toArray(new String[columnList.size()]);
    }
}
