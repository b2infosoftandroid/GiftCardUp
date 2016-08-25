package com.b2infosoft.giftcardup.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.b2infosoft.giftcardup.R;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    EditText f_name,l_name,email,mobile,password,password_confirm;
    EditText address,suite_no,city,state,zip_code,company_name;
    Button next,back,create_acct;
    LinearLayout layout1,layout2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        f_name = (EditText)findViewById(R.id.sign_up_name_first);
        l_name = (EditText)findViewById(R.id.sign_up_name_last);
        email = (EditText)findViewById(R.id.sign_up_email);
        mobile = (EditText)findViewById(R.id.sign_up_mobile_number);
        password = (EditText)findViewById(R.id.sign_up_password);
        password_confirm = (EditText)findViewById(R.id.sign_up_password_confirm);
        next = (Button)findViewById(R.id.sign_up_action_next);
        next.setOnClickListener(this);
        layout2 = (LinearLayout)findViewById(R.id.sign_up_ViewSecond);
        layout1 = (LinearLayout)findViewById(R.id.sign_up_ViewFirst);

        address = (EditText)findViewById(R.id.sign_up_address);
        suite_no = (EditText)findViewById(R.id.sign_up_suite_number);
        city = (EditText)findViewById(R.id.sign_up_city);
        state = (EditText)findViewById(R.id.sign_up_state);
        zip_code = (EditText)findViewById(R.id.sign_up_zip_code);
        company_name = (EditText)findViewById(R.id.sign_up_company_name);
        back = (Button)findViewById(R.id.sign_up_action_back);
        create_acct = (Button)findViewById(R.id.sign_up_action_create_account);
        back.setOnClickListener(this);
        create_acct.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up_action_next:
                layout2.setVisibility(View.VISIBLE);
                layout1.setVisibility(View.GONE);
                break;
            case R.id.sign_up_action_back:
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.GONE);
                break;
            case R.id.sign_up_action_create_account:

                break;
            default:
                break;
        }
    }


}
