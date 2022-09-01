package org.smartregister.chw.cdp.util;

import org.smartregister.chw.cdp.CdpLibrary;
import org.smartregister.chw.cdp.domain.Visit;
import org.smartregister.chw.cdp.domain.VisitDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisitUtils {
    public static List<Visit> getVisits(String memberID) {

        List<Visit> visits = getVisitsOnly(memberID, Constants.EVENT_TYPE.CDP_OUTLET_RESTOCK);

        int x = 0;
        while (visits.size() > x) {
            Visit visit = visits.get(x);
            List<VisitDetail> detailList = getVisitDetailsOnly(visit.getVisitId());
            visits.get(x).setVisitDetails(getVisitGroups(detailList));
            x++;
        }

        return visits;
    }

    public static List<Visit> getVisitsOnly(String memberID, String visitName) {
        return new ArrayList<>(CdpLibrary.getInstance().visitRepository().getVisits(memberID, visitName));
    }

    public static List<VisitDetail> getVisitDetailsOnly(String visitID) {
        return CdpLibrary.getInstance().visitDetailsRepository().getVisits(visitID);
    }

    public static Map<String, List<VisitDetail>> getVisitGroups(List<VisitDetail> detailList) {
        Map<String, List<VisitDetail>> visitMap = new HashMap<>();

        for (VisitDetail visitDetail : detailList) {

            List<VisitDetail> visitDetailList = visitMap.get(visitDetail.getVisitKey());
            if (visitDetailList == null)
                visitDetailList = new ArrayList<>();

            visitDetailList.add(visitDetail);

            visitMap.put(visitDetail.getVisitKey(), visitDetailList);
        }
        return visitMap;
    }
}
