package com.b2infosoft.giftcardup.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Config;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.model.BankInfo;
import com.b2infosoft.giftcardup.services.ConnectivityReceiver;
import com.b2infosoft.giftcardup.urlconnection.MultipartUtility;
import com.b2infosoft.giftcardup.volly.LruBitmapCache;

import java.io.IOException;

public class UpdateAccountInfo1 extends AppCompatActivity {
    private final String TAG = UpdateAccountInfo1.class.getName();
    private Tags tags;
    private Active active;
    private Urls urls;
    private Config config;
    //Image request code
    private final int PICK_IMAGE_REQUEST = 1;
    //Uri to store the image uri
    private Uri filePath;
    private Bitmap bitmap;
    private BankInfo info;

    Button save, chooseImage;
    String bank_name, bank_account, bank_routing;
    EditText name, routing_no, account_no;
    AppCompatImageView imageView;

    private void init() {
        tags = Tags.getInstance();
        active = Active.getInstance(getApplicationContext());
        urls = Urls.getInstance();
        config = Config.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.activity_update_account_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView = (AppCompatImageView) findViewById(R.id.void_check_image);

        if(getIntent().hasExtra(tags.BANK_INFO)){
            info = (BankInfo) getIntent().getSerializableExtra(tags.BANK_INFO);
            String url = config.getGiftCardImageAddress().concat(info.getVoidCheckImage1());
            Log.d("IMAGE",url);
            LruBitmapCache.loadCacheImage(this, imageView, config.getGiftCardImageAddress().concat(info.getVoidCheckImage1()), TAG);
        }

        name = (EditText) findViewById(R.id.bank_name);
        routing_no = (EditText) findViewById(R.id.bank_routing_no);
        account_no = (EditText) findViewById(R.id.bank_account_no);

        name.setText(info.getName());
        routing_no.setText(info.getRoutingNumber());
        account_no.setText(info.getAccountNumber());

        chooseImage = (Button) findViewById(R.id.choose_void_image);
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        save = (Button) findViewById(R.id.bank_save_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bank_name = name.getText().toString();
                bank_account = account_no.getText().toString();
                bank_routing = routing_no.getText().toString();
                BankInfo bankInfo = new BankInfo();
                bankInfo.setName(bank_name);
                bankInfo.setAccountNumber(bank_account);
                bankInfo.setRoutingNumber(bank_routing);
               // bankInfo.setVoidCheckImage1(bitmap);
                new UpdateBankAccount(bankInfo).execute();
            }
        });
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
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = this.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == this.RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                if (imageView != null)
                    imageView.setImageBitmap(bitmap);
                else {
                    Toast.makeText(this, "IMAGE NULL", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //replaceFragment();
    }

    private void getValues() {

    }

    private class UpdateBankAccount extends AsyncTask<String, String, String> {

        BankInfo bankInfo;

        public UpdateBankAccount(BankInfo bankInfo) {
            this.bankInfo = bankInfo;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = urls.getUserInfo();
            try {
                MultipartUtility multipart = new MultipartUtility(url);
                multipart.addHeaderField("User-Agent", "CodeJava");
                multipart.addHeaderField("Test-Header", "Header-Value");

                multipart.addFormField(tags.USER_ACTION, tags.BANK_ACCOUNT_UPDATE);
                multipart.addFormField(tags.BANK_INFO_ID, String.valueOf(info.getId()));
                multipart.addFormField(tags.BANK_INFO_NAME, bankInfo.getName());
                multipart.addFormField(tags.BANK_INFO_ACCOUNT_NUMBER,bankInfo.getAccountNumber());
                multipart.addFormField(tags.BANK_INFO_ROUTING_NUMBER, bankInfo.getRoutingNumber());
                if (bitmap != null) {
                    multipart.addFilePartBitmap(tags.BANK_VOID_IMAGE, "bank_void_image.png", bitmap);
                }
                return multipart.finishString();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage() + "");
            }

            return "";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            startActivity(new Intent(UpdateAccountInfo1.this,ProfileBankDetails.class));
            finish();
            super.onPostExecute(s);
        }
    }
    private boolean isConnected() {
        return ConnectivityReceiver.isConnected();
    }
}
