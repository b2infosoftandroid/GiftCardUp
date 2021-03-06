package com.b2infosoft.giftcardup.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.GiftCardApp;
import com.b2infosoft.giftcardup.model.Cart;
import com.b2infosoft.giftcardup.app.Config;
import com.b2infosoft.giftcardup.app.Format;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.custom.AlertBox;
import com.b2infosoft.giftcardup.custom.Progress;
import com.b2infosoft.giftcardup.model.EmptyBrand;
import com.b2infosoft.giftcardup.model.Order;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;
import com.b2infosoft.giftcardup.volly.LruBitmapCache;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private GiftCardApp app;
    private Cart cart;
    private final String TAG = MyOrderAdapter.class.getName();
    private Urls urls;
    private Tags tags;
    private Active active;
    DMRRequest dmrRequest;
    private Progress progress;
    private Context context;
    private Format format;
    private List<Object> cardInfoList;
    private Config config;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_EMPTY = 1;
    String str;

    public MyOrderAdapter(Context context, List<Object> cardInfoList) {
        this.context = context;
        this.cardInfoList = cardInfoList;
        format = Format.getInstance();
        config = Config.getInstance();
        tags = Tags.getInstance();
        urls = Urls.getInstance();
        active = Active.getInstance(context);
        dmrRequest = DMRRequest.getInstance(context, TAG);
        progress = new Progress(context);
        app = (GiftCardApp) context.getApplicationContext();
        cart = app.getCart();
    }

    public class CardHolder extends RecyclerView.ViewHolder {

        ImageView image;
        Button action;
        TextView name, card_id;
        TextView value, delivery;
        TextView price, status;

        public CardHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.card_image);
            action = (Button) view.findViewById(R.id.card_dispute_btn);
            name = (TextView) view.findViewById(R.id.card);
            card_id = (TextView) view.findViewById(R.id.card_id);
            value = (TextView) view.findViewById(R.id.card_value);
            price = (TextView) view.findViewById(R.id.card_price);
            delivery = (TextView) view.findViewById(R.id.card_delivery);
            status = (TextView) view.findViewById(R.id.card_status);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_my_order_item, parent, false);
            return new CardHolder(view);
        } else if (viewType == VIEW_TYPE_EMPTY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_empty_fragment, parent, false);
            return new EmptyHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return cardInfoList.get(position) instanceof EmptyBrand ? VIEW_TYPE_EMPTY : VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CardHolder) {
            final Order card = (Order) cardInfoList.get(position);
            final CardHolder cardHolder = (CardHolder) holder;

            LruBitmapCache.loadCacheImage(context, cardHolder.image, config.getGiftCardImageAddress().concat(card.getCardImg()), "");
            cardHolder.name.setText(card.getCardName());
            cardHolder.card_id.setText(String.valueOf(card.getMainOrderId()));
            cardHolder.delivery.setText(card.getOrderDate());
            if (card.getApproveStatus() == 7) {
                str = "Under Investigation";
                cardHolder.action.setVisibility(View.GONE);
            } else if (card.getApproveStatus() == 3 || card.getApproveStatus() == 6) {
                str = "Completed";
            } else if (card.getApproveStatus() == 8) {
                str = "Investigated";
                cardHolder.action.setVisibility(View.GONE);
            }
            cardHolder.status.setText(str);
            cardHolder.value.setText("$" + card.getValue());
            cardHolder.price.setText("$" + card.getPrice());
            cardHolder.action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Dispute Gift Card");
                    final EditText ed1 = new EditText(context);
                    ed1.setLines(5);
                    ed1.setGravity(Gravity.START);
                    ed1.setPadding(16, 16, 16, 16);
                    ed1.setHint("Add Order Review");
                    ed1.setBackground(context.getResources().getDrawable(R.drawable.layout_border));
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    lp.setMargins(20, 10, 20, 0);
                    ed1.setLayoutParams(lp);
                    builder.setView(ed1);
                    builder.setPositiveButton("Send Review", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progress.show();
                            final Map<String, String> map = new HashMap<>();
                            map.put(tags.USER_ACTION, tags.CARD_DISPUTE_REVIEW);
                            map.put(tags.USER_ID, active.getUser().getUserId());
                            map.put(tags.GIFT_ID, String.valueOf(card.getGiftCardId()));
                            map.put(tags.REVIEW, ed1.getText().toString());

                            dmrRequest.doPost(urls.getUserInfo(), map, new DMRResult() {
                                @Override
                                public void onSuccess(JSONObject jsonObject) {
                                    progress.dismiss();
                                    try {
                                        if (jsonObject.has(tags.SUCCESS)) {
                                            if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                                                AlertBox box = new AlertBox(context);
                                                box.setMessage("Successful");
                                                box.show();
                                                card.setApproveStatus(7);
                                                MyOrderAdapter.super.notifyDataSetChanged();
                                            } else if (jsonObject.getInt(tags.SUCCESS) == tags.FAIL) {

                                            } else {

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
                                    if (volleyError.getMessage() != null)
                                        Log.e(TAG, volleyError.getMessage());
                                    progress.dismiss();
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
            });
        } else if (holder instanceof EmptyHolder) {

        }
    }

    @Override
    public int getItemCount() {
        return cardInfoList == null ? 0 : cardInfoList.size();
    }

}
