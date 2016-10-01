package com.b2infosoft.giftcardup.model;

import android.util.Log;

import com.b2infosoft.giftcardup.app.Tags;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Microsoft on 10/1/2016.
 */
public class UserBalance {
    float available_fund;
    float total_saving;
    float total_sold;

    public float getAvailable_fund() {
        return available_fund;
    }

    public void setAvailable_fund(float available_fund) {
        this.available_fund = available_fund;
    }

    public float getTotal_saving() {
        return total_saving;
    }

    public void setTotal_saving(float total_saving) {
        this.total_saving = total_saving;
    }

    public float getTotal_sold() {
        return total_sold;
    }

    public void setTotal_sold(float total_sold) {
        this.total_sold = total_sold;
    }

    public static UserBalance fromJSON(JSONObject object) {
        UserBalance balance = new UserBalance();
        Tags tags = Tags.getInstance();
        try {
            if (object.has(tags.BALANCE)) {
                balance.setAvailable_fund((float) object.getDouble(tags.BALANCE));
            }
            if (object.has(tags.TOTAL_SAVE)) {
                balance.setTotal_saving((float) object.getDouble(tags.TOTAL_SAVE));
            }
            if (object.has(tags.TOTAL_SOLD)) {
                balance.setTotal_sold((float) object.getDouble(tags.TOTAL_SOLD));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(UserBalance.class.getName(), e.getMessage());
        }
        return balance;
    }
}
