package com.b2infosoft.giftcardup.adapter;

/**
 * Created by rajesh on 9/16/2016.
 */
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
import com.b2infosoft.giftcardup.app.Format;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.listener.OnLoadMoreListener;
import com.b2infosoft.giftcardup.model.GetWithdrawHistory;
import com.b2infosoft.giftcardup.volly.DMRRequest;

import java.util.List;

public class HistoryDialogBoxAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // RAJESH CHANGE
    private Context context;
    private Format format;
    private List<GetWithdrawHistory> cardInfoList;

    public HistoryDialogBoxAdapter(Context context, List<GetWithdrawHistory> cardInfoList) {
        this.context = context;
        this.cardInfoList = cardInfoList;
        format = Format.getInstance();
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


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_dialog_withdrawal_history_inner, parent, false);
        return new CardHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CardHolder) {
            final GetWithdrawHistory card = cardInfoList.get(position);
            final CardHolder cardHolder = (CardHolder) holder;
            cardHolder.giftCard.setText(card.getCardName());
            cardHolder.cardDate.setText(card.getRequestDate().equalsIgnoreCase("00/00/0000") ? "" : format.getDate(card.getRequestDate()));
            cardHolder.status.setText(card.getPaymentStatus() == 0 ? "Processing" : "Completed");
            cardHolder.fund.setText("$" + String.valueOf(card.getCreditAmount()));
        }
    }

    @Override
    public int getItemCount() {
        return cardInfoList == null ? 0 : cardInfoList.size();
    }

}


