package org.smartregister.chw.cdp.contract;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.smartregister.chw.cdp.domain.MemberObject;
import org.smartregister.domain.AlertStatus;

import java.util.Date;

public interface BaseCdpProfileContract {
    interface View extends InteractorCallBack {

        void setProfileViewWithData();


        void showProgressBar(boolean status);


        void hideView();

        void startFormActivity(JSONObject formJson);
    }

    interface Presenter {

        void fillProfileData(@Nullable MemberObject memberObject);

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