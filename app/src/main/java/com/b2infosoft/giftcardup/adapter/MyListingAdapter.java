package com.b2infosoft.giftcardup.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.activity.Main;
import com.b2infosoft.giftcardup.app.Config;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.listener.OnLoadMoreListener;
import com.b2infosoft.giftcardup.model.CompanyBrand;
import com.b2infosoft.giftcardup.model.GiftCard;
import com.b2infosoft.giftcardup.volly.LruBitmapCache;

import java.util.List;

public class MyListingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener mOnLoadMoreListener;
    private Context context;
    private List<GiftCard> cardInfoList;
    private Config config;
    private Tags tags;
    int count = 0;

    public MyListingAdapter(Context context, List<GiftCard> cardInfoList, RecyclerView recyclerView) {
        this.context = context;
        this.cardInfoList = cardInfoList;
        config = Config.getInstance();
        tags = Tags.getInstance();
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public class CardHolder extends RecyclerView.ViewHolder {
        TextView giftCard;
        ImageView cardType;
        TextView cardValue;
        TextView cardPrice;
        TextView cardSell;
        TextView listedOn;
        TextView soldOn;
        TextView fund;
        ImageView quickSell;
        TextView status;
        public CardHolder(View view) {
            super(view);
            giftCard = (TextView) view.findViewById(R.id.company_card_gift_card);
            cardType = (ImageView) view.findViewById(R.id.company_card_e_card);
            cardValue = (TextView) view.findViewById(R.id.company_card_card_value);
            cardPrice = (TextView) view.findViewById(R.id.company_card_card_price);
            cardSell = (TextView) view.findViewById(R.id.company_card_card_sell);
            listedOn = (TextView) view.findViewById(R.id.company_card_listed_on);
            soldOn = (TextView) view.findViewById(R.id.company_card_sold_on);
            fund = (TextView) view.findViewById(R.id.company_card_fund);
            quickSell = (ImageView) view.findViewById(R.id.company_card_quick_sell);
            status = (TextView) view.findViewById(R.id.company_card_status);

        }
    }

    public class LoadingHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.onMoreLoadingProgressBar);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_my_listing_item, parent, false);
            return new CardHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_item, parent, false);
            return new LoadingHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return cardInfoList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CardHolder) {
            final GiftCard card = cardInfoList.get(position);
            final CardHolder cardHolder = (CardHolder) holder;

            /* E-Card */
            if (card.getCardType() == 2) {
                cardHolder.cardType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_check_24dp));
            } else {
                cardHolder.cardType.setImageDrawable(null);
            }
            /* Speedy Shell */
            if (card.getStatusType() == 2) {
                cardHolder.quickSell.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_check_24dp));
            } else {
                cardHolder.quickSell.setImageDrawable(null);
            }
            cardHolder.giftCard.setText(card.getCardName() + "(" + card.getSerialNumber() + ")");
            cardHolder.cardSell.setText(String.valueOf(card.getPercentageOff()) + "%");
            cardHolder.cardValue.setText("$" + String.valueOf(card.getCardPrice()));
            cardHolder.cardPrice.setText("$" + String.valueOf(card.getCardValue()));
            cardHolder.cardSell.setText(card.getStatusType()==2?card.getSellingPercentage():card.getSell()+"%");
            cardHolder.listedOn.setText(card.getApproveDate().equalsIgnoreCase("00-00-0000")?"":card.getApproveDate());
            cardHolder.soldOn.setText(card.getSoldOn().equalsIgnoreCase("00-00-0000")?"":card.getSoldOn());
            cardHolder.status.setText(card.getApproveStatusName(card.getApproveStatus()));
        } else if (holder instanceof LoadingHolder) {
            LoadingHolder loadingHolder = (LoadingHolder) holder;
            loadingHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return cardInfoList == null ? 0 : cardInfoList.size();
    }

    public void setLoaded() {
        isLoading = false;
    }
}
