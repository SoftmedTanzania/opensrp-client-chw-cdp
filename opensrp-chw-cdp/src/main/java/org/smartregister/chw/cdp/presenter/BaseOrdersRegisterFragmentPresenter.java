package org.smartregister.chw.cdp.presenter;

import org.smartregister.chw.cdp.contract.BaseCdpRegisterFragmentContract;
import org.smartregister.chw.cdp.contract.BaseOrdersRegisterFragmentContract;
import org.smartregister.chw.cdp.fragment.BaseOrdersRegisterFragment;
import org.smartregister.chw.cdp.util.Constants;

import java.lang.ref.WeakReference;

import static org.apache.commons.lang3.StringUtils.trim;

public class BaseOrdersRegisterFragmentPresenter implements BaseOrdersRegisterFragmentContract.Presenter {
    protected WeakReference<BaseOrdersRegisterFragmentContract.View> viewReference;
    protected BaseOrdersRegisterFragmentContract.Interactor interactor;
    protected BaseOrdersRegisterFragmentContract.Model model;
    private String taskFocus;

    public BaseOrdersRegisterFragmentPresenter(BaseOrdersRegisterFragmentContract.View view, BaseOrdersRegisterFragmentContract.Interactor interactor, BaseOrdersRegisterFragmentContract.Model model) {
        this.viewReference = new WeakReference<>(view);
        this.model = model;
        this.interactor = interactor;
    }

    @Override
    public void setTasksFocus(String taskFocus) {
        this.taskFocus = taskFocus;
    }

    @Override
    public String getMainCondition() {
        return "";
    }

    @Override
    public String getDefaultSortQuery() {
        return "";
    }

    @Override
    public String getMainTable() {
        return Constants.TABLES.CDP_ORDERS;
    }

    @Override
    public void processViewConfigurations() {
        //
    }


    @Override
    public void initializeQueries(String mainCondition) {
        String tableName = getMainTable();
        mainCondition = trim(getMainCondition()).equals("") ? mainCondition : getMainCondition();
        String countSelect = model.countSelect(tableName, mainCondition);
        String mainSelect = model.mainSelect(tableName, mainCondition);

        if (getView() != null) {

            getView().initializeQueryParams(tableName, countSelect, mainSelect);
            getView().initializeAdapter(tableName);

            getView().countExecute();
            getView().filterandSortInInitializeQueries();
        }
    }

    protected BaseOrdersRegisterFragmentContract.View getView() {
        if (viewReference != null)
            return viewReference.get();
        else
            return null;
    }

    @Override
    public void startSync() {
        //
    }

    @Override
    public void searchGlobally(String uniqueId) {
        //
    }
}
