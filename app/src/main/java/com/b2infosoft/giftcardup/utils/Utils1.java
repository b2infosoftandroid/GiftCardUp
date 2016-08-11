package com.b2infosoft.giftcardup.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.custom.BadgeDrawable;

/**
 * Created by rajesh on 7/28/2016.
 */

public class Utils1 {
    public static void setBadgeCount(Context context, LayerDrawable icon, int count) {
        BadgeDrawable badge;
        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }
        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge1, badge);
    }
}