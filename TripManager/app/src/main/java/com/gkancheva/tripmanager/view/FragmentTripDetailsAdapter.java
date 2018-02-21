package com.gkancheva.tripmanager.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gkancheva.tripmanager.controller.trip.SingleTripFragment;

public class FragmentTripDetailsAdapter extends FragmentStatePagerAdapter {

    private long mTripId;

    public FragmentTripDetailsAdapter(FragmentManager fm, long tripId) {
        super(fm);
        this.mTripId = tripId;
    }

    @Override
    public Fragment getItem(int position) {
        return SingleTripFragment.newInstance(position, mTripId);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Events";
            case 1:
                return "Expenses";
            default:
                return " ";
        }
    }
}
