package com.b2infosoft.giftcardup.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.adapter.BankDetailRecyclerViewAdapter;
import com.b2infosoft.giftcardup.adapter.BankDetailRecyclerViewAdapter1;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.model.BankInfo;
import com.b2infosoft.giftcardup.services.ConnectivityReceiver;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileBankDetails extends AppCompatActivity {

    private final String TAG = ProfileBankDetails.class.getName();
    private Tags tags;
    private Active active;
    private Urls urls;
    DMRRequest dmrRequest;
    List<BankInfo> bankInfos;

    RecyclerView recyclerView;
    FloatingActionButton add_account;
    BankDetailRecyclerViewAdapter1 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        urls = Urls.getInstance();
        tags = Tags.getInstance();
        active = Active.getInstance(this);
        dmrRequest = DMRRequest.getInstance(this, TAG);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        add_account = (FloatingActionButton)findViewById(R.id.floating_add_account_btn);
        add_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileBankDetails.this, AddAccountInfo1.class));
            }
        });
        getBankDetails();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                this.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getBankDetails(){
        final Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.BANK_ACCOUNT_INFO);
        map.put(tags.USER_ID, active.getUser().getUserId()+ "");
        dmrRequest.doPost(urls.getUserInfo(), map, new DMRResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                //Log.d("watch",jsonObject.toString());
                try {
                    if (jsonObject.has(tags.SUCCESS)) {
                        if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                            if (jsonObject.has(tags.BANK_ACCOUNT_INFO)) {
                                if(bankInfos == null){
                                    bankInfos = new ArrayList<>();
                                }
                                JSONArray jsonArray =  jsonObject.getJSONArray(tags.BANK_ACCOUNT_INFO);
                                for(int i = 0;i< jsonArray.length();i++) {
                                    BankInfo info = BankInfo.fromJSON(jsonArray.getJSONObject(i));
                                    bankInfos.add(info);
                                }
                                adapter = new BankDetailRecyclerViewAdapter1(getApplicationContext(),bankInfos);
                                recyclerView.setAdapter(adapter);
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
                if (volleyError.getMessage() != null)
                    Log.e(TAG,volleyError.getMessage());
            }
        });
    }
    private boolean isConnected() {
        return ConnectivityReceiver.isConnected();
    }
}
