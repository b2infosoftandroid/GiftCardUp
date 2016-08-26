package com.b2infosoft.giftcardup.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AnimationUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.database.DBHelper;
import com.b2infosoft.giftcardup.model.ContactInformation;
import com.b2infosoft.giftcardup.model.State;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
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
    private void loadDefaultData(){
        Map<String,String> map = new HashMap<>();
        map.put(tags.USER_ACTION,tags.STATES_ALL);
        dmrRequest.doPost(urls.getAppAction(),map,this);
    }
    @Override
    public void onSuccess(JSONObject jsonObject) {
        try {
            if (jsonObject.has(tags.SUCCESS)) {
                if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                    if (jsonObject.has(tags.STATES_ALL)) {
                        dbHelper.deleteState();
                        JSONArray jsonArray = jsonObject.getJSONArray(tags.STATES_ALL);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            dbHelper.setState(State.fromJSON(jsonArray.getJSONObject(i)));
                        }
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
