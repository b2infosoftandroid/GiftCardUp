package com.b2infosoft.giftcardup.services;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.b2infosoft.giftcardup.app.Validation;

/**
 * Created by rajesh on 9/17/2016.
 */
public class MyServices {

    public static void startLeftCartTimeService(Context context) {
        if (!Validation.isServiceRunning(context, CartStatus.class.getName())) {
            context.startService(new Intent(context, CartStatus.class));
        } else {
            showMessage(context);
        }
    }
    private static void showMessage(Context context) {
        Toast.makeText(context, "Already Start", Toast.LENGTH_SHORT).show();
    }
}
