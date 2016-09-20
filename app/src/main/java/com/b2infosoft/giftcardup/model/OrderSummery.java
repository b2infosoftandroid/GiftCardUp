package com.b2infosoft.giftcardup.model;

/**
 * Created by rajesh on 9/20/2016.
 */

public class OrderSummery {
    private float price;
    private float shipping;
    private float discount;
    private float discountPercentage;

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getShipping() {
        return shipping;
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
        return (price + shipping) - discount;
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
}
