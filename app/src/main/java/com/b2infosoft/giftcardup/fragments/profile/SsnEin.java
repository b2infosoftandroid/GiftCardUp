package com.b2infosoft.giftcardup.fragments.profile;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.b2infosoft.giftcardup.R;

import ru.noties.scrollable.CanScrollVerticallyDelegate;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SsnEin.OnFragmentSsnEin} interface
 * to handle interaction events.
 * Use the {@link SsnEin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SsnEin extends Fragment implements CanScrollVerticallyDelegate {

    private final String TAG = SsnEin.class.getName();
    View mView;
    Button b1,upload_image,next_btn,save,cancel;
    EditText ssn_ein,name,routing_no,account_no;
    ScrollView step_one,step_two;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentSsnEin mListener;

    public SsnEin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileSsnEin.
     */
    public static SsnEin newInstance(String param1, String param2) {
        SsnEin fragment = new SsnEin();
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

        mView = inflater.inflate(R.layout.fragment_ssn_ein, container, false);
        step_one = (ScrollView)mView.findViewById(R.id.scroll_view_step_one);
        ssn_ein = (EditText)mView.findViewById(R.id.ssn_ein_no);
        upload_image = (Button)mView.findViewById(R.id.ssn_ein_upload_identification_img);
        name = (EditText)mView.findViewById(R.id.bank_name);
        routing_no = (EditText)mView.findViewById(R.id.bank_routing_no);
        account_no = (EditText)mView.findViewById(R.id.bank_account_no);
        save = (Button)mView.findViewById(R.id.bank_save_btn);
        cancel = (Button)mView.findViewById(R.id.bank_cancel_btn);
        b1 = (Button) mView.findViewById(R.id.ssn_ein_approved_for_sell);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step_one.setVisibility(View.VISIBLE);
                b1.setVisibility(View.GONE);
                //getApproved();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return mView;
    }

    private void getApproved(){

        final Dialog dialog = new Dialog(getContext());
        dialog.setTitle("GET APPROVED FOR SELLING");
        dialog.setContentView(R.layout.custom_dialog_approved_for_sell);

        step_one = (ScrollView)dialog.findViewById(R.id.scroll_view_step_one);
        step_two = (ScrollView)dialog.findViewById(R.id.scroll_view_bank_info);
        ssn_ein = (EditText)dialog.findViewById(R.id.ssn_ein_no);
        upload_image = (Button)dialog.findViewById(R.id.ssn_ein_upload_identification_img);
        next_btn = (Button)dialog.findViewById(R.id.ssn_ein_next_button);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog.show();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSsnEin(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentSsnEin) {
            mListener = (OnFragmentSsnEin) context;
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

    @Override
    public boolean canScrollVertically(int direction) {
        return mView != null && mView.canScrollVertically(direction);
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
    public interface OnFragmentSsnEin {
        void onSsnEin(Uri uri);
    }
}
