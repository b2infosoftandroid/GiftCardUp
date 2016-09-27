package com.b2infosoft.giftcardup.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.api.interfaces.PaymentForm;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.credential.KeyDetails;
import com.b2infosoft.giftcardup.custom.AlertBox;
import com.b2infosoft.giftcardup.custom.Progress;
import com.b2infosoft.giftcardup.model.OrderSummery;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.model.Order;
import com.stripe.model.Product;
import com.stripe.model.ProductCollection;
import com.stripe.net.RequestOptions;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

public class PayCreditDebit extends AppCompatActivity implements PaymentForm {
    public static final String TAG = PayCreditDebit.class.getName();
    private Tags tags;
    private OrderSummery orderSummery;
    EditText cardNumber, cvcNumber;
    Spinner monthNumber, yearNumber;
    Button action;
    Progress progress;

    private void init() {
        tags = Tags.getInstance();
        progress = new Progress(this);
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

        cardNumber = (EditText) findViewById(R.id.pay_card_no);
        monthNumber = (Spinner) findViewById(R.id.month_spinner);
        yearNumber = (Spinner) findViewById(R.id.date_spinner);
        cvcNumber = (EditText) findViewById(R.id.pay_security_code);
        action = (Button) findViewById(R.id.action_continue);
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmpty();
            }
        });
    }

    private void checkEmpty() {
        String card = cardNumber.getText().toString();
        String cvc = cvcNumber.getText().toString();
        TextView spin1 = (TextView) monthNumber.getSelectedView();
        TextView spin2 = (TextView) yearNumber.getSelectedView();

        cardNumber.setError(null);
        cvcNumber.setError(null);
        spin1.setError(null);
        spin2.setError(null);
        if (card.length() == 0) {
            cardNumber.setError("Fill Card Number");
            cardNumber.requestFocus();
            return;
        }
        if (monthNumber.getSelectedItemPosition() == 0) {
            spin1.setError("Fill Expiry");
            monthNumber.requestFocus();
            return;
        }
        if (yearNumber.getSelectedItemPosition() == 0) {
            spin2.setError("Fill Expiry");
            yearNumber.requestFocus();
            return;
        }
        if (cvc.length() == 0 || cvc.length() > 3 || cvc.length() < 3) {
            cvcNumber.setError("Invalid CVC");
            cvcNumber.requestFocus();
            return;
        }

        validCard();
    /*
        Intent intent = new Intent(getApplicationContext(), PlaceOrder.class);
        intent.putExtra("Method", "Credit/Debit Card");
        intent.putExtra("Card", card);
        intent.putExtra("CVC", cvc);
        intent.putExtra("Month", monthNumber.getSelectedItemPosition());
        intent.putExtra("Date", yearNumber.getSelectedItem().toString());
        startActivity(intent);
        */
    }

    private void initUI() {

    }

    private void validCard() {
        Card card = new Card(getCardNumber(), getExpMonth(), getExpYear(), getCvc());
        //card.setCurrency(getCurrency());
        boolean validation = card.validateCard();
        if (validation) {
            progress.show();
            new Stripe().createToken(
                    card,
                    KeyDetails.PUBLISHABLE_KEY,
                    new TokenCallback() {
                        public void onSuccess(Token token) {
                            //getTokenList().addToList(token);
                            progress.dismiss();
                            Log.d("TOKEN NUMBER", token.getId());
                            AlertBox box = new AlertBox(PayCreditDebit.this);
                            box.setMessage(token.getId());
                            box.show();
                        }

                        public void onError(Exception error) {
                            progress.dismiss();
                            Log.e(TAG, error.getLocalizedMessage());
                            AlertBox box = new AlertBox(PayCreditDebit.this);
                            box.setMessage(error.getLocalizedMessage());
                            box.show();
                        }
                    });
        } else if (!card.validateNumber()) {
            cardNumber.setError("The card number that you entered is invalid");
            cardNumber.requestFocus();
            return;
        } else if (!card.validateExpiryDate()) {
            ((TextView) yearNumber.getSelectedView()).setError("The expiration date that you entered is invalid");
            yearNumber.requestFocus();
            return;
        } else if (!card.validateCVC()) {
            cvcNumber.setError("The CVC code that you entered is invalid");
            cvcNumber.requestFocus();
            return;
        } else {
            AlertBox box = new AlertBox(this);
            box.setMessage("The card details that you entered are invalid");
            box.show();
        }
    }

    private void Next() {
        RequestOptions requestOptions = RequestOptions.builder().setApiKey(KeyDetails.PUBLISHABLE_KEY).build();
    }
    private ProductCollection getAllProducts(){
        Map<String,Object> map = new HashMap<>();
        map.put("id",1);
        map.put("name",1);
        map.put("caption",1);
        map.put("description",1);
        map.put("images",1);
        map.put("url",1);
        map.put("shippable",1);

        ProductCollection collection = new ProductCollection();
        
        return collection;
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
    public String getCardNumber() {
        return this.cardNumber.getText().toString();
    }

    @Override
    public String getCvc() {
        return this.cvcNumber.getText().toString();
    }

    @Override
    public Integer getExpMonth() {
        return getInteger(this.monthNumber);
    }

    @Override
    public Integer getExpYear() {
        return getInteger(this.yearNumber);
    }

    @Override
    public String getCurrency() {
        return KeyDetails.CURRENCY_UNSPECIFIED;
    }

    private Integer getInteger(Spinner spinner) {
        try {
            return Integer.parseInt(spinner.getSelectedItem().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
