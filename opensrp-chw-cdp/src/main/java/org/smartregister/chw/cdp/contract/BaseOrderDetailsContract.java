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

        void showOutOfStockDialog();

        void hideButtons();

    }

    interface Presenter {
        void fillViewWithData(CommonPersonObjectClient pc);

        void saveForm(String jsonString);

        void startForm(String formName, String entityId) throws Exception;

        @Nullable
        View getView();

        void refreshViewPageBottom(CommonPersonObjectClient pc);

        void cancelOrderRequest(CommonPersonObjectClient pc);
    }

    interface Interactor {

        void saveForm(CommonPersonObjectClient pc, String jsonString, final InteractorCallBack callBack) throws Exception;

        void cancelOrderRequest(CommonPersonObjectClient pc) throws Exception;

        String getOrderStatus(CommonPersonObjectClient pc);
    }

    interface Model {

        JSONObject getFormAsJson(String formName, String entityId) throws Exception;

    }
    interface  InteractorCallBack {

    }
}
