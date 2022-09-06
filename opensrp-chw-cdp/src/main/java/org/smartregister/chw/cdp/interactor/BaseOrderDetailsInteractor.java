package org.smartregister.chw.cdp.interactor;

import org.smartregister.chw.cdp.contract.BaseOrderDetailsContract;
import org.smartregister.chw.cdp.util.AppExecutors;

import androidx.annotation.VisibleForTesting;

public class BaseOrderDetailsInteractor implements BaseOrderDetailsContract.Interactor {
    protected AppExecutors appExecutors;

    @VisibleForTesting
    BaseOrderDetailsInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseOrderDetailsInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void saveForm(String jsonString, BaseOrderDetailsContract.InteractorCallBack callBack) {
        //
    }
}
