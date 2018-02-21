package com.gkancheva.tripmanager.controller.trip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gkancheva.tripmanager.MainActivity;
import com.gkancheva.tripmanager.R;
import com.gkancheva.tripmanager.model.expense.Expense;
import com.gkancheva.tripmanager.model.trip.Trip;
import com.gkancheva.tripmanager.repositories.EventExpenseManager;
import com.gkancheva.tripmanager.repositories.TripManager;
import com.gkancheva.tripmanager.view.FragmentTripDetailsAdapter;

import java.text.SimpleDateFormat;
import java.util.List;

public class TripTabsFragment extends Fragment implements View.OnClickListener {

    private final static String TRIP_INDEX = "Trip index";
    private SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd, MMM, yyyy");
    private long mTripId;
    private Trip mTrip;
    private TripManager mTripManager;
    private EventExpenseManager mEventExpenseManager;
    private Button mButton;
    private TextView mTxtNoTripToShow;
    private List<Expense> expenses;

    public TripTabsFragment() {}

    public static TripTabsFragment newInstance(long tripIndex) {
        TripTabsFragment mFragment = new TripTabsFragment();
        Bundle mArgs = new Bundle();
        mArgs.putLong(TRIP_INDEX, tripIndex);
        mFragment.setArguments(mArgs);
        return mFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_layout_fragment, container, false);
        Bundle args = getArguments();
        mTripId = args.getLong(TRIP_INDEX);
        mTripManager = new TripManager();
        mEventExpenseManager = new EventExpenseManager();
        mButton = (Button) view.findViewById(R.id.btn_add_trip);
        if(mTripId != -1) {
            mTrip = mTripManager.findTripById(mTripId);
        }
        if(mTrip != null) {
            ((MainActivity)getActivity()).updateToolbar(R.string.trip_name, mTrip.getName(), R.string.starting_on, formatter.format(mTrip.getStartDate()));
        } else {
            ((MainActivity)getActivity()).updateToolbar(R.string.app_name, -1);
        }
        expenses = mEventExpenseManager.getAllExpensesByTrip(mTrip);
        if(mTrip != null) {
            if(expenses.size() > 0) {
                FragmentTripDetailsAdapter tabsAdapter = new FragmentTripDetailsAdapter(getChildFragmentManager(), mTrip.getId());
                ViewPager tabsPager = (ViewPager)view.findViewById(R.id.tab_viewpager);
                tabsPager.setAdapter(tabsAdapter);
                TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tab_layout);
                tabLayout.setupWithViewPager(tabsPager);
            } else {
                mButton.setVisibility(View.VISIBLE);
                mTxtNoTripToShow = (TextView)view.findViewById(R.id.no_trips);
                mTxtNoTripToShow.setText(R.string.info_no_events_to_show);
                mButton.setText(R.string.add_new_event);
                mButton.setOnClickListener(this);
            }
        } else {
            mButton.setVisibility(View.VISIBLE);
            mTxtNoTripToShow = (TextView)view.findViewById(R.id.no_trips);
            mTxtNoTripToShow.setText(R.string.info_no_trips_to_show);
            mButton.setText(R.string.add_new_trip);
            mButton.setOnClickListener(this);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_add_trip && mTrip == null) {
            ((MainActivity)getActivity()).showAddNewTrip();
        } else {
            ((MainActivity)getActivity()).showAddNewCategoryExp(mTrip.getId());
        }
    }
}
