package org.smartregister.chw.cdp.interactor;

import org.smartregister.chw.cdp.contract.BaseCdpProfileContract;
import org.smartregister.chw.cdp.util.AppExecutors;
import org.smartregister.chw.cdp.util.CdpUtil;

import androidx.annotation.VisibleForTesting;

public class BaseCdpProfileInteractor implements BaseCdpProfileContract.Interactor {
    protected AppExecutors appExecutors;

    @VisibleForTesting
    BaseCdpProfileInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseCdpProfileInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void refreshProfileInfo() {
        //Implement
    }

    @Override
    public void saveRegistration(final String jsonString, final BaseCdpProfileContract.InteractorCallBack callback) {

        Runnable runnable = () -> {
            try {
                CdpUtil.saveFormEvent(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
        appExecutors.diskIO().execute(runnable);
    }
}
