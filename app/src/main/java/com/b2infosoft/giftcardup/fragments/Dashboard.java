package com.b2infosoft.giftcardup.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.adapter.CardAdapter;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.listener.OnLoadMoreListener;
import com.b2infosoft.giftcardup.model.CompanyBrand;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemCreator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dashboard extends Fragment {
    private static final String TAG = Dashboard.class.getName();
    private Urls urls;
    private Tags tags;
    private Active active;
    DMRRequest dmrRequest;
    RecyclerView recyclerView;
    CardAdapter adapter;
    Button actionButton;
    List<CompanyBrand> cardList;
    boolean isLoading = false;
    boolean isMore  = false;
    int loadMore = 0;
    private OnFragmentDashboard mListener;
    public Dashboard() {

    }

    private void init(){
        dmrRequest = DMRRequest.getInstance(getActivity(), TAG);
        urls = Urls.getInstance();
        tags = Tags.getInstance();
        active = Active.getInstance(getActivity());
        cardList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        init();
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        actionButton = (Button) view.findViewById(R.id.floating_btn);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T",
                                         "U","V","W","X","Y","Z"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         actionButton.setText(items[which]);
                    }
                });
                builder.create().show();
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CardAdapter(getActivity(), cardList, recyclerView);
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if(isMore) {
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

    private void setDataInRecycleView(final List<CompanyBrand> cards) {
        if (isLoading) {
            cardList.remove(cardList.size() - 1);
            adapter.notifyItemRemoved(cardList.size());
            isLoading = false;
        }
        cardList.addAll(cards);
        adapter.notifyDataSetChanged();
        adapter.setLoaded();
        //setPaginate();
    }

    private void setPaginate() {
        Paginate.Callbacks callbacks = new Paginate.Callbacks() {
            @Override
            public void onLoadMore() {

            }

            @Override
            public boolean isLoading() {
                return false;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return false;
            }
        };
        Paginate.with(recyclerView, callbacks)
                .setLoadingTriggerThreshold(2)
                .addLoadingListItem(true)
                .setLoadingListItemCreator(new CustomLoadingListItemCreator())
                .build();
        //.setLoadingListItemSpanSizeLookup(new CustomLoadingListItemSpanLookup())
    }

    private class CustomLoadingListItemCreator implements LoadingListItemCreator {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_loading_item, parent, false);
            return new LoadingHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof LoadingHolder) {
                LoadingHolder loadingHolder = (LoadingHolder) holder;
                loadingHolder.progressBar.setIndeterminate(true);
            }
        }

        public class LoadingHolder extends RecyclerView.ViewHolder {
            public ProgressBar progressBar;

            public LoadingHolder(View itemView) {
                super(itemView);
                progressBar = (ProgressBar) itemView.findViewById(R.id.onMoreLoadingProgressBar);
            }
        }
    }

    private void loadCards() {
        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION,tags.COMPANY_ALL_BRAND);
        map.put(tags.LOAD_MORE,String.valueOf(loadMore));
        dmrRequest.doPost(urls.getGiftCardInfo(), map, new DMRResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.has(tags.SUCCESS)) {
                        if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                            List<CompanyBrand> cards = new ArrayList<CompanyBrand>();
                            JSONArray jsonArray = jsonObject.getJSONArray(tags.GIFT_CARDS);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                CompanyBrand brand = new CompanyBrand();
                                cards.add(brand.fromJSON(jsonArray.getJSONObject(i)));
                            }
                            setDataInRecycleView(cards);
                        } else if (jsonObject.getInt(tags.SUCCESS) == tags.FAIL) {

                        } else {

                        }
                    }
                    if(jsonObject.has(tags.IS_MORE)){
                        isMore = jsonObject.getBoolean(tags.IS_MORE);
                        if(isMore){
                            loadMore+=tags.DEFAULT_LOADING_DATA;
                        }
                    }
                } catch (JSONException e) {

                }
            }

            @Override
            public void onError(VolleyError volleyError) {

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
}
