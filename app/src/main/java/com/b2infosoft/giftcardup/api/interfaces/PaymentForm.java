package com.b2infosoft.giftcardup.api.interfaces;

/**
 * Created by rajesh on 9/26/2016.
 */
public interface PaymentForm {
    public String getCardNumber();
    public String getCvc();
    public Integer getExpMonth();
    public Integer getExpYear();
    public String getCurrency();
}
