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
import android.widget.TextView;
import android.widget.Toast;

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Config;
import com.b2infosoft.giftcardup.app.Notify;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.database.DBHelper;
import com.b2infosoft.giftcardup.fragments.BulkListing;
import com.b2infosoft.giftcardup.fragments.Dashboard;
import com.b2infosoft.giftcardup.fragments.MyListing;
import com.b2infosoft.giftcardup.fragments.RecommendBrands;
import com.b2infosoft.giftcardup.fragments.ReferralRewards;
import com.b2infosoft.giftcardup.fragments.SellCards;
import com.b2infosoft.giftcardup.fragments.ShippingCenter;
import com.b2infosoft.giftcardup.fragments.SpeedySell;
import com.b2infosoft.giftcardup.fragments.TinderWork;
import com.b2infosoft.giftcardup.fragments.Profile;
import com.b2infosoft.giftcardup.fragments.WithdrawalHistory;
import com.b2infosoft.giftcardup.fragments.profile.BankInformation;
import com.b2infosoft.giftcardup.fragments.profile.Identification;
import com.b2infosoft.giftcardup.fragments.profile.SsnEin;
import com.b2infosoft.giftcardup.model.CompanyCategory;
import com.b2infosoft.giftcardup.model.User;
import com.b2infosoft.giftcardup.utils.Utils1;
import com.b2infosoft.giftcardup.utils.Utils2;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.LruBitmapCache;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;
import java.util.Locale;

public class Main extends GiftCardUp {
    private final static String TAG = Main.class.getName();
    DMRRequest dmrRequest;
    Config config;
    Urls urls;
    Active active;
    Tags tags;
    DBHelper dbHelper;
    Notify notify;
    int count;
    NavigationView navigationViewRight, navigationViewLeft;
    View headerView, isLoginLayout, isLogoutLayout;
    CircularImageView user_profile_icon;
    TextView user_profile_name, user_total_sold, user_total_saving;
    DrawerLayout drawer;

