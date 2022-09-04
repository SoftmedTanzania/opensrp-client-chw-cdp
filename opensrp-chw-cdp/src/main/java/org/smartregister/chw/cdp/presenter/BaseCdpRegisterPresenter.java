package org.smartregister.chw.cdp.presenter;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.json.JSONObject;
import org.smartregister.chw.cdp.contract.BaseCdpRegisterContract;
import org.smartregister.cdp.R;
import org.smartregister.chw.cdp.pojo.CdpOutletEventClient;
import org.smartregister.chw.cdp.pojo.RegisterParams;
import org.smartregister.chw.cdp.util.Constants;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;

public class BaseCdpRegisterPresenter implements BaseCdpRegisterContract.Presenter, BaseCdpRegisterContract.InteractorCallBack {

    public static final String TAG = BaseCdpRegisterPresenter.class.getName();

    protected WeakReference<BaseCdpRegisterContract.View> viewReference;
    private BaseCdpRegisterContract.Interactor interactor;
    protected BaseCdpRegisterContract.Model model;

    public BaseCdpRegisterPresenter(BaseCdpRegisterContract.View view, BaseCdpRegisterContract.Model model, BaseCdpRegisterContract.Interactor interactor) {
        viewReference = new WeakReference<>(view);
        this.interactor = interactor;
        this.model = model;
    }

    @Override
    public void startForm(String formName, String entityId, String metadata, String currentLocationId) throws Exception {
        if (StringUtils.isBlank(entityId)) {
            if(Objects.equals(formName, Constants.FORMS.CDP_OUTLET_REGISTRATION)){
                Triple<String, String, String> triple = Triple.of(formName, metadata, currentLocationId);
                interactor.getNextUniqueId(triple, this);
            }
            return;
        }

        JSONObject form = model.getFormAsJson(formName, entityId, currentLocationId);
        getView().startFormActivity(form);
    }

    @Override
    public void saveForm(String jsonString) {
        try {
            getView().showProgressDialog(R.string.saving_dialog_title);
            interactor.saveRegistration(jsonString, this);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    @Override
    public void saveOrderForm(String jsonString, String encounterType) {
        try {
            getView().showProgressDialog(R.string.saving_dialog_title);
            interactor.processSaveOrderForm(jsonString, this, encounterType);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    @Override
    public void saveForm(String jsonString, RegisterParams registerParams) {
        //Use this for registration form
        try {
            List<CdpOutletEventClient> cdpOutletEventClientList = model.processRegistration(jsonString);
            if(cdpOutletEventClientList == null || cdpOutletEventClientList.isEmpty()){
                return;
            }
            interactor.saveRegistration(cdpOutletEventClientList, jsonString, registerParams, this);
        } catch (Exception e){
            Timber.e(e);
        }
    }


    @Override
    public void onRegistrationSaved() {
        getView().hideProgressDialog();

    }

    @Override
    public void onRegistrationSaved(boolean editMode) {
        getView().hideProgressDialog();
    }

    @Override
    public void onNoUniqueId() {
        if(getView() != null)
            getView().displayShortToast(R.string.no_unique_id);
    }

    @Override
    public void onUniqueIdFetched(Triple<String, String, String> triple, String entityId) {
        if(getView() != null){
            try {
                startForm(triple.getLeft(), entityId, triple.getMiddle(), triple.getRight());
            } catch (Exception e){
                Timber.e(e);
                getView().displayToast(R.string.error_unable_to_start_form);
            }
        }
    }

    @Override
    public void registerViewConfigurations(List<String> list) {
//        implement
    }

    @Override
    public void unregisterViewConfiguration(List<String> list) {
//        implement
    }

    @Override
    public void onDestroy(boolean b) {
//        implement
    }

    @Override
    public void updateInitials() {
//        implement
    }

    private BaseCdpRegisterContract.View getView() {
        if (viewReference != null)
            return viewReference.get();
        else
            return null;
    }
}
