package com.b2infosoft.giftcardup.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Alert;
import com.b2infosoft.giftcardup.app.GiftCardApp;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.model.OrderSummery;
import com.b2infosoft.giftcardup.services.ConnectivityReceiver;

public class PayPayPal extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private Tags tags;
    private OrderSummery orderSummery;
    Button action;
    private Alert alert;
    View main_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_paypal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        initUI();
        if (getIntent().hasExtra(tags.ORDER_SUMMERY)) {
            orderSummery = (OrderSummery) getIntent().getSerializableExtra(tags.ORDER_SUMMERY);
        }

        action = (Button) findViewById(R.id.action_continue);
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlaceOrder.class);
                intent.putExtra(tags.ORDER_SUMMERY, orderSummery);
                startActivity(intent);
            }
        });
    }

    private void init() {
        tags = Tags.getInstance();
        alert = Alert.getInstance(this);
        main_view = findViewById(R.id.main_view);

    }

    private void initUI() {

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
    protected void onResume() {
        super.onResume();
        GiftCardApp.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        alert.showSnackIsConnectedView(main_view, isConnected);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private boolean isConnected() {
        return ConnectivityReceiver.isConnected();
    }
}
