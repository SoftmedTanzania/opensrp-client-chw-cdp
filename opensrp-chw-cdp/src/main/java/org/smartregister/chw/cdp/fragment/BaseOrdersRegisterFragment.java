package org.smartregister.chw.cdp.fragment;

import android.view.View;

import org.smartregister.cdp.R;
import org.smartregister.view.customcontrols.FontVariant;

public class BaseOrdersRegisterFragment extends BaseCdpRegisterFragment {
    @Override
    public void setupViews(View view) {
        super.setupViews(view);
        if (titleView != null) {
            titleView.setVisibility(android.view.View.VISIBLE);
            titleView.setText(getString(R.string.order_receive));
            titleView.setFontVariant(FontVariant.REGULAR);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_cdp_order_receive;
    }
}
