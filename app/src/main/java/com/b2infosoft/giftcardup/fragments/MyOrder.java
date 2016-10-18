package com.b2infosoft.giftcardup.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.adapter.MyOrderAdapter;
import com.b2infosoft.giftcardup.adapter.ShipmentCardRecyclerViewAdapter;
import com.b2infosoft.giftcardup.app.Alert;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.custom.Progress;
import com.b2infosoft.giftcardup.model.EmptyBrand;
import com.b2infosoft.giftcardup.model.GiftCard;
import com.b2infosoft.giftcardup.model.Order;
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

public class MyOrder extends Fragment {
    private static final String TAG = MyOrder.class.getName();
    private Alert alert;
    private Urls urls;
    private Tags tags;
    private Active active;
    DMRRequest dmrRequest;
    private Progress progress;

    RecyclerView recyclerView;
    List<Object> cardList;
    MyOrderAdapter adapter;
    FrameLayout frameLayout;

    public MyOrder() {

    }

    private void init() {
        dmrRequest = DMRRequest.getInstance(getActivity(), TAG);
        urls = Urls.getInstance();
        tags = Tags.getInstance();
        active = Active.getInstance(getActivity());
        progress = new Progress(getActivity());
        cardList = new ArrayList<>();
        alert = Alert.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        init();
        View view =  inflater.inflate(R.layout.fragment_my_order, container, false);
        frameLayout = (FrameLayout)view.findViewById(R.id.frame);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyOrderAdapter(getContext(), cardList);
        recyclerView.setAdapter(adapter);
        loadCards();
        return view;
    }

    private void loadCards() {
        if (!active.isLogin()) {
            return;
        }
        if(!isConnected()){
            alert.showSnackIsConnected(isConnected());
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.MY_ORDERS_ALL);
        map.put(tags.USER_ID, active.getUser().getUserId());
        dmrRequest.doPost(urls.getUserInfo(), map, new DMRResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.has(tags.SUCCESS)) {
                        if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                            if (jsonObject.has(tags.MY_ORDERS_ALL)) {
                                List<Order> cards = new ArrayList<>();
                                JSONArray jsonArray = jsonObject.getJSONArray(tags.MY_ORDERS_ALL);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Order card = new Order();
                                    cards.add(card.fromJSON(jsonArray.getJSONObject(i)));
                                }
                                setDataInRecycleView(cards);
                            }
                        } else if (jsonObject.getInt(tags.SUCCESS) == tags.FAIL) {
                            frameLayout.setVisibility(View.VISIBLE);
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

    private void setDataInRecycleView(final List<Order> cards) {
        if (cards.size() > 0)
            cardList.addAll(cards);
        if (cardList.size() == 0) {
            cardList.add(new EmptyBrand());
        }
        adapter.notifyDataSetChanged();
    }
    private boolean isConnected() {
        return ConnectivityReceiver.isConnected();
    }
}
