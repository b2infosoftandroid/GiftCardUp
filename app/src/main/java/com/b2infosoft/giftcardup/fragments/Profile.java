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

import ru.noties.scrollable.CanScrollVerticallyDelegate;
import ru.noties.scrollable.OnScrollChangedListener;
import ru.noties.scrollable.ScrollableLayout;
public class Profile extends Fragment {

    private final String TAG = Profile.class.getName();
    View mView,header;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    CircularImageView circularImageView;
    TextView name,membership,saving,sold;
    ViewPagerAdapter adapter;

    ScrollableLayout mScrollableLayout;
    public Profile() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.fragment_profile, container, false);
        header = mView.findViewById(R.id.header);
        tabLayout = (TabLayout)mView.findViewById(R.id.tabs);

        viewPager = (ViewPager)mView.findViewById(R.id.view_pager);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setTabIcons();

        mScrollableLayout = (ScrollableLayout)mView.findViewById(R.id.scrollable_layout);
        mScrollableLayout.setDraggableView(tabLayout);
        mScrollableLayout.setCanScrollVerticallyDelegate(new CanScrollVerticallyDelegate() {
            @Override
            public boolean canScrollVertically(int direction) {
                return adapter.canScrollVertically(viewPager.getCurrentItem(), direction);
            }
        });
        mScrollableLayout.setOnScrollChangedListener(new OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int y, int oldY, int maxY) {
                // Sticky behavior
                final float tabsTranslationY;
                if (y < maxY) {
                    tabsTranslationY = .0F;
                } else {
                    tabsTranslationY = y - maxY;
                }
                tabLayout.setTranslationY(tabsTranslationY);
                header.setTranslationY(y / 2);
            }
        });
        /*
        membership = (TextView)view.findViewById(R.id.profile_member);
        name = (TextView)view.findViewById(R.id.profile_user_name);
        saving = (TextView)view.findViewById(R.id.total_saving);
        sold = (TextView)view.findViewById(R.id.total_sold);
        circularImageView = (CircularImageView)view.findViewById(R.id.profile_user_image);
        */
        return mView;
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
        tabone.setTextSize(12f);
        tabone.setTextColor(getResources().getColor(R.color.profile_text_view_text));
        tabone.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_check_icon,0,0);
        tabLayout.getTabAt(0).setCustomView(tabone);

        tabone = new TextView(getActivity());
        tabone.setText("Bank details");
        tabone.setAllCaps(true);
        tabone.setTextSize(12f);
        tabone.setTextColor(getResources().getColor(R.color.profile_text_view_text));
        tabone.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_check_icon,0,0);
        tabLayout.getTabAt(1).setCustomView(tabone);

        tabone = new TextView(getActivity());
        tabone.setText("SSN/EIN");
        tabone.setGravity(Gravity.CENTER);
        tabone.setAllCaps(true);
        tabone.setTextSize(12f);
        tabone.setTextColor(getResources().getColor(R.color.profile_text_view_text));
        tabone.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_check_icon,0,0);
        tabLayout.getTabAt(2).setCustomView(tabone);
    }
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
