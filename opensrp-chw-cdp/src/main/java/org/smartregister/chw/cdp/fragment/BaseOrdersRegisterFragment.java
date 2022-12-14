package org.smartregister.chw.cdp.fragment;

import static org.smartregister.chw.cdp.fragment.BaseCdpRegisterFragment.CLICK_VIEW_NORMAL;
import static org.smartregister.util.JsonFormUtils.generateRandomUUIDString;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.google.android.material.tabs.TabLayout;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.cdp.R;
import org.smartregister.chw.cdp.CdpLibrary;
import org.smartregister.chw.cdp.activity.BaseOrderDetailsActivity;
import org.smartregister.chw.cdp.contract.BaseOrdersRegisterFragmentContract;
import org.smartregister.chw.cdp.dao.CdpStockingDao;
import org.smartregister.chw.cdp.model.BaseOrdersRegisterFragmentModel;
import org.smartregister.chw.cdp.presenter.BaseOrdersRegisterFragmentPresenter;
import org.smartregister.chw.cdp.provider.BaseOrdersRegisterProvider;
import org.smartregister.chw.cdp.util.CdpUtil;
import org.smartregister.chw.cdp.util.Constants;
import org.smartregister.chw.cdp.util.DBConstants;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.Obs;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.view.customcontrols.CustomFontTextView;
import org.smartregister.view.customcontrols.FontVariant;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import timber.log.Timber;

public class BaseOrdersRegisterFragment extends BaseRegisterFragment implements BaseOrdersRegisterFragmentContract.View {
    protected Toolbar toolbar;
    protected CustomFontTextView titleView;
    protected String customGroupFilter;
    private TextView tvMaleCondomsStockOnHandCount;
    private TextView tvFemaleCondomsStockOnHandCount;

    @Override
    public void setupViews(View view) {
        super.setupViews(view);

        toolbar = view.findViewById(org.smartregister.R.id.register_toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.setContentInsetsRelative(0, 0);
        toolbar.setContentInsetStartWithNavigation(0);
        tvMaleCondomsStockOnHandCount = view.findViewById(R.id.male_condoms_stock_count_tv);
        tvFemaleCondomsStockOnHandCount = view.findViewById(R.id.female_condoms_stock_count_tv);

        AllSharedPreferences allSharedPreferences = CdpLibrary.getInstance().context().allSharedPreferences();
        String locationId = allSharedPreferences.fetchUserLocalityId(allSharedPreferences.fetchRegisteredANM());

        // Update top left icon
        // Update top left icon
        qrCodeScanImageView = view.findViewById(org.smartregister.R.id.scanQrCode);
        if (qrCodeScanImageView != null) {
            qrCodeScanImageView.setVisibility(android.view.View.GONE);
        }

        // Update Search bar
        android.view.View searchBarLayout = view.findViewById(org.smartregister.R.id.search_bar_layout);
        searchBarLayout.setVisibility(View.GONE);

        if (getSearchView() != null) {
            getSearchView().setVisibility(View.GONE);
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
            titleView.setText(getFragmentTitle());
            titleView.setFontVariant(FontVariant.REGULAR);
        }

        setUpTabLayout(view);

        updateStockOnHand(CdpStockingDao.getCurrentMaleCondomCount(locationId), CdpStockingDao.getCurrentFemaleCondomCount(locationId));
    }

    @Override
    public void onResume() {
        super.onResume();
        new android.os.Handler().postDelayed(() -> setupViews(rootView), 1000);
    }

    protected void updateStockOnHand(int maleCondomsStockOnHand, int femaleCondomsStockOnHand) {
        if (tvMaleCondomsStockOnHandCount != null)
            tvMaleCondomsStockOnHandCount.setText(getString(R.string.male_condoms_stock_on_hand, maleCondomsStockOnHand));
        if (tvFemaleCondomsStockOnHandCount != null)
            tvFemaleCondomsStockOnHandCount.setText(getString(R.string.female_condoms_stock_on_hand, femaleCondomsStockOnHand));
    }

    protected int getFragmentTitle() {
        return R.string.menu_order_condoms;
    }

    @Override
    public BaseOrdersRegisterFragmentContract.Presenter presenter() {
        return (BaseOrdersRegisterFragmentContract.Presenter) presenter;
    }

    @Override
    public BaseOrdersRegisterFragmentContract.Model model() {
        return new BaseOrdersRegisterFragmentModel();
    }

    @Override
    public void initializeAdapter(String tableName) {
        BaseOrdersRegisterProvider registerProvider = new BaseOrdersRegisterProvider(getActivity(), registerActionHandler, paginationViewHandler);
        clientAdapter = new RecyclerViewPaginatedAdapter(null, registerProvider, context().commonrepository(this.tablename));
        clientAdapter.setCurrentlimit(20);
        clientsView.setAdapter(clientAdapter);
    }

    @Override
    public void startOrderForm() {
        //implement in chw and hf
    }

    @Override
    public void countExecute() {
        Cursor c = null;
        try {

            String query = "select count(*) from " + presenter().getMainTable() + " inner join " + Constants.TABLES.TASK +
                    " on " + presenter().getMainTable() + "." + DBConstants.KEY.BASE_ENTITY_ID + " = " +
                    Constants.TABLES.TASK + "." + DBConstants.KEY.FOR +
                    " where " + presenter().getMainCondition();


            if (StringUtils.isNotBlank(customGroupFilter)) {
                query = query + " and ( " + customGroupFilter + " ) ";
            }

            c = commonRepository().rawCustomQueryForAdapter(query);
            c.moveToFirst();
            clientAdapter.setTotalcount(c.getInt(0));
            Timber.v("total count here %s", clientAdapter.getTotalcount());

            clientAdapter.setCurrentlimit(20);
            clientAdapter.setCurrentoffset(0);

        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return new CursorLoader(requireActivity()) {
                @Override
                public Cursor loadInBackground() {
                    // Count query
                    final String COUNT = "count_execute";
                    if (args != null && args.getBoolean(COUNT)) {
                        countExecute();
                    }
                    String query = presenter().getDefaultFilterSortQuery(customGroupFilter, mainSelect, Sortqueries, clientAdapter);
                    return commonRepository().rawCustomQueryForAdapter(query);
                }
            };
        }
        return super.onCreateLoader(id, args);
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
        if (getActivity() == null || !(view.getTag() instanceof CommonPersonObjectClient)) {
            return;
        }

        CommonPersonObjectClient client = (CommonPersonObjectClient) view.getTag();
        if (view.getTag(R.id.VIEW_ID) == CLICK_VIEW_NORMAL) {
            showDetails(client);
        }
    }

    @Override
    public void showDetails(CommonPersonObjectClient cp) {
        BaseOrderDetailsActivity.startMe(requireActivity(), cp);
    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_cdp_order_receive;
    }

    @Override
    protected void initializePresenter() {
        presenter = new BaseOrdersRegisterFragmentPresenter(this, model());
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
    protected void attachSyncButton(View view) {
        syncButton = view.findViewById(R.id.sync_refresh);
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
                startOrderForm();
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

    protected void setUpTabLayout(View view) {
        TabLayout tabLayout = getTabLayout(view);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        customGroupFilter = "";
                        filterandSortExecute();
                        break;
                    case 1:
                        customGroupFilter = presenter().getSentOrdersQuery();
                        filterandSortExecute();
                        break;
                    case 2:
                        customGroupFilter = presenter().getSuccessFulOrdersQuery();
                        filterandSortExecute();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //do something
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //do something
            }
        });

    }

    protected TabLayout getTabLayout(View view) {
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setVisibility(View.VISIBLE);
        return tabLayout;
    }

}
