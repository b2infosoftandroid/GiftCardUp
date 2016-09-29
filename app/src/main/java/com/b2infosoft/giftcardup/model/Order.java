package com.b2infosoft.giftcardup.model;
/**
 * Created by rajesh on 9/29/2016.
 */
public class Order {
    private int orderId;
    private int userId;
    private int giftCardId;
    private String mailType;
    private String mailClassPrice;
    private String orderDate;
    private int status;
    private int readStatus;
    private int transactionId;
    private int mainOrderId;

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

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getMainOrderId() {
        return mainOrderId;
    }

    public void setMainOrderId(int mainOrderId) {
        this.mainOrderId = mainOrderId;
    }

}
