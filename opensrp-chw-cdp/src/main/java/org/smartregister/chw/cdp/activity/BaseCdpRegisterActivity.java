package org.smartregister.chw.cdp.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.Form;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.AllConstants;
import org.smartregister.Context;
import org.smartregister.cdp.R;
import org.smartregister.chw.cdp.CdpLibrary;
import org.smartregister.chw.cdp.contract.BaseCdpRegisterContract;
import org.smartregister.chw.cdp.fragment.BaseCdpRegisterFragment;
import org.smartregister.chw.cdp.fragment.BaseOrdersRegisterFragment;
import org.smartregister.chw.cdp.interactor.BaseCdpRegisterInteractor;
import org.smartregister.chw.cdp.listener.BaseCdpBottomNavigationListener;
import org.smartregister.chw.cdp.model.BaseCdpRegisterModel;
import org.smartregister.chw.cdp.pojo.RegisterParams;
import org.smartregister.chw.cdp.presenter.BaseCdpRegisterPresenter;
import org.smartregister.chw.cdp.util.CdpJsonFormUtils;
import org.smartregister.chw.cdp.util.CdpUtil;
import org.smartregister.chw.cdp.util.Constants;
import org.smartregister.helper.BottomNavigationHelper;
import org.smartregister.listener.BottomNavigationListener;
import org.smartregister.repository.BaseRepository;
import org.smartregister.util.Utils;
import org.smartregister.view.activity.BaseRegisterActivity;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import androidx.annotation.MenuRes;
import androidx.fragment.app.Fragment;
import timber.log.Timber;

public class BaseCdpRegisterActivity extends BaseRegisterActivity implements BaseCdpRegisterContract.View {

