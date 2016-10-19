package com.b2infosoft.giftcardup.activity;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.adapter.CheckOutAdapter;
import com.b2infosoft.giftcardup.app.GiftCardApp;
import com.b2infosoft.giftcardup.model.Cart;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.database.DBHelper;
import com.b2infosoft.giftcardup.model.CartSummary;
import com.b2infosoft.giftcardup.model.ContactInformation;
import com.b2infosoft.giftcardup.model.ControlPanel;
import com.b2infosoft.giftcardup.model.GiftCard;
import com.b2infosoft.giftcardup.model.OrderSummery;
import com.b2infosoft.giftcardup.model.User;
import com.b2infosoft.giftcardup.services.ConnectivityReceiver;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckOut extends AppCompatActivity implements DMRResult {
    public static final String TAG = CheckOut.class.getName();
    private GiftCardApp app;
    private Urls urls;
    private Tags tags;
    private Active active;
    DMRRequest dmrRequest;
    RecyclerView recyclerView;
    CheckOutAdapter adapter;
    List<GiftCard> cardList;
    Cart cart;
    DBHelper dbHelper;
    ControlPanel controlPanel;
    Button action_continue;

    private void init() {
        tags = Tags.getInstance();
        dmrRequest = DMRRequest.getInstance(this, TAG);
        urls = Urls.getInstance();
        active = Active.getInstance(this);
        cardList = new ArrayList<>();
        app = (GiftCardApp) getApplicationContext();
        cart = app.getCart();
        dbHelper = new DBHelper(this);
        controlPanel = dbHelper.getControlPanel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        action_continue = (Button) findViewById(R.id.action_continue);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //  refreshShoppingCartItemList();
        fetchContactInfo();
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

    @Override
    public void onSuccess(JSONObject jsonObject) {
        try {
            if (jsonObject.has(tags.SUCCESS)) {
                if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                    if (jsonObject.has(tags.USER_CONTACT_INFORMATION)) {
                        ContactInformation information = ContactInformation.fromJSON(jsonObject.getJSONObject(tags.USER_CONTACT_INFORMATION));
                        refreshShoppingCartItemList(information);
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
        if (volleyError.getMessage() != null)
            Log.e(TAG, volleyError.getMessage());
    }

    private void refreshShoppingCartItemList(ContactInformation information) {
        List<Object> cartList = new ArrayList<>();
        cartList.add(information);
        for (GiftCard giftCard : cart.getCartItemList()) {
            cartList.add(giftCard);
        }
        CartSummary summary = new CartSummary(cart.getCartItemList());
        OrderSummery orderSummery = new OrderSummery();
        orderSummery.setPrice(summary.getValue());
        orderSummery.setShipping(Float.parseFloat(controlPanel.getShippingCharge()));
        orderSummery.setDefaultShipping(cart.getCartItemList(), "First Class", Float.parseFloat(controlPanel.getFirstClassPrice()));
        cartList.add(orderSummery);
        adapter = new CheckOutAdapter(this, cartList, action_continue);
        recyclerView.setAdapter(adapter);
    }

    private boolean isConnected() {
        return ConnectivityReceiver.isConnected();
    }
}
