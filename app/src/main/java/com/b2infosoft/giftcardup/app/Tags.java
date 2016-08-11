package com.b2infosoft.giftcardup.app;

/**
 * Created by rajesh on 8/10/2016.
 */
public class Tags {
    private static Tags ourInstance = new Tags();

    public static Tags getInstance() {
        return ourInstance;
    }

    private Tags() {
    }

    /////////////   GLOBAL TAGS   ////////////
    public final String  SUCCESS= "success";
    public final String  MESSAGE= "message";

    public final int SUSPEND = 2;
    public final int PASS = 1;
    public final int FAIL = 0;

    //////////////  USER ACTIONS    //////////////

    public final String GIFT_CARDS = "gift_cards";
    public final String  USER_REQUEST= "user_request";


    //////////////  ARRAYS   /////////////////////

    public final String ARR_LOGIN_TYPE[]={"login","profile","update","logout"};
    public final String ARR_CARD_TYPE[]={"Physical","eCard"};


    /////////////  RESPONSE ARRAY  ////////////////
    public final String  USER_INFO= "user_info";

    /////////////// GIFT CARD ///////////////////////

    public final String GIFT_CARD_ID = "gift_card_id";
    public final String SAVE_UP_TO = "save_up_to";
    public final String TOTAL_CARD = "total_card";
    public final String TOTAL_PERCENTAGE = "total_percentage";
    public final String READ_STATUS = "read_status";
    public final String PARENT_GIFT_CARD_ID = "parent_gift_card_id";
    public final String CARD_NAME = "card_name";
    public final String SERIAL_NUMBER = "serial_number";
    public final String CARD_PIN = "card_pin";
    public final String CARD_PRICE = "card_price";
    public final String CARD_TYPE = "card_type";
    public final String VALUE = "value";
    public final String SELL = "sell";
    public final String SELLING_PERCENTAGE = "selling_percentage";
    public final String YOUR_EARNING = "your_earning";
    public final String SHIPPING_AND_COMMISSION_CHARGE = "shipping_and_commission_charge";
    public final String PERCENTAGE_OFF = "percentage_off";
    public final String SOLD_ON = "sold_on";
    public final String CARD_DESCRIPTION = "card_description";
    public final String CARD_IMG = "card_img";
    public final String APPROVE_DATE = "approve_date";
    public final String STATUS_TYPE = "status_type";
    public final String SOLD_STATUS = "sold_status";
    public final String BARCODE_IMG = "barcode_img";
    public final String DENY_REASON = "deny_reason";
    public final String NEED_REVIEW = "need_review";


    /////////////// USER PROFILE  ///////////////

    public final String  USER_ID= "user_id";
    public final String  EMPLOYEE_ID= "employee_id";
    public final String  USER_TYPE= "user_type";
    public final String  FIRST_NAME= "first_name";
    public final String  LAST_NAME= "last_name";
    public final String  PHONE_NUMBER= "phone_number";
    public final String  GENDER= "gender";
    public final String  FB_ID= "fb_id";
    public final String  ADDRESS= "address";
    public final String  SUITE_NUMBER= "suite_number";
    public final String  CITY= "city";
    public final String  ZIP_CODE= "zip_code";
    public final String  STATE= "state";
    public final String  COMPANY_NAME= "company_name";
    public final String  EMAIL= "email";
    public final String  PASSWORD= "password";
    public final String  IMAGE= "image";
    public final String  IDENTIFY_IMAGE= "identity_image";
    public final String  GROUP_ID= "group_id";
    public final String  JOIN_DATE= "join_date";
    public final String  EMAIL_VERIFY_STATUS= "email_verify_status";
    public final String  APPROVE_STATUS= "approve_status";
    public final String  VERIFY_IDENTITY= "verify_identity";
    public final String  REFERRAL_AMOUNT= "referral_amount";
    public final String  PAY_PAL_ID= "paypal_id";



    /////////////  COMPANY PROFILE  ////////////////

    public final String  COMPANY_ID= "company_id";

}
