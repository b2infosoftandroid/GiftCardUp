package com.b2infosoft.giftcardup.model;

import android.app.Application;

import com.b2infosoft.giftcardup.model.GiftCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajesh on 9/14/2016.
 */
public class Cart{

    private List<GiftCard> giftCardList = new ArrayList<>();

    public int getCartItemCount() {
        return giftCardList.size();
    }

    public boolean addCartItem(GiftCard giftCard) {
        return giftCardList.add(giftCard);
    }

    public boolean removeCardItem(GiftCard giftCard) {
        return giftCardList.remove(giftCard);
    }

    public List<GiftCard> getCartItemList() {
        return giftCardList;
    }

    public void removeAll() {
        giftCardList.clear();
    }

}
