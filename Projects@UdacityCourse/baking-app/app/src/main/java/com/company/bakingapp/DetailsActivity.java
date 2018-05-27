package com.company.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.company.bakingapp.models.Recipe;
import com.company.bakingapp.models.Step;

public class DetailsActivity extends AppCompatActivity
    implements FragmentDetailsList.OnStepSelectedListener {

    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if(getIntent() == null) {
            this.closeOnError();
            return;
        }

        this.mRecipe = getIntent().getParcelableExtra("RECIPE");
        if(this.mRecipe == null){
            this.closeOnError();
            return;
        }
        this.populateUi(savedInstanceState);
    }

    @Override
    public void onStepSelected(Step step) {
        FragmentDetailView fragmentDetailView = new FragmentDetailView();
        if(findViewById(R.id.fragment_details_view) == null) {
            Bundle args = new Bundle();
            args.putParcelable("STEP", step);
            fragmentDetailView.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragmentDetailView)
                    .addToBackStack(null)
                    .commit();
        } else {
            fragmentDetailView = (FragmentDetailView) getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_details_view);
            fragmentDetailView.setContent(step);
        }
    }

    private void populateUi(Bundle savedInstanceState) {
        if(savedInstanceState == null) {
            FragmentDetailsList fragmentDetailsList = new FragmentDetailsList();
            if(findViewById(R.id.fragment_details_view) == null) {
                Bundle args = new Bundle();
                args.putParcelable("RECIPE", this.mRecipe);
                fragmentDetailsList.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragmentDetailsList)
                        .commit();
            } else {
                fragmentDetailsList = (FragmentDetailsList) getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_details_list);
                fragmentDetailsList.setContent(this.mRecipe);
                FragmentDetailView frDetail = (FragmentDetailView) getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_details_view);
                frDetail.setContent(this.mRecipe.getSteps().get(0));
            }
        } else {

        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Error opening recipe details", Toast.LENGTH_SHORT).show();
    }
}
