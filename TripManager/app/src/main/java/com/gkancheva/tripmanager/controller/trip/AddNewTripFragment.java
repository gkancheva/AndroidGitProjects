package com.gkancheva.tripmanager.controller.trip;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gkancheva.tripmanager.MainActivity;
import com.gkancheva.tripmanager.R;
import com.gkancheva.tripmanager.model.trip.Trip;
import com.gkancheva.tripmanager.repositories.TripManager;
import com.gkancheva.tripmanager.view.DatePickerFragment;
import com.gkancheva.tripmanager.view.DoneOnEditorActionListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNewTripFragment extends Fragment implements View.OnClickListener{

    private TripManager mTripManager;
    private Trip mTrip;
    private EditText mTxtTripName, mTxtStartDate, mTxtEndDate, mTxtBudget;
    private Button mButton;
    private String mTripName, mStrStartDate, mStrEndDate;
    private Date mStartDate, mEndDate;
    private Double mBudget;

    public AddNewTripFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_add, container, false);
        setViews(view);
        mButton.setOnClickListener(this);
        return view;
    }

    private boolean validateInput() {
        boolean hasNoError = true;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        if(mTxtTripName != null && !mTxtTripName.getText().toString().isEmpty()) {
            mTripName = mTxtTripName.getText().toString();
            if(mTxtBudget != null && !mTxtBudget.getText().toString().isEmpty()) {
                mBudget = Double.parseDouble(mTxtBudget.getText().toString());
                if(mStrStartDate != null && !mStrStartDate.isEmpty() &&
                        mStrEndDate != null && !mStrEndDate.isEmpty()) {
                    try {
                        mStartDate = format.parse(mStrStartDate);
                        mEndDate = format.parse(mStrEndDate);
                        if(mStartDate.after(mEndDate)) {
                            mTxtEndDate.setError("End date should be after the start date");
                            hasNoError = false;
                        }
                    } catch (ParseException e) {
                        mTxtStartDate.setError("Please select a valid dates");
                        mTxtEndDate.setError("Please select a valid dates");
                        hasNoError = false;
                    }
                } else {
                    if(mTxtStartDate.getText().toString().isEmpty()) {
                        mTxtStartDate.setError("Start date cannot be empty");
                    } else {
                        mTxtEndDate.setError("End date cannot be empty");
                    }
                    hasNoError = false;
                }
            } else {
                mTxtBudget.setError("Budget cannot be empty");
                hasNoError = false;
            }
        } else {
            mTxtTripName.setError("Trip name cannot be empty");
            hasNoError = false;
        }
        if(!hasNoError) {
            Toast.makeText(getActivity(), "Please fill form correctly.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setViews(View v) {
        mTxtTripName = (EditText)v.findViewById(R.id.add_trip_name);
        mTxtTripName.setOnEditorActionListener(new DoneOnEditorActionListener());
        mTxtStartDate = (EditText)v.findViewById(R.id.add_trip_start_date);
        mTxtEndDate = (EditText)v.findViewById(R.id.add_trip_end_date);
        mTxtBudget = (EditText)v.findViewById(R.id.add_trip_budget);
        mTxtBudget.setOnEditorActionListener(new DoneOnEditorActionListener());
        mButton = (Button)v.findViewById(R.id.btn_add_new_trip);

        mTxtStartDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mTxtStartDate != null) {
                    mStrStartDate = mTxtStartDate.getText().toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                mTxtStartDate.setError(null);
            }
        });

        mTxtEndDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mTxtEndDate != null) {
                    mStrEndDate = mTxtEndDate.getText().toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                mTxtEndDate.setError(null);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_add_new_trip) {
            if(validateInput()) {
                mTrip = new Trip(mStartDate, mEndDate, mTripName, mBudget);
                mTripManager = new TripManager();
                if(mTripManager.saveNewTrip(mTrip)) {
                    ((MainActivity)getActivity()).scheduleNotificationForTrip(mStartDate, mTripName, mStartDate.toString());
                    ((MainActivity)getActivity()).showTrips();
                    Toast.makeText(getContext(), R.string.successfully_saved_trip, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), R.string.fail_saving_trip, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mTxtStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment dialog = new DatePickerFragment(v);
                    dialog.show(getActivity().getFragmentManager(), String.valueOf(R.string.date_picker));
                }
            }
        });
        mTxtEndDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment dialog = new DatePickerFragment(v);
                    dialog.show(getActivity().getFragmentManager(), String.valueOf(R.string.date_picker));
                }
            }
        });
    }
}
