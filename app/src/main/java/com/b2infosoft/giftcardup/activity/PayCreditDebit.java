package com.b2infosoft.giftcardup.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.model.OrderSummery;

public class PayCreditDebit extends AppCompatActivity {
    private Tags tags;
    private OrderSummery orderSummery;
    EditText card_no,security;
    Button action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_credit_debit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        initUI();
        if (getIntent().hasExtra(tags.ORDER_SUMMERY)) {
            orderSummery = (OrderSummery) getIntent().getSerializableExtra(tags.ORDER_SUMMERY);
        }

        card_no = (EditText)findViewById(R.id.pay_card_no);
        security = (EditText)findViewById(R.id.pay_security_code);
        action = (Button)findViewById(R.id.action_continue);
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PlaceOrder.class);
                intent.putExtra("Method","Credit/Debit Card");
                startActivity(intent);
            }
        });
    }
    private void init() {
        tags = Tags.getInstance();
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
