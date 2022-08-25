package org.smartregister.chw.cdp.model;

import org.json.JSONObject;
import org.smartregister.chw.cdp.contract.BaseCdpRegisterContract;
import org.smartregister.chw.cdp.util.TestJsonFormUtils;

public class BaseCdpRegisterModel implements BaseCdpRegisterContract.Model {

    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception {
        JSONObject jsonObject = TestJsonFormUtils.getFormAsJson(formName);
        TestJsonFormUtils.getRegistrationForm(jsonObject, entityId, currentLocationId);

        return jsonObject;
    }

}
