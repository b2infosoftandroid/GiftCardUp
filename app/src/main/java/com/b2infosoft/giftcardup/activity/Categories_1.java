package com.b2infosoft.giftcardup.activity;

import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.adapter.CardAdapter;
import com.b2infosoft.giftcardup.adapter.CardAdapter_1;
import com.b2infosoft.giftcardup.app.Cart;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.listener.OnLoadMoreListener;
import com.b2infosoft.giftcardup.model.CompanyBrand;
import com.b2infosoft.giftcardup.model.CompanyCategory;
import com.b2infosoft.giftcardup.model.EmptyBrand;
import com.b2infosoft.giftcardup.utils.Utils1;
import com.b2infosoft.giftcardup.utils.Utils2;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;
import com.paginate.Paginate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Categories_1 extends AppCompatActivity implements DMRResult,Paginate.Callbacks{
    private Cart cart;
    private static final String TAG = Categories_1.class.getName();
    private Urls urls;
    private Tags tags;
    private Active active;
    DMRRequest dmrRequest;
    RecyclerView recyclerView;
    CardAdapter_1 adapter;
    List<Object> cardList;
    private CompanyCategory category;
    private int unReadNotifications = 0;
    private View data_available_view;
    /*    PAGINATION START      */
    boolean isLoading = false;
    boolean isMore = false;
    int loadMore = 0;
    private int threshold = 4;
    private boolean addLoadingRow = true;
    private long networkDelay = 2000;
    private Handler handler;
    private Paginate paginate;
    /*    PAGINATION END        */

    private void init() {
        dmrRequest = DMRRequest.getInstance(this, TAG);
        urls = Urls.getInstance();
        tags = Tags.getInstance();
        active = Active.getInstance(this);
        cardList = new ArrayList<>();
        handler = new Handler();
        cart = (Cart) getApplicationContext();
    }
    private void setupPagination() {
        if (paginate != null) {
            paginate.unbind();
        }
        handler.removeCallbacks(fakeCallback);
        int layoutOrientation = OrientationHelper.VERTICAL;
        RecyclerView.LayoutManager layoutManager = layoutManager = new LinearLayoutManager(this, layoutOrientation, false);
        recyclerView.setLayoutManager(layoutManager);
        setMyAdapter(new ArrayList<Object>());
    }

    private void setMyAdapter(List<Object> items) {
        adapter = new CardAdapter_1(this, items);
        recyclerView.setAdapter(adapter);
        paginate = Paginate.with(recyclerView, this)
                .setLoadingTriggerThreshold(threshold)
                .addLoadingListItem(addLoadingRow)
                .build();
    }

    private void addData(final List<Object> items) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.add(items);
            }
        });
    }

    @Override
    public synchronized void onLoadMore() {
        // Fake asynchronous loading that will generate page of random data after some delay
        handler.postDelayed(fakeCallback, networkDelay);
    }

    @Override
    public synchronized boolean isLoading() {
        return isLoading; // Return boolean weather data is already loading or not
    }

    @Override
    public boolean hasLoadedAllItems() {
        //return page == totalPages; // If all pages are loaded return true
        return !isMore;
    }

    private Runnable fakeCallback = new Runnable() {
        @Override
        public void run() {
            paginate.setHasMoreDataToLoad(isLoading());
            loadCards();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        if (getIntent().hasExtra(tags.CATEGORIES)) {
            category = (CompanyCategory) getIntent().getExtras().getSerializable(tags.CATEGORIES);
            setTitle(category.getCategoryName() + "");
        }
        data_available_view = findViewById(R.id.frame);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        setupPagination();
        checkNotificationUnRead();
        loadCards();
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
    public void onBackPressed() {
        super.onBackPressed();
    }
/*
    private void setDataInRecycleView(final List<CompanyBrand> cards) {

        if (isLoading) {
            cardList.remove(cardList.size() - 1);
            adapter.notifyItemRemoved(cardList.size());
            isLoading = false;
        }
        if (cards.size() > 0)
            cardList.addAll(cards);
        if (cardList.size() == 0) {
            cardList.add(new EmptyBrand());
        }
        adapter.notifyDataSetChanged();
        adapter.setLoaded();
        //setPaginate();
    }
  */

    private synchronized   void loadCards() {
        if (isLoading()) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.COMPANY_ALL_BRAND);
        map.put(tags.LOAD_MORE, String.valueOf(loadMore));
        map.put(tags.CATEGORIES_ID, String.valueOf(category.getCategoryID()));
        isLoading = true;
        data_available_view.setVisibility(View.GONE);
        dmrRequest.doPost(urls.getGiftCardInfo(), map, new DMRResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                isLoading = false;
                try {
                    if (jsonObject.has(tags.SUCCESS)) {
                        if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                            List<Object> cards = new ArrayList<>();
                            if (jsonObject.has(tags.GIFT_CARDS)) {
                                JSONArray jsonArray = jsonObject.getJSONArray(tags.GIFT_CARDS);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    CompanyBrand brand = new CompanyBrand();
                                    cards.add(brand.fromJSON(jsonArray.getJSONObject(i)));
                                }
                            }
                            if (jsonObject.has(tags.IS_MORE)) {
                                isMore = jsonObject.getBoolean(tags.IS_MORE);
                                if (isMore) {
                                    loadMore += tags.DEFAULT_LOADING_DATA;
                                } else {
                                    loadMore += cards.size();
                                }
                            }
                            addData(cards);
                            //setDataInRecycleView(cards);
                        } else if (jsonObject.getInt(tags.SUCCESS) == tags.FAIL) {

                        } else {

                        }
                    }
                    if (adapter.getItemCount() == 0)
                        data_available_view.setVisibility(View.VISIBLE);
                    else
                        data_available_view.setVisibility(View.GONE);
                    /*
                    if (jsonObject.has(tags.IS_MORE)) {
                        isMore = jsonObject.getBoolean(tags.IS_MORE);
                        if (isMore) {
                            loadMore += tags.DEFAULT_LOADING_DATA;
                        }
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG,e.getMessage());
                }
            }

            @Override
            public void onError(VolleyError volleyError) {
                isLoading = true;
                volleyError.printStackTrace();
                if (volleyError.getMessage() != null)
                    Log.e(TAG,volleyError.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action_complete bar if it is present.
        getMenuInflater().inflate(R.menu.company, menu);
        // Get the notifications MenuItem and LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.action_notifications);
        LayerDrawable icon = (LayerDrawable) item.getIcon();

        // Update LayerDrawable's BadgeDrawable
        Utils2.setBadgeCount(this, icon, unReadNotifications);

        MenuItem item1 = menu.findItem(R.id.action_cart_item);
        LayerDrawable icon1 = (LayerDrawable) item1.getIcon();

        // Update LayerDrawable's BadgeDrawable
        Utils1.setBadgeCount(this, icon1, cart.getCartItemCount());
        return true;
    }
    private void checkNotificationUnRead() {
        if (active.isLogin()) {
            Map<String, String> map = new HashMap<>();
            map.put(tags.USER_ACTION, tags.GET_NOTIFICATIONS_UN_READ);
            map.put(tags.USER_ID, active.getUser().getUserId());
            dmrRequest.doPost(urls.getAppAction(), map, this);
        }
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        try {
            if (jsonObject.has(tags.SUCCESS)) {
                if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                    if (jsonObject.has(tags.GET_NOTIFICATIONS_UN_READ)) {
                        unReadNotifications = jsonObject.getInt(tags.GET_NOTIFICATIONS_UN_READ);
                        invalidateOptionsMenu();
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
            Log.e(TAG, volleyError.getMessage());
    }

}
