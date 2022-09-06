package org.smartregister.chw.cdp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import org.smartregister.cdp.R;
import org.smartregister.chw.cdp.util.Constants;
import org.smartregister.chw.cdp.util.DBConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.util.Utils;
import org.smartregister.view.activity.SecuredActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

public class BaseOrderDetailsActivity extends SecuredActivity {

    protected Toolbar toolbar;
    protected TextView tvTitle;
    protected CommonPersonObjectClient client;
    protected TextView condomType;
    protected TextView condomBrand;
    protected TextView quantity;

    public static void startMe(Activity activity, CommonPersonObjectClient pc) {
        Intent intent = new Intent(activity, BaseOrderDetailsActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.CLIENT, pc);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreation() {
        setContentView(R.layout.activity_order_request_details);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            client = (CommonPersonObjectClient) getIntent().getSerializableExtra(Constants.ACTIVITY_PAYLOAD.CLIENT);
        }
        setupViews();
    }

    @Override
    protected void onResumption() {
        //override
    }

    protected void setupViews() {
        toolbar = findViewById(R.id.collapsing_toolbar);
        tvTitle = findViewById(R.id.tvTitle);
        condomBrand = findViewById(R.id.condom_brand);
        condomType = findViewById(R.id.condom_type);
        quantity = findViewById(R.id.condom_quantity);

        condomType.setText(Utils.getValue(client, DBConstants.KEY.CONDOM_TYPE, true));
        condomBrand.setText(Utils.getValue(client, DBConstants.KEY.CONDOM_BRAND, true));
        quantity.setText(Utils.getValue(client, DBConstants.KEY.QUANTITY_REQ, true));

        setUpActionBar();
    }

    private void setUpActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.text_blue), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

}
