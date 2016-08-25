package com.b2infosoft.giftcardup.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.model.GetOffer;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SellCards.OnFragmentSellCards} interface
 * to handle interaction events.
 * Use the {@link SellCards#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellCards extends Fragment implements DMRResult {
    private final String TAG = SellCards.class.getName();
    Tags tags;
    Urls urls;
    DMRRequest dmrRequest;
    EditText merchant, value;
    Button get_offer, accept_offer;
    TableLayout tableLayout;
    LinearLayout linearLayout;
    TextView name, payout, action;
    ImageView imageAction;
    Queue<GetOffer> offerQueue = new LinkedList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentSellCards mListener;

    public SellCards() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellCards.
     */
    // TODO: Rename and change types and number of parameters
    public static SellCards newInstance(String param1, String param2) {
        SellCards fragment = new SellCards();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        urls = Urls.getInstance();
        tags = Tags.getInstance();
        dmrRequest = DMRRequest.getInstance(getContext(), TAG);
        View view = inflater.inflate(R.layout.fragment_sell_cards, container, false);
        merchant = (EditText) view.findViewById(R.id.sell_gift_card_merchant);
        value = (EditText) view.findViewById(R.id.sell_gift_card_value);
        tableLayout = (TableLayout) view.findViewById(R.id.sell_gift_card_detail_table);
        linearLayout = (LinearLayout) view.findViewById(R.id.sell_cards_relative_layout);
        get_offer = (Button) view.findViewById(R.id.sell_gift_card_btn);
        accept_offer = (Button) view.findViewById(R.id.sell_gift_card_accept_btn);
        addHeader();
        get_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBlank();
            }
        });
        accept_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new SpeedySell());
            }
        });
        return view;
    }

    private void checkBlank() {
        String sell_merchant = merchant.getText().toString();
        String sell_value = value.getText().toString();
        merchant.setError(null);
        value.setError(null);
        if (merchant.length() == 0) {
            merchant.setError("Please Enter Merchant Name");
            merchant.requestFocus();
            return;
        }
        if (value.length() == 0) {
            value.setError("Please Enter Value");
            value.requestFocus();
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.GET_OFFER);
        map.put(tags.COMPANY_ID, sell_merchant);
        map.put(tags.SELL_GIFT_CARD_BALANCE, sell_value);
        dmrRequest.doPost(urls.getUrlCardsAll(), map, this);

    }

    private void addHeader() {
        if(tableLayout.getChildCount()>0){
            tableLayout.removeAllViews();
        }
        TableRow tr_head = new TableRow(getContext());
        tr_head.setBackgroundColor(getResources().getColor(R.color.button_background));
        name = new TextView(getContext());
        name.setText("NAME");
        name.setTextColor(getResources().getColor(R.color.button_foreground));
        name.setPadding(15, 30, 0, 30);
        name.setTypeface(null, Typeface.BOLD);
        tr_head.addView(name);

        payout = new TextView(getContext());
        payout.setText("PAYOUT");
        payout.setTextColor(getResources().getColor(R.color.button_foreground));
        payout.setPadding(15, 30, 0, 30);
        payout.setTypeface(null, Typeface.BOLD);
        tr_head.addView(payout);

        action = new TextView(getContext());
        action.setText("ACTION");
        action.setTextColor(getResources().getColor(R.color.button_foreground));
        action.setPadding(15, 30, 0, 30);
        action.setTypeface(null, Typeface.BOLD);
        tr_head.addView(action);
        tableLayout.addView(tr_head);
    }
    private void checkAllOffer() {
        if (offerQueue.size() > 0) {
            linearLayout.setVisibility(View.VISIBLE);
            addHeader();
            int total=0;
            for(GetOffer offer:offerQueue){
                addDetails(offer);
                total+=offer.getCardOffer();
            }
            addLastRow(total);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
    }
    private void addDetails(final GetOffer offer) {
        TableRow tr1 = new TableRow(getContext());
        tr1.setBackgroundColor(getResources().getColor(R.color.profile_text));
        name = new TextView(getContext());
        name.setText(offer.getCardName());
        name.setTextColor(getResources().getColor(R.color.button_foreground));
        name.setPadding(15, 30, 0, 30);
        name.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT, 1f));
        tr1.addView(name);

        payout = new TextView(getContext());
        payout.setText("Up To $" + offer.getCardOffer());
        payout.setTextColor(getResources().getColor(R.color.button_foreground));
        payout.setPadding(15, 30, 0, 30);
        payout.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT, 1f));
        tr1.addView(payout);

        imageAction = new ImageView(getContext());
        //imageAction.setText("");
        //imageAction.setTextColor(getResources().getColor(R.color.button_foreground));
        imageAction.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_delete_24dp));
        imageAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offerQueue.remove(offer);
                checkAllOffer();
            }
        });
        imageAction.setPadding(15, 30, 0, 30);
        imageAction.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT, 1f));
        tr1.addView(imageAction);

        tableLayout.addView(tr1);
    }

    private void addLastRow(int total) {
        TableRow trLast = new TableRow(getContext());
        trLast.setBackgroundColor(getResources().getColor(R.color.button_background));
        name = new TextView(getContext());
        name.setText("TOTAL");
        name.setTextColor(getResources().getColor(R.color.button_foreground));
        name.setPadding(15, 30, 0, 30);
        name.setTypeface(null, Typeface.BOLD);
        trLast.addView(name);

        payout = new TextView(getContext());
        payout.setText("Up To $"+total);
        payout.setTextColor(getResources().getColor(R.color.button_foreground));
        payout.setPadding(15, 30, 0, 30);
        payout.setTypeface(null, Typeface.BOLD);
        trLast.addView(payout);
        tableLayout.addView(trLast);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSellCards(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentSellCards) {
            mListener = (OnFragmentSellCards) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        Log.d(TAG, jsonObject.toString());
        try {
            if (jsonObject.has(tags.SUCCESS)) {
                if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                    if (jsonObject.has(tags.GET_OFFER)) {
                        GetOffer getOffer = new GetOffer();
                        GetOffer offer = getOffer.fromJSON(jsonObject.getJSONObject(tags.GET_OFFER));
                        offerQueue.add(offer);
                        checkAllOffer();
                    }
                } else if (jsonObject.getInt(tags.SUCCESS) == tags.FAIL) {

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



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentSellCards {
        void onSellCards(Uri uri);
    }
}
