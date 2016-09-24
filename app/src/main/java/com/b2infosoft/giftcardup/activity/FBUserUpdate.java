package com.b2infosoft.giftcardup.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.app.Validation;
import com.b2infosoft.giftcardup.database.DBHelper;
import com.b2infosoft.giftcardup.model.State;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FBUserUpdate extends AppCompatActivity implements DMRResult {
    private final String TAG = FBUserUpdate.class.getName();
    EditText email,mobile, password, password_confirm, address, suite_no, city, zip_code, company_name;
    Button update;
    Spinner s1;
    Validation validation;
    Tags tags;
    DMRRequest dmrRequest;
    Urls urls;
    DBHelper dbHelper;
    String user_id;

    private void init() {
        validation = Validation.getInstance();
        tags = Tags.getInstance();
        dmrRequest = DMRRequest.getInstance(this, TAG);
        urls = Urls.getInstance();
        dbHelper = new DBHelper(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fbuser_update);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();

        if (getIntent().hasExtra(tags.USER_ID)) {
            user_id = getIntent().getExtras().getString(tags.USER_ID);
        }

        email = (EditText) findViewById(R.id.sign_up_email);
        mobile = (EditText) findViewById(R.id.sign_up_mobile_number);
        password = (EditText) findViewById(R.id.sign_up_password);
        password_confirm = (EditText) findViewById(R.id.sign_up_password_confirm);
        address = (EditText) findViewById(R.id.sign_up_address);
        suite_no = (EditText) findViewById(R.id.sign_up_suite_number);
        city = (EditText) findViewById(R.id.sign_up_city);
        s1 = (Spinner) findViewById(R.id.sign_up_state);
        zip_code = (EditText) findViewById(R.id.sign_up_zip_code);
        company_name = (EditText) findViewById(R.id.sign_up_company_name);
        update = (Button) findViewById(R.id.sign_up_action_next);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBlank();
            }
        });
        setState();
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

    private void setState() {
        List<String> states = new ArrayList<>();
        states.add("SELECT STATE");
        for (String state : dbHelper.getStateMap().keySet()) {
            states.add(state.toUpperCase(Locale.getDefault()));
        }
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, states);
        s1.setAdapter(adapter);
        /*
        State state = dbHelper.getStateByAbbreviation(abbreviation);
        SpinnerAdapter arrayAdapter = s1.getAdapter();
        for (int i = 0; i < arrayAdapter.getCount(); i++) {
            String item = arrayAdapter.getItem(i).toString();
            if (item.equalsIgnoreCase(state.getName())) {
                s1.setSelection(i);
                break;
            }
        }
        */
    }

    private void checkBlank() {
        String mail_id = email.getText().toString();
        String phone = mobile.getText().toString();
        String passwrd = password.getText().toString();
        String passwrd_confrm = password_confirm.getText().toString();
        String address_1 = address.getText().toString();
        String city_name = city.getText().toString();
        String zip = zip_code.getText().toString();
        String suite = suite_no.getText().toString();
        String company = company_name.getText().toString();
        State state = dbHelper.getStateByName(s1.getSelectedItem().toString());

        email.setError(null);
        mobile.setError(null);
        password.setError(null);
        password_confirm.setError(null);
        address.setError(null);
        city.setError(null);
        zip_code.setError(null);
        TextView selectedTextView = (TextView) s1.getSelectedView();
        selectedTextView.setError(null);

        if (!validation.isEmail(mail_id)) {
            email.setError("Enter Correct Email Id");
            email.requestFocus();
            return;
        }
        if (!validation.isMobileNumber(phone)) {
            mobile.setError("Enter Correct Mobile Number");
            mobile.requestFocus();
            return;
        }
        if (!validation.isPassword(passwrd)) {
            password.setError("Password Should Be Greater Than 5 Digit");
            password.requestFocus();
            return;
        }
        if (!validation.isPasswordConfirm(passwrd, passwrd_confrm)) {
            password_confirm.setError("Both Passwords are Different");
            password_confirm.requestFocus();
            return;
        }
        if (address.length() == 0) {
            address.setError("Please Fill Address");
            address.requestFocus();
            return;
        }
        if (city.length() == 0) {
            city.setError("Please Fill City");
            city.requestFocus();
            return;
        }
        if (s1.getSelectedItemPosition() == 0) {
            selectedTextView.setError("Please Select State");
            s1.requestFocus();
            return;
        }
        if (zip_code.length() == 0) {
            zip_code.setError("Please Fill Zip Code");
            zip_code.requestFocus();
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.FB_PROFILE_UPDATE);
        map.put(tags.USER_ID, user_id);
        map.put(tags.EMAIL, mail_id);
        map.put(tags.PHONE_NUMBER, phone);
        map.put(tags.PASSWORD, passwrd);
        map.put(tags.ADDRESS, address_1);
        map.put(tags.SUITE_NUMBER, suite);
        map.put(tags.CITY, city_name);
        map.put(tags.STATE, state.getAbbreviation());
        map.put(tags.ZIP_CODE, zip);
        map.put(tags.COMPANY_NAME, company);
        dmrRequest.doPost(urls.getUserInfo(), map, this);
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }
}
