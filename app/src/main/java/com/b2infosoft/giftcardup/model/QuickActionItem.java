package com.b2infosoft.giftcardup.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Microsoft on 8/13/2016.
 */
public class QuickActionItem {
    private String title;
    private Drawable icon;

    public QuickActionItem(String title, Drawable icon) {
        this.title = title;
        this.icon = icon;
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

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
