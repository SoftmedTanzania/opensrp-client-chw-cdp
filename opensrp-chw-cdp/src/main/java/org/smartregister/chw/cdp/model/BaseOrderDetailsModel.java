package org.smartregister.chw.cdp.model;

import static org.smartregister.chw.cdp.dao.CdpStockingDao.getCurrentCondomCountByBrand;
import static org.smartregister.chw.cdp.dao.CdpStockingDao.getCurrentFemaleCondomCount;
import static org.smartregister.chw.cdp.dao.CdpStockingDao.getCurrentMaleCondomCount;

import org.json.JSONObject;
import org.smartregister.chw.cdp.contract.BaseOrderDetailsContract;
import org.smartregister.chw.cdp.dao.CdpStockingDao;
import org.smartregister.chw.cdp.util.CdpJsonFormUtils;
import org.smartregister.util.Utils;

import java.util.List;

public class BaseOrderDetailsModel implements BaseOrderDetailsContract.Model {
    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String condomType) throws Exception {
        JSONObject form = CdpJsonFormUtils.getFormAsJson(formName);
        String userLocationId = Utils.getAllSharedPreferences().fetchUserLocalityId(Utils.getAllSharedPreferences().fetchRegisteredANM());
        JSONObject global = form.getJSONObject("global");
        if (condomType != null) {
            global.put("condom_type", condomType);
            global.put("male_condom_count", getCurrentMaleCondomCount(userLocationId));
            global.put("female_condom_count", getCurrentFemaleCondomCount(userLocationId));

            List<String> condomBrands = CdpStockingDao.getCondomBrands();
            for (String condomBrand : condomBrands) {
                global.put("male_condom_"+ condomBrand+"_count", getCurrentCondomCountByBrand(condomBrand, CdpStockingDao.CondomStockLog.CondomType.MALE));
                global.put("female_condom_"+ condomBrand+"_count", getCurrentCondomCountByBrand(condomBrand, CdpStockingDao.CondomStockLog.CondomType.FEMALE));
            }
        }
        return form;
    }

    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String condomType, String quantity) throws Exception {
        JSONObject form = CdpJsonFormUtils.getFormAsJson(formName);
        JSONObject global = form.getJSONObject("global");
        if (condomType != null && quantity != null) {
            global.put("condom_type", condomType);
            global.put("condom_quantity", quantity);

            List<String> condomBrands = CdpStockingDao.getCondomBrands();
            for (String condomBrand : condomBrands) {
                global.put("male_condom_"+ condomBrand+"_count", getCurrentCondomCountByBrand(condomBrand, CdpStockingDao.CondomStockLog.CondomType.MALE));
                global.put("female_condom_"+ condomBrand+"_count", getCurrentCondomCountByBrand(condomBrand, CdpStockingDao.CondomStockLog.CondomType.FEMALE));
            }
        }
        return form;
    }
}
