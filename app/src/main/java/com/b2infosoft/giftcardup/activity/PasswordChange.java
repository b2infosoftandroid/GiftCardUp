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
import com.b2infosoft.giftcardup.services.ConnectivityReceiver;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PasswordChange extends AppCompatActivity implements View.OnClickListener, DMRResult {
    private static final String TAG = PasswordChange.class.getName();
    Active active;
    Validation validation;
    Tags tags;
    Urls urls;
    DMRRequest dmrRequest;
    EditText password, password_confirm;
    Button action_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.activity_password_change);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initUI();
    }

    private void init() {
        active = Active.getInstance(this);
        tags = Tags.getInstance();
        urls = Urls.getInstance();
        validation = Validation.getInstance();
        dmrRequest = DMRRequest.getInstance(this, TAG);
    }

    private void initUI() {
        password = (EditText) findViewById(R.id.password);
        password_confirm = (EditText) findViewById(R.id.password_confirm);
        action_change = (Button) findViewById(R.id.action_change);
        action_change.setOnClickListener(this);
    }

    private void changePassword() {
        String mPassword = password.getText().toString();
        String mPasswordConfirm = password_confirm.getText().toString();
        password.setError(null);
        password_confirm.setError(null);
        if (!validation.isPassword(mPassword)) {
            password.setError("Password Should Be Greater Than 5 Digit");
            password.requestFocus();
            return;
        }
        if (!validation.isPassword(mPasswordConfirm)) {
            password_confirm.setError("Password Should Be Greater Than 5 Digit");
            password_confirm.requestFocus();
            return;
        }
        if (!validation.isPasswordConfirm(mPassword, mPasswordConfirm)) {
            password_confirm.setError("Both Passwords are Different");
            password_confirm.requestFocus();
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.CHANGE_PASSWORD);
        map.put(tags.USER_ID, active.getValue(tags.OTP_ID));
        map.put(tags.PASSWORD, mPassword);
        dmrRequest.doPost(urls.getUserInfo(), map, this);
    }

    @Override
    public void onClick(View v) {
        if (R.id.action_change == v.getId()) {
            changePassword();
        }
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        Log.d("checkuser", jsonObject.toString());
        try {
            if (jsonObject.has(tags.SUCCESS)) {
                if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Successfully Change Password");
                    builder.setPositiveButton("GO LOGIN", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            goLogin();
                        }
                    });
                    builder.create().show();
                } else if (jsonObject.getInt(tags.SUCCESS) == tags.FAIL) {
                    AlertBox alertBox = new AlertBox(this);
                    alertBox.setTitle("Alert");
                    alertBox.setMessage("Something wrong");
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
            Log.e(TAG, volleyError.getMessage());
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
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private void goLogin() {
        startActivity(new Intent(this, Login.class));
        finish();
    }

    private boolean isConnected() {
        return ConnectivityReceiver.isConnected();
    }
}
