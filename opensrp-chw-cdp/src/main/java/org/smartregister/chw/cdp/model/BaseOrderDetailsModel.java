package org.smartregister.chw.cdp.model;

import org.json.JSONObject;
import org.smartregister.chw.cdp.contract.BaseOrderDetailsContract;
import org.smartregister.chw.cdp.util.CdpJsonFormUtils;

public class BaseOrderDetailsModel implements BaseOrderDetailsContract.Model {
    @Override
    public JSONObject getFormAsJson(String formName, String entityId) throws Exception {
        return CdpJsonFormUtils.getFormAsJson(formName);
    }
}
