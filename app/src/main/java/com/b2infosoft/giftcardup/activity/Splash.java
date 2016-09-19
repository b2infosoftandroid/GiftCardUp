package com.b2infosoft.giftcardup.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AnimationUtils;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.database.DBHelper;
import com.b2infosoft.giftcardup.model.CompanyCategory;
import com.b2infosoft.giftcardup.model.MailPrice;
import com.b2infosoft.giftcardup.model.State;
import com.b2infosoft.giftcardup.model.User;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Splash extends AppCompatActivity implements DMRResult {
    private final static String TAG = Splash.class.getName();
    private Active active;
    private Tags tags;
    private Urls urls;
    private DMRRequest dmrRequest;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
        loadDefaultData();
        findViewById(R.id.imageView2).startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                    startActivity(new Intent(Splash.this, Main.class));
                    Splash.this.finish();
                } catch (InterruptedException e) {

                }
            }
        }).start();
    }

    private void init() {
        active = Active.getInstance(this);
        tags = Tags.getInstance();
        urls = Urls.getInstance();
        dmrRequest = DMRRequest.getInstance(this, TAG);
        dbHelper = new DBHelper(this);
    }

    private void loadDefaultData() {

        /*LOADING ALL CATEGORIES*/
        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.COMPANY_CATEGORY_ALL);
        dmrRequest.doPost(urls.getAppAction(), map, this);

        /* LOADING ALL STATES */
        Map<String, String> map2 = new HashMap<>();
        map2.put(tags.USER_ACTION, tags.STATES_ALL);
        dmrRequest.doPost(urls.getAppAction(), map2, this);

        /* LOADING ALL STATES */
        Map<String, String> map3 = new HashMap<>();
        map3.put(tags.USER_ACTION, tags.MY_CONTROL_PANEL);
        dmrRequest.doPost(urls.getAppAction(), map3, this);

        /*LOADING USER PROFILE*/
        if (active.isLogin()) {
            Map<String, String> map1 = new HashMap<>();
            map1.put(tags.USER_ACTION, tags.USER_INFO);
            map1.put(tags.USER_ID, active.getUser().getUserId() + "");
            dmrRequest.doPost(urls.getUserInfo(), map1, this);
        }

    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        Log.d("SPLASH DATA", jsonObject.toString());
        try {
            if (jsonObject.has(tags.SUCCESS)) {
                if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                    if (jsonObject.has(tags.STATES_ALL)) {
                        dbHelper.deleteState();
                        JSONArray jsonArray = jsonObject.getJSONArray(tags.STATES_ALL);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            dbHelper.setState(State.fromJSON(jsonArray.getJSONObject(i)));
                        }
                    } else if (jsonObject.has(tags.CATEGORIES)) {
                        dbHelper.deleteCategories();
                        List<CompanyCategory> categoryList = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray(tags.CATEGORIES);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            dbHelper.setCategory(CompanyCategory.fromJSON(jsonArray.getJSONObject(i)));
                        }
                    } else if (jsonObject.has(tags.USER_INFO)) {
                        JSONObject object = jsonObject.getJSONObject(tags.USER_INFO);
                        User user = User.fromJSON(object);
                        active.setUser(user);
                    } else if (jsonObject.has(tags.MY_CONTROL_PANEL)) {
                        JSONObject object = jsonObject.getJSONObject(tags.MY_CONTROL_PANEL);
                        dbHelper.deleteMailPrice();
                        dbHelper.setMailPrice(MailPrice.fromJSON(object));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

    }

    @Override
    public void onError(VolleyError volleyError) {
        volleyError.printStackTrace();
        Log.e(TAG, volleyError.getMessage());
    }
}
