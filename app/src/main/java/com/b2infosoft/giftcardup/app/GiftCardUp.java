package com.b2infosoft.giftcardup.app;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.b2infosoft.giftcardup.fragments.Dashboard;
import com.b2infosoft.giftcardup.fragments.SellCards;
import com.b2infosoft.giftcardup.fragments.SpeedySell;
import com.b2infosoft.giftcardup.fragments.TinderWork;
import com.b2infosoft.giftcardup.fragments.profile.BankInformation;
import com.b2infosoft.giftcardup.fragments.profile.Identification;
import com.b2infosoft.giftcardup.fragments.profile.SsnEin;

/**
 * Created by rajesh on 9/14/2016.
 */
abstract public class GiftCardUp extends AppCompatActivity implements View.OnClickListener, BankInformation.OnFragmentBankInformation, Identification.OnFragmentIdentification, SsnEin.OnFragmentSsnEin, TinderWork.OnFragmentDashboard1, Dashboard.OnFragmentDashboard, SellCards.OnFragmentSellCards, SpeedySell.OnFragmentSpeedyCell {
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBankInformation(Uri uri) {

    }

    @Override
    public void onDashboard(Uri uri) {

    }

    @Override
    public void onDashboard1(Uri uri) {

    }

    @Override
    public void onIdentification(Uri uri) {

    }

    @Override
    public void onSellCards(Uri uri) {

    }

    @Override
    public void onSpeedyCell(Uri uri) {

    }

    @Override
    public void onSsnEin(Uri uri) {

    }
}
