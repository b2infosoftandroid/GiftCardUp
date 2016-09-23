package com.b2infosoft.giftcardup.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Format;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.custom.Progress;
import com.b2infosoft.giftcardup.filters.EditTextMaxFloat;
import com.b2infosoft.giftcardup.filters.EditTextMaxInteger;
import com.b2infosoft.giftcardup.model.GetOffer;
import com.b2infosoft.giftcardup.model.Merchant;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;
import com.libaml.android.view.chip.ChipLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TooManyListenersException;

/**
 * A simple {@link Fragment} subclass.
 */
public class BulkListing extends Fragment implements DMRResult, View.OnClickListener, TextWatcher, View.OnFocusChangeListener {
    private final static String TAG = BulkListing.class.getName();
    private Urls urls;
    private Tags tags;
    private Active active;
    private Format format;
    DMRRequest dmrRequest;
    Map<String, Merchant> hashMap;
    private Progress progress;
    View mView;
    float price, sell;
    int whoHasFocus = 0;
    ChipLayout chipLayout;
    AutoCompleteTextView brand_name;
    ImageView cardType;
    TextView textView;
    EditText serial_number, card_pin, card_balance, selling_percentage, asking_price, your_earning;
    Button cancel, save;
    boolean itemSelected = false;
    private final String ERROR_MESSAGE_ASKING_PRICE = "Must be less than Balance";
    private final String ERROR_MESSAGE_SELLING_PERCENTAGE = "Must be 0.0 < 100.00 %";
    private Merchant merchant;
    private String shipping_charge;

    public BulkListing() {
        // Required empty public constructor
    }

