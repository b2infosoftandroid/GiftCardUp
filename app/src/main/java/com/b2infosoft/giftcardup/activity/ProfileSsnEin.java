package com.b2infosoft.giftcardup.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.custom.AlertBox;
import com.b2infosoft.giftcardup.model.Approve;
import com.b2infosoft.giftcardup.services.ConnectivityReceiver;
import com.b2infosoft.giftcardup.urlconnection.MultipartUtility;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileSsnEin extends AppCompatActivity implements DMRResult {

    private final String TAG = ProfileSsnEin.class.getName();
    private Active active;
    private Tags tags;
    private Urls urls;
    private DMRRequest dmrRequest;
    private final int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    private Bitmap bitmap;
    AlertDialog.Builder dialog;

    Button identification_button, save, cancel;

    ImageView identification_image;
    RadioButton idTypeSSN, idTypeEIN;
    EditText ssn_ein;
    ScrollView scroll_view_step_one;
    LinearLayout approve_for_selling_layout;

    private void init() {
        active = Active.getInstance(this);
        tags = Tags.getInstance();
        urls = Urls.getInstance();
        dmrRequest = DMRRequest.getInstance(this, TAG);
        dialog = new AlertDialog.Builder(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssn_ein);
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        scroll_view_step_one = (ScrollView) findViewById(R.id.scroll_view_step_one);
        approve_for_selling_layout = (LinearLayout) findViewById(R.id.approve_for_selling_layout);
        identification_button = (Button) findViewById(R.id.ssn_ein_identification_button);
        identification_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        identification_image = (ImageView) findViewById(R.id.ssn_ein_identification_image);
        ssn_ein = (EditText) findViewById(R.id.ssn_ein_no);
        idTypeSSN = (RadioButton) findViewById(R.id.id_type_ssn);
        idTypeEIN = (RadioButton) findViewById(R.id.id_type_ein);
        cancel = (Button) findViewById(R.id.bank_cancel_btn);
        save = (Button) findViewById(R.id.bank_save_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ssnName = ssn_ein.getText().toString();
                String idType = "";
                if (idTypeEIN.isSelected()) {
                    idType = "SSN";
                } else {
                    idType = "EIN";
                }
                if (bitmap == null) {
                    Toast.makeText(getApplicationContext(), "Please Select ProfileIdentification Image", Toast.LENGTH_SHORT).show();
                }
                new AddInformation(bitmap, ssnName, idType).execute();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
    public void onSuccess(JSONObject jsonObject) {
        try {
            if (jsonObject.has(tags.SUCCESS)) {
                if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {

                } else if (jsonObject.getInt(tags.SUCCESS) == tags.FAIL) {

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onError(VolleyError volleyError) {
        volleyError.printStackTrace();
        if (volleyError.getMessage() != null)
            Log.e(TAG,volleyError.getMessage());
    }

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
                if (identification_image != null)
                    identification_image.setImageBitmap(bitmap);
                else {
                    Toast.makeText(this, "IMAGE NULL", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class AddInformation extends AsyncTask<String, String, String> {
        private Bitmap image;
        private String ssnNumber;
        private String idType;

        public AddInformation(Bitmap image, String ssnNumber, String idType) {
            this.image = image;
            this.ssnNumber = ssnNumber;
            this.idType = idType;
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

                multipart.addFormField(tags.USER_ACTION, tags.ADD_IDENTIFICATION_INFO);
                multipart.addFormField(tags.USER_ID, active.getUser().getUserId() + "");
                multipart.addFormField(tags.SSN_EIN, ssnNumber);
                multipart.addFormField(tags.ID_TYPE, idType);

                multipart.addFilePartBitmap(tags.BANK_VOID_IMAGE, "identification.png", image);
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
            Log.d("OUTPUT", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.has(tags.SUCCESS)) {
                    if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                        dialog.setMessage("Successfully Submitted");
                        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(ProfileSsnEin.this, ProfileNew.class));
                                finish();
                            }
                        });
                    } else if (jsonObject.getInt(tags.SUCCESS) == tags.FAIL) {
                        dialog.setMessage("Try Again");
                        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                    }
                }
                dialog.create().show();
            } catch (JSONException e) {

            }
            super.onPostExecute(s);

        }
    }
    private boolean isConnected() {
        return ConnectivityReceiver.isConnected();
    }
}
