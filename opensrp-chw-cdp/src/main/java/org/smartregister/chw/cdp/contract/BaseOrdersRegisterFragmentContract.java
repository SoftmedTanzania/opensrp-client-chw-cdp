package org.smartregister.chw.cdp.contract;

import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.view.contract.BaseRegisterFragmentContract;

import java.util.Set;

public interface BaseOrdersRegisterFragmentContract {
    interface View extends BaseRegisterFragmentContract.View {

        Presenter presenter();

        void initializeAdapter(String tableName);
    }

    interface Presenter extends BaseRegisterFragmentContract.Presenter {
        void setTasksFocus(String taskFocus);

        String getMainCondition();

        String getDefaultSortQuery();

        String getMainTable();
    }

    interface Interactor {

    }

    interface InteractorCallBack {
        void clientDetails(CommonPersonObjectClient client);
    }

    interface Model {
        String countSelect(String tableName, String mainCondition);

        String mainSelect(String tableName, String mainCondition);
    }
}
