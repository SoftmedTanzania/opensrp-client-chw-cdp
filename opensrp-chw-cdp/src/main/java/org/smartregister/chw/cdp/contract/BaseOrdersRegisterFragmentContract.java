package org.smartregister.chw.cdp.contract;

import org.json.JSONObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.view.contract.BaseRegisterFragmentContract;

public interface BaseOrdersRegisterFragmentContract {
    interface View extends BaseRegisterFragmentContract.View {

        Presenter presenter();

        Model model();

        void initializeAdapter(String tableName);

        void startOrderForm();
    }

    interface Presenter extends BaseRegisterFragmentContract.Presenter {
        void setTasksFocus(String taskFocus);

        String getMainCondition();

        String getDefaultSortQuery();

        String getMainTable();

    }

    interface Interactor {
        void saveRegistration(String jsonString, final InteractorCallBack callBack);
    }

    interface InteractorCallBack {
        void clientDetails(CommonPersonObjectClient client);

        void onRegistrationSaved();
    }

    interface Model {
        String countSelect(String tableName, String mainCondition);

        String mainSelect(String tableName, String mainCondition);

        JSONObject getOrderFormAsJson(String formName) throws Exception;
    }
}
