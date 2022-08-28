package org.smartregister.chw.cdp.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.cdp.CdpLibrary;
import org.smartregister.chw.cdp.pojo.CdpOutletEventClient;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.FormEntityConstants;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.sync.helper.ECSyncHelper;
import org.smartregister.util.JsonFormUtils;

import java.util.Calendar;
import java.util.Date;

import timber.log.Timber;

import static org.smartregister.chw.cdp.util.CdpJsonFormUtils.formTag;
import static org.smartregister.util.JsonFormUtils.generateRandomUUIDString;
import static org.smartregister.util.JsonFormUtils.getFieldValue;
import static org.smartregister.util.JsonFormUtils.getJSONObject;

public class OutletJsonFormUtil {

    public static CdpOutletEventClient processOutletRegistrationForm(AllSharedPreferences allSharedPreferences, String jsonString) {
        try {
            Triple<Boolean, JSONObject, JSONArray> registrationFormParams = CdpJsonFormUtils.validateParameters(jsonString);
            if (!(Boolean) registrationFormParams.getLeft()) {
                return null;
            } else {
                JSONObject jsonForm = (JSONObject) registrationFormParams.getMiddle();
                JSONArray fields = (JSONArray) registrationFormParams.getRight();
                String entityId = JsonFormUtils.getString(jsonForm, "entity_id");
                if (StringUtils.isBlank(entityId)) {
                    entityId = generateRandomUUIDString();
                }
                lastInteractedWith(fields);
                Client baseClient = JsonFormUtils.createBaseClient(fields, formTag(allSharedPreferences), entityId);
                baseClient.setBirthdate(new Date());
                baseClient.setClientType("Outlet");
                Event baseEvent = JsonFormUtils.createEvent(fields, getJSONObject(jsonForm, "metadata"), formTag(allSharedPreferences), entityId, Constants.EVENT_TYPE.CDP_OUTLET_REGISTRATION, Constants.TABLES.CDP_OUTLET);
                tagSyncMetadata(allSharedPreferences, baseEvent);
                return new CdpOutletEventClient(baseClient, baseEvent);
            }
        } catch (Exception e) {
            Timber.e(e);
            return null;
        }
    }

    protected static void lastInteractedWith(JSONArray fields) {
        try {
            JSONObject lastInteractedWith = new JSONObject();
            lastInteractedWith.put("key", "last_interacted_with");
            lastInteractedWith.put("value", Calendar.getInstance().getTimeInMillis());
            fields.put(lastInteractedWith);
        } catch (JSONException var2) {
            Timber.e(var2);
        }

    }

    protected static Event tagSyncMetadata(AllSharedPreferences allSharedPreferences, Event event) {
        String providerId = allSharedPreferences.fetchRegisteredANM();
        event.setProviderId(providerId);
        event.setLocationId(locationId(allSharedPreferences));
        event.setChildLocationId(allSharedPreferences.fetchCurrentLocality());
        event.setTeam(allSharedPreferences.fetchDefaultTeam(providerId));
        event.setTeamId(allSharedPreferences.fetchDefaultTeamId(providerId));
        event.setClientDatabaseVersion(CdpLibrary.getInstance().getDatabaseVersion());
        event.setClientApplicationVersion(CdpLibrary.getInstance().getApplicationVersion());
        return event;
    }

    protected static String locationId(AllSharedPreferences allSharedPreferences) {
        String providerId = allSharedPreferences.fetchRegisteredANM();
        String userLocationId = allSharedPreferences.fetchUserLocalityId(providerId);
        if (StringUtils.isBlank(userLocationId)) {
            userLocationId = allSharedPreferences.fetchDefaultLocalityId(providerId);
        }
        return userLocationId;
    }

    public static void mergeAndSaveClient(ECSyncHelper ecUpdater, Client baseClient) throws Exception {
        JSONObject updatedClientJson = new JSONObject(org.smartregister.util.JsonFormUtils.gson.toJson(baseClient));
        JSONObject originalClientJsonObject = ecUpdater.getClient(baseClient.getBaseEntityId());
        JSONObject mergedJson = org.smartregister.util.JsonFormUtils.merge(originalClientJsonObject, updatedClientJson);
        JSONObject relationships = mergedJson.optJSONObject("relationships");
        if ((relationships == null || relationships.length() == 0) && originalClientJsonObject != null) {
            mergedJson.put("relationships", originalClientJsonObject.optJSONObject("relationships"));
        }

        ecUpdater.addClient(baseClient.getBaseEntityId(), mergedJson);
    }


}
