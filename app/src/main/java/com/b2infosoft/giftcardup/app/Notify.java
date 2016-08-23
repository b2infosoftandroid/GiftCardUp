package com.b2infosoft.giftcardup.app;

/**
 * Created by Microsoft on 8/23/2016.
 */
public class Notify {

    private static Notify ourInstance = new Notify();
    int count = 0;

    public static Notify getInstance(){
        return ourInstance;
    }

    public int setValue(int count){
        return count;
    }

    public int getCount(){
        return count;
    }
}
