package org.smartregister.chw.cdp.model;

import org.json.JSONObject;
import org.smartregister.chw.cdp.contract.BaseCdpProfileContract;
import org.smartregister.chw.cdp.util.CdpJsonFormUtils;

public class BaseCdpProfileModel implements BaseCdpProfileContract.Model {
    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception {
        JSONObject form = CdpJsonFormUtils.getFormAsJson(formName);
        CdpJsonFormUtils.getRegistrationForm(form, entityId, currentLocationId);

        return form;
    }
}
