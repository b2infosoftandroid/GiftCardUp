package com.b2infosoft.giftcardup.app;

/**
 * Created by rajesh on 7/24/2016.
 */

public class Config {
    private final String SITE_URL = "www.giftcardup.com";
    private final String SERVER_IP = "192.168.0.52";
    public final String GIFT_CARD_IMAGE_FOLDER ="images/upload/";
    public final String USER_PROFILE_IMAGE_FOLDER ="img/avatar/";
    private final String REFERRAL_ADDRESS = "http://giftcardup.com/signup.php?ref=";
    private final Boolean isLive = true;
    private static Config ourInstance = new Config();
    public static Config getInstance() {
        return ourInstance;
    }

    private Config() {

    }
    public String getGiftCardImageAddress(){
        if(isLive){
            return "http://".concat(SITE_URL).concat("/").concat(GIFT_CARD_IMAGE_FOLDER);
        }else{
            return "http://".concat(SERVER_IP).concat("/giftcard2/").concat(GIFT_CARD_IMAGE_FOLDER);
        }
    }
    public String getUserProfileImageAddress(){
        if(isLive){
            return "http://".concat(SITE_URL).concat("/").concat(USER_PROFILE_IMAGE_FOLDER);
        }else{
            return "http://".concat(SERVER_IP).concat("/giftcard2/").concat(USER_PROFILE_IMAGE_FOLDER);
        }
    }
    public String getServerAddress(){
        if(isLive){
            return "http://".concat(SITE_URL).concat("/giftcard_services/");
        }else{
            //return "http://".concat(SERVER_IP).concat("/giftcard2/giftcard_services/");
            //return "http://".concat(SERVER_IP).concat("/rajesh/giftcard_services/");
            return "http://".concat(SERVER_IP).concat("/giftcard2/giftcard_services/");
        }
    }
    public String getReferralAddress(){
        return REFERRAL_ADDRESS;
    }
}



