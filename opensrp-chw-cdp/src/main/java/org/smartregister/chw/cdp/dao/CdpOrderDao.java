package org.smartregister.chw.cdp.dao;

import org.joda.time.DateTime;
import org.smartregister.chw.cdp.util.Constants;
import org.smartregister.dao.AbstractDao;

public class CdpOrderDao extends AbstractDao {
    private static final String mainOrdersTable = Constants.TABLES.CDP_ORDERS;
    private static final String mainFeedbackTable = Constants.TABLES.CDP_ORDER_FEEDBACK;

    public static void updateOrderData(String locationId,
                                       String baseEntityId,
                                       String formSubmissionId,
                                       String condomType,
                                       String condomBrand,
                                       String quantityRequested,
                                       String requestType,
                                       String requestDate) {


        String sql = "INSERT INTO " + mainOrdersTable + "" +
                "    (id, location_id, form_submission_id, base_entity_id, condom_type, condom_brand, quantity_requested, request_type, requested_at) " +
                "         VALUES ('" + baseEntityId + "', '" + locationId + "', '" + formSubmissionId + "', '" + baseEntityId + "', '" + condomType + "', '" + condomBrand + "', '" + quantityRequested + "', '" + requestType + "', '" + requestDate + "')" +
                "       ON CONFLICT (id) DO UPDATE" +
                "       SET location_id = '" + locationId + "'," +
                "           form_submission_id = '" + formSubmissionId + "', " +
                "           condom_type = '" + condomType + "', " +
                "           condom_brand = '" + condomBrand + "', " +
                "           quantity_requested = '" + quantityRequested + "', " +
                "           request_type = '" + requestType + "', " +
                "           requested_at = '" + requestDate + "'" +
                "       ";
        updateDB(sql);
    }

    public static void updateFeedbackData(String locationId,
                                          String baseEntityId,
                                          String requestReference,
                                          String condomType,
                                          String condomBrand,
                                          String quantityResponse,
                                          String responseStatus,
                                          String responseDate) {

        String sql = "INSERT INTO " + mainFeedbackTable + "" +
                "    (id, location_id, request_reference, base_entity_id, condom_type, condom_brand, quantity_response, response_status, response_at) " +
                "         VALUES ('" + baseEntityId + "', '" + locationId + "', '" + requestReference + "', '" + baseEntityId + "', '" + condomType + "', '" + condomBrand + "', '" + quantityResponse + "', '" + responseStatus + "', '" + responseDate + "')" +
                "       ON CONFLICT (id) DO UPDATE" +
                "       SET location_id = '" + locationId + "'," +
                "           request_reference = '" + requestReference + "', " +
                "           condom_type = '" + condomType + "', " +
                "           condom_brand = '" + condomBrand + "', " +
                "           quantity_response = '" + quantityResponse + "', " +
                "           response_status = '" + responseStatus + "', " +
                "           response_at = '" + responseDate + "'" +
                "       ";
        updateDB(sql);
    }


}
