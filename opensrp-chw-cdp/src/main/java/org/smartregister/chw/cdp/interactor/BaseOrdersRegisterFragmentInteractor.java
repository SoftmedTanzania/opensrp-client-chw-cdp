package org.smartregister.chw.cdp.interactor;

import org.smartregister.chw.cdp.contract.BaseOrdersRegisterFragmentContract;
import org.smartregister.chw.cdp.util.AppExecutors;

import androidx.annotation.VisibleForTesting;

public class BaseOrdersRegisterFragmentInteractor implements BaseOrdersRegisterFragmentContract.Interactor {

    private AppExecutors appExecutors;

    @VisibleForTesting
    BaseOrdersRegisterFragmentInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseOrdersRegisterFragmentInteractor() {
        this(new AppExecutors());
    }
}
