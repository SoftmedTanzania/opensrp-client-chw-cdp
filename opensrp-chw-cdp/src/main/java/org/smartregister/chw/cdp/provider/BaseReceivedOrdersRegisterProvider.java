package org.smartregister.chw.cdp.provider;

import android.content.Context;
import android.view.View;

import org.smartregister.cdp.R;
import org.smartregister.chw.cdp.CdpLibrary;
import org.smartregister.chw.cdp.fragment.BaseCdpRegisterFragment;
import org.smartregister.chw.cdp.holders.OrdersViewHolder;
import org.smartregister.chw.cdp.util.Constants;
import org.smartregister.chw.cdp.util.DBConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Location;
import org.smartregister.repository.LocationRepository;
import org.smartregister.util.Utils;

import timber.log.Timber;

public class BaseReceivedOrdersRegisterProvider extends BaseOrdersRegisterProvider {
    private Context context;
    private View.OnClickListener onClickListener;
    private LocationRepository locationRepository;

    public BaseReceivedOrdersRegisterProvider(Context context, View.OnClickListener onClickListener, View.OnClickListener paginationClickListener) {
        super(context, onClickListener, paginationClickListener);
        this.context = context;
        this.onClickListener = onClickListener;
        this.locationRepository = CdpLibrary.getInstance().context().getLocationRepository();
    }

    @Override
    protected void populateOrderDetailColumn(CommonPersonObjectClient pc, OrdersViewHolder viewHolder) {
        try {

            String healthFacilityId = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.LOCATION_ID, true);
            String condomType = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.CONDOM_TYPE, true);
            String condomBrand = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.CONDOM_BRAND, true);
            String condomQuantity = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.QUANTITY_REQ, false);
            String orderStatus = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.STATUS, false);

            if (healthFacilityId != null) {
                Location location = locationRepository.getLocationById(healthFacilityId);
                String healthFacilityName = location.getProperties().getName();
                viewHolder.health_facility.setText(healthFacilityName);
            }
            viewHolder.condom_type.setText(condomType);
            viewHolder.condom_brand.setText(condomBrand);
            viewHolder.quantity.setText(condomQuantity);
            viewHolder.status.setText(getStatusString(context, orderStatus));
            if (orderStatus.equals(Constants.OrderStatus.FAILED)) {
                viewHolder.status.setTextColor(context.getResources().getColor(R.color.error_color));
            }
            if (orderStatus.equals(Constants.OrderStatus.COMPLETE)) {
                viewHolder.status.setTextColor(context.getResources().getColor(R.color.alert_complete_green));
            }
            viewHolder.registerColumns.setOnClickListener(onClickListener);
            viewHolder.registerColumns.setTag(pc);
            viewHolder.registerColumns.setTag(R.id.VIEW_ID, BaseCdpRegisterFragment.CLICK_VIEW_NORMAL);

        } catch (Exception e) {
            Timber.e(e);
        }
    }
}

