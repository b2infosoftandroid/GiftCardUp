package com.b2infosoft.giftcardup.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.b2infosoft.giftcardup.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SpeedySell.OnFragmentSpeedyCell} interface
 * to handle interaction events.
 * Use the {@link SpeedySell#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpeedySell extends Fragment {

    private final String TAG = SpeedySell.class.getName();

    Button cancel,submit;
    TableLayout t1;
    TextView gift_cards,card_type,pin,serial_no,balance,earning,selling,action;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentSpeedyCell mListener;

    public SpeedySell() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SpeedySell.
     */
    // TODO: Rename and change types and number of parameters
    public static SpeedySell newInstance(String param1, String param2) {
        SpeedySell fragment = new SpeedySell();
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
        view =  inflater.inflate(R.layout.fragment_speedy_cell, container, false);
        t1 = (TableLayout)view.findViewById(R.id.speedy_cell_table);
        cancel = (Button)view.findViewById(R.id.speedy_cell_cancel);
        submit = (Button)view.findViewById(R.id.speedy_cell_submit);

        TableRow tr_head = new TableRow(getContext());
        tr_head.setBackgroundColor(getResources().getColor(R.color.button_background));
        gift_cards = new TextView(getContext());
        gift_cards.setText("GIFT CARD'S");
        gift_cards.setTextColor(getResources().getColor(R.color.button_foreground));
        gift_cards.setPadding(15, 30, 15, 30);
        gift_cards.setTypeface(null, Typeface.BOLD);
        tr_head.addView(gift_cards);

        card_type = new TextView(getContext());
        card_type.setText("CARD TYPE");
        card_type.setTextColor(getResources().getColor(R.color.button_foreground));
        card_type.setPadding(15, 30, 15, 30);
        card_type.setTypeface(null, Typeface.BOLD);
        tr_head.addView(card_type);

        serial_no = new TextView(getContext());
        serial_no.setText("SERIAL NUMBER");
        serial_no.setTextColor(getResources().getColor(R.color.button_foreground));
        serial_no.setPadding(15, 30, 15, 30);
        serial_no.setTypeface(null, Typeface.BOLD);
        tr_head.addView(serial_no);

        pin = new TextView(getContext());
        pin.setText("PIN");
        pin.setTextColor(getResources().getColor(R.color.button_foreground));
        pin.setPadding(15, 30, 15, 30);
        pin.setTypeface(null, Typeface.BOLD);
        tr_head.addView(pin);

        balance = new TextView(getContext());
        balance.setText("BALANCE");
        balance.setTextColor(getResources().getColor(R.color.button_foreground));
        balance.setPadding(15, 30, 15, 30);
        balance.setTypeface(null, Typeface.BOLD);
        tr_head.addView(balance);

        earning = new TextView(getContext());
        earning.setText("YOUR EARNING");
        earning.setTextColor(getResources().getColor(R.color.button_foreground));
        earning.setPadding(15, 30, 15, 30);
        earning.setTypeface(null, Typeface.BOLD);
        tr_head.addView(earning);

        selling = new TextView(getContext());
        selling.setText("SELLING %");
        selling.setTextColor(getResources().getColor(R.color.button_foreground));
        selling.setPadding(15, 30, 15, 30);
        selling.setTypeface(null, Typeface.BOLD);
        tr_head.addView(selling);

        action = new TextView(getContext());
        action.setText("ACTION");
        action.setTextColor(getResources().getColor(R.color.button_foreground));
        action.setPadding(15, 30, 15, 30);
        action.setTypeface(null, Typeface.BOLD);
        tr_head.addView(action);
        t1.addView(tr_head);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSpeedyCell(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentSpeedyCell) {
            mListener = (OnFragmentSpeedyCell) context;
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentSpeedyCell {
        // TODO: Update argument type and name
        void onSpeedyCell(Uri uri);
    }
}
