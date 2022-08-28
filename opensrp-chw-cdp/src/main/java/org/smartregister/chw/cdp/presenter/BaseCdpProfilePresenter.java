package org.smartregister.chw.cdp.presenter;

import android.content.Context;
import androidx.annotation.Nullable;

import org.smartregister.chw.cdp.contract.BaseCdpProfileContract;
import org.smartregister.chw.cdp.domain.MemberObject;

import java.lang.ref.WeakReference;

import timber.log.Timber;


public class BaseCdpProfilePresenter implements BaseCdpProfileContract.Presenter {
    protected WeakReference<BaseCdpProfileContract.View> view;
    protected BaseCdpProfileContract.Interactor interactor;
    protected Context context;

    public BaseCdpProfilePresenter(BaseCdpProfileContract.View view, BaseCdpProfileContract.Interactor interactor) {
        this.view = new WeakReference<>(view);
        this.interactor = interactor;
    }

    @Override
    public void fillProfileData(MemberObject memberObject) {
        if (memberObject != null && getView() != null) {
            getView().setProfileViewWithData();
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
