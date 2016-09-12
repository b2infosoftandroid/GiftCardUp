package com.b2infosoft.giftcardup.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.b2infosoft.giftcardup.model.Merchant;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;
import com.libaml.android.view.chip.ChipLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SpeedySell extends Fragment implements TextWatcher, View.OnClickListener ,DMRResult{

    private final String TAG = SpeedySell.class.getName();

    private Urls urls;
    private Tags tags;
    private Active active;
    private Format format;
    DMRRequest dmrRequest;
    Map<String, Merchant> hashMap;
    private Progress progress;

    ChipLayout chipLayout;
    AutoCompleteTextView brand_name;
    ImageView cardType;
    TextView textView;
    EditText serial_number, card_pin, card_balance, selling_percentage, your_earning;
    Button cancel, save;
    boolean itemSelected = false;
    private final String ERROR_MESSAGE_ASKING_PRICE = "Must be less than Balance";
    private final String ERROR_MESSAGE_SELLING_PERCENTAGE = "Must be 0.0 < 100.00 %";
    private Merchant merchant;



    private View mView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentSpeedyCell mListener;

    public SpeedySell() {
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

    public static SpeedySell newInstance(String param1, String param2) {
        SpeedySell fragment = new SpeedySell();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init();
        mView = inflater.inflate(R.layout.fragment_speedy_sell, container, false);
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
        selling_percentage.setFilters(new InputFilter[]{new EditTextMaxFloat(selling_percentage, 100f, ERROR_MESSAGE_SELLING_PERCENTAGE)});
        your_earning = (EditText) mView.findViewById(R.id.your_earning);
        your_earning.setEnabled(false);
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
                                    hashMap.put(merchant.getCompanyID(), merchant);
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

        serial_number.setText(null);
        card_pin.setText(null);
        card_balance.setText(null);
        selling_percentage.setText(null);
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

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSpeedyCell(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentSpeedyCell) {
            mListener = (OnFragmentSpeedyCell) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
            String bb = card_balance.getText().toString();
            String ss = selling_percentage.getText().toString();
            if (!TextUtils.isEmpty(bb)) {
                float bal = Float.parseFloat(bb);
                if (!TextUtils.isEmpty(ss)) {
                    if (ss.contains("%")) {
                        ss = ss.replaceAll("%", "");
                    }
                    float sell = Float.parseFloat(ss);
                    float price = bal * sell / 100;
                    your_earning.setText(format.getStringFloat(price));
                }
            }else{
                your_earning.setText(null);
            }
        }
    }
    private void addSpeedyGiftCard() {
        String company_id = merchant.getCompanyID();
        String card_name = merchant.getCompanyName();
        String serial_no = serial_number.getText().toString();
        String pin = card_pin.getText().toString();
        String balance = card_balance.getText().toString();
        String earning = your_earning.getText().toString();

        String selling_per = selling_percentage.getText().toString();
        String description = "";

        serial_number.setError(null);
        card_pin.setError(null);
        card_balance.setError(null);
        selling_percentage.setError(null);
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

        if (TextUtils.isEmpty(earning)) {
            your_earning.setError("Please Fill Card Your Earning");
            your_earning.requestFocus();
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.ADD_SPEEDY_GIFT_CARD);
        map.put(tags.USER_ID, active.getUser().getUserId() + "");
        map.put(tags.COMPANY_ID, company_id);
        map.put(tags.GIFT_CARD_CARD_NAME, card_name);
        map.put(tags.GIFT_CARD_SERIAL_NUMBER, serial_no);
        map.put(tags.GIFT_CARD_CARD_PIN, pin);
        map.put(tags.GIFT_CARD_CARD_PRICE, balance);
        map.put(tags.GIFT_CARD_YOUR_EARNING, earning);
        map.put(tags.GIFT_CARD_SELLING_PERCENTAGE, selling_per);
        progress.show();
        dmrRequest.doPost(urls.getGiftCardInfo(), map, this);
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        progress.dismiss();
        brand_name.requestFocus();
        Log.d(TAG, jsonObject.toString());
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
                addSpeedyGiftCard();
                break;
            default:
        }
    }
    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
    public interface OnFragmentSpeedyCell {
        void onSpeedyCell(Uri uri);
    }
}
