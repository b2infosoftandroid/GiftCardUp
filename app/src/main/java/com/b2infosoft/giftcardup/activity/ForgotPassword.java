package com.b2infosoft.giftcardup.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.app.Validation;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ForgotPassword extends AppCompatActivity implements DMRResult{
    private static final String TAG = ForgotPassword.class.getName();
    Validation validation;
    Tags tags;
    Urls urls;
    DMRRequest dmrRequest;

    EditText mail,mobile;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tags = Tags.getInstance();
        urls = Urls.getInstance();
        validation = Validation.getInstance();

        mail = (EditText)findViewById(R.id.forgot_mail);
        mobile = (EditText)findViewById(R.id.forgot_mobile);
        submit = (Button)findViewById(R.id.forgot_submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 checkBlank();
            }
        });
    }

    private void checkBlank(){
        String email = mail.getText().toString();
        String phone = mobile.getText().toString();

        mail.setError(null);
        mobile.setError(null);

        if(!validation.isEmail(email)){
            mail.setError("Please Enter Valid Email");
            mail.requestFocus();
            return;
        }
        if(!validation.isMobileNumber(phone)){
            mobile.setError("Mobile No. Should be of 10 Digits");
            mobile.requestFocus();
            return;
        }

        HashMap<String,String> map = new HashMap<>();
        map.put(tags.USER_ACTION,"");
        map.put(tags.USER_ACTION,email);
        map.put(tags.USER_ACTION,phone);
        dmrRequest.doPost(urls.getUserInfo(),map,this);

    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        try {
            if(jsonObject.has(tags.SUCCESS)){
                if(jsonObject.getInt(tags.SUCCESS) == tags.PASS){

                }else if(jsonObject.getInt(tags.SUCCESS) == tags.FAIL){

                }
            }
        }catch (JSONException e){

        }

    }

    @Override
    public void onError(VolleyError volleyError) {
        volleyError.printStackTrace();
        if (volleyError.getMessage() != null)
            Log.e(TAG, volleyError.getMessage());
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
}
