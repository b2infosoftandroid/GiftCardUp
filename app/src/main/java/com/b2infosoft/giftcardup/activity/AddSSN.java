package com.b2infosoft.giftcardup.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Alert;
import com.b2infosoft.giftcardup.app.GiftCardApp;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.custom.AlertBox;
import com.b2infosoft.giftcardup.model.User;
import com.b2infosoft.giftcardup.services.ConnectivityReceiver;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddSSN extends AppCompatActivity implements DMRResult, ConnectivityReceiver.ConnectivityReceiverListener {
    private final static String TAG = AddSSN.class.getName();
    private Active active;
    private Tags tags;
    private Urls urls;
    private DMRRequest dmrRequest;
    private Intent intent;

    private EditText ssn_ein_no;
    private RadioButton id_type_ssn, id_type_ein;
    private RadioGroup radioGroup;
    private Button cancel, save;
    private Alert alert;
    View main_view;

    private void init() {
        tags = Tags.getInstance();
        active = Active.getInstance(this);
        urls = Urls.getInstance();
        dmrRequest = DMRRequest.getInstance(this, TAG);
        intent = new Intent(this, MyProfile.class);
        intent.putExtra(tags.SELECTED_TAB, 2);
        alert = Alert.getInstance(this);
        main_view = findViewById(R.id.main_view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        setContentView(R.layout.activity_add_ssn);
        ssn_ein_no = (EditText) findViewById(R.id.ssn_ein_no);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        id_type_ssn = (RadioButton) findViewById(R.id.id_type_ssn);
        id_type_ein = (RadioButton) findViewById(R.id.id_type_ein);
        cancel = (Button) findViewById(R.id.bank_cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        save = (Button) findViewById(R.id.bank_save_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ssnName = ssn_ein_no.getText().toString();
                String idType = "";
                if (id_type_ssn.isSelected()) {
                    idType = "SSN";
                } else {
                    idType = "EIN";
                }
                if(!active.isLogin())
                    return;
                if(!isConnected()){
                    alert.showSnackIsConnectedView(main_view,isConnected());
                    return;
                }
                Map<String, String> map = new HashMap<String, String>();
                map.put(tags.USER_ACTION, tags.ADD_IDENTIFICATION_SSN);
                map.put(tags.USER_ID, active.getUser().getUserId() + "");
                map.put(tags.SSN_EIN, ssnName);
                map.put(tags.ID_TYPE, idType);

                dmrRequest.doPost(urls.getUserInfo(), map, AddSSN.this);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                this.onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        GiftCardApp.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        alert.showSnackIsConnectedView(main_view, isConnected);
    }
    @Override
    public void onSuccess(JSONObject jsonObject) {
        //Toast.makeText(this,jsonObject.toString(),Toast.LENGTH_SHORT).show();
        try {
            if (jsonObject.has(tags.SUCCESS)) {
                int success = jsonObject.getInt(tags.SUCCESS);
                if (success == tags.PASS) {
                    AlertBox box = new AlertBox(this);
                    box.setMessage("Your Request is Processing");
                    box.show();
                    viewBlank();
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
        if (volleyError.getMessage() != null)
            Log.e(TAG, volleyError.getMessage());
    }

    private void viewBlank() {
        ssn_ein_no.setText("");
        radioGroup.clearCheck();
    }

    private boolean isConnected() {
        return ConnectivityReceiver.isConnected();
    }
}
