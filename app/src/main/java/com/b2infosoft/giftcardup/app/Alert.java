package com.b2infosoft.giftcardup.app;

import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.b2infosoft.giftcardup.R;

/**
 * Created by rajesh on 10/18/2016.
 */
public class Alert {
    private Activity activity;

    public static Alert getInstance(Activity activity) {
        return new Alert(activity);
    }

    private Alert(Activity activity) {
        this.activity = activity;
    }

    // Showing the status in Snackbar
    public void showSnackIsConnected(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = activity.getResources().getString(R.string.is_connected);
            color = Color.WHITE;
        } else {
            message = activity.getResources().getString(R.string.is_disconnected);
            color = Color.RED;
        }
        Snackbar snackbar = Snackbar.make(activity.findViewById(R.id.toolbar), message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }
    public void showSnack(boolean isConnected,String message) {
        int color;
        if (isConnected) {
            color = Color.WHITE;
        } else {
            color = Color.RED;
        }
        Snackbar snackbar = Snackbar.make(activity.findViewById(R.id.toolbar), message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    public void showToastIsConnected(boolean isConnected){
        String message;
        if (isConnected) {
            message = activity.getResources().getString(R.string.is_connected);
        } else {
            message = activity.getResources().getString(R.string.is_disconnected);
        }
        Toast.makeText(activity,message,Toast.LENGTH_SHORT).show();
    }
    public void showToast(String message){
        Toast.makeText(activity,message,Toast.LENGTH_SHORT).show();
    }
}
