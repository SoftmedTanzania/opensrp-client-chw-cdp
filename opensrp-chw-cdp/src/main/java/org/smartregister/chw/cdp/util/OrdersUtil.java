package org.smartregister.chw.cdp.util;

import org.joda.time.DateTime;
import org.json.JSONArray;
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

import java.util.ArrayList;
import java.util.Date;

import timber.log.Timber;

import static com.vijay.jsonwizard.utils.FormUtils.getFieldJSONObject;
import static org.smartregister.chw.cdp.util.CdpJsonFormUtils.fields;
import static org.smartregister.chw.cdp.util.CdpJsonFormUtils.processJsonForm;
import static org.smartregister.chw.cdp.util.Constants.STEP_ONE;
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
        if (focus.equalsIgnoreCase(Constants.EVENT_TYPE.CDP_ORDER_FROM_FACILITY)) {
            String groupId = getFacilityId(jsonString);
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

    public static void persistEvent(Event baseEvent) {
        JSONObject eventJson = null;
        try {
            eventJson = new JSONObject(CdpJsonFormUtils.gson.toJson(baseEvent));
            CdpUtil.getSyncHelper().addEvent(baseEvent.getBaseEntityId(), eventJson, BaseRepository.TYPE_Unprocessed);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void orderResponseOutOfStock(Task currentTask, String teamId) throws Exception {
        AllSharedPreferences allSharedPreferences = CdpLibrary.getInstance().context().allSharedPreferences();
        String baseEntityId = generateRandomUUIDString();
        String currentTeamId = allSharedPreferences.fetchDefaultTeamId(allSharedPreferences.fetchRegisteredANM());
        String currentLocationId = allSharedPreferences.fetchUserLocalityId(allSharedPreferences.fetchRegisteredANM());

        Task canceledTask = getCancelledTask(currentTask);
        Event orderFeedBackEvent = getFeedbackEventOutOfStock(baseEntityId, currentTask, allSharedPreferences, Constants.EVENT_TYPE.CDP_ORDER_FEEDBACK, currentTask.getLocation(), teamId);
        Event orderFeedBackEventFacilityCopy = getFeedbackEventOutOfStock(baseEntityId, currentTask, allSharedPreferences, Constants.EVENT_TYPE.CDP_ORDER_FEEDBACK_OWN_COPY, currentLocationId, currentTeamId);
        persistTask(canceledTask);
        persistEvent(orderFeedBackEvent);
        persistEvent(orderFeedBackEventFacilityCopy);
        CdpUtil.startClientProcessing();
    }

    public static void orderResponseRestocking(Task currentTask, AllSharedPreferences allSharedPreferences, String jsonString, String teamId) {
        Event baseEvent = processJsonForm(allSharedPreferences, jsonString);

        if (baseEvent != null && currentTask != null) {
            baseEvent.setBaseEntityId(generateRandomUUIDString());
            processFormForStockChanges(baseEvent, allSharedPreferences);

            Event feedbackEvent = getFeedBackEvent(baseEvent, currentTask, Constants.EVENT_TYPE.CDP_ORDER_FEEDBACK, currentTask.getLocation(), teamId);
            persistEvent(feedbackEvent);


            String currentTeamId = allSharedPreferences.fetchDefaultTeamId(allSharedPreferences.fetchRegisteredANM());
            String currentLocationId = allSharedPreferences.fetchUserLocalityId(allSharedPreferences.fetchRegisteredANM());
            Event feedbackEventCopy = getFeedBackEvent(baseEvent, currentTask, Constants.EVENT_TYPE.CDP_ORDER_FEEDBACK_OWN_COPY, currentLocationId, currentTeamId);
            persistEvent(feedbackEventCopy);


            Task inTransitTask = getInTransitTask(currentTask);
            persistTask(inTransitTask);

            CdpUtil.startClientProcessing();
        }

    }

    private static void processFormForStockChanges(Event baseEvent, AllSharedPreferences allSharedPreferences) {
        baseEvent.setFormSubmissionId(generateRandomUUIDString());
        try {
            CdpUtil.processEvent(allSharedPreferences, baseEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Event getFeedBackEvent(Event baseEvent, Task currentTask, String eventType, String locationId, String teamId) {
        DateTime now = new DateTime();
        baseEvent.setEventType(eventType);
        baseEvent.addObs(new Obs().withFormSubmissionField(Constants.JSON_FORM_KEY.RESPONSE_STATUS).withValue(Constants.ResponseStatus.RESTOCKED)
                .withFieldCode(Constants.JSON_FORM_KEY.RESPONSE_STATUS).withFieldType("formsubmissionField").withFieldDataType("text").withParentCode("").withHumanReadableValues(new ArrayList<>()));
        baseEvent.addObs(new Obs().withFormSubmissionField(Constants.JSON_FORM_KEY.RESPONSE_DATE).withValue(String.valueOf(now.getMillis()))
                .withFieldCode(Constants.JSON_FORM_KEY.RESPONSE_DATE).withFieldType("formsubmissionField").withFieldDataType("text").withParentCode("").withHumanReadableValues(new ArrayList<>()));
        baseEvent.addObs(new Obs().withFormSubmissionField(Constants.JSON_FORM_KEY.REQUEST_REFERENCE).withValue(currentTask.getReasonReference())
                .withFieldCode(Constants.JSON_FORM_KEY.REQUEST_REFERENCE).withFieldType("formsubmissionField").withFieldDataType("text").withParentCode("").withHumanReadableValues(new ArrayList<>()));
        baseEvent.setLocationId(locationId);
        baseEvent.setTeamId(teamId);
        baseEvent.setFormSubmissionId(generateRandomUUIDString());
        return baseEvent;
    }

    private static boolean shouldCreateReceiveFromFacilityEvent(String jsonString) {
        try {
            JSONObject form = new JSONObject(jsonString);
            String encounter_type = form.optString(Constants.JSON_FORM_EXTRA.ENCOUNTER_TYPE);
            if (encounter_type.equals(Constants.EVENT_TYPE.CDP_CONDOM_DISTRIBUTION_OUTSIDE)) {
                return true;
            }
        } catch (Exception e) {
            Timber.e(e);
        }

        return false;
    }

    /**
     * Feedback event for Out of stock
     *
     * @param currentTask          current task
     * @param allSharedPreferences allSharedPreferences
     * @param locationId           sync_location_id
     * @param teamId               sync_team_id
     * @param eventType            eventTypeName
     * @return Event Feedback Event
     * The event:
     * - should update the response_status with out_of_stock
     * - should update the response date
     * - should set request reference
     */
    private static Event getFeedbackEventOutOfStock(String baseEntityId, Task currentTask, AllSharedPreferences allSharedPreferences, String eventType, String locationId, String teamId) {
        DateTime now = new DateTime();
        Event baseEvent = (Event) new Event()
                .withBaseEntityId(baseEntityId)
                .withEventDate(new Date())
                .withFormSubmissionId(generateRandomUUIDString())
                .withEventType(eventType)
                .withEntityType(Constants.TABLES.CDP_ORDER_FEEDBACK)
                .withProviderId(allSharedPreferences.fetchRegisteredANM())
                .withLocationId(locationId)
                .withTeamId(teamId)
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

    public static Task getCancelledTask(Task currentTask) {
        DateTime now = new DateTime();
        currentTask.setStatus(Task.TaskStatus.CANCELLED);
        currentTask.setBusinessStatus(BusinessStatus.CANCELLED);
        currentTask.setLastModified(now);
        currentTask.setSyncStatus(BaseRepository.TYPE_Unsynced);
        return currentTask;
    }

    public static Task getInTransitTask(Task currentTask) {
        DateTime now = new DateTime();
        currentTask.setStatus(Task.TaskStatus.IN_PROGRESS);
        currentTask.setBusinessStatus(BusinessStatus.IN_PROGRESS);
        currentTask.setLastModified(now);
        currentTask.setSyncStatus(BaseRepository.TYPE_Unsynced);
        return currentTask;
    }

    public static Task getCompletedTask(Task currentTask) {
        DateTime now = new DateTime();
        currentTask.setStatus(Task.TaskStatus.COMPLETED);
        currentTask.setBusinessStatus(BusinessStatus.COMPLETE);
        currentTask.setLastModified(now);
        currentTask.setSyncStatus(BaseRepository.TYPE_Unsynced);
        return currentTask;
    }

    public static void processMarkAsReceived(AllSharedPreferences allSharedPreferences, String jsonString, Task task) {
        Event baseEvent = processJsonForm(allSharedPreferences, jsonString);
        if (baseEvent != null && task != null) {
            try {
                baseEvent.setBaseEntityId(generateRandomUUIDString());
                CdpUtil.processEvent(allSharedPreferences, baseEvent);
                persistTask(getCompletedTask(task));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private static String getFacilityId(String jsonString) {
        try {
            JSONObject form = new JSONObject(jsonString);
            JSONObject facilityObj = getFieldJSONObject(fields(form, STEP_ONE), "receiving_order_facility");
            if (facilityObj != null) {
                String val = facilityObj.getString("value");
                JSONArray arr = new JSONArray(val);
                return arr.getJSONObject(0).getJSONObject("property").getString("confirmed-id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public interface BusinessStatus {
        String ORDERED = "ordered";
        String IN_PROGRESS = "In-Progress";
        String COMPLETE = "Complete";
        String CANCELLED = "Cancelled";
    }
}
