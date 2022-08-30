package org.smartregister.chw.cdp.fragment;

import android.view.View;
import android.widget.Toast;

import org.smartregister.cdp.R;
import org.smartregister.chw.cdp.CdpLibrary;
import org.smartregister.chw.cdp.util.CdpUtil;
import org.smartregister.chw.cdp.util.Constants;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.Obs;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.view.customcontrols.FontVariant;

import java.util.ArrayList;
import java.util.Date;

import timber.log.Timber;

import static org.smartregister.util.JsonFormUtils.generateRandomUUIDString;

public class BaseOrdersRegisterFragment extends BaseCdpRegisterFragment {
    @Override
    public void setupViews(View view) {
        super.setupViews(view);
        if (titleView != null) {
            titleView.setVisibility(View.VISIBLE);
            titleView.setText(getString(R.string.order_receive));
            titleView.setFontVariant(FontVariant.REGULAR);
        }
        getSearchView().setVisibility(View.GONE);
        android.view.View searchBarLayout = view.findViewById(org.smartregister.R.id.search_bar_layout);
        searchBarLayout.setVisibility(View.GONE);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_cdp_order_receive;
    }

    @Override
    protected void refreshSyncProgressSpinner() {
        if (syncProgressBar != null) {
            syncProgressBar.setVisibility(android.view.View.GONE);
        }
        if (syncButton != null) {
            syncButton.setVisibility(android.view.View.VISIBLE);
            syncButton.setPadding(0, 0, 10, 0);
            syncButton.setImageDrawable(context().getDrawable(R.drawable.ic_add_white_24));
            syncButton.setOnClickListener(view -> {
                loadTestStockData();
            });
        }
    }

    protected void loadTestStockData() {
        //Do Something
        AllSharedPreferences allSharedPreferences = CdpLibrary.getInstance().context().allSharedPreferences();

        Toast.makeText(getContext(), "Loading data", Toast.LENGTH_SHORT).show();
        Event baseEvent = (Event) new Event()
                .withBaseEntityId(generateRandomUUIDString())
                .withEventDate(new Date())
                .withFormSubmissionId(generateRandomUUIDString())
                .withEventType(Constants.EVENT_TYPE.CDP_RECEIVE_FROM_FACILITY)
                .withEntityType(Constants.TABLES.CDP_STOCK_COUNT)
                .withProviderId(allSharedPreferences.fetchRegisteredANM())
                .withLocationId(allSharedPreferences.fetchDefaultLocalityId(allSharedPreferences.fetchRegisteredANM()))
                .withTeamId(allSharedPreferences.fetchDefaultTeamId(allSharedPreferences.fetchRegisteredANM()))
                .withTeam(allSharedPreferences.fetchDefaultTeam(allSharedPreferences.fetchRegisteredANM()))
                .withClientDatabaseVersion(CdpLibrary.getInstance().getDatabaseVersion())
                .withClientApplicationVersion(CdpLibrary.getInstance().getApplicationVersion())
                .withDateCreated(new Date());

        baseEvent.addObs(new Obs().withFormSubmissionField(Constants.JSON_FORM_KEY.FEMALE_CONDOMS_OFFSET).withValue("10")
                .withFieldCode(Constants.JSON_FORM_KEY.FEMALE_CONDOMS_OFFSET).withFieldType("formsubmissionField").withFieldDataType("text").withParentCode("").withHumanReadableValues(new ArrayList<>()));
        baseEvent.addObs(new Obs().withFormSubmissionField(Constants.JSON_FORM_KEY.MALE_CONDOMS_OFFSET).withValue("15")
                .withFieldCode(Constants.JSON_FORM_KEY.MALE_CONDOMS_OFFSET).withFieldType("formsubmissionField").withFieldDataType("text").withParentCode("").withHumanReadableValues(new ArrayList<>()));


        try {
            CdpUtil.processEvent(allSharedPreferences, baseEvent);
        } catch (Exception e) {
            Timber.e(e);
        }
    }
}
