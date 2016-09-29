package com.b2infosoft.giftcardup.model;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rajesh on 9/20/2016.
 */

public class OrderSummery implements Serializable {
    private float price;
    private float shipping;
    private float discount;
    private float discountPercentage;
    private String method;
    private float commission;
    private Map<String, MailPrice> priceHashMap = new HashMap<>();

    public String getItemId() {
        StringBuffer buffer = new StringBuffer();
        for (String id : priceHashMap.keySet()) {
            buffer.append(id).append(",");
        }
        return buffer.toString();
    }

    public int getTotalItem() {
        return priceHashMap.size();
    }

    public String getItemData() {
        JSONArray array = new JSONArray();
        try {
            Gson gson = new Gson();
            for (String id : priceHashMap.keySet()) {
                JSONObject object = new JSONObject();
                object.put("id", id);
                object.put("mail_type", gson.toJson(priceHashMap.get(id)));
                array.put(object);
            }
        } catch (JSONException data) {
            data.printStackTrace();
            Log.e("OrderSummery", data.getMessage());
        }
        return array.toString();
    }

    public String toJSON(String id, MailPrice price) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{");

        return "";
    }

    /*
    *
    * 	//  item_id :-> [{"id":30,"mail_type":{"name":"First Class Mail","price":5}},{"id":31,"mail_type":{"name":"Top Class Mail","price":10}},{"id":33,"mail_type":{"name":"Express Mail","price":15}}]

    * */

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getShipping() {
        float sss = 0.0f;
        for (MailPrice price : priceHashMap.values()) {
            sss += price.getPrice();
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public float getCommission() {
        return commission;
    }

    public void setCommission(float commission) {
        this.commission = commission;
    }

    public void setDefaultShipping(List<GiftCard> giftCards, String type, float price) {
        for (GiftCard card : giftCards) {
            if (card.getCardType() != 2) {
                if (!priceHashMap.containsKey(card.getGiftCardID() + "")) {
                    priceHashMap.put(card.getGiftCardID() + "", new MailPrice(type, price));
                }
            } else {
                priceHashMap.put(card.getGiftCardID() + "", new MailPrice("No", 0.0f));
            }
        }
    }

    private class MailPrice implements Serializable {
        private String name;
        private float price;

        public MailPrice(String name, float cost) {
            this.name = name;
            this.price = cost;
        }

        public String getName() {
            return name;
        }

        public float getPrice() {
            return price;
        }
    }

}
