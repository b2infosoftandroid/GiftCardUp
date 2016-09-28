package com.b2infosoft.giftcardup.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Config;
import com.b2infosoft.giftcardup.app.Format;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.custom.Progress;
import com.b2infosoft.giftcardup.model.Approve;
import com.b2infosoft.giftcardup.model.ContactInformation;
import com.b2infosoft.giftcardup.model.User;
import com.b2infosoft.giftcardup.urlconnection.MultipartUtility;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;
import com.b2infosoft.giftcardup.volly.LruBitmapCache;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileNew extends AppCompatActivity implements View.OnClickListener,DMRResult{
    final private static String TAG = ProfileNew.class.getName();
    private Active active;
    private Tags tags;
    private Urls urls;
    private Config config;
    private Format format;
    private DMRRequest dmrRequest;
    Progress progress;

    CollapsingToolbarLayout toolbarLayout;
    ImageView profile_image,arrow1,arrow2,arrow3,identity,bank,ssn;
    TextView member_science,total_sold,total_saving,mail,mobile,address;
    private final int PICK_IMAGE_REQUEST = 1;
    private Map<Integer,Integer> approveMap;
    private Uri filePath;
    private Bitmap bitmap;

    private void init() {
        active = Active.getInstance(this);
        tags = Tags.getInstance();
        urls = Urls.getInstance();
        config = Config.getInstance();
        format = Format.getInstance();
        dmrRequest = DMRRequest.getInstance(this,TAG);
        approveMap = new HashMap<>();
        approveMap.put(0,R.drawable.ic_u_pending);
        approveMap.put(1,R.drawable.ic_u_approved);
        approveMap.put(2,R.drawable.ic_u_rejected);
        approveMap.put(3,R.drawable.ic_u_expire);
        approveMap.put(4,R.drawable.ic_u_suspend);
        approveMap.put(9,R.drawable.ic_u_not_submitted);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.activity_profile_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        User user = active.getUser();

        //getSupportActionBar().setSubtitle("Member Since : ".concat(format.getDate(user.getJoinDate())));

        toolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(user.getFirstName()+" "+user.getLastName());

        profile_image = (ImageView)findViewById(R.id.profile_user_image);
        identity = (ImageView)findViewById(R.id.user_identity_approve);
        bank = (ImageView)findViewById(R.id.user_bank_approve);
        ssn = (ImageView)findViewById(R.id.user_ssn_approve);
        arrow1 = (ImageView)findViewById(R.id.identification_arrow);
        arrow1.setOnClickListener(this);
        arrow2 = (ImageView)findViewById(R.id.bank_arrow);
        arrow2.setOnClickListener(this);
        arrow3 = (ImageView)findViewById(R.id.ssn_arrow);
        arrow3.setOnClickListener(this);
        total_saving = (TextView)findViewById(R.id.total_saving);
        total_sold = (TextView)findViewById(R.id.total_sold);
        mail = (TextView)findViewById(R.id.profile_short_mail);
        mobile = (TextView)findViewById(R.id.profile_short_phone);
        address = (TextView)findViewById(R.id.profile_short_address);
       // member_science = (TextView)findViewById(R.id.profile_member);
        if (active.isLogin()) {
            mail.setText(user.getEmail());
        }

        LruBitmapCache.loadCacheImageProfile(this, profile_image, config.getUserProfileImageAddress().concat(user.getImage()), TAG);
        total_saving.setText("$"+user.getTotalSave());
        total_sold.setText("$"+user.getTotalSold());
       // member_science.setText("Member Since : ".concat(format.getDate(user.getJoinDate())));
         fetchContactInfo();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
    }

    private void setProfile(ContactInformation information) {
            mobile.setText(information.getPhoneNumber());
            address.setText(information.getAddressFull(this));
    }


    private void setIcons(Approve approve){
           identity.setImageResource(approveMap.get(approve.getIdentification()));
           bank.setImageResource(approveMap.get(approve.getBank()));
           ssn.setImageResource(approveMap.get(approve.getSsn()));
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
                if (profile_image != null)
                    profile_image.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        new updateImage().execute();
    }

    private class updateImage extends AsyncTask<String,String ,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progress.show();
        }

        @Override
        protected void onPostExecute(String s) {
            //progress.dismiss();
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {
            User user = active.getUser();
            String url = urls.getUserInfo();
            try {
                MultipartUtility multipart = new MultipartUtility(url);
                multipart.addHeaderField("User-Agent", "CodeJava");
                multipart.addHeaderField("Test-Header", "Header-Value");
                multipart.addFormField(tags.USER_ACTION, tags.UPDATE_PROFILE_PIC);
                multipart.addFormField(tags.USER_ID, active.getUser().getUserId() + "");
                multipart.addFilePartBitmap(tags.PROFILE_NAME, "bank_void_image.png", bitmap);
                multipart.addFormField(tags.PROFILE_OLD_NAME,user.getImage() );
                return multipart.finishString();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage() + "");
            }
            return "";
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.identification_arrow:
                  startActivity(new Intent(this, ProfileIdentification.class));
                break;
            case R.id.bank_arrow:
                startActivity(new Intent(this, ProfileBankDetails.class));
                break;
            case R.id.ssn_arrow:
                startActivity(new Intent(this, ProfileSsnEin.class));
                break;
            default:

        }
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        try {
            if (jsonObject.has(tags.SUCCESS)) {
                if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                    if(jsonObject.has(tags.USER_ALL_APPROVE_INFO)){
                        setIcons(Approve.fromJSON(jsonObject.getJSONObject(tags.USER_ALL_APPROVE_INFO)));
                    }
                    if (jsonObject.has(tags.USER_CONTACT_INFORMATION)) {
                        ContactInformation information = ContactInformation.fromJSON(jsonObject.getJSONObject(tags.USER_CONTACT_INFORMATION));
                        setProfile(information);
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

    private void fetchContactInfo() {
        if (active.isLogin()) {
            User user = active.getUser();

            /* LOADING USER DETAILS */
            Map<String, String> map1 = new HashMap<>();
            map1.put(tags.USER_ACTION, tags.USER_CONTACT_INFORMATION);
            map1.put(tags.USER_ID, user.getUserId() + "");
            dmrRequest.doPost(urls.getUserInfo(), map1, this);
        }
    }
}
