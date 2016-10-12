package com.b2infosoft.giftcardup.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.activity.CompanyCard;
import com.b2infosoft.giftcardup.activity.CompanyCard_1;
import com.b2infosoft.giftcardup.app.Config;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.listener.OnLoadMoreListener;
import com.b2infosoft.giftcardup.model.CompanyBrand;
import com.b2infosoft.giftcardup.model.EmptyBrand;
import com.b2infosoft.giftcardup.volly.LruBitmapCache;

import java.util.List;

public class CardAdapter_1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final int VIEW_TYPE_EMPTY = 2;
    private Context context;
    private List<Object> cardList;
    private Config config;
    private Tags tags;
    public CardAdapter_1(Context context, List<Object> cardList) {
        this.context = context;
        this.cardList = cardList;
        config = Config.getInstance();
        tags = Tags.getInstance();
    }

    public class CardHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView imageUrl;
        TextView count;
        TextView discount;

        public CardHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.card_item_company_name);
            count = (TextView) view.findViewById(R.id.card_item_count);
            discount = (TextView) view.findViewById(R.id.card_item_saving);
            imageUrl = (ImageView) view.findViewById(R.id.card_item_imgae);
        }
    }

    public class LoadingHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.onMoreLoadingProgressBar);
        }
    }

    public class EmptyHolder extends RecyclerView.ViewHolder {

        public EmptyHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_card_item, parent, false);
            return new CardHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_item, parent, false);
            return new LoadingHolder(view);
        } else if (viewType == VIEW_TYPE_EMPTY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_empty_brand, parent, false);
            return new EmptyHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        Object obj = cardList.get(position);
        return obj instanceof EmptyBrand ? VIEW_TYPE_EMPTY : obj == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CardHolder) {
            final CompanyBrand card = (CompanyBrand)cardList.get(position);
            final CardHolder cardHolder = (CardHolder) holder;
            cardHolder.name.setText(card.getCompanyName());
            cardHolder.discount.setText(String.valueOf(card.getDiscount()) + "%");
            cardHolder.count.setText("(" + String.valueOf(card.getCount()) + ")");
            final String url = config.getGiftCardImageAddress().concat(card.getImage());
            cardHolder.imageUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CompanyCard_1.class);
                    intent.putExtra(tags.COMPANY_BRAND, card);
                    context.startActivity(intent);
                }
            });
            LruBitmapCache.loadCacheImage(context, cardHolder.imageUrl, url, CardAdapter_1.class.getName());
        } else if (holder instanceof LoadingHolder) {
            LoadingHolder loadingHolder = (LoadingHolder) holder;
            loadingHolder.progressBar.setIndeterminate(true);
        } else if (holder instanceof EmptyHolder) {

        }
    }

    @Override
    public int getItemCount() {
        return cardList == null ? 0 : cardList.size();
    }
    public void clear() {
        cardList.clear();
        this.notifyDataSetChanged();
    }

    public void add(List<Object> items) {
        int previousDataSize = this.cardList.size();
        this.cardList.addAll(items);
        notifyItemRangeInserted(previousDataSize, items.size());
    }
}
