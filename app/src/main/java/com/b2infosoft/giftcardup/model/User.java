package com.b2infosoft.giftcardup.model;

import android.util.Log;

import com.b2infosoft.giftcardup.app.Tags;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rajesh on 8/11/2016.
 */
public class User {
    private int userId;
    private int employeeId;
    private int userType;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String gender;
    private String fbId;
    private String address;
    private String suiteNumber;
    private String city;
    private String zipCode;
    private String state;
    private String companyName;
    private String email;
    private String password;
    private String image;
    private String identifyImage;
    private int groupId;
    private String joinDate;
    private int emailVerifyStatus;
    private int approveStatus;
    private int verifyIdentity;
    private int referralAmount;
    private String payPalId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIdentifyImage() {
        return identifyImage;
    }

    public void setIdentifyImage(String identifyImage) {
        this.identifyImage = identifyImage;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public int getEmailVerifyStatus() {
        return emailVerifyStatus;
    }

    public void setEmailVerifyStatus(int emailVerifyStatus) {
        this.emailVerifyStatus = emailVerifyStatus;
    }

    public int getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(int approveStatus) {
        this.approveStatus = approveStatus;
    }

    public int getVerifyIdentity() {
        return verifyIdentity;
    }

    public void setVerifyIdentity(int verifyIdentity) {
        this.verifyIdentity = verifyIdentity;
    }

    public int getReferralAmount() {
        return referralAmount;
    }

    public void setReferralAmount(int referralAmount) {
        this.referralAmount = referralAmount;
    }

    public String getPayPalId() {
        return payPalId;
    }

    public void setPayPalId(String payPalId) {
        this.payPalId = payPalId;
    }

    public static User fromJSON(JSONObject object) {
        Tags tags = Tags.getInstance();
        User user = new User();
        try {
            if (object.has(tags.USER_ID)) {
                user.setUserId(object.getInt(tags.USER_ID));
            }
            if (object.has(tags.EMPLOYEE_ID)) {
                user.setEmployeeId(object.getInt(tags.EMPLOYEE_ID));
            }
            if (object.has(tags.USER_TYPE)) {
                user.setUserType(object.getInt(tags.USER_TYPE));
            }
            if (object.has(tags.FIRST_NAME)) {
                user.setFirstName(object.getString(tags.FIRST_NAME));
            }

            if (object.has(tags.LAST_NAME)) {
                user.setLastName(object.getString(tags.LAST_NAME));
            }
            if (object.has(tags.PHONE_NUMBER)) {
                user.setPhoneNumber(object.getString(tags.PHONE_NUMBER));
            }
            if (object.has(tags.GENDER)) {
                user.setGender(object.getString(tags.GENDER));
            }
            if (object.has(tags.FB_ID)) {
                user.setFbId(object.getString(tags.FB_ID));
            }
            if (object.has(tags.ADDRESS)) {
                user.setAddress(object.getString(tags.ADDRESS));
            }
            if (object.has(tags.SUITE_NUMBER)) {
                user.setSuiteNumber(object.getString(tags.SUITE_NUMBER));
            }
            if (object.has(tags.CITY)) {
                user.setCity(object.getString(tags.CITY));
            }
            if (object.has(tags.ZIP_CODE)) {
                user.setZipCode(object.getString(tags.ZIP_CODE));
            }
            if (object.has(tags.STATE)) {
                user.setState(object.getString(tags.STATE));
            }
            if (object.has(tags.COMPANY_NAME)) {
                user.setCompanyName(object.getString(tags.COMPANY_NAME));
            }
            if (object.has(tags.EMAIL)) {
                user.setEmail(object.getString(tags.EMAIL));
            }
            if (object.has(tags.PASSWORD)) {
                user.setPassword(object.getString(tags.PASSWORD));
            }
            if (object.has(tags.IMAGE)) {
                user.setImage(object.getString(tags.IMAGE));
            }
            if (object.has(tags.IDENTIFY_IMAGE)) {
                user.setIdentifyImage(object.getString(tags.IDENTIFY_IMAGE));
            }
            if (object.has(tags.GROUP_ID)) {
                user.setGroupId(object.getInt(tags.GROUP_ID));
            }
            if (object.has(tags.JOIN_DATE)) {
                user.setJoinDate(object.getString(tags.JOIN_DATE));
            }
            if (object.has(tags.EMAIL_VERIFY_STATUS)) {
                user.setEmailVerifyStatus(object.getInt(tags.EMAIL_VERIFY_STATUS));
            }
            if (object.has(tags.APPROVE_STATUS)) {
                user.setApproveStatus(object.getInt(tags.APPROVE_STATUS));
            }
            if (object.has(tags.VERIFY_IDENTITY)) {
                user.setVerifyIdentity(object.getInt(tags.VERIFY_IDENTITY));
            }
            if (object.has(tags.REFERRAL_AMOUNT)) {
                user.setReferralAmount(object.getInt(tags.REFERRAL_AMOUNT));
            }
            if (object.has(tags.PAY_PAL_ID)) {
                user.setPayPalId(object.getString(tags.PAY_PAL_ID));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("USER", e.getMessage());
        }
        return user;
    }
}
