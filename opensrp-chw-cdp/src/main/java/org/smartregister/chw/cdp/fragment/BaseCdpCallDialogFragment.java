package org.smartregister.chw.cdp.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.cdp.contract.BaseCdpCallDialogContract;
import org.smartregister.chw.cdp.domain.OutletObject;
import org.smartregister.chw.cdp.listener.BaseCdpCallWidgetDialogListener;
import org.smartregister.cdp.R;

import static android.view.View.GONE;
import static org.smartregister.util.Utils.getName;

public class BaseCdpCallDialogFragment extends DialogFragment implements BaseCdpCallDialogContract.View {

    public static final String DIALOG_TAG = "BaseCDPCallDialogFragment_DIALOG_TAG";
    private static OutletObject outletObject;
    private View.OnClickListener listener = null;

    public static BaseCdpCallDialogFragment launchDialog(Activity activity, OutletObject oO) {
        BaseCdpCallDialogFragment dialogFragment = BaseCdpCallDialogFragment.newInstance();
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        Fragment prev = activity.getFragmentManager().findFragmentByTag(DIALOG_TAG);
        outletObject = oO;
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        dialogFragment.show(ft, DIALOG_TAG);

        return dialogFragment;
    }

    public static BaseCdpCallDialogFragment newInstance() {
        return new BaseCdpCallDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.ChwTheme_Dialog_FullWidth);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup dialogView = (ViewGroup) inflater.inflate(R.layout.cdp_member_call_widget_dialog_fragment, container, false);
        setUpPosition();
        if (listener == null) {
            listener = new BaseCdpCallWidgetDialogListener(this);
        }

        initUI(dialogView);
        return dialogView;
    }

    private void setCallTitle(ViewGroup rootView, int viewId, final String message) {
        TextView callTitle = rootView.findViewById(viewId);

        callTitle.setText(String.format("%s %s", message, getResources().getString(R.string.call_cdp_client)));

    }

    private void initUI(ViewGroup rootView) {
//        if (StringUtils.isNotBlank(outletObject.getPhoneNumber())) {
//            setCallTitle(rootView, R.id.call_title, getResources().getString(R.string.call));
//
//            rootView.findViewById(R.id.cdp_layout_family_head).setVisibility(GONE);
//
//            //just a member
//            TextView cdpClientNameTextView = rootView.findViewById(R.id.call_cdp_client_name);
//            cdpClientNameTextView.setText(String.format("%s %s %s", MEMBER_OBJECT.getFirstName(), MEMBER_OBJECT.getMiddleName(), MEMBER_OBJECT.getLastName()));
//
//            setCallTitle(rootView, R.id.call_cdp_client_title, "");
//            TextView callCDPClientPhone = rootView.findViewById(R.id.call_cdp_client_phone);
//            callCDPClientPhone.setTag(MEMBER_OBJECT.getPhoneNumber());
//            callCDPClientPhone.setText(getName(getCurrentContext().getString(R.string.call), MEMBER_OBJECT.getPhoneNumber()));
//            callCDPClientPhone.setOnClickListener(listener);
//
//        }

        rootView.findViewById(R.id.cdp_call_close).setOnClickListener(listener);
    }

    private void setUpPosition() {
        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        p.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
        p.y = 20;
        getDialog().getWindow().setAttributes(p);
    }

    @Override
    public Context getCurrentContext() {
        return getActivity();
    }

    @Override
    public void setPendingCallRequest(BaseCdpCallDialogContract.Dialer dialer) {
//        Implement pending call request
//        BaseAncWomanCallDialogContract.Dialer mDialer = dialer;
    }
}
