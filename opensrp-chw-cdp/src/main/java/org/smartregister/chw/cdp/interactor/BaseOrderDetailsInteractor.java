package org.smartregister.chw.cdp.interactor;

import org.smartregister.chw.cdp.CdpLibrary;
import org.smartregister.chw.cdp.contract.BaseOrderDetailsContract;
import org.smartregister.chw.cdp.util.AppExecutors;
import org.smartregister.chw.cdp.util.DBConstants;
import org.smartregister.chw.cdp.util.OrdersUtil;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Task;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.util.Utils;

import androidx.annotation.VisibleForTesting;

public class BaseOrderDetailsInteractor implements BaseOrderDetailsContract.Interactor {
    protected AppExecutors appExecutors;

    @VisibleForTesting
    BaseOrderDetailsInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseOrderDetailsInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void saveForm(CommonPersonObjectClient pc, String jsonString, BaseOrderDetailsContract.InteractorCallBack callBack) throws Exception {
        AllSharedPreferences allSharedPreferences = CdpLibrary.getInstance().context().allSharedPreferences();
        String taskId = Utils.getValue(pc, DBConstants.KEY.TASK_ID, false);
        Task task = OrdersUtil.getTaskRepository().getTaskByIdentifier(taskId);
        OrdersUtil.orderResponseRestocking(task, allSharedPreferences, jsonString);
    }

    @Override
    public void saveReceivedStockForm(CommonPersonObjectClient pc, String jsonString, BaseOrderDetailsContract.InteractorCallBack callBack) throws Exception {
        AllSharedPreferences allSharedPreferences = CdpLibrary.getInstance().context().allSharedPreferences();
        String taskId = Utils.getValue(pc, DBConstants.KEY.TASK_ID, false);
        Task task = OrdersUtil.getTaskRepository().getTaskByIdentifier(taskId);
        OrdersUtil.processMarkAsReceived(allSharedPreferences, jsonString, task);
    }

    @Override
    public void cancelOrderRequest(CommonPersonObjectClient pc) throws Exception {
        String taskId = Utils.getValue(pc, DBConstants.KEY.TASK_ID, false);
        Task task = OrdersUtil.getTaskRepository().getTaskByIdentifier(taskId);
        OrdersUtil.orderResponseOutOfStock(task);
    }

    @Override
    public String getOrderStatus(CommonPersonObjectClient pc) {
        return Utils.getValue(pc, DBConstants.KEY.STATUS, false);
    }

    @Override
    public boolean isRespondingFacility(CommonPersonObjectClient pc) {
        String requestLocation = Utils.getValue(pc, DBConstants.KEY.LOCATION_ID, false);
        String facilityLocation = Utils.getAllSharedPreferences().fetchDefaultLocalityId(Utils.getAllSharedPreferences().fetchRegisteredANM());
        return !requestLocation.equals(facilityLocation);
    }

}
