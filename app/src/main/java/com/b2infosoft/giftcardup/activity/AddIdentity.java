package com.b2infosoft.giftcardup.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.urlconnection.MultipartUtility;
import java.io.IOException;


public class AddIdentity extends AppCompatActivity {
    private final static String TAG = AddIdentity.class.getName();
    private Active active;
    private Tags tags;
    private Urls urls;
    private Intent intent;
    //Image request code
    private final int PICK_IMAGE_REQUEST = 1;
    //Uri to store the image uri
    private Uri filePath;
    private Bitmap bitmap;
    private ImageView imageView;
    private Button choose, upload;

    private void init() {
        active = Active.getInstance(this);
        tags = Tags.getInstance();
        urls = Urls.getInstance();
        intent = new Intent(this, MyProfile.class);
        intent.putExtra(tags.SELECTED_TAB, 2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_identity);
        init();
        imageView = (ImageView) findViewById(R.id.identity_image);
        choose = (Button) findViewById(R.id.choose_identity);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        upload = (Button) findViewById(R.id.upload_identity);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap != null) {
                    new UpdateIdentity(bitmap).execute();
                } else {
                    Toast.makeText(AddIdentity.this, "Please Choose Identity", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        //replaceFragment();
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

    private class UpdateIdentity extends AsyncTask<String, String, String> {

        Bitmap bitmap;

        public UpdateIdentity(Bitmap bitmap) {
            this.bitmap = bitmap;
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
                multipart.addFormField(tags.USER_ACTION, tags.ADD_IDENTIFICATION);
                multipart.addFormField(tags.USER_ID, active.getUser().getUserId() + "");
                multipart.addFilePartBitmap(tags.BANK_VOID_IMAGE, "bank_void_image.png", bitmap);
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
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d(TAG,s);
            super.onPostExecute(s);
        }
    }
}
