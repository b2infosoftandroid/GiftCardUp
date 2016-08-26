package com.b2infosoft.giftcardup.credential;

import android.content.Context;
import android.content.SharedPreferences;

import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.model.User;
import com.b2infosoft.giftcardup.utils.ObjectSerializer;
import com.google.gson.Gson;

/**
 * Created by rajesh on 7/12/2016.
 */

public class Active {
    private Context context;
    private Gson gson;
    private SharedPreferences setting;

    private Active(Context context) {
        this.context = context;
        this.gson = new Gson();
        this.setting = context.getSharedPreferences(USER_CREDENTIALS, PRIVATE_KEY);
    }

    public static Active getInstance(Context context) {
        return new Active(context);
    }

    private final String USER_CREDENTIALS = "dmr_rajesh";
    private final int PRIVATE_KEY = 0;
    private final String DEVICE_LOGIN = "device_login";
    private final String DEVICE_LOGIN_USER_DETAILS = "device_login_user_details";
    private final boolean STATUS[] = {false, true};

    public void setKey(String key, String value) {

        setting.edit()
                .putString(key, value)
                .commit();
    }

    public String getValue(String key) {
        return setting.getString(key, "");
    }

    public void setLogin() {
        setting.edit()
                .putBoolean(DEVICE_LOGIN, STATUS[1])
                .commit();
    }

    public void setLogOut() {
        setting.edit()
                .putBoolean(DEVICE_LOGIN, STATUS[0])
                .putString(DEVICE_LOGIN_USER_DETAILS, null)
                .commit();
    }

    public void setUser(User user) {
        setting.edit()
                .putString(DEVICE_LOGIN_USER_DETAILS, gson.toJson(user))
                .commit();
    }

    public User getUser() {
        String user = setting.getString(DEVICE_LOGIN_USER_DETAILS, null);
        if (user != null) {
            return gson.fromJson(user, User.class);
        } else {
            return null;
        }
    }

    public boolean isLogin() {
        return setting.getBoolean(DEVICE_LOGIN, STATUS[0]);
    }
}
