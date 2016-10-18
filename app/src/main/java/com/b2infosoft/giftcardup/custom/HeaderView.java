package com.b2infosoft.giftcardup.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;

import android.widget.TextView;

import com.b2infosoft.giftcardup.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rajesh on 10/13/2016.
 */
public class HeaderView extends LinearLayout {
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.member_since)
    TextView member_since;

    public HeaderView(Context context) {
        super(context);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void bindTo(String name, String lastSeen) {
        this.title.setText(name);
        this.member_since.setText(lastSeen);
    }

    public void setTextSize(float size) {
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }
}
