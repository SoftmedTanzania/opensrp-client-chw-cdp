package org.smartregister.chw.cdp.util;

import org.jetbrains.annotations.NotNull;
import org.smartregister.chw.cdp.CdpLibrary;
import org.smartregister.chw.cdp.pojo.CdpOutletEventClient;

import java.util.ArrayList;
import java.util.List;

public class OutletUtil {
    @NotNull
    public static List<CdpOutletEventClient> getOutletEventClient(String jsonString) {
        List<CdpOutletEventClient> outletEventClients = new ArrayList<>();

        CdpOutletEventClient outletDetailsEvent = OutletJsonFormUtil.processOutletRegistrationForm(
                CdpLibrary.getInstance().context().allSharedPreferences(), jsonString);
        if (outletDetailsEvent == null) {
            return outletEventClients;
        }

        outletEventClients.add(new CdpOutletEventClient(outletDetailsEvent.getClient(), outletDetailsEvent.getEvent()));
        return outletEventClients;
    }
}
