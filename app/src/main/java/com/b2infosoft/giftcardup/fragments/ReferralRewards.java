package com.b2infosoft.giftcardup.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Format;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.custom.Progress;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReferralRewards extends Fragment implements View.OnClickListener,DMRResult{
    private final static String TAG = RecommendBrands.class.getName();
    private Urls urls;
    private Tags tags;
    private Active active;
    private Format format;
    DMRRequest dmrRequest;
    private Progress progress;
    /* UI Controls View */
    private View mView;
    private EditText referral_url;
    private Button action_referral;
    public ReferralRewards() {
        // Required empty public constructor
    }
    private void init() {
        dmrRequest = DMRRequest.getInstance(getActivity(), TAG);
        urls = Urls.getInstance();
        tags = Tags.getInstance();
        active = Active.getInstance(getActivity());
        format = Format.getInstance();
        progress = new Progress(getActivity());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        init();
        mView= inflater.inflate(R.layout.fragment_referral_rewards, container, false);
        referral_url = (EditText) mView.findViewById(R.id.referral_url);
        referral_url.setText(urls.getReferralURL(active.getUser().getReferralCode()));
        action_referral = (Button) mView.findViewById(R.id.action_refer);
        action_referral.setOnClickListener(this);
        return mView;
    }

    @Override
    public void onClick(View v) {
        if(R.id.action_refer==v.getId()){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, urls.getReferralURL(active.getUser().getReferralCode()));
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Refer to..."));
        }
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        progress.dismiss();
        try{
            if(jsonObject.has(tags.SUCCESS)){
                if(jsonObject.getInt(tags.SUCCESS)==tags.PASS){

                }
            }
        }catch (JSONException e){
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onError(VolleyError volleyError) {
        progress.dismiss();
        volleyError.printStackTrace();
        Log.e(TAG, volleyError.getMessage());
    }
}
