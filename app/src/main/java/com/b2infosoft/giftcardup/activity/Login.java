package com.b2infosoft.giftcardup.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.custom.AlertBox;
import com.b2infosoft.giftcardup.model.User;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements DMRResult {
    private final static String TAG = Login.class.getName();
    private Urls urls;
    private Tags tags;
    private Active active;
    DMRRequest dmrRequest;
    EditText userName, userPassword;
    Button login_button;
    TextView forgot_password, sign_up;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        urls = Urls.getInstance();
        tags = Tags.getInstance();
        active = Active.getInstance(this);

        if(active.isLogin()){
            loginSuccess();
        }

        dmrRequest = DMRRequest.getInstance(this, TAG);
        userName = (EditText) findViewById(R.id.username_login);
        userPassword = (EditText) findViewById(R.id.password_login);
        login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
        forgot_password = (TextView) findViewById(R.id.forgot_password_text);
        sign_up = (TextView) findViewById(R.id.sign_up_text);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, SignUp.class));
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ForgotPassword.class));
            }
        });
    }
    private void loginSuccess(){
        startActivity(new Intent(this,Main.class));
        finish();
    }
    private void attemptLogin() {

        String uName = userName.getText().toString();
        String uPass = userPassword.getText().toString();
        userName.setError(null);
        userPassword.setError(null);

        if (uName.length() == 0) {
            userName.setError("Invalid User Name");
            userName.requestFocus();
            return;
        }
        if (uPass.length() == 0) {
            userPassword.setError("Invalid password");
            userPassword.requestFocus();
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_REQUEST, tags.ARR_LOGIN_TYPE[0]);
        map.put(tags.USER_ID, uName);
        map.put(tags.PASSWORD, uPass);

        dmrRequest.doPost(urls.getUrlLogin(), map, this);
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        try {
            if (jsonObject.has(tags.SUCCESS)) {
                int success = jsonObject.getInt(tags.SUCCESS);
                if (success == tags.PASS) {
                    if(jsonObject.has(tags.USER_INFO)){
                        JSONObject object = jsonObject.getJSONObject(tags.USER_INFO);
                        User user = User.fromJSON(object);
                        active.setKey(tags.USER_ID,user.getUserId()+"");
                        active.setKey(tags.USER_TYPE,user.getUserType()+"");
                        active.setLogin();
                        loginSuccess();
                    }
                } else if (success == tags.SUSPEND) {
                    String message = "" ;
                    if(jsonObject.has(tags.MESSAGE)){
                        message = jsonObject.getString(tags.MESSAGE);
                    }
                    AlertBox box = new AlertBox(this);
                    box.setTitle("Alert");
                    box.setMessage(message);
                    box.show();
                } else if (success == tags.FAIL) {
                    String message = "" ;
                    if(jsonObject.has(tags.MESSAGE)){
                        message = jsonObject.getString(tags.MESSAGE);
                    }
                    AlertBox box = new AlertBox(this);
                    box.setTitle("Alert");
                    box.setMessage(message);
                    box.show();
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
}
