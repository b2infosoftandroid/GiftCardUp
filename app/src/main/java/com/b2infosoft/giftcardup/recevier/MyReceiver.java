package com.b2infosoft.giftcardup.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.b2infosoft.giftcardup.activity.ShoppingCart;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ShoppingCart.class.getName())){
            Toast.makeText(context,"Working Shopping  Cart",Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(context,"Working Shopping  Cart",Toast.LENGTH_SHORT).show();
    }
}
