package com.b2infosoft.giftcardup.filters;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by rajesh on 9/7/2016.
 */

public class EditTextMaxInteger implements InputFilter{
    private int maxValue;

    public EditTextMaxInteger(int maxValue) {
        this.maxValue = maxValue;
    }
    public EditTextMaxInteger(String maxValue) {
        this.maxValue = Integer.parseInt(maxValue);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try{
            String replacement = source.subSequence(start, end).toString();
            String newVal = dest.toString().substring(0, dstart) + replacement +dest.toString().substring(dend, dest.toString().length());
            if(TextUtils.isEmpty(newVal)){
                return "";
            }
            int input = Integer.parseInt(newVal);
            if (input<maxValue)
                return null;
            Log.d("ADD",input+"");
        }catch (NumberFormatException e){
            e.printStackTrace();
            Log.d(EditTextMaxInteger.class.getName(),e.getMessage());
        }
        return null;
    }
}
