package org.smartregister.chw.cdp.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.smartregister.cdp.R;
import org.smartregister.chw.cdp.CdpLibrary;
import org.smartregister.chw.cdp.contract.BaseOrdersRegisterFragmentContract;
import org.smartregister.chw.cdp.interactor.BaseOrdersRegisterFragmentInteractor;
import org.smartregister.chw.cdp.model.BaseOrdersRegisterFragmentModel;
import org.smartregister.chw.cdp.presenter.BaseOrdersRegisterFragmentPresenter;
import org.smartregister.chw.cdp.provider.BaseOrdersRegisterProvider;
import org.smartregister.chw.cdp.util.CdpUtil;
import org.smartregister.chw.cdp.util.Constants;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.Obs;
import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.view.customcontrols.CustomFontTextView;
import org.smartregister.view.customcontrols.FontVariant;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import androidx.appcompat.widget.Toolbar;
import timber.log.Timber;

import static org.smartregister.util.JsonFormUtils.generateRandomUUIDString;

public class BaseOrdersRegisterFragment extends BaseRegisterFragment implements BaseOrdersRegisterFragmentContract.View {
    protected Toolbar toolbar;
    protected CustomFontTextView titleView;

    @Override
    public void setupViews(View view) {
        super.setupViews(view);

        toolbar = view.findViewById(org.smartregister.R.id.register_toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.setContentInsetsRelative(0, 0);
        toolbar.setContentInsetStartWithNavigation(0);

        // Update top left icon
        // Update top left icon
        qrCodeScanImageView = view.findViewById(org.smartregister.R.id.scanQrCode);
        if (qrCodeScanImageView != null) {
            qrCodeScanImageView.setVisibility(android.view.View.GONE);
        }

        // Update Search bar
        android.view.View searchBarLayout = view.findViewById(org.smartregister.R.id.search_bar_layout);
        searchBarLayout.setBackgroundResource(R.color.customAppThemeBlue);

        if (getSearchView() != null) {
            getSearchView().setBackgroundResource(R.color.white);
            getSearchView().setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_search, 0, 0, 0);
        }


        //hide views
        ImageView logo = view.findViewById(org.smartregister.R.id.opensrp_logo_image_view);
        logo.setVisibility(android.view.View.GONE);
        android.view.View topLeftLayout = view.findViewById(R.id.top_left_layout);
        topLeftLayout.setVisibility(android.view.View.GONE);
        android.view.View topRightLayout = view.findViewById(R.id.top_right_layout);
        topRightLayout.setVisibility(android.view.View.VISIBLE);
        android.view.View sortFilterBarLayout = view.findViewById(R.id.register_sort_filter_bar_layout);
        sortFilterBarLayout.setVisibility(android.view.View.GONE);
        android.view.View filterSortLayout = view.findViewById(R.id.filter_sort_layout);
        filterSortLayout.setVisibility(android.view.View.GONE);

        titleView = view.findViewById(R.id.txt_title_label);
        if (titleView != null) {
            titleView.setVisibility(android.view.View.VISIBLE);
            titleView.setText(R.string.order_receive);
            titleView.setFontVariant(FontVariant.REGULAR);
        }
    }


    @Override
    public BaseOrdersRegisterFragmentContract.Presenter presenter() {
        return (BaseOrdersRegisterFragmentContract.Presenter) presenter;
    }

    @Override
    public void initializeAdapter(String tableName) {
        BaseOrdersRegisterProvider registerProvider = new BaseOrdersRegisterProvider(getActivity(), registerActionHandler, paginationViewHandler);
        clientAdapter = new RecyclerViewPaginatedAdapter(null, registerProvider, context().commonrepository(this.tablename));
        clientAdapter.setCurrentlimit(20);
        clientsView.setAdapter(clientAdapter);
    }


    @Override
    protected String getMainCondition() {
        return presenter().getMainCondition();
    }

    @Override
    protected String getDefaultSortQuery() {
        return presenter().getDefaultSortQuery();
    }

    @Override
    protected void startRegistration() {
        //
    }

    @Override
    protected void onViewClicked(View view) {
        //
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_cdp_order_receive;
    }

    @Override
    protected void initializePresenter() {
        presenter = new BaseOrdersRegisterFragmentPresenter(this, new BaseOrdersRegisterFragmentInteractor(), new BaseOrdersRegisterFragmentModel());
    }

    @Override
    public void setUniqueID(String qrCode) {
        //
    }

    @Override
    public void setAdvancedSearchFormData(HashMap<String, String> advancedSearchFormData) {
        //
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

    @Override
    public void showNotFoundPopup(String opensrpId) {
        //
    }

}
