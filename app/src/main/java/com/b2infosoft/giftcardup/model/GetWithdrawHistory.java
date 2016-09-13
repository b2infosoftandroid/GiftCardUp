package com.b2infosoft.giftcardup.model;

import android.util.Log;

import com.b2infosoft.giftcardup.app.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Microsoft on 9/13/2016.
 */
public class GetWithdrawHistory implements Serializable {
    private String requestDate;
    private String paymentMethod;
    private String amount;
    private String approveDate;
    private String status;
    private int requestId;
    private int userId;
    private int bankId;
    private int paymentIds;
    private int paymentStatus;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public int getPaymentIds() {
        return paymentIds;
    }

    public void setPaymentIds(int paymentIds) {
        this.paymentIds = paymentIds;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(String approveDate) {
        this.approveDate = approveDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public GetWithdrawHistory fromJSON(JSONObject object) {
        Tags tags = Tags.getInstance();
        GetWithdrawHistory card = new GetWithdrawHistory();
        try {
            if (object.has(tags.BANK_INFO_DATE)) {
                card.setRequestDate(object.getString(tags.BANK_INFO_DATE));
            }
            if (object.has(tags.GIFT_CARD_USER_ID)) {
                card.setUserId(object.getInt(tags.GIFT_CARD_USER_ID));
            }
            if (object.has(tags.WITHDRAWAL_PAYMENT_METHOD)) {
                card.setPaymentMethod(object.getString(tags.WITHDRAWAL_PAYMENT_METHOD));
            }
            if (object.has(tags.WITHDRAWAL_AMOUNT)) {
                card.setAmount(object.getString(tags.WITHDRAWAL_AMOUNT));
            }
            if (object.has(tags.GIFT_CARD_APPROVE_DATE)) {
                card.setApproveDate(object.getString(tags.GIFT_CARD_APPROVE_DATE));
            }
            if (object.has(tags.GIFT_CARD_READ_STATUS)) {
                card.setStatus(object.getString(tags.GIFT_CARD_READ_STATUS));
            }
            if (object.has(tags.WITHDRAWAL_REQUEST_ID)) {
                card.setRequestId(object.getInt(tags.WITHDRAWAL_REQUEST_ID));
            }
            if (object.has(tags.WITHDRAWAL_PAYMENT_ID)) {
                card.setPaymentIds(object.getInt(tags.WITHDRAWAL_PAYMENT_ID));
            }
            if (object.has(tags.WITHDRAWAL_BANK_ID)) {
                card.setBankId(object.getInt(tags.WITHDRAWAL_BANK_ID));
            }
            if (object.has(tags.WITHDRAWAL_PAYMENT_STATUS)) {
                card.setPaymentStatus(object.getInt(tags.WITHDRAWAL_PAYMENT_STATUS));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(GetWithdrawHistory.class.getName(), e.getMessage());
        }
        return card;
    }
}
