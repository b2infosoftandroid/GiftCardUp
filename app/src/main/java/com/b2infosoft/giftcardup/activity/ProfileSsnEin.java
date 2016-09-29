package com.b2infosoft.giftcardup.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
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
import com.b2infosoft.giftcardup.urlconnection.MultipartUtility;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileSsnEin extends AppCompatActivity implements DMRResult{

    private final String TAG = ProfileSsnEin.class.getName();
    private Active active;
    private Tags tags;
    private Urls urls;
    private DMRRequest dmrRequest;
    private final int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    private Bitmap bitmap;

    Button approve_for_selling, identification_button, next_btn, save, cancel;
    Button verification_email, verification_identity, verification_ssn;
    TextView verification_email_status, verification_identity_status, verification_ssn_status;

    ImageView identification_image;
    RadioButton idTypeSSN, idTypeEIN;
    EditText ssn_ein;
    ScrollView scroll_view_step_one;
    LinearLayout approve_for_selling_layout, approve_status_layout;

    private HashMap<Integer, String> statusName = new HashMap<>();
    private HashMap<Integer, Integer> statusColor = new HashMap<>();

    private void init() {
        active = Active.getInstance(this);
        tags = Tags.getInstance();
        urls = Urls.getInstance();
        dmrRequest = DMRRequest.getInstance(this, TAG);
        statusName.put(0, "PENDING");
        statusName.put(1, "APPROVE");
        statusName.put(2, "REJECTED");
        statusName.put(3, "EXPIRED");
        statusName.put(4, "SUSPEND");
        statusName.put(9, "NOT SUBMITTED");

        statusColor.put(0, getResources().getColor(R.color.verification_pending));
        statusColor.put(1, getResources().getColor(R.color.verification_approve));
        statusColor.put(2, getResources().getColor(R.color.verification_rejected));
        statusColor.put(3, getResources().getColor(R.color.verification_expired));
        statusColor.put(4, getResources().getColor(R.color.verification_suspend));
        statusColor.put(9, getResources().getColor(R.color.verification_not_submitted));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssn_ein);
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        scroll_view_step_one = (ScrollView)findViewById(R.id.scroll_view_step_one);
        approve_for_selling_layout = (LinearLayout) findViewById(R.id.approve_for_selling_layout);
        approve_status_layout = (LinearLayout)findViewById(R.id.approve_status_layout);
        approve_for_selling = (Button) findViewById(R.id.ssn_ein_approved_for_sell);
        approve_for_selling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approve_for_selling.setVisibility(View.GONE);
                approve_for_selling_layout.setVisibility(View.VISIBLE);
            }
        });
        identification_button = (Button)findViewById(R.id.ssn_ein_identification_button);
        identification_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        identification_image = (ImageView)findViewById(R.id.ssn_ein_identification_image);
        ssn_ein = (EditText)findViewById(R.id.ssn_ein_no);
        idTypeSSN = (RadioButton)findViewById(R.id.id_type_ssn);
        idTypeEIN = (RadioButton)findViewById(R.id.id_type_ein);
        cancel = (Button)findViewById(R.id.bank_cancel_btn);
        save = (Button)findViewById(R.id.bank_save_btn);
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
                approve_for_selling.setVisibility(View.VISIBLE);
                approve_for_selling_layout.setVisibility(View.GONE);
            }
        });
        getApproveForSelling();

        verification_email = (Button)findViewById(R.id.verified_email);
        verification_identity = (Button)findViewById(R.id.verified_identity);
        verification_identity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileSsnEin.this, AddIdentity.class));
            }
        });

        verification_ssn = (Button)findViewById(R.id.verified_ssn);
        verification_ssn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileSsnEin.this, AddSSN.class));
            }
        });

        verification_email_status = (TextView)findViewById(R.id.verified_email_status);
        verification_identity_status = (TextView)findViewById(R.id.verified_identity_status);
        verification_ssn_status = (TextView)findViewById(R.id.verified_ssn_status);
        checkVerifiedStatus();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                this.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateVerifiedStatus(Approve approve) {
        verification_email_status.setText(statusName.get(approve.getEmail()));
        verification_email_status.setBackgroundColor(statusColor.get(approve.getEmail()));
        //       verification_email_status.setBackgroundColor(getResources().getColor());
        verification_identity_status.setText(statusName.get(approve.getIdentification()));
        verification_identity_status.setBackgroundColor(statusColor.get(approve.getIdentification()));
        verification_ssn_status.setText(statusName.get(approve.getSsn()));
        verification_ssn_status.setBackgroundColor(statusColor.get(approve.getSsn()));
        int identi = approve.getIdentification();
        if (identi != 0 && identi != 1) {
            verification_identity.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_publish_24dp, 0);
            verification_identity.setClickable(true);
        } else {
            verification_identity.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            verification_identity.setClickable(false);
        }
        int ssn = approve.getSsn();
        if (ssn != 0 && ssn != 1) {
            verification_ssn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_publish_24dp, 0);
            verification_ssn.setClickable(true);
        } else {
            verification_ssn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            verification_ssn.setClickable(false);
        }
    }

    private void checkVerifiedStatus() {
        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.USER_ALL_APPROVE_INFO);
        map.put(tags.USER_ID, active.getUser().getUserId() + "");
        dmrRequest.doPost(urls.getUserInfo(), map, this);
    }

    private void getApproveForSelling() {
        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.CHECK_APPROVE_FOR_SELLING);
        map.put(tags.USER_ID, active.getUser().getUserId() + "");
        dmrRequest.doPost(urls.getUserInfo(), map, this);
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        try {
            if (jsonObject.has(tags.SUCCESS)) {
                if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                    if (jsonObject.has(tags.CHECK_APPROVE_FOR_SELLING)) {
                        if (jsonObject.getInt(tags.CHECK_APPROVE_FOR_SELLING) == tags.PASS) {
                            approve_for_selling.setVisibility(View.GONE);
                            approve_status_layout.setVisibility(View.VISIBLE);
                        } else if (jsonObject.getInt(tags.CHECK_APPROVE_FOR_SELLING) == tags.FAIL) {
                            approve_for_selling.setVisibility(View.VISIBLE);
                            approve_status_layout.setVisibility(View.GONE);
                        }
                    }
                    if (jsonObject.has(tags.USER_ALL_APPROVE_INFO)) {
                        updateVerifiedStatus(Approve.fromJSON(jsonObject.getJSONObject(tags.USER_ALL_APPROVE_INFO)));
                    }
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
        Log.e(TAG, volleyError.getMessage());
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
                approve_for_selling.setVisibility(View.GONE);
                approve_for_selling_layout.setVisibility(View.VISIBLE);
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
            super.onPostExecute(s);

        }
    }
}
