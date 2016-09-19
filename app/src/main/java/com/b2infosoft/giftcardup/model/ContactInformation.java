package com.b2infosoft.giftcardup.model;

import android.content.Context;
import android.util.Log;

import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.database.DBHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by rajesh on 8/26/2016.
 */

public class ContactInformation implements Serializable {
    private int contactID;
    private int userID;
    private String phoneNumber;
    private String address;
    private String suiteNumber;
    private String city;
    private String zipCode;
    private String state;
    private String companyName;
    private String date;

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSuiteNumber() {
        return suiteNumber;
    }

    public void setSuiteNumber(String suiteNumber) {
        this.suiteNumber = suiteNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddressFull(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        State state = dbHelper.getStateByAbbreviation(getState());
        StringBuffer address = new StringBuffer();
        address.append(getAddress() + ",");
        address.append(getSuiteNumber() + ",");
        address.append(getCity() + ",");
        address.append(state.getName() + ",");
        address.append(getZipCode());
        return address.toString();
    }

    public static ContactInformation fromJSON(JSONObject object) {
        Tags tags = Tags.getInstance();
        ContactInformation information = new ContactInformation();
        try {
            if (object.has(tags.CONTACT_INFO_CONTACT_ID)) {
                information.setContactID(object.getInt(tags.CONTACT_INFO_CONTACT_ID));
            }
            if (object.has(tags.CONTACT_INFO_USER_ID)) {
                information.setUserID(object.getInt(tags.CONTACT_INFO_USER_ID));
            }
            if (object.has(tags.CONTACT_INFO_PHONE_NUMBER)) {
                information.setPhoneNumber(object.getString(tags.CONTACT_INFO_PHONE_NUMBER));
            }
            if (object.has(tags.CONTACT_INFO_ADDRESS)) {
                information.setAddress(object.getString(tags.CONTACT_INFO_ADDRESS));
            }
            if (object.has(tags.CONTACT_INFO_SUITE_NUMBER)) {
                information.setSuiteNumber(object.getString(tags.CONTACT_INFO_SUITE_NUMBER));
            }
            if (object.has(tags.CONTACT_INFO_CITY)) {
                information.setCity(object.getString(tags.CONTACT_INFO_CITY));
            }
            if (object.has(tags.CONTACT_INFO_ZIP_CODE)) {
                information.setZipCode(object.getString(tags.CONTACT_INFO_ZIP_CODE));
            }
            if (object.has(tags.CONTACT_INFO_STATE)) {
                information.setState(object.getString(tags.CONTACT_INFO_STATE));
            }
            if (object.has(tags.CONTACT_INFO_COMPANY_NAME)) {
                information.setCompanyName(object.getString(tags.CONTACT_INFO_COMPANY_NAME));
            }
            if (object.has(tags.CONTACT_INFO_DATE)) {
                information.setDate(object.getString(tags.CONTACT_INFO_DATE));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(ContactInformation.class.getName(), e.getMessage());
        }
        return information;
    }
}
