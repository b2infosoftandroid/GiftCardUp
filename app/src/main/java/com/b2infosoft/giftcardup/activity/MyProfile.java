package com.b2infosoft.giftcardup.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.adapter.ViewPagerAdapter;
import com.b2infosoft.giftcardup.app.Config;
import com.b2infosoft.giftcardup.app.Format;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.fragments.profile.BankInformation;
import com.b2infosoft.giftcardup.fragments.profile.Identification;
import com.b2infosoft.giftcardup.fragments.profile.SsnEin;
import com.b2infosoft.giftcardup.fragments.profile.SsnEin1;
import com.b2infosoft.giftcardup.model.Approve;
import com.b2infosoft.giftcardup.model.User;
import com.b2infosoft.giftcardup.services.ConnectivityReceiver;
import com.b2infosoft.giftcardup.urlconnection.MultipartUtility;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;
import com.b2infosoft.giftcardup.volly.LruBitmapCache;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyProfile extends AppCompatActivity implements Identification.OnFragmentIdentification, BankInformation.OnFragmentBankInformation, SsnEin.OnFragmentSsnEin, SsnEin1.OnFragmentSsnEin, DMRResult {
    final private static String TAG = MyProfile.class.getName();
    private Active active;
    private Tags tags;
    private Urls urls;
    private Config config;
    private Format format;
    private DMRRequest dmrRequest;
    private final int PICK_IMAGE_REQUEST = 1;
    private Map<Integer,Integer> approveMap;
    static private TabLayout tabLayout;
    static private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    CircularImageView profile_image;
    ProgressBar profile_progress_bar;
    private Uri filePath;
    private Bitmap bitmap,oldFileName;

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
        setContentView(R.layout.activity_my_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        setSupportActionBar(toolbar);
       // getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.htab_viewpager);
        setUpViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.htab_tabs);
        tabLayout.setupWithViewPager(viewPager);
        setTabIcons();
        updateTabIcons();
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        if(getIntent().hasExtra(tags.SELECTED_TAB)){
            setSelectedTabIndex(getIntent().getIntExtra(tags.SELECTED_TAB,0));
        }
        setProfile();
    }
    private void setProfile(){
        User user = active.getUser();
        TextView member_science = (TextView)findViewById(R.id.profile_member);
        member_science.setGravity(Gravity.RIGHT);
        TextView total_saving = (TextView)findViewById(R.id.total_saving);
        TextView total_sold = (TextView)findViewById(R.id.total_sold);
        TextView profile_user_name = (TextView)findViewById(R.id.profile_user_name);

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        Toast.makeText(this,currentapiVersion + "",Toast.LENGTH_SHORT).show();
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.htab_collapse_toolbar);
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){

        } else{

        }

        profile_image = (CircularImageView)findViewById(R.id.profile_user_image);
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                showFileChooser();
            }
        });
        member_science.setText("Member Since: ".concat(format.getDate(user.getJoinDate())));
        total_saving.setText("$"+user.getTotalSave());
        total_sold.setText("$"+user.getTotalSold());
        profile_user_name.setText(user.getFirstName() + " " + user.getLastName());

        //LruBitmapCache.loadCacheImageProfile(this, profile_image, config.getUserProfileImageAddress().concat(user.getImage()), TAG);
        if(user.getImage().length()>0&&user.getImage().contains(".")) {
            LruBitmapCache.loadCacheImageProfile(this, profile_image, config.getUserProfileImageAddress().concat(user.getImage()), TAG);
        }

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
                if (profile_image != null)
                    profile_image.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        new UpdateProfile().execute();
    }

    private void setUpViewPager(ViewPager pager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Identification(), "ProfileIdentification");
        adapter.addFragment(new BankInformation(), "Bank details");
        adapter.addFragment(new SsnEin1(), "SSN/EIN");
        pager.setAdapter(adapter);
    }
    private void updateTabIcons(){
        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.USER_ALL_APPROVE_INFO);
        map.put(tags.USER_ID, active.getUser().getUserId() + "");
        dmrRequest.doPost(urls.getUserInfo(), map, this);
    }
    private void setTabIcons() {
        TextView tabOne = new TextView(this);
        tabOne.setText("ProfileIdentification");
        tabOne.setGravity(Gravity.CENTER);
        tabOne.setAllCaps(true);
        tabOne.setTextSize(10f);
        tabOne.setTextColor(getResources().getColor(R.color.profile_text_view_text));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_check_icon, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        tabOne = new TextView(this);
        tabOne.setText("Bank details");
        tabOne.setGravity(Gravity.CENTER);
        tabOne.setAllCaps(true);
        tabOne.setTextSize(10f);
        tabOne.setTextColor(getResources().getColor(R.color.profile_text_view_text));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_check_icon, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabOne);

        tabOne = new TextView(this);
        tabOne.setText("SSN/EIN");
        tabOne.setGravity(Gravity.CENTER);
        tabOne.setAllCaps(true);
        tabOne.setTextSize(10f);
        tabOne.setTextColor(getResources().getColor(R.color.profile_text_view_text));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_check_icon, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabOne);
    }
    private void setTabIcons(Approve approve) {
        tabLayout.getTabAt(0).setCustomView(null);
        tabLayout.getTabAt(1).setCustomView(null);
        tabLayout.getTabAt(2).setCustomView(null);

        TextView tabOne = new TextView(this);
        tabOne.setText("ProfileIdentification");
        tabOne.setGravity(Gravity.CENTER);
        tabOne.setAllCaps(true);
        tabOne.setTextSize(10f);
        tabOne.setTextColor(getResources().getColor(R.color.profile_text_view_text));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, approveMap.get(approve.getIdentification()), 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        tabOne = new TextView(this);
        tabOne.setText("Bank details");
        tabOne.setGravity(Gravity.CENTER);
        tabOne.setAllCaps(true);
        tabOne.setTextSize(10f);
        tabOne.setTextColor(getResources().getColor(R.color.profile_text_view_text));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, approveMap.get(approve.getBank()), 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabOne);

        tabOne = new TextView(this);
        tabOne.setText("SSN/EIN");
        tabOne.setGravity(Gravity.CENTER);
        tabOne.setAllCaps(true);
        tabOne.setTextSize(10f);
        tabOne.setTextColor(getResources().getColor(R.color.profile_text_view_text));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, approveMap.get(approve.getSsn()), 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabOne);
    }

    public static void setSelectedTabIndex(final int tabIndex) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tabLayout.getTabAt(tabIndex).select();
                viewPager.setCurrentItem(tabIndex);
            }
        }, 100);
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        try {
            if (jsonObject.has(tags.SUCCESS)) {
                if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                    if(jsonObject.has(tags.USER_ALL_APPROVE_INFO)){
                        setTabIcons(Approve.fromJSON(jsonObject.getJSONObject(tags.USER_ALL_APPROVE_INFO)));
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
        if (volleyError.getMessage() != null)
            Log.e(TAG,volleyError.getMessage());
    }

    @Override
    public void onBankInformation(Uri uri) {

    }

    @Override
    public void onIdentification(Uri uri) {

    }

    @Override
    public void onSsnEin(Uri uri) {

    }

    private class UpdateProfile extends AsyncTask<String,String ,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
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
    private boolean isConnected() {
         return ConnectivityReceiver.isConnected();
    }
}
