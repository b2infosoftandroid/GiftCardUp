package com.b2infosoft.giftcardup.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.custom.AlertBox;
import com.b2infosoft.giftcardup.custom.Progress;
import com.b2infosoft.giftcardup.model.User;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements DMRResult {
    private final static String TAG = Login.class.getName();
    private Urls urls;
    private Tags tags;
    private Active active;
    DMRRequest dmrRequest;
    private Progress progress;
    EditText userName, userPassword;
    Button login_button;
    TextView forgot_password, sign_up;

    /* FACEBOOK INTEGRATION  */
    LoginButton loginButtonFB;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        urls = Urls.getInstance();
        tags = Tags.getInstance();
        active = Active.getInstance(this);

        if (active.isLogin()) {
            loginSuccess();
        }
        progress=new Progress(this);
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
        initFB();
    }

    private void initFB() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        loginButtonFB = (LoginButton) findViewById(R.id.login_button_fb);
        loginButtonFB.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        loginButtonFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            if (object.has("email")) {
                                Map<String, String> map = new HashMap<>();
                                map.put(tags.USER_ACTION, tags.FB_LOGIN_USER);
                                map.put(tags.EMAIL, object.getString("email"));
                                map.put(tags.FB_ID, object.getString("id"));
                                map.put(tags.FIRST_NAME, object.getString("first_name"));
                                map.put(tags.LAST_NAME, object.getString("last_name"));
                                integrateWithFB(map);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("PRINT", e.getMessage());
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,first_name,last_name");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d("SUCCESS CANCEL ", "CANCEL");
            }

            @Override
            public void onError(FacebookException volleyError) {
                volleyError.printStackTrace();
                if (volleyError.getMessage() != null)
                    Log.e(TAG, volleyError.getMessage());
            }
        });
        loginButtonFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedIn()) {
                    Profile profile = Profile.getCurrentProfile();
                } else {
                    Log.d("PROFILE", "LOG OUT");
                }
            }
        });
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    private void loginSuccess() {
        startActivity(new Intent(this, Main.class));
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
        map.put(tags.USER_ACTION, tags.USER_LOGIN);
        map.put(tags.USER_ID, uName);
        map.put(tags.PASSWORD, uPass);
        progress.show();
        dmrRequest.doPost(urls.getUserInfo(), map, this);
    }

    private void integrateWithFB(Map<String, String> map) {
        dmrRequest.doPost(urls.getUserInfo(), map, new DMRResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.has(tags.SUCCESS)) {
                        if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                            if (jsonObject.has(tags.USER_TYPE)) {
                                if (jsonObject.getInt(tags.USER_TYPE) == tags.EXISTING_USER) {
                                    if (jsonObject.has(tags.USER_INFO)) {
                                        JSONObject object = jsonObject.getJSONObject(tags.USER_INFO);
                                        User user = User.fromJSON(object);
                                        active.setUser(user);
                                        active.setLogin();
                                        loginSuccess();
                                    }
                                } else if (jsonObject.getInt(tags.USER_TYPE) == tags.NEW_USER) {
                                    Log.d("WORK", "IN");
                                    if (jsonObject.has(tags.USER_ID)) {
                                        Log.d("WORK", "IN 1");
                                        Intent intent = new Intent(Login.this, FBUserUpdate.class);
                                        intent.putExtra(tags.USER_ID, jsonObject.getString(tags.USER_ID));
                                        startActivity(intent);
                                        finish();
                                    }
                                }
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
                if (volleyError.getMessage() != null)
                    Log.e(TAG, volleyError.getMessage());
            }
        });
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        progress.dismiss();
        try {
            if (jsonObject.has(tags.SUCCESS)) {
                int success = jsonObject.getInt(tags.SUCCESS);
                if (success == tags.PASS) {
                    if (jsonObject.has(tags.USER_INFO)) {
                        JSONObject object = jsonObject.getJSONObject(tags.USER_INFO);
                        User user = User.fromJSON(object);
                        active.setUser(user);
                        active.setLogin();
                        loginSuccess();
                    }
                } else if (success == tags.SUSPEND) {
                    String message = "";
                    if (jsonObject.has(tags.MESSAGE)) {
                        message = jsonObject.getString(tags.MESSAGE);
                    }
                    AlertBox box = new AlertBox(this);
                    box.setTitle("Alert");
                    box.setMessage(message);
                    box.show();
                } else if (success == tags.FAIL) {
                    String message = "";
                    if (jsonObject.has(tags.MESSAGE)) {
                        message = jsonObject.getString(tags.MESSAGE);
                    }
                    AlertBox box = new AlertBox(this);
                    box.setTitle("Alert");
                    box.setMessage(message);
                    box.show();
                }
            }else{
                AlertBox box = new AlertBox(this);
                box.setTitle("Alert");
                box.setMessage("Something went wrong :"+jsonObject);
                box.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onError(VolleyError volleyError) {
        progress.dismiss();
        volleyError.printStackTrace();
        if (volleyError.getMessage() != null)
            Log.e(TAG, volleyError.getMessage());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
