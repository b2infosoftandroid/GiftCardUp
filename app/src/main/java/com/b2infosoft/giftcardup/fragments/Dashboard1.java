package com.b2infosoft.giftcardup.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.activity.QuickAction;
import com.b2infosoft.giftcardup.model.QuickActionItem;
import com.b2infosoft.library.SwipeFlingAdapterView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Dashboard1.OnFragmentDashboard1} interface
 * to handle interaction events.
 * Use the {@link Dashboard1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Dashboard1 extends Fragment {

    private ArrayList<String> list;
    private static ArrayAdapter adapter;
    private SwipeFlingAdapterView flingAdapterView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentDashboard1 mListener;

    public Dashboard1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Dashboard1.
     */
    // TODO: Rename and change types and number of parameters
    public static Dashboard1 newInstance(String param1, String param2) {
        Dashboard1 fragment = new Dashboard1();
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
        view = inflater.inflate(R.layout.fragment_dashboard1, container, false);

        flingAdapterView = (SwipeFlingAdapterView)view.findViewById(R.id.frame);
        list = new ArrayList<>();
        list.add("physics");
        list.add("chemistry");
        list.add("science");
        list.add("maths");
        list.add("biology");
        list.add("english");

        adapter = new ArrayAdapter(getContext(),R.layout.fragment_dashboard1_item,R.id.text_hello,list);
        flingAdapterView.setAdapter(adapter);

        flingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                list.remove(0);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {

            }

            @Override
            public void onRightCardExit(Object dataObject) {

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onDashboard1(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentDashboard1) {
            mListener = (OnFragmentDashboard1) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentDashboard1");
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
    public interface OnFragmentDashboard1 {
        // TODO: Update argument type and name
        void onDashboard1(Uri uri);
    }
}
