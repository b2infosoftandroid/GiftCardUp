package com.b2infosoft.giftcardup.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Alert;
import com.b2infosoft.giftcardup.app.Format;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.app.Validation;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.custom.Progress;
import com.b2infosoft.giftcardup.services.ConnectivityReceiver;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendBrands extends Fragment implements View.OnClickListener,DMRResult {
    private final static String TAG = RecommendBrands.class.getName();
    private Alert alert;
    private Urls urls;
    private Tags tags;
    private Active active;
    private Format format;
    private Validation validation;
    DMRRequest dmrRequest;
    private Progress progress;

    /* UI Controls View */
    private View mView;
    private EditText company_name, website_link, phone_number;
    private Button brand_submit;

    public RecommendBrands() {
        // Required empty public constructor
    }

    private void init() {
        dmrRequest = DMRRequest.getInstance(getActivity(), TAG);
        urls = Urls.getInstance();
        tags = Tags.getInstance();
        active = Active.getInstance(getActivity());
        format = Format.getInstance();
        validation = Validation.getInstance();
        progress = new Progress(getActivity());
        alert = Alert.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        init();
        mView = inflater.inflate(R.layout.fragment_recommend_brands, container, false);
        company_name = (EditText) mView.findViewById(R.id.company_name);
        website_link = (EditText) mView.findViewById(R.id.website_link);
        phone_number = (EditText) mView.findViewById(R.id.phone_number);
        brand_submit = (Button) mView.findViewById(R.id.brand_submit);
        brand_submit.setOnClickListener(this);
        return mView;
    }

    @Override
    public void onClick(View v) {
        if (R.id.brand_submit == v.getId()) {
            addCompanyBrand();
        }
    }
    private void addCompanyBrand() {
        String comp_name = company_name.getText().toString();
        String web_link = website_link.getText().toString();
        String phone = phone_number.getText().toString();

        company_name.setError(null);
        website_link.setError(null);
        phone_number.setError(null);
        if (TextUtils.isEmpty(comp_name)) {
            company_name.setError("Please Fill Company Name");
            company_name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(web_link)) {
            website_link.setError("Please Fill Website Link");
            website_link.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            phone_number.setError("Please Fill Phone Number");
            phone_number.requestFocus();
            if(validation.isMobileNumber(phone)){
                phone_number.setError("Please Enter Correct Phone Number");
                phone_number.requestFocus();
            }
            return;
        }

        if (!active.isLogin()) {
            return;
        }
        if(!isConnected()){
            alert.showSnackIsConnected(isConnected());
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.ADD_COMPANY_BRANDS);
        map.put(tags.USER_ID, active.getUser().getUserId());
        map.put(tags.COMPANY_NAME, comp_name);
        map.put(tags.COMPANY_BRAND_WEBSITE_LINK, web_link);
        map.put(tags.PHONE_NUMBER, phone);
        progress.show();
        dmrRequest.doPost(urls.getCompany(), map, this);
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        progress.dismiss();
        try{
            if(jsonObject.has(tags.SUCCESS)){
                if(jsonObject.getInt(tags.SUCCESS)==tags.PASS){
                    refreshForm();
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
            Log.e(TAG,e.getMessage());
        }
    }

    @Override
    public void onError(VolleyError volleyError) {
        progress.dismiss();
        volleyError.printStackTrace();
        if (volleyError.getMessage() != null)
            Log.e(TAG,volleyError.getMessage());
    }
    private void refreshForm(){
        company_name.setText(null);
        company_name.setError(null);
        website_link.setText(null);
        website_link.setError(null);
        phone_number.setText(null);
        phone_number.setError(null);
    }

    private boolean isConnected() {
        return ConnectivityReceiver.isConnected();
    }

}
