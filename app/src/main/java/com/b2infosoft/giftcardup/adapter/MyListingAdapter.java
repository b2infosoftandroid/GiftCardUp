package com.b2infosoft.giftcardup.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.b2infosoft.giftcardup.model.EmptyBrand;
import com.b2infosoft.giftcardup.model.GiftCard;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;
import com.b2infosoft.giftcardup.volly.LruBitmapCache;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyListingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = MyListingAdapter.class.getName();
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final int VIEW_TYPE_EMPTY = 2;
    private Context context;
    private List<Object> cardInfoList;
    private Config config;
    private Tags tags;
    private DMRRequest dmrRequest;
    private Urls urls;
    private Active active;

    public MyListingAdapter(Context context, List<Object> cardInfoList) {
        this.context = context;
        this.cardInfoList = cardInfoList;
        config = Config.getInstance();
        tags = Tags.getInstance();
        dmrRequest = DMRRequest.getInstance(context, TAG);
        urls = Urls.getInstance();
        active = Active.getInstance(context);
    }
    public class CardHolder extends RecyclerView.ViewHolder {
        TextView giftCard;
        ImageView cardType, cardImage;
        TextView cardValue;
        TextView cardPrice;
        TextView cardSell;
        TextView listedOn;
        TextView soldOn;
        TextView fund;
        LinearLayout card2;
        CardView card1;
        ImageView quickSell;
        TextView status;
        ImageView action_edit, action_delete, action_deny, action_need_review, action_investigate;
        //View action_divider;

        public CardHolder(View view) {
            super(view);
            card1 = (CardView) view.findViewById(R.id.card_view1);
            card2 = (LinearLayout) view.findViewById(R.id.card_view2);
            cardImage = (ImageView) view.findViewById(R.id.my_listing_card_image);
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
            action_edit = (ImageView) view.findViewById(R.id.action_edit);
            action_delete = (ImageView) view.findViewById(R.id.action_delete);
            action_deny = (ImageView) view.findViewById(R.id.action_deny);
            action_need_review = (ImageView) view.findViewById(R.id.action_need_review);
            action_investigate = (ImageView) view.findViewById(R.id.action_investigate);
            //action_divider = view.findViewById(R.id.action_divider);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_my_listing_item, parent, false);
            return new CardHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_item, parent, false);
            return new LoadingHolder(view);
        } else if (viewType == VIEW_TYPE_EMPTY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_empty_fragment, parent, false);
            return new EmptyHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        Object obj = cardInfoList.get(position);
        return obj instanceof EmptyBrand ? VIEW_TYPE_EMPTY : obj == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CardHolder) {
            final GiftCard card = (GiftCard) cardInfoList.get(position);
            final CardHolder cardHolder = (CardHolder) holder;
            String url = config.getGiftCardImageAddress();
            if (card.getCardImage() == null || (!card.getCardImage().contains(".")) || card.getCardImage().length() == 0) {
                if (card.getCardCompanyImage() != null && !card.getCardCompanyImage().equalsIgnoreCase("null")) {
                    url = url.concat(card.getCardCompanyImage());
                }
            } else {
                url = url.concat(card.getCardImage());
            }
            Log.d("urls", url);
            LruBitmapCache.loadCacheImage(context, cardHolder.cardImage, url, "");
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
            //cardHolder.cardSell.setText(String.valueOf(card.getPercentageOff()) + "%");
            cardHolder.cardValue.setText("$" + String.valueOf(card.getCardPrice()));
            cardHolder.cardPrice.setText("$" + String.valueOf(card.getCardValue()));
            cardHolder.cardSell.setText(card.getStatusType() == 2 ? card.getSellingPercentage() : card.getSell() + "%");
            cardHolder.listedOn.setText(card.getApproveDate().equalsIgnoreCase("00-00-0000") ? "" : card.getApproveDate());
            cardHolder.soldOn.setText(card.getSoldOn().equalsIgnoreCase("00-00-0000") ? "" : card.getSoldOn());
            cardHolder.fund.setText("$" + card.getYourEarning());
            cardHolder.status.setText(card.getApproveStatusName(card.getApproveStatus()));

            cardHolder.status.setBackgroundColor(card.getApproveStatusColor(context, card.getApproveStatus()));

            setActions(cardHolder, card.getApproveStatus());
            cardHolder.card1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardHolder.card2.setVisibility(cardHolder.card2.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                }
            });
            cardHolder.action_edit.setOnClickListener(new OnClick(card));
            cardHolder.action_delete.setOnClickListener(new OnClick(card));
            cardHolder.action_deny.setOnClickListener(new OnClick(card));
            cardHolder.action_investigate.setOnClickListener(new OnClick(card));
            cardHolder.action_need_review.setOnClickListener(new OnClick(card));
        } else if (holder instanceof LoadingHolder) {
            LoadingHolder loadingHolder = (LoadingHolder) holder;
            loadingHolder.progressBar.setIndeterminate(true);
        } else if (holder instanceof EmptyHolder) {

        }
    }

    private class OnClick implements View.OnClickListener {
        private GiftCard giftCard;

        public OnClick(GiftCard giftCard) {
            this.giftCard = giftCard;
        }

        @Override
        public void onClick(View v) {
            AlertBox box = new AlertBox(context);
            switch (v.getId()) {
                case R.id.action_edit:
                    Intent intent = new Intent(context, EditGiftCard.class);
                    intent.putExtra(tags.GIFT_CARDS, giftCard);
                    context.startActivity(intent);
                    break;
                case R.id.action_delete:
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Alert");
                    builder.setMessage("Sure You Want To Delete Card");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Map<String, String> map = new HashMap<>();
                            map.put(tags.USER_ACTION, tags.DELETE_GIFT_CARD);
                            map.put(tags.USER_ID, active.getUser().getUserId() + "");
                            map.put(tags.CARD_ID, giftCard.getGiftCardID() + "");
                            dmrRequest.doPost(urls.getGiftCardInfo(), map, new DMRResult() {
                                @Override
                                public void onSuccess(JSONObject jsonObject) {
                                    try {
                                        if (jsonObject.has(tags.SUCCESS)) {
                                            if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                                                int index = cardInfoList.indexOf(giftCard);
                                                giftCard.setApproveStatus(5);
                                                cardInfoList.set(index, giftCard);
                                                MyListingAdapter.super.notifyDataSetChanged();
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
                                        Log.e(TAG,volleyError.getMessage());
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {

                                }
                            });
                        }
                    });
                    builder.create().show();
                    break;
                case R.id.action_deny:
                    box.setTitle("Reason for card deny");
                    box.setMessage(giftCard.getDenyReason());
                    box.show();
                    break;
                case R.id.action_investigate:
                    box.setTitle("Admin Comment");
                    box.setMessage(giftCard.getDisputeResult());
                    box.show();
                    break;
                case R.id.action_need_review:
                    box.setTitle("Reason for needs review");
                    box.setMessage(giftCard.getNeedReview());
                    box.show();
                    break;
            }
        }
    }

    private void setActions(CardHolder cardHolder, int status) {
        cardHolder.action_edit.setVisibility(View.GONE);
        cardHolder.action_delete.setVisibility(View.GONE);
        cardHolder.action_deny.setVisibility(View.GONE);
        cardHolder.action_investigate.setVisibility(View.GONE);
        cardHolder.action_need_review.setVisibility(View.GONE);
        //cardHolder.action_divider.setVisibility(View.GONE);
        switch (status) {
            case 0:
                    /* PROCESSING */
                //cardHolder.action_divider.setVisibility(View.VISIBLE);
                cardHolder.action_edit.setVisibility(View.VISIBLE);
                cardHolder.action_delete.setVisibility(View.VISIBLE);
                break;
            case 1:
                    /* LISTED */
                //cardHolder.action_divider.setVisibility(View.VISIBLE);
                cardHolder.action_edit.setVisibility(View.VISIBLE);
                cardHolder.action_delete.setVisibility(View.VISIBLE);
                break;
            case 2:
                    /* SOLD */
                break;
            case 3:
                    /* SOLD  */
                break;
            case 4:
                    /*  DENIED  */
                //cardHolder.action_divider.setVisibility(View.VISIBLE);
                cardHolder.action_deny.setVisibility(View.VISIBLE);
                break;
            case 5:
                    /*  DELETED */
                break;
            case 6:
                    /*  Pending Shipment*/
                break;
            case 7:
                    /*  Under Investigation*/
                break;
            case 8:
                    /*  Investigate */
                //cardHolder.action_divider.setVisibility(View.VISIBLE);
                cardHolder.action_investigate.setVisibility(View.VISIBLE);
                break;
            case 9:
                    /*  Need Review */
                //cardHolder.action_divider.setVisibility(View.VISIBLE);
                cardHolder.action_edit.setVisibility(View.VISIBLE);
                cardHolder.action_delete.setVisibility(View.VISIBLE);
                cardHolder.action_need_review.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return cardInfoList == null ? 0 : cardInfoList.size();
    }

    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public void add(List<Object> items) {
        int previousDataSize = this.cardInfoList.size();
        this.cardInfoList.addAll(items);
        notifyItemRangeInserted(previousDataSize, items.size());
    }
    public void removeAllItem() {
        cardInfoList.clear();
        notifyDataSetChanged();
    }
}
