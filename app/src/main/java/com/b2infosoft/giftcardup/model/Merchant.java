package com.b2infosoft.giftcardup.model;

import android.util.Log;

import com.b2infosoft.giftcardup.app.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by rajesh on 8/25/2016.
 */
public class Merchant implements Serializable{
    private String companyID;
    private String companyName;
    private int cardType;
    private String sellingPercentage;

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * 1=> physical card
     * 2=> e-card
     * @return int 1,2
     */

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public String getSellingPercentage() {
        return sellingPercentage;
    }

    public void setSellingPercentage(String sellingPercentage) {
        this.sellingPercentage = sellingPercentage;
    }

    public static Merchant fromJSON(JSONObject object) {
        Tags tags = Tags.getInstance();
        Merchant merchant = new Merchant();
        try {
            if (object.has(tags.COMPANY_ID)) {
                merchant.setCompanyID(object.getString(tags.COMPANY_ID));
            }
            if (object.has(tags.COMPANY_NAME)) {
                merchant.setCompanyName(object.getString(tags.COMPANY_NAME));
            }
            if (object.has(tags.CARD_TYPE)) {
                merchant.setCardType(object.getInt(tags.CARD_TYPE));
            }
            if (object.has(tags.GIFT_CARD_SELLING_PERCENTAGE)) {
                merchant.setSellingPercentage(object.getString(tags.GIFT_CARD_SELLING_PERCENTAGE));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(Merchant.class.getName(), e.getMessage());
        }
        return merchant;
    }
}
