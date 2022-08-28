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
    }

    interface Presenter {

        void fillProfileData(@Nullable OutletObject outletObject);

        void saveForm(String jsonString);

        @Nullable
        View getView();

        void refreshProfileBottom();

        void recordCDPButton(String visitState);
    }

    interface Interactor {

        void refreshProfileInfo();

        void saveRegistration(String jsonString, final BaseCdpProfileContract.InteractorCallBack callBack);
    }


    interface InteractorCallBack {


    }
}