package com.b2infosoft.giftcardup.model;

import android.graphics.Bitmap;

/**
 * Created by Microsoft on 9/1/2016.
 */
public class AddNewAccount {
    private String bankName;
    private String bankAccountNo;
    private String bankRountingNo;
    private Bitmap voidImage;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getBankRountingNo() {
        return bankRountingNo;
    }

    public void setBankRountingNo(String bankRountingNo) {
        this.bankRountingNo = bankRountingNo;
    }

    public Bitmap getVoidImage() {
        return voidImage;
    }

    public void setVoidImage(Bitmap voidImage) {
        this.voidImage = voidImage;
    }

}
