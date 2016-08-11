package com.b2infosoft.giftcardup.credential;

import android.content.Context;
import android.content.SharedPreferences;
import com.b2infosoft.giftcardup.app.Tags;

/**
 * Created by rajesh on 7/12/2016.
 */

public class Active {
    private Context context;

    private Active(Context context) {
        this.context = context;
    }

    public static Active getInstance(Context context) {
        return new Active(context);
    }
    private final String USER_CREDENTIALS = "dmr_rajesh";
    private final int PRIVATE_KEY = 0;
    private final String DEVICE_LOGIN = "device_login";
    private final boolean STATUS[] = {false, true};

    public void setKey(String key, String value) {
        SharedPreferences setting = context.getSharedPreferences(USER_CREDENTIALS, PRIVATE_KEY);
        setting.edit()
                .putString(key, value)
                .commit();
    }

    public String getValue(String key) {
        SharedPreferences setting = context.getSharedPreferences(USER_CREDENTIALS, 0);
        return setting.getString(key, "");
    }

    public void setLogin() {
        SharedPreferences setting = context.getSharedPreferences(USER_CREDENTIALS, PRIVATE_KEY);
        setting.edit()
                .putBoolean(DEVICE_LOGIN, STATUS[1])
                .commit();
    }

    public void setLogOut() {
        Tags tags = Tags.getInstance();
        SharedPreferences setting = context.getSharedPreferences(USER_CREDENTIALS, PRIVATE_KEY);
        setting.edit()
                .putBoolean(DEVICE_LOGIN, STATUS[0])
                .putString(tags.USER_ID, "")
                .putString(tags.USER_TYPE, "")
                .commit();
    }

    public boolean isLogin() {
        SharedPreferences setting = context.getSharedPreferences(USER_CREDENTIALS, 0);
        return setting.getBoolean(DEVICE_LOGIN, STATUS[0]);
    }
}
