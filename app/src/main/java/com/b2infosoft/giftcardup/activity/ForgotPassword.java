package com.b2infosoft.giftcardup.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.b2infosoft.giftcardup.R;

public class ForgotPassword extends AppCompatActivity {

    EditText old_pass,new_pass,confirm_new_pass;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        old_pass = (EditText)findViewById(R.id.forgot_old_pass);
        new_pass = (EditText)findViewById(R.id.forgot_new_pass);
        confirm_new_pass = (EditText)findViewById(R.id.forgot_confirm_new_pass);
        submit = (Button)findViewById(R.id.forgot_submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
