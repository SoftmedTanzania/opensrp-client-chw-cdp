package org.smartregister.chw.cdp.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.smartregister.cdp.R;
import org.smartregister.chw.cdp.activity.BaseCdpProfileActivity;
import org.smartregister.chw.cdp.contract.BaseCdpRegisterFragmentContract;
import org.smartregister.chw.cdp.model.BaseCdpRegisterFragmentModel;
import org.smartregister.chw.cdp.presenter.BaseCdpRegisterFragmentPresenter;
import org.smartregister.chw.cdp.provider.BaseCdpRegisterProvider;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;
import org.smartregister.view.customcontrols.CustomFontTextView;
import org.smartregister.view.customcontrols.FontVariant;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.HashMap;
import java.util.Set;

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
}
