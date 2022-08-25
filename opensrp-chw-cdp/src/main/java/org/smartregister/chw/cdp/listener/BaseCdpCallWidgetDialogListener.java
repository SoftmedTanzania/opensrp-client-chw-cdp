package org.smartregister.chw.cdp.listener;


import android.view.View;

import org.smartregister.chw.cdp.fragment.BaseCdpCallDialogFragment;
import org.smartregister.chw.cdp.util.TestUtil;
import org.smartregister.cdp.R;

import timber.log.Timber;

public class BaseCdpCallWidgetDialogListener implements View.OnClickListener {

    private BaseCdpCallDialogFragment callDialogFragment;

    public BaseCdpCallWidgetDialogListener(BaseCdpCallDialogFragment dialogFragment) {
        callDialogFragment = dialogFragment;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.cdp_call_close) {
            callDialogFragment.dismiss();
        } else if (i == R.id.cdp_call_head_phone) {
            try {
                String phoneNumber = (String) v.getTag();
                TestUtil.launchDialer(callDialogFragment.getActivity(), callDialogFragment, phoneNumber);
                callDialogFragment.dismiss();
            } catch (Exception e) {
                Timber.e(e);
            }
        } else if (i == R.id.call_cdp_client_phone) {
            try {
                String phoneNumber = (String) v.getTag();
                TestUtil.launchDialer(callDialogFragment.getActivity(), callDialogFragment, phoneNumber);
                callDialogFragment.dismiss();
            } catch (Exception e) {
                Timber.e(e);
            }
        }
    }
}
