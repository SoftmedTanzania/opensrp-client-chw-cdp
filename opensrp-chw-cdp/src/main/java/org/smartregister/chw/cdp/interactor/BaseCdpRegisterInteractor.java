package org.smartregister.chw.cdp.interactor;

import android.support.annotation.VisibleForTesting;

import org.smartregister.chw.cdp.contract.BaseCdpRegisterContract;
import org.smartregister.chw.cdp.util.AppExecutors;
import org.smartregister.chw.cdp.util.TestUtil;

public class BaseCdpRegisterInteractor implements BaseCdpRegisterContract.Interactor {

    private AppExecutors appExecutors;

    @VisibleForTesting
    BaseCdpRegisterInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseCdpRegisterInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void saveRegistration(final String jsonString, final BaseCdpRegisterContract.InteractorCallBack callBack) {

        Runnable runnable = () -> {
            try {
                TestUtil.saveFormEvent(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }

            appExecutors.mainThread().execute(() -> callBack.onRegistrationSaved());
        };
        appExecutors.diskIO().execute(runnable);
    }
}
