package com.b2infosoft.giftcardup.services;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by Microsoft on 9/20/2016.
 */
public class MyGcmListenerService extends GcmListenerService {
    private static final String TAG = MyGcmListenerService.class.getName();

    @Override
    public void onMessageReceived(String s, Bundle bundle) {
        super.onMessageReceived(s, bundle);
        String message = bundle.getString("Hello");

        sendNotification(message);
    }

    private void sendNotification(String msg){


    }
}

