package com.b2infosoft.giftcardup.activity;

import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Cart;
import com.b2infosoft.giftcardup.app.Config;
import com.b2infosoft.giftcardup.app.GiftCardUp;
import com.b2infosoft.giftcardup.app.Notify;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.database.DBHelper;
import com.b2infosoft.giftcardup.fragments.AvailableFund;
import com.b2infosoft.giftcardup.fragments.BulkListing;
import com.b2infosoft.giftcardup.fragments.Dashboard;
import com.b2infosoft.giftcardup.fragments.MyListing;
import com.b2infosoft.giftcardup.fragments.RecommendBrands;
import com.b2infosoft.giftcardup.fragments.ReferralRewards;
import com.b2infosoft.giftcardup.fragments.SellCards;
import com.b2infosoft.giftcardup.fragments.ShippingCenter;
import com.b2infosoft.giftcardup.fragments.SpeedySell;
import com.b2infosoft.giftcardup.fragments.WithdrawalHistory;
import com.b2infosoft.giftcardup.model.CompanyCategory;
import com.b2infosoft.giftcardup.model.GiftCard;
import com.b2infosoft.giftcardup.model.User;
import com.b2infosoft.giftcardup.services.MyServices;
import com.b2infosoft.giftcardup.utils.Utils1;
import com.b2infosoft.giftcardup.utils.Utils2;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;
import com.b2infosoft.giftcardup.volly.LruBitmapCache;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
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
    private Cart cart;
    DMRRequest dmrRequest;
    Config config;
    Urls urls;
    Active active;
    Tags tags;
    DBHelper dbHelper;
    Notify notify;
    NavigationView navigationViewRight, navigationViewLeft;
    View headerView, isLoginLayout, isLogoutLayout;
    CircularImageView user_profile_icon;
    TextView user_profile_name, user_total_sold, user_total_saving;
    DrawerLayout drawer;

    /* FACEBOOK INTEGRATION */
    CallbackManager callbackManager;

    private void init() {
        dmrRequest = DMRRequest.getInstance(this, TAG);
        urls = Urls.getInstance();
        active = Active.getInstance(this);
        tags = Tags.getInstance();
        notify = Notify.getInstance();
        config = Config.getInstance();
        dbHelper = new DBHelper(this);
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);
        cart = (Cart) getApplicationContext();
        init();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
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

        isLoginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main.this, MyProfile.class));
            }
        });
        setNavigationMenu();
        updateMenuItemLeft(dbHelper.getCategories());
        replaceFragment(new Dashboard());
        if (active.getUser() != null)
            loadAvailableCartItems();
        checkFBLogin();
    }

    private void checkFBLogin() {
        if (!active.isLogin()) {
            showMessage("Log Out");
            if (isLoggedIn()) {
                showMessage("FB LOGIN");
                GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (response.getError() == null) {
                            if (object != null) {
                                try {
                                    showMessage("Data FOUND");
                                    Log.d("OBJECT",object.toString());
                                    if (object.has("id")) {
                                        Map<String, String> map = new HashMap<>();
                                        map.put(tags.USER_ACTION, tags.FB_LOGIN_USER);
                                        map.put(tags.FB_ID, object.getString("id"));
                                        map.put(tags.FIRST_NAME, object.getString("first_name"));
                                        map.put(tags.LAST_NAME, object.getString("last_name"));
                                        integrateWithFB(map);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                showMessage("Data Not Found");
                            }
                        } else {
                            Log.d("ERROR_RES", "NOT NULL");
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,first_name,last_name");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }
        }
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

    private void checkBackgroundServices() {
        MyServices.startLeftCartTimeService(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkBackgroundServices();
        setNavigationMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
        checkBackgroundServices();
    }
    private void integrateWithFB(Map<String, String> map) {
        dmrRequest.doPost(urls.getUserInfo(), map, new DMRResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                showMessage("FB CHECK");
                try {
                    if (jsonObject.has(tags.SUCCESS)) {
                        if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                            if (jsonObject.has(tags.USER_TYPE)) {
                                if (jsonObject.getInt(tags.USER_TYPE) == tags.EXISTING_USER) {
                                    if (jsonObject.has(tags.USER_INFO)) {
                                        JSONObject object = jsonObject.getJSONObject(tags.USER_INFO);
                                        User user = User.fromJSON(object);
                                        active.setUser(user);
                                        active.setLogin();
                                        setNavigationMenu();
                                    }
                                } else if (jsonObject.getInt(tags.USER_TYPE) == tags.NEW_USER || jsonObject.getInt(tags.USER_TYPE) == tags.PASSWORD_NOT_UPDATE) {
                                    if (jsonObject.has(tags.USER_ID)) {
                                        Intent intent = new Intent(Main.this, FBUserUpdate.class);
                                        intent.putExtra(tags.USER_ID, jsonObject.getString(tags.USER_ID));
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
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
        });
    }

    private void loadAvailableCartItems() {
        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.CHECK_CART_ITEMS);
        map.put(tags.USER_ID, active.getUser().getUserId() + "");
        dmrRequest.doPost(urls.getCartInfo(), map, new DMRResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.has(tags.SUCCESS)) {
                        if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                            if (jsonObject.has(tags.GIFT_CARDS)) {
                                JSONArray array = jsonObject.getJSONArray(tags.GIFT_CARDS);
                                cart.removeAll();
                                for (int i = 0; i < array.length(); i++) {
                                    cart.addCartItem(GiftCard.fromJSON(array.getJSONObject(i)));
                                }
                            }
                        } else if (jsonObject.getInt(tags.SUCCESS) == tags.FAIL) {
                            cart.removeAll();
                        }
                    }
                    invalidateOptionsMenu();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        if (active.getUser() != null)
            loadAvailableCartItems();
        invalidateOptionsMenu();
        checkBackgroundServices();
        setNavigationMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (active.getUser() != null)
            loadAvailableCartItems();
        invalidateOptionsMenu();
        checkBackgroundServices();
        setNavigationMenu();
        checkFBLogin();
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
        Utils1.setBadgeCount(this, icon1, cart.getCartItemCount());

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
                startActivity(new Intent(Main.this, MyProfile.class));
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
                    replaceFragment(new AvailableFund());
                    setTitle("Available Fund");
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
            user_total_saving.setText("$" + user.getTotalSave());
            user_total_sold.setText("$" + user.getTotalSold());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
