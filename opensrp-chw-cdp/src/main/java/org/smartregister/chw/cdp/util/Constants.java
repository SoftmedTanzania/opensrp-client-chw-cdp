package org.smartregister.chw.cdp.util;

public interface Constants {

    int REQUEST_CODE_GET_JSON = 2244;
    String ENCOUNTER_TYPE = "encounter_type";
    String STEP_ONE = "step1";
    String STEP_TWO = "step2";

    interface JSON_FORM_EXTRA {
        String JSON = "json";
        String ENCOUNTER_TYPE = "encounter_type";
        String ENTITY_ID = "ENTITY_ID";
    }

    interface EVENT_TYPE {
        String CDP_OUTLET_REGISTRATION = "CDP Outlet Registration";
        String CDP_REGISTRATION = "CDP Registration";
        String CDP_FOLLOW_UP_VISIT = "CDP Follow-up Visit";
        String CDP_OUTLET_VISIT = "CDP Outlet Visit";
        String CDP_RECEIVE_FROM_FACILITY = "CDP Receive From Facility";
        String CDP_OUTLET_RESTOCK = "CDP Outlet Restock";
        String CDP_CONDOM_ORDER = "CDP Condom Order";
    }

    interface FORMS {
        String CDP_OUTLET_REGISTRATION = "cdp_outlet_registration";
        String CDP_OUTLET_RESTOCK = "cdp_outlet_restock";
        String CD_OUTLET_VISIT = "cdp_outlet_visit";
    }

    interface TABLES {
        String CDP_OUTLET = "ec_cdp_outlet";
        String CDP_REGISTER = "ec_cdp_register";
        String CDP_FOLLOW_UP = "ec_cdp_follow_up_visit";
        String CDP_STOCK_COUNT = "ec_cdp_stock_count";
        String CDP_STOCK_LOG = "ec_cdp_stock_log";
        String CDP_OUTLET_VISIT = "ec_cdp_outlet_visit";
        String CDP_ORDERS = "ec_cdp_orders";
        String TASK = "task";
    }

    interface ACTIVITY_PAYLOAD {
        String BASE_ENTITY_ID = "BASE_ENTITY_ID";
        String FAMILY_BASE_ENTITY_ID = "FAMILY_BASE_ENTITY_ID";
        String ACTION = "ACTION";
        String CDP_FORM_NAME = "CDP_FORM_NAME";
        String OUTLET_OBJECT = "OUTLET_OBJECT";
    }

    interface ACTIVITY_PAYLOAD_TYPE {
        String REGISTRATION = "REGISTRATION";
        String FOLLOW_UP_VISIT = "FOLLOW_UP_VISIT";
    }

    interface CONFIGURATION {
        String CDP_CONFIRMATION = "cdp_confirmation";
    }

    interface CDP_MEMBER_OBJECT {
        String MEMBER_OBJECT = "memberObject";
    }

    interface KEY {
        String PHOTO = "photo";
    }

    interface JSON_FORM_KEY {
        String UNIQUE_ID = "unique_id";
        String FEMALE_CONDOMS_OFFSET = "female_condoms_offset";
        String MALE_CONDOMS_OFFSET = "male_condoms_offset";
    }

    interface STOCK_EVENT_TYPES {
        String INCREMENT = "increment";
        String DECREMENT = "decrement";
    }
}