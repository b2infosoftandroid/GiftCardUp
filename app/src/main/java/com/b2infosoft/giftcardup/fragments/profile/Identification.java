package com.b2infosoft.giftcardup.fragments.profile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SpinnerAdapter;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.database.DBHelper;
import com.b2infosoft.giftcardup.model.ContactInformation;
import com.b2infosoft.giftcardup.model.State;
import com.b2infosoft.giftcardup.model.User;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ru.noties.scrollable.CanScrollVerticallyDelegate;

public class Identification extends Fragment implements CanScrollVerticallyDelegate, DMRResult {
    private final String TAG = Identification.class.getName();
    private Active active;
    private Tags tags;
    private DMRRequest dmrRequest;
    private DBHelper dbHelper;
    private Urls urls;
    EditText f_name, l_name, email, mobile, city, state, zip_code, cmpny_name, paypal_id, address, suite_no;
    Button b1,b2;
    private AppCompatSpinner appCompatSpinner;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentIdentification mListener;

    public Identification() {
    }

    private void init() {
        active = Active.getInstance(getActivity());
        tags = Tags.getInstance();
        dmrRequest = DMRRequest.getInstance(getActivity(), TAG);
        urls = Urls.getInstance();
        dbHelper = new DBHelper(getActivity());
    }

    public static Identification newInstance(String param1, String param2) {
        Identification fragment = new Identification();
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
        View view = inflater.inflate(R.layout.fragment_identification, container, false);
        f_name = (EditText) view.findViewById(R.id.identity_f_name);
        l_name = (EditText) view.findViewById(R.id.identity_l_name);
        email = (EditText) view.findViewById(R.id.identity_email);
        mobile = (EditText) view.findViewById(R.id.identity_phone);
        address = (EditText) view.findViewById(R.id.identity_address);
        suite_no = (EditText) view.findViewById(R.id.identity_suite_no);
        city = (EditText) view.findViewById(R.id.identity_city);
        zip_code = (EditText) view.findViewById(R.id.identity_zip_code);
        cmpny_name = (EditText) view.findViewById(R.id.identity_cmpny_name);
        paypal_id = (EditText) view.findViewById(R.id.identity_paypal_id);
        appCompatSpinner = (AppCompatSpinner) view.findViewById(R.id.identity_state_spinner);
        b1 = (Button)view.findViewById(R.id.edit_save_btn);
        b2 = (Button)view.findViewById(R.id.save_btn);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b2.setVisibility(View.VISIBLE);
                b1.setVisibility(View.GONE);
                enableProfile(true);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String paypal = paypal_id.getText().toString();
                paypal_id.setError(null);
                if(appCompatSpinner.getSelectedItemPosition()==0){
                    appCompatSpinner.requestFocus();
                    return;
                }
                if(paypal_id.length() == 0){
                    paypal_id.setError("PLEASE ENTER ID");
                    paypal_id.setFocusable(true);
                    return;
                }

                updateProfile();
            }
        });

        enableProfile(false);
        setProfile();
        fetchContactInfo();
        return view;
    }

    private void updateProfile(){

        State state =dbHelper.getStateByName(appCompatSpinner.getSelectedItem().toString());
        User user = active.getUser();
        Map<String,String> map = new HashMap<>();
        map.put(tags.USER_ACTION,tags.USER_PROFILE_UPDATE);
        map.put(tags.FIRST_NAME,f_name.getText().toString());
        map.put(tags.LAST_NAME,l_name.getText().toString());
        map.put(tags.PHONE_NUMBER,mobile.getText().toString());
        map.put(tags.ADDRESS,address.getText().toString());
        map.put(tags.SUITE_NUMBER,suite_no.getText().toString());
        map.put(tags.COMPANY_NAME,cmpny_name.getText().toString());
        map.put(tags.ZIP_CODE,zip_code.getText().toString());
        map.put(tags.PAY_PAL_ID,paypal_id.getText().toString());
        map.put(tags.CITY,city.getText().toString());
        map.put(tags.EMPLOYEE_ID,user.getEmployeeId() + "");
        map.put(tags.USER_ID,user.getUserId() + "");
        map.put(tags.STATE,state.getAbbreviation());
        dmrRequest.doPost(urls.getUserInfo(),map,this);
    }

    private void fetchContactInfo() {
        if (active.isLogin()) {
            User user = active.getUser();

            /* LOADING USER DETAILS */
            Map<String, String> map1 = new HashMap<>();
            map1.put(tags.USER_ACTION, tags.USER_CONTACT_INFORMATION);
            map1.put(tags.USER_ID, user.getUserId() + "");
            dmrRequest.doPost(urls.getUserInfo(), map1, this);
        }
    }

    private void enableProfile(boolean isUpdate) {
        f_name.setEnabled(isUpdate);
        l_name.setEnabled(isUpdate);
        mobile.setEnabled(isUpdate);
        address.setEnabled(isUpdate);
        suite_no.setEnabled(isUpdate);
        city.setEnabled(isUpdate);
        zip_code.setEnabled(isUpdate);
        cmpny_name.setEnabled(isUpdate);
        paypal_id.setEnabled(isUpdate);
        appCompatSpinner.setEnabled(isUpdate);
    }

    private void setProfile() {
        if (active.isLogin()) {
            User user = active.getUser();
            Gson gson = new Gson();
            f_name.setText(user.getFirstName());
            l_name.setText(user.getLastName());
            email.setText(user.getEmail());
            paypal_id.setText(user.getPayPalId());
        }
    }

    private void refreshContactInformation(ContactInformation information) {
        mobile.setText(information.getPhoneNumber());
        address.setText(information.getAddress());
        suite_no.setText(information.getSuiteNumber());
        city.setText(information.getCity());
        zip_code.setText(information.getZipCode());
        cmpny_name.setText(information.getCompanyName());
        setStates(information.getState());
    }

    private void setStates(String abbreviation) {
        List<String> states = new ArrayList<>();
        states.add("SELECT STATE");
        for (String state : dbHelper.getStateMap().keySet()) {
            states.add(state.toUpperCase(Locale.getDefault()));
        }
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, states);
        appCompatSpinner.setAdapter(adapter);
        State state = dbHelper.getStateByAbbreviation(abbreviation);
        SpinnerAdapter arrayAdapter = appCompatSpinner.getAdapter();
        for(int i = 0;i<arrayAdapter.getCount();i++) {
            String item = arrayAdapter.getItem(i).toString();
            if(item.equalsIgnoreCase(state.getName())){
                appCompatSpinner.setSelection(i);
                break;
            }
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onIdentification(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentIdentification) {
            mListener = (OnFragmentIdentification) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentIdentification");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return true;
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        Log.d(TAG, jsonObject.toString());
        try {
            if (jsonObject.has(tags.SUCCESS)) {
                if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                    if (jsonObject.has(tags.USER_CONTACT_INFORMATION)) {
                        ContactInformation information = ContactInformation.fromJSON(jsonObject.getJSONObject(tags.USER_CONTACT_INFORMATION));
                        refreshContactInformation(information);
                    }
                    if(jsonObject.has(tags.USER_PROFILE_UPDATE)){
                       User user = User.fromJSON(jsonObject.getJSONObject(tags.USER_PROFILE_UPDATE));
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

    public interface OnFragmentIdentification {
        void onIdentification(Uri uri);
    }
}
