package com.b2infosoft.giftcardup.volly;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import org.json.JSONObject;
import java.util.Map;

/**
 * Created by rajesh on 8/10/2016.
 */

public class DMRRequest {
    private Context context;
    private String tag;
    private DMRRequest(Context context, String tag) {
        this.context = context;
        this.tag = tag;
    }
    public static final DMRRequest getInstance(Context context, String tag) {
        return new DMRRequest(context, tag);
    }
    public void doPost(String url,Map<String, String> stringMap, final DMRResult dmrResult) {
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, url, stringMap, new DMRSuccess() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dmrResult.onError(error);
            }
            @Override
            public void onResponse(Object response) {
                dmrResult.onSuccess((JSONObject) response);
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(customRequest);
    }
    public void cancelAll(){
        MySingleton.getInstance(context).getRequestQueue().cancelAll(tag);
    }
}
