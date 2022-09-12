package org.smartregister.chw.cdp.model;

import static org.smartregister.chw.cdp.dao.CdpStockingDao.getCurrentFemaleCondomCount;
import static org.smartregister.chw.cdp.dao.CdpStockingDao.getCurrentMaleCondomCount;

import org.json.JSONObject;
import org.smartregister.chw.cdp.contract.RestockingHistoryContract;
import org.smartregister.chw.cdp.util.CdpJsonFormUtils;
import org.smartregister.util.Utils;

public class BaseRestockingHistoryModel implements RestockingHistoryContract.Model {
    @Override
    public JSONObject getFormAsJson(String formName, String entityId) throws Exception {
        JSONObject form = CdpJsonFormUtils.getFormAsJson(formName);
        String userLocationId = Utils.getAllSharedPreferences().fetchUserLocalityId(Utils.getAllSharedPreferences().fetchRegisteredANM());
        form.getJSONObject("global").put("male_condom_count", getCurrentMaleCondomCount(userLocationId));
        form.getJSONObject("global").put("female_condom_count", getCurrentFemaleCondomCount(userLocationId));
        CdpJsonFormUtils.getRegistrationForm(form, entityId, "");
        return form;
    }
}
