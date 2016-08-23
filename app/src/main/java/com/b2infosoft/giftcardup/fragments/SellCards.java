package com.b2infosoft.giftcardup.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

import com.b2infosoft.giftcardup.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SellCards.OnFragmentSellCards} interface
 * to handle interaction events.
 * Use the {@link SellCards#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellCards extends Fragment {

    private final String TAG = SellCards.class.getName();

    EditText merchant,value;
    Button get_offer;
    TableLayout tableLayout;
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
        View view = null;
        view =  inflater.inflate(R.layout.fragment_sell_cards, container, false);

        merchant = (EditText)view.findViewById(R.id.sell_gift_card_merchant);
        value = (EditText)view.findViewById(R.id.sell_gift_card_value);
        tableLayout = (TableLayout)view.findViewById(R.id.sell_gift_card_detail_table);
        get_offer = (Button)view.findViewById(R.id.sell_gift_card_btn);
        get_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDetails();
            }
        });
        return view;
    }

    private static void addDetails(){


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
        // TODO: Update argument type and name
        void onSellCards(Uri uri);
    }
}
