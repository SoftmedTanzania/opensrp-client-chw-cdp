package org.smartregister.chw.cdp.contract;

import android.content.Context;

public interface BaseCdpCallDialogContract {

    interface View {
        void setPendingCallRequest(Dialer dialer);
        Context getCurrentContext();
    }

    interface Dialer {
        void callMe();
    }
}
