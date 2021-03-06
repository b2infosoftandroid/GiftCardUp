package com.b2infosoft.giftcardup.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.activity.ShoppingCart;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CartStatus extends Service {
    public final static String TAG = CartStatus.class.getName();
    private Urls urls;
    private Tags tags;
    private Active active;
    DMRRequest dmrRequest;
    private Handler handler = new Handler();

    public CartStatus() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        urls = Urls.getInstance();
        tags = Tags.getInstance();
        active = Active.getInstance(this);
        dmrRequest = DMRRequest.getInstance(this, TAG);
        Log.d(TAG, "onStart");
        handler.postDelayed(runnable, 0);
    }

    int i = 0;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (active.getUser() == null) {
                CartStatus.this.stopSelf();
                return;
            }
            Map<String, String> map = new HashMap<>();
            map.put(tags.USER_ACTION, tags.CHECK_CART_STATUS);
            map.put(tags.USER_ID, active.getUser().getUserId() + "");
            dmrRequest.doPost(urls.getCartInfo(), map, new DMRResult() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    Log.d("CART_INFO_BACKGROUND", jsonObject.toString());
                    try {
                        Intent intent1 = new Intent(ShoppingCart.TAG);
                        if (jsonObject.has(tags.SUCCESS)) {
                            if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                                if (jsonObject.has(tags.LEFT_TIME)) {
                                    if (jsonObject.getString(tags.LEFT_TIME) != null) {
                                        intent1.putExtra(tags.LEFT_TIME, jsonObject.getString(tags.LEFT_TIME));
                                        CartStatus.this.sendBroadcast(intent1);
                                    }
                                } else {
                                    CartStatus.this.sendBroadcast(intent1);
                                    CartStatus.this.stopSelf();
                                }
                                if (jsonObject.has(tags.STATUS)) {
                                    if (jsonObject.getInt(tags.STATUS) == tags.FAIL) {
                                        MyNotification notification = new MyNotification(CartStatus.this);
                                        String message = "Please check your shopping cart otherwise next to 2 minutes cart will be empty.";
                                        notification.setNotificationCartLeftTime(message);
                                    }
                                }
                            } else if (jsonObject.getInt(tags.SUCCESS) == tags.FAIL) {
                                CartStatus.this.sendBroadcast(intent1);
                                CartStatus.this.stopSelf();
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
                    if (volleyError.getMessage() != null)
                        Log.e(TAG, volleyError.getMessage());
                }
            });
            if (handler != null) {
                handler.postDelayed(runnable, 1000);
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler = null;
        Log.d(TAG, "onDestroyed");
    }
}
