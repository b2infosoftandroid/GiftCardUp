package com.b2infosoft.giftcardup.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.adapter.CartAdapter;
import com.b2infosoft.giftcardup.app.Alert;
import com.b2infosoft.giftcardup.app.GiftCardApp;
import com.b2infosoft.giftcardup.model.Cart;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.model.CartSummary;
import com.b2infosoft.giftcardup.model.EmptyCart;
import com.b2infosoft.giftcardup.model.GiftCard;
import com.b2infosoft.giftcardup.services.ConnectivityReceiver;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    public static final String TAG = ShoppingCart.class.getName();
    private Urls urls;
    private Tags tags;
    private Active active;
    private Alert alert;
    View main_view;
    DMRRequest dmrRequest;
    RecyclerView recyclerView;
    CartAdapter adapter;
    List<GiftCard> cardList;
    Cart cart;
    private GiftCardApp app;
    /* UI COMPONENTS */
    Button action_checkout;
    MenuItem left_time;
    /* BROADCAST RECEIVER */
    IntentFilter intentFilter = new IntentFilter(TAG);
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(tags.LEFT_TIME)) {
                if (left_time != null)
                    left_time.setTitle(intent.getStringExtra(tags.LEFT_TIME));
            } else {
                if (left_time != null)
                    left_time.setTitle("");
            }
        }
    };

    private void init() {
        tags = Tags.getInstance();
        dmrRequest = DMRRequest.getInstance(this, TAG);
        urls = Urls.getInstance();
        active = Active.getInstance(this);
        cardList = new ArrayList<>();
        app = (GiftCardApp)getApplicationContext();
        cart = app.getCart();
        alert = Alert.getInstance(this);
        main_view = findViewById(R.id.main_view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        action_checkout = (Button) findViewById(R.id.action_checkout);
        action_checkout.setOnClickListener(this);
        refreshShoppingCartItemList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_checkout:
                startActivity(new Intent(this, CheckOut.class));
                break;
            default:

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action_complete bar if it is present.
        getMenuInflater().inflate(R.menu.shopping_cart, menu);
        // Get the notifications MenuItem and LayerDrawable (layer-list)
        left_time = menu.findItem(R.id.left_time);
        return super.onCreateOptionsMenu(menu);
    }

    private void refreshShoppingCartItemList() {
        List<Object> cartList = new ArrayList<>();
        for (GiftCard giftCard : cart.getCartItemList()) {
            cartList.add(giftCard);
        }
        if (cartList.size() > 0) {
            CartSummary summary = new CartSummary(cart.getCartItemList());
            cartList.add(summary);
        }
        if (cartList.size() == 0) {
            cartList.add(new EmptyCart());
            action_checkout.setVisibility(View.GONE);
        }
        adapter = new CartAdapter(this, cartList, action_checkout);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadAvailableCartItems();
        invalidateOptionsMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAvailableCartItems();
        invalidateOptionsMenu();
        this.registerReceiver(broadcastReceiver, intentFilter);
        GiftCardApp.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        alert.showSnackIsConnectedView(main_view, isConnected);
    }
    @Override
    protected void onStart() {
        super.onStart();
        invalidateOptionsMenu();
        this.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(this.broadcastReceiver);
    }

    private void loadAvailableCartItems() {
        if (!active.isLogin())
            return;
        if(!isConnected()){
            alert.showSnackIsConnectedView(main_view,isConnected());
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.CHECK_CART_ITEMS);
        map.put(tags.USER_ID, active.getUser().getUserId());
        dmrRequest.doPost(urls.getCartInfo(), map, new DMRResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                // Log.d("cartData",jsonObject.toString());
                try {
                    if (jsonObject.has(tags.SUCCESS)) {
                        if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                            if (jsonObject.has(tags.GIFT_CARDS)) {
                                JSONArray array = jsonObject.getJSONArray(tags.GIFT_CARDS);
                                cart.removeAll();
                                for (int i = 0; i < array.length(); i++) {
                                    cart.addCartItem(GiftCard.fromJSON(array.getJSONObject(i)));
                                }
                                app.setCart(cart);
                            }
                        } else if (jsonObject.getInt(tags.SUCCESS) == tags.FAIL) {
                            cart.removeAll();
                            app.setCart(cart);
                        }
                    }
                    invalidateOptionsMenu();
                    refreshShoppingCartItemList();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onError(VolleyError volleyError) {
                volleyError.printStackTrace();
                if (volleyError.getMessage() != null)
                    Log.e(TAG, volleyError.getMessage());
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private boolean isConnected() {
        return ConnectivityReceiver.isConnected();
    }
}
