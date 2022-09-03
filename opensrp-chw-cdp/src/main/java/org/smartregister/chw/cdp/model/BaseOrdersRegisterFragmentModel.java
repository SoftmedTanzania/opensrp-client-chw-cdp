package org.smartregister.chw.cdp.model;

import org.jetbrains.annotations.NotNull;
import org.smartregister.chw.cdp.contract.BaseOrdersRegisterFragmentContract;
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
    public String mainSelect(@NonNull String tableName, String entityTable, @NonNull String mainCondition) {
        SmartRegisterQueryBuilder queryBuilder = new SmartRegisterQueryBuilder();
        queryBuilder.selectInitiateMainTable(tableName, mainColumns(tableName), DBConstants.KEY.BASE_ENTITY_ID);
        queryBuilder.selectInitiateMainTable(tableName, mainColumns(tableName));
        queryBuilder.customJoin("INNER JOIN " + Constants.TABLES.CDP_OUTLET + " ON  " + tableName+ "." +DBConstants.KEY.BASE_ENTITY_ID  + " = " + Constants.TABLES.CDP_OUTLET + "." + DBConstants.KEY.BASE_ENTITY_ID + " COLLATE NOCASE ");
        return queryBuilder.mainCondition(mainCondition);
    }

    @NotNull
    public String[] mainColumns(String tableName) {
        Set<String> columnList = new HashSet<>();
        columnList.add(tableName + "." + DBConstants.KEY.BASE_ENTITY_ID);
        columnList.add(Constants.TABLES.CDP_OUTLET + "." + DBConstants.KEY.RELATIONAL_ID);
        columnList.add(Constants.TABLES.CDP_OUTLET+ "." + DBConstants.KEY.OUTLET_NAME);
        columnList.add(Constants.TABLES.CDP_OUTLET+ "." + DBConstants.KEY.OUTLET_TYPE);
        columnList.add(Constants.TABLES.CDP_OUTLET+ "." + DBConstants.KEY.OUTLET_VILLAGE_STREET_NAME);
        columnList.add(Constants.TABLES.CDP_OUTLET+ "." + DBConstants.KEY.OUTLET_WARD_NAME);
        columnList.add(Constants.TABLES.CDP_OUTLET+ "." + DBConstants.KEY.FOCAL_PERSON_NAME);
        columnList.add(Constants.TABLES.CDP_OUTLET+ "." + DBConstants.KEY.FOCAL_PERSON_PHONE);

        return columnList.toArray(new String[columnList.size()]);
    }
}
