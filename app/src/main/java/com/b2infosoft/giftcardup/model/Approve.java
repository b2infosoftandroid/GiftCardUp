package com.b2infosoft.giftcardup.model;

import android.util.Log;

import com.b2infosoft.giftcardup.app.Tags;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rajesh on 9/2/2016.
 */

public class Approve {
    private int identification;
    private int bank;
    private int ssn;
    private int email;
    /**
    * 0 => Pending Approval
    * 1 => Approved
    * 2 => Rejected
    * 4 => Suspended
    * 9 => Not Submitted
    * @return int
    * */
    public int getSsn() {
        return ssn;
    }

    public void setSsn(int ssn) {
        this.ssn = ssn;
    }
    /**
    * 0 => Pending Approval
    * 1 => Approved
    * 2 => Rejected
    * 4 => Suspended
    * 9 => Not Submitted
    * @return int
    * */
    public int getBank() {
        return bank;
    }

    public void setBank(int bank) {
        this.bank = bank;
    }
    /**
    * 0 => Pending Approval
    * 1 => Approved
    * 2 => Rejected
    * 3 => Expired
    * 4 => Suspended
    * 9 => Not Submitted
    * @return int
    * */
    public int getIdentification() {
        return identification;
    }

    public void setIdentification(int identification) {
        this.identification = identification;
    }

    public int getEmail() {
        return email;
    }

    public void setEmail(int email) {
        this.email = email;
    }

    public static Approve fromJSON(JSONObject object) {
        Tags tags = Tags.getInstance();
        Approve approve = new Approve();
        try {
            if (object.has(tags.USER_IDENTIFICATION)) {
                approve.setIdentification(object.getInt(tags.USER_IDENTIFICATION));
            }
            if (object.has(tags.USER_BANK)) {
                approve.setBank(object.getInt(tags.USER_BANK));
            }
            if (object.has(tags.USER_SSN)) {
                approve.setSsn(object.getInt(tags.USER_SSN));
            }
            if (object.has(tags.EMAIL_VERIFY_STATUS)) {
                approve.setEmail(object.getInt(tags.EMAIL_VERIFY_STATUS));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(Approve.class.getName(), e.getMessage());
        }
        return approve;
    }
}
