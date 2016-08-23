package com.b2infosoft.giftcardup.model;

import android.util.Log;
import com.b2infosoft.giftcardup.app.Tags;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;

/**
 * Created by rajesh on 8/21/2016.
 */

public class GiftCard implements Serializable {
    private int giftCardID;
    private int readStatus;
    private int userID;
    private int companyID;
    private int parentGiftCardID;
    private String cardName;
    private String serialNumber;
    private String cardPin;
    private double cardPrice;
    private double cardValue;
    private String sell;
    private String sellingPercentage;
    private String yourEarning;
    private String shippingAndCommissionCharge;
    private int percentageOff;
    private String soldOn;
    private String cardDescription;
    private String cardImage;
    private String approveDate;
    private int approveStatus;
    private int statusType;
    private int soldStatus;
    private String barCodeImage;
    private String denyReason;
    private String needReview;

    public int getGiftCardID() {
        return giftCardID;
    }

    public void setGiftCardID(int giftCardID) {
        this.giftCardID = giftCardID;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public int getParentGiftCardID() {
        return parentGiftCardID;
    }

    public void setParentGiftCardID(int parentGiftCardID) {
        this.parentGiftCardID = parentGiftCardID;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getCardPin() {
        return cardPin;
    }

    public void setCardPin(String cardPin) {
        this.cardPin = cardPin;
    }

    public double getCardPrice() {
        return cardPrice;
    }

    public void setCardPrice(double cardPrice) {
        this.cardPrice = cardPrice;
    }

    public double getCardValue() {
        return cardValue;
    }

    public void setCardValue(double cardValue) {
        this.cardValue = cardValue;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getSellingPercentage() {
        return sellingPercentage;
    }

    public void setSellingPercentage(String sellingPercentage) {
        this.sellingPercentage = sellingPercentage;
    }

    public String getYourEarning() {
        return yourEarning;
    }

    public void setYourEarning(String yourEarning) {
        this.yourEarning = yourEarning;
    }

    public String getShippingAndCommissionCharge() {
        return shippingAndCommissionCharge;
    }

    public void setShippingAndCommissionCharge(String shippingAndCommissionCharge) {
        this.shippingAndCommissionCharge = shippingAndCommissionCharge;
    }

    public int getPercentageOff() {
        return percentageOff;
    }

    public void setPercentageOff(int percentageOff) {
        this.percentageOff = percentageOff;
    }

    public String getSoldOn() {
        return soldOn;
    }

    public void setSoldOn(String soldOn) {
        this.soldOn = soldOn;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }

    public String getCardImage() {
        return cardImage;
    }

    public void setCardImage(String cardImage) {
        this.cardImage = cardImage;
    }

    public String getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(String approveDate) {
        this.approveDate = approveDate;
    }

    public int getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(int approveStatus) {
        this.approveStatus = approveStatus;
    }

    public int getStatusType() {
        return statusType;
    }

    public void setStatusType(int statusType) {
        this.statusType = statusType;
    }

    public int getSoldStatus() {
        return soldStatus;
    }

    public void setSoldStatus(int soldStatus) {
        this.soldStatus = soldStatus;
    }

    public String getBarCodeImage() {
        return barCodeImage;
    }

    public void setBarCodeImage(String barCodeImage) {
        this.barCodeImage = barCodeImage;
    }

    public String getDenyReason() {
        return denyReason;
    }

    public void setDenyReason(String denyReason) {
        this.denyReason = denyReason;
    }

    public String getNeedReview() {
        return needReview;
    }

    public void setNeedReview(String needReview) {
        this.needReview = needReview;
    }

    public GiftCard fromJSON(JSONObject object) {
        Tags tags = Tags.getInstance();
        GiftCard card = new GiftCard();
        try {
            if (object.has(tags.GIFT_CARD_GIFT_CARD_ID)) {
                card.setGiftCardID(object.getInt(tags.GIFT_CARD_GIFT_CARD_ID));
            }
            if (object.has(tags.GIFT_CARD_READ_STATUS)) {
                card.setReadStatus(object.getInt(tags.GIFT_CARD_READ_STATUS));
            }
            if (object.has(tags.GIFT_CARD_USER_ID)) {
                card.setUserID(object.getInt(tags.USER_ID));
            }
            if (object.has(tags.GIFT_CARD_COMPANY_ID)) {
                card.setCompanyID(object.getInt(tags.COMPANY_ID));
            }
            if (object.has(tags.GIFT_CARD_PARENT_GIFT_CARD_ID)) {
                card.setParentGiftCardID(object.getInt(tags.GIFT_CARD_PARENT_GIFT_CARD_ID));
            }
            if (object.has(tags.GIFT_CARD_CARD_NAME)) {
                card.setCardName(object.getString(tags.GIFT_CARD_CARD_NAME));
            }
            if (object.has(tags.GIFT_CARD_SERIAL_NUMBER)) {
                card.setSerialNumber(object.getString(tags.GIFT_CARD_SERIAL_NUMBER));
            }
            if (object.has(tags.GIFT_CARD_CARD_PIN)) {
                card.setCardPin(object.getString(tags.GIFT_CARD_CARD_PIN));
            }
            if (object.has(tags.GIFT_CARD_CARD_PRICE)) {
                card.setCardPrice(object.getDouble(tags.GIFT_CARD_CARD_PRICE));
            }
            if (object.has(tags.GIFT_CARD_VALUE)) {
                card.setCardValue(object.getDouble(tags.GIFT_CARD_VALUE));
            }
            if (object.has(tags.GIFT_CARD_SELL)) {
                card.setSell(object.getString(tags.GIFT_CARD_SELL));
            }
            if (object.has(tags.GIFT_CARD_SELLING_PERCENTAGE)) {
                card.setSellingPercentage(object.getString(tags.GIFT_CARD_SELLING_PERCENTAGE));
            }
            if (object.has(tags.GIFT_CARD_YOUR_EARNING)) {
                card.setYourEarning(object.getString(tags.GIFT_CARD_YOUR_EARNING));
            }
            if (object.has(tags.GIFT_CARD_SHIPPING_AND_COMMISSION_CHARGE)) {
                card.setShippingAndCommissionCharge(object.getString(tags.GIFT_CARD_SHIPPING_AND_COMMISSION_CHARGE));
            }
            if (object.has(tags.GIFT_CARD_PERCENTAGE_OFF)) {
                card.setPercentageOff(object.getInt(tags.GIFT_CARD_PERCENTAGE_OFF));
            }
            if (object.has(tags.GIFT_CARD_SOLD_ON)) {
                card.setSoldOn(object.getString(tags.GIFT_CARD_SOLD_ON));
            }
            if (object.has(tags.GIFT_CARD_CARD_DESCRIPTION)) {
                card.setCardDescription(object.getString(tags.GIFT_CARD_CARD_DESCRIPTION));
            }
            if (object.has(tags.GIFT_CARD_CARD_IMG)) {
                card.setCardImage(object.getString(tags.GIFT_CARD_CARD_IMG));
            }
            if (object.has(tags.GIFT_CARD_APPROVE_DATE)) {
                card.setApproveDate(object.getString(tags.GIFT_CARD_APPROVE_DATE));
            }
            if (object.has(tags.GIFT_CARD_APPROVE_STATUS)) {
                card.setApproveStatus(object.getInt(tags.GIFT_CARD_APPROVE_STATUS));
            }
            if (object.has(tags.GIFT_CARD_STATUS_TYPE)) {
                card.setStatusType(object.getInt(tags.GIFT_CARD_STATUS_TYPE));
            }
            if (object.has(tags.GIFT_CARD_SOLD_STATUS)) {
                card.setSoldStatus(object.getInt(tags.GIFT_CARD_SOLD_STATUS));
            }
            if (object.has(tags.GIFT_CARD_BARCODE_IMG)) {
                card.setBarCodeImage(object.getString(tags.GIFT_CARD_BARCODE_IMG));
            }
            if (object.has(tags.GIFT_CARD_DENY_REASON)) {
                card.setDenyReason(object.getString(tags.GIFT_CARD_DENY_REASON));
            }
            if (object.has(tags.GIFT_CARD_NEED_REVIEW)) {
                card.setNeedReview(object.getString(tags.GIFT_CARD_NEED_REVIEW));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(GiftCard.class.getName(), e.getMessage());
        }
        return card;
    }
}
