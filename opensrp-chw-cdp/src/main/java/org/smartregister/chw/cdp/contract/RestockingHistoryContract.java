package org.smartregister.chw.cdp.contract;

import android.content.Context;

import org.json.JSONObject;
import org.smartregister.chw.cdp.domain.Visit;

import java.util.List;

public interface RestockingHistoryContract {
    interface View {

        void initializePresenter();

        Presenter getPresenter();

        void onDataReceived(List<Visit> visits);

        Context getViewContext();

        android.view.View renderView(List<Visit> visits);

        void startRestockingForm(String formName) throws Exception;

        void startFormActivity(JSONObject form);

    }

    interface Presenter {

        void initialize();

        View getView();

        void startForm(String formName) throws Exception;

        void saveForm(String jsonString);
    }

    interface Model {
        JSONObject getFormAsJson(String formName) throws Exception;
    }

    interface Interactor {
        void saveRegistration(String jsonString, final InteractorCallBack callBack);

        void getMemberHistory(String memberID, Context context, InteractorCallBack callBack);

    }

    interface InteractorCallBack {

        void onDataFetched(List<Visit> visits);

    }
}
