package org.smartregister.chw.cdp.model;

import org.json.JSONObject;
import org.smartregister.chw.cdp.contract.BaseOrderDetailsContract;
import org.smartregister.chw.cdp.util.CdpJsonFormUtils;

public class BaseOrderDetailsModel implements BaseOrderDetailsContract.Model {
    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String condomType) throws Exception {
        JSONObject form = CdpJsonFormUtils.getFormAsJson(formName);
        JSONObject global = form.getJSONObject("global");
        if (condomType != null) {
            global.put("condom_type", condomType);
        }
        return form;
    }
}
