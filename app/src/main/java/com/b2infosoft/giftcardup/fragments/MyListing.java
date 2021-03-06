package com.b2infosoft.giftcardup.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.adapter.MyListingAdapter;
import com.b2infosoft.giftcardup.app.Alert;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.custom.Progress;
import com.b2infosoft.giftcardup.model.CompanyBrand;
import com.b2infosoft.giftcardup.model.GiftCard;
import com.b2infosoft.giftcardup.services.ConnectivityReceiver;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class MyListing extends Fragment implements View.OnClickListener, Paginate.Callbacks {
    private final static String TAG = MyListing.class.getName();
    private Alert alert;
    private Urls urls;
    private Tags tags;
    private Active active;
    DMRRequest dmrRequest;
    private Progress progress;
    View mView;
    RecyclerView recyclerView;
    View data_available_view;
    MyListingAdapter adapter;
    List<Object> cardList;
    boolean isFilter = false;
    EditText search;
    Spinner type;
    ImageButton action_search;
    CompanyBrand brand;

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


    public MyListing() {

    }

    private void init() {
        dmrRequest = DMRRequest.getInstance(getActivity(), TAG);
        urls = Urls.getInstance();
        tags = Tags.getInstance();
        active = Active.getInstance(getActivity());
        progress = new Progress(getActivity());
        cardList = new ArrayList<>();
        handler = new Handler();
        alert =Alert.getInstance(getActivity());
    }

    private void setupPagination() {
        if (paginate != null) {
            paginate.unbind();
        }
        handler.removeCallbacks(fakeCallback);
        int layoutOrientation = OrientationHelper.VERTICAL;
        RecyclerView.LayoutManager layoutManager = layoutManager = new LinearLayoutManager(getActivity(), layoutOrientation, false);
        recyclerView.setLayoutManager(layoutManager);
        setMyAdapter(new ArrayList<Object>());
    }

    private void setMyAdapter(List<Object> items) {
        adapter = new MyListingAdapter(getActivity(), items);
        recyclerView.setAdapter(adapter);
        paginate = Paginate.with(recyclerView, this)
                .setLoadingTriggerThreshold(threshold)
                .addLoadingListItem(addLoadingRow)
                .build();
    }

    private void addData(final List<Object> items) {
        getActivity().runOnUiThread(new Runnable() {
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
        return false;
    }

    private Runnable fakeCallback = new Runnable() {
        @Override
        public void run() {
            paginate.setHasMoreDataToLoad(isLoading());
            loadCards();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        init();
        mView = inflater.inflate(R.layout.fragment_my_listing, container, false);
        data_available_view = mView.findViewById(R.id.frame);
        recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        setupPagination();
        loadCards();
        search = (EditText) mView.findViewById(R.id.search);
        type = (Spinner) mView.findViewById(R.id.type);
        action_search = (ImageButton) mView.findViewById(R.id.action_search);
        action_search.setOnClickListener(this);
        return mView;
    }

    @Override
    public void onClick(View v) {
        if (R.id.action_search == v.getId()) {
            filter_search();
        }
    }

    private void filter_search() {
        isFilter = false;
        String search_keyword = search.getText().toString();
        int position = type.getSelectedItemPosition();
        search.setError(null);
        if (TextUtils.isEmpty(search_keyword)) {
            search.setError("Enter Keyword");
            search.requestFocus();
            return;
        }
        if (position == 0) {
            return;
        }
        isFilter = true;
        if (adapter.getItemCount() > 0) {
            adapter.removeAllItem();
        }
        loadMore = 0;
        isLoading = false;
        loadCards();
    }

    private synchronized void loadCards() {
        if (!active.isLogin()) {
            return;
        }
        if(!isConnected()){
            alert.showSnackIsConnected(isConnected());
            return;
        }
        if (isLoading()) {
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.GET_GIFT_CARD_BY_USER_ID);
        map.put(tags.USER_ID, active.getUser().getUserId() + "");
        map.put(tags.LOAD_MORE, String.valueOf(loadMore));
        if (isFilter) {
            map.put(tags.KEYWORD_TYPE, type.getSelectedItem().toString());
            map.put(tags.KEYWORD_SEARCH, search.getText().toString());
        }
        isLoading = true;
        data_available_view.setVisibility(View.GONE);
        dmrRequest.doPost(urls.getGiftCardInfo(), map, new DMRResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                isLoading = false;
                try {
                    if (jsonObject.has(tags.SUCCESS)) {
                        if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                            data_available_view.setVisibility(View.GONE);
                            List<Object> cards = new ArrayList<>();
                            if (jsonObject.has(tags.GIFT_CARDS)) {
                                JSONArray jsonArray = jsonObject.getJSONArray(tags.GIFT_CARDS);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    GiftCard card = new GiftCard();
                                    cards.add(card.fromJSON(jsonArray.getJSONObject(i)));
                                }
                                //setDataInRecycleView(cards);
                                addData(cards);
                            }
                            if (jsonObject.has(tags.IS_MORE)) {
                                data_available_view.setVisibility(View.GONE);
                                isMore = jsonObject.getBoolean(tags.IS_MORE);
                                if (isMore) {
                                    loadMore += tags.DEFAULT_LOADING_DATA;
                                } else {
                                    loadMore += cards.size();
                                }
                            }
                        } else if (jsonObject.getInt(tags.SUCCESS) == tags.FAIL) {
                            if (adapter.getItemCount() == 0)
                                data_available_view.setVisibility(View.VISIBLE);
                            else
                                data_available_view.setVisibility(View.GONE);
                        }
                    }/*
                    if (jsonObject.has(tags.IS_MORE)) {
                        data_available_view.setVisibility(View.GONE);
                        isMore = jsonObject.getBoolean(tags.IS_MORE);
                        if (isMore) {
                            loadMore += tags.DEFAULT_LOADING_DATA;
                        }
                    }*/
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
            }
        });
    }
    private boolean isConnected() {
        return ConnectivityReceiver.isConnected();
    }
    /*
    private void setDataInRecycleView(final List<GiftCard> cards) {
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
    }
    */
}
