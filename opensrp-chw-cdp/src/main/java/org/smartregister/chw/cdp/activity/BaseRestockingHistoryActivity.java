package org.smartregister.chw.cdp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import org.smartregister.cdp.R;
import org.smartregister.chw.cdp.domain.OutletObject;
import org.smartregister.chw.cdp.util.Constants;
import org.smartregister.view.activity.SecuredActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

public class BaseRestockingHistoryActivity extends SecuredActivity {

    protected OutletObject outletObject;

    public static void startMe(Activity activity, OutletObject outletObject) {
        Intent intent = new Intent(activity, BaseRestockingHistoryActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.OUTLET_OBJECT, outletObject);
        activity.startActivity(intent);
    }


    protected void setupViews() {
        Toolbar toolbar = findViewById(R.id.collapsing_toolbar);
        TextView tvTitle = findViewById(R.id.tvTitle);

        tvTitle.setText(getString(R.string.back_to_outlet, outletObject.getOutletName()));
        setUpActionBar(toolbar);
    }


    private void setUpActionBar(Toolbar toolbar) {

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

    @Override
    protected void onCreation() {
        setContentView(R.layout.cdp_restocking_visit_history);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            outletObject = (OutletObject) getIntent().getSerializableExtra(Constants.ACTIVITY_PAYLOAD.OUTLET_OBJECT);
        }
        setupViews();
    }

    @Override
    protected void onResumption() {
        //Overridden
    }
}
