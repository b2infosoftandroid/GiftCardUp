package com.b2infosoft.giftcardup.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Config;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.listener.OnLoadMoreListener;
import com.b2infosoft.giftcardup.model.GetWithdrawHistory;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WithdrawalHistoryRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = WithdrawalHistoryRecyclerViewAdapter.class.getName();
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener mOnLoadMoreListener;
    private Context context;
    private List<GetWithdrawHistory> cardInfoList;
    private Config config;
    HistoryDialogBoxAdapter adapter;
    private Tags tags;
    private DMRRequest dmrRequest;
    private Urls urls;
    private Active active;

    public WithdrawalHistoryRecyclerViewAdapter(Context context, List<GetWithdrawHistory> cardInfoList, RecyclerView recyclerView) {
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
        TextView request_date;
        TextView payment_method;
        TextView amount;
        TextView approve_date;
        TextView status;
        int count = 0;
        LinearLayout layout;
        Button request_id;
        View action_divider;

        public CardHolder(View view) {
            super(view);
            request_date = (TextView) view.findViewById(R.id.withdrawal_history_req_date);
            payment_method = (TextView) view.findViewById(R.id.withdrawal_history_pay_method);
            amount = (TextView) view.findViewById(R.id.withdrawal_history_amount);
            approve_date = (TextView) view.findViewById(R.id.withdrawal_history_approve_date);
            status = (TextView) view.findViewById(R.id.withdrawal_history_status);
            request_id = (Button) view.findViewById(R.id.withdrawal_history_req_id_btn);

            layout = (LinearLayout)view.findViewById(R.id.layout_2);

            request_id = (Button) view.findViewById(R.id.withdrawal_history_req_id_btn);
            request_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count = count + 1;
                    if(count % 2 != 0) {
                        request_id.setText("");
                        layout.setVisibility(View.VISIBLE);
                    }else {
                        request_id.setText("");
                        layout.setVisibility(View.GONE);
                    }
                }
            });
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_withdrawal_history_card, parent, false);
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
        if (holder instanceof CardHolder)
        {
            final GetWithdrawHistory card = cardInfoList.get(position);
            final CardHolder cardHolder = (CardHolder) holder;

            cardHolder.request_date.setText(card.getRequestDate().equalsIgnoreCase("00-00-0000") ? "" : card.getRequestDate());
            cardHolder.status.setText(card.getPaymentStatus() == 0 ? "Processing" : "Complete");
            cardHolder.payment_method.setText(card.getPaymentMethod());
            cardHolder.amount.setText("$" + String.valueOf(card.getAmount()));
            cardHolder.approve_date.setText(card.getApproveDate().equalsIgnoreCase("00/00/0000") ? "" : card.getApproveDate());
            cardHolder.request_id.setText(String.valueOf(card.getRequestId()));

            cardHolder.request_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog dialog = new Dialog(context);
                    dialog.setTitle("Available Funds");
                    dialog.setContentView(R.layout.custom_dialog_history);

                   final RecyclerView  recyclerView = (RecyclerView) dialog.findViewById(R.id.recycler_view);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    Map<String, String> map = new HashMap<>();
                    map.put(tags.USER_ACTION, tags.WITHDRAWAL_HISTORY_VIEW);
                    map.put(tags.WITHDRAWAL_PAYMENT_ID, card.getPaymentIds() + "");
                    dmrRequest.doPost(urls.getUserInfo(), map, new DMRResult() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            Log.d("history", jsonObject.toString());
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
                                            adapter = new HistoryDialogBoxAdapter(context, cards);
                                            recyclerView.setAdapter(adapter);

                                        }
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
                    dialog.show();
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
