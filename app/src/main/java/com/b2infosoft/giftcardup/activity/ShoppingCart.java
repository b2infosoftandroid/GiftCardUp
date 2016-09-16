package com.b2infosoft.giftcardup.activity;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.adapter.CartAdapter;
import com.b2infosoft.giftcardup.app.Cart;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.model.GiftCard;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart extends AppCompatActivity {
    private static final String TAG = ShoppingCart.class.getName();
    private Urls urls;
    private Tags tags;
    private Active active;
    DMRRequest dmrRequest;
    RecyclerView recyclerView;
    CartAdapter adapter;
    List<GiftCard> cardList;
    Cart cart;
    private void init() {
        tags = Tags.getInstance();
        dmrRequest = DMRRequest.getInstance(this, TAG);
        urls = Urls.getInstance();
        active = Active.getInstance(this);
        cardList = new ArrayList<>();
        cart = (Cart)getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshShoppingCartItemList();
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
    private void refreshShoppingCartItemList(){
        List<GiftCard> cartList = cart.getCartItemList();
        cartList.add(null);
        adapter = new CartAdapter(this, cartList);
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
                    refreshShoppingCartItemList();
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
}
