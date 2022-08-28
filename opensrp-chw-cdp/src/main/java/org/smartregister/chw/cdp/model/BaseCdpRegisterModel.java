package org.smartregister.chw.cdp.model;

import org.json.JSONObject;
import org.smartregister.chw.cdp.contract.BaseCdpRegisterContract;
import org.smartregister.chw.cdp.pojo.CdpOutletEventClient;
import org.smartregister.chw.cdp.util.CdpJsonFormUtils;
import org.smartregister.chw.cdp.util.OutletUtil;

import java.util.List;

public class BaseCdpRegisterModel implements BaseCdpRegisterContract.Model {

    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception {
        JSONObject jsonObject = CdpJsonFormUtils.getFormAsJson(formName);
        CdpJsonFormUtils.getRegistrationForm(jsonObject, entityId, currentLocationId);

        return jsonObject;
    }

    @Override
    public List<CdpOutletEventClient> processRegistration(String jsonString) {
        return OutletUtil.getOutletEventClient(jsonString);
    }


}
