package com.b2infosoft.giftcardup.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import com.b2infosoft.giftcardup.custom.AlertBox;
import com.b2infosoft.giftcardup.model.ContactInformation;
import com.b2infosoft.giftcardup.model.OrderSummery;
import com.b2infosoft.giftcardup.model.User;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PlaceOrder extends AppCompatActivity implements DMRResult {
    private static final String TAG = PlaceOrder.class.getName();
    Tags tags;
    Active active;
    Urls urls;
    User user;
    DMRRequest dmrRequest;
    TextView name, address, mobile, method, card_no, security_code, expiry;
    Button action_complete;
    LinearLayout linearLayout;
    private OrderSummery orderSummery;

    private void init() {
        tags = Tags.getInstance();
        active = Active.getInstance(this);
        urls = Urls.getInstance();
        user = active.getUser();
        dmrRequest = DMRRequest.getInstance(this, TAG);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        if (getIntent().hasExtra(tags.ORDER_SUMMERY)) {
            orderSummery = (OrderSummery) getIntent().getExtras().getSerializable(tags.ORDER_SUMMERY);
        }
        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);
        mobile = (TextView) findViewById(R.id.phone);
        card_no = (TextView) findViewById(R.id.pay_credit_card_no);
        expiry = (TextView) findViewById(R.id.pay_credit_expiry_date);
        security_code = (TextView) findViewById(R.id.pay_credit_security_code);
        linearLayout = (LinearLayout) findViewById(R.id.layout);
        method = (TextView) findViewById(R.id.pay_method);
        action_complete = (Button) findViewById(R.id.action_complete);
        action_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PlaceOrder.this);
                builder.setTitle("Alert");
                builder.setMessage("Are you sure Complete Order?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        orderPlace();
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        fetchAddress();
        setUI();
    }

    private void setUI() {
        if (orderSummery != null) {
            method.setText(orderSummery.getMethod());
            if (orderSummery.getMethod().equalsIgnoreCase(tags.PAYMENT_WITH_CARD)) {
                linearLayout.setVisibility(View.VISIBLE);
                String num = getIntent().getStringExtra("Card");
                String code = getIntent().getStringExtra("CVC");
                int month = getIntent().getIntExtra("Month", 0);
                String monthDate = getIntent().getStringExtra("Date");
                card_no.setText("Card Number : " + num);
                security_code.setText("Security code : " + code);
                expiry.setText("Expiration Date : " + month + " Months " + monthDate + " days");
            }
        }
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

    private void fetchAddress() {
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
                    volleyError.printStackTrace();
                    if (volleyError.getMessage() != null)
                        Log.e(TAG,volleyError.getMessage());
                }
            });
        }
    }

    private void setAddressData(ContactInformation info) {
        name.setText(user.getFirstName() + " " + user.getLastName());
        address.setText(info.getAddressFull(this));
        mobile.setText(info.getPhoneNumber());
    }

    private void orderPlace() {
        if (orderSummery != null) {
            Map<String, String> map = new HashMap<>();
            map.put(tags.USER_ACTION, tags.PAY_WITH_AVAILABLE_FUND);
            map.put(tags.USER_ID, active.getUser().getUserId());
            map.put(tags.ITEM_DATA, orderSummery.getItemData());
            map.put(tags.ITEM_ID, orderSummery.getItemId());
            map.put(tags.TOTAL_PRICE, orderSummery.getBalance() + "");
            map.put(tags.TOTAL_ITEM, orderSummery.getTotalItem() + "");
            map.put(tags.COMMISSION, orderSummery.getCommission() + "");
            dmrRequest.doPost(urls.getPayment(), map, this);
        } else {
            AlertBox box = new AlertBox(this);
            box.setTitle("Alert");
            box.setMessage("Invalid Order");
            box.show();
        }
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        try {
            if (jsonObject.has(tags.SUCCESS)) {
                if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                    orderSummery = null;
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Alert");
                    builder.setMessage("Your Order Successfully Complete. Please Check My Order");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(PlaceOrder.this,Main.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra(tags.SELECTED_FRAGMENTS, tags.FRAGMENT_MY_ORDERS);
                            startActivity(intent);
                            dialog.dismiss();
                            finish();
                        }
                    });
                    builder.create().show();
                } else if (jsonObject.getInt(tags.SUCCESS) == tags.FAIL) {
                    AlertBox box = new AlertBox(this);
                    box.setTitle("Alert");
                    box.setMessage("Something went wrong. Try Again");
                    box.show();
                }
            }
        } catch (JSONException error) {
            error.printStackTrace();
            Log.e(TAG, error.getLocalizedMessage());
        }
    }

    @Override
    public void onError(VolleyError volleyError) {
        volleyError.printStackTrace();
        if (volleyError.getMessage() != null)
            Log.e(TAG,volleyError.getMessage());
    }
}
