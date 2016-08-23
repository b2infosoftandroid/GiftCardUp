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

import com.b2infosoft.giftcardup.R;

import ru.noties.scrollable.CanScrollVerticallyDelegate;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BankInformation.OnFragmentBankInformation} interface
 * to handle interaction events.
 * Use the {@link BankInformation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BankInformation extends Fragment implements CanScrollVerticallyDelegate{

    private final String TAG = BankInformation.class.getName();

    Button add_account,save,cancel;
    EditText name,routing_no,account_no;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentBankInformation mListener;

    public BankInformation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BankInformation.
     */
    // TODO: Rename and change types and number of parameters
    public static BankInformation newInstance(String param1, String param2) {
        BankInformation fragment = new BankInformation();
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
        view = inflater.inflate(R.layout.fragment_bank_info, container, false);

        add_account = (Button)view.findViewById(R.id.bank_info_add_account);
        add_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewAccount();
            }
        });
        return view;
    }

    private void addNewAccount(){

        final Dialog dialog = new Dialog(getContext());
        dialog.setTitle("ADD BANK DETAIL");
        dialog.setContentView(R.layout.custom_dialog_add_account);

        name = (EditText)dialog.findViewById(R.id.bank_name);
        routing_no = (EditText)dialog.findViewById(R.id.bank_routing_no);
        account_no = (EditText)dialog.findViewById(R.id.bank_account_no);
        save = (Button)dialog.findViewById(R.id.bank_save_btn);
        cancel = (Button)dialog.findViewById(R.id.bank_cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog.show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onBankInformation(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentBankInformation) {
            mListener = (OnFragmentBankInformation) context;
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
        return false;
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
    public interface OnFragmentBankInformation {
        // TODO: Update argument type and name
        void onBankInformation(Uri uri);
    }
}
