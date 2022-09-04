package org.smartregister.chw.cdp.dao;

import org.smartregister.chw.cdp.util.Constants;
import org.smartregister.dao.AbstractDao;

import java.util.Date;

public class CdpOrderDao extends AbstractDao {
    private static final String mainOrdersTable = Constants.TABLES.CDP_ORDERS;

    public static void updateOrderData(String locationId,
                                          String baseEntityId,
                                          String formSubmissionId,
                                          String condomType,
                                          String condomBrand,
                                          String quantityRequested,
                                          String requestType) {

        Date now = new Date();

        String sql = "INSERT INTO " + mainOrdersTable + "" +
                "    (id, location_id, form_submission_id, base_entity_id, condom_type, condom_brand, quantity_requested, request_type, requested_at) " +
                "         VALUES ('" + baseEntityId + "', '" + locationId +"', '" + formSubmissionId + "', '" + baseEntityId + "', '" + condomType + "', '" + condomBrand + "', '" + quantityRequested + "', '" + requestType + "', '" + now + "')" +
                "       ON CONFLICT (id) DO UPDATE" +
                "       SET location_id = '" + locationId + "'," +
                "           form_submission_id = '" + formSubmissionId + "', " +
                "           condom_type = '" + condomType + "', " +
                "           condom_brand = '" + condomBrand + "', " +
                "           quantity_requested = '" + quantityRequested + "', " +
                "           request_type = '" + requestType + "', " +
                "           requested_at = '" + now + "'" +
                "       ";
        updateDB(sql);
    }

}
