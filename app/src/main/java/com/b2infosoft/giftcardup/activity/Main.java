package com.b2infosoft.giftcardup.activity;

import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.fragments.Dashboard;
import com.b2infosoft.giftcardup.fragments.Dashboard1;
import com.b2infosoft.giftcardup.fragments.Profile;
import com.b2infosoft.giftcardup.fragments.profile.BankInformation;
import com.b2infosoft.giftcardup.fragments.profile.Identification;
import com.b2infosoft.giftcardup.fragments.profile.SsnEin;
import com.b2infosoft.giftcardup.model.QuickActionItem;
import com.b2infosoft.giftcardup.utils.Utils1;
import com.b2infosoft.giftcardup.utils.Utils2;
import com.mikhaellopez.circularimageview.CircularImageView;

public class Main extends GiftCardUp {
    Active active;
    NavigationView navigationViewRight, navigationViewLeft;
    View headerView;
    CircularImageView user_profile_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        active = Active.getInstance(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //View view1 = getLayoutInflater().inflate(R.layout.fragment_dashboard,null);
        //toolbar.addView(view1);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationViewRight = (NavigationView) findViewById(R.id.nav_view_right);
        navigationViewRight.setNavigationItemSelectedListener(new MenuSelect());
        navigationViewLeft = (NavigationView) findViewById(R.id.nav_view_left);
        navigationViewLeft.setNavigationItemSelectedListener(new MenuSelect());

        headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_main, navigationViewRight, false);
        user_profile_icon = (CircularImageView) headerView.findViewById(R.id.user_profile_icon);
        user_profile_icon.setOnClickListener(this);
        setNavigationMenu();
        updateMenuItemLeft();
    }
    private void updateMenuItemLeft() {
        Menu menu = navigationViewLeft.getMenu();
        String s[] = {"Apparel","Arts and Crafts","Baby and Kids","Books and Magazines","Coffee Shops","Computer and Software","Department Stores",
                      "Discount Stores","Electronics","Entertainment","Restaurants","Gas and Automotive","Grocery Stores","Health and Wellness",
                      "Home and Garden","Jewelry and Watches","Office Supplies","Pets","Shoes","Sporting and Outdoors","Toys","Travel",
                      "Tata"};
        for(final String s1 : s){
            MenuItem menuItem = menu.add(s1);
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Toast.makeText(Main.this,s1,Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if(drawer.isDrawerOpen(GravityCompat.START)){
                drawer.closeDrawer(GravityCompat.START);
            }else if(drawer.isDrawerOpen(GravityCompat.END)){
                drawer.closeDrawer(GravityCompat.END);
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
                replaceFragment(new Dashboard1());
                break;
            default:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.END)){
            drawer.closeDrawer(GravityCompat.END);
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

    @Override
    public void onDashboard1(Uri uri) {

    }

    @Override
    public void onDashboard(Uri uri) {

    }

    private class MenuSelect implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            switch (id) {
                case R.id.menu_item_dashboard_left:
                     replaceFragment(new Dashboard());
                    break;
                case R.id.menu_item_my_listing:

                    break;
                case R.id.menu_item_bulk_listing:

                    break;
                case R.id.menu_item_speedy_sell:

                    break;
                case R.id.menu_item_shipping_center:

                    break;
                case R.id.menu_item_available_fund:

                    break;
                case R.id.menu_item_withdrawal_history:

                    break;
                case R.id.menu_item_recommend_brand:

                    break;
                case R.id.menu_item_referral_rewards:

                    break;
                case R.id.menu_item_my_account:

                    replaceFragment(new Profile());
                    setTitle("PROFILE");

                    //startActivity(new Intent(Main.this,MyAccount.class));

                    break;
                case R.id.menu_item_my_orders:

                    break;
                case R.id.menu_item_my_cart:

                    break;
                case R.id.menu_item_logout:
                    active.setLogOut();
                    setNavigationMenu();
                    break;
                case R.id.menu_item_login:
                    startActivity(new Intent(Main.this, Login.class));
                    break;
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if(drawer.isDrawerOpen(GravityCompat.START)){
                drawer.closeDrawer(GravityCompat.START);
            }else if(drawer.isDrawerOpen(GravityCompat.END)){
                drawer.closeDrawer(GravityCompat.END);
            }
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

    private void setNavigationMenu() {
        navigationViewRight.removeHeaderView(headerView);
        navigationViewRight.addHeaderView(headerView);
        setSellerMenu(false);
        setLoginMenu(active.isLogin());
    }

    private void setSellerMenu(boolean bol) {
        navigationViewRight.getMenu().setGroupVisible(R.id.menu_1, bol);
        navigationViewRight.getMenu().setGroupVisible(R.id.menu_2, bol);
        navigationViewRight.getMenu().setGroupVisible(R.id.menu_3, bol);
    }

    private void setLoginMenu(boolean bol) {
        navigationViewRight.getMenu().setGroupVisible(R.id.menu_5, bol);
        navigationViewRight.getMenu().setGroupVisible(R.id.menu_6, !bol);
    }

}

abstract class GiftCardUp extends AppCompatActivity implements View.OnClickListener, Profile.OnFragmentProfile, BankInformation.OnFragmentBankInformation, Identification.OnFragmentIdentification, SsnEin.OnFragmentSsnEin, Dashboard1.OnFragmentDashboard1, Dashboard.OnFragmentDashboard {

}
