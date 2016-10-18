package com.b2infosoft.giftcardup.app;

import android.app.Application;

import com.b2infosoft.giftcardup.model.Cart;
import com.b2infosoft.giftcardup.services.ConnectivityReceiver;

/**
 * Created by rajesh on 10/17/2016.
 */
public class GiftCardApp extends Application {
    private static GiftCardApp  mInstance;
    private Cart cart;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized GiftCardApp getInstance(){
        return mInstance;
    }

    public Cart getCart() {
        if (cart==null){
            return new Cart();
        }
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
