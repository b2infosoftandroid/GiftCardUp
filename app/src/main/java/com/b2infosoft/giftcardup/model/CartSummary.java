package com.b2infosoft.giftcardup.model;

import android.util.Log;

import java.util.List;

/**
 * Created by rajesh on 9/16/2016.
 */

public class CartSummary {
    private List<GiftCard> cardList;
    private float value;
    private float price;
    private float saving;

    public CartSummary(List<GiftCard> cardList) {
        this.cardList = cardList;
    }

    public float getValue() {
        float value = 0.0f;
        if (cardList == null)
            return value;
        for (GiftCard giftCard : cardList) {
            String v = giftCard.getCardValue();
            if (v.length() == 0) {
                v = "0";
            }
            try {
                value += Float.parseFloat(v);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                Log.e("Error", ex.getMessage());
            }
        }
        return value;
    }

    public float getPrice() {
        float price = 0.0f;
        if (cardList == null)
            return price;
        for (GiftCard giftCard : cardList) {
            String p = giftCard.getCardPrice();
            if (p.length() == 0) {
                p = "0";
            }
            try {
                price += Float.parseFloat(p);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                Log.e("Error", ex.getMessage());
            }
        }
        return price;
    }

    public float getSaving() {
        return getPrice() - getValue();
    }
}
