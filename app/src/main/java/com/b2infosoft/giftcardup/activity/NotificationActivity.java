package com.b2infosoft.giftcardup.activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.adapter.NotificationAdapter;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.model.Notification;
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

public class NotificationActivity extends AppCompatActivity implements Paginate.Callbacks {
    private static final String TAG = NotificationActivity.class.getName();
    private Urls urls;
    private Tags tags;
    private Active active;
    DMRRequest dmrRequest;
    RecyclerView recyclerView;
    NotificationAdapter adapter;
    List<Notification> notificationList;
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
        tags = Tags.getInstance();
        dmrRequest = DMRRequest.getInstance(this, TAG);
        urls = Urls.getInstance();
        notificationList = new ArrayList<>();
        active = Active.getInstance(this);
        handler = new Handler();
    }

    private void setupPagination() {
        if (paginate != null) {
            paginate.unbind();
        }
        handler.removeCallbacks(fakeCallback);
        int layoutOrientation = OrientationHelper.VERTICAL;
        RecyclerView.LayoutManager layoutManager = layoutManager = new LinearLayoutManager(this, layoutOrientation, false);
        recyclerView.setLayoutManager(layoutManager);
        setMyAdapter(new ArrayList<Notification>());
    }

    private void setMyAdapter(List<Notification> items) {
        adapter = new NotificationAdapter(this, items);
        recyclerView.setAdapter(adapter);
        paginate = Paginate.with(recyclerView, this)
                .setLoadingTriggerThreshold(threshold)
                .addLoadingListItem(addLoadingRow)
                .build();
    }

    private void addData(final List<Notification> items) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.add(items);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        data_available_view = findViewById(R.id.frame);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        setupPagination();
        loadNotifications();
        updateReadNotification();
    }

    private void updateReadNotification() {
        if (!active.isLogin()) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.SET_NOTIFICATIONS_READ);
        map.put(tags.USER_ID, active.getUser().getUserId());
        dmrRequest.doPost(urls.getAppAction(), map, new DMRResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                Log.d(TAG, jsonObject.toString());
                try {
                    if (jsonObject.has(tags.SUCCESS)) {
                        if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {

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
        });
    }

    private synchronized void loadNotifications() {
        if (!active.isLogin()) {
            return;
        }
        if (isLoading()) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.GET_NOTIFICATIONS);
        map.put(tags.USER_ID, active.getUser().getUserId());
        map.put(tags.LOAD_MORE, String.valueOf(loadMore));
        isLoading = true;
        data_available_view.setVisibility(View.GONE);
        dmrRequest.doPost(urls.getAppAction(), map, new DMRResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                isLoading = false;
                try {
                    if (jsonObject.has(tags.SUCCESS)) {
                        if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                            List<Notification> arrayList = new ArrayList<Notification>();
                            if (jsonObject.has(tags.GET_NOTIFICATIONS)) {
                                JSONArray jsonArray = jsonObject.getJSONArray(tags.GET_NOTIFICATIONS);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    arrayList.add(Notification.fromJSON(jsonArray.getJSONObject(i)));
                                }
                                addData(arrayList);
                            }
                            if (jsonObject.has(tags.IS_MORE)) {
                                isMore = jsonObject.getBoolean(tags.IS_MORE);
                                if (isMore) {
                                    loadMore += tags.DEFAULT_LOADING_DATA;
                                } else {
                                    loadMore += arrayList.size();
                                }
                            }
                        } else if (jsonObject.getInt(tags.SUCCESS) == tags.FAIL) {

                        }
                    }
                    if (adapter.getItemCount() == 0)
                        data_available_view.setVisibility(View.VISIBLE);
                    else
                        data_available_view.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onError(VolleyError volleyError) {
                isLoading = false;
                volleyError.printStackTrace();
                if (volleyError.getMessage() != null)
                    Log.e(TAG, volleyError.getMessage());
                if (adapter.getItemCount() == 0)
                    data_available_view.setVisibility(View.VISIBLE);
                else
                    data_available_view.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                this.onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
        return false;
    }

    private Runnable fakeCallback = new Runnable() {
        @Override
        public void run() {
            paginate.setHasMoreDataToLoad(isLoading());
            loadNotifications();
        }
    };
}
