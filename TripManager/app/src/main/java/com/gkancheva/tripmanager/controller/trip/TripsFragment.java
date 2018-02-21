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
import com.gkancheva.tripmanager.model.trip.Trip;
import com.gkancheva.tripmanager.repositories.EventExpenseManager;
import com.gkancheva.tripmanager.repositories.TripManager;
import com.gkancheva.tripmanager.view.RVTripAdapter;

import java.util.ArrayList;
import java.util.List;

public class TripsFragment extends Fragment implements RVTripAdapter.IOnTripClickListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RVTripAdapter mTripAdapter;
    private List<Trip> mTrips;
    private TripManager mTripManager;

    public TripsFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_all, container, false);
        ((MainActivity)getActivity()).updateToolbar(R.string.app_name, "", R.string.trips, "");
        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_trips);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mTripManager = new TripManager();
        mTrips = new ArrayList<>();
        mTrips = mTripManager.findOrdered();
        mTripAdapter = new RVTripAdapter(mTrips, this);
        mRecyclerView.setAdapter(mTripAdapter);
        return view;
    }

    @Override
    public void onTripSelected(View v, int position) {
        ((MainActivity)getActivity()).showSingleTripInfo(mTrips.get(position).getId());
    }

    @Override
    public void onTripLongClicked(View v, final int position) {
        if(mTripManager.findTripById(mTrips.get(position).getId()) != null) {
            AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
            b.setMessage(R.string.delete_trip_question).setTitle(getResources().getString(R.string.deleting_trip) + " " +  mTrips.get(position).getName());
            b.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EventExpenseManager eem = new EventExpenseManager();
                    eem.deleteAllExpensesByTrip(mTrips.get(position).getId());
                    eem.deleteAllEventsByTrip(mTrips.get(position).getId());
                    mTripManager.deleteTrip(mTrips.get(position).getId());
                    Toast.makeText(getContext(), R.string.trip_deleted, Toast.LENGTH_SHORT).show();
                    ((MainActivity)getActivity()).showTrips();
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
