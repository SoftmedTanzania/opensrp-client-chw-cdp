package org.smartregister.chw.cdp.presenter;

import android.content.Context;
import androidx.annotation.Nullable;

import org.json.JSONObject;
import org.smartregister.chw.cdp.contract.BaseCdpProfileContract;
import org.smartregister.chw.cdp.domain.OutletObject;

import java.lang.ref.WeakReference;

import timber.log.Timber;


public class BaseCdpProfilePresenter implements BaseCdpProfileContract.Presenter {
    protected WeakReference<BaseCdpProfileContract.View> view;
    protected BaseCdpProfileContract.Interactor interactor;
    protected BaseCdpProfileContract.Model model;
    protected Context context;
    protected OutletObject outletObject;

    public BaseCdpProfilePresenter(BaseCdpProfileContract.View view, BaseCdpProfileContract.Interactor interactor, BaseCdpProfileContract.Model model, OutletObject outletObject) {
        this.view = new WeakReference<>(view);
        this.interactor = interactor;
        this.outletObject = outletObject;
        this.model = model;
        fillProfileData(outletObject);
    }

    @Override
    public void fillProfileData(OutletObject outletObject) {
        if (outletObject != null && getView() != null) {
            getView().setProfileViewWithData(outletObject);
        }
    }

    @Override
    public void recordCDPButton(@Nullable String visitState) {
       //Implement
    }

    @Override
    @Nullable
    public BaseCdpProfileContract.View getView() {
        if (view != null && view.get() != null)
            return view.get();

        return null;
    }

    @Override
    public void refreshProfileBottom() {
        interactor.refreshProfileInfo();
    }

    @Override
    public void saveForm(String jsonString) {
        try {
            interactor.saveRegistration(jsonString, getView());
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    public void startForm(String formName, String entityId, String metadata, String currentLocationId) throws Exception {
        JSONObject form = model.getFormAsJson(formName, entityId, currentLocationId);
        getView().startFormActivity(form);
    }
}
