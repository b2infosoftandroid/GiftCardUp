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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.fragments.Dashboard;
import com.b2infosoft.giftcardup.fragments.Dashboard1;
import com.b2infosoft.giftcardup.fragments.Profile;
import com.b2infosoft.giftcardup.fragments.profile.BankInformation;
import com.b2infosoft.giftcardup.fragments.profile.Identification;
import com.b2infosoft.giftcardup.fragments.profile.SsnEin;
import com.b2infosoft.giftcardup.model.CompanyCategory;
import com.b2infosoft.giftcardup.model.QuickActionItem;
import com.b2infosoft.giftcardup.utils.Utils1;
import com.b2infosoft.giftcardup.utils.Utils2;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Main extends GiftCardUp {
    private final static String TAG = Main.class.getName();
    DMRRequest dmrRequest;
    Urls urls;
    Active active;
    Tags tags;
    NavigationView navigationViewRight, navigationViewLeft;
    View headerView;
    CircularImageView user_profile_icon;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dmrRequest = DMRRequest.getInstance(this, TAG);
        urls = Urls.getInstance();
        active = Active.getInstance(this);
        tags = Tags.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //View view1 = getLayoutInflater().inflate(R.layout.fragment_dashboard,null);
        //toolbar.addView(view1);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        replaceFragment(new Dashboard());
    }

    private void updateMenuItemLeft() {
        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.COMPANY_CATEGORY_ALL);
        dmrRequest.doPost(urls.getUrlAppActions(), map, new DMRResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                Log.d(TAG, jsonObject.toString());
                try {
                    if (jsonObject.has(tags.CATEGORIES)) {
                        List<CompanyCategory> categoryList = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray(tags.CATEGORIES);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CompanyCategory category = new CompanyCategory();
                            categoryList.add(category.fromJSON(jsonArray.getJSONObject(i)));
                        }
                        updateMenuItemLeft(categoryList);
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
        });

    }

    private void updateMenuItemLeft(List<CompanyCategory> categoryList) {
        Menu menu = navigationViewLeft.getMenu();
        for (final CompanyCategory category : categoryList) {
            MenuItem menuItem = menu.add(R.id.menu_2,Menu.NONE,Menu.NONE,category.getCategoryName().toUpperCase(Locale.getDefault()));
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent intent = new Intent(Main.this,Categories.class);
                    intent.putExtra(tags.CATEGORIES,category);
                    startActivity(intent);
                    return false;
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else if (drawer.isDrawerOpen(GravityCompat.END)) {
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
        switch (id) {
            case R.id.action_cart_item:
                 startActivity(new Intent(this,ShoppingCart.class));
                return true;
            case R.id.action_notifications:

                return true;
            case R.id.action_profile:
                if (drawer != null) {
                    if (drawer.isDrawerOpen(GravityCompat.END)) {
                        drawer.closeDrawer(GravityCompat.END);
                    } else {
                        drawer.openDrawer(GravityCompat.END);
                    }
                }
                return true;
            case R.id.action_settings:

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
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }
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
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else if (drawer.isDrawerOpen(GravityCompat.END)) {
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
