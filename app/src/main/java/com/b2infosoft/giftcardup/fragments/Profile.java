package com.b2infosoft.giftcardup.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.adapter.ViewPagerAdapter;
import com.b2infosoft.giftcardup.fragments.profile.BankInformation;
import com.b2infosoft.giftcardup.fragments.profile.Identification;
import com.b2infosoft.giftcardup.fragments.profile.SsnEin;
import com.mikhaellopez.circularimageview.CircularImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Profile.OnFragmentProfile} interface
 * to handle interaction events.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {

    private final String TAG = Profile.class.getName();

    private TabLayout tabLayout;
    private ViewPager viewPager;
    CircularImageView circularImageView;
    TextView name,membership,saving,sold;
    ViewPagerAdapter adapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentProfile mListener;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
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
        view =  inflater.inflate(R.layout.fragment_profile, container, false);
        membership = (TextView)view.findViewById(R.id.profile_member);
        name = (TextView)view.findViewById(R.id.profile_user_name);
        saving = (TextView)view.findViewById(R.id.total_saving);
        sold = (TextView)view.findViewById(R.id.total_sold);
        circularImageView = (CircularImageView)view.findViewById(R.id.profile_user_image);

        viewPager = (ViewPager)view.findViewById(R.id.viewpager);
        setUpViewPager(viewPager);
        tabLayout = (TabLayout)view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setTabIcons();

        return view;
    }

    private void setUpViewPager(ViewPager pager){
        adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new Identification(),"identification");
        adapter.addFragment(new BankInformation(),"Bank details");
        adapter.addFragment(new SsnEin(),"SSN/EIN");
        pager.setAdapter(adapter);
    }

    private void setTabIcons(){
        TextView tabone = new TextView(getActivity());
        tabone.setText("identification");
        tabone.setGravity(Gravity.CENTER);
        tabone.setAllCaps(true);
        tabone.setTextColor(getResources().getColor(R.color.profile_text_view_text));
        tabone.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_check_icon,0,0);
        tabLayout.getTabAt(0).setCustomView(tabone);

        tabone = new TextView(getActivity());
        tabone.setText("Bank details");
        tabone.setAllCaps(true);
        tabone.setTextColor(getResources().getColor(R.color.profile_text_view_text));
        tabone.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_check_icon,0,0);
        tabLayout.getTabAt(1).setCustomView(tabone);

        tabone = new TextView(getActivity());
        tabone.setText("SSN/EIN");
        tabone.setGravity(Gravity.CENTER);
        tabone.setAllCaps(true);
        tabone.setTextColor(getResources().getColor(R.color.profile_text_view_text));
        tabone.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_check_icon,0,0);
        tabLayout.getTabAt(2).setCustomView(tabone);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onProfile(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentProfile) {
            mListener = (OnFragmentProfile) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentProfile");
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
    public interface OnFragmentProfile {
        // TODO: Update argument type and name
        void onProfile(Uri uri);
    }
}
