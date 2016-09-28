package com.b2infosoft.giftcardup.activity;

import android.app.Activity;
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
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.model.AddNewAccount;
import com.b2infosoft.giftcardup.urlconnection.MultipartUtility;

import java.io.IOException;

public class AddAccountInfo1 extends AppCompatActivity {
    private final String TAG = AddAccountInfo1.class.getName();
    private Tags tags;
    private Active active;
    private Urls urls;
    //Image request code
    private final int PICK_IMAGE_REQUEST = 1;
    //Uri to store the image uri
    private Uri filePath;
    private Bitmap bitmap;

    Button save, chooseImage;
    String bank_name, bank_account, bank_routing;
    EditText name, routing_no, account_no;
    AppCompatImageView imageView;

    private void init() {
        tags = Tags.getInstance();
        active = Active.getInstance(getApplicationContext());
        urls = Urls.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.activity_add_account_info);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        name = (EditText) findViewById(R.id.bank_name);
        routing_no = (EditText) findViewById(R.id.bank_routing_no);
        account_no = (EditText) findViewById(R.id.bank_account_no);
        imageView = (AppCompatImageView) findViewById(R.id.void_check_image);
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
                AddNewAccount account = new AddNewAccount();
                account.setBankName(bank_name);
                account.setBankAccountNo(bank_account);
                account.setBankRountingNo(bank_routing);
                account.setVoidImage(bitmap);
                new AddBankAccount(account).execute();
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
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
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
    }

    private class AddBankAccount extends AsyncTask<String, String, String> {

        AddNewAccount newAccount;

        public AddBankAccount(AddNewAccount newAccount) {
            this.newAccount = newAccount;
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

                multipart.addFormField(tags.USER_ACTION, tags.BANK_ACCOUNT_ADD);
                multipart.addFormField(tags.BANK_NAME, newAccount.getBankName());
                multipart.addFormField(tags.USER_ID, active.getUser().getUserId() + "");
                multipart.addFormField(tags.BANK_ACCOUNT_NUMBER, newAccount.getBankAccountNo());
                multipart.addFormField(tags.BANK_ROUTING_NUMBER, newAccount.getBankRountingNo());
                if (newAccount.getVoidImage() != null) {
                    multipart.addFilePartBitmap(tags.BANK_VOID_IMAGE, "bank_void_image.png", newAccount.getVoidImage());
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
            startActivity(new Intent(AddAccountInfo1.this,ProfileBankDetails.class));
            super.onPostExecute(s);
        }
    }
}
