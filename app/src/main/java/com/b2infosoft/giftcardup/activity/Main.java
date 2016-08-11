package com.b2infosoft.giftcardup.activity;

import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.fragments.Profile;
import com.b2infosoft.giftcardup.fragments.profile.BankInformation;
import com.b2infosoft.giftcardup.fragments.profile.Identification;
import com.b2infosoft.giftcardup.fragments.profile.SsnEin;
import com.b2infosoft.giftcardup.utils.Utils1;
import com.b2infosoft.giftcardup.utils.Utils2;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.List;

public class Main extends GiftCardUp {
    NavigationView navigationView;
    View headerView;
    RelativeLayout content;
    CircularImageView user_profile_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new MenuSelect());
        headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_main, navigationView, true);
        user_profile_icon = (CircularImageView) headerView.findViewById(R.id.user_profile_icon);
        user_profile_icon.setOnClickListener(this);

        content = (RelativeLayout) findViewById(R.id.main_content);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    private void setFunctions() {
        // Dummy function
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        // Get the notifications MenuItem and LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.action_notifications);
        LayerDrawable icon = (LayerDrawable) item.getIcon();

        // Update LayerDrawable's BadgeDrawable
        Utils2.setBadgeCount(this, icon, 2);

        MenuItem item1 = menu.findItem(R.id.action_cart_item);
        LayerDrawable icon1 = (LayerDrawable) item.getIcon();

        // Update LayerDrawable's BadgeDrawable
        Utils1.setBadgeCount(this, icon1, 2);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_profile_icon:
                //startActivity(new Intent(this,Profile.class));
                replaceFragment(new Profile());
                setTitle("PROFILE");
                break;
            default:
                  break;
        }
    }

    @Override
    public void onProfile(Uri uri) {

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


    private class MenuSelect implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            switch (id) {

            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    }
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

}

abstract class GiftCardUp extends AppCompatActivity implements View.OnClickListener, Profile.OnFragmentProfile, BankInformation.OnFragmentBankInformation, Identification.OnFragmentIdentification, SsnEin.OnFragmentSsnEin {

}
