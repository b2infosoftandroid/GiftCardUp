package com.b2infosoft.giftcardup.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.database.DBHelper;
import com.b2infosoft.giftcardup.model.ContactInformation;
import com.b2infosoft.giftcardup.model.State;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChangeAddress extends AppCompatActivity implements DMRResult{
    private final String TAG = ChangeAddress.class.getName();
    EditText address,city,zipCode,suiteNo,mobile,company;
    AppCompatSpinner spinner;
    Button update,cancel;
    Tags tags;
    Urls urls;
    DBHelper dbHelper;
    Active active;
    DMRRequest dmrRequest;

    private void init(){
        tags = Tags.getInstance();
        urls = Urls.getInstance();
        dbHelper = new DBHelper(getApplicationContext());
        active = Active.getInstance(getApplicationContext());
        dmrRequest = DMRRequest.getInstance(getApplicationContext(),TAG);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        address = (EditText)findViewById(R.id.address);
        company = (EditText)findViewById(R.id.address_company);
        mobile = (EditText)findViewById(R.id.address_phone_no);
        city = (EditText)findViewById(R.id.address_city);
        zipCode = (EditText)findViewById(R.id.address_zip_code);
        suiteNo = (EditText)findViewById(R.id.address_suite_no);
        spinner = (AppCompatSpinner)findViewById(R.id.address_state_spinner);
        update = (Button)findViewById(R.id.address_update_btn);
        cancel = (Button)findViewById(R.id.address_cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    updateAddress();
            }
        });

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put(tags.USER_ACTION,tags.USER_CONTACT_INFORMATION);
        hashMap.put(tags.USER_ID,active.getUser().getUserId());
        dmrRequest.doPost(urls.getUserInfo(),hashMap,this);

    }

    private void setData(ContactInformation information){
        address.setText(information.getAddress());
        mobile.setText(information.getPhoneNumber());
        city.setText(information.getCity());
        zipCode.setText(information.getZipCode());
        suiteNo.setText(information.getSuiteNumber());
        company.setText(information.getCompanyName());
        setState(information.getState());
    }

    private void setState(String abbreviation){
        List<String> states = new ArrayList<>();
        states.add("SELECT STATE");
        for (String state : dbHelper.getStateMap().keySet()) {
            states.add(state.toUpperCase(Locale.getDefault()));
        }
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, states);
        spinner.setAdapter(adapter);
        State state = dbHelper.getStateByAbbreviation(abbreviation);
        SpinnerAdapter arrayAdapter = spinner.getAdapter();
        for(int i = 0;i<arrayAdapter.getCount();i++) {
            String item = arrayAdapter.getItem(i).toString();
            if(item.equalsIgnoreCase(state.getName())){
                spinner.setSelection(i);
                break;
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                this.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        try {
            if(jsonObject.has(tags.SUCCESS)){
                if(jsonObject.getInt(tags.SUCCESS) == tags.PASS){
                       if(jsonObject.has(tags.USER_CONTACT_INFORMATION)){
                           ContactInformation info = ContactInformation.fromJSON(jsonObject.getJSONObject(tags.USER_CONTACT_INFORMATION));
                           setData(info);
                       }
                }

            }
        }catch (JSONException e){
            e.printStackTrace();
            Log.d(TAG,e.getMessage());
        }

    }

    @Override
    public void onError(VolleyError volleyError) {
        volleyError.printStackTrace();
        Log.d(TAG,volleyError.getMessage());
    }


    private void updateAddress(){
        String address1  = address.getText().toString();
        String add_city  = city.getText().toString();
        String add_zip  = zipCode.getText().toString();
        String add_suite  = suiteNo.getText().toString();
        String add_cmpny  = company.getText().toString();
        String add_phone  = mobile.getText().toString();
        State state = dbHelper.getStateByName(spinner.getSelectedItem().toString());
        Gson gson = new Gson();
        Log.d("GSON",gson.toJson(state));

        Log.d("addState",state.getAbbreviation());

        HashMap<String,String> map = new HashMap<>();
        map.put(tags.USER_ACTION,tags.UPDATE_ADDRESS);
        map.put(tags.USER_ID,active.getUser().getUserId());
        map.put(tags.CONTACT_INFO_CITY,add_city);
        map.put(tags.CONTACT_INFO_ZIP_CODE,add_zip);
        map.put(tags.CONTACT_INFO_STATE,state.getAbbreviation());
        map.put(tags.CONTACT_INFO_COMPANY_NAME,add_cmpny);
        map.put(tags.CONTACT_INFO_PHONE_NUMBER,add_phone);
        map.put(tags.ADDRESS,address1);
        map.put(tags.CONTACT_INFO_SUITE_NUMBER,add_suite);
        dmrRequest.doPost(urls.getUserInfo(),map,this);
    }
}
