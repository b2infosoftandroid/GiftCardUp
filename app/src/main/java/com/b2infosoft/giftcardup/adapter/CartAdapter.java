package com.b2infosoft.giftcardup.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Cart;
import com.b2infosoft.giftcardup.app.Config;
import com.b2infosoft.giftcardup.app.Format;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.custom.AlertBox;
import com.b2infosoft.giftcardup.custom.Progress;
import com.b2infosoft.giftcardup.model.CartSummary;
import com.b2infosoft.giftcardup.model.GiftCard;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;
import com.b2infosoft.giftcardup.volly.LruBitmapCache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Cart cart;
    private final String TAG = CartAdapter.class.getName();
    private Urls urls;
    private Tags tags;
    private Active active;
    DMRRequest dmrRequest;
    private Progress progress;
    private Context context;
    private Format format;
    private List<Object> cardInfoList;
    private Config config;
    private final int VIEW_CART_ITEM = 0;
    private final int VIEW_CART_SUMMARY = 1;

    public CartAdapter(Context context, List<Object> cardInfoList) {
        this.context = context;
        this.cardInfoList = cardInfoList;
        format = Format.getInstance();
        config = Config.getInstance();
        tags = Tags.getInstance();
        urls = Urls.getInstance();
        active = Active.getInstance(context);
        dmrRequest = DMRRequest.getInstance(context,TAG);
        progress = new Progress(context);
        cart = (Cart)context.getApplicationContext();
    }

    public class CardHolder extends RecyclerView.ViewHolder {

        ImageView image;
        Button action_delete;
        TextView name;
        TextView type;
        TextView value;
        TextView price;
        TextView saving;

        public CardHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.card_image);
            action_delete = (Button) view.findViewById(R.id.action_delete);
            name = (TextView) view.findViewById(R.id.card_name);
            type = (TextView) view.findViewById(R.id.card_type);
            value = (TextView) view.findViewById(R.id.card_value);
            price = (TextView) view.findViewById(R.id.card_price);
            saving = (TextView) view.findViewById(R.id.card_saving);
        }
    }

    public class Summery extends RecyclerView.ViewHolder {
        TextView value;
        TextView price;
        TextView saving;

        public Summery(View view) {
            super(view);
            value = (TextView) view.findViewById(R.id.card_value);
            price = (TextView) view.findViewById(R.id.card_price);
            saving = (TextView) view.findViewById(R.id.card_saving);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_CART_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cart_item, parent, false);
            return new CardHolder(view);
        } else if (viewType == VIEW_CART_SUMMARY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cart_item_summery, parent, false);
            return new Summery(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return cardInfoList.get(position) instanceof CartSummary ? VIEW_CART_SUMMARY : VIEW_CART_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CardHolder) {
            final GiftCard card = (GiftCard) cardInfoList.get(position);
            final CardHolder cardHolder = (CardHolder) holder;
            LruBitmapCache.loadCacheImage(context, cardHolder.image, config.getGiftCardImageAddress().concat(card.getCardImage()), "");
            cardHolder.type.setText(card.getCardType() == 2 ? "E-CARD" : "PHYSICAL");
            cardHolder.name.setText(card.getCardName());
            cardHolder.value.setText("$" + card.getCardPrice());
            cardHolder.price.setText("$" + card.getCardValue());
            cardHolder.saving.setText(String.valueOf(card.getSellingPercentage()) + "%");
            cardHolder.action_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Alert");
                    builder.setMessage("Sure to delete ?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            progress.show();
                            final Map<String, String> map = new HashMap<>();
                            map.put(tags.USER_ACTION, tags.REMOVE_CART_ITEM_GIFT_CARD);
                            map.put(tags.USER_ID, active.getUser().getUserId());
                            map.put(tags.GIFT_CARD_GIFT_CARD_ID, card.getGiftCardID() + "");

                            dmrRequest.doPost(urls.getCartInfo(), map, new DMRResult() {
                                @Override
                                public void onSuccess(JSONObject jsonObject) {
                                    progress.dismiss();
                                    try {
                                        if (jsonObject.has(tags.SUCCESS)) {
                                            if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                                                JSONArray array = jsonObject.getJSONArray(tags.GIFT_CARDS);
                                                cart.removeAll();
                                                for (int i = 0; i < array.length(); i++) {
                                                    cart.addCartItem(GiftCard.fromJSON(array.getJSONObject(i)));
                                                }
                                                showMessage("Successfully remove to Cart ");
                                                cardInfoList.remove(card);
                                                CartAdapter.super.notifyDataSetChanged();
                                            } else if (jsonObject.getInt(tags.SUCCESS) == tags.SUSPEND) {

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
                                    Log.e(TAG, volleyError.getMessage());
                                    progress.dismiss();
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
            });
        } else if (holder instanceof Summery) {
            final CartSummary card = (CartSummary) cardInfoList.get(position);
            final Summery cardHolder = (Summery) holder;
            cardHolder.value.setText("$" + card.getPrice());
            cardHolder.price.setText("$" + card.getValue());
            cardHolder.saving.setText("$" + card.getSaving());
        }
    }

    @Override
    public int getItemCount() {
        return cardInfoList == null ? 0 : cardInfoList.size();
    }

    private void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
