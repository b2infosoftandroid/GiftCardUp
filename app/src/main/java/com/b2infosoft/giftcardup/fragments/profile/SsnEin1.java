package com.b2infosoft.giftcardup.fragments.profile;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.activity.AddIdentity;
import com.b2infosoft.giftcardup.activity.AddSSN;
import com.b2infosoft.giftcardup.activity.MyProfile;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.model.Approve;
import com.b2infosoft.giftcardup.urlconnection.MultipartUtility;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ru.noties.scrollable.CanScrollVerticallyDelegate;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SsnEin1.OnFragmentSsnEin} interface
 * to handle interaction events.
 * Use the {@link SsnEin1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SsnEin1 extends Fragment implements CanScrollVerticallyDelegate, DMRResult {

    private final String TAG = SsnEin1.class.getName();
    private Active active;
    private Tags tags;
    private Urls urls;
    private DMRRequest dmrRequest;

    //Image request code
    private final int PICK_IMAGE_REQUEST = 1;
    //Uri to store the image uri
    private Uri filePath;
    private Bitmap bitmap;

    View mView;

    Button approve_for_selling, identification_button, next_btn, save, cancel;
    Button verification_email, verification_identity, verification_ssn;
    TextView verification_email_status, verification_identity_status, verification_ssn_status;

    ImageView identification_image;
    RadioButton idTypeSSN, idTypeEIN;
    EditText ssn_ein, name, routing_no, account_no;
    ScrollView scroll_view_step_one, step_two;
    View approve_for_selling_layout, approve_status_layout;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentSsnEin mListener;
    private HashMap<Integer, String> statusName = new HashMap<>();
    private HashMap<Integer, Integer> statusColor = new HashMap<>();

    public SsnEin1() {
        // Required empty public constructor
    }

    private void init() {
        active = Active.getInstance(getActivity());
        tags = Tags.getInstance();
        urls = Urls.getInstance();
        dmrRequest = DMRRequest.getInstance(getActivity(), TAG);
        statusName.put(0, "PENDING");
        statusName.put(1, "APPROVE");
        statusName.put(2, "REJECTED");
        statusName.put(3, "EXPIRED");
        statusName.put(4, "SUSPEND");
        statusName.put(9, "NOT SUBMITTED");

        statusColor.put(0, getResources().getColor(R.color.verification_pending));
        statusColor.put(1, getResources().getColor(R.color.verification_approve));
        statusColor.put(2, getResources().getColor(R.color.verification_rejected));
        statusColor.put(3, getResources().getColor(R.color.verification_expired));
        statusColor.put(4, getResources().getColor(R.color.verification_suspend));
        statusColor.put(9, getResources().getColor(R.color.verification_not_submitted));
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SsnEin.
     */
    public static SsnEin1 newInstance(String param1, String param2) {
        SsnEin1 fragment = new SsnEin1();
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
        init();
        mView = inflater.inflate(R.layout.fragment_ssn_ein_1, container, false);
        scroll_view_step_one = (ScrollView) mView.findViewById(R.id.scroll_view_step_one);
        approve_for_selling_layout = mView.findViewById(R.id.approve_for_selling_layout);
        approve_status_layout = mView.findViewById(R.id.approve_status_layout);
        approve_for_selling = (Button) mView.findViewById(R.id.ssn_ein_approved_for_sell);
        approve_for_selling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approve_for_selling.setVisibility(View.GONE);
                approve_for_selling_layout.setVisibility(View.VISIBLE);
            }
        });
        identification_button = (Button) mView.findViewById(R.id.ssn_ein_identification_button);
        identification_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        identification_image = (ImageView) mView.findViewById(R.id.ssn_ein_identification_image);
        ssn_ein = (EditText) mView.findViewById(R.id.ssn_ein_no);
        idTypeSSN = (RadioButton) mView.findViewById(R.id.id_type_ssn);
        idTypeEIN = (RadioButton) mView.findViewById(R.id.id_type_ein);
        save = (Button) mView.findViewById(R.id.bank_save_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ssnName = ssn_ein.getText().toString();
                String idType = "";
                if (idTypeEIN.isSelected()) {
                    idType = "SSN";
                } else {
                    idType = "EIN";
                }
                if (bitmap == null) {
                    Toast.makeText(getActivity(), "Please Select Identification Image", Toast.LENGTH_SHORT).show();
                }
                new AddInformation(bitmap, ssnName, idType).execute();
            }
        });
        cancel = (Button) mView.findViewById(R.id.bank_cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approve_for_selling.setVisibility(View.VISIBLE);
                approve_for_selling_layout.setVisibility(View.GONE);
            }
        });
        getApproveForSelling();

        verification_email = (Button) mView.findViewById(R.id.verified_email);
        verification_identity = (Button) mView.findViewById(R.id.verified_identity);
        verification_identity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddIdentity.class));
            }
        });

        verification_ssn = (Button) mView.findViewById(R.id.verified_ssn);
        verification_ssn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddSSN.class));
            }
        });

        verification_email_status = (TextView) mView.findViewById(R.id.verified_email_status);
        verification_identity_status = (TextView) mView.findViewById(R.id.verified_identity_status);
        verification_ssn_status = (TextView) mView.findViewById(R.id.verified_ssn_status);
        checkVerifiedStatus();
        return mView;
    }

    private void updateVerifiedStatus(Approve approve) {
        verification_email_status.setText(statusName.get(approve.getEmail()));
        verification_email_status.setBackgroundColor(statusColor.get(approve.getEmail()));
        //       verification_email_status.setBackgroundColor(getResources().getColor());
        verification_identity_status.setText(statusName.get(approve.getIdentification()));
        verification_identity_status.setBackgroundColor(statusColor.get(approve.getIdentification()));
        verification_ssn_status.setText(statusName.get(approve.getSsn()));
        verification_ssn_status.setBackgroundColor(statusColor.get(approve.getSsn()));
        int identi = approve.getIdentification();
        if (identi != 0 && identi != 1) {
            verification_identity.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_publish_24dp, 0);
            verification_identity.setClickable(true);
        } else {
            verification_identity.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            verification_identity.setClickable(false);
        }
        int ssn = approve.getSsn();
        if (ssn != 0 && ssn != 1) {
            verification_ssn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_publish_24dp, 0);
            verification_ssn.setClickable(true);
        } else {
            verification_ssn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            verification_ssn.setClickable(false);
        }
    }

    private void checkVerifiedStatus() {
        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.USER_ALL_APPROVE_INFO);
        map.put(tags.USER_ID, active.getUser().getUserId() + "");
        dmrRequest.doPost(urls.getUserInfo(), map, this);
    }

    private void getApproveForSelling() {
        Map<String, String> map = new HashMap<>();
        map.put(tags.USER_ACTION, tags.CHECK_APPROVE_FOR_SELLING);
        map.put(tags.USER_ID, active.getUser().getUserId() + "");
        dmrRequest.doPost(urls.getUserInfo(), map, this);
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        try {
            if (jsonObject.has(tags.SUCCESS)) {
                if (jsonObject.getInt(tags.SUCCESS) == tags.PASS) {
                    if (jsonObject.has(tags.CHECK_APPROVE_FOR_SELLING)) {
                        if (jsonObject.getInt(tags.CHECK_APPROVE_FOR_SELLING) == tags.PASS) {
                            approve_for_selling.setVisibility(View.GONE);
                            approve_status_layout.setVisibility(View.VISIBLE);
                        } else if (jsonObject.getInt(tags.CHECK_APPROVE_FOR_SELLING) == tags.FAIL) {
                            approve_for_selling.setVisibility(View.VISIBLE);
                            approve_status_layout.setVisibility(View.GONE);
                        }
                    }
                    if (jsonObject.has(tags.USER_ALL_APPROVE_INFO)) {
                        updateVerifiedStatus(Approve.fromJSON(jsonObject.getJSONObject(tags.USER_ALL_APPROVE_INFO)));
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
        MyProfile.setSelectedTabIndex(2);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                if (identification_image != null)
                    identification_image.setImageBitmap(bitmap);
                else {
                    Toast.makeText(getContext(), "IMAGE NULL", Toast.LENGTH_SHORT).show();
                }
                approve_for_selling.setVisibility(View.GONE);
                approve_for_selling_layout.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //replaceFragment();
    }

    private class AddInformation extends AsyncTask<String, String, String> {
        private Bitmap image;
        private String ssnNumber;
        private String idType;

        public AddInformation(Bitmap image, String ssnNumber, String idType) {
            this.image = image;
            this.ssnNumber = ssnNumber;
            this.idType = idType;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = urls.getUserInfo();
            try {
                MultipartUtility multipart = new MultipartUtility(url);
                multipart.addHeaderField("User-Agent", "CodeJava");
                multipart.addHeaderField("Test-Header", "Header-Value");

                multipart.addFormField(tags.USER_ACTION, tags.ADD_IDENTIFICATION_INFO);
                multipart.addFormField(tags.USER_ID, active.getUser().getUserId() + "");
                multipart.addFormField(tags.SSN_EIN, ssnNumber);
                multipart.addFormField(tags.ID_TYPE, idType);

                multipart.addFilePartBitmap(tags.BANK_VOID_IMAGE, "identification.png", image);
                return multipart.finishString();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage() + "");
            }

            return "";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("OUTPUT", s);
            super.onPostExecute(s);

        }
    }
}
