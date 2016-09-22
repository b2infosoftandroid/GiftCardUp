package com.b2infosoft.giftcardup.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.model.OrderSummery;

public class PayCreditDebit extends AppCompatActivity {
    private Tags tags;
    private OrderSummery orderSummery;
    EditText card_no, security;
    Spinner month, datespin;
    Button action;

    private void init() {
        tags = Tags.getInstance();
    }

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

        card_no = (EditText) findViewById(R.id.pay_card_no);
        month = (Spinner) findViewById(R.id.month_spinner);
        datespin = (Spinner) findViewById(R.id.date_spinner);
        security = (EditText) findViewById(R.id.pay_security_code);
        action = (Button) findViewById(R.id.action_continue);
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmpty();
            }
        });
    }

    private void checkEmpty() {
        String card = card_no.getText().toString();
        String cvc = security.getText().toString();
        TextView spin1 = (TextView) month.getSelectedView();
        TextView spin2 = (TextView) datespin.getSelectedView();

        card_no.setError(null);
        security.setError(null);
        spin1.setError(null);
        spin2.setError(null);
        if (card.length() == 0) {
            card_no.setError("Fill Card Number");
            card_no.requestFocus();
            return;
        }
        if (month.getSelectedItemPosition() == 0) {
            spin1.setError("Fill Expiry");
            month.requestFocus();
            return;
        }
        if (datespin.getSelectedItemPosition() == 0) {
            spin2.setError("Fill Expiry");
            datespin.requestFocus();
            return;
        }
        if (cvc.length() == 0) {
            security.setError("Invalid CVC");
            security.requestFocus();
            return;
        }

        Intent intent = new Intent(getApplicationContext(), PlaceOrder.class);
        intent.putExtra("Method", "Credit/Debit Card");
        startActivity(intent);
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
