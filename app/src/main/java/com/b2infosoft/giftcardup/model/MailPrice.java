package com.b2infosoft.giftcardup.model;

import android.util.Log;

import com.b2infosoft.giftcardup.app.Tags;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rajesh on 9/19/2016.
 */

public class MailPrice {
    private String firstClass ;
    private String priorityMail ;
    private String expressMail ;

    public String getFirstClass() {
        return firstClass;
    }

    public void setFirstClass(String firstClass) {
        this.firstClass = firstClass;
    }

    public String getPriorityMail() {
        return priorityMail;
    }

    public void setPriorityMail(String priorityMail) {
        this.priorityMail = priorityMail;
    }

    public String getExpressMail() {
        return expressMail;
    }

    public void setExpressMail(String expressMail) {
        this.expressMail = expressMail;
    }

    public static MailPrice fromJSON(JSONObject object) {
        Tags tags = Tags.getInstance();
        MailPrice price = new MailPrice();
        try {
            if (object.has(tags.FIRST_CLASS_PRICE)) {
                price.setFirstClass(object.getString(tags.FIRST_CLASS_PRICE));
            }
            if (object.has(tags.PRIORITY_PRICE)) {
                price.setPriorityMail(object.getString(tags.PRIORITY_PRICE));
            }
            if (object.has(tags.EXPRESS_PRICE)) {
                price.setExpressMail(object.getString(tags.EXPRESS_PRICE));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(MailPrice.class.getName(), e.getMessage());
        }
        return price;
    }
}
