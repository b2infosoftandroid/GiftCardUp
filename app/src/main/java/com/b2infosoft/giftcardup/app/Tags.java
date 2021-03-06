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

    /////////////   PAYMENTS METHODS   /////////
    public final String PAYMENT_WITH_PAY_PAL = "PayPal";
    public final String PAYMENT_WITH_AVAILABLE_FUND = "Available Fund";
    public final String PAYMENT_WITH_CARD = "Card";

    public final String CURRENCY = "usd";

    /////////////   GLOBAL TAGS   ////////////
    public final String SUCCESS = "success";
    public final String MESSAGE = "message";
    public final String IS_MORE = "is_more";
    public final String LOAD_MORE = "load_more";
    public final String SORT_BY = "sort_by";
    public final String CATEGORIES_ID = "categories_id";
    public final String SELECTED_TAB = "selected_tab";
    public final String REFERRAL_CODE = "referral_code";
    public final String SELECTED_FRAGMENTS = "selected_fragments";


    /////////////////  FRAGMENTS NAME  //////////
    public final String FRAGMENT_MY_ORDERS = "fragment_my_order";

    public final int SUSPEND = 2;
    public final int PASS = 1;
    public final int FAIL = 0;
    public final int EXISTING_USER = 3;
    public final int NEW_USER = 4;
    public final int PASSWORD_NOT_UPDATE = 5;
    public final int DEFAULT_LOADING_DATA = 6;
    public final int INVALID_EMAIL = 7;
    public final String ONE_TIME_PASSWORD   =  "one_time_password";
    public final String OTP_ID   =  "otp_id";

    //////////////  USER ACTIONS    //////////////
    public final String USER_ACTION = "user_action";
    public final String COMPANY_ALL_BRAND = "company_all_brand";
    public final String COMPANY_ID_BRAND = "company_id_brand";
    public final String COMPANY_CATEGORY_ALL = "company_category_all";
    public final String GET_OFFER = "get_offer";
    public final String USER_REQUEST = "user_request";
    public final String STATES_ALL = "states_all";
    public final String COMPANY_BRANDS = "company_brands";
    public final String USER_CONTACT_INFORMATION = "user_contact_information";
    public final String USER_PROFILE_UPDATE = "user_profile_update";
    public final String BANK_ACCOUNT_ADD = "bank_account_add";
    public final String BANK_ACCOUNT_INFO = "bank_account_info";
    public final String BANK_ACCOUNT_UPDATE = "bank_account_update";
    public final String CHECK_APPROVE_FOR_SELLING = "check_approve_for_selling";
    public final String ADD_IDENTIFICATION_INFO = "add_identification_info";
    public final String USER_ALL_APPROVE_INFO = "user_all_approve_info";
    public final String ADD_IDENTIFICATION = "add_identification";
    public final String ADD_IDENTIFICATION_SSN = "add_identification_ssn";
    public final String ADD_GIFT_CARD = "add_gift_card";
    public final String GET_EARNING_PRICE = "get_earning_price";
    public final String GET_GIFT_CARD_BY_USER_ID = "get_gift_card_by_user_id";
    public final String DELETE_GIFT_CARD = "delete_gift_card";
    public final String EDIT_GIFT_CARD = "edit_gift_card";
    public final String ADD_SPEEDY_GIFT_CARD = "add_speedy_gift_card";
    public final String GET_PENDING_SHIPMENT_CARDS = "get_pending_shipment_cards";
    public final String ADD_COMPANY_BRANDS = "add_company_brands";
    public final String WITHDRAWAL_HISTORY = "withdrawal_history";
    public final String WITHDRAWAL_HISTORY_VIEW = "withdrawal_history_view";
    public final String AVAILABLE_FUND = "available_fund";
    public final String AVAILABLE_FUND_BALANCE = "available_fund_balance";
    public final String WITHDRAWAL_REQUEST = "withdrawal_request";
    public final String SEND_WITHDRAWAL_REQUEST = "send_withdrawal_request";
    public final String ADD_CART_ITEM_GIFT_CARD = "add_card_item_gift_card";
    public final String REMOVE_CART_ITEM_GIFT_CARD = "remove_card_item_gift_card";
    public final String CHECK_CART_ITEMS = "check_cart_items";
    public final String UPDATE_PROFILE_PIC = "update_profile_pic";
    public final String CHECK_CART_STATUS = "check_cart_status";
    public final String UPDATE_ADDRESS = "update_address";
    public final String MY_CONTROL_PANEL = "my_control_panel";
    public final String PROMO_CODE_APPLY = "promo_code_apply";
    public final String FB_LOGIN_USER = "fb_login_user";
    public final String FB_PROFILE_UPDATE = "fb_profile_update";
    public final String PAY_WITH_AVAILABLE_FUND = "pay_with_available_fund";
    public final String PAY_WITH_CARD = "pay_with_card";
    public final String MY_ORDERS_ALL = "my_orders_all";
    public final String CARD_DISPUTE_REVIEW = "card_dispute_review";
    public final String GET_NOTIFICATIONS = "get_notifications";
    public final String GET_NOTIFICATIONS_UN_READ = "get_notifications_un_read";
    public final String SET_NOTIFICATIONS_READ = "set_notifications_read";
    public final String RESEND_VERIFICATION_EMAIL = "resend_verification_mail";
    public final String FORGOT_PASSWORD_REQUEST = "forgot_password_request";
    public final String CHANGE_PASSWORD = "chagne_password";


    /*  CART PROCESSING  */
    public final String LEFT_TIME = "left_time";
    public final String STATUS = "status";


    /* USER LOGIN SIGNUP */
    public final String USER_LOGIN = "login";
    public final String USER_SIGNUP = "sign_up";
    public final String USER_UPDATE = "update";
    public final String USER_PROFILE = "profile";

    /////////// ARRAYS  //////////
    public final String GIFT_CARDS = "gift_cards";
    public final String CATEGORIES = "categories";
    public final String STATE_ARR = "states_all";
    public final String ARR_BANK_DETAILS = "bank_details";

    //////////////  ARRAYS   /////////////////////
    public final String ARR_CARD_TYPE[] = {"Physical", "eCard"};

    /////////////  RESPONSE ARRAY  ////////////////
    public final String USER_INFO = "user_info";

    ///////////////// WITH OUT TABLE  /////////////////
    public final String TOTAL_CARD = "total_card";
    public final String TOTAL_PERCENTAGE = "total_percentage";
    public final String SAVE_UP_TO = "save_up_to";

    /////////////// COMPANY BRAND ///////////////////////
    public final String COMPANY_BRAND = "company_brand";
    public final String COMPANY_BRAND_COMPANY_ID = "company_id";
    public final String COMPANY_BRAND_CAT_ID = "cat_id";
    public final String COMPANY_BRAND_COMPANY_NAME = "comapany_name";
    public final String COMPANY_BRAND_COMPANY_PERCENTAGE = "comapany_percentage";
    public final String COMPANY_BRAND_SELLING_COMMISSION = "selling_commission";
    public final String COMPANY_BRAND_DESCRIPTION = "description";
    public final String COMPANY_BRAND_IMAGE = "image";
    public final String COMPANY_BRAND_WEBSITE_LINK = "website_link";
    public final String COMPANY_BRAND_PHONE_NUMBER = "phone_number";
    public final String COMPANY_BRAND_CARD_TYPE = "card_type";
    public final String COMPANY_BRAND_COMMISSION_TYPE = "commission_type";
    public final String COMPANY_BRAND_ALLOW_CARD = "allow_card";
    public final String COMPANY_BRAND_DATE = "date";
    public final String COMPANY_BRAND_USER_ID = "user_id";
    public final String COMPANY_BRAND_STATUS = "status";

    /////////////// GIFT CARD ///////////////////////
    public final String GIFT_CARD_GIFT_CARD_ID = "gift_card_id";
    public final String GIFT_CARD_READ_STATUS = "read_status";
    public final String GIFT_CARD_USER_ID = "user_id";
    public final String GIFT_CARD_BUYER_ID = "buyer_id";
    public final String GIFT_CARD_COMPANY_ID = "company_id";
    public final String GIFT_CARD_PARENT_GIFT_CARD_ID = "parent_gift_card_id";
    public final String GIFT_CARD_CARD_NAME = "card_name";
    public final String GIFT_CARD_SERIAL_NUMBER = "serial_number";
    public final String GIFT_CARD_CARD_PIN = "card_pin";
    public final String GIFT_CARD_CARD_PRICE = "card_price";
    public final String GIFT_CARD_CARD_VALUE = "card_value";
    public final String GIFT_CARD_VALUE = "value";
    public final String GIFT_CARD_SELL = "sell";
    public final String GIFT_CARD_SELLING_PERCENTAGE = "selling_percentage";
    public final String GIFT_CARD_YOUR_EARNING = "your_earning";
    public final String GIFT_CARD_SHIPPING_AND_COMMISSION_CHARGE = "shipping_and_commission_charge";
    public final String GIFT_CARD_PERCENTAGE_OFF = "percentage_off";
    public final String GIFT_CARD_SOLD_ON = "sold_on";
    public final String GIFT_CARD_CARD_DESCRIPTION = "card_description";
    public final String GIFT_CARD_CARD_IMG = "card_img";
    public final String GIFT_CARD_CARD_OWNER = "card_owner";
    public final String GIFT_CARD_CARD_DISCOUNT = "card_discount";
    public final String GIFT_CARD_APPROVE_DATE = "approve_date";
    public final String GIFT_CARD_APPROVE_STATUS = "approve_status";
    public final String GIFT_CARD_STATUS_TYPE = "status_type";
    public final String GIFT_CARD_SOLD_STATUS = "sold_status";
    public final String GIFT_CARD_BARCODE_IMG = "barcode_img";
    public final String GIFT_CARD_DENY_REASON = "deny_reason";
    public final String GIFT_CARD_NEED_REVIEW = "need_review";
    public final String GIFT_CARD_DISPUTE_RESULT = "dispute_result";
    public final String GIFT_CARD_COMPANY_IMAGE = "card_company_image";


    /////////////// USER PROFILE  ///////////////

    public final String USER = "user";
    public final String USER_TYPE = "user_type";
    public final String FIRST_NAME = "first_name";
    public final String LAST_NAME = "last_name";
    public final String PHONE_NUMBER = "phone_number";
    public final String GENDER = "gender";
    public final String ADDRESS = "address";
    public final String SUITE_NUMBER = "suite_number";
    public final String CITY = "city";
    public final String ZIP_CODE = "zip_code";
    public final String STATE = "state";
    public final String COMPANY_NAME = "company_name";
    public final String EMAIL = "email";
    public final String PASSWORD = "password";
    public final String IMAGE = "image";
    public final String IDENTIFY_IMAGE = "identity_image";
    public final String GROUP_ID = "group_id";
    public final String JOIN_DATE = "join_date";
    public final String EMAIL_VERIFY_STATUS = "email_verify_status";
    public final String APPROVE_STATUS = "approve_status";
    public final String VERIFY_IDENTITY = "verify_identity";
    public final String REFERRAL_AMOUNT = "referral_amount";
    public final String PAY_PAL_ID = "paypal_id";
    public final String CARD_TYPE = "card_type";
    public final String TOTAL_SAVE = "total_save";
    public final String TOTAL_SOLD = "total_sold";
    public final String CARD_ID = "card_id";
    public final String BALANCE = "balance";


    /////////////  ID  ////////////////
    public final String USER_ID = "user_id";
    public final String EMPLOYEE_ID = "employee_id";
    public final String COMPANY_ID = "company_id";
    public final String FB_ID = "fb_id";

    ////////////  COMPANY CATEGORY /////////
    public final String COMPANY_CATEGORY_ID = "cat_id";
    public final String COMPANY_CATEGORY_NAME = "category_name";

    ////////////  SELL GIFT CARD //////////////////
    public final String SELL_GIFT_CARD_NAME = "sell_gift_card_name";
    public final String SELL_GIFT_CARD_TYPE = "sell_gift_card_type";
    public final String SELL_GIFT_CARD_OFFER = "sell_gift_card_offer";
    public final String SELL_GIFT_CARD_BALANCE = "sell_gift_card_balance";
    public final String SELL_GIFT_CARD_OWNER_BALANCE = "sell_gift_card_owner_balance";
    public final String SELL_GIFT_CARD_IMAGE = "sell_gift_card_image";
    public final String SELL_GIFT_CARD_COMPANY_ID = "sell_gift_card_company_id";
    public final String SELL_GIFT_CARD_COMPANY_NAME = "sell_gift_card_company_name";
    public final String SELL_GIFT_CARD_COMPANY_PERCENTAGE = "sell_gift_card_company_percentage";

    ////////////////  STATE ALL ////////////////////
    public final String STATE_ID = "state_id";
    public final String STATE_NAME = "state";
    public final String STATE_ABBRE = "abbreviation";

    ///////////////// CONTACT INFO TAGS  /////////////

    public final String CONTACT_INFO_CONTACT_ID = "contact_id";
    public final String CONTACT_INFO_USER_ID = "user_id";
    public final String CONTACT_INFO_PHONE_NUMBER = "phone_number";
    public final String CONTACT_INFO_ADDRESS = "adderss";
    public final String CONTACT_INFO_SUITE_NUMBER = "suite_number";
    public final String CONTACT_INFO_CITY = "city";
    public final String CONTACT_INFO_ZIP_CODE = "zip_code";
    public final String CONTACT_INFO_STATE = "state";
    public final String CONTACT_INFO_COMPANY_NAME = "company_name";
    public final String CONTACT_INFO_DATE = "date";

    ////////////////  ADD BANK ACCOUNT   ///////////////
    public final String BANK_VOID_IMAGE = "filedata3";
    public final String BANK_NAME = "add_bank_name";
    public final String BANK_ROUTING_NUMBER = "add_routing_number";
    public final String BANK_ACCOUNT_NUMBER = "add_account_number";

    ///////////////  ADD IDENTIFICATION INFORMATION  /////////
    public final String SSN_EIN = "ssn_ein";
    public final String ID_TYPE = "id_type";

    /////////////////  BANK INFO TAGS  //////////////////
    public final String BANK_INFO = "bank_info";
    public final String BANK_INFO_ID = "bank_detail_id";
    public final String BANK_INFO_USER_ID = "user_id";
    public final String BANK_INFO_ID_NUMBER = "id_number";
    public final String BANK_INFO_VOID_CHECK_IMAGE_1 = "void_check_image_1";
    public final String BANK_INFO_VOID_CHECK_IMAGE_2 = "void_check_image_2";
    public final String BANK_INFO_NAME = "bank_name";
    public final String BANK_INFO_ROUTING_NUMBER = "routing_number";
    public final String BANK_INFO_CITY = "bank_city";
    public final String BANK_INFO_COUNTRY = "country";
    public final String BANK_INFO_ACCOUNT_TYPE = "account_type";
    public final String BANK_INFO_ACCOUNT_NAME = "account_name";
    public final String BANK_INFO_ACCOUNT_NUMBER = "account_number";
    public final String BANK_INFO_ADDRESS = "address_line";
    public final String BANK_INFO_DATE = "date";
    public final String BANK_INFO_STATUS = "status";


    ///////////////  APPROVE INFORMATION  //////////
    public final String USER_IDENTIFICATION = "user_identification";
    public final String USER_BANK = "user_bank";
    public final String USER_SSN = "user_ssn";
    public final String KEYWORD_TYPE = "keyword_type";
    public final String KEYWORD_SEARCH = "keyword_search";


    public final String EARNING_PRICE = "earning_price";
    public final String SHIPPING_COMMISSION_CHARGE = "shipping_commission_charge";


    //////////////////////     WITHDRAWAL HISTORY    /////////////////////
    public final String WITHDRAWAL_REQUEST_ID = "withdrawal_request_id";
    public final String WITHDRAWAL_PAYMENT_ID = "payment_ids";
    public final String WITHDRAWAL_PAYMENT_METHOD = "payment_method";
    public final String WITHDRAWAL_PAYMENT_STATUS = "payment_status";
    public final String WITHDRAWAL_AMOUNT = "amount";
    public final String WITHDRAWAL_BANK_ID = "bank_id";
    public final String WITHDRAWAL_USER_PAYMENT_ID = "user_payment_id";
    public final String WITHDRAWAL_CREDIT_AMOUNT = "credit_amount";
    public final String WITHDRAWAL_DEBIT_AMOUNT = "debit_amount";
    public final String WITHDRAWAL_COMMISSION_CHARGE = "commission_charge";
    public final String WITHDRAWAL_SUMMARY = "summary";
    public final String WITHDRAWAL_REQUEST_STATUS = "withdrawal_request_status";
    public final String WITHDRAWAL_REQUEST_DATE = "request_date";

    //////////////////     PROFILE PIC UPDATE  ////////////////////
    public final String PROFILE_NAME = "filedata8";
    public final String PROFILE_OLD_NAME = "old_file_name";

    /*                  MAIL PRICE   */
    public final String FIRST_CLASS_PRICE = "first_class_price";
    public final String PRIORITY_PRICE = "priority_price";
    public final String EXPRESS_PRICE = "express_price";

    /*                  PROMO CODE      */
    public final String PROMO_CODE = "promo_code";
    public final String TOTAL_AMOUNT = "total_amount";
    public final String AMOUNT_TYPE = "amount_type";
    public final String TOTAL = "total";

    /////////////     PUSH NOTIFICATION    /////////////////
    public final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public final String REGISTRATION_COMPLETE = "registrationComplete";

    ///////////////   CONTROL PANEL    /////////////
    public final String ID = "id";
    public final String GENERAL_COMMISSION = "general_commission";
    public final String SHIPPING_CHARGE = "shipping_charge";
    public final String PROCESS_TIME = "process_time";
    public final String CARD_ATTEMPT_TIME = "card_attempt_time";
    public final String MINIMUM_SCORE = "minimum_score";
    public final String MAXIMUM_SCORE = "maximum_score";

    /*          PAYMENTS        */
    public final String ORDER_SUMMERY = "order_summery";
    public final String ITEM_DATA = "item_data";
    public final String ITEM_ID = "item_id";
    public final String TOTAL_PRICE = "total_price";
    public final String TOTAL_ITEM = "total_item";
    public final String COMMISSION = "commission";

    /*         MY ORDER             */
    public final String GIFT_ID = "gift_id";
    public final String ORDER_ID = "order_id";
    public final String REVIEW = "review";
    public final String MAIL_TYPE = "mail_type";
    public final String MAIL_CLASS_PRICE = "mail_class_price";
    public final String ORDER_DATE = "order_date";
    public final String TRANSACTION_ID = "transaction_id";
    public final String MAIN_ORDER_ID = "main_order_id";

    ///////////////   CARD ///////////////////
    public final String CARD_NUMBER = "number";
    public final String CARD_EXP_MONTH = "exp_month";
    public final String CARD_EXP_YEAR = "exp_year";
    public final String CARD_CVC_CHECK = "cvc_check";
    public final String CARD_CURRENCY = "currency";

    /////////////  NOTIFICATION  ///////////////
    public final String SENDER_NAME = "sender_name";
    public final String SENDER_ID = "sender_id";
    public final String RECEIVER_ID = "receiver_id";
    public final String DATE = "date";
    public final String TIME_AGO = "time_ago";

    /////////////  MERCHANT  ////////
    public final String MERCHANT = "merchant";


}
