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

}
