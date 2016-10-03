package com.b2infosoft.giftcardup.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.app.Validation;
import com.b2infosoft.giftcardup.custom.AlertBox;
import com.b2infosoft.giftcardup.database.DBHelper;
import com.b2infosoft.giftcardup.model.State;
import com.b2infosoft.giftcardup.model.User;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SignUp extends AppCompatActivity implements View.OnClickListener, DMRResult {

    private final String TAG = SignUp.class.getName();
    EditText f_name, l_name, email, mobile, password, password_confirm;
    EditText address, suite_no, city, state, zip_code, company_name;
    Button next, back, create_acct;
    Spinner s1;
    LinearLayout layout1, layout2;
    Tags tags;
    DMRRequest dmrRequest;
    Urls urls;
    Validation validation;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tags = Tags.getInstance();
        urls = Urls.getInstance();
        dbHelper = new DBHelper(this);
        dmrRequest = DMRRequest.getInstance(this, TAG);
        f_name = (EditText) findViewById(R.id.sign_up_name_first);
        l_name = (EditText) findViewById(R.id.sign_up_name_last);
        email = (EditText) findViewById(R.id.sign_up_email);
        mobile = (EditText) findViewById(R.id.sign_up_mobile_number);
        password = (EditText) findViewById(R.id.sign_up_password);
        password_confirm = (EditText) findViewById(R.id.sign_up_password_confirm);
        next = (Button) findViewById(R.id.sign_up_action_next);
        next.setOnClickListener(this);
        layout2 = (LinearLayout) findViewById(R.id.sign_up_ViewSecond);
        layout1 = (LinearLayout) findViewById(R.id.sign_up_ViewFirst);

        address = (EditText) findViewById(R.id.sign_up_address);
        suite_no = (EditText) findViewById(R.id.sign_up_suite_number);
        city = (EditText) findViewById(R.id.sign_up_city);
        //state = (EditText)findViewById(R.id.sign_up_state);
        s1 = (Spinner) findViewById(R.id.sign_up_state);
        zip_code = (EditText) findViewById(R.id.sign_up_zip_code);
        company_name = (EditText) findViewById(R.id.sign_up_company_name);
        back = (Button) findViewById(R.id.sign_up_action_back);
        create_acct = (Button) findViewById(R.id.sign_up_action_create_account);
        back.setOnClickListener(this);
        create_acct.setOnClickListener(this);

        setState();

    }

    private void setState() {
        List<String> states = new ArrayList<>();
        states.add("SELECT STATE");
        for (String state : dbHelper.getStateMap().keySet()) {
            states.add(state.toUpperCase(Locale.getDefault()));
        }
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, states);
        s1.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                this.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up_action_next:
                checkNext();
                break;
            case R.id.sign_up_action_back:
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.GONE);
                break;
            case R.id.sign_up_action_create_account:
                checkSignUp();
                break;
            default:
                break;
        }
    }

    private void checkNext() {
        validation = Validation.getInstance();
        String first_name = f_name.getText().toString();
        String last_name = l_name.getText().toString();
        String mail_id = email.getText().toString();
        String phone = mobile.getText().toString();
        String passwrd = password.getText().toString();
        String passwrd_confrm = password_confirm.getText().toString();

        f_name.setError(null);
        l_name.setError(null);
        email.setError(null);
        mobile.setError(null);
        password.setError(null);
        password_confirm.setError(null);

        if (f_name.length() == 0) {
            f_name.setError("Please Enter Your Name");
            f_name.requestFocus();
            return;
        }
        if (l_name.length() == 0) {
            l_name.setError("Please Enter Last Name");
            l_name.requestFocus();
            return;
        }
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
        layout2.setVisibility(View.VISIBLE);
        layout1.setVisibility(View.GONE);
    }

    private void checkSignUp() {
        String first_name = f_name.getText().toString();
        String last_name = l_name.getText().toString();
        String mail_id = email.getText().toString();
        String phone = mobile.getText().toString();
        String passwrd = password.getText().toString();

        String address_1 = address.getText().toString();
        String city_name = city.getText().toString();
        String zip = zip_code.getText().toString();
        String suite = suite_no.getText().toString();
        String company = company_name.getText().toString();
        State state = dbHelper.getStateByName(s1.getSelectedItem().toString());

        address.setError(null);
        city.setError(null);
        zip_code.setError(null);
        TextView selectedTextView = (TextView) s1.getSelectedView();
        selectedTextView.setError(null);
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
        map.put(tags.USER_ACTION, tags.USER_SIGNUP);
        map.put(tags.EMPLOYEE_ID, "1");
        map.put(tags.FIRST_NAME, first_name);
        map.put(tags.LAST_NAME, last_name);
        map.put(tags.EMAIL, mail_id);
        map.put(tags.PHONE_NUMBER, phone);
        map.put(tags.PASSWORD, passwrd);
        map.put(tags.ADDRESS, address_1);
        if (suite == null) {
            map.put(tags.SUITE_NUMBER, "");
        } else {
            map.put(tags.SUITE_NUMBER, suite);
        }
        map.put(tags.CITY, city_name);
        map.put(tags.STATE, state.getAbbreviation());
        map.put(tags.ZIP_CODE, zip);
        if (company == null) {
            map.put(tags.COMPANY_NAME, "");
        } else {
            map.put(tags.COMPANY_NAME, company);
        }
        dmrRequest.doPost(urls.getUserInfo(), map, this);
    }

    private void signUpSuccess() {
        startActivity(new Intent(this, Main.class));
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        Log.d("checkuser", jsonObject.toString());
        try {
            if (jsonObject.has(tags.SUCCESS)) {
                if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Successfully Registered");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            signUpSuccess();
                        }
                    });
                    builder.create().show();

                } else if (jsonObject.getInt(tags.SUCCESS) == tags.FAIL) {

                } else if (jsonObject.getInt(tags.SUCCESS) == tags.EXISTING_USER) {
                    AlertBox alertBox = new AlertBox(this);
                    alertBox.setTitle("Alert");
                    alertBox.setMessage("User Already Exist");
                    alertBox.show();

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onError(VolleyError volleyError) {
        volleyError.printStackTrace();
        if (volleyError.getMessage() != null)
            Log.e(TAG,volleyError.getMessage());
    }


}
