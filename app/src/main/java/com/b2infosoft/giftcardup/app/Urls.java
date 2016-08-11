package com.b2infosoft.giftcardup.app;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by rajesh on 7/24/2016.
 */

public class Urls {
    private final String TAG = Urls.class.getName();
    private static Urls ourInstance = new Urls();
    private Config config = Config.getInstance();

    public static Urls getInstance() {
        return ourInstance;
    }

    private Urls() {

    }

    public String getStringRequest() {
        return config.getServerAddress().concat("string_request.php");
    }

    public String getPath(String path) {
        return config.SERVER_PATH.concat(path);
    }

    public String getUrl(String path, HashMap<String, String> map) {
        if (!map.isEmpty()) {
            StringBuilder builder = new StringBuilder(path);
            builder.append("?");
            Set<String> keySet = map.keySet();
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = map.get(key);
                builder.append(key);
                builder.append("=");
                try {
                    value = URLEncoder.encode(value, "UTF-8");
                } catch (UnsupportedEncodingException e) {

                }
                builder.append(value);
                builder.append("&");
            }
            String newPath = builder.toString();
            String url = newPath.substring(0, newPath.length() - 1);
            return url;
        } else {
            return path;
        }
    }

    public String getUrlLogin(){
        return config.getServerAddress().concat("UserInfo.php");
    }
}
