package org.smartregister.chw.cdp.dao;

import org.joda.time.DateTime;
import org.smartregister.chw.cdp.util.Constants;
import org.smartregister.dao.AbstractDao;

public class CdpOrderDao extends AbstractDao {

    public static void updateOrderData(String orderTable, String locationId,
                                       String baseEntityId,
                                       String formSubmissionId,
                                       String condomType,
                                       String condomBrand,
                                       String quantityRequested,
                                       String requestType) {

        DateTime now = new DateTime();

        String sql = "INSERT INTO " + orderTable + "" +
                "    (id, location_id, form_submission_id, base_entity_id, condom_type, condom_brand, quantity_requested, request_type, requested_at) " +
                "         VALUES ('" + baseEntityId + "', '" + locationId + "', '" + formSubmissionId + "', '" + baseEntityId + "', '" + condomType + "', '" + condomBrand + "', '" + quantityRequested + "', '" + requestType + "', '" + now.getMillis() + "')" +
                "       ON CONFLICT (id) DO UPDATE" +
                "       SET location_id = '" + locationId + "'," +
                "           form_submission_id = '" + formSubmissionId + "', " +
                "           condom_type = '" + condomType + "', " +
                "           condom_brand = '" + condomBrand + "', " +
                "           quantity_requested = '" + quantityRequested + "', " +
                "           request_type = '" + requestType + "', " +
                "           requested_at = '" + now.getMillis() + "'" +
                "       ";
        updateDB(sql);
    }

}
