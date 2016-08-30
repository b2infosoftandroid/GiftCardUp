package com.b2infosoft.giftcardup.model;

/**
 * Created by Microsoft on 8/30/2016.
 */
public class AcceptOffer {
    private String cardType;
    private int serialNo;
    private int pin;
    private int cardBalance;
    private int cardEarning;
    private int cardSelling;

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public int getCardBalance() {
        return cardBalance;
    }

    public void setCardBalance(int cardBalance) {
        this.cardBalance = cardBalance;
    }

    public int getCardEarning() {
        return cardEarning;
    }

    public void setCardEarning(int cardEarning) {
        this.cardEarning = cardEarning;
    }

    public int getCardSelling() {
        return cardSelling;
    }

    public void setCardSelling(int cardSelling) {
        this.cardSelling = cardSelling;
    }
}
