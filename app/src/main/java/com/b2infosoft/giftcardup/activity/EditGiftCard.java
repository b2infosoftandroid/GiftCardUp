package com.b2infosoft.giftcardup.activity;

import android.content.DialogInterface;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Format;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.custom.AlertBox;
import com.b2infosoft.giftcardup.custom.Progress;
import com.b2infosoft.giftcardup.filters.EditTextMaxFloat;
import com.b2infosoft.giftcardup.model.GiftCard;
import com.b2infosoft.giftcardup.services.ConnectivityReceiver;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditGiftCard extends AppCompatActivity implements TextWatcher, View.OnClickListener, DMRResult {
    private final static String TAG = EditGiftCard.class.getName();
    private Urls urls;
    private Tags tags;
    private Active active;
    private Format format;
    DMRRequest dmrRequest;
    private Progress progress;
    private GiftCard giftCard;
    AutoCompleteTextView brand_name;
    ImageView cardType;
    EditText serial_number, card_pin, card_balance, selling_percentage, asking_price, your_earning, card_description;
    Button cancel, save;
    //View mAskingPrice;
    boolean itemSelected = false;
    private String shipping_charge;
    private final String ERROR_MESSAGE_ASKING_PRICE = "Must be less than Balance";
    private final String ERROR_MESSAGE_SELLING_PERCENTAGE = "Must be 0.0 < 100.00 %";

    private void init() {
        dmrRequest = DMRRequest.getInstance(this, TAG);
        urls = Urls.getInstance();
        tags = Tags.getInstance();
        active = Active.getInstance(this);
        format = Format.getInstance();
        progress = new Progress(this);
        if (getIntent().hasExtra(tags.GIFT_CARDS)) {
            giftCard = (GiftCard) getIntent().getSerializableExtra(tags.GIFT_CARDS);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gift_card);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        initView();
        if (giftCard != null) {
            setOldData(giftCard);
        }
    }

    private void initView() {
        brand_name = (AutoCompleteTextView) findViewById(R.id.brand_name);
        cardType = (ImageView) findViewById(R.id.cardType);
        serial_number = (EditText) findViewById(R.id.serial_number);
        card_pin = (EditText) findViewById(R.id.card_pin);
        card_description = (EditText) findViewById(R.id.card_description);
        card_balance = (EditText) findViewById(R.id.card_balance);
        card_balance.addTextChangedListener(this);
        selling_percentage = (EditText) findViewById(R.id.selling_percentage);
        selling_percentage.addTextChangedListener(this);
        selling_percentage.setFilters(new InputFilter[]{new EditTextMaxFloat(selling_percentage, 100f, ERROR_MESSAGE_SELLING_PERCENTAGE)});
        asking_price = (EditText) findViewById(R.id.asking_price);
        asking_price.addTextChangedListener(this);
        your_earning = (EditText) findViewById(R.id.your_earning);
        your_earning.setEnabled(false);

        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);

        // mAskingPrice = findViewById(R.id.view_asking_price);
    }

    private void setOldData(GiftCard oldData) {
        brand_name.setText(oldData.getCardName());
        if (oldData.getCardType() == 2) {
            ///  E-CARD
            cardType.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_24dp));
        } else if (oldData.getCardType() == 1) {
            /// PHYSICAL CARD
            cardType.setImageDrawable(null);
        }
        serial_number.setText(oldData.getSerialNumber());
        card_pin.setText(oldData.getCardPin());
        card_description.setText(oldData.getCardDescription());
        selling_percentage.setText(format.getStringFloat(oldData.getStatusType() == 2 ? oldData.getSellingPercentage() : oldData.getSell()));
        card_balance.setText(format.getStringFloat(oldData.getCardPrice()));
        if (oldData.getStatusType() != 2) {
            /* Set Data when Card type is not Quick Sell (Speedy Shell) */
            asking_price.setText(format.getStringFloat(oldData.getCardValue()));
        }
        if (oldData.getStatusType() == 2) {
            your_earning.setText(format.getStringFloat(oldData.getCardValue()));
        } else {
            your_earning.setText(format.getStringFloat(oldData.getYourEarning()));
        }
        if (oldData.getStatusType() == 2) {
            selling_percentage.setEnabled(false);
            // mAskingPrice.setVisibility(View.GONE);
        } else {
            selling_percentage.setEnabled(true);
            //mAskingPrice.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                finish();
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
        switch (v.getId()) {
            case R.id.cancel:
                super.onBackPressed();
                finish();
                break;
            case R.id.save:
                updateGiftCard();
                break;
            default:
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable == card_balance.getEditableText()) {
            float bal = 0.0f;
            float sell = 0.0f;
            String bb = card_balance.getText().toString();
            String ss = selling_percentage.getText().toString();
            if (giftCard.getStatusType() == 2) {
                if (!TextUtils.isEmpty(bb)) {
                    if (bb.contains("%")) {
                        bb = ss.replaceAll("%", "");
                    }
                    sell = Float.parseFloat(bb);
                    String per = giftCard.getSellingPercentage();
                    if (per.contains("%")) {
                        per = per.replaceAll("%", "");
                    }
                    if (per == null || per.length() == 0) {
                        per = "0.0";
                    }
                    float percentage = Float.parseFloat(per);
                    float earning = percentage * sell / 100;
                    your_earning.setText(format.getStringFloat(earning));
                } else {
                    your_earning.setText(null);
                }
            } else {
                if (!TextUtils.isEmpty(bb)) {
                    asking_price.setFilters(new InputFilter[]{new EditTextMaxFloat(asking_price, bb, ERROR_MESSAGE_ASKING_PRICE)});
                } else {
                    asking_price.setFilters(new InputFilter[]{new EditTextMaxFloat(asking_price, 0, ERROR_MESSAGE_ASKING_PRICE)});
                }
                if (!TextUtils.isEmpty(bb)) {
                    bal = Float.parseFloat(bb);
                    if (!TextUtils.isEmpty(ss)) {
                        if (ss.contains("%")) {
                            ss = ss.replaceAll("%", "");
                        }
                        sell = Float.parseFloat(ss);
                        final float price = bal * sell / 100;
                    /*  WORKING ONLY NOT A QUICK SHELL GIFT CARD */
                        if (giftCard.getStatusType() != 2) {
                            asking_price.removeTextChangedListener(this);
                            asking_price.setText(format.getStringFloat(price));
                            getEarning();
                            asking_price.addTextChangedListener(this);
                        }
                    }
                }
            }

        } else if (editable == selling_percentage.getEditableText()) {
            float bal = 0.0f;
            float sell = 0.0f;
            String bb = card_balance.getText().toString();
            if (!TextUtils.isEmpty(bb)) {
                bal = Float.parseFloat(bb);
                if (!TextUtils.isEmpty(editable)) {
                    String str = editable.toString();
                    if (str.contains("%")) {
                        str = str.replaceAll("%", "");
                    }
                    sell = Float.parseFloat(str);
                    final float price = bal * sell / 100;

                    /*  WORKING ONLY NOT A QUICK SHELL GIFT CARD */
                    if (giftCard.getStatusType() != 2) {
                        asking_price.removeTextChangedListener(this);
                        asking_price.setText(format.getStringFloat(price));
                        getEarning();
                        asking_price.addTextChangedListener(this);
                    }
                }
            } else {
                your_earning.setText(null);
            }
        } else if (editable == asking_price.getEditableText()) {
            float bal = 0.0f;
            float price = 0.0f;
            String bb = card_balance.getText().toString();
            if (!TextUtils.isEmpty(bb)) {
                bal = Float.parseFloat(bb);
                if (!TextUtils.isEmpty(editable)) {
                    String str = editable.toString();
                    if (str.contains("%")) {
                        str = str.replaceAll("%", "");
                    }
                    price = Float.parseFloat(str);
                    final float selling = 100 * price / bal;
                    /*  WORKING ONLY NOT A QUICK SHELL GIFT CARD */
                    if (giftCard.getStatusType() != 2) {
                        selling_percentage.removeTextChangedListener(this);
                        selling_percentage.setText(format.getStringFloat(selling));
                        getEarning();
                        selling_percentage.addTextChangedListener(this);
                    }
                }
            } else {
                your_earning.setText(null);
            }
        }
    }

    private void getEarning() {
        dmrRequest.cancelAll();
        //String card_name = brand_name.getText().toString();
        String card_name = giftCard.getCardName();
        String company_id = giftCard.getCompanyID() + "";
        String price = asking_price.getText().toString();
        final Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.GET_EARNING_PRICE);
        map.put(tags.USER_ID, active.getUser().getUserId() + "");
        map.put(tags.COMPANY_ID, company_id);
        map.put(tags.GIFT_CARD_VALUE, price);

        dmrRequest.doPost(urls.getGiftCardInfo(), map, new DMRResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.has(tags.SUCCESS)) {
                        if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                            if (jsonObject.has(tags.GET_EARNING_PRICE)) {
                                JSONObject object = jsonObject.getJSONObject(tags.GET_EARNING_PRICE);
                                if (object.has(tags.EARNING_PRICE)) {
                                    your_earning.setText(format.getStringFloat(object.getString(tags.EARNING_PRICE)));
                                }
                                if (object.has(tags.SHIPPING_COMMISSION_CHARGE)) {
                                    shipping_charge = format.getStringFloat(object.getString(tags.SHIPPING_COMMISSION_CHARGE));
                                }
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
                    Log.e(TAG, volleyError.getMessage());
            }
        });
    }

    private void updateGiftCard() {
        String card_name = giftCard.getCardName();
        String company_id = giftCard.getCompanyID() + "";
        String serial_no = serial_number.getText().toString();
        String pin = card_pin.getText().toString();
        String balance = card_balance.getText().toString();
        String ask_price = asking_price.getText().toString();
        String earning = your_earning.getText().toString();

        String selling_per = selling_percentage.getText().toString();
        String description = card_description.getText().toString();

        serial_number.setError(null);
        card_pin.setError(null);
        card_balance.setError(null);
        selling_percentage.setError(null);
        asking_price.setError(null);
        your_earning.setError(null);
        if (TextUtils.isEmpty(serial_no)) {
            serial_number.setError("Please Fill Card Serial Number");
            serial_number.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(pin)) {
            card_pin.setError("Please Fill Card Pin Number");
            card_pin.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(selling_per)) {
            selling_percentage.setError("Please Fill Card Selling %");
            selling_percentage.requestFocus();
            return;
        }
        if (giftCard.getStatusType() != 2) {
            if (TextUtils.isEmpty(ask_price)) {
                asking_price.setError("Please Fill Card Asking Price");
                asking_price.requestFocus();
                return;
            }
            if (shipping_charge == null) {
                showToast("Shipping Charge is Null. Try Again");
                return;
            }
            if (TextUtils.isEmpty(shipping_charge)) {
                showToast("Shipping Charge is Null. Try Again");
                return;
            }
        }
        if (TextUtils.isEmpty(earning)) {
            your_earning.setError("Please Fill Card Your Earning");
            your_earning.requestFocus();
            return;
        }


        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.EDIT_GIFT_CARD);
        map.put(tags.USER_ID, active.getUser().getUserId() + "");
        map.put(tags.CARD_ID, giftCard.getGiftCardID() + "");
        map.put(tags.CARD_TYPE, giftCard.getCardType() + "");
        map.put(tags.COMPANY_ID, company_id);
        map.put(tags.GIFT_CARD_YOUR_EARNING, earning);
        map.put(tags.GIFT_CARD_SERIAL_NUMBER, serial_no);
        map.put(tags.GIFT_CARD_CARD_PIN, pin);
        map.put(tags.GIFT_CARD_CARD_VALUE, balance);
        if (giftCard.getStatusType() == 2) {
            map.put(tags.GIFT_CARD_CARD_PRICE, earning);
        } else {
            map.put(tags.GIFT_CARD_CARD_PRICE, ask_price);
            map.put(tags.GIFT_CARD_SHIPPING_AND_COMMISSION_CHARGE, shipping_charge);
        }
        map.put(tags.GIFT_CARD_CARD_DISCOUNT, selling_per);
        map.put(tags.GIFT_CARD_CARD_DESCRIPTION, description);
        progress.show();
        dmrRequest.doPost(urls.getGiftCardInfo(), map, this);
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        progress.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        try {
            if (jsonObject.has(tags.SUCCESS)) {
                if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                    builder.setTitle("Successfully update");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onBackPressed();
                            finish();
                        }
                    });
                } else if (jsonObject.getInt(tags.SUCCESS) == tags.FAIL) {
                    builder.setTitle("Something went wrong? Try Again");
                }
            }
            builder.create().show();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onError(VolleyError volleyError) {
        progress.dismiss();
        volleyError.printStackTrace();
        if (volleyError.getMessage() != null)
            Log.e(TAG, volleyError.getMessage());
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private boolean isConnected() {
         return ConnectivityReceiver.isConnected();
    }
}
