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
                                Profile profile = Profile.getCurrentProfile();
                                Map<String, String> map = new HashMap<>();
                                map.put(tags.USER_ACTION, tags.FB_LOGIN_USER);
                                map.put(tags.EMAIL, object.getString("email"));
                                map.put(tags.FB_ID, profile.getId());
                                map.put(tags.FIRST_NAME, profile.getFirstName());
                                map.put(tags.LAST_NAME, profile.getLastName());
                                integrateWithFB(map);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("PRINT", e.getMessage());
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d("SUCCESS CANCEL ", "CANCEL");
            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
                Log.d("Error ", error.getMessage());
            }
        });
        loginButtonFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedIn()) {
                    Log.d("PROFILE", "LOG IN");
                    Profile profile = Profile.getCurrentProfile();
                    Log.d("PROFILE NAME", profile.getFirstName() + " " + profile.getLastName());
                    Log.d("PROFILE ID", profile.getId());

                    Gson gson = new Gson();
                    Log.d("PROFILE", gson.toJson(profile));

                } else {
                    Log.d("PROFILE", "LOG OUT");
                }
            }
        });
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Gson gson = new Gson();
        Log.d("PROFILE TOKENS", gson.toJson(accessToken));
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
                Log.e(TAG, volleyError.getMessage());
            }
        });
    }


    @Override
    public void onSuccess(JSONObject jsonObject) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
