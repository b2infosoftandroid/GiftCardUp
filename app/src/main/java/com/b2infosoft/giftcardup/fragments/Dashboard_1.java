package com.b2infosoft.giftcardup.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.adapter.CardAdapter_1;
import com.b2infosoft.giftcardup.app.Alert;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.model.CompanyBrand;
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

public class Dashboard_1 extends Fragment implements Paginate.Callbacks {
    private static final String TAG = Dashboard_1.class.getName();
    private Alert alert;
    private Urls urls;
    private Tags tags;
    private Active active;
    DMRRequest dmrRequest;
    RecyclerView recyclerView;
    CardAdapter_1 adapter;
    Button actionButton;
    List<Object> cardList;
    private OnFragmentDashboard mListener;
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


    public Dashboard_1() {

    }

    private void init() {
        alert = Alert.getInstance(getActivity());
        dmrRequest = DMRRequest.getInstance(getActivity(), TAG);
        urls = Urls.getInstance();
        tags = Tags.getInstance();
        active = Active.getInstance(getActivity());
        cardList = new ArrayList<>();
        handler = new Handler();
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
        adapter = new CardAdapter_1(getActivity(), items);
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
                if (items != null) {
                    if (adapter != null)
                        adapter.add(items);
                }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        init();
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        actionButton = (Button) view.findViewById(R.id.floating_btn);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = {"ALL", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
                        "U", "V", "W", "X", "Y", "Z"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        actionButton.setText(items[which]);
                        adapter.clear();
                        loadMore = 0;
                        isLoading = false;
                        loadCards();
                    }
                });
                builder.create().show();
            }
        });
        data_available_view = view.findViewById(R.id.frame);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        setupPagination();
        loadCards();
        return view;
    }

    /*
        private void setDataInRecycleView(final List<CompanyBrand> cards) {
            if (isLoading) {
                cardList.remove(cardList.size() - 1);
                adapter.notifyItemRemoved(cardList.size());
                isLoading = false;
            }
            //Log.d("search",cards.size() + "");
            if (cards.size() > 0)
                cardList.addAll(cards);
            if (cardList.size() == 0) {
                cardList.add(new EmptyBrand());
            }
            adapter.notifyDataSetChanged();
        }
    */
    private synchronized void loadCards() {
        if (isLoading()) {
            return;
        }
        if (!isConnected()) {
            alert.showSnackIsConnected(isConnected());
            return;
        }
        String s = actionButton.getText().toString();
        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.COMPANY_ALL_BRAND);
        map.put(tags.LOAD_MORE, String.valueOf(loadMore));
        if (!s.equalsIgnoreCase("ALL")) {
            map.put(tags.SORT_BY, s);
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
                    }
                    */
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

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onDashboard(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentDashboard) {
            mListener = (OnFragmentDashboard) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentDashboard");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentDashboard {
        void onDashboard(Uri uri);
    }

    // Method to manually check connection status
    private boolean isConnected() {
        return ConnectivityReceiver.isConnected();
    }

}
