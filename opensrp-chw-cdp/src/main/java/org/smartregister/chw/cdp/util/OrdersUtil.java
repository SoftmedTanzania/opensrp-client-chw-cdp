package org.smartregister.chw.cdp.util;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.cdp.CdpLibrary;
import org.smartregister.chw.cdp.pojo.CdpOrderTaskEvent;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.Obs;
import org.smartregister.domain.Task;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.TaskRepository;
import org.smartregister.util.JsonFormUtils;

import java.util.ArrayList;
import java.util.Date;

import static org.smartregister.chw.cdp.util.CdpJsonFormUtils.processJsonForm;
import static org.smartregister.util.JsonFormUtils.generateRandomUUIDString;

public class OrdersUtil {

    private static final String PLAN_ID = "5270285b-5a3b-4647-b772-c0b3c52e2b71";
    private static final String CODE = "CondomOrder";

    public static CdpOrderTaskEvent createOrderTaskEvent(AllSharedPreferences allSharedPreferences, String jsonString, String focus) {

        Event event = processJsonForm(allSharedPreferences, jsonString);
        event.setBaseEntityId(generateRandomUUIDString());
        Task task = null;
        if (focus.equalsIgnoreCase(Constants.EVENT_TYPE.CDP_CONDOM_ORDER)) {
            //TODO: read the value from the JSON object for facility ordering
            String groupId = allSharedPreferences.fetchDefaultLocalityId(allSharedPreferences.fetchRegisteredANM());
            task = createTask(allSharedPreferences, focus, event, groupId);
        }
        return new CdpOrderTaskEvent(event, task);

    }

    public static Task createTask(AllSharedPreferences allSharedPreferences, String focus, Event event, String groupId) {
        DateTime now = new DateTime();
        Task task = new Task();
        task.setIdentifier(generateRandomUUIDString());
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

    public static void persistEvent(Event baseEvent){
        JSONObject eventJson = null;
        try {
            eventJson = new JSONObject(CdpJsonFormUtils.gson.toJson(baseEvent));
            CdpUtil.getSyncHelper().addEvent(baseEvent.getBaseEntityId(), eventJson, BaseRepository.TYPE_Unprocessed);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface BusinessStatus {
        String ORDERED = "ordered";
        String IN_PROGRESS = "In-Progress";
        String COMPLETE = "Complete";
        String CANCELLED = "Cancelled";
    }

    public static void orderResponseOutOfStock(Task currentTask) throws Exception {
        AllSharedPreferences allSharedPreferences = CdpLibrary.getInstance().context().allSharedPreferences();
        Task canceledTask = getCancelledTask(currentTask);
        Event orderFeedBackEvent = getFeedbackEventOutOfStock(currentTask, allSharedPreferences);
        persistTask(canceledTask);
        persistEvent(orderFeedBackEvent);
        CdpUtil.startClientProcessing();
    }


    /**
     * Feedback event for Out of stock
     * @param currentTask current task
     * @return Event Feedback Event
     * The event:
     *      - should update the response_status with out_of_stock
     *      - should update the response date
     *      - should set request reference
     * */
    private static Event getFeedbackEventOutOfStock(Task currentTask, AllSharedPreferences allSharedPreferences) {
        DateTime now = new DateTime();
        Event baseEvent = (Event) new Event()
                .withBaseEntityId(generateRandomUUIDString())
                .withEventDate(new Date())
                .withFormSubmissionId(JsonFormUtils.generateRandomUUIDString())
                .withEventType(Constants.EVENT_TYPE.CDP_ORDER_FEEDBACK)
                .withEntityType(Constants.TABLES.CDP_ORDER_FEEDBACK)
                .withProviderId(allSharedPreferences.fetchRegisteredANM())
                .withLocationId(currentTask.getLocation()) // <== use task location_id as location for syncing
                .withTeamId(allSharedPreferences.fetchDefaultTeamId(allSharedPreferences.fetchRegisteredANM()))
                .withTeam(allSharedPreferences.fetchDefaultTeam(allSharedPreferences.fetchRegisteredANM()))
                .withClientDatabaseVersion(CdpLibrary.getInstance().getDatabaseVersion())
                .withClientApplicationVersion(CdpLibrary.getInstance().getApplicationVersion())
                .withDateCreated(new Date());
        baseEvent.addObs(new Obs().withFormSubmissionField(Constants.JSON_FORM_KEY.RESPONSE_STATUS).withValue(Constants.ResponseStatus.OUT_OF_STOCK)
                .withFieldCode(Constants.JSON_FORM_KEY.RESPONSE_STATUS).withFieldType("formsubmissionField").withFieldDataType("text").withParentCode("").withHumanReadableValues(new ArrayList<>()));
        baseEvent.addObs(new Obs().withFormSubmissionField(Constants.JSON_FORM_KEY.RESPONSE_DATE).withValue(String.valueOf(now.getMillis()))
                .withFieldCode(Constants.JSON_FORM_KEY.RESPONSE_DATE).withFieldType("formsubmissionField").withFieldDataType("text").withParentCode("").withHumanReadableValues(new ArrayList<>()));
        baseEvent.addObs(new Obs().withFormSubmissionField(Constants.JSON_FORM_KEY.REQUEST_REFERENCE).withValue(currentTask.getReasonReference())
                .withFieldCode(Constants.JSON_FORM_KEY.REQUEST_REFERENCE).withFieldType("formsubmissionField").withFieldDataType("text").withParentCode("").withHumanReadableValues(new ArrayList<>()));
        return baseEvent;
    }

    public static Task getCancelledTask(Task currentTask){
        DateTime now = new DateTime();
        currentTask.setStatus(Task.TaskStatus.CANCELLED);
        currentTask.setBusinessStatus(BusinessStatus.CANCELLED);
        currentTask.setLastModified(now);
        currentTask.setSyncStatus(BaseRepository.TYPE_Unsynced);
        return currentTask;
    }
}