    private void init() {
        dmrRequest = DMRRequest.getInstance(this, TAG);
        urls = Urls.getInstance();
        active = Active.getInstance(this);
        tags = Tags.getInstance();
        notify = Notify.getInstance();
        config = Config.getInstance();
        dbHelper = new DBHelper(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        count = intent.getIntExtra("COUNT",0);
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
        isLoginLayout = headerView.findViewById(R.id.layout_user_is_login);
        isLogoutLayout = headerView.findViewById(R.id.layout_user_is_log_out);
        user_profile_icon = (CircularImageView) headerView.findViewById(R.id.user_profile_icon);
        user_profile_name = (TextView) headerView.findViewById(R.id.user_profile_name);
        user_total_sold = (TextView) headerView.findViewById(R.id.user_total_sold);
        user_total_saving = (TextView) headerView.findViewById(R.id.user_total_saving);
        user_profile_icon.setOnClickListener(this);
        setNavigationMenu();
        updateMenuItemLeft(dbHelper.getCategories());
        replaceFragment(new Dashboard());
    }

    private void updateMenuItemLeft(List<CompanyCategory> categoryList) {
        Menu menu = navigationViewLeft.getMenu();
        for (final CompanyCategory category : categoryList) {
            MenuItem menuItem = menu.add(R.id.menu_2, Menu.NONE, Menu.NONE, category.getCategoryName().toUpperCase(Locale.getDefault()));
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent intent = new Intent(Main.this, Categories.class);
                    intent.putExtra(tags.CATEGORIES, category);
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
        LayerDrawable icon1 = (LayerDrawable) item1.getIcon();

        // Update LayerDrawable's BadgeDrawable
        Utils1.setBadgeCount(this, icon1, count);
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
                startActivity(new Intent(this, ShoppingCart.class));
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
                replaceFragment(new TinderWork());
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

    @Override
    public void onSellCards(Uri uri) {

    }

    @Override
    public void onSpeedyCell(Uri uri) {

    }



    private class MenuSelect implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            switch (id) {
                case R.id.menu_item_dashboard_left:
                    replaceFragment(new Dashboard());
                    setTitle("Dashboard");
                    break;
                case R.id.menu_item_sell_gift_cards:
                    replaceFragment(new SellCards());
                    setTitle("SELL GIFTCARD");
                    break;
                case R.id.menu_item_my_listing:
                    replaceFragment(new MyListing());
                    setTitle("My Listing");
                    break;
                case R.id.menu_item_bulk_listing:
                    replaceFragment(new BulkListing());
                    setTitle("Bulk Listing");
                    break;
                case R.id.menu_item_speedy_sell:
                    replaceFragment(new SpeedySell());
                    setTitle("Speedy Sell");
                    break;
                case R.id.menu_item_shipping_center:
                    replaceFragment(new ShippingCenter());
                    setTitle("Shipping Center");
                    break;
                case R.id.menu_item_available_fund:

                    break;
                case R.id.menu_item_withdrawal_history:
                     replaceFragment(new WithdrawalHistory());
                    setTitle("Withdrawal History");
                    break;
                case R.id.menu_item_recommend_brand:
                    replaceFragment(new RecommendBrands());
                    setTitle("Recommend Brands");
                    break;
                case R.id.menu_item_referral_rewards:
                    replaceFragment(new ReferralRewards());
                    setTitle("Referral Rewards");
                    break;
                case R.id.menu_item_my_account:
                    /*
                        replaceFragment(new Profile());
                        setTitle("PROFILE");
                    */
                    startActivity(new Intent(Main.this, MyProfile.class));
                    //startActivity(new Intent(Main.this,MyAccount.class));

                    break;
                case R.id.menu_item_my_orders:

                    break;
                case R.id.menu_item_my_cart:
                       startActivity(new Intent(Main.this, ShoppingCart.class));
                    break;
                case R.id.menu_item_logout:
                    active.setLogOut();
                    setNavigationMenu();
                    replaceFragment(new Dashboard());
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
        if (fragment instanceof Dashboard) {

        } else {
            transaction.addToBackStack(null);
        }
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void setNavigationMenu() {
        boolean userIsLogin = active.isLogin();
        navigationViewRight.removeHeaderView(headerView);
        if (userIsLogin) {
            User user = active.getUser();
            user_profile_name.setText(user.getFirstName() + " " + user.getLastName());
            LruBitmapCache.loadCacheImage(this, user_profile_icon, config.getUserProfileImageAddress().concat(user.getImage()), TAG);
            setMenuItems(user.getUserType());
        } else {
            setMenuItems(0);
            user_profile_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_icon));
        }
        navigationViewRight.addHeaderView(headerView);
    }

    private void setMenuItems(int userType) {
        navigationViewRight.getMenu().setGroupVisible(R.id.menu_1, userType == 2 ? true : false);
        navigationViewRight.getMenu().setGroupVisible(R.id.menu_2, userType == 2 ? true : false);
        navigationViewRight.getMenu().setGroupVisible(R.id.menu_3, userType == 2 ? true : false);
        navigationViewRight.getMenu().setGroupVisible(R.id.menu_4, userType == 0 ? false : true);
        navigationViewRight.getMenu().setGroupVisible(R.id.menu_5, userType != 0 ? true : false);
        navigationViewRight.getMenu().setGroupVisible(R.id.menu_6, userType == 0 ? true : false);
        isLoginLayout.setVisibility(userType != 0 ? View.VISIBLE : View.GONE);
        isLogoutLayout.setVisibility(userType == 0 ? View.VISIBLE : View.GONE);
    }
}

abstract class GiftCardUp extends AppCompatActivity implements View.OnClickListener, BankInformation.OnFragmentBankInformation, Identification.OnFragmentIdentification, SsnEin.OnFragmentSsnEin, TinderWork.OnFragmentDashboard1, Dashboard.OnFragmentDashboard, SellCards.OnFragmentSellCards, SpeedySell.OnFragmentSpeedyCell {

}
