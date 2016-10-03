package com.b2infosoft.giftcardup.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.model.OrderSummery;
import com.b2infosoft.giftcardup.model.UserBalance;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PayAvailableFun extends AppCompatActivity implements View.OnClickListener, DMRResult {
    public static final String TAG = PayAvailableFun.class.getName();
    /* INITIAL REQUIREMENTS */
    private Tags tags;
    private Active active;
    private OrderSummery orderSummery;
    private DMRRequest dmrRequest;
    private Urls urls;

    /* UI VIEW */
    private TextView available_fund, balance, available_fund_1, balance_1, remaining;
    private RadioButton paypal;
    private Button action_continue;
    private View less_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_available_fun);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        initUI();
        if (getIntent().hasExtra(tags.ORDER_SUMMERY)) {
            orderSummery = (OrderSummery) getIntent().getExtras().getSerializable(tags.ORDER_SUMMERY);
        }
        checkAvailableBalance();
    }

    private void init() {
        active = Active.getInstance(this);
        tags = Tags.getInstance();
        dmrRequest = DMRRequest.getInstance(this, TAG);
        urls = Urls.getInstance();
    }

    private void initUI() {
        available_fund = (TextView) findViewById(R.id.available_fund);
        balance = (TextView) findViewById(R.id.balance);
        available_fund_1 = (TextView) findViewById(R.id.available_fund_1);
        balance_1 = (TextView) findViewById(R.id.balance_1);
        remaining = (TextView) findViewById(R.id.remaining);
        paypal = (RadioButton) findViewById(R.id.paypal);
        action_continue = (Button) findViewById(R.id.action_continue);
        action_continue.setOnClickListener(this);
        less_amount = findViewById(R.id.less_amount);
    }

    private void checkAvailableBalance() {
        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.AVAILABLE_FUND_BALANCE);
        map.put(tags.USER_ID, active.getUser().getUserId());
        dmrRequest.doPost(urls.getUserInfo(), map, this);
    }

    private void updateUI(double available) {
        available_fund.setText("$" + available);
        balance.setText("$" + orderSummery.getBalance());

        /* SHOW : IF AVAILABLE FOUND LESS THEN BALANCE  */
        available_fund_1.setText("$" + available);
        balance_1.setText("$" + orderSummery.getBalance());
        remaining.setText("$" + (available - orderSummery.getBalance()));

        if (available - orderSummery.getBalance() < 0) {
            less_amount.setVisibility(View.VISIBLE);
        } else {
            less_amount.setVisibility(View.GONE);
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

    @Override
    public void onClick(View v) {
        if (R.id.action_continue == v.getId()) {
            Intent intent = new Intent(getApplicationContext(), PlaceOrder.class);
            intent.putExtra(tags.ORDER_SUMMERY, orderSummery);
            startActivity(intent);
        }
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        try {
            if (jsonObject.has(tags.SUCCESS)) {
                if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                    if (jsonObject.has(tags.AVAILABLE_FUND_BALANCE)) {
                        //updateUI((float) jsonObject.getDouble(tags.AVAILABLE_FUND_BALANCE));
                        UserBalance balance = UserBalance.fromJSON(jsonObject.getJSONObject(tags.AVAILABLE_FUND_BALANCE));
                        updateUI(balance.getAvailable_fund());
                    }
                }
            }
        } catch (JSONException error) {
            error.printStackTrace();
            if (error.getMessage() != null)
                Log.e(TAG, error.getLocalizedMessage());
        }
    }

    @Override
    public void onError(VolleyError volleyError) {
        volleyError.printStackTrace();
        if (volleyError.getMessage() != null)
            Log.e(TAG, volleyError.getMessage());
    }
}
