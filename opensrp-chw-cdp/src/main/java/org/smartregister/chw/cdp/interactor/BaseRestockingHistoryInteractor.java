package org.smartregister.chw.cdp.interactor;

import android.content.Context;

import org.smartregister.chw.cdp.contract.RestockingHistoryContract;
import org.smartregister.chw.cdp.util.AppExecutors;
import org.smartregister.chw.cdp.util.CdpUtil;

import androidx.annotation.VisibleForTesting;

public class BaseRestockingHistoryInteractor implements RestockingHistoryContract.Interactor {

    protected AppExecutors appExecutors;

    @VisibleForTesting
    BaseRestockingHistoryInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseRestockingHistoryInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void saveRegistration(String jsonString, RestockingHistoryContract.InteractorCallBack callBack) {
        Runnable runnable = () -> {
            try {
                CdpUtil.saveFormEvent(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getMemberHistory(String memberID, Context context, RestockingHistoryContract.InteractorCallBack callBack) {
        //do nothing for now
    }
}
