package org.smartregister.chw.cdp.contract;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.smartregister.chw.cdp.domain.OutletObject;

public interface BaseCdpProfileContract {
    interface View extends InteractorCallBack {

        void setProfileViewWithData(OutletObject outletObject);


        void showProgressBar(boolean status);


        void hideView();

        void startFormActivity(JSONObject formJson);

        void updateLastRecordedStock();
    }

    interface Presenter {

        void fillProfileData(@Nullable OutletObject outletObject);

        void saveForm(String jsonString);

        void startForm(String formName, String entityId, String metadata, String currentLocationId) throws Exception;

        @Nullable
        View getView();

        void refreshProfileBottom();

        void recordCDPButton(String visitState);

        void refreshLastVisitData(OutletObject outletObject);
    }

    interface Interactor {

        void refreshProfileInfo();

        void saveRegistration(String jsonString, final BaseCdpProfileContract.InteractorCallBack callBack);
    }

    interface Model {

        JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception;

    }


    interface InteractorCallBack {


    }
}