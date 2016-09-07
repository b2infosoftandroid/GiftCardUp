package com.b2infosoft.giftcardup.custom;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by rajesh on 9/7/2016.
 */
public class Progress extends ProgressDialog {
    public Progress(Context context) {
        super(context);
        setCancelable(true);
        setMessage("Wait...");
        setProgressStyle(STYLE_SPINNER);
        setProgress(0);
        setMax(100);
    }
}
