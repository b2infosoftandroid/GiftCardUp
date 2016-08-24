package com.b2infosoft.giftcardup.model;

import android.util.Log;

import com.b2infosoft.giftcardup.app.Tags;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rajesh on 8/24/2016.
 */
public class GetOffer {
    private int companyPercentage;
    private int cardOffer;
    private int cardOwnerBalance;
    private int cardBalance;
    private String companyName;
    private int cardType;
    private String cardImage;
    private int cardCompanyID;
    private String cardName;

    public int getCompanyPercentage() {
        return companyPercentage;
    }

    public void setCompanyPercentage(int companyPercentage) {
        this.companyPercentage = companyPercentage;
    }

    public int getCardOffer() {
        return cardOffer;
    }

    public void setCardOffer(int cardOffer) {
        this.cardOffer = cardOffer;
    }

    public int getCardOwnerBalance() {
        return cardOwnerBalance;
    }

    public void setCardOwnerBalance(int cardOwnerBalance) {
        this.cardOwnerBalance = cardOwnerBalance;
    }

    public int getCardBalance() {
        return cardBalance;
    }

    public void setCardBalance(int cardBalance) {
        this.cardBalance = cardBalance;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public String getCardImage() {
        return cardImage;
    }

    public void setCardImage(String cardImage) {
        this.cardImage = cardImage;
    }

    public int getCardCompanyID() {
        return cardCompanyID;
    }

    public void setCardCompanyID(int cardCompanyID) {
        this.cardCompanyID = cardCompanyID;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public GetOffer fromJSON(JSONObject object) {
        Tags tags = Tags.getInstance();
        GetOffer getOffer = new GetOffer();
        try {
            if (object.has(tags.SELL_GIFT_CARD_NAME)) {
                getOffer.setCardName(object.getString(tags.SELL_GIFT_CARD_NAME));
            }
            if (object.has(tags.SELL_GIFT_CARD_TYPE)) {
                getOffer.setCardType(object.getInt(tags.SELL_GIFT_CARD_TYPE));
            }
            if (object.has(tags.SELL_GIFT_CARD_OFFER)) {
                getOffer.setCardOffer(object.getInt(tags.SELL_GIFT_CARD_OFFER));
            }
            if (object.has(tags.SELL_GIFT_CARD_BALANCE)) {
                getOffer.setCardBalance(object.getInt(tags.SELL_GIFT_CARD_BALANCE));
            }
            if (object.has(tags.SELL_GIFT_CARD_OWNER_BALANCE)) {
                getOffer.setCardOwnerBalance(object.getInt(tags.SELL_GIFT_CARD_OWNER_BALANCE));
            }
            if (object.has(tags.SELL_GIFT_CARD_IMAGE)) {
                getOffer.setCardImage(object.getString(tags.SELL_GIFT_CARD_IMAGE));
            }
            if (object.has(tags.SELL_GIFT_CARD_COMPANY_ID)) {
                getOffer.setCardCompanyID(object.getInt(tags.SELL_GIFT_CARD_COMPANY_ID));
            }
            if (object.has(tags.SELL_GIFT_CARD_COMPANY_NAME)) {
                getOffer.setCompanyName(object.getString(tags.SELL_GIFT_CARD_COMPANY_NAME));
            }
            if (object.has(tags.SELL_GIFT_CARD_COMPANY_PERCENTAGE)) {
                getOffer.setCompanyPercentage(object.getInt(tags.SELL_GIFT_CARD_COMPANY_PERCENTAGE));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(GetOffer.class.getName(), e.getMessage());
        }
        return getOffer;
    }

}
