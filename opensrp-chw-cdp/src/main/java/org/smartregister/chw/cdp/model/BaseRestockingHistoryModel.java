package org.smartregister.chw.cdp.model;

import org.json.JSONObject;
import org.smartregister.chw.cdp.contract.RestockingHistoryContract;
import org.smartregister.chw.cdp.util.CdpJsonFormUtils;

public class BaseRestockingHistoryModel implements RestockingHistoryContract.Model {
    @Override
    public JSONObject getFormAsJson(String formName) throws Exception {
        return CdpJsonFormUtils.getFormAsJson(formName);
    }
}
