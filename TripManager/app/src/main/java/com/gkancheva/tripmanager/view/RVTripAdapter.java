package com.gkancheva.tripmanager.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gkancheva.tripmanager.R;
import com.gkancheva.tripmanager.model.trip.Trip;
import com.gkancheva.tripmanager.repositories.EventExpenseManager;

import java.text.SimpleDateFormat;
import java.util.List;

public class RVTripAdapter extends RecyclerView.Adapter<RVTripAdapter.TripViewHolder>{

    private List<Trip> mTrips;
    private Trip mTrip;
    private EventExpenseManager mExpManager;
    private IOnTripClickListener mListener;
    private SimpleDateFormat mDateFormat;

    public RVTripAdapter(List<Trip> trips, IOnTripClickListener mListener) {
        this.mTrips = trips;
        this.mListener = mListener;
    }

    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_row_trip, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TripViewHolder holder, int position) {
        mDateFormat = new SimpleDateFormat("dd-MMM-yyy");
        mExpManager = new EventExpenseManager();
        mTrip = mTrips.get(position);
        holder.mTxtTripName.setText(mTrip.getName());
        holder.mTxtTripDescription.setText(mTrip.getName());
        holder.mTxtTripDates.setText(mDateFormat.format(mTrip.getStartDate()) + " ~ " + mDateFormat.format(mTrip.getEndDate()));
        holder.mTxtTripExpenses.setText(String.format("%.2f", mExpManager.getTotalSumExpByTrip(mTrip)));
        holder.mTxtTripBudget.setText(String.format("%.2f", mTrip.getBudget()));
    }

    public interface IOnTripClickListener {
        public void onTripSelected(View v, int position);
        public void onTripLongClicked(View v, int position);
    }

    public class TripViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        TextView mTxtTripName;
        TextView mTxtTripDescription;
        TextView mTxtTripDates;
        TextView mTxtTripExpenses;
        TextView mTxtTripBudget;

        public TripViewHolder(View v) {
            super(v);
            mTxtTripName = (TextView)v.findViewById(R.id.trip_name);
            mTxtTripDescription = (TextView)v.findViewById(R.id.trip_description);
            mTxtTripDates = (TextView)v.findViewById(R.id.trip_dates);
            mTxtTripExpenses = (TextView)v.findViewById(R.id.trip_expenses);
            mTxtTripBudget = (TextView)v.findViewById(R.id.trip_budget);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onTripSelected(v, getLayoutPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            mListener.onTripLongClicked(v, getLayoutPosition());
            return true;
        }
    }

    @Override
    public int getItemCount() {
        return this.mTrips.size();
    }


}
