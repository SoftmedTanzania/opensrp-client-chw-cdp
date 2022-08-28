package org.smartregister.chw.cdp.presenter;

import android.content.Context;
import androidx.annotation.Nullable;

import org.smartregister.chw.cdp.contract.BaseCdpProfileContract;
import org.smartregister.chw.cdp.domain.OutletObject;

import java.lang.ref.WeakReference;

import timber.log.Timber;


public class BaseCdpProfilePresenter implements BaseCdpProfileContract.Presenter {
    protected WeakReference<BaseCdpProfileContract.View> view;
    protected BaseCdpProfileContract.Interactor interactor;
    protected Context context;
    protected OutletObject outletObject;

    public BaseCdpProfilePresenter(BaseCdpProfileContract.View view, BaseCdpProfileContract.Interactor interactor, OutletObject outletObject) {
        this.view = new WeakReference<>(view);
        this.interactor = interactor;
        this.outletObject = outletObject;
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
}
