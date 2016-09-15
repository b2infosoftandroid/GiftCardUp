package com.b2infosoft.giftcardup.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Format;
import com.b2infosoft.giftcardup.model.GetWithdrawHistory;
import com.b2infosoft.giftcardup.model.GiftCard;
import com.google.gson.Gson;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private Format format;
    private List<GiftCard> cardInfoList;

    public CartAdapter(Context context, List<GiftCard> cardInfoList) {
        this.context = context;
        this.cardInfoList = cardInfoList;
        format = Format.getInstance();
    }

    public class CardHolder extends RecyclerView.ViewHolder {
        TextView type;
        TextView value;
        TextView price;
        TextView saving;

        public CardHolder(View view) {
            super(view);
            type = (TextView) view.findViewById(R.id.card_type);
            value = (TextView) view.findViewById(R.id.card_value);
            price = (TextView) view.findViewById(R.id.card_price);
            saving = (TextView) view.findViewById(R.id.card_saving);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cart_item, parent, false);
        return new CardHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CardHolder) {
            final GiftCard card = cardInfoList.get(position);
            Gson gson = new Gson();
            Log.d("DATA",gson.toJson(card));
            final CardHolder cardHolder = (CardHolder) holder;
            cardHolder.type.setText(card.getCardType()==2?"E-CARD":"PHYSICAL");
            cardHolder.value.setText("$"+card.getCardPrice());
            cardHolder.price.setText("$"+card.getCardValue());
            cardHolder.saving.setText(String.valueOf(card.getSellingPercentage()));
        }
    }

    @Override
    public int getItemCount() {
        return cardInfoList == null ? 0 : cardInfoList.size();
    }

}
