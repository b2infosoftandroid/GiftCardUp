package com.b2infosoft.giftcardup.model;

import android.util.Log;

import com.b2infosoft.giftcardup.app.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by rajesh on 8/20/2016.
 */

public class CompanyBrand implements Serializable{
    private int companyID;
    private int catID;
    private String companyName;
    private int companyPercentage;
    private int sellingCommission;
    private String description;
    private String image;
    private String webSiteLink;
    private String phoneNumber;
    private int cardType;
    private int commissionType;
    private String allowCard;
    private String date;
    private int userID;
    private String status;
    private int count;
    private int discount;

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public int getCatID() {
        return catID;
    }

    public void setCatID(int catID) {
        this.catID = catID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getCompanyPercentage() {
        return companyPercentage;
    }

    public void setCompanyPercentage(int companyPercentage) {
        this.companyPercentage = companyPercentage;
    }

    public int getSellingCommission() {
        return sellingCommission;
    }

    public void setSellingCommission(int sellingCommission) {
        this.sellingCommission = sellingCommission;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWebSiteLink() {
        return webSiteLink;
    }

    public void setWebSiteLink(String webSiteLink) {
        this.webSiteLink = webSiteLink;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public int getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(int commissionType) {
        this.commissionType = commissionType;
    }

    public String getAllowCard() {
        return allowCard;
    }

    public void setAllowCard(String allowCard) {
        this.allowCard = allowCard;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
    public CompanyBrand fromJSON(JSONObject object){
        Tags tags = Tags.getInstance();
        CompanyBrand brand = new CompanyBrand();
        try{
            if(object.has(tags.COMPANY_BRAND_COMPANY_ID)){
                brand.setCompanyID(object.getInt(tags.COMPANY_BRAND_COMPANY_ID));
            }
            if(object.has(tags.COMPANY_BRAND_CAT_ID)){
                brand.setCatID(object.getInt(tags.COMPANY_BRAND_CAT_ID));
            }
            if(object.has(tags.COMPANY_BRAND_COMPANY_NAME)){
                brand.setCompanyName(object.getString(tags.COMPANY_BRAND_COMPANY_NAME));
            }
            if(object.has(tags.COMPANY_BRAND_COMPANY_PERCENTAGE)){
                brand.setCompanyPercentage(object.getInt(tags.COMPANY_BRAND_COMPANY_PERCENTAGE));
            }
            if(object.has(tags.COMPANY_BRAND_SELLING_COMMISSION)){
                brand.setSellingCommission(object.getInt(tags.COMPANY_BRAND_SELLING_COMMISSION));
            }
            if(object.has(tags.COMPANY_BRAND_DESCRIPTION)){
                brand.setDescription(object.getString(tags.COMPANY_BRAND_DESCRIPTION));
            }
            if(object.has(tags.COMPANY_BRAND_IMAGE)){
                brand.setImage(object.getString(tags.COMPANY_BRAND_IMAGE));
            }
            if(object.has(tags.COMPANY_BRAND_WEBSITE_LINK)){
                brand.setWebSiteLink(object.getString(tags.COMPANY_BRAND_WEBSITE_LINK));
            }
            if(object.has(tags.COMPANY_BRAND_PHONE_NUMBER)){
                brand.setPhoneNumber(object.getString(tags.COMPANY_BRAND_PHONE_NUMBER));
            }
            if(object.has(tags.COMPANY_BRAND_CARD_TYPE)){
                brand.setPhoneNumber(object.getString(tags.COMPANY_BRAND_CARD_TYPE));
            }
            if(object.has(tags.COMPANY_BRAND_COMMISSION_TYPE)){
                brand.setCommissionType(object.getInt(tags.COMPANY_BRAND_COMMISSION_TYPE));
            }
            if(object.has(tags.COMPANY_BRAND_ALLOW_CARD)){
                brand.setAllowCard(object.getString(tags.COMPANY_BRAND_ALLOW_CARD));
            }
            if(object.has(tags.COMPANY_BRAND_DATE)){
                brand.setDate(object.getString(tags.COMPANY_BRAND_DATE));
            }
            if(object.has(tags.COMPANY_BRAND_USER_ID)){
                brand.setUserID(object.getInt(tags.COMPANY_BRAND_USER_ID));
            }
            if(object.has(tags.COMPANY_BRAND_STATUS)){
                brand.setUserID(object.getInt(tags.COMPANY_BRAND_STATUS));
            }
            if(object.has(tags.TOTAL_CARD)){
                brand.setCount(object.getInt(tags.TOTAL_CARD));
            }
            if(object.has(tags.TOTAL_PERCENTAGE)){
                brand.setDiscount(object.getInt(tags.TOTAL_PERCENTAGE));
            }
        }catch (JSONException e){
            e.printStackTrace();
            Log.d(CompanyBrand.class.getName(),e.getMessage());
        }
        return brand;
    }
}
