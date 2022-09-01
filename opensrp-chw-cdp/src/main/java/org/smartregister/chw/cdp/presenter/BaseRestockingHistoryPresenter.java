package org.smartregister.chw.cdp.presenter;

import org.json.JSONObject;
import org.smartregister.chw.cdp.contract.RestockingHistoryContract;
import org.smartregister.chw.cdp.domain.Visit;

import java.lang.ref.WeakReference;
import java.util.List;

public class BaseRestockingHistoryPresenter implements RestockingHistoryContract.Presenter, RestockingHistoryContract.InteractorCallBack {

    private final RestockingHistoryContract.Interactor interactor;
    private final RestockingHistoryContract.Model model;
    private final WeakReference<RestockingHistoryContract.View> view;
    private final String outletID;

    public BaseRestockingHistoryPresenter(RestockingHistoryContract.View view, RestockingHistoryContract.Interactor interactor, RestockingHistoryContract.Model model, String outletId) {
        this.interactor = interactor;
        this.view = new WeakReference<>(view);
        this.outletID = outletId;
        this.model = model;

        initialize();
    }

    @Override
    public void initialize() {
        if (getView() == null)
            return;

        interactor.getMemberHistory(outletID, getView().getViewContext(), this);
    }

    @Override
    public RestockingHistoryContract.View getView() {
        if (view.get() != null) {
            return view.get();
        } else {
            return null;
        }
    }

    @Override
    public void startForm(String formName, String outletID) throws Exception {
        JSONObject form = model.getFormAsJson(formName, outletID);
        getView().startFormActivity(form);
    }

    @Override
    public void saveForm(String jsonString) {
        interactor.saveRegistration(jsonString, this);
    }

    @Override
    public void onDataFetched(List<Visit> visits) {
        getView().onDataReceived(visits);
    }
}
