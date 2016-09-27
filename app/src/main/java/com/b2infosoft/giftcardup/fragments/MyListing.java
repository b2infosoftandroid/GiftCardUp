package com.b2infosoft.giftcardup.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.adapter.MyListingAdapter;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.custom.Progress;
import com.b2infosoft.giftcardup.listener.OnLoadMoreListener;
import com.b2infosoft.giftcardup.model.EmptyBrand;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class MyListing extends Fragment implements View.OnClickListener {
    private final static String TAG = MyListing.class.getName();
    private Urls urls;
    private Tags tags;
    private Active active;
    DMRRequest dmrRequest;
    private Progress progress;
    View mView;
    RecyclerView recyclerView;
    FrameLayout frameLayout;
    MyListingAdapter adapter;
    List<Object> cardList;
    boolean isLoading = false;
    boolean isMore = false;
    int loadMore = 0;
    boolean isFilter = false;
    EditText search;
    Spinner type;
    ImageButton action_search;

    public MyListing() {

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
        mView = inflater.inflate(R.layout.fragment_my_listing, container, false);
        frameLayout = (FrameLayout)mView.findViewById(R.id.frame);
        recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyListingAdapter(getContext(), cardList, recyclerView);
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
        if (cardList.size() > 0) {
            cardList.clear();
            adapter.removeAllItem();
        }
        loadMore = 0;
        loadCards();
    }

    private void loadCards() {
        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.GET_GIFT_CARD_BY_USER_ID);
        map.put(tags.USER_ID, active.getUser().getUserId() + "");
        map.put(tags.LOAD_MORE, String.valueOf(loadMore));
        if (isFilter) {
            map.put(tags.KEYWORD_TYPE, type.getSelectedItem().toString());
            map.put(tags.KEYWORD_SEARCH, search.getText().toString());
        }
        dmrRequest.doPost(urls.getGiftCardInfo(), map, new DMRResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                Log.d("history", jsonObject.toString());
                try {
                    if (jsonObject.has(tags.SUCCESS)) {
                        if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                            if (jsonObject.has(tags.GIFT_CARDS)) {
                                List<GiftCard> cards = new ArrayList<>();
                                JSONArray jsonArray = jsonObject.getJSONArray(tags.GIFT_CARDS);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    GiftCard card = new GiftCard();
                                    cards.add(card.fromJSON(jsonArray.getJSONObject(i)));
                                }
                                setDataInRecycleView(cards);
                            }
                        } else if (jsonObject.getInt(tags.SUCCESS) == tags.FAIL) {
                            frameLayout.setVisibility(View.VISIBLE);
                        } else {

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
}
