package com.b2infosoft.giftcardup.fragments.profile;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.activity.AddAccountInfo;
import com.b2infosoft.giftcardup.activity.MyProfile;
import com.b2infosoft.giftcardup.adapter.BankDetailRecyclerViewAdapter;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.fragments.Profile;
import com.b2infosoft.giftcardup.model.BankInfo;
import com.b2infosoft.giftcardup.model.Merchant;
import com.b2infosoft.giftcardup.urlconnection.MultipartUtility;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import ru.noties.scrollable.CanScrollVerticallyDelegate;

public class BankInformation extends Fragment implements CanScrollVerticallyDelegate {
    private final String TAG = BankInformation.class.getName();
    private Tags tags;
    private Active active;
    private Urls urls;
    DMRRequest dmrRequest;
    List<BankInfo> bankInfos;

    RecyclerView recyclerView;
    FloatingActionButton add_account;
    BankDetailRecyclerViewAdapter adapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentBankInformation mListener;

    public BankInformation() {
        // Required empty public constructor
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        urls = Urls.getInstance();
        tags = Tags.getInstance();
        active = Active.getInstance(getContext());
        dmrRequest = DMRRequest.getInstance(getContext(), TAG);
        View view = inflater.inflate(R.layout.fragment_bank_info, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        add_account = (FloatingActionButton)view.findViewById(R.id.floating_add_account_btn);
        add_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddAccountInfo.class));
            }
        });
        getBankDetails();
        return view;
    }

    private void getBankDetails(){
        final Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.BANK_ACCOUNT_INFO);
        map.put(tags.USER_ID, active.getUser().getUserId()+ "");
        dmrRequest.doPost(urls.getUserInfo(), map, new DMRResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                //Log.d("watch",jsonObject.toString());
                try {
                    if (jsonObject.has(tags.SUCCESS)) {
                        if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                            if (jsonObject.has(tags.BANK_ACCOUNT_INFO)) {
                                if(bankInfos == null){
                                    bankInfos = new ArrayList<>();
                                }
                                JSONArray jsonArray =  jsonObject.getJSONArray(tags.BANK_ACCOUNT_INFO);
                                for(int i = 0;i< jsonArray.length();i++) {
                                    BankInfo info = BankInfo.fromJSON(jsonArray.getJSONObject(i));
                                    bankInfos.add(info);
                                }
                                adapter = new BankDetailRecyclerViewAdapter(getContext(),bankInfos);
                                recyclerView.setAdapter(adapter);
                            }
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


    private void replaceFragment() {
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
                    + " must implement OnFragmentBankInformation");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return true;
    }

    public interface OnFragmentBankInformation {
        void onBankInformation(Uri uri);
    }

}
