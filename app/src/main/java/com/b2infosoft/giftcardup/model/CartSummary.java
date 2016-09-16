package com.b2infosoft.giftcardup.model;

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
    public float getValue(){
        return 0.0f;
    }
    public float getPrice(){
        return 0.0f;
    }
    public float getSaving(){
        return 0.0f;
    }
}