    private void init() {
        dmrRequest = DMRRequest.getInstance(getActivity(), TAG);
        urls = Urls.getInstance();
        tags = Tags.getInstance();
        active = Active.getInstance(getActivity());
        format = Format.getInstance();
        hashMap = new HashMap<>();
        progress = new Progress(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        init();
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_bulk_listing, container, false);
        //chipLayout = (ChipLayout) mView.findViewById(R.id.chip_layout);

        brand_name = (AutoCompleteTextView) mView.findViewById(R.id.brand_name);
        brand_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && itemSelected) {
                    brand_name.setText("");
                    itemSelected = false;
                    enableCardForm(itemSelected);
                }
            }
        });
        cardType = (ImageView) mView.findViewById(R.id.cardType);
        textView = (TextView)mView.findViewById(R.id.e_card_img_text);
        serial_number = (EditText) mView.findViewById(R.id.serial_number);
        card_pin = (EditText) mView.findViewById(R.id.card_pin);
        card_balance = (EditText) mView.findViewById(R.id.card_balance);
        card_balance.addTextChangedListener(this);
        selling_percentage = (EditText) mView.findViewById(R.id.selling_percentage);
        selling_percentage.addTextChangedListener(this);
        selling_percentage.setFilters(new InputFilter[]{new EditTextMaxFloat(selling_percentage, 100f, ERROR_MESSAGE_SELLING_PERCENTAGE)});
        //selling_percentage.setOnFocusChangeListener(this);
        asking_price = (EditText) mView.findViewById(R.id.asking_price);
        asking_price.addTextChangedListener(this);
        //asking_price.setOnFocusChangeListener(this);
        your_earning = (EditText) mView.findViewById(R.id.your_earning);
        your_earning.setEnabled(false);
        cancel = (Button) mView.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        save = (Button) mView.findViewById(R.id.save);
        save.setOnClickListener(this);
        loadMerchants();
        return mView;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable == card_balance.getEditableText()) {
            float bal = 0.0f;
            float sell = 0.0f;
            String bb = card_balance.getText().toString();
            String ss = selling_percentage.getText().toString();
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
                    asking_price.removeTextChangedListener(this);
                    asking_price.setText(format.getStringFloat(price));
                    getEarning();
                    asking_price.addTextChangedListener(this);
                }
            }else{
                your_earning.setText(null);
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
                    price = Float.parseFloat(str);
                    final float price = bal * sell / 100;
                    asking_price.removeTextChangedListener(this);
                    asking_price.setText(format.getStringFloat(price));
                    getEarning();
                    asking_price.addTextChangedListener(this);
                }
            }else{
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
                    selling_percentage.removeTextChangedListener(this);
                    selling_percentage.setText(format.getStringFloat(selling));
                    getEarning();
                    selling_percentage.addTextChangedListener(this);
                }
            }else{
                your_earning.setText(null);
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.selling_percentage) {
            if (!hasFocus) {
                String data = selling_percentage.getText().toString();
                float bal = 0.0f;
                float sell = 0.0f;
                String bb = card_balance.getText().toString();
                if (!TextUtils.isEmpty(bb)) {
                    bal = Float.parseFloat(bb);
                }
                if (!TextUtils.isEmpty(data)) {
                    sell = Float.parseFloat(data);
                    final float price = bal * sell / 100;
                    asking_price.setText(format.getStringFloat(price));

                }
            }
        } else if (v.getId() == R.id.asking_price) {
            if (!hasFocus) {
                float bal = 0.0f;
                float price = 0.0f;
                String data = asking_price.getText().toString();
                String bb = card_balance.getText().toString();
                if (!TextUtils.isEmpty(bb)) {
                    bal = Float.parseFloat(bb);
                }
                if (!TextUtils.isEmpty(data)) {
                    price = Float.parseFloat(data);
                    final float selling = 100 * price / bal;
                    selling_percentage.setText(format.getStringFloat(selling));
                }
            }
        }
    }

    private void loadMerchants() {
        final Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.COMPANY_BRANDS);
        dmrRequest.doPost(urls.getAppAction(), map, new DMRResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.has(tags.SUCCESS)) {
                        if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                            if (jsonObject.has(tags.COMPANY_BRANDS)) {
                                if (hashMap.size() > 0) {
                                    hashMap.clear();
                                }
                                JSONArray jsonArray = jsonObject.getJSONArray(tags.COMPANY_BRANDS);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    merchant = Merchant.fromJSON(jsonArray.getJSONObject(i));
                                    hashMap.put(merchant.getCompanyID(),merchant);
                                }
                            }
                            refreshMerchant();
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
                Log.e(TAG, volleyError.getMessage());
            }
        });
    }

    private void getEarning() {
        dmrRequest.cancelAll();
        String card_name = brand_name.getText().toString();
        String company_id = getCompanyID(card_name);
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
                Log.e(TAG, volleyError.getMessage());
            }
        });
    }

    private void refreshMerchant() {
        final String[] array = new String[hashMap.size()];
        int i = 0;
        for (String s : hashMap.keySet()) {
            array[i++] = hashMap.get(s).getCompanyName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, array);
        brand_name.setAdapter(adapter);
        brand_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Merchant merchant = hashMap.get(getCompanyID(brand_name.getAdapter().getItem(position) + ""));
                itemSelected = true;
                enableCardForm(itemSelected);
                brand_name.setText(merchant.getCompanyName());
//                Toast.makeText(getContext(),merchant.getCardType(),Toast.LENGTH_SHORT).show();
                if (merchant.getCardType() == 2) {
                    ///  E-CARD
                    textView.setVisibility(View.GONE);
                    cardType.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_24dp));
                } else if (merchant.getCardType() == 1) {
                    /// PHYSICAL CARD
                    cardType.setImageDrawable(null);
                }
                serial_number.requestFocus();
                selling_percentage.setText(format.getStringFloat(merchant.getSellingPercentage()));
            }
        });
        // chipLayout.setAdapter(adapter);
    }

    private void enableCardForm(boolean isMerchant) {
        cardType.setImageDrawable(null);
        serial_number.setEnabled(isMerchant);
        card_pin.setEnabled(isMerchant);
        card_balance.setEnabled(isMerchant);
        selling_percentage.setEnabled(isMerchant);
        asking_price.setEnabled(isMerchant);
        serial_number.setText(null);
        card_pin.setText(null);
        card_balance.setText(null);
        selling_percentage.setText(null);
        asking_price.setText(null);
        your_earning.setText(null);
    }

    private String getCompanyID(String companyName) {
        for (String s : hashMap.keySet()) {
            if (hashMap.get(s).getCompanyName().equalsIgnoreCase(companyName)) {
                return s;
            }
        }
        return null;
    }

    private void addGiftCard() {
        String company_id = merchant.getCompanyID();
        String card_name = brand_name.getText().toString();
        String serial_no = serial_number.getText().toString();
        String pin = card_pin.getText().toString();
        String balance = card_balance.getText().toString();
        String ask_price = asking_price.getText().toString();
        String earning = your_earning.getText().toString();

        String selling_per = selling_percentage.getText().toString();
        String description = "";

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
        if (TextUtils.isEmpty(ask_price)) {
            asking_price.setError("Please Fill Card Asking Price");
            asking_price.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(earning)) {
            your_earning.setError("Please Fill Card Your Earning");
            your_earning.requestFocus();
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

        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.ADD_GIFT_CARD);
        map.put(tags.USER_ID, active.getUser().getUserId() + "");
        map.put(tags.COMPANY_ID, company_id);
        map.put(tags.GIFT_CARD_CARD_NAME, card_name);
        map.put(tags.GIFT_CARD_SERIAL_NUMBER, serial_no);
        map.put(tags.GIFT_CARD_CARD_PIN, pin);
        map.put(tags.GIFT_CARD_CARD_VALUE, balance);
        map.put(tags.GIFT_CARD_SHIPPING_AND_COMMISSION_CHARGE, shipping_charge);
        map.put(tags.GIFT_CARD_CARD_PRICE, ask_price);
        map.put(tags.GIFT_CARD_YOUR_EARNING, earning);
        map.put(tags.GIFT_CARD_CARD_DISCOUNT, selling_per);
        map.put(tags.GIFT_CARD_CARD_DESCRIPTION, description);
        progress.show();
        dmrRequest.doPost(urls.getGiftCardInfo(), map, this);
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        progress.dismiss();
        try {
            if(jsonObject.has(tags.SUCCESS)){
                if(jsonObject.getInt(tags.SUCCESS) == tags.PASS){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Alert");
                    builder.setMessage("Your Card is Successfully Added");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                            builder1.setMessage("Do You Want To Add More Cards ? ");
                            builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    brand_name.requestFocus();
                                }
                            });
                            builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                        replaceFragment(new MyListing());
                                }
                            });
                            builder1.create().show();
                        }
                    });
                    builder.create().show();
                }
            }
        }catch (JSONException e){

        }
    }

    @Override
    public void onError(VolleyError volleyError) {
        progress.dismiss();
        volleyError.printStackTrace();
        Log.e(TAG, volleyError.getMessage());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                brand_name.requestFocus();
                break;
            case R.id.save:
                addGiftCard();
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

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, fragment);
        if (fragment instanceof Dashboard) {

        } else {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}
