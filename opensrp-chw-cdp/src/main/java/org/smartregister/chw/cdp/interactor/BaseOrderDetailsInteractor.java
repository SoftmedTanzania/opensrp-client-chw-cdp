package org.smartregister.chw.cdp.interactor;

import org.smartregister.chw.cdp.contract.BaseOrderDetailsContract;
import org.smartregister.chw.cdp.util.AppExecutors;
import org.smartregister.chw.cdp.util.DBConstants;
import org.smartregister.chw.cdp.util.OrdersUtil;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Task;
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
    public void saveForm(String jsonString, BaseOrderDetailsContract.InteractorCallBack callBack) {
        //
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
}
