package org.smartregister.chw.cdp.util;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.cdp.CdpLibrary;
import org.smartregister.chw.cdp.pojo.CdpOrderTaskEvent;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.domain.Task;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.TaskRepository;

import static org.smartregister.chw.cdp.util.CdpJsonFormUtils.processJsonForm;

public class OrdersUtil {

    private static final String PLAN_ID = "5270285b-5a3b-4647-b772-c0b3c52e2b71";
    private static final String CODE = "CondomOrder";

    public static CdpOrderTaskEvent createOrderTaskEvent(AllSharedPreferences allSharedPreferences, String jsonString, String focus) {
        JSONObject formData;
        try {
            formData = new JSONObject(jsonString);
            String encounter_type = formData.optString(Constants.JSON_FORM_EXTRA.ENCOUNTER_TYPE);

            Event event = processJsonForm(allSharedPreferences, jsonString);
            Task task = null;
            if (encounter_type.equalsIgnoreCase(Constants.EVENT_TYPE.CDP_CONDOM_ORDER)) {
                //TODO: read the value from the JSON object for facility ordering
                String groupId = allSharedPreferences.fetchDefaultLocalityId(allSharedPreferences.fetchRegisteredANM());
                assert event != null;
                task = createTask(allSharedPreferences, focus, event, groupId);
            }
            return new CdpOrderTaskEvent(event, task);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Task createTask(AllSharedPreferences allSharedPreferences, String focus, Event event, String groupId) {
        DateTime now = new DateTime();
        Task task = new Task();
        task.setPlanIdentifier(PLAN_ID); //=
        task.setGroupIdentifier(groupId); //= referralTask.groupId
        task.setStatus(Task.TaskStatus.READY); //=
        task.setBusinessStatus(BusinessStatus.ORDERED);
        task.setPriority(3);
        task.setCode(CODE);
        task.setDescription(focus);
        task.setFocus(focus);
        task.setForEntity(event.getBaseEntityId());
        task.setExecutionStartDate(now);
        task.setAuthoredOn(now);
        task.setLastModified(now);
        task.setReasonReference(event.getFormSubmissionId());
        task.setOwner(allSharedPreferences.fetchRegisteredANM());
        task.setSyncStatus(BaseRepository.TYPE_Created);
        task.setRequester(
                allSharedPreferences.getANMPreferredName(allSharedPreferences.fetchRegisteredANM()));
        task.setLocation(
                allSharedPreferences.fetchUserLocalityId(allSharedPreferences.fetchRegisteredANM()));
        return task;
    }

    public static TaskRepository getTaskRepository() {
        return CdpLibrary.getInstance().context().getTaskRepository();
    }

    public static void persistTask(Task task) {
        if (task != null)
            getTaskRepository().addOrUpdate(task);
    }

    public interface BusinessStatus {
        String ORDERED = "ordered";
        String IN_PROGRESS = "In-Progress";
        String COMPLETE = "Complete";
        String EXPIRED = "Expired";
    }
}
