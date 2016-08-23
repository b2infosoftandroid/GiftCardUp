package com.b2infosoft.giftcardup.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Config;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.listener.OnLoadMoreListener;
import com.b2infosoft.giftcardup.model.CompanyBrand;
import com.b2infosoft.giftcardup.model.GiftCard;
import com.b2infosoft.giftcardup.volly.LruBitmapCache;
import com.b2infosoft.giftcardup.volly.MySingleton;

import java.util.List;

public class CompanyCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
    private CompanyBrand companyBrand;

    public CompanyCardAdapter(Context context, List<GiftCard> cardInfoList, RecyclerView recyclerView,CompanyBrand companyBrand) {
        this.context = context;
        this.cardInfoList = cardInfoList;
        config = Config.getInstance();
        this.companyBrand = companyBrand;
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
        TextView cardType;
        ImageView imageUrl;
        TextView cardValue;
        TextView cardOff;
        TextView cardPrice;
        Button buyNow,addToCart;
        public CardHolder(View view) {
            super(view);
            cardType = (TextView) view.findViewById(R.id.company_card_e_card);
            cardValue = (TextView) view.findViewById(R.id.company_card_card_value);
            cardOff = (TextView) view.findViewById(R.id.company_card_card_off);
            cardPrice = (TextView) view.findViewById(R.id.company_card_card_price);
            imageUrl = (ImageView) view.findViewById(R.id.company_card_image);
            buyNow = (Button)view.findViewById(R.id.company_card_card_buy_now);
            addToCart = (Button)view.findViewById(R.id.company_card_add_to_cart);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_card_item_company, parent, false);
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
            cardHolder.cardType.setText(companyBrand.getCardType()==2?"YES":"NO");
            cardHolder.cardOff.setText(String.valueOf(card.getPercentageOff())+"%");
            cardHolder.cardValue.setText("$"+String.valueOf(card.getCardPrice()));
            cardHolder.cardPrice.setText("$"+String.valueOf(card.getCardValue()));
            cardHolder.buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "LOGIN FIRST", Toast.LENGTH_SHORT).show();
                }
            });
            cardHolder.addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"Item is added to card",Toast.LENGTH_SHORT).show();
                }
            });
            final String url = config.getGiftCardImageAddress().concat(companyBrand.getImage());
            LruBitmapCache.loadCacheImage(context, cardHolder.imageUrl, url, CompanyCardAdapter.class.getName());
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
