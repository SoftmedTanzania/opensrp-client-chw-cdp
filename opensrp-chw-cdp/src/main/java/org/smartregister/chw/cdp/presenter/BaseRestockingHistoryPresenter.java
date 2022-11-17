package org.smartregister.chw.cdp.presenter;

import static org.smartregister.chw.cdp.util.CdpJsonFormUtils.cdpFormFields;
import static org.smartregister.chw.cdp.util.Constants.FORMS.CDP_RECEIVE_CONDOM_FROM_ORGANIZATIONS;
import static org.smartregister.chw.cdp.util.Constants.JSON_FORM_KEY.ISSUING_ORGANIZATION;
import static org.smartregister.util.Utils.getAllSharedPreferences;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONObject;
import org.smartregister.chw.cdp.contract.RestockingHistoryContract;
import org.smartregister.chw.cdp.domain.Visit;
import org.smartregister.util.JsonFormUtils;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;

public class BaseRestockingHistoryPresenter implements RestockingHistoryContract.Presenter, RestockingHistoryContract.InteractorCallBack {

    private final RestockingHistoryContract.Interactor interactor;
    private final RestockingHistoryContract.Model model;
    private final WeakReference<RestockingHistoryContract.View> view;
    private final String outletID;

    public BaseRestockingHistoryPresenter(RestockingHistoryContract.View view, RestockingHistoryContract.Interactor interactor, RestockingHistoryContract.Model model, String outletId) {
        this.interactor = interactor;
        this.view = new WeakReference<>(view);
        this.outletID = outletId;
        this.model = model;

        initialize();
    }

    @Override
    public void initialize() {
        if (getView() == null)
            return;

        interactor.getMemberHistory(outletID, getView().getViewContext(), this);
    }

    @Override
    public RestockingHistoryContract.View getView() {
        if (view.get() != null) {
            return view.get();
        } else {
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void startForm(String formName, String outletID) throws Exception {
        JSONObject form = model.getFormAsJson(formName, outletID);

        String userLocationTag = getAllSharedPreferences().fetchUserLocationTag();
        if (formName.equalsIgnoreCase(CDP_RECEIVE_CONDOM_FROM_ORGANIZATIONS) && (userLocationTag == null || !userLocationTag.contains("msd_code"))) {
            try {
                JSONArray fields = cdpFormFields(form);
                Objects.requireNonNull(JsonFormUtils.getFieldJSONObject(fields, ISSUING_ORGANIZATION)).getJSONArray("options").remove(0);
            } catch (Exception e) {
                Timber.e(e);
            }
        }
        getView().startFormActivity(form);
    }

    @Override
    public void saveForm(String jsonString) {
        interactor.saveRegistration(jsonString, this);
    }

    @Override
    public void onDataFetched(List<Visit> visits) {
        if (getView() != null) {
            getView().onDataReceived(visits);
        }
    }

    @Override
    public void onRegistrationSaved() {
        if (getView() != null && getView().getMainLayout() != null) {
            if (getView().getMainLayout().getChildCount() > 1) {
                getView().getMainLayout().removeViews(0, getView().getMainLayout().getChildCount());
            }
            getView().getPresenter().initialize();
        }
    }
}
