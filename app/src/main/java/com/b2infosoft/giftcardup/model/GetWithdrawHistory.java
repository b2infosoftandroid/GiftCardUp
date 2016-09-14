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
    private String withdrawalRequestDate;
    private String paymentMethod;
    private String debitAmount;
    private String creditAmount;
    private String commissionCharge;
    private String summary;
    private String requestStatus;
    private String date;
    private String cardName;
    private String amount;
    private String approveDate;
    private String status;
    private int requestId;
    private int userId;
    private int giftCardId;
    private int userPaymentId;
    private int bankId;
    private String paymentIds;
    private int paymentStatus;
    private String payStatus;

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getWithdrawalRequestDate() {
        return withdrawalRequestDate;
    }

    public void setWithdrawalRequestDate(String withdrawalRequestDate) {
        this.withdrawalRequestDate = withdrawalRequestDate;
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

    public String getCommissionCharge() {
        return commissionCharge;
    }

    public void setCommissionCharge(String commissionCharge) {
        this.commissionCharge = commissionCharge;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public int getGiftCardId() {
        return giftCardId;
    }

    public void setGiftCardId(int giftCardId) {
        this.giftCardId = giftCardId;
    }

    public int getUserPaymentId() {
        return userPaymentId;
    }

    public void setUserPaymentId(int userPaymentId) {
        this.userPaymentId = userPaymentId;
    }

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

    public String getPaymentIds() {
        return paymentIds;
    }

    public void setPaymentIds(String paymentIds) {
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
                card.setPaymentIds(object.getString(tags.WITHDRAWAL_PAYMENT_ID));
            }
            if (object.has(tags.WITHDRAWAL_BANK_ID)) {
                card.setBankId(object.getInt(tags.WITHDRAWAL_BANK_ID));
            }
            if (object.has(tags.WITHDRAWAL_USER_PAYMENT_ID)) {
                card.setUserPaymentId(object.getInt(tags.WITHDRAWAL_USER_PAYMENT_ID));
            }
            if (object.has(tags.GIFT_CARD_GIFT_CARD_ID)) {
                card.setGiftCardId(object.getInt(tags.GIFT_CARD_GIFT_CARD_ID));
            }
            if (object.has(tags.WITHDRAWAL_CREDIT_AMOUNT)) {
                card.setCreditAmount(object.getString(tags.WITHDRAWAL_CREDIT_AMOUNT));
            }
            if (object.has(tags.WITHDRAWAL_DEBIT_AMOUNT)) {
                card.setDebitAmount(object.getString(tags.WITHDRAWAL_DEBIT_AMOUNT));
            }
            if (object.has(tags.WITHDRAWAL_COMMISSION_CHARGE)) {
                card.setCommissionCharge(object.getString(tags.WITHDRAWAL_COMMISSION_CHARGE));
            }
            if (object.has(tags.WITHDRAWAL_SUMMARY)) {
                card.setSummary(object.getString(tags.WITHDRAWAL_SUMMARY));
            }
            if (object.has(tags.WITHDRAWAL_REQUEST_STATUS)) {
                card.setRequestStatus(object.getString(tags.WITHDRAWAL_REQUEST_STATUS));
            }
            if (object.has(tags.WITHDRAWAL_REQUEST_DATE)) {
                card.setWithdrawalRequestDate(object.getString(tags.WITHDRAWAL_REQUEST_DATE));
            }
            if (object.has(tags.GIFT_CARD_CARD_NAME)) {
                card.setCardName(object.getString(tags.GIFT_CARD_CARD_NAME));
            }
            if (object.has(tags.WITHDRAWAL_PAYMENT_STATUS)) {
                card.setPayStatus(object.getString(tags.WITHDRAWAL_PAYMENT_STATUS));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(GetWithdrawHistory.class.getName(), e.getMessage());
        }
        return card;
    }
}
