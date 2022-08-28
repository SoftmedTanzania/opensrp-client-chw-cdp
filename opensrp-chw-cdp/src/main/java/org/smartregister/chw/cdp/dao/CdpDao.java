package org.smartregister.chw.cdp.dao;

import org.smartregister.chw.cdp.domain.OutletObject;
import org.smartregister.dao.AbstractDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CdpDao extends AbstractDao {

    public static Date getCDPTestDate(String baseEntityID) {
        String sql = "select cdp_test_date from ec_cdp_confirmation where base_entity_id = '" + baseEntityID + "'";

        DataMap<Date> dataMap = cursor -> getCursorValueAsDate(cursor, "cdp_test_date", getNativeFormsDateFormat());

        List<Date> res = readData(sql, dataMap);
        if (res == null || res.size() != 1)
            return null;

        return res.get(0);
    }
    public static Date getCDPFollowUpVisitDate(String baseEntityID) {
        String sql = "SELECT eventDate FROM event where eventType ='CDP Follow-up Visit' AND baseEntityId ='" + baseEntityID + "'";

        DataMap<Date> dataMap = cursor -> getCursorValueAsDate(cursor, "eventDate", getNativeFormsDateFormat());

        List<Date> res = readData(sql, dataMap);
        if (res == null || res.size() != 1)
            return null;

        return res.get(0);
    }
    public static void closeCDPMemberFromRegister(String baseEntityID) {
        String sql = "update ec_cdp_confirmation set is_closed = 1 where base_entity_id = '" + baseEntityID + "'";
        updateDB(sql);
    }

    public static boolean isRegisteredForCDP(String baseEntityID) {
        String sql = "SELECT count(p.base_entity_id) count FROM ec_cdp_confirmation p " +
                "WHERE p.base_entity_id = '" + baseEntityID + "' AND p.is_closed = 0 AND p.cdp  = 1 " +
                "AND datetime('NOW') <= datetime(p.last_interacted_with/1000, 'unixepoch', 'localtime','+15 days')";

        DataMap<Integer> dataMap = cursor -> getCursorIntValue(cursor, "count");

        List<Integer> res = readData(sql, dataMap);
        if (res == null || res.size() != 1)
            return false;

        return res.get(0) > 0;
    }

    public static Integer getCDPFamilyMembersCount(String familyBaseEntityId) {
        String sql = "SELECT count(emc.base_entity_id) count FROM ec_cdp_confirmation emc " +
                "INNER Join ec_family_member fm on fm.base_entity_id = emc.base_entity_id " +
                "WHERE fm.relational_id = '" + familyBaseEntityId + "' AND fm.is_closed = 0 " +
                "AND emc.is_closed = 0 AND emc.cdp = 1";

        DataMap<Integer> dataMap = cursor -> getCursorIntValue(cursor, "count");

        List<Integer> res = readData(sql, dataMap);
        if (res == null || res.size() == 0)
            return 0;
        return res.get(0);
    }

    public static OutletObject getOutlet(String baseEntityID) {
        String sql = "SELECT p.outlet_name, p.outlet_type, p.outlet_ward_name, p.unique_id " +
                "           FROM ec_cdp_outlet p " +
                "           INNER JOIN ec_cdp_register m on p.base_entity_id = '" + baseEntityID +"' " +
                "           WHERE m.is_closed is 0";
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        DataMap<OutletObject> dataMap = cursor -> {
            OutletObject outletObject = new OutletObject();

            outletObject.setOutletName(getCursorValue(cursor, "outlet_name", ""));
            outletObject.setOutletId(getCursorValue(cursor, "unique_id", ""));
            outletObject.setOutletWardName(getCursorValue(cursor, "outlet_ward_name", ""));
            outletObject.setOutletType(getCursorValue(cursor, "outlet_type", ""));

            return outletObject;
        };

        List<OutletObject> res = readData(sql, dataMap);
        if (res == null || res.size() != 1)
            return null;

        return res.get(0);
    }
}
