package com.b2infosoft.giftcardup.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.model.QuickActionItem;

/**
 * Created by Microsoft on 8/14/2016.
 */
public class Testexample extends AppCompatActivity {

    QuickAction quickAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        QuickActionItem item = new QuickActionItem("physics");
        QuickActionItem item1 = new QuickActionItem("chemistry");
        QuickActionItem item2 = new QuickActionItem("science");
        QuickActionItem item3 = new QuickActionItem("biology");

        quickAction  = new QuickAction(this,QuickAction.VERTICAL);

        quickAction.addActionItem(item);
        quickAction.addActionItem(item1);
        quickAction.addActionItem(item2);
        quickAction.addActionItem(item3);

        Button b1 = (Button)findViewById(R.id.btn_test);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quickAction.show(v);
            }
        });
    }
}
