package com.b2infosoft.giftcardup.model;

import android.util.Log;

import com.b2infosoft.giftcardup.app.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by rajesh on 9/1/2016.
 */
public class BankInfo implements Serializable {
    private int id;
    private int userID;
    private String idNumber;
    private String voidCheckImage1;
    private String voidCheckImage2;
    private String name;
    private String routingNumber;
    private String city;
    private String country;
    private String accountType;
    private String accountName;
    private String accountNumber;
    private String address;
    private String date;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getVoidCheckImage1() {
        return voidCheckImage1;
    }

    public void setVoidCheckImage1(String voidCheckImage1) {
        this.voidCheckImage1 = voidCheckImage1;
    }

    public String getVoidCheckImage2() {
        return voidCheckImage2;
    }

    public void setVoidCheckImage2(String voidCheckImage2) {
        this.voidCheckImage2 = voidCheckImage2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    /**
     * 0 = > Unapproved,
     * 1 = > Approved,
     * 2 = > Rejected,
     * 4 = > Suspend
     * @return 0,1,2,4
     *
     */
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static BankInfo fromJSON(JSONObject object) {
        Tags tags = Tags.getInstance();
        BankInfo bankInfo = new BankInfo();
        try {
            if (object.has(tags.BANK_INFO_ID)) {
                bankInfo.setId(object.getInt(tags.BANK_INFO_ID));
            }
            if (object.has(tags.BANK_INFO_USER_ID)) {
                bankInfo.setUserID(object.getInt(tags.BANK_INFO_USER_ID));
            }
            if (object.has(tags.BANK_INFO_ID_NUMBER)) {
                bankInfo.setIdNumber(object.getString(tags.BANK_INFO_ID_NUMBER));
            }
            if (object.has(tags.BANK_INFO_VOID_CHECK_IMAGE_1)) {
                bankInfo.setVoidCheckImage1(object.getString(tags.BANK_INFO_VOID_CHECK_IMAGE_1));
            }
            if (object.has(tags.BANK_INFO_VOID_CHECK_IMAGE_2)) {
                bankInfo.setVoidCheckImage2(object.getString(tags.BANK_INFO_VOID_CHECK_IMAGE_2));
            }
            if (object.has(tags.BANK_INFO_NAME)) {
                bankInfo.setName(object.getString(tags.BANK_INFO_NAME));
            }
            if (object.has(tags.BANK_INFO_ROUTING_NUMBER)) {
                bankInfo.setRoutingNumber(object.getString(tags.BANK_INFO_ROUTING_NUMBER));
            }
            if (object.has(tags.BANK_INFO_CITY)) {
                bankInfo.setCity(object.getString(tags.BANK_INFO_CITY));
            }
            if (object.has(tags.BANK_INFO_COUNTRY)) {
                bankInfo.setCountry(object.getString(tags.BANK_INFO_COUNTRY));
            }
            if (object.has(tags.BANK_INFO_ACCOUNT_TYPE)) {
                bankInfo.setAccountType(object.getString(tags.BANK_INFO_ACCOUNT_TYPE));
            }
            if (object.has(tags.BANK_INFO_ACCOUNT_NAME)) {
                bankInfo.setAccountName(object.getString(tags.BANK_INFO_ACCOUNT_NAME));
            }
            if (object.has(tags.BANK_INFO_ACCOUNT_NUMBER)) {
                bankInfo.setAccountNumber(object.getString(tags.BANK_INFO_ACCOUNT_NUMBER));
            }
            if (object.has(tags.BANK_INFO_ADDRESS)) {
                bankInfo.setAddress(object.getString(tags.BANK_INFO_ADDRESS));
            }
            if (object.has(tags.BANK_INFO_DATE)) {
                bankInfo.setDate(object.getString(tags.BANK_INFO_DATE));
            }
            if (object.has(tags.BANK_INFO_STATUS)) {
                bankInfo.setStatus(object.getInt(tags.BANK_INFO_STATUS));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(BankInfo.class.getName(), e.getMessage());
        }
        return bankInfo;
    }
}
