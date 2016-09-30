package com.b2infosoft.giftcardup.model;

import android.util.Log;

import com.b2infosoft.giftcardup.app.Tags;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rajesh on 9/29/2016.
 */

public class Order {
    private String cardName;
    private String cardImg;
    private int orderId;
    private int userId;
    private int giftCardId;
    private String mailType;
    private String mailClassPrice;
    private String orderDate;
    private int status;
    private int readStatus;
    private String transactionId;
    private int mainOrderId;

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardImg() {
        return cardImg;
    }

    public void setCardImg(String cardImg) {
        this.cardImg = cardImg;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGiftCardId() {
        return giftCardId;
    }

    public void setGiftCardId(int giftCardId) {
        this.giftCardId = giftCardId;
    }

    public String getMailType() {
        return mailType;
    }

    public void setMailType(String mailType) {
        this.mailType = mailType;
    }

    public String getMailClassPrice() {
        return mailClassPrice;
    }

    public void setMailClassPrice(String mailClassPrice) {
        this.mailClassPrice = mailClassPrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public int getMainOrderId() {
        return mainOrderId;
    }

    public void setMainOrderId(int mainOrderId) {
        this.mainOrderId = mainOrderId;
    }

    public static Order fromJSON(JSONObject object) {
        Order order = new Order();
        Tags tags = Tags.getInstance();
        try {
            if (object.has(tags.GIFT_CARD_CARD_NAME)) {
                order.setCardName(object.getString(tags.GIFT_CARD_CARD_NAME));
            }
            if (object.has(tags.GIFT_CARD_CARD_IMG)) {
                order.setCardImg(object.getString(tags.GIFT_CARD_CARD_IMG));
            }
            if (object.has(tags.ORDER_ID)) {
                order.setOrderId(object.getInt(tags.ORDER_ID));
            }
            if (object.has(tags.USER_ID)) {
                order.setUserId(object.getInt(tags.USER_ID));
            }
            if (object.has(tags.GIFT_CARD_GIFT_CARD_ID)) {
                order.setGiftCardId(object.getInt(tags.GIFT_CARD_GIFT_CARD_ID));
            }
            if (object.has(tags.MAIL_TYPE)) {
                order.setMailType(object.getString(tags.MAIL_TYPE));
            }
            if (object.has(tags.MAIL_CLASS_PRICE)) {
                order.setMailClassPrice(object.getString(tags.MAIL_CLASS_PRICE));
            }
            if (object.has(tags.ORDER_DATE)) {
                order.setOrderDate(object.getString(tags.ORDER_DATE));
            }
            if (object.has(tags.STATUS)) {
                order.setStatus(object.getInt(tags.STATUS));
            }
            if (object.has(tags.GIFT_CARD_READ_STATUS)) {
                order.setReadStatus(object.getInt(tags.GIFT_CARD_READ_STATUS));
            }
            if (object.has(tags.TRANSACTION_ID)) {
                order.setTransactionId(object.getString(tags.TRANSACTION_ID));
            }
            if (object.has(tags.MAIN_ORDER_ID)) {
                order.setMainOrderId(object.getInt(tags.MAIN_ORDER_ID));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(Order.class.getName(), e.getMessage());
        }
        return order;
    }

}
