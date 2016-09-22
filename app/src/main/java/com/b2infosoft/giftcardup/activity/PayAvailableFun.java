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

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.model.OrderSummery;
import com.b2infosoft.giftcardup.model.User;

public class PayAvailableFun extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = PayAvailableFun.class.getName();
    /* INITIAL REQUIREMENTS */
    private Tags tags;
    private Active active;
    private OrderSummery orderSummery;

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
        updateUI();
    }

    private void init() {
        active = Active.getInstance(this);
        tags = Tags.getInstance();
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

    private void updateUI() {

        User user = active.getUser();
        float available = 0.0f;
        try {
            available = Float.parseFloat(user.getTotalSold());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        available_fund.setText("$" + available);
        balance.setText("$" + orderSummery.getBalance());

        /* SHOW : IF AVAILABLE FOUND LESS THEN BALANCE  */
        available_fund_1.setText("$" + available);
        balance_1.setText("$" + orderSummery.getBalance());
        remaining.setText("$" + (available - orderSummery.getBalance()));

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
            Intent intent = new Intent(getApplicationContext(),PlaceOrder.class);
            intent.putExtra("Method","Available Funds");
            startActivity(intent);
        }
    }
}
