package com.b2infosoft.giftcardup.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.activity.ChangeAddress;
import com.b2infosoft.giftcardup.activity.Payments;
import com.b2infosoft.giftcardup.app.Cart;
import com.b2infosoft.giftcardup.app.Config;
import com.b2infosoft.giftcardup.app.Format;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.custom.AlertBox;
import com.b2infosoft.giftcardup.custom.Progress;
import com.b2infosoft.giftcardup.database.DBHelper;
import com.b2infosoft.giftcardup.model.ContactInformation;
import com.b2infosoft.giftcardup.model.ControlPanel;
import com.b2infosoft.giftcardup.model.GiftCard;
import com.b2infosoft.giftcardup.model.OrderSummery;
import com.b2infosoft.giftcardup.model.User;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;
import com.b2infosoft.giftcardup.volly.LruBitmapCache;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckOutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
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
    OrderSummery orderSummery;
    private int shipping_view;
    private CheckBox shipping;
    private TextView error_shipping;
    private Button action_continue;

    public CheckOutAdapter(Context context, List<Object> cardInfoList, Button action_continue) {
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
        this.action_continue = action_continue;
        this.action_continue.setOnClickListener(this);
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
        View shipping_view;
        CheckBox shipping;
        TextView error_shipping;

        public Address(View view) {
            super(view);
            edit = (TextView) view.findViewById(R.id.edit);
            name = (TextView) view.findViewById(R.id.name);
            address = (TextView) view.findViewById(R.id.address);
            phone = (TextView) view.findViewById(R.id.phone);
            shipping = (CheckBox) view.findViewById(R.id.shipping);
            shipping_view = view.findViewById(R.id.shipping_view);
            error_shipping = (TextView) view.findViewById(R.id.error_shipping);
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
            final ControlPanel panel = dbHelper.getControlPanel();
            cardHolder.first_class.setText("First Class [$" + String.valueOf(panel.getFirstClassPrice()) + "]");
            cardHolder.priority_mail.setText("Priority Mail [$" + String.valueOf(panel.getPriorityPrice()) + "]");
            cardHolder.express_mail.setText("Express Mail [$" + String.valueOf(panel.getExpressPrice()) + "]");
            cardHolder.first_class.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (cardHolder.first_class.isChecked()) {
                        if (orderSummery != null)
                            orderSummery.setShippingMailType(card.getGiftCardID() + "", "First Class", Float.parseFloat(panel.getFirstClassPrice()));
                        CheckOutAdapter.super.notifyDataSetChanged();
                    }
                }
            });
            cardHolder.priority_mail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (cardHolder.priority_mail.isChecked()) {
                        if (orderSummery != null)
                            orderSummery.setShippingMailType(card.getGiftCardID() + "", "Priority Mail", Float.parseFloat(panel.getPriorityPrice()));
                        CheckOutAdapter.super.notifyDataSetChanged();
                    }
                }
            });
            cardHolder.express_mail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (cardHolder.express_mail.isChecked()) {
                        if (orderSummery != null)
                            orderSummery.setShippingMailType(card.getGiftCardID() + "", "Express Mail", Float.parseFloat(panel.getExpressPrice()));
                        CheckOutAdapter.super.notifyDataSetChanged();
                    }
                }
            });
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
            this.shipping = cardHolder.shipping;
            this.error_shipping = cardHolder.error_shipping;
            if (isPhysicalFound()) {
                if (cardHolder.shipping_view.getVisibility() == View.GONE) {
                    shipping_view = View.VISIBLE;
                    cardHolder.shipping_view.setVisibility(shipping_view);
                }
            } else {
                if (cardHolder.shipping_view.getVisibility() == View.VISIBLE) {
                    shipping_view = View.GONE;
                    cardHolder.shipping_view.setVisibility(shipping_view);
                }
            }
        } else if (holder instanceof Order) {
            orderSummery = (OrderSummery) cardInfoList.get(position);
            final Order order = (Order) holder;
            order.price.setText("$" + String.valueOf(orderSummery.getPrice()));
            order.shipping.setText("$" + String.valueOf(orderSummery.getShipping()));
            order.discount.setText("$" + String.valueOf(orderSummery.getDiscount()));
            order.balance.setText("$" + String.valueOf(orderSummery.getBalance()));
            order.apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String promo = order.promotion_code.getText().toString();
                    if (TextUtils.isEmpty(promo)) {
                        order.promotion_code.setError("Enter Promo code");
                        order.promotion_code.requestFocus();
                        return;
                    }
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(tags.USER_ACTION, tags.PROMO_CODE_APPLY);
                    map.put(tags.USER_ID, active.getUser().getUserId());
                    map.put(tags.PROMO_CODE, promo);
                    map.put(tags.TOTAL_AMOUNT, orderSummery.getBalance() + "");
                    dmrRequest.doPost(urls.getCartInfo(), map, new DMRResult() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            try {
                                if (jsonObject.has(tags.SUCCESS)) {
                                    if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                                        int total = 0;
                                        if (jsonObject.has(tags.TOTAL)) {
                                            total = jsonObject.getInt(tags.TOTAL);
                                        }
                                        if (jsonObject.has(tags.AMOUNT_TYPE)) {
                                            if (jsonObject.getInt(tags.AMOUNT_TYPE) == tags.PASS) {
                                            /* 1 MEAN PROMO DISCOUNT IN $ */
                                                orderSummery.setDiscountAmount(total);
                                            }
                                            if (jsonObject.getInt(tags.AMOUNT_TYPE) == tags.SUSPEND) {
                                            /* 2 MEAN PROMO DISCOUNT IN PERCENTAGE */
                                                orderSummery.setDiscountPercentage(total);
                                            }
                                        }
                                        CheckOutAdapter.super.notifyDataSetChanged();
                                        showMessage("Your Code Successfully Applied.");
                                        order.apply.setEnabled(false);
                                    }
                                    if (jsonObject.getInt(tags.SUCCESS) == tags.FAIL) {
                                        /* 0  mean promo code not match */
                                        AlertBox box = new AlertBox(context);
                                        box.setTitle("Alert");
                                        box.setMessage("Promotion code does not match. Please enter valid promotion code.");
                                        box.show();
                                    }
                                    if (jsonObject.getInt(tags.SUCCESS) == tags.SUSPEND) {
                                        /* 2 mean amount is less */
                                        AlertBox box = new AlertBox(context);
                                        box.setTitle("Alert");
                                        box.setMessage("Promotion code does not apply. Because your total amount is less than to promotion code amount.");
                                        box.show();
                                    }
                                    if (jsonObject.getInt(tags.SUCCESS) == tags.EXISTING_USER) {
                                        /* 3 mean User already used this promo code */
                                        AlertBox box = new AlertBox(context);
                                        box.setTitle("Alert");
                                        box.setMessage("Already User Promo Code");
                                        box.show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(TAG, e.getMessage());
                            }
                        }

                        @Override
                        public void onError(VolleyError volleyError) {

                        }
                    });
                }
            });
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

    @Override
    public void onClick(View v) {
        if (shipping_view == View.VISIBLE) {
            if (shipping.isChecked()) {
                error_shipping.setVisibility(View.GONE);
            } else {
                error_shipping.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Please select shipping.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Intent intent = new Intent(context, Payments.class);
        intent.putExtra(tags.ORDER_SUMMERY, orderSummery);
        context.startActivity(intent);
    }
}
