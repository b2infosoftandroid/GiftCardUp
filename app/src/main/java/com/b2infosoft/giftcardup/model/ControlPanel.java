package com.b2infosoft.giftcardup.model;

import android.util.Log;

import com.b2infosoft.giftcardup.app.Tags;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rajesh on 9/20/2016.
 */
public class ControlPanel {
    private String id;
    private String generalCommission;
    private String shippingCharge;
    private String sellingPercentage;
    private String companyName;
    private String phoneNumber;
    private String email;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String processTime;
    private String cardAttemptTime;
    private String image;
    private String firstClassPrice;
    private String priorityPrice;
    private String expressPrice;
    private String minimumScore;
    private String maximumScore;
    private String referralAmount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGeneralCommission() {
        return generalCommission;
    }

    public void setGeneralCommission(String generalCommission) {
        this.generalCommission = generalCommission;
    }

    public String getShippingCharge() {
        return shippingCharge;
    }

    public void setShippingCharge(String shippingCharge) {
        this.shippingCharge = shippingCharge;
    }

    public String getSellingPercentage() {
        return sellingPercentage;
    }

    public void setSellingPercentage(String sellingPercentage) {
        this.sellingPercentage = sellingPercentage;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getProcessTime() {
        return processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

    public String getCardAttemptTime() {
        return cardAttemptTime;
    }

    public void setCardAttemptTime(String cardAttemptTime) {
        this.cardAttemptTime = cardAttemptTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFirstClassPrice() {
        return firstClassPrice;
    }

    public void setFirstClassPrice(String firstClassPrice) {
        this.firstClassPrice = firstClassPrice;
    }

    public String getPriorityPrice() {
        return priorityPrice;
    }

    public void setPriorityPrice(String priorityPrice) {
        this.priorityPrice = priorityPrice;
    }

    public String getExpressPrice() {
        return expressPrice;
    }

    public void setExpressPrice(String expressPrice) {
        this.expressPrice = expressPrice;
    }

    public String getMinimumScore() {
        return minimumScore;
    }

    public void setMinimumScore(String minimumScore) {
        this.minimumScore = minimumScore;
    }

    public String getMaximumScore() {
        return maximumScore;
    }

    public void setMaximumScore(String maximumScore) {
        this.maximumScore = maximumScore;
    }

    public String getReferralAmount() {
        return referralAmount;
    }

    public void setReferralAmount(String referralAmount) {
        this.referralAmount = referralAmount;
    }

    public static ControlPanel fromJSON(JSONObject object) {
        Tags tags = Tags.getInstance();
        ControlPanel panel = new ControlPanel();
        try {
            if (object.has(tags.ID)) {
                panel.setId(object.getString(tags.ID));
            }
            if (object.has(tags.GENERAL_COMMISSION)) {
                panel.setGeneralCommission(object.getString(tags.GENERAL_COMMISSION));
            }
            if (object.has(tags.SHIPPING_CHARGE)) {
                panel.setShippingCharge(object.getString(tags.SHIPPING_CHARGE));
            }
            if (object.has(tags.GIFT_CARD_SELLING_PERCENTAGE)) {
                panel.setSellingPercentage(object.getString(tags.GIFT_CARD_SELLING_PERCENTAGE));
            }
            if (object.has(tags.COMPANY_NAME)) {
                panel.setCompanyName(object.getString(tags.COMPANY_NAME));
            }
            if (object.has(tags.PHONE_NUMBER)) {
                panel.setPhoneNumber(object.getString(tags.PHONE_NUMBER));
            }
            if (object.has(tags.EMAIL)) {
                panel.setEmail(object.getString(tags.EMAIL));
            }
            if (object.has(tags.ADDRESS)) {
                panel.setAddress(object.getString(tags.ADDRESS));
            }
            if (object.has(tags.CITY)) {
                panel.setAddress(object.getString(tags.CITY));
            }
            if (object.has(tags.STATE)) {
                panel.setState(object.getString(tags.STATE));
            }
            if (object.has(tags.ZIP_CODE)) {
                panel.setZipCode(object.getString(tags.ZIP_CODE));
            }
            if (object.has(tags.PROCESS_TIME)) {
                panel.setProcessTime(object.getString(tags.PROCESS_TIME));
            }
            if (object.has(tags.CARD_ATTEMPT_TIME)) {
                panel.setCardAttemptTime(object.getString(tags.CARD_ATTEMPT_TIME));
            }
            if (object.has(tags.IMAGE)) {
                panel.setImage(object.getString(tags.IMAGE));
            }
            if (object.has(tags.FIRST_CLASS_PRICE)) {
                panel.setFirstClassPrice(object.getString(tags.FIRST_CLASS_PRICE));
            }
            if (object.has(tags.PRIORITY_PRICE)) {
                panel.setPriorityPrice(object.getString(tags.PRIORITY_PRICE));
            }
            if (object.has(tags.EXPRESS_PRICE)) {
                panel.setExpressPrice(object.getString(tags.EXPRESS_PRICE));
            }
            if (object.has(tags.MINIMUM_SCORE)) {
                panel.setMinimumScore(object.getString(tags.MINIMUM_SCORE));
            }
            if (object.has(tags.MAXIMUM_SCORE)) {
                panel.setMaximumScore(object.getString(tags.MAXIMUM_SCORE));
            }
            if (object.has(tags.REFERRAL_AMOUNT)) {
                panel.setReferralAmount(object.getString(tags.REFERRAL_AMOUNT));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(ControlPanel.class.getName(), e.getMessage());
        }
        return panel;
    }
}
