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
        String CDP_REGISTRATION = "CDP Registration";
        String CDP_FOLLOW_UP_VISIT = "CDP Follow-up Visit";
    }

    interface FORMS {
        String CDP_REGISTRATION = "cdp_confirmation";
    }

    interface TABLES {
        String CDP_REGISTER = "ec_cdp_register";
        String CDP_FOLLOW_UP = "ec_cdp_follow_up_visit";
    }

    interface ACTIVITY_PAYLOAD {
        String BASE_ENTITY_ID = "BASE_ENTITY_ID";
        String FAMILY_BASE_ENTITY_ID = "FAMILY_BASE_ENTITY_ID";
        String ACTION = "ACTION";
        String CDP_FORM_NAME = "CDP_FORM_NAME";

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

}