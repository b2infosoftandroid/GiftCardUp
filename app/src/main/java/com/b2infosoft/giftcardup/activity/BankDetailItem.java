package com.b2infosoft.giftcardup.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.urlconnection.MultipartUtility;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.util.UUID;

public class BankDetailItem extends AppCompatActivity {

    private final String TAG = BankDetailItem.class.getName();
    private Tags tags;
    private Active active;
    private Urls urls;
    private final int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    private Bitmap bitmap;

    Button save, chooseImage;
    EditText name, routing_no, account_no, status;
    AppCompatImageView imageView;
    ImageView edit, less;
    TextView bankName;
    LinearLayout linearLayout;
    int count = 0;

    private void init() {
        tags = Tags.getInstance();
        active = Active.getInstance(this);
        urls = Urls.getInstance();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_detail_item);
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bankName = (TextView)findViewById(R.id.name);
        name = (EditText)findViewById(R.id.bank_name);
        routing_no = (EditText)findViewById(R.id.bank_routing_no);
        account_no = (EditText)findViewById(R.id.bank_account_no);
        status = (EditText)findViewById(R.id.bank_status);
        linearLayout = (LinearLayout)findViewById(R.id.layout_2);
        edit = (ImageView)findViewById(R.id.bank_info_edit);
        less = (ImageView)findViewById(R.id.bank_info_less);
        less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableInfo(false);
                save.setVisibility(View.GONE);
                count = count + 1;
                if (count % 2 != 0) {
                    linearLayout.setVisibility(View.GONE);
                    less.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_24dp));
                } else {
                    linearLayout.setVisibility(View.VISIBLE);
                    less.setImageDrawable(getResources().getDrawable(R.drawable.ic_subtract_24dp));
                }
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableInfo(true);
                save.setVisibility(View.VISIBLE);
            }
        });
        imageView = (AppCompatImageView)findViewById(R.id.void_check_image);
        chooseImage = (Button)findViewById(R.id.choose_void_image);
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        save = (Button)findViewById(R.id.bank_save_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddBankAccount().execute();
            }
        });
    }

    private void enableInfo(boolean isUpdate) {
        name.setEnabled(isUpdate);
        routing_no.setEnabled(isUpdate);
        account_no.setEnabled(isUpdate);
        chooseImage.setVisibility(View.VISIBLE);
        status.setEnabled(isUpdate);
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
        MyProfile.setSelectedTabIndex(1);
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
    }

    private void uploadMultipart(Bitmap bitmap) {
        String uploadId = UUID.randomUUID().toString();
        String path = getPath(filePath);
        String url = urls.getUserInfo();
        try {

            MultipartUtility multipart = new MultipartUtility(url);
            multipart.addHeaderField("User-Agent", "CodeJava");
            multipart.addHeaderField("Test-Header", "Header-Value");

            multipart.addFormField(tags.USER_ACTION, tags.BANK_ACCOUNT_ADD);
            multipart.addFormField(tags.BANK_NAME, "RAJESH");
            multipart.addFormField(tags.USER_ID, active.getUser().getUserId() + "");
            multipart.addFormField(tags.BANK_ACCOUNT_NUMBER, "123456");
            multipart.addFormField(tags.BANK_ROUTING_NUMBER, "654321");
            multipart.addFilePartBitmap(tags.BANK_VOID_IMAGE, "bank_void_image.png", bitmap);
            Log.d("OUTPUT", "" + multipart.finishString());

            MultipartUploadRequest uploadRequest = new MultipartUploadRequest(this, uploadId, url)
                    .addFileToUpload(path, tags.BANK_VOID_IMAGE)
                    .addParameter(tags.USER_ACTION, tags.BANK_ACCOUNT_ADD)
                    .addParameter(tags.BANK_NAME, "RAJESH")
                    .addParameter(tags.USER_ID, active.getUser().getUserId() + "")
                    .addParameter(tags.BANK_ACCOUNT_NUMBER, "123456")
                    .addParameter(tags.BANK_ROUTING_NUMBER, "654321")
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setMethod("POST");
            String str = uploadRequest.startUpload();
            Log.d(TAG, str);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage() + "");
        }

    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = this.getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private class AddBankAccount extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            uploadMultipart(bitmap);
            return null;
        }
    }

}
