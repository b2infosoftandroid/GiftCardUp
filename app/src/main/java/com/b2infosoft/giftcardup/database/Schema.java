package com.b2infosoft.giftcardup.database;

/**
 * Created by rajesh on 8/2/2016.
 */
public class Schema {
    private Schema() {

    }

    public static Schema getInstance() {
        return new Schema();
    }

    /*       CATEGORY TABLE SCHEMA  START   */

    public final String CATEGORY_TABLE = "category";
    public final String CATEGORY_NAME_COLUMN = "category_name";
    public final String CATEGORY_ID_COLUMN = "category_id";

    /*       CATEGORY TABLE SCHEMA  END   */

    /*       STATE TABLE SCHEMA START       */
    public final String STATE_TABLE = "state";
    public final String STATE_ID_COLUMN = "state_id";
    public final String STATE_NAME_COLUMN = "state_name";
    public final String STATE_ABBREVIATION_COLUMN = "state_abbreviation";
    /*       STATE TABLE SCHEMA END       */

    /*       MAIL PRICE TABLE SCHEMA START       */
    public final String MAIL_PRICE_TABLE = "mail_price";
    public final String MAIL_PRICE_FIRST_CLASS_COLUMN = "first_class_price";
    public final String MAIL_PRICE_PRIORITY_COLUMN = "priority_price";
    public final String MAIL_PRICE_EXPRESS_COLUMN = "express_price";
    /*       MAIL PRICE TABLE SCHEMA END       */


    /*        CONTROL PANEL TABLE SCHEMA START   */
    public final String CONTROL_PANEL_TABLE = "control_panel";
    public final String CONTROL_PANEL_ID = "id";
    public final String CONTROL_PANEL_GENERAL_COMMISSION = "general_commission";
    public final String CONTROL_PANEL_SHIPPING_CHARGE = "shipping_charge";
    public final String CONTROL_PANEL_SELLING_PERCENTAGE = "selling_percentage";
    public final String CONTROL_PANEL_COMPANY_NAME = "company_name";
    public final String CONTROL_PANEL_PHONE_NO = "phone_number";
    public final String CONTROL_PANEL_EMAIL = "email";
    public final String CONTROL_PANEL_CITY = "address";
    public final String CONTROL_PANEL_ADDRESS = "city";
    public final String CONTROL_PANEL_STATE = "state";
    public final String CONTROL_PANEL_ZIP_CODE = "zip_code";
    public final String CONTROL_PANEL_PROCESS_TIME = "process_time";
    public final String CONTROL_PANEL_CARD_ATTEMPT_TIME = "card_attempt_time";
    public final String CONTROL_PANEL_IMAGE = "image";
    public final String CONTROL_PANEL_FIRST_CLASS_PRICE = "first_class_price";
    public final String CONTROL_PANEL_PRIORITY_PRICE = "priority_price";
    public final String CONTROL_PANEL_EXPRESS_PRICE = "express_price";
    public final String CONTROL_PANEL_MIN_SCORE = "minimum_score";
    public final String CONTROL_PANEL_MAX_SCORE = "maximum_score";
    public final String CONTROL_PANEL_REFFERAL_AMOUNT = "referral_amount";
    /*        CONTROL PANEL TABLE SCHEMA END   */


}
