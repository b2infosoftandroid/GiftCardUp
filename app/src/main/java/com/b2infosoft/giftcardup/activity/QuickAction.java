package com.b2infosoft.giftcardup.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.model.QuickActionItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Microsoft on 8/13/2016.
 */
public class QuickAction {

    private PopupWindow popupWindow;
    private int orientation;
    LayoutInflater  inflater;
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private Context context;
    List<QuickActionItem> actionItems = new ArrayList<>();

    public QuickAction() {

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    private void addActionItem(QuickActionItem item){
        actionItems.add(item);

        String title = item.getTitle();
        Drawable icon  = item.getIcon();

        View view  = null;
        if(orientation == VERTICAL){
              view = inflater.inflate(R.layout.quick_action_item_vertical,null);
        }

        TextView textView = (TextView)view.findViewById(R.id.text_demo);
        if(title != null){
            textView.setText(title);
        }
    }
}
