package com.b2infosoft.giftcardup.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.activity.ChangeAddress;
import com.b2infosoft.giftcardup.app.Cart;
import com.b2infosoft.giftcardup.app.Config;
import com.b2infosoft.giftcardup.app.Format;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.custom.Progress;
import com.b2infosoft.giftcardup.database.DBHelper;
import com.b2infosoft.giftcardup.model.CartSummary;
import com.b2infosoft.giftcardup.model.ContactInformation;
import com.b2infosoft.giftcardup.model.GiftCard;
import com.b2infosoft.giftcardup.model.MailPrice;
import com.b2infosoft.giftcardup.model.OrderSummery;
import com.b2infosoft.giftcardup.model.User;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;
import com.b2infosoft.giftcardup.volly.LruBitmapCache;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckOutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Cart cart;
    private final String TAG = CheckOutAdapter.class.getName();
    private Urls urls;
    private Tags tags;
    private Active active;
    DMRRequest dmrRequest;
    private Progress progress;
    private Context context;
    private Format format;
    private List<Object> cardInfoList;
    private Config config;
    private DBHelper dbHelper;
    private final int VIEW_CART_ITEM = 0;
    private final int VIEW_CART_ADDRESS = 1;
    private final int VIEW_CART_ORDER_SUMMERY = 2;

    public CheckOutAdapter(Context context, List<Object> cardInfoList) {
        this.context = context;
        this.cardInfoList = cardInfoList;
        format = Format.getInstance();
        config = Config.getInstance();
        tags = Tags.getInstance();
        urls = Urls.getInstance();
        active = Active.getInstance(context);
        dmrRequest = DMRRequest.getInstance(context, TAG);
        progress = new Progress(context);
        cart = (Cart) context.getApplicationContext();
        dbHelper = new DBHelper(context);
    }

    public class CardHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView type;
        TextView value;
        TextView price;
        View physical, e_card;
        RadioButton first_class, priority_mail, express_mail;

        public CardHolder(View view) {
            super(view);
            physical = view.findViewById(R.id.view_e_card);
            e_card = view.findViewById(R.id.view_physical_card);
            image = (ImageView) view.findViewById(R.id.card_image);
            name = (TextView) view.findViewById(R.id.card_name);
            type = (TextView) view.findViewById(R.id.card_type);
            value = (TextView) view.findViewById(R.id.card_value);
            price = (TextView) view.findViewById(R.id.card_price);
            first_class = (RadioButton) view.findViewById(R.id.first_class);
            priority_mail = (RadioButton) view.findViewById(R.id.priority_mail);
            express_mail = (RadioButton) view.findViewById(R.id.express_mail);
        }
    }

    public class Address extends RecyclerView.ViewHolder {
        TextView edit;
        TextView name;
        TextView address;
        TextView phone;
        View shipping;

        public Address(View view) {
            super(view);
            edit = (TextView) view.findViewById(R.id.edit);
            name = (TextView) view.findViewById(R.id.name);
            address = (TextView) view.findViewById(R.id.address);
            phone = (TextView) view.findViewById(R.id.phone);
            shipping = view.findViewById(R.id.shipping);
        }
    }

    public class Order extends RecyclerView.ViewHolder {
        TextView price;
        TextView shipping;
        TextView discount;
        TextView balance;
        EditText promotion_code;
        Button apply;

        public Order(View view) {
            super(view);
            price = (TextView) view.findViewById(R.id.price);
            shipping = (TextView) view.findViewById(R.id.shipping);
            discount = (TextView) view.findViewById(R.id.discount);
            balance = (TextView) view.findViewById(R.id.balance);
            promotion_code = (EditText) view.findViewById(R.id.promotion_code);
            apply = (Button) view.findViewById(R.id.apply);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_CART_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cart_checkout_item_1, parent, false);
            return new CardHolder(view);
        } else if (viewType == VIEW_CART_ADDRESS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cart_item_address, parent, false);
            return new Address(view);
        } else if (viewType == VIEW_CART_ORDER_SUMMERY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cart_item_summery_order, parent, false);
            return new Order(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        Object o = cardInfoList.get(position);
        return o instanceof ContactInformation ? VIEW_CART_ADDRESS : o instanceof GiftCard ? VIEW_CART_ITEM : VIEW_CART_ORDER_SUMMERY;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CardHolder) {
            final GiftCard card = (GiftCard) cardInfoList.get(position);
            final CardHolder cardHolder = (CardHolder) holder;
            LruBitmapCache.loadCacheImage(context, cardHolder.image, config.getGiftCardImageAddress().concat(card.getCardImage()), "");
            cardHolder.type.setText(card.getCardType() == 2 ? "E-CARD" : "PHYSICAL");
            cardHolder.e_card.setVisibility(card.getCardType() == 2 ? View.GONE : View.VISIBLE);
            cardHolder.physical.setVisibility(card.getCardType() == 2 ? View.VISIBLE : View.GONE);

            cardHolder.name.setText(card.getCardName());
            cardHolder.value.setText("$" + card.getCardPrice());
            cardHolder.price.setText("$" + card.getCardValue());
            MailPrice price = dbHelper.getMailPrice();
            cardHolder.first_class.setText("First Class [$" + String.valueOf(price.getFirstClass()) + "]");
            cardHolder.priority_mail.setText("First Class [$" + String.valueOf(price.getPriorityMail()) + "]");
            cardHolder.express_mail.setText("First Class [$" + String.valueOf(price.getExpressMail()) + "]");

        } else if (holder instanceof Address) {
            final ContactInformation contactInformation = (ContactInformation) cardInfoList.get(position);
            final Address cardHolder = (Address) holder;
            User user = active.getUser();

            cardHolder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, ChangeAddress.class));
                }
            });
            cardHolder.name.setText(user.getFirstName() + " " + user.getLastName());
            cardHolder.address.setText(contactInformation.getAddressFull(context));
            cardHolder.phone.setText(contactInformation.getPhoneNumber());
            if (isPhysicalFound()) {
                if (cardHolder.shipping.getVisibility() == View.GONE) {
                    cardHolder.shipping.setVisibility(View.VISIBLE);
                }
            } else {
                if (cardHolder.shipping.getVisibility() == View.VISIBLE) {
                    cardHolder.shipping.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return cardInfoList == null ? 0 : cardInfoList.size();
    }

    private void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private void isLastCard() {
        if (cardInfoList.size() == 1) {
            cardInfoList.clear();
        }
        CheckOutAdapter.super.notifyDataSetChanged();
    }

    public boolean isPhysicalFound() {
        for (Object o : cardInfoList) {
            if (o instanceof GiftCard) {
                GiftCard card = (GiftCard) o;
                if (card.getCardType() != 2) {
                    return true;
                }
            }
        }
        return false;
    }
}
