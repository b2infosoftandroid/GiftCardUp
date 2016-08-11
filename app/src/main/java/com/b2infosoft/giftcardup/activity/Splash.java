package com.b2infosoft.giftcardup.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AnimationUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.b2infosoft.giftcardup.R;

import org.json.JSONObject;

public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        findViewById(R.id.imageView2).startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(4000);
                    startActivity(new Intent(Splash.this,Login.class));
                }catch (InterruptedException e){

                }
            }
        }).start();
    }

}
