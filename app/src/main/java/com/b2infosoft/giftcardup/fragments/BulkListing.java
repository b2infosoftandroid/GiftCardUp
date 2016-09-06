package com.b2infosoft.giftcardup.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.model.GetOffer;
import com.b2infosoft.giftcardup.model.Merchant;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;
import com.libaml.android.view.chip.ChipLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TooManyListenersException;

/**
 * A simple {@link Fragment} subclass.
 */
public class BulkListing extends Fragment implements DMRResult, View.OnClickListener {
    private final static String TAG = BulkListing.class.getName();
    private Urls urls;
    private Tags tags;
    private Active active;
    DMRRequest dmrRequest;
    Queue<GetOffer> offerQueue;
    Map<String, String> hashMap;
    View mView;

    ChipLayout chipLayout;
    AutoCompleteTextView brand_name;
    TextView cardType;
    EditText serial_number, card_pin, card_balance, selling_percentage, asking_price, your_earning;
    Button cancel, save;

    public BulkListing() {
        // Required empty public constructor
    }

    private void init() {
        dmrRequest = DMRRequest.getInstance(getActivity(), TAG);
        urls = Urls.getInstance();
        tags = Tags.getInstance();
        active = Active.getInstance(getActivity());
        offerQueue = new LinkedList<>();
        hashMap = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        init();
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_bulk_listing, container, false);
        //chipLayout = (ChipLayout) mView.findViewById(R.id.chip_layout);
        brand_name = (AutoCompleteTextView) mView.findViewById(R.id.brand_name);
        cardType = (TextView) mView.findViewById(R.id.cardType);
        serial_number = (EditText) mView.findViewById(R.id.serial_number);
        card_pin = (EditText) mView.findViewById(R.id.card_pin);
        card_balance = (EditText) mView.findViewById(R.id.card_balance);
        selling_percentage = (EditText) mView.findViewById(R.id.selling_percentage);
        asking_price = (EditText) mView.findViewById(R.id.asking_price);
        your_earning = (EditText) mView.findViewById(R.id.your_earning);
        cancel = (Button) mView.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        save = (Button) mView.findViewById(R.id.save);
        save.setOnClickListener(this);
        loadMerchants();
        return mView;
    }

    private void loadMerchants() {
        final Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.COMPANY_BRANDS);
        dmrRequest.doPost(urls.getAppAction(), map, new DMRResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                Log.d(TAG, jsonObject.toString());
                try {
                    if (jsonObject.has(tags.SUCCESS)) {
                        if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                            if (jsonObject.has(tags.COMPANY_BRANDS)) {
                                if (hashMap.size() > 0) {
                                    hashMap.clear();
                                }
                                JSONArray jsonArray = jsonObject.getJSONArray(tags.COMPANY_BRANDS);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Merchant merchant = Merchant.fromJSON(jsonArray.getJSONObject(i));
                                    hashMap.put(merchant.getCompanyID(), merchant.getCompanyName());
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

    private void refreshMerchant() {
        String[] array = new String[hashMap.size()];
        int i = 0;
        for (String s : hashMap.keySet()) {
            array[i++] = hashMap.get(s);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, array);
        brand_name.setAdapter(adapter);
        // chipLayout.setAdapter(adapter);
    }

    private String getCompanyID(String companyName) {
        for (String s : hashMap.keySet()) {
            if (hashMap.get(s).equalsIgnoreCase(companyName)) {
                return s;
            }
        }
        return null;
    }

    private void addGiftCard() {
    /*
        List<String> list = chipLayout.getText();
        if (list.size() == 0) {
            return;
        }
        String card_name = list.get(0);
        String company_id = hashMap.get(card_name);
*/
        String card_name = brand_name.getText().toString();
        String company_id = getCompanyID(card_name);

        String serial_no = serial_number.getText().toString();
        String pin = card_pin.getText().toString();
        String balance = card_balance.getText().toString();
        String selling_per = selling_percentage.getText().toString();
        String ask_price = asking_price.getText().toString();
        String earning = your_earning.getText().toString();

        String discount = "";
        String description = "";

        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.ADD_GIFT_CARD);
        map.put(tags.COMPANY_ID, company_id);
        map.put(tags.USER_ID, active.getUser().getUserId() + "");
        map.put(tags.GIFT_CARD_CARD_NAME, card_name);
        map.put(tags.GIFT_CARD_CARD_PRICE, ask_price);
        map.put(tags.GIFT_CARD_CARD_VALUE, balance);
        map.put(tags.GIFT_CARD_SHIPPING_AND_COMMISSION_CHARGE, selling_per);
        map.put(tags.GIFT_CARD_YOUR_EARNING, earning);
        map.put(tags.GIFT_CARD_SERIAL_NUMBER, serial_no);
        map.put(tags.GIFT_CARD_CARD_PIN, pin);
        map.put(tags.GIFT_CARD_CARD_DISCOUNT, discount);
        map.put(tags.GIFT_CARD_CARD_DESCRIPTION, description);
        dmrRequest.doPost(urls.getGiftCardInfo(), map, this);
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        Log.d(TAG, jsonObject.toString());
    }

    @Override
    public void onError(VolleyError volleyError) {
        volleyError.printStackTrace();
        Log.e(TAG, volleyError.getMessage());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                break;
            case R.id.save:
                addGiftCard();
                break;
            default:

        }
    }
}
