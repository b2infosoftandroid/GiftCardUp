package com.b2infosoft.giftcardup.services;

import android.content.Intent;

import com.b2infosoft.giftcardup.activity.PaymentWithdrawalRequest;
import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Microsoft on 9/20/2016.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService{

    private final String TAG = MyInstanceIDListenerService.class.getName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Intent i = new Intent(this, PaymentWithdrawalRequest.class);
        startService(i);
    }
}
