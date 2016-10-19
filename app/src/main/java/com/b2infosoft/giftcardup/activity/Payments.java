package com.b2infosoft.giftcardup.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Alert;
import com.b2infosoft.giftcardup.app.GiftCardApp;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.model.OrderSummery;
import com.b2infosoft.giftcardup.services.ConnectivityReceiver;

public class Payments extends AppCompatActivity implements View.OnClickListener , ConnectivityReceiver.ConnectivityReceiverListener {
    private Tags tags;
    RadioButton available_fund, credit_debit, paypal;
    Button action_continue;
    private OrderSummery orderSummery;
    private Alert alert;
    View main_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        initUI();
        if (getIntent().hasExtra(tags.ORDER_SUMMERY)) {
            orderSummery = (OrderSummery) getIntent().getSerializableExtra(tags.ORDER_SUMMERY);
        }
    }

    private void init() {
        tags = Tags.getInstance();
        alert = Alert.getInstance(this);
        main_view = findViewById(R.id.main_view);
    }

    private void initUI() {
        available_fund = (RadioButton) findViewById(R.id.available_fund);
        credit_debit = (RadioButton) findViewById(R.id.credit_debit);
        paypal = (RadioButton) findViewById(R.id.paypal);
        paypal.setVisibility(View.GONE);
        action_continue = (Button) findViewById(R.id.action_continue);
        action_continue.setOnClickListener(this);

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
    protected void onResume() {
        super.onResume();
        GiftCardApp.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        alert.showSnackIsConnectedView(main_view, isConnected);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_continue:
                if (available_fund.isChecked()) {
                    Intent intent = new Intent(this, PayAvailableFun.class);
                    orderSummery.setMethod(tags.PAYMENT_WITH_AVAILABLE_FUND);
                    intent.putExtra(tags.ORDER_SUMMERY, orderSummery);
                    startActivity(intent);
                } else if (credit_debit.isChecked()) {
                    Intent intent = new Intent(this, PayCreditDebit.class);
                    orderSummery.setMethod(tags.PAYMENT_WITH_CARD);
                    intent.putExtra(tags.ORDER_SUMMERY, orderSummery);
                    startActivity(intent);
                } else if (paypal.isChecked()) {
                    Intent intent = new Intent(this, PayPayPal.class);
                    orderSummery.setMethod(tags.PAYMENT_WITH_PAY_PAL);
                    intent.putExtra(tags.ORDER_SUMMERY, orderSummery);
                    startActivity(intent);
                }
                break;
        }
    }
    private boolean isConnected() {
        return ConnectivityReceiver.isConnected();
    }
}
