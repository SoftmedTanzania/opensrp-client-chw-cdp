package org.smartregister.chw.cdp.presenter;

import android.content.Context;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.smartregister.chw.cdp.contract.BaseOrderDetailsContract;
import org.smartregister.chw.cdp.util.Constants;
import org.smartregister.commonregistry.CommonPersonObjectClient;

import java.lang.ref.WeakReference;

import timber.log.Timber;

public class BaseOrderDetailsPresenter implements BaseOrderDetailsContract.Presenter, BaseOrderDetailsContract.InteractorCallBack {
    protected WeakReference<BaseOrderDetailsContract.View> view;
    protected BaseOrderDetailsContract.Interactor interactor;
    protected BaseOrderDetailsContract.Model model;
    protected Context context;

    public BaseOrderDetailsPresenter(BaseOrderDetailsContract.View view, BaseOrderDetailsContract.Interactor interactor, BaseOrderDetailsContract.Model model, CommonPersonObjectClient pc) {
        this.view = new WeakReference<>(view);
        this.interactor = interactor;
        this.model = model;
        fillViewWithData(pc);
        refreshViewPageBottom(pc);
    }

    @Override
    public void fillViewWithData(CommonPersonObjectClient pc) {
        if (pc != null && getView() != null) {
            getView().setDetailViewWithData(pc);
        }
    }

    @Override
    public void saveForm(String jsonString) {
        try {
            interactor.saveForm(jsonString, this);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    public void startForm(String formName, String entityId) throws Exception {
        JSONObject form = model.getFormAsJson(formName, entityId);
        if (getView() != null)
            getView().startFormActivity(form);
    }

    @Override
    public BaseOrderDetailsContract.@Nullable View getView() {
        if (view != null && view.get() != null)
            return view.get();

        return null;
    }


    @Override
    public void refreshViewPageBottom(CommonPersonObjectClient pc) {
        String status = interactor.getOrderStatus(pc);
        if(!status.equalsIgnoreCase(Constants.OrderStatus.READY) && getView() != null){
            getView().hideButtons();
        }
    }

    @Override
    public void cancelOrderRequest(CommonPersonObjectClient pc) {
        interactor.cancelOrderRequest(pc);
    }
}