    protected String BASE_ENTITY_ID;
    protected String FAMILY_BASE_ENTITY_ID;
    protected String ACTION;
    protected String FORM_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BASE_ENTITY_ID = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID);
        FAMILY_BASE_ENTITY_ID = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.FAMILY_BASE_ENTITY_ID);
        ACTION = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.ACTION);
        FORM_NAME = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.CDP_FORM_NAME);
        onStartActivityWithAction();
    }

    /**
     * Process a payload when an activity is started with an action
     */
    protected void onStartActivityWithAction() {
        if (FORM_NAME != null && ACTION != null) {
            startFormActivity(FORM_NAME, BASE_ENTITY_ID, null);
        }
    }

    @Override
    public void startRegistration() {
        startFormActivity(FORM_NAME, null, null);
    }

    @Override
    public void startFormActivity(String formName, String entityId, String metaData) {
        try {
            if (mBaseFragment instanceof BaseCdpRegisterFragment) {
                String locationId = Context.getInstance().allSharedPreferences().getPreference(AllConstants.CURRENT_LOCATION_ID);
                presenter().startForm(formName, entityId, metaData, locationId);
            }
        } catch (Exception e) {
            Timber.e(e);
            displayToast(getString(R.string.error_unable_to_start_form));
        }
    }

    @Override
    public void startFormActivity(JSONObject jsonForm) {
        Intent intent = new Intent(this, BaseCdpRegisterActivity.class);
        intent.putExtra(Constants.JSON_FORM_EXTRA.JSON, jsonForm.toString());

        if (getFormConfig() != null) {
            intent.putExtra(JsonFormConstants.JSON_FORM_KEY.FORM, getFormConfig());
        }
        startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }

    @Override
    public void startFormActivity(JSONObject jsonForm, String formName) {
        if (formName.equals(Constants.FORMS.CDP_OUTLET_REGISTRATION)) {
            try {
                String locationId = Context.getInstance().allSharedPreferences().getPreference(AllConstants.CURRENT_LOCATION_ID);
                presenter().startForm(formName, null, null, locationId);
            } catch (Exception e) {
                Timber.e(e);
                displayToast(getString(R.string.error_unable_to_start_form));
            }
        }
    }

    @Override
    public Form getFormConfig() {
        return null;
    }

    @Override
    protected void onActivityResultExtended(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_GET_JSON && resultCode == RESULT_OK) {
            String jsonString = data.getStringExtra(Constants.JSON_FORM_EXTRA.JSON);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                String encounter_type = jsonObject.getString("encounter_type");
                if (encounter_type.equalsIgnoreCase(Constants.EVENT_TYPE.CDP_OUTLET_REGISTRATION)) {
                    RegisterParams registerParam = new RegisterParams();
                    registerParam.setEditMode(false);
                    registerParam.setFormTag(CdpJsonFormUtils.formTag(CdpLibrary.getInstance().context().allSharedPreferences()));
                    showProgressDialog(R.string.saving_dialog_title);
                    presenter().saveForm(jsonString, registerParam);
                } else if (encounter_type.equalsIgnoreCase(Constants.EVENT_TYPE.CDP_CONDOM_ORDER)) {
                    presenter().saveOrderForm(jsonString, encounter_type);
                } else {
                    presenter().saveForm(jsonString);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            finish();
        }
    }

    @Override
    public List<String> getViewIdentifiers() {
        return Arrays.asList(Constants.CONFIGURATION.CDP_CONFIRMATION);
    }

    /**
     * Override this to subscribe to bottom navigation
     */
    @Override
    protected void registerBottomNavigation() {
        bottomNavigationHelper = new BottomNavigationHelper();
        bottomNavigationView = findViewById(org.smartregister.R.id.bottom_navigation);

        if (bottomNavigationView != null) {
            bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
            bottomNavigationView.getMenu().removeItem(org.smartregister.R.id.action_clients);
            bottomNavigationView.getMenu().removeItem(R.id.action_register);
            bottomNavigationView.getMenu().removeItem(org.smartregister.R.id.action_search);
            bottomNavigationView.getMenu().removeItem(org.smartregister.R.id.action_library);
            bottomNavigationView.inflateMenu(getMenuResource());
            bottomNavigationHelper.disableShiftMode(bottomNavigationView);
            BottomNavigationListener familyBottomNavigationListener = new BaseCdpBottomNavigationListener(this);
            bottomNavigationView.setOnNavigationItemSelectedListener(familyBottomNavigationListener);
        }
    }

    @MenuRes
    public int getMenuResource() {
        return R.menu.bottom_nav_condom_programming;
    }

    @Override
    protected void initializePresenter() {
        presenter = new BaseCdpRegisterPresenter(this, new BaseCdpRegisterModel(), new BaseCdpRegisterInteractor());
    }

    @Override
    protected BaseRegisterFragment getRegisterFragment() {
        return new BaseCdpRegisterFragment();
    }

    @Override
    protected Fragment[] getOtherFragments() {
        return new Fragment[]{
                new BaseOrdersRegisterFragment()
        };
    }

    @Override
    public BaseCdpRegisterContract.Presenter presenter() {
        return (BaseCdpRegisterContract.Presenter) presenter;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_GET_JSON && resultCode == RESULT_OK) {
            String jsonString = data.getStringExtra(Constants.JSON_FORM_EXTRA.JSON);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                String encounter_type = jsonObject.getString("encounter_type");
                if (encounter_type.equalsIgnoreCase(Constants.EVENT_TYPE.CDP_OUTLET_REGISTRATION)) {
                    RegisterParams registerParam = new RegisterParams();
                    registerParam.setEditMode(false);
                    registerParam.setFormTag(CdpJsonFormUtils.formTag(CdpLibrary.getInstance().context().allSharedPreferences()));
                    showProgressDialog(R.string.saving_dialog_title);
                    presenter().saveForm(jsonString, registerParam);
                } else if (encounter_type.equalsIgnoreCase(Constants.EVENT_TYPE.CDP_CONDOM_ORDER)) {
                    presenter().saveOrderForm(jsonString, encounter_type);
                } else {
                    presenter().saveForm(jsonString);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            startClientProcessing();
        }
    }

    private void updateFormField(JSONArray formFieldArrays, String formFieldKey, String updateValue) {
        if (updateValue != null) {
            JSONObject formObject = org.smartregister.util.JsonFormUtils.getFieldJSONObject(formFieldArrays, formFieldKey);
            if (formObject != null) {
                try {
                    formObject.remove(org.smartregister.util.JsonFormUtils.VALUE);
                    formObject.put(org.smartregister.util.JsonFormUtils.VALUE, updateValue);
                } catch (JSONException e) {
                    Timber.e(e);
                }
            }
        }
    }

    public void startClientProcessing() {
        try {
            long lastSyncTimeStamp = Utils.getAllSharedPreferences().fetchLastUpdatedAtDate(0);
            Date lastSyncDate = new Date(lastSyncTimeStamp);
            CdpUtil.getClientProcessorForJava().processClient(CdpUtil.getSyncHelper().getEvents(lastSyncDate, BaseRepository.TYPE_Unprocessed));
            Utils.getAllSharedPreferences().saveLastUpdatedAtDate(lastSyncDate.getTime());
        } catch (Exception e) {
            Timber.d(e);
        }

    }

    public void startOutletForm() {
        // Implement
    }

}
