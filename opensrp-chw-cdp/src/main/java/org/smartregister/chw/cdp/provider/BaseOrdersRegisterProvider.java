package org.smartregister.chw.cdp.provider;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.smartregister.cdp.R;
import org.smartregister.chw.cdp.fragment.BaseCdpRegisterFragment;
import org.smartregister.chw.cdp.holders.FooterViewHolder;
import org.smartregister.chw.cdp.holders.OrdersViewHolder;
import org.smartregister.chw.cdp.util.Constants;
import org.smartregister.chw.cdp.util.DBConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.cursoradapter.RecyclerViewProvider;
import org.smartregister.util.Utils;
import org.smartregister.view.contract.SmartRegisterClient;
import org.smartregister.view.contract.SmartRegisterClients;
import org.smartregister.view.dialog.FilterOption;
import org.smartregister.view.dialog.ServiceModeOption;
import org.smartregister.view.dialog.SortOption;
import org.smartregister.view.viewholder.OnClickFormLauncher;

import java.text.MessageFormat;
import java.util.Locale;

import timber.log.Timber;

public class BaseOrdersRegisterProvider implements RecyclerViewProvider<OrdersViewHolder> {
    private final LayoutInflater inflater;
    private Context context;
    private View.OnClickListener onClickListener;
    private View.OnClickListener paginationClickListener;

    public BaseOrdersRegisterProvider(Context context, View.OnClickListener onClickListener, View.OnClickListener paginationClickListener) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.onClickListener = onClickListener;
        this.paginationClickListener = paginationClickListener;
    }

    @Override
    public void getView(Cursor cursor, SmartRegisterClient client, OrdersViewHolder viewHolder) {
        CommonPersonObjectClient pc = (CommonPersonObjectClient) client;
        populateOrderDetailColumn(pc, viewHolder);
    }

    private void populateOrderDetailColumn(CommonPersonObjectClient pc, OrdersViewHolder viewHolder) {
        try {

            String condomType = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.CONDOM_TYPE, true);
            String condomBrand = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.CONDOM_BRAND, true);
            String condomQuantity = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.QUANTITY_REQ, false);
            String orderStatus = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.STATUS, false);

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

    private String getStatusString(Context context, String st) {
        switch (st.toUpperCase(Locale.ROOT)) {
            case Constants.OrderStatus.FAILED:
                return context.getString(R.string.order_status_failed);
            case Constants.OrderStatus.COMPLETE:
                return context.getString(R.string.order_status_complete);
            default:
                return context.getString(R.string.order_status_pending);
        }
    }

    @Override
    public void getFooterView(RecyclerView.ViewHolder viewHolder, int currentPageCount, int totalPageCount, boolean hasNext, boolean hasPrevious) {
        FooterViewHolder footerViewHolder = (FooterViewHolder) viewHolder;
        footerViewHolder.pageInfoView.setText(MessageFormat.format(context.getString(org.smartregister.R.string.str_page_info), currentPageCount, totalPageCount));

        footerViewHolder.nextPageView.setVisibility(hasNext ? View.VISIBLE : View.INVISIBLE);
        footerViewHolder.previousPageView.setVisibility(hasPrevious ? View.VISIBLE : View.INVISIBLE);

        footerViewHolder.nextPageView.setOnClickListener(paginationClickListener);
        footerViewHolder.previousPageView.setOnClickListener(paginationClickListener);
    }

    @Override
    public SmartRegisterClients updateClients(FilterOption villageFilter, ServiceModeOption serviceModeOption, FilterOption searchFilter, SortOption sortOption) {
        return null;
    }

    @Override
    public void onServiceModeSelected(ServiceModeOption serviceModeOption) {
        //implement
    }

    @Override
    public OnClickFormLauncher newFormLauncher(String formName, String entityId, String metaData) {
        return null;
    }

    @Override
    public LayoutInflater inflater() {
        return inflater;
    }

    @Override
    public OrdersViewHolder createViewHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.orders_list_row, parent, false);
        return new OrdersViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder createFooterHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.smart_register_pagination, parent, false);
        return new FooterViewHolder(view);
    }

    @Override
    public boolean isFooterViewHolder(RecyclerView.ViewHolder viewHolder) {
        return viewHolder instanceof FooterViewHolder;
    }
}

