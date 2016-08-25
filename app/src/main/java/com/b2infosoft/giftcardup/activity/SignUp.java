package com.b2infosoft.giftcardup.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.app.Validation;
import com.b2infosoft.giftcardup.model.User;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity implements View.OnClickListener,DMRResult {

    private final String TAG = SignUp.class.getName();
    EditText f_name,l_name,email,mobile,password,password_confirm;
    EditText address,suite_no,city,state,zip_code,company_name;
    Button next,back,create_acct;
    LinearLayout layout1,layout2;
    Tags tags;
    DMRRequest dmrRequest;
    Urls urls;
    Validation validation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tags = Tags.getInstance();
        urls = Urls.getInstance();
        dmrRequest = DMRRequest.getInstance(this,TAG);
        f_name = (EditText)findViewById(R.id.sign_up_name_first);
        l_name = (EditText)findViewById(R.id.sign_up_name_last);
        email = (EditText)findViewById(R.id.sign_up_email);
        mobile = (EditText)findViewById(R.id.sign_up_mobile_number);
        password = (EditText)findViewById(R.id.sign_up_password);
        password_confirm = (EditText)findViewById(R.id.sign_up_password_confirm);
        next = (Button)findViewById(R.id.sign_up_action_next);
        next.setOnClickListener(this);
        layout2 = (LinearLayout)findViewById(R.id.sign_up_ViewSecond);
        layout1 = (LinearLayout)findViewById(R.id.sign_up_ViewFirst);

        address = (EditText)findViewById(R.id.sign_up_address);
        suite_no = (EditText)findViewById(R.id.sign_up_suite_number);
        city = (EditText)findViewById(R.id.sign_up_city);
        state = (EditText)findViewById(R.id.sign_up_state);
        zip_code = (EditText)findViewById(R.id.sign_up_zip_code);
        company_name = (EditText)findViewById(R.id.sign_up_company_name);
        back = (Button)findViewById(R.id.sign_up_action_back);
        create_acct = (Button)findViewById(R.id.sign_up_action_create_account);
        back.setOnClickListener(this);
        create_acct.setOnClickListener(this);

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

    private void checkNext(){
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

        if(f_name.length() == 0){
            f_name.setError("Please Enter Your Name");
            f_name.requestFocus();
            return;
        }
        if(l_name.length() == 0){
            l_name.setError("Please Enter Last Name");
            l_name.requestFocus();
            return;
        }
        if(!validation.isEmail(mail_id)){
            email.setError("Enter Correct Email Id");
            email.requestFocus();
            return;
        }
        if(!validation.isMobileNumber(phone)){
            mobile.setError("Enter Correct Mobile Number");
            mobile.requestFocus();
            return;
        }
        if(!validation.isPassword(passwrd)){
            password.setError("Password Should Be Greater Than 5 Digit");
            password.requestFocus();
            return;
        }
        if(!validation.isPasswordConfirm(passwrd,passwrd_confrm)){
            password_confirm.setError("Both Passwords are Different");
            password_confirm.requestFocus();
            return;
        }
        layout2.setVisibility(View.VISIBLE);
        layout1.setVisibility(View.GONE);

    }

    private void checkSignUp(){
        String addrss = address.getText().toString();
        String city_name = city.getText().toString();
        String zip = zip_code.getText().toString();
        String stat = state.getText().toString();

        address.setError(null);
        city.setError(null);
        state.setError(null);
        zip_code.setError(null);

        if(address.length() == 0){
            address.setError("Please Fill Address");
            address.requestFocus();
            return;
        }
        if(city.length() == 0){
            city.setError("Please Fill City");
            city.requestFocus();
            return;
        }
        if(state.length() == 0){
            state.setError("Please Select State");
            state.requestFocus();
            return;
        }
        if(zip_code.length() == 0){
            zip_code.setError("Please Fill Zip Code");
            zip_code.requestFocus();
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.USER_SIGNUP);
        map.put(tags.EMPLOYEE_ID,"1");
        map.put(tags.FIRST_NAME,f_name.getText().toString());
        map.put(tags.LAST_NAME, l_name.getText().toString());
        map.put(tags.EMAIL,email.getText().toString());
        map.put(tags.PHONE_NUMBER, mobile.getText().toString());
        map.put(tags.PASSWORD,password.getText().toString());
        map.put(tags.ADDRESS, addrss);
        map.put(tags.SUITE_NUMBER, suite_no.getText().toString());
        map.put(tags.CITY,city_name);
        map.put(tags.STATE,stat);
        map.put(tags.ZIP_CODE, zip);
        map.put(tags.COMPANY_NAME, company_name.getText().toString());
        dmrRequest.doPost(urls.getUserInfo(), map, this);
    }

    private void signUpSuccess(){
       startActivity(new Intent(this,Main.class));
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        Log.d("success",jsonObject.toString());
           try {
               if (jsonObject.has(tags.SUCCESS)) {
                   if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                       if (jsonObject.has(tags.USER_SIGNUP)) {
                           User user = User.fromJSON(jsonObject.getJSONObject(tags.USER_SIGNUP));
                           Toast.makeText(this,"success",Toast.LENGTH_SHORT).show();
                           signUpSuccess();

                       }
                   } else if (jsonObject.getInt(tags.SUCCESS) == tags.FAIL) {

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
}
