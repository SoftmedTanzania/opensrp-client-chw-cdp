package org.smartregister.chw.cdp.util;

import static org.smartregister.util.Utils.getAllSharedPreferences;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONObject;
import org.opensrp.api.constants.Gender;
import org.smartregister.cdp.R;
import org.smartregister.chw.cdp.CdpLibrary;
import org.smartregister.chw.cdp.contract.BaseCdpCallDialogContract;
import org.smartregister.chw.cdp.pojo.CdpOrderTaskEvent;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.domain.Location;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.LocationRepository;
import org.smartregister.sync.ClientProcessorForJava;
import org.smartregister.sync.helper.ECSyncHelper;
import org.smartregister.util.PermissionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class CdpUtil {

    public static void processEvent(AllSharedPreferences allSharedPreferences, Event baseEvent) throws Exception {
        if (baseEvent != null) {
            CdpJsonFormUtils.tagEvent(allSharedPreferences, baseEvent);
            JSONObject eventJson = new JSONObject(CdpJsonFormUtils.gson.toJson(baseEvent));

            getSyncHelper().addEvent(baseEvent.getBaseEntityId(), eventJson, BaseRepository.TYPE_Unprocessed);
            startClientProcessing();
        }
    }

    public static void startClientProcessing() {
        try {
            long lastSyncTimeStamp = getAllSharedPreferences().fetchLastUpdatedAtDate(0);
            Date lastSyncDate = new Date(lastSyncTimeStamp);
            getClientProcessorForJava().processClient(getSyncHelper().getEvents(lastSyncDate, BaseRepository.TYPE_Unprocessed));
            getAllSharedPreferences().saveLastUpdatedAtDate(lastSyncDate.getTime());
        } catch (Exception e) {
            Timber.d(e);
        }
    }

    public static ECSyncHelper getSyncHelper() {
        return CdpLibrary.getInstance().getEcSyncHelper();
    }

    public static ClientProcessorForJava getClientProcessorForJava() {
        return CdpLibrary.getInstance().getClientProcessorForJava();
    }

    public static Spanned fromHtml(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(text);
        }
    }

    @SuppressLint("HardwareIds")
    public static boolean launchDialer(final Activity activity, final BaseCdpCallDialogContract.View callView, final String phoneNumber) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            // set a pending call execution request
            if (callView != null) {
                callView.setPendingCallRequest(() -> CdpUtil.launchDialer(activity, callView, phoneNumber));
            }

            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, PermissionUtils.PHONE_STATE_PERMISSION_REQUEST_CODE);

            return false;
        } else {

            if (((TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number()
                    == null) {

                Timber.i("No dial application so we launch copy to clipboard...");

                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(activity.getText(R.string.copied_phone_number), phoneNumber);
                clipboard.setPrimaryClip(clip);

                CopyToClipboardDialog copyToClipboardDialog = new CopyToClipboardDialog(activity, R.style.copy_clipboard_dialog);
                copyToClipboardDialog.setContent(phoneNumber);
                copyToClipboardDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                copyToClipboardDialog.show();
                // no phone
                Toast.makeText(activity, activity.getText(R.string.copied_phone_number), Toast.LENGTH_SHORT).show();

            } else {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                activity.startActivity(intent);
            }
            return true;
        }
    }

    public static void saveFormEvent(final String jsonString) throws Exception {
        AllSharedPreferences allSharedPreferences = CdpLibrary.getInstance().context().allSharedPreferences();
        Event baseEvent = CdpJsonFormUtils.processJsonForm(allSharedPreferences, jsonString);
        CdpUtil.processEvent(allSharedPreferences, baseEvent);
    }

    public static void saveTaskEvent(final String jsonString, String encounterType) throws Exception {
        AllSharedPreferences allSharedPreferences = CdpLibrary.getInstance().context().allSharedPreferences();

        CdpOrderTaskEvent cdpOrderTaskEvent = OrdersUtil.createOrderTaskEvent(allSharedPreferences, jsonString, encounterType);
        CdpUtil.processEvent(allSharedPreferences, cdpOrderTaskEvent.getEvent());
        OrdersUtil.persistTask(cdpOrderTaskEvent.getTask());

    }

    public static int getMemberProfileImageResourceIdentifier(String entityType) {
        return R.mipmap.ic_member;
    }

    /**
     * @param timestamp timestamp
     * @return 'dd-mm-yyyy'
     */
    public static String formatTimeStamp(Long timestamp) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ROOT);
        return df.format(timestamp);
    }

    /**
     * Get the location name from Id
     *
     * @param locationId Location uuid in string
     * @return location name
     */
    public static String getRequesterNameFromId(String locationId) {
        LocationRepository locationRepository = CdpLibrary.getInstance().context().getLocationRepository();
        Location location = locationRepository.getLocationById(locationId);
        if (location != null && location.getProperties() != null) {
            return location.getProperties().getName();
        }
        return "";
    }


    public static String getGenderTranslated(Context context, String gender) {
        if (gender.equalsIgnoreCase(Gender.MALE.toString())) {
            return context.getResources().getString(R.string.male);
        } else if (gender.equalsIgnoreCase(Gender.FEMALE.toString())) {
            return context.getResources().getString(R.string.female);
        }
        return "";
    }

    public static int getStringFromResources(String identifier, Activity activity) {
        return activity.getResources().getIdentifier(identifier, "string", activity.getPackageName());
    }
}
