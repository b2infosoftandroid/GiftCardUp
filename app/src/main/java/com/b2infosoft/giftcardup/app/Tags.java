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
    public final String SUCCESS = "success";
    public final String MESSAGE = "message";
    public final String IS_MORE = "is_more";
    public final String LOAD_MORE = "load_more";
    public final String SORT_BY = "sort_by";
    public final String CATEGORIES_ID = "categories_id";

    public final int SUSPEND = 2;
    public final int PASS = 1;
    public final int FAIL = 0;
    public final int EXISTING_USER = 3;
    public final int DEFAULT_LOADING_DATA = 10;

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


    /* USER LOGIN SIGNUP */
    public final String USER_LOGIN = "login";
    public final String USER_SIGNUP = "sign_up";
    public final String USER_UPDATE = "update";
    public final String USER_PROFILE = "profile";

    /////////// ARRAYS  //////////
    public final String GIFT_CARDS = "gift_cards";
    public final String CATEGORIES = "categories";
    public final String STATE_ARR = "states_all";

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
    public final String GIFT_CARD_COMPANY_ID = "company_id";
    public final String GIFT_CARD_PARENT_GIFT_CARD_ID = "parent_gift_card_id";
    public final String GIFT_CARD_CARD_NAME = "card_name";
    public final String GIFT_CARD_SERIAL_NUMBER = "serial_number";
    public final String GIFT_CARD_CARD_PIN = "card_pin";
    public final String GIFT_CARD_CARD_PRICE = "card_price";
    public final String GIFT_CARD_VALUE = "value";
    public final String GIFT_CARD_SELL = "sell";
    public final String GIFT_CARD_SELLING_PERCENTAGE = "selling_percentage";
    public final String GIFT_CARD_YOUR_EARNING = "your_earning";
    public final String GIFT_CARD_SHIPPING_AND_COMMISSION_CHARGE = "shipping_and_commission_charge";
    public final String GIFT_CARD_PERCENTAGE_OFF = "percentage_off";
    public final String GIFT_CARD_SOLD_ON = "sold_on";
    public final String GIFT_CARD_CARD_DESCRIPTION = "card_description";
    public final String GIFT_CARD_CARD_IMG = "card_img";
    public final String GIFT_CARD_APPROVE_DATE = "approve_date";
    public final String GIFT_CARD_APPROVE_STATUS = "approve_status";
    public final String GIFT_CARD_STATUS_TYPE = "status_type";
    public final String GIFT_CARD_SOLD_STATUS = "sold_status";
    public final String GIFT_CARD_BARCODE_IMG = "barcode_img";
    public final String GIFT_CARD_DENY_REASON = "deny_reason";
    public final String GIFT_CARD_NEED_REVIEW = "need_review";


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


    /////////////////  BANK INFO TAGS  //////////////////
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

}
