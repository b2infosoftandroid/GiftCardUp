package com.b2infosoft.giftcardup.activity;

import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.adapter.ViewPagerAdapter;
import com.b2infosoft.giftcardup.fragments.profile.BankInformation;
import com.b2infosoft.giftcardup.fragments.profile.Identification;
import com.b2infosoft.giftcardup.fragments.profile.SsnEin;

public class MyProfile extends AppCompatActivity implements Identification.OnFragmentIdentification, BankInformation.OnFragmentBankInformation, SsnEin.OnFragmentSsnEin {
    static private TabLayout tabLayout;
    static private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        setSupportActionBar(toolbar);
*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.htab_viewpager);
        setUpViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.htab_tabs);
        tabLayout.setupWithViewPager(viewPager);
        setTabIcons();
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
    }

    private void setUpViewPager(ViewPager pager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Identification(), "Identification");
        adapter.addFragment(new BankInformation(), "Bank details");
        adapter.addFragment(new SsnEin(), "SSN/EIN");
        pager.setAdapter(adapter);
    }

    private void setTabIcons() {
        TextView tabOne = new TextView(this);
        tabOne.setText("Identification");
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
    public void onBankInformation(Uri uri) {

    }

    @Override
    public void onIdentification(Uri uri) {

    }

    @Override
    public void onSsnEin(Uri uri) {

    }
}
