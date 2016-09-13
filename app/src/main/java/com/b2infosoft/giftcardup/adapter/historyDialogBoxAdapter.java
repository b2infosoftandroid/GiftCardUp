package com.b2infosoft.giftcardup.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Config;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.listener.OnLoadMoreListener;
import com.b2infosoft.giftcardup.model.GetWithdrawHistory;
import com.b2infosoft.giftcardup.volly.DMRRequest;

import java.util.List;

public class HistoryDialogBoxAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = HistoryDialogBoxAdapter.class.getName();
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener mOnLoadMoreListener;
    private Context context;
    private List<GetWithdrawHistory> cardInfoList;
    private Config config;
    private Tags tags;
    private DMRRequest dmrRequest;
    private Urls urls;
    private Active active;

    public HistoryDialogBoxAdapter(Context context, List<GetWithdrawHistory> cardInfoList, RecyclerView recyclerView) {
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
        TextView cardDate;
        TextView status;
        TextView fund;

        public CardHolder(View view) {
            super(view);
            giftCard = (TextView) view.findViewById(R.id.withdrawal_history_card_name);
            cardDate = (TextView) view.findViewById(R.id.withdrawal_history_date);
            status = (TextView) view.findViewById(R.id.withdrawal_history_status2);
            fund = (TextView) view.findViewById(R.id.withdrawal_history_fund);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_dialog_withdrawal_history_inner, parent, false);
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
            final GetWithdrawHistory card = cardInfoList.get(position);
            final CardHolder cardHolder = (CardHolder) holder;

            cardHolder.giftCard.setText(card.getCardName());
            cardHolder.cardDate.setText(card.getRequestDate().equalsIgnoreCase("00/00/0000") ? "" : card.getRequestDate());
            cardHolder.status.setText(card.getPaymentStatus());
            cardHolder.fund.setText("$" + String.valueOf(card.getCreditAmount()));
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
