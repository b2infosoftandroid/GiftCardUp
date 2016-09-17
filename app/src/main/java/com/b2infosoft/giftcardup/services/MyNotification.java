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
    Notification myNotification;
    public MyNotification(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
    }

    public void setNotificationCartLeftTime(String message) {

        Intent intent = new Intent(context, ShoppingCart.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Notification.Builder builder = new Notification.Builder(context);

        builder.setAutoCancel(true );
        builder.setTicker("GiftCardUp Notification");
        builder.setContentTitle("Cart Item");
        builder.setContentText(message);
        builder.setSmallIcon(R.drawable.ic_logo);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
        //builder.setSubText("This is subtext...");   //API level 16
        builder.setNumber(1);
        //builder.build();

        myNotification = builder.getNotification();
        notificationManager.notify(100, myNotification);
    }
}
