package com.b2infosoft.giftcardup.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Config;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.listener.OnLoadMoreListener;
import com.b2infosoft.giftcardup.model.BankInfo;
import com.b2infosoft.giftcardup.model.GetWithdrawHistory;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvailableFundRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = AvailableFundRecyclerViewAdapter.class.getName();
    private Context context;
    private List<GetWithdrawHistory> cardInfoList;
    private Config config;
    private Tags tags;
    private DMRRequest dmrRequest;
    private Urls urls;
    private Active active;

    public AvailableFundRecyclerViewAdapter(Context context, List<GetWithdrawHistory> cardInfoList, RecyclerView recyclerView) {
        this.context = context;
        this.cardInfoList = cardInfoList;
        config = Config.getInstance();
        tags = Tags.getInstance();
        dmrRequest = DMRRequest.getInstance(context, TAG);
        urls = Urls.getInstance();
        active = Active.getInstance(context);
    }

    public class CardHolder extends RecyclerView.ViewHolder {
        TextView payment_no;
        TextView gift_card;
        TextView date;
        TextView fund;
        TextView withdrawal;
        TextView status;
        Spinner spinner;
        RadioButton ach,cheque,paypal;
        Button withdrawReq, sendReq;

        public CardHolder(View view) {
            super(view);
            payment_no = (TextView) view.findViewById(R.id.available_fund_pay_no);
            gift_card = (TextView) view.findViewById(R.id.available_fund_gift_card);
            date = (TextView) view.findViewById(R.id.available_fund_date);
            status = (TextView) view.findViewById(R.id.available_fund_status);
            fund = (TextView) view.findViewById(R.id.available_fund_funds);
            withdrawal = (TextView) view.findViewById(R.id.available_fund_withdrawal);
            withdrawReq = (Button) view.findViewById(R.id.available_fund_withdrawal_request);
            withdrawReq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setTitle("Payment Withdrawal Request");
                    dialog.setContentView(R.layout.custom_dialog_available_fund_withdraw_req);

                    sendReq = (Button)dialog.findViewById(R.id.send_req_btn);
                    spinner = (Spinner)dialog.findViewById(R.id.withdraw_req_spinner_total_fund);
                    ach = (RadioButton)dialog.findViewById(R.id.withdraw_req_ach);
                    cheque = (RadioButton)dialog.findViewById(R.id.withdraw_req_cheque);
                    paypal = (RadioButton)dialog.findViewById(R.id.withdraw_req_paypal);

                    Map<String, String> map = new HashMap<>();
                    map.put(tags.USER_ACTION, tags.WITHDRAWAL_REQUEST);
                    map.put(tags.USER_ID, active.getUser().getUserId() + "");
                    dmrRequest.doPost(urls.getUserInfo(), map, new DMRResult() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            try {
                                if (jsonObject.has(tags.SUCCESS)) {
                                    if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                                        if (jsonObject.has(tags.ARR_BANK_DETAILS)) {
                                            List<BankInfo> infos = new ArrayList<>();
                                            JSONArray jsonArray = jsonObject.getJSONArray(tags.ARR_BANK_DETAILS);
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                BankInfo info = new BankInfo();
                                                infos.add(info.fromJSON(jsonArray.getJSONObject(i)));

                                                if(ach.isSelected()){
                                                    Spinner spinner1 = new Spinner(context);
                                                    //ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,"");
                                                    //spinner1.setAdapter(spinnerArrayAdapter);
                                                }
                                            }
                                        }
                                        if(jsonObject.has(tags.WITHDRAWAL_AMOUNT)){
                                            GetWithdrawHistory info = new GetWithdrawHistory();
                                           // ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, info);
                                           // spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                           // spinner.setAdapter(spinnerAdapter);
                                           // spinnerAdapter.add(info.getAmount());
                                           // spinnerAdapter.notifyDataSetChanged();
                                        }
                                        if(jsonObject.has(tags.WITHDRAWAL_PAYMENT_ID)){

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
                    dialog.show();

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
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_available_fund_card, parent, false);
            return new CardHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CardHolder)
        {
            final GetWithdrawHistory card = cardInfoList.get(position);
            final CardHolder cardHolder = (CardHolder) holder;

            cardHolder.payment_no.setText(String.valueOf(card.getUserPaymentId()));
            cardHolder.gift_card.setText(card.getCardName());
            cardHolder.date.setText(card.getRequestDate().equalsIgnoreCase("00/00/0000") ? "" : card.getRequestDate());
            cardHolder.status.setText(card.getPayStatus());
            cardHolder.fund.setText("$" + String.valueOf(card.getCreditAmount()));
            cardHolder.withdrawal.setText("$" + String.valueOf(card.getDebitAmount()));
        }
    }

    @Override
    public int getItemCount() {
        return cardInfoList == null ? 0 : cardInfoList.size();
    }

}
