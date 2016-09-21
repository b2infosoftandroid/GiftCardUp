package com.b2infosoft.giftcardup.model;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rajesh on 9/20/2016.
 */

public class OrderSummery implements Serializable {
    private float price;
    private float shipping;
    private float discount;
    private float discountPercentage;
    private Map<String, MailPrice> priceHashMap = new HashMap<>();

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getShipping() {
        float sss = 0.0f;
        for (MailPrice price : priceHashMap.values()) {
            sss += price.getCost();
        }
        return shipping + sss;
    }

    public void setShipping(float shipping) {
        this.shipping = shipping;
    }

    public float getDiscount() {
        return discount;
    }

    private void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getBalance() {
        return (price + getShipping()) - discount;
    }

    public float getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(float discountPercentage) {
        if (discountPercentage > 0) {
            setDiscount((price * discountPercentage) / 100);
        }
        this.discountPercentage = discountPercentage;
    }

    public void setDiscountAmount(float discountPercentage) {
        setDiscount(discountPercentage);
        this.discountPercentage = discountPercentage;
    }

    public void setShippingMailType(String cardID, String mailType, float cost) {
        priceHashMap.put(cardID, new MailPrice(mailType, cost));
    }

    private class MailPrice implements Serializable{
        private String name;
        private float cost;

        public MailPrice(String name, float cost) {
            this.name = name;
            this.cost = cost;
        }

        public String getName() {
            return name;
        }

        public float getCost() {
            return cost;
        }
    }
}
