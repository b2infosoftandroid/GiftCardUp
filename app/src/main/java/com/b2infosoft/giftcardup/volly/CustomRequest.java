package com.b2infosoft.giftcardup.volly;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rajesh on 8/11/2016.
 */
public class CustomRequest extends Request<JSONObject> {
    private Response.Listener<JSONObject> responseListener;
    private Map<String, String> params;
    private DMRSuccess dmrSuccess;

    public CustomRequest(String url, Map<String, String> params,
                         Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.responseListener = reponseListener;
        this.params = params;
    }

    public CustomRequest(int method, String url, Map<String, String> params,
                         Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.responseListener = reponseListener;
        this.params = params;
    }

    public CustomRequest(int method, String url, Map<String, String> params,
                         DMRSuccess dmrSuccess) {
        super(method, url, dmrSuccess);
        this.dmrSuccess = dmrSuccess;
        this.params = params;
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return super.getHeaders();
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
        RetryPolicy policy = new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 5000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 5000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        };
        return super.setRetryPolicy(policy);
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        if (dmrSuccess != null)
            dmrSuccess.onResponse(response);
        if (responseListener != null)
            responseListener.onResponse(response);
    }
}
