package org.smartregister.chw.cdp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;
import org.smartregister.cdp.R;
import org.smartregister.chw.cdp.contract.BaseCdpProfileContract;
import org.smartregister.chw.cdp.interactor.BaseCdpProfileInteractor;
import org.smartregister.chw.cdp.presenter.BaseCdpProfilePresenter;
import org.smartregister.chw.cdp.util.Constants;
import org.smartregister.helper.ImageRenderHelper;
import org.smartregister.view.activity.BaseProfileActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;


public class BaseCdpProfileActivity extends BaseProfileActivity implements BaseCdpProfileContract.View, BaseCdpProfileContract.InteractorCallBack {
    protected BaseCdpProfileContract.Presenter profilePresenter;
    protected CircleImageView imageView;
    protected TextView textViewName;
    protected TextView textViewUniqueID;
    protected TextView textViewLocation;
    protected TextView textViewOutletType;
    protected TextView tvLastRecordedStock;
    protected RelativeLayout rlLastRecordedStock;
    protected TextView tvLastVisitSub;
    protected RelativeLayout rlVisitHistory;
    protected Button btnRecordFollowup;


    public static void startProfileActivity(Activity activity, String baseEntityId) {
        Intent intent = new Intent(activity, BaseCdpProfileActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, baseEntityId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreation() {
        setContentView(R.layout.activity_cdp_profile);
        Toolbar toolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
//        String baseEntityId = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.text_blue), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
        }

        toolbar.setNavigationOnClickListener(v -> BaseCdpProfileActivity.this.finish());
        appBarLayout = this.findViewById(R.id.collapsing_toolbar_appbarlayout);
        if (Build.VERSION.SDK_INT >= 21) {
            appBarLayout.setOutlineProvider(null);
        }

        textViewName = findViewById(R.id.textview_name);
        textViewUniqueID = findViewById(R.id.textview_id);
        imageView = findViewById(R.id.imageview_profile);
        textViewLocation = findViewById(R.id.textview_location);
        textViewOutletType = findViewById(R.id.outlet_type);
        tvLastRecordedStock = findViewById(R.id.tv_lastRecordedStock);
        rlLastRecordedStock = findViewById(R.id.rlLastRecordedStock);
        tvLastVisitSub = findViewById(R.id.tv_visitHistory_sub);
        rlVisitHistory = findViewById(R.id.rlVisitHistory);
        btnRecordFollowup = findViewById(R.id.btn_record_visit);


        btnRecordFollowup.setOnClickListener(this);

        imageRenderHelper = new ImageRenderHelper(this);
        initializePresenter();
        setupViews();
    }

    @Override
    protected void setupViews() {
        //Implement
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.title_layout) {
            onBackPressed();
        }
    }

    @Override
    protected void initializePresenter() {
        showProgressBar(true);
        profilePresenter = new BaseCdpProfilePresenter(this, new BaseCdpProfileInteractor());
        fetchProfileData();
        profilePresenter.refreshProfileBottom();
    }


    @Override
    public void hideView() {
        btnRecordFollowup.setVisibility(View.GONE);
    }

    @Override
    public void startFormActivity(JSONObject formJson) {
        // Implement
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void setProfileViewWithData() {
        //Implement
    }


    @Override
    protected ViewPager setupViewPager(ViewPager viewPager) {
        return null;
    }

    @Override
    protected void fetchProfileData() {
        //fetch profile data
    }

    @Override
    public void showProgressBar(boolean status) {
        //Implement
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_GET_JSON && resultCode == RESULT_OK) {
            profilePresenter.saveForm(data.getStringExtra(Constants.JSON_FORM_EXTRA.JSON));
            finish();
        }
    }
}
