package com.b2infosoft.giftcardup.custom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.b2infosoft.giftcardup.R;

/**
 * Created by rajesh on 8/11/2016.
 */
public class AlertBox{
    AlertDialog.Builder builder;
    public AlertBox(Context context) {
        builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }
    public void setTitle(String title){
        builder.setTitle(title);
    }
    public void setMessage(String message){
        builder.setMessage(message);
    }
    public void show(){
        builder.create().show();
    }
}
