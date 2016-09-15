package com.b2infosoft.giftcardup.model;

import android.util.Log;

import com.b2infosoft.giftcardup.app.Tags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Microsoft on 9/15/2016.
 */
public class Withdrawal {
    private static Withdrawal ourInstance = new Withdrawal();

    public static Withdrawal getInstance() {
        return ourInstance;
    }

    private Withdrawal() {
    }

    private String debitAmount;
    private String creditAmount;
    private String totalAmount;
    private String paymentIDs;
    private List<BankInfo> bankInfoList;

    public static Withdrawal getOurInstance() {
        return ourInstance;
    }

    public static void setOurInstance(Withdrawal ourInstance) {
        Withdrawal.ourInstance = ourInstance;
    }

    public String getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(String debitAmount) {
        this.debitAmount = debitAmount;
    }

    public String getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentIDs() {
        return paymentIDs;
    }

    public void setPaymentIDs(String paymentIDs) {
        this.paymentIDs = paymentIDs;
    }

    public List<BankInfo> getBankInfoList() {
        return bankInfoList;
    }

    public void setBankInfoList(List<BankInfo> bankInfoList) {
        this.bankInfoList = bankInfoList;
    }

    public static Withdrawal fromJSON(JSONObject jsonObject) {
        Withdrawal withdrawal = Withdrawal.getOurInstance();
        Tags tags = Tags.getInstance();
        try {
            if (jsonObject.has(tags.SUCCESS)) {
                if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                    if (jsonObject.has(tags.ARR_BANK_DETAILS)) {
                        JSONArray jsonArray = jsonObject.getJSONArray(tags.ARR_BANK_DETAILS);
                        List<BankInfo> bankInfoList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            bankInfoList.add(BankInfo.fromJSON(jsonArray.getJSONObject(i)));
                        }
                        withdrawal.setBankInfoList(bankInfoList);
                    }
                    if (jsonObject.has(tags.WITHDRAWAL_CREDIT_AMOUNT)) {
                       withdrawal.setCreditAmount(jsonObject.getString(tags.WITHDRAWAL_CREDIT_AMOUNT));
                    }
                    if (jsonObject.has(tags.WITHDRAWAL_DEBIT_AMOUNT)) {
                        withdrawal.setDebitAmount(jsonObject.getString(tags.WITHDRAWAL_DEBIT_AMOUNT));
                    }
                    if (jsonObject.has(tags.WITHDRAWAL_PAYMENT_ID)) {
                        withdrawal.setPaymentIDs(jsonObject.getString(tags.WITHDRAWAL_PAYMENT_ID));
                    }
                    if (jsonObject.has(tags.WITHDRAWAL_AMOUNT)) {
                        withdrawal.setTotalAmount(jsonObject.getString(tags.WITHDRAWAL_AMOUNT));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(Withdrawal.class.getName(), e.getMessage());
        }
        return withdrawal;
    }
}
