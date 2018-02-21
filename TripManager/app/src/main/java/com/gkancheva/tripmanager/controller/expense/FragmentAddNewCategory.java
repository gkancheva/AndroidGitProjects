package com.gkancheva.tripmanager.controller.expense;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gkancheva.tripmanager.MainActivity;
import com.gkancheva.tripmanager.R;
import com.gkancheva.tripmanager.model.trip.Trip;
import com.gkancheva.tripmanager.repositories.TripManager;
import com.gkancheva.tripmanager.view.RVCategoryAdapter;

import java.util.Arrays;
import java.util.List;

public class FragmentAddNewCategory extends Fragment implements RVCategoryAdapter.IOnCategoryClickListener{

    private static final String TRIP_INDEX = "Trip index";
    private static final String TRANSPORTATION = "Transportation", RENT_A_CAR = "Rent a car", EXPENSE = "Expense", ACCOMMODATION = "Accommodation";
    private Trip mTrip;
    private TripManager mTripManager;
    private long mTripIndex;
    private RecyclerView mrv;
    private RVCategoryAdapter mRVAdapter;
    private List<String> mCategories;

    public FragmentAddNewCategory(){}

    public static FragmentAddNewCategory newInstance(long tripIndex) {
        FragmentAddNewCategory mFragment = new FragmentAddNewCategory();
        Bundle mArgs = new Bundle();
        mArgs.putLong(TRIP_INDEX, tripIndex);
        mFragment.setArguments(mArgs);
        return mFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exp_categories_add, container, false);
        Bundle args = getArguments();
        mTripIndex = args.getLong(TRIP_INDEX);
        mTripManager = new TripManager();
        mTrip = mTripManager.findTripById(mTripIndex);

        ActionBar toolbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        toolbar.setTitle(R.string.adding_new_plan);
        toolbar.setSubtitle("to: " + mTrip.getName());

        mrv = (RecyclerView)view.findViewById(R.id.rv_categories);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mrv.setLayoutManager(llm);
        mCategories = Arrays.asList(TRANSPORTATION, ACCOMMODATION, RENT_A_CAR, EXPENSE);
        mRVAdapter = new RVCategoryAdapter(mCategories, this);
        mrv.setAdapter(mRVAdapter);
        return view;
    }

    @Override
    public void onCategorySelected(View v, int i) {
        String category = "";
        switch (i) {
            case 0:
                category = TRANSPORTATION;
                break;
            case 1:
                category = ACCOMMODATION;
                break;
            case 2:
                category = RENT_A_CAR;
                break;
            case 3:
                category = EXPENSE;
                break;
            default:
                break;
        }
        ((MainActivity)getActivity()).showAddNewExpense(mTripIndex, category);
    }
}
