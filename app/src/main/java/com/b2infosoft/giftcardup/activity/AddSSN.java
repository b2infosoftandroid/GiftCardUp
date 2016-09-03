package com.b2infosoft.giftcardup.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddSSN extends AppCompatActivity  implements DMRResult{
    private final static String TAG = AddSSN.class.getName();
    private Active active;
    private Tags tags;
    private Urls urls;
    private DMRRequest dmrRequest;
    private Intent intent;

    private EditText ssn_ein_no;
    private RadioButton id_type_ssn,id_type_ein;
    private Button cancel,save;
    private void init() {
        tags = Tags.getInstance();
        active = Active.getInstance(getApplicationContext());
        urls = Urls.getInstance();
        dmrRequest = DMRRequest.getInstance(this,TAG);
        intent = new Intent(this, MyProfile.class);
        intent.putExtra(tags.SELECTED_TAB, 2);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.activity_add_ssn);
        ssn_ein_no = (EditText)findViewById(R.id.ssn_ein_no);
        id_type_ssn = (RadioButton)findViewById(R.id.id_type_ssn);
        id_type_ein = (RadioButton)findViewById(R.id.id_type_ein);
        cancel = (Button)findViewById(R.id.bank_cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        save = (Button)findViewById(R.id.bank_save_btn);
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
                Map<String,String> map = new HashMap<String, String>();
                map.put(tags.USER_ACTION,tags.ADD_IDENTIFICATION_SSN);
                map.put(tags.USER_ID, active.getUser().getUserId() + "");
                map.put(tags.SSN_EIN, ssnName);
                map.put(tags.ID_TYPE, idType);

                dmrRequest.doPost(urls.getUserInfo(),map,AddSSN.this);
            }
        });
    }
    @Override
    public void onSuccess(JSONObject jsonObject) {
        Toast.makeText(this,jsonObject.toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(VolleyError volleyError) {

    }
}
