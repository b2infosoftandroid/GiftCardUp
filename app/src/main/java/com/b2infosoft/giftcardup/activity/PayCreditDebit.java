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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.api.interfaces.PaymentForm;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.credential.KeyDetails;
import com.b2infosoft.giftcardup.custom.AlertBox;
import com.b2infosoft.giftcardup.custom.Progress;
import com.b2infosoft.giftcardup.model.OrderSummery;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PayCreditDebit extends AppCompatActivity implements PaymentForm, DMRResult {
    public static final String TAG = PayCreditDebit.class.getName();
    private Tags tags;
    private Active active;
    private Urls urls;
    private OrderSummery orderSummery;
    EditText cardNumber, cvcNumber;
    Spinner monthNumber, yearNumber;
    Button action;
    Progress progress;
    DMRRequest dmrRequest;

    private void init() {
        tags = Tags.getInstance();
        urls = Urls.getInstance();
        active = Active.getInstance(this);
        dmrRequest = DMRRequest.getInstance(this, TAG);
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
    }

    private void initUI() {

    }

    private void validCard() {
        Card card = new Card(getCardNumber(), getExpMonth(), getExpYear(), getCvc());
        boolean validation = card.validateCard();
        if (validation) {
            progress.show();
            new Stripe().createToken(
                    card,
                    KeyDetails.PUBLISHABLE_KEY,
                    new TokenCallback() {
                        public void onSuccess(Token token) {
                            progress.dismiss();
                            orderPlace();
                        }

                        public void onError(Exception volleyError) {
                            progress.dismiss();
                            volleyError.printStackTrace();
                            if (volleyError.getMessage() != null)
                                Log.e(TAG, volleyError.getMessage());
                            AlertBox box = new AlertBox(PayCreditDebit.this);
                            box.setMessage(volleyError.getLocalizedMessage());
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

    private void orderPlace() {
        if (orderSummery != null) {
            Map<String, String> map = new HashMap<>();
            map.put(tags.USER_ACTION, tags.PAY_WITH_CARD);
            map.put(tags.USER_ID, active.getUser().getUserId());
            map.put(tags.ITEM_DATA, orderSummery.getItemData());
            map.put(tags.ITEM_ID, orderSummery.getItemId());
            map.put(tags.TOTAL_PRICE, Math.round(orderSummery.getBalance() * 100) + "");
            map.put(tags.TOTAL_ITEM, orderSummery.getTotalItem() + "");
            map.put(tags.COMMISSION, orderSummery.getCommission() + "");
            map.put(tags.CARD_NUMBER, getCardNumber());
            map.put(tags.CARD_EXP_MONTH, getExpMonth() + "");
            map.put(tags.CARD_EXP_YEAR, getExpYear() + "");
            map.put(tags.CARD_CVC_CHECK, getCvc());
            map.put(tags.CARD_CURRENCY, getCurrency());
            progress.show();
            dmrRequest.doPost(urls.getPayment(), map, this);
        } else {
            AlertBox box = new AlertBox(this);
            box.setTitle("Alert");
            box.setMessage("Invalid Order");
            box.show();
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
    public void onSuccess(JSONObject jsonObject) {
        progress.dismiss();
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
                            Intent intent = new Intent(PayCreditDebit.this, Main.class);
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
            Log.e(TAG, volleyError.getMessage());
        progress.dismiss();
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
        return tags.CURRENCY;
    }

    private Integer getInteger(Spinner spinner) {
        try {
            return Integer.parseInt(spinner.getSelectedItem().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
