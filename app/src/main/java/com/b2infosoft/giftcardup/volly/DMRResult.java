package com.b2infosoft.giftcardup.volly;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by rajesh on 8/10/2016.
 */
public interface DMRResult {
    void onSuccess(JSONObject jsonObject);
    void onError(VolleyError volleyError);
}
