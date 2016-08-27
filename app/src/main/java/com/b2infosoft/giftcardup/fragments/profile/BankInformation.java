package com.b2infosoft.giftcardup.fragments.profile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.fragments.Profile;

import java.io.IOException;

import ru.noties.scrollable.CanScrollVerticallyDelegate;

public class BankInformation extends Fragment implements CanScrollVerticallyDelegate {
    private final String TAG = BankInformation.class.getName();
    private Tags tags;
    private Active active;

    //Image request code
    private final int PICK_IMAGE_REQUEST = 1;
    //Uri to store the image uri
    private Uri filePath;
    private Bitmap bitmap;

    Button add_account, save, cancel, chooseImage;
    EditText name, routing_no, account_no;
    AppCompatImageView imageView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentBankInformation mListener;

    public BankInformation() {
        // Required empty public constructor
    }

    private void init() {
        tags = Tags.getInstance();
        active = Active.getInstance(getContext());
    }

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
        View view = inflater.inflate(R.layout.fragment_bank_info, container, false);
        add_account = (Button) view.findViewById(R.id.bank_info_add_account);
        add_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewAccount();
            }
        });
        return view;
    }

    private void addNewAccount() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setTitle("ADD BANK DETAIL");
        dialog.setContentView(R.layout.custom_dialog_add_account);
        name = (EditText) dialog.findViewById(R.id.bank_name);
        routing_no = (EditText) dialog.findViewById(R.id.bank_routing_no);
        account_no = (EditText) dialog.findViewById(R.id.bank_account_no);
        imageView = (AppCompatImageView) dialog.findViewById(R.id.void_check_image);
        chooseImage = (Button) dialog.findViewById(R.id.choose_void_image);
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        save = (Button) dialog.findViewById(R.id.bank_save_btn);
        cancel = (Button) dialog.findViewById(R.id.bank_cancel_btn);
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

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                if (imageView != null)
                    imageView.setImageBitmap(bitmap);
                else{
                    Toast.makeText(getContext(),"IMAGE NULL",Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        replaceFragment();
    }
    private void replaceFragment(){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, new Profile());
        transaction.addToBackStack(null);
        transaction.commit();
    }
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

    public interface OnFragmentBankInformation {
        void onBankInformation(Uri uri);
    }
}
