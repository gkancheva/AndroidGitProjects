package com.gkancheva.tripmanager.controller.trip;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gkancheva.tripmanager.MainActivity;
import com.gkancheva.tripmanager.R;
import com.gkancheva.tripmanager.model.expense.Event;
import com.gkancheva.tripmanager.model.expense.Expense;
import com.gkancheva.tripmanager.model.trip.Trip;
import com.gkancheva.tripmanager.repositories.EventExpenseManager;
import com.gkancheva.tripmanager.repositories.TripManager;
import com.gkancheva.tripmanager.view.RVEventAdapter;
import com.gkancheva.tripmanager.view.RVExpenseAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SingleTripFragment extends Fragment implements RVExpenseAdapter.IOnExpenseClickListener, RVEventAdapter.IOnEventClickListener{

    private static final String TAB_POSITION = "tab_position", TRIP_ID = "trip_id";
    private SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd, MMM, yyyy");
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private TripManager mTripManager;
    private EventExpenseManager mEventExpenseManager;
    private Trip mTrip;
    private List<Expense> mExpenses;
    private List<Event> mEvents;

    public SingleTripFragment(){}

    public static SingleTripFragment newInstance(int tabPosition, long tripId) {
        SingleTripFragment mFragment = new SingleTripFragment();
        Bundle mArgs = new Bundle();
        mArgs.putInt(TAB_POSITION, tabPosition);
        mArgs.putLong(TRIP_ID, tripId);
        mFragment.setArguments(mArgs);
        return mFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle mArgs = getArguments();
        int tabPosition = mArgs.getInt(TAB_POSITION);
        long tripId = mArgs.getLong(TRIP_ID);
        View view = inflater.inflate(R.layout.fragment_trip_single, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_single_trip);
        mLinearLayoutManager = new LinearLayoutManager(view.getContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mTripManager = new TripManager();
        mTrip = mTripManager.findTripById(tripId);
        if(mTrip != null) {
            ((MainActivity)getActivity()).updateToolbar(R.string.trip_name, mTrip.getName(), R.string.starting_on, formatter.format(mTrip.getStartDate()));
            mEventExpenseManager = new EventExpenseManager();
            if(mTrip.getStartDate().before(new Date())) {
                mExpenses = mEventExpenseManager.getAllExpensesByTrip(mTrip);
                mEvents = mEventExpenseManager.getAllEventsByTrip(mTrip);
            } else {
                mExpenses = mEventExpenseManager.getAllExpensesByTrip(mTrip);
                mEvents = mEventExpenseManager.getAllUpcomingEventsByTrip(mTrip);
            }
            switch (tabPosition) {
                case 0:
                    RVEventAdapter eventAdapter = new RVEventAdapter(mEvents, this);
                    mRecyclerView.setAdapter(eventAdapter);
                    break;
                case 1:
                    RVExpenseAdapter expenseAdapter = new RVExpenseAdapter(mExpenses, this);
                    mRecyclerView.setAdapter(expenseAdapter);
                    break;
            }
        } else {
            showLayoutWithoutTrips();
        }

        return view;
    }

    private void showLayoutWithoutTrips() {
        Toast.makeText(getContext(), R.string.info_no_trips_to_show, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onExpenseSelected(View v, int i) {
        ((MainActivity)getActivity()).showSingleExpense(mExpenses.get(i));
    }

    @Override
    public void onEventSelected(View v, int i) {
        ((MainActivity)getActivity()).showSingleEvent(mEvents.get(i));
    }

    @Override
    public void onEventLongClicked(View v, final int i) {
        if(mEvents.get(i) != null) {
            AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
            b.setMessage(R.string.delete_event_question).setTitle(getResources().getString(R.string.deleting_event) + " " +  mEvents.get(i).getTitle());
            b.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(mEventExpenseManager.deleteEvent(mEvents.get(i))) {
                        Toast.makeText(getContext(), R.string.event_deleted, Toast.LENGTH_SHORT).show();
                        ((MainActivity)getActivity()).showHomeFragment();
                    }
                }
            });
            b.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog alert = b.create();
            alert.show();
        }
    }
}
