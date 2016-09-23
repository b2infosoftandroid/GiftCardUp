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
import com.b2infosoft.giftcardup.adapter.ShipmentCardRecyclerViewAdapter;
import com.b2infosoft.giftcardup.adapter.WithdrawalHistoryRecyclerViewAdapter;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.custom.Progress;
import com.b2infosoft.giftcardup.listener.OnLoadMoreListener;
import com.b2infosoft.giftcardup.model.EmptyBrand;
import com.b2infosoft.giftcardup.model.GetWithdrawHistory;
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


public class WithdrawalHistory extends Fragment {
    private final static String TAG = WithdrawalHistory.class.getName();
    private Urls urls;
    private Tags tags;
    private Active active;
    DMRRequest dmrRequest;
    private Progress progress;
    FrameLayout frameLayout;
    RecyclerView recyclerView;
    WithdrawalHistoryRecyclerViewAdapter adapter;
    List<Object> cardList;
    boolean isLoading = false;
    boolean isMore = false;
    int loadMore = 0;


    public WithdrawalHistory() {
        // Required empty public constructor
    }

    private void init() {
        dmrRequest = DMRRequest.getInstance(getActivity(), TAG);
        urls = Urls.getInstance();
        tags = Tags.getInstance();
        active = Active.getInstance(getActivity());
        progress = new Progress(getActivity());
        cardList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        init();
        View view = inflater.inflate(R.layout.fragment_withdrawal_history, container, false);
        frameLayout = (FrameLayout)view.findViewById(R.id.frame);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new WithdrawalHistoryRecyclerViewAdapter(getContext(), cardList, recyclerView);
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (isMore) {
                    cardList.add(null);
                    adapter.notifyItemInserted(cardList.size() - 1);
                    isLoading = true;
                    loadCards();
                }
            }
        });
        loadCards();
        return view;
    }

    private void loadCards() {
        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.WITHDRAWAL_HISTORY);
        map.put(tags.USER_ID, active.getUser().getUserId() + "");
        dmrRequest.doPost(urls.getUserInfo(), map, new DMRResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                //Log.d("history", jsonObject.toString());
                try {
                    if (jsonObject.has(tags.SUCCESS)) {
                        if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                            if (jsonObject.has(tags.WITHDRAWAL_HISTORY)) {
                                List<GetWithdrawHistory> cards = new ArrayList<>();
                                JSONArray jsonArray = jsonObject.getJSONArray(tags.WITHDRAWAL_HISTORY);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    GetWithdrawHistory card = new GetWithdrawHistory();
                                    cards.add(card.fromJSON(jsonArray.getJSONObject(i)));
                                }
                                setDataInRecycleView(cards);
                            }
                        } else if (jsonObject.getInt(tags.SUCCESS) == tags.FAIL) {
                            frameLayout.setVisibility(View.VISIBLE);
                        }
                    }
                    if (jsonObject.has(tags.IS_MORE)) {
                        isMore = jsonObject.getBoolean(tags.IS_MORE);
                        if (isMore) {
                            loadMore += tags.DEFAULT_LOADING_DATA;
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

    private void setDataInRecycleView(final List<GetWithdrawHistory> cards) {
        if (isLoading) {
            cardList.remove(cardList.size() - 1);
            adapter.notifyItemRemoved(cardList.size());
            isLoading = false;
        }
        Log.d("find",cards.size() + "");
        if (cards.size() > 0)
            cardList.addAll(cards);
        if (cardList.size() == 0) {
            cardList.add(new EmptyBrand());
        }
        adapter.notifyDataSetChanged();
        adapter.setLoaded();
    }

}
