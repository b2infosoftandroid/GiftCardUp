package com.b2infosoft.giftcardup.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.custom.AlertBox;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ForgotPassword extends AppCompatActivity implements DMRResult{
    private static final String TAG = ForgotPassword.class.getName();
    Active active;
    Validation validation;
    Tags tags;
    Urls urls;
    DMRRequest dmrRequest;

    EditText mail;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        mail = (EditText)findViewById(R.id.forgot_mail);
        submit = (Button)findViewById(R.id.forgot_submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 checkBlank();
            }
        });
    }
    private void init(){
        active = Active.getInstance(this);
        tags = Tags.getInstance();
        urls = Urls.getInstance();
        validation = Validation.getInstance();
        dmrRequest= DMRRequest.getInstance(this,TAG);
    }
    private void checkBlank(){
        String email = mail.getText().toString();
        mail.setError(null);
        if(!validation.isEmail(email)){
            mail.setError("Please Enter Valid Email");
            mail.requestFocus();
            return;
        }

        HashMap<String,String> map = new HashMap<>();
        map.put(tags.USER_ACTION,tags.FORGOT_PASSWORD_REQUEST);
        map.put(tags.USER_ID,email);
        dmrRequest.doPost(urls.getUserInfo(),map,this);
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        Log.d("OTP",jsonObject.toString());
        AlertBox box = new AlertBox(this);
        box.setTitle("Alert");
        try {
            if(jsonObject.has(tags.SUCCESS)){
                if(jsonObject.getInt(tags.SUCCESS) == tags.PASS){
                    if(jsonObject.has(tags.ONE_TIME_PASSWORD)){
                        active.setKey(tags.ONE_TIME_PASSWORD,jsonObject.getString(tags.ONE_TIME_PASSWORD));
                        active.setKey(tags.OTP_ID,jsonObject.getString(tags.OTP_ID));
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Alert");
                        builder.setMessage("OTP send email. Please Check");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                OTPConfirm();
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    }
                }else if(jsonObject.getInt(tags.SUCCESS) == tags.INVALID_EMAIL){
                    box.setMessage("Invalid User");
                    box.show();
                }else if(jsonObject.getInt(tags.SUCCESS) == tags.FAIL){
                    box.setMessage("Something went wrong. Try Again");
                    box.show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private void OTPConfirm(){
        startActivity(new Intent(this,OtpConfirm.class));
    }
}
