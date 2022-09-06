package org.smartregister.chw.cdp.model;

import org.json.JSONObject;
import org.smartregister.chw.cdp.contract.BaseOrderDetailsContract;

public class BaseOrderDetailsModel implements BaseOrderDetailsContract.Model {
    @Override
    public JSONObject getFormAsJson(String formName, String entityId) throws Exception {
        return null;
    }
}
