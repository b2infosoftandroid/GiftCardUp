package com.b2infosoft.giftcardup.adapter;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.support.v7.widget.CardView;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.activity.CompanyCard;
import com.b2infosoft.giftcardup.activity.Login;
import com.b2infosoft.giftcardup.activity.Main;
import com.b2infosoft.giftcardup.activity.ShoppingCart;
import com.b2infosoft.giftcardup.app.Cart;
import com.b2infosoft.giftcardup.app.Config;
import com.b2infosoft.giftcardup.app.Format;
import com.b2infosoft.giftcardup.app.Notify;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.app.Validation;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.custom.Progress;
import com.b2infosoft.giftcardup.listener.OnLoadMoreListener;
import com.b2infosoft.giftcardup.model.CompanyBrand;
import com.b2infosoft.giftcardup.model.GiftCard;
import com.b2infosoft.giftcardup.services.CartStatus;
import com.b2infosoft.giftcardup.services.MyServices;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;
import com.b2infosoft.giftcardup.volly.LruBitmapCache;
import com.b2infosoft.giftcardup.volly.MySingleton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = CompanyCardAdapter.class.getName();
    private Urls urls;
    private Tags tags;
    private Active active;
    private Format format;
    DMRRequest dmrRequest;
    private Progress progress;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener mOnLoadMoreListener;
    private Context context;
    private List<GiftCard> cardInfoList;
    private Config config;
    private CompanyBrand companyBrand;

    public CompanyCardAdapter(Context context, List<GiftCard> cardInfoList, RecyclerView recyclerView, CompanyBrand companyBrand) {
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
        dmrRequest = DMRRequest.getInstance(context, TAG);
        urls = Urls.getInstance();
        tags = Tags.getInstance();
        active = Active.getInstance(context);
        format = Format.getInstance();
        progress = new Progress(context);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public class CardHolder extends RecyclerView.ViewHolder {
        ImageView cardType;
        ImageView imageUrl;
        TextView cardValue,name;
        TextView cardOff,saving;
        TextView cardPrice;
        Button buyNow, info;
        Button add_to_cart, card_buy_now;
        CardView card1;
        LinearLayout linearLayout, layout2;
        int count = 0;

        public CardHolder(View view) {
            super(view);
            cardType = (ImageView) view.findViewById(R.id.card_type);
            name = (TextView) view.findViewById(R.id.card_item_company_name);
            cardValue = (TextView) view.findViewById(R.id.company_card_card_value);
            cardOff = (TextView) view.findViewById(R.id.company_card_card_off);
            saving = (TextView) view.findViewById(R.id.card_item_saving);
            cardPrice = (TextView) view.findViewById(R.id.company_card_card_price);
            imageUrl = (ImageView) view.findViewById(R.id.company_card_image);
            buyNow = (Button) view.findViewById(R.id.company_card_card_buy_now);
            add_to_cart = (Button) view.findViewById(R.id.add_to_cart);
            card_buy_now = (Button) view.findViewById(R.id.card_buy_now);
            card1 = (CardView) view.findViewById(R.id.card_view1);
            layout2 = (LinearLayout) view.findViewById(R.id.card_view2);
            card1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layout2.setVisibility(layout2.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                }
            });
            linearLayout = (LinearLayout) view.findViewById(R.id.buy_card_linear_layout);
            info = (Button) view.findViewById(R.id.buy_card_info_btn);
            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    linearLayout.setVisibility(linearLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
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
            final GiftCard giftCard = cardInfoList.get(position);
            final CardHolder cardHolder = (CardHolder) holder;
            if (companyBrand.getCardType() == 2) {
                cardHolder.cardType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_check_24dp));
            } else {
                cardHolder.cardType.setImageDrawable(null);
            }
            cardHolder.name.setText(giftCard.getCardName());
            cardHolder.cardOff.setText(String.valueOf(giftCard.getPercentageOff()) + "%");
            cardHolder.saving.setText(String.valueOf(giftCard.getPercentageOff()) + "%");
            cardHolder.cardValue.setText("$" + String.valueOf(giftCard.getCardPrice()));
            cardHolder.cardPrice.setText("$" + String.valueOf(giftCard.getCardValue()));
            cardHolder.add_to_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Cart cart = (Cart) context.getApplicationContext();
                    String cardAction = cardHolder.add_to_cart.getText().toString();
                    if(!active.isLogin()){
                        context.startActivity(new Intent(context, Login.class));
                        return;
                    }
                    if (cardAction.equalsIgnoreCase("Add to cart")) {
                        final Map<String, String> map = new HashMap<>();
                        map.put(tags.USER_ACTION, tags.ADD_CART_ITEM_GIFT_CARD);
                        map.put(tags.USER_ID, active.getUser().getUserId());
                        map.put(tags.GIFT_CARD_GIFT_CARD_ID, giftCard.getGiftCardID() + "");
                        dmrRequest.doPost(urls.getCartInfo(), map, new DMRResult() {
                            @Override
                            public void onSuccess(JSONObject jsonObject) {
                                try {
                                    if (jsonObject.has(tags.SUCCESS)) {
                                        if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                                            JSONArray array = jsonObject.getJSONArray(tags.GIFT_CARDS);
                                            cart.removeAll();
                                            for (int i = 0; i < array.length(); i++) {
                                                GiftCard giftCard1 = GiftCard.fromJSON(array.getJSONObject(i));
                                                cart.addCartItem(giftCard);
                                            }
                                            showMessage("Successfully Added to Cart");
                                            cardHolder.add_to_cart.setText("Remove to cart");
                                            ((CompanyCard) context).invalidateOptionsMenu();
                                            MyServices.startLeftCartTimeService(context);
                                        } else if (jsonObject.getInt(tags.SUCCESS) == tags.SUSPEND) {
                                            showMessage("You have to attempt more three times. So you can add item in cart after three hours.");
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
                            }
                        });
                    } else if (cardAction.equalsIgnoreCase("remove to cart")) {
                        final Map<String, String> map = new HashMap<>();
                        map.put(tags.USER_ACTION, tags.REMOVE_CART_ITEM_GIFT_CARD);
                        map.put(tags.USER_ID, active.getUser().getUserId());
                        map.put(tags.GIFT_CARD_GIFT_CARD_ID, giftCard.getGiftCardID() + "");
                        dmrRequest.doPost(urls.getCartInfo(), map, new DMRResult() {
                            @Override
                            public void onSuccess(JSONObject jsonObject) {
                                try {
                                    if (jsonObject.has(tags.SUCCESS)) {
                                        if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                                            JSONArray array = jsonObject.getJSONArray(tags.GIFT_CARDS);
                                            cart.removeAll();
                                            for (int i = 0; i < array.length(); i++) {
                                                cart.addCartItem(GiftCard.fromJSON(array.getJSONObject(i)));
                                            }
                                            cardHolder.add_to_cart.setText("Add to cart");
                                            showMessage("Successfully remove to Cart ");
                                            ((Activity) context).invalidateOptionsMenu();
                                            MyServices.startLeftCartTimeService(context);
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
                            }
                        });
                    }
                }
            });
            cardHolder.card_buy_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Cart cart = (Cart) context.getApplicationContext();
                    if(!active.isLogin()){
                        context.startActivity(new Intent(context, Login.class));
                        return;
                    }
                        final Map<String, String> map = new HashMap<>();
                        map.put(tags.USER_ACTION, tags.ADD_CART_ITEM_GIFT_CARD);
                        map.put(tags.USER_ID, active.getUser().getUserId());
                        map.put(tags.GIFT_CARD_GIFT_CARD_ID, giftCard.getGiftCardID() + "");
                        dmrRequest.doPost(urls.getCartInfo(), map, new DMRResult() {
                            @Override
                            public void onSuccess(JSONObject jsonObject) {
                                try {
                                    if (jsonObject.has(tags.SUCCESS)) {
                                        if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                                            JSONArray array = jsonObject.getJSONArray(tags.GIFT_CARDS);
                                            cart.removeAll();
                                            for (int i = 0; i < array.length(); i++) {
                                                GiftCard giftCard1 = GiftCard.fromJSON(array.getJSONObject(i));
                                                cart.addCartItem(giftCard);
                                            }
                                            showMessage("Successfully Added to Cart");
                                            ((CompanyCard) context).invalidateOptionsMenu();
                                            MyServices.startLeftCartTimeService(context);
                                            context.startActivity(new Intent(context, ShoppingCart.class));
                                        } else if (jsonObject.getInt(tags.SUCCESS) == tags.SUSPEND) {
                                            showMessage("You have to attempt more three times. So you can add item in cart after three hours.");
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
                            }
                        });
                    }
            });
            //count++;
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

    private void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
