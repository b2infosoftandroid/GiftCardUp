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
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.activity.MyProfile;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.urlconnection.MultipartUtility;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BankDetail.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BankDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BankDetail extends Fragment {
    private final String TAG = BankDetail.class.getName();
    private Tags tags;
    private Active active;
    private Urls urls;
    //Image request code
    private final int PICK_IMAGE_REQUEST = 1;
    //Uri to store the image uri
    private Uri filePath;
    private Bitmap bitmap;

    Button save, chooseImage;
    EditText name, routing_no, account_no, status;
    AppCompatImageView imageView;
    ImageView edit, less;
    LinearLayout linearLayout;
    int count = 0;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BankDetail() {
        // Required empty public constructor
    }

    private void init() {
        tags = Tags.getInstance();
        active = Active.getInstance(getContext());
        urls = Urls.getInstance();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BankDetail.
     */
    // TODO: Rename and change types and number of parameters
    public static BankDetail newInstance(String param1, String param2) {
        BankDetail fragment = new BankDetail();
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
        init();
        View view = inflater.inflate(R.layout.fragment_bank_detail, container, false);
        name = (EditText) view.findViewById(R.id.bank_name);
        routing_no = (EditText) view.findViewById(R.id.bank_routing_no);
        account_no = (EditText) view.findViewById(R.id.bank_account_no);
        status = (EditText) view.findViewById(R.id.bank_status);
        linearLayout = (LinearLayout) view.findViewById(R.id.layout_2);
        edit = (ImageView) view.findViewById(R.id.bank_info_edit);
        less = (ImageView) view.findViewById(R.id.bank_info_less);
        less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableInfo(false);
                save.setVisibility(View.GONE);
                count = count + 1;
                if (count % 2 != 0) {
                    linearLayout.setVisibility(View.GONE);
                    less.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_24dp));
                } else {
                    linearLayout.setVisibility(View.VISIBLE);
                    less.setImageDrawable(getResources().getDrawable(R.drawable.ic_subtract_24dp));
                }
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableInfo(true);
                save.setVisibility(View.VISIBLE);
            }
        });
        imageView = (AppCompatImageView) view.findViewById(R.id.void_check_image);
        chooseImage = (Button) view.findViewById(R.id.choose_void_image);
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        save = (Button) view.findViewById(R.id.bank_save_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddBankAccount().execute();
            }
        });
        return view;
    }

    private void enableInfo(boolean isUpdate) {
        name.setEnabled(isUpdate);
        routing_no.setEnabled(isUpdate);
        account_no.setEnabled(isUpdate);
        chooseImage.setEnabled(isUpdate);
        status.setEnabled(isUpdate);
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
        MyProfile.setSelectedTabIndex(1);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                if (imageView != null)
                    imageView.setImageBitmap(bitmap);
                else {
                    Toast.makeText(getContext(), "IMAGE NULL", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //replaceFragment();
    }

    private void uploadMultipart(Bitmap bitmap) {
        String uploadId = UUID.randomUUID().toString();
        String path = getPath(filePath);
        String url = urls.getUserInfo();
        try {

            MultipartUtility multipart = new MultipartUtility(url);
            multipart.addHeaderField("User-Agent", "CodeJava");
            multipart.addHeaderField("Test-Header", "Header-Value");

            multipart.addFormField(tags.USER_ACTION, tags.BANK_ACCOUNT_ADD);
            multipart.addFormField(tags.BANK_NAME, "RAJESH");
            multipart.addFormField(tags.USER_ID, active.getUser().getUserId() + "");
            multipart.addFormField(tags.BANK_ACCOUNT_NUMBER, "123456");
            multipart.addFormField(tags.BANK_ROUTING_NUMBER, "654321");
            multipart.addFilePartBitmap(tags.BANK_VOID_IMAGE, "bank_void_image.png", bitmap);
            Log.d("OUTPUT", "" + multipart.finishString());

            MultipartUploadRequest uploadRequest = new MultipartUploadRequest(getActivity(), uploadId, url)
                    .addFileToUpload(path, tags.BANK_VOID_IMAGE)
                    .addParameter(tags.USER_ACTION, tags.BANK_ACCOUNT_ADD)
                    .addParameter(tags.BANK_NAME, "RAJESH")
                    .addParameter(tags.USER_ID, active.getUser().getUserId() + "")
                    .addParameter(tags.BANK_ACCOUNT_NUMBER, "123456")
                    .addParameter(tags.BANK_ROUTING_NUMBER, "654321")
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setMethod("POST");
            String str = uploadRequest.startUpload();
            Log.d(TAG, str);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage() + "");
        }

    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private class AddBankAccount extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            uploadMultipart(bitmap);
            return null;
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
