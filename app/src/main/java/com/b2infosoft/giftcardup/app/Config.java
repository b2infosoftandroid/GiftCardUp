package com.b2infosoft.giftcardup.app;

/**
 * Created by rajesh on 7/24/2016.
 */
public class Config {
    private final String SERVER_IP = "192.168.0.55";
    public final String SERVER_PATH ="http://app.paathshala.net.in/App/";
    private final Boolean LIVE_SERVER = false;
    private static Config ourInstance = new Config();

    public static Config getInstance() {
        return ourInstance;
    }
    private Config() {

    }
    public String getServerAddress(){
        if(LIVE_SERVER){
            return "";
        }else{
            return "http://".concat(SERVER_IP).concat("/rajesh/giftcard_services/");
        }
    }

}
