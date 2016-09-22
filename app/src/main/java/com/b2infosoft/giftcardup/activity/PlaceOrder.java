package com.b2infosoft.giftcardup.activity;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.model.ContactInformation;
import com.b2infosoft.giftcardup.model.User;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PlaceOrder extends AppCompatActivity {
    private static final String TAG = PlaceOrder.class.getName();
    Tags tags;
    Active active;
    Urls urls;
    User user;
    DMRRequest dmrRequest;
    TextView name,address,mobile,method,card_no,security_code,expiry;
    Button action;
    LinearLayout linearLayout;

    private void init(){
        tags = Tags.getInstance();
        active = Active.getInstance(this);
        urls = Urls.getInstance();
        user = active.getUser();
        dmrRequest = DMRRequest.getInstance(this,TAG);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        String str = getIntent().getExtras().getString("Mehtod");

        name = (TextView)findViewById(R.id.name);
        address = (TextView)findViewById(R.id.address);
        mobile = (TextView)findViewById(R.id.phone);
        card_no = (TextView)findViewById(R.id.pay_credit_card_no);
        expiry = (TextView)findViewById(R.id.pay_credit_expiry_date);
        security_code = (TextView)findViewById(R.id.pay_credit_security_code);
        linearLayout = (LinearLayout)findViewById(R.id.layout);
        method = (TextView)findViewById(R.id.pay_method);
        action = (Button)findViewById(R.id.action_complete);
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        fetchAddress();
        method.setText("METHOD :" + str);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void fetchAddress(){
        if (active.isLogin()) {
            Map<String, String> map = new HashMap<>();
            map.put(tags.USER_ACTION, tags.USER_CONTACT_INFORMATION);
            map.put(tags.USER_ID, user.getUserId() + "");
            dmrRequest.doPost(urls.getUserInfo(), map, new DMRResult() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        if (jsonObject.has(tags.SUCCESS)) {
                            if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                                if (jsonObject.has(tags.USER_CONTACT_INFORMATION)) {
                                    ContactInformation info = ContactInformation.fromJSON(jsonObject.getJSONObject(tags.USER_CONTACT_INFORMATION));
                                    setAddressData(info);
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

                }
            });
        }
    }

    private void setAddressData(ContactInformation info){
        name.setText(user.getFirstName() + " " + user.getLastName());
        address.setText(info.getAddressFull(this));
        mobile.setText(info.getPhoneNumber());

    }
}
