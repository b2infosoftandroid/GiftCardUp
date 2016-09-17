package com.b2infosoft.giftcardup.services;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.activity.ShoppingCart;

/**
 * Created by rajesh on 9/17/2016.
 */
public class MyNotification {
    Context context;
    NotificationManager notificationManager;

    public MyNotification(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
    }

    public void setNotificationCartLeftTime(String title, String message) {
        /*
        Notification notification = new Notification(R.drawable.ic_logo, "Cart Alert", System.currentTimeMillis());
        Intent intent = new Intent(context, ShoppingCart.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        notification.setLatestEventInfo(context, title, message, pendingIntent);
        notificationManager.notify(9999, notification);
        */
    }
}
