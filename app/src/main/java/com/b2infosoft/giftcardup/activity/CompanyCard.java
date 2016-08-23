package com.b2infosoft.giftcardup.activity;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.adapter.CompanyCardAdapter;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.listener.OnLoadMoreListener;
import com.b2infosoft.giftcardup.model.CompanyBrand;
import com.b2infosoft.giftcardup.model.GiftCard;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyCard extends AppCompatActivity {
    private static final String TAG = CompanyCard.class.getName();
    private Urls urls;
    private Tags tags;
    private Active active;
    DMRRequest dmrRequest;
    RecyclerView recyclerView;
    CompanyCardAdapter adapter;
    List<GiftCard> cardList;
    boolean isLoading = false;
    boolean isMore  = false;
    int loadMore = 0;
    private CompanyBrand companyBrand;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_card);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tags =Tags.getInstance();
        dmrRequest = DMRRequest.getInstance(this, TAG);
        urls = Urls.getInstance();
        active = Active.getInstance(this);
        cardList = new ArrayList<>();
        if(getIntent().hasExtra(tags.COMPANY_BRAND)){
            companyBrand =(CompanyBrand) getIntent().getExtras().getSerializable(tags.COMPANY_BRAND);
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CompanyCardAdapter(this, cardList, recyclerView,companyBrand);
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
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDataInRecycleView(final List<GiftCard> cards) {
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
    private void loadCards() {
        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION,tags.COMPANY_ID_BRAND);
        map.put(tags.COMPANY_ID,String.valueOf(companyBrand.getCompanyID()));
        map.put(tags.LOAD_MORE,String.valueOf(loadMore));
        dmrRequest.doPost(urls.getUrlCardsAll(), map, new DMRResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.has(tags.SUCCESS)) {
                        if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                            List<GiftCard> cards = new ArrayList<GiftCard>();
                            if(jsonObject.has(tags.GIFT_CARDS)) {
                                JSONArray jsonArray = jsonObject.getJSONArray(tags.GIFT_CARDS);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    GiftCard card = new GiftCard();
                                    cards.add(card.fromJSON(jsonArray.getJSONObject(i)));
                                    Log.d(TAG,jsonArray.getJSONObject(i).toString());
                                }
                                setDataInRecycleView(cards);
                            }
                        } else if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {

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
                    e.printStackTrace();
                    Log.e(TAG,e.getMessage());
                }
            }

            @Override
            public void onError(VolleyError volleyError) {
                volleyError.printStackTrace();
                Log.e(TAG,volleyError.getMessage());
            }
        });
    }
}
