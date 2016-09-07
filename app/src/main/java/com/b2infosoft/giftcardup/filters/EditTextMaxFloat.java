package com.b2infosoft.giftcardup.filters;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by rajesh on 9/7/2016.
 */

public class EditTextMaxFloat implements InputFilter{
    private EditText editText;
    private float maxValue;
    private String errorMessage;

    public EditTextMaxFloat(EditText editText, float maxValue, String errorMessage) {
        this.editText = editText;
        this.maxValue = maxValue;
        this.errorMessage = errorMessage;
    }

    public EditTextMaxFloat(EditText editText,String maxValue, String errorMessage) {
        this.editText = editText;
        this.maxValue = Float.parseFloat(maxValue);
        this.errorMessage = errorMessage;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try{
            editText.setError(null);
            String replacement = source.subSequence(start, end).toString();
            String newVal = dest.toString().substring(0, dstart) + replacement +dest.toString().substring(dend, dest.toString().length());
            if(TextUtils.isEmpty(newVal)){
                return null;
            }
            float input = Float.parseFloat(newVal);
            if (input<maxValue) {
                return null;
            }
            else{
                editText.setError(errorMessage);
                editText.requestFocus();
            }
            Log.d("MIN : ",input+" ,MAX"+maxValue);
        }catch (NumberFormatException e){
            e.printStackTrace();
            Log.d(EditTextMaxFloat.class.getName(),e.getMessage());
        }
        return "";
    }
}
