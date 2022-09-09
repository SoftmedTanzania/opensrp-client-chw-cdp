package org.smartregister.chw.cdp.util;

import org.apache.commons.lang3.tuple.Triple;
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

    public static void orderResponseOutOfStock(Task currentTask) throws Exception {
        AllSharedPreferences allSharedPreferences = CdpLibrary.getInstance().context().allSharedPreferences();
        Task canceledTask = getCancelledTask(currentTask);
        Event orderFeedBackEvent = getFeedbackEventOutOfStock(currentTask, allSharedPreferences);
        persistTask(canceledTask);
        persistEvent(orderFeedBackEvent);
        CdpUtil.startClientProcessing();
    }

    public static void orderResponseRestocking(Task currentTask, AllSharedPreferences allSharedPreferences, String jsonString) {
        Event baseEvent = processJsonForm(allSharedPreferences, jsonString);
        DateTime now = new DateTime();
        if (baseEvent != null && currentTask != null) {
            baseEvent.setEventType(Constants.EVENT_TYPE.CDP_ORDER_FEEDBACK);
            baseEvent.addObs(new Obs().withFormSubmissionField(Constants.JSON_FORM_KEY.RESPONSE_STATUS).withValue(Constants.ResponseStatus.RESTOCKED)
                    .withFieldCode(Constants.JSON_FORM_KEY.RESPONSE_STATUS).withFieldType("formsubmissionField").withFieldDataType("text").withParentCode("").withHumanReadableValues(new ArrayList<>()));
            baseEvent.addObs(new Obs().withFormSubmissionField(Constants.JSON_FORM_KEY.RESPONSE_DATE).withValue(String.valueOf(now.getMillis()))
                    .withFieldCode(Constants.JSON_FORM_KEY.RESPONSE_DATE).withFieldType("formsubmissionField").withFieldDataType("text").withParentCode("").withHumanReadableValues(new ArrayList<>()));
            baseEvent.addObs(new Obs().withFormSubmissionField(Constants.JSON_FORM_KEY.REQUEST_REFERENCE).withValue(currentTask.getReasonReference())
                    .withFieldCode(Constants.JSON_FORM_KEY.REQUEST_REFERENCE).withFieldType("formsubmissionField").withFieldDataType("text").withParentCode("").withHumanReadableValues(new ArrayList<>()));
            baseEvent.setLocationId(currentTask.getLocation());
            baseEvent.setBaseEntityId(generateRandomUUIDString());

            Task inTransitTask = getInTransitTask(currentTask);
            persistTask(inTransitTask);
            persistEvent(baseEvent);
            CdpUtil.startClientProcessing();
        }

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
     * @param currentTask current task
     * @return Event Feedback Event
     * The event:
     * - should update the response_status with out_of_stock
     * - should update the response date
     * - should set request reference
     */
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

    public static Task getCancelledTask(Task currentTask) {
        DateTime now = new DateTime();
        currentTask.setStatus(Task.TaskStatus.CANCELLED);
        currentTask.setBusinessStatus(BusinessStatus.CANCELLED);
        currentTask.setLastModified(now);
        currentTask.setSyncStatus(BaseRepository.TYPE_Unsynced);
        return currentTask;
    }

    public static Task getCompletedTask(Task currentTask) {
        DateTime now = new DateTime();
        currentTask.setStatus(Task.TaskStatus.IN_PROGRESS);
        currentTask.setBusinessStatus(BusinessStatus.IN_PROGRESS);
        currentTask.setLastModified(now);
        currentTask.setSyncStatus(BaseRepository.TYPE_Unsynced);
        return currentTask;
    }

    public static Task getInTransitTask(Task currentTask) {
        DateTime now = new DateTime();
        currentTask.setStatus(Task.TaskStatus.COMPLETED);
        currentTask.setBusinessStatus(BusinessStatus.COMPLETE);
        currentTask.setLastModified(now);
        currentTask.setSyncStatus(BaseRepository.TYPE_Unsynced);
        return currentTask;
    }

    private static void createReceiveFromFacilityEvent(AllSharedPreferences allSharedPreferences, Task task, String jsonString) {
        String condomType = getCondomTypeAndOffset(jsonString).getLeft();
        String offset = getCondomTypeAndOffset(jsonString).getRight();
        Event baseEvent = (Event) new Event()
                .withBaseEntityId(generateRandomUUIDString())
                .withEventDate(new Date())
                .withFormSubmissionId(generateRandomUUIDString())
                .withEventType(Constants.EVENT_TYPE.CDP_RECEIVE_FROM_FACILITY)
                .withEntityType(Constants.TABLES.CDP_STOCK_COUNT)
                .withProviderId(allSharedPreferences.fetchRegisteredANM())
                .withLocationId(task.getLocation())
                .withTeamId(allSharedPreferences.fetchDefaultTeamId(allSharedPreferences.fetchRegisteredANM()))
                .withTeam(allSharedPreferences.fetchDefaultTeam(allSharedPreferences.fetchRegisteredANM()))
                .withClientDatabaseVersion(CdpLibrary.getInstance().getDatabaseVersion())
                .withClientApplicationVersion(CdpLibrary.getInstance().getApplicationVersion())
                .withDateCreated(new Date());

        if (condomType.equals("male_condom")) {
            baseEvent.addObs(new Obs().withFormSubmissionField(Constants.JSON_FORM_KEY.MALE_CONDOMS_OFFSET).withValue(offset)
                    .withFieldCode(Constants.JSON_FORM_KEY.MALE_CONDOMS_OFFSET).withFieldType("formsubmissionField").withFieldDataType("text").withParentCode("").withHumanReadableValues(new ArrayList<>()));
        } else if (condomType.equals("female_condom")) {
            baseEvent.addObs(new Obs().withFormSubmissionField(Constants.JSON_FORM_KEY.FEMALE_CONDOMS_OFFSET).withValue(offset)
                    .withFieldCode(Constants.JSON_FORM_KEY.FEMALE_CONDOMS_OFFSET).withFieldType("formsubmissionField").withFieldDataType("text").withParentCode("").withHumanReadableValues(new ArrayList<>()));
        }
        persistEvent(baseEvent);
        persistTask(getCompletedTask(task));
    }

    private static Triple<String, String, String> getCondomTypeAndOffset(String jsonString) {
        Triple<String, String, String> vals = null;
        try {
            JSONObject form = new JSONObject(jsonString);
            JSONObject condomTypeObj = getFieldJSONObject(fields(form, STEP_ONE), "condom_type");
            String condomType = condomTypeObj.getString("value");

            JSONObject quantityResponseObj = getFieldJSONObject(fields(form, STEP_ONE), "quantity_response");
            String offset = quantityResponseObj.getString("value");

            vals = Triple.of(condomType, null, offset);

        } catch (Exception e) {
            Timber.e(e);
        }

        return vals;
    }

    private static String getFacilityId(String jsonString) {
        try {
            JSONObject form = new JSONObject(jsonString);
            JSONObject facilityObj = getFieldJSONObject(fields(form, STEP_ONE), "receiving_order_facility");
            if (facilityObj != null) {
                return facilityObj.getString("value");
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
