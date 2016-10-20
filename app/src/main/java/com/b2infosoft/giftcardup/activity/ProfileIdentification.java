package com.b2infosoft.giftcardup.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SpinnerAdapter;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Alert;
import com.b2infosoft.giftcardup.app.GiftCardApp;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.custom.AlertBox;
import com.b2infosoft.giftcardup.database.DBHelper;
import com.b2infosoft.giftcardup.model.ContactInformation;
import com.b2infosoft.giftcardup.model.State;
import com.b2infosoft.giftcardup.model.User;
import com.b2infosoft.giftcardup.services.ConnectivityReceiver;
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

public class ProfileIdentification extends AppCompatActivity implements DMRResult, ConnectivityReceiver.ConnectivityReceiverListener {
    private final String TAG = ProfileIdentification.class.getName();
    private Active active;
    private Tags tags;
    private DMRRequest dmrRequest;
    private DBHelper dbHelper;
    private Urls urls;
    AlertBox box;
    EditText f_name, l_name, email, mobile, city, zip_code, cmpny_name, paypal_id, address, suite_no;
    Button b2;
    private AppCompatSpinner appCompatSpinner;
    private Alert alert;
    View main_view;
    private void init() {
        active = Active.getInstance(this);
        tags = Tags.getInstance();
        dmrRequest = DMRRequest.getInstance(this, TAG);
        urls = Urls.getInstance();
        dbHelper = new DBHelper(this);
        box = new AlertBox(this);
        alert = Alert.getInstance(this);
        main_view = findViewById(R.id.main_view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification);
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        f_name = (EditText)findViewById(R.id.identity_f_name);
        l_name = (EditText)findViewById(R.id.identity_l_name);
        email = (EditText)findViewById(R.id.identity_email);
        mobile = (EditText)findViewById(R.id.identity_phone);
        address = (EditText)findViewById(R.id.identity_address);
        suite_no = (EditText)findViewById(R.id.identity_suite_no);
        city = (EditText)findViewById(R.id.identity_city);
        zip_code = (EditText)findViewById(R.id.identity_zip_code);
        cmpny_name = (EditText)findViewById(R.id.identity_cmpny_name);
        paypal_id = (EditText)findViewById(R.id.identity_paypal_id);
        appCompatSpinner = (AppCompatSpinner)findViewById(R.id.identity_state_spinner);
        b2 = (Button)findViewById(R.id.save_btn);
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
                    paypal_id.requestFocus();
                    return;
                }

                updateProfile();
                b2.setVisibility(View.GONE);
                //enableProfile(false);
            }
        });

        enableProfile(false);
        setProfile();
        fetchContactInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_identification,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        GiftCardApp.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        alert.showSnackIsConnectedView(main_view, isConnected);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                this.onBackPressed();
                return true;
            case R.id.action_edit:
                b2.setVisibility(View.VISIBLE);
                enableProfile(true);
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchContactInfo() {
            if(!active.isLogin())
                return;
            if(!isConnected()){
                alert.showSnackIsConnectedView(main_view,isConnected());
                return;
            }
            /* LOADING USER DETAILS */
            Map<String, String> map1 = new HashMap<>();
            map1.put(tags.USER_ACTION, tags.USER_CONTACT_INFORMATION);
            map1.put(tags.USER_ID, active.getUser().getUserId());
            dmrRequest.doPost(urls.getUserInfo(), map1, this);
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


    private void updateProfile(){
        if(!active.isLogin())
            return;
        if(!isConnected()){
            alert.showSnackIsConnectedView(main_view,isConnected());
            return;
        }
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
        map.put(tags.USER_ID,user.getUserId());
        map.put(tags.STATE,state.getAbbreviation());
        dmrRequest.doPost(urls.getUserInfo(), map, new DMRResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.has(tags.SUCCESS)) {
                        if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                            box.setMessage("Successfully Updated");
                            box.show();
                            enableProfile(false);

                        }else if (jsonObject.getInt(tags.SUCCESS) == tags.FAIL) {
                            box.setMessage("Something went wrong. Try Again");
                            box.show();
                        }
                    }
                }catch (JSONException e){
                    box.setMessage("Something went wrong. Try Again");
                    box.show();
                }
            }

            @Override
            public void onError(VolleyError volleyError) {

            }
        });
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
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, states);
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


    @Override
    public void onSuccess(JSONObject jsonObject) {
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
    private boolean isConnected() {
        return ConnectivityReceiver.isConnected();
    }
}
