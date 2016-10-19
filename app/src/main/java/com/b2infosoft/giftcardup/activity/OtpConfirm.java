package com.b2infosoft.giftcardup.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Alert;
import com.b2infosoft.giftcardup.app.GiftCardApp;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.services.ConnectivityReceiver;

public class OtpConfirm extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    Active active;
    Tags tags;
    private Button action_confirm;
    private EditText otp;
    private Alert alert;
    View main_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_confirm);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        otp = (EditText) findViewById(R.id.otp);
        action_confirm = (Button) findViewById(R.id.action_confirm);
        action_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        GiftCardApp.getInstance().setConnectivityListener(this);
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        alert.showSnackIsConnectedView(main_view, isConnected);
    }

    private void init() {
        active = Active.getInstance(this);
        tags = Tags.getInstance();
        alert = Alert.getInstance(this);
        main_view = findViewById(R.id.main_view);
    }

    private void confirm() {
        String otp_ = otp.getText().toString();
        otp.setError(null);
        if (TextUtils.isEmpty(otp_)) {
            otp.setError("Please Enter Otp Code");
            otp.requestFocus();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        if (active.getValue(tags.ONE_TIME_PASSWORD).equals(otp_)) {
            builder.setMessage("Successfully OTP Match");
            builder.setPositiveButton("CHANGE PASSWORD", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    goChangePassword();
                }
            });
        } else {
            builder.setMessage("OTP Can't Match");
            builder.setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                this.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private void goChangePassword() {
        startActivity(new Intent(this, PasswordChange.class));
    }


    private boolean isConnected() {
        return ConnectivityReceiver.isConnected();
    }
}
