package com.b2infosoft.giftcardup.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.activity.EditGiftCard;
import com.b2infosoft.giftcardup.app.Config;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.custom.AlertBox;
import com.b2infosoft.giftcardup.listener.OnLoadMoreListener;
import com.b2infosoft.giftcardup.model.GiftCard;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShipmentCardRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = ShipmentCardRecyclerViewAdapter.class.getName();
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
    private DMRRequest dmrRequest;
    private Urls urls;
    private Active active;

    public ShipmentCardRecyclerViewAdapter(Context context, List<GiftCard> cardInfoList, RecyclerView recyclerView) {
        this.context = context;
        this.cardInfoList = cardInfoList;
        config = Config.getInstance();
        tags = Tags.getInstance();
        dmrRequest = DMRRequest.getInstance(context, TAG);
        urls = Urls.getInstance();
        active = Active.getInstance(context);
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
        TextView cardOwner;
        TextView cardSerialNo;
        TextView cardPin;
        TextView cardBalance;
        TextView soldOn;
        TextView value;
        TextView sellingPercent;
        TextView type;
        TextView status;
        int count = 0;
        LinearLayout layout;
        Button shipmentLabel,info;
        View action_divider;

        public CardHolder(View view) {
            super(view);
            giftCard = (TextView) view.findViewById(R.id.company_card_gift_card);
            soldOn = (TextView) view.findViewById(R.id.company_card_sold_on);
            status = (TextView) view.findViewById(R.id.company_card_status);
            cardOwner = (TextView) view.findViewById(R.id.company_card_owner);
            cardSerialNo = (TextView) view.findViewById(R.id.company_card_serial_number);
            cardPin = (TextView) view.findViewById(R.id.company_card_pin);
            cardBalance = (TextView) view.findViewById(R.id.company_card_balance);
            value = (TextView) view.findViewById(R.id.company_card_value);
            sellingPercent = (TextView) view.findViewById(R.id.company_card_selling_percentage);
            type = (TextView) view.findViewById(R.id.company_card_type);
            layout = (LinearLayout)view.findViewById(R.id.layout_2);

            info = (Button) view.findViewById(R.id.info_btn);
            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count = count + 1;
                    if(count % 2 != 0) {
                        info.setText("- INFO");
                        layout.setVisibility(View.VISIBLE);
                    }else {
                        info.setText("+ INFO");
                        layout.setVisibility(View.GONE);
                    }
                }
            });
            shipmentLabel = (Button) view.findViewById(R.id.shipment_label_btn);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_pending_shipment_card, parent, false);
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

            cardHolder.giftCard.setText(card.getCardName() + "(" + card.getSerialNumber() + ")");
            //cardHolder.cardSell.setText(String.valueOf(card.getPercentageOff()) + "%");
            cardHolder.soldOn.setText(card.getSoldOn().equalsIgnoreCase("00-00-0000") ? "" : card.getSoldOn());
            cardHolder.status.setText(card.getApproveStatusName(card.getApproveStatus()));
            cardHolder.cardOwner.setText(card.getCardOwner());
            cardHolder.cardSerialNo.setText(card.getSerialNumber());
            cardHolder.cardPin.setText(card.getCardPin());
            cardHolder.cardBalance.setText("$" + String.valueOf(card.getCardPrice()));
            cardHolder.value.setText("$" + String.valueOf(card.getCardValue()));
            cardHolder.sellingPercent.setText(card.getSellingPercentage());

            cardHolder.type.setText(card.getStatusType() == 2 ? "Speedy Sell" : null);
            cardHolder.shipmentLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
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

    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public void removeAllItem(){
        cardInfoList.clear();
        notifyDataSetChanged();
    }
}
