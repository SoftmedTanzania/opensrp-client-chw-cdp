package org.smartregister.chw.cdp.contract;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;

public interface BaseOrderDetailsContract {

    interface View {
        void setDetailViewWithData(CommonPersonObjectClient pc);

        void startFormActivity(JSONObject formJson);

        int getMainContentView();

        void initializePresenter();

        void recordOutOfStockResponse();

    }

    interface Presenter {
        void fillViewWithData(CommonPersonObjectClient pc);

        void saveForm(String jsonString);

        void startForm(String formName, String entityId) throws Exception;

        @Nullable
        View getView();

        void refreshViewPageBottom();
    }

    interface Interactor {

        void saveForm(String jsonString, final InteractorCallBack callBack);
    }

    interface Model {

        JSONObject getFormAsJson(String formName, String entityId) throws Exception;

    }
    interface  InteractorCallBack {

    }
}
