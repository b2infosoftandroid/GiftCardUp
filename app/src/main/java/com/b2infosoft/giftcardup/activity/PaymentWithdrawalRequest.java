package com.b2infosoft.giftcardup.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.model.BankInfo;
import com.b2infosoft.giftcardup.model.GetWithdrawHistory;
import com.b2infosoft.giftcardup.model.Withdrawal;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentWithdrawalRequest extends AppCompatActivity {
    private final static String TAG = PaymentWithdrawalRequest.class.getName();
    private Urls urls;
    private Tags tags;
    private Active active;
    DMRRequest dmrRequest;
    private Withdrawal withdrawal;
    Spinner spinner, spinner2;
    RadioButton ach, cheque, paypal;
    Button sendReq;
    int id;
    String method;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_withdrawal_request);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dmrRequest = DMRRequest.getInstance(getApplicationContext(), TAG);
        urls = Urls.getInstance();
        tags = Tags.getInstance();
        active = Active.getInstance(getApplicationContext());

        sendReq = (Button) findViewById(R.id.send_req_btn);
        spinner = (Spinner) findViewById(R.id.withdraw_req_spinner_total_fund);
        spinner2 = (Spinner) findViewById(R.id.withdraw_req_spinner_ach);

        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.withdraw_req_ach:
                        spinner2.setVisibility(View.VISIBLE);
                        break;
                    case R.id.withdraw_req_cheque:
                        spinner2.setVisibility(View.GONE);
                        break;
                    case R.id.withdraw_req_paypal:
                        spinner2.setVisibility(View.GONE);
                        break;
                }
            }
        });
        ach = (RadioButton) findViewById(R.id.withdraw_req_ach);
        cheque = (RadioButton) findViewById(R.id.withdraw_req_cheque);
        paypal = (RadioButton) findViewById(R.id.withdraw_req_paypal);
        fetchData();
        sendReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<BankInfo> info = withdrawal.getBankInfoList();
                sendData(info);
            }
        });

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

    public void fetchData() {
        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.WITHDRAWAL_REQUEST);
        map.put(tags.USER_ID, active.getUser().getUserId() + "");
        dmrRequest.doPost(urls.getUserInfo(), map, new DMRResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                Log.d("information", jsonObject.toString());
                try {
                    if (jsonObject.has(tags.SUCCESS)) {
                        if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {

                            withdrawal = Withdrawal.fromJSON(jsonObject);

                            String amount = withdrawal.getTotalAmount();
                            if (amount == null) {
                                amount = "0.00";
                            }
                            String[] arr = {"Total Available Funds($" + amount + ")"};
                            spinner.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, arr));

                            List<BankInfo> info = withdrawal.getBankInfoList();
                            String banks[] = new String[info.size() + 1];
                            int value = banks.length;
                            Log.d("value",value + "");
                            for (int i = 0; i < banks.length; i++) {
                                if(i==0){
                                    banks[i] = "Select Your Bank";
                                    continue;
                                }
                                banks[i] = info.get(i-1).getName();
                            }
                            spinner2.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, banks));
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

    private void sendData(List<BankInfo> info) {
        String ids = withdrawal.getPaymentIDs();
        String amount = withdrawal.getTotalAmount();
        String bankId = "";
        if(ach.isSelected()) {
            if (spinner2.getSelectedItemPosition() == 0) {
               // ((TextView)spinner2.getSelectedView()).setError("");
                return;
            }
            String bankName = spinner2.getSelectedItem().toString();
            List<BankInfo>  infoList =    withdrawal.getBankInfoList();
            for (BankInfo info1:infoList){
                if(info1.getName().equalsIgnoreCase(bankName)){
                    bankId = info1.getId()+"";
                    break;
                }
            }
        }

        if(ach.isSelected()){
            method ="ACH";
        } else if(paypal.isSelected()){
            method ="Paypal";
        }else if(cheque.isSelected()){
            method ="Cheque";
        }
        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.SEND_WITHDRAWAL_REQUEST);
        map.put(tags.USER_ID, active.getUser().getUserId() + "");
        map.put(tags.WITHDRAWAL_PAYMENT_METHOD, method);
        map.put(tags.WITHDRAWAL_PAYMENT_ID, ids);
        if(ach.isSelected()) {
            map.put(tags.WITHDRAWAL_BANK_ID, bankId);
        }
        map.put(tags.WITHDRAWAL_AMOUNT, amount);
        dmrRequest.doPost(urls.getUserInfo(), map, new DMRResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.has(tags.SUCCESS)) {
                        if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                            finish();
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

}
