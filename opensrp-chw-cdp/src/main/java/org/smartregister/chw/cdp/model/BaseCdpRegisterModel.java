package org.smartregister.chw.cdp.model;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.smartregister.chw.cdp.contract.BaseCdpRegisterContract;
import org.smartregister.chw.cdp.pojo.CdpOutletEventClient;
import org.smartregister.chw.cdp.util.CdpJsonFormUtils;
import org.smartregister.chw.cdp.util.Constants;
import org.smartregister.chw.cdp.util.OutletUtil;
import org.smartregister.util.JsonFormUtils;

import java.util.List;

import timber.log.Timber;

import static com.vijay.jsonwizard.utils.FormUtils.fields;
import static com.vijay.jsonwizard.utils.FormUtils.getFieldJSONObject;
import static org.smartregister.chw.cdp.util.CdpJsonFormUtils.METADATA;
import static org.smartregister.util.JsonFormUtils.ENCOUNTER_LOCATION;
import static org.smartregister.util.JsonFormUtils.STEP1;

public class BaseCdpRegisterModel implements BaseCdpRegisterContract.Model {

    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception {
        if (formName.equals(Constants.FORMS.CDP_OUTLET_REGISTRATION)) {
            try {
                JSONObject form = CdpJsonFormUtils.getFormAsJson(formName);
                if (form == null) {
                    return null;
                }

                form.getJSONObject(METADATA).put(ENCOUNTER_LOCATION, currentLocationId);
                JSONObject stepOneUniqueId = getFieldJSONObject(fields(form, STEP1), Constants.JSON_FORM_KEY.UNIQUE_ID);

                String newEntityId = entityId;
                if (StringUtils.isNotBlank(entityId)) {
                    newEntityId = entityId.replace("-", "");
                }

                if (stepOneUniqueId != null) {
                    stepOneUniqueId.remove(JsonFormUtils.VALUE);
                    stepOneUniqueId.put(JsonFormUtils.VALUE, newEntityId);
                }
                return form;
            } catch (Exception e) {
                Timber.e(e);
                return null;
            }
        } else {
            JSONObject form = CdpJsonFormUtils.getFormAsJson(formName);
            CdpJsonFormUtils.getRegistrationForm(form, entityId, currentLocationId);

            return form;
        }

    }

    @Override
    public List<CdpOutletEventClient> processRegistration(String jsonString) {
        return OutletUtil.getOutletEventClient(jsonString);
    }


}
