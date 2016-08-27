package com.b2infosoft.giftcardup.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.adapter.ViewPagerAdapter;
import com.b2infosoft.giftcardup.app.Config;
import com.b2infosoft.giftcardup.app.Format;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.fragments.profile.BankInformation;
import com.b2infosoft.giftcardup.fragments.profile.Identification;
import com.b2infosoft.giftcardup.fragments.profile.SsnEin;
import com.b2infosoft.giftcardup.model.User;
import com.b2infosoft.giftcardup.volly.LruBitmapCache;
import com.mikhaellopez.circularimageview.CircularImageView;

import ru.noties.scrollable.CanScrollVerticallyDelegate;
import ru.noties.scrollable.OnScrollChangedListener;
import ru.noties.scrollable.ScrollableLayout;

public class Profile extends Fragment {
    private final String TAG = Profile.class.getName();
    private Config config;
    private Active active;
    private Format format;
    View mView, header;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    CircularImageView circularImageView;
    TextView name, membership, saving, sold;
    ViewPagerAdapter adapter;

    ScrollableLayout mScrollableLayout;

    public Profile() {

    }

    private void init() {
        config = Config.getInstance();
        active = Active.getInstance(getActivity());
        format = Format.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        init();
        mView = inflater.inflate(R.layout.fragment_profile, container, false);
        header = mView.findViewById(R.id.header);
        tabLayout = (TabLayout) mView.findViewById(R.id.tabs);

        viewPager = (ViewPager) mView.findViewById(R.id.view_pager);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setTabIcons();

        mScrollableLayout = (ScrollableLayout) mView.findViewById(R.id.scrollable_layout);
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
        setInitialize(active.isLogin());
        return mView;
    }

    private void setInitialize(boolean isLogin) {
        membership = (TextView) mView.findViewById(R.id.profile_member);
        name = (TextView) mView.findViewById(R.id.profile_user_name);
        saving = (TextView) mView.findViewById(R.id.total_saving);
        sold = (TextView) mView.findViewById(R.id.total_sold);
        circularImageView = (CircularImageView) mView.findViewById(R.id.profile_user_image);
        if (isLogin) {
            User user = active.getUser();
            name.setText(user.getFirstName() + " " + user.getLastName());
            //name.setTypeface(Typeface.DEFAULT_BOLD);
            membership.setText("Member Since: ".concat(format.getDate(user.getJoinDate())));
            saving.setText(user.getTotalSave());
            sold.setText(user.getTotalSold());
            LruBitmapCache.loadCacheImage(getActivity(), circularImageView, config.getUserProfileImageAddress().concat(user.getImage()), TAG);
        }
    }

    private void setUpViewPager(ViewPager pager) {
        adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new Identification(), "Identification");
        adapter.addFragment(new BankInformation(), "Bank details");
        adapter.addFragment(new SsnEin(), "SSN/EIN");
        pager.setAdapter(adapter);
    }

    private void setTabIcons() {
        TextView tabOne = new TextView(getActivity());
        tabOne.setText("Identification");
        tabOne.setGravity(Gravity.CENTER);
        tabOne.setAllCaps(true);
        tabOne.setTextSize(10f);
        tabOne.setTextColor(getResources().getColor(R.color.profile_text_view_text));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_check_icon, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        tabOne = new TextView(getActivity());
        tabOne.setText("Bank details");
        tabOne.setGravity(Gravity.CENTER);
        tabOne.setAllCaps(true);
        tabOne.setTextSize(10f);
        tabOne.setTextColor(getResources().getColor(R.color.profile_text_view_text));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_check_icon, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabOne);

        tabOne = new TextView(getActivity());
        tabOne.setText("SSN/EIN");
        tabOne.setGravity(Gravity.CENTER);
        tabOne.setAllCaps(true);
        tabOne.setTextSize(10f);
        tabOne.setTextColor(getResources().getColor(R.color.profile_text_view_text));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_check_icon, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabOne);
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
