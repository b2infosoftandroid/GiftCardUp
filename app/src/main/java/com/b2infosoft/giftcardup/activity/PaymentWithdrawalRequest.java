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
import android.widget.Spinner;
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
    List<GetWithdrawHistory> withdrawHistoryList;

    Spinner spinner,spinner2;
    RadioButton ach,cheque,paypal;
    Button sendReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_withdrawal_request);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dmrRequest = DMRRequest.getInstance(getApplicationContext(), TAG);
        urls = Urls.getInstance();
        tags = Tags.getInstance();
        active = Active.getInstance(getApplicationContext());

        sendReq = (Button)findViewById(R.id.send_req_btn);
        spinner = (Spinner)findViewById(R.id.withdraw_req_spinner_total_fund);
        spinner2 = (Spinner)findViewById(R.id.withdraw_req_spinner_ach);
        ach = (RadioButton)findViewById(R.id.withdraw_req_ach);
        cheque = (RadioButton)findViewById(R.id.withdraw_req_cheque);
        paypal = (RadioButton)findViewById(R.id.withdraw_req_paypal);

        if(ach.isSelected()){
            spinner2.setVisibility(View.VISIBLE);
        }

        sendReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> map = new HashMap<>();
                map.put(tags.USER_ACTION, tags.SEND_WITHDRAWAL_REQUEST);
                map.put(tags.USER_ID, active.getUser().getUserId() + "");
                map.put(tags.WITHDRAWAL_PAYMENT_METHOD, "");
                map.put(tags.WITHDRAWAL_PAYMENT_ID, "");
                map.put(tags.WITHDRAWAL_BANK_ID, "");
                map.put(tags.WITHDRAWAL_AMOUNT, "");
                dmrRequest.doPost(urls.getUserInfo(), map, new DMRResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        try {
                            if (jsonObject.has(tags.SUCCESS)) {
                                if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {

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
        });
        fetchData();
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

    public void fetchData(){
        showMessage("FetchdATA cALLING");
        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.WITHDRAWAL_REQUEST);
        map.put(tags.USER_ID, active.getUser().getUserId() + "");
        dmrRequest.doPost(urls.getUserInfo(), map, new DMRResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                showMessage("success");
                Log.d("information",jsonObject.toString());
                try {
                    if (jsonObject.has(tags.SUCCESS)) {
                        if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                            Withdrawal withdrawal = Withdrawal.getOurInstance();


/*                            if (jsonObject.has(tags.ARR_BANK_DETAILS)) {
                                ArrayList<BankInfo> infos = new ArrayList<>();
                                JSONArray jsonArray = jsonObject.getJSONArray(tags.ARR_BANK_DETAILS);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    BankInfo info = new BankInfo();
                                    infos.add(info.fromJSON(jsonArray.getJSONObject(i)));
                                    setData(infos);
                                }
                            }
                            if(jsonObject.has(tags.WITHDRAWAL_AMOUNT)){
                                GetWithdrawHistory info = new GetWithdrawHistory();
                                withdrawHistoryList.add(info.fromJSON(jsonObject.getJSONObject(tags.WITHDRAWAL_AMOUNT)));
                                updateAmount(withdrawHistoryList);

                            }
                            if(jsonObject.has(tags.WITHDRAWAL_PAYMENT_ID)){
                                GetWithdrawHistory info = new GetWithdrawHistory();
                                info.fromJSON(jsonObject.getJSONObject(tags.WITHDRAWAL_PAYMENT_ID));
                            }
                            */
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
                showMessage("error");
            }

        });
    }

    private void setData(List<BankInfo> infolist){
        for (int i = 0; i < infolist.size(); i++) {
            BankInfo info = infolist.get(i);
            String name = info.getName();
            String[] arr = {"Select your Bank",name};
            spinner2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arr));
        }
 }

    private void updateAmount(List<GetWithdrawHistory> list) {
        for (int i = 0; i < list.size(); i++) {
            GetWithdrawHistory info = list.get(i);
            String amount = info.getAmount();
            String[] arr = {"Total Available Funds($" + amount + ")"};
            spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arr));
        }
    }
    private void showMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
