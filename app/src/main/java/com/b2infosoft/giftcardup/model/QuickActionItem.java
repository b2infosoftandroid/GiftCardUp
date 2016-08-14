package com.b2infosoft.giftcardup.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Microsoft on 8/13/2016.
 */
public class QuickActionItem {
    private String title;
    private Drawable iconImg;

    public QuickActionItem(String title, Drawable iconImg) {
        this.title = title;
        this.iconImg = iconImg;
    }

    public QuickActionItem(String title) {

        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable geticonImg() {
        return iconImg;
    }

    public void seticonImg(Drawable icon) {
        this.iconImg = icon;
    }
}
