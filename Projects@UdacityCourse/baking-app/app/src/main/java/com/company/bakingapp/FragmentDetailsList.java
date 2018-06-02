package com.company.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.bakingapp.models.Recipe;
import com.company.bakingapp.models.Step;
import com.company.bakingapp.services.StepClickListener;
import com.company.bakingapp.views.ItemDivider;
import com.company.bakingapp.views.StepsRVAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentDetailsList extends Fragment
        implements StepClickListener {

    private static final String RECIPE = "RECIPE";

    public interface OnStepSelectedListener {
        void onStepSelected(Step step);
    }

    public FragmentDetailsList() {
    }

    private OnStepSelectedListener mListener;
    private Recipe mRecipe;
    @BindView(R.id.tv_ingredients) TextView mTvIngredients;
    @BindView(R.id.rv_steps) RecyclerView mRvSteps;
    private StepsRVAdapter mStepsRVAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            this.mRecipe = savedInstanceState.getParcelable(RECIPE);
        }
        View view = inflater.inflate(R.layout.fragment_recipe_details_list, container, false);
        ButterKnife.bind(this, view);
        this.setElements();
        if(getArguments() != null && getArguments().getParcelable(RECIPE) != null) {
            this.mRecipe = getArguments().getParcelable(RECIPE);
            this.setContent(this.mRecipe);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE, this.mRecipe);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getArguments() != null && getArguments().getParcelable(RECIPE) != null) {
            this.mRecipe = getArguments().getParcelable(RECIPE);
            this.setContent(this.mRecipe);
        } else if(this.mRecipe != null) {
            this.setContent(this.mRecipe);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mListener = (OnStepSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepSelectedListener");
        }
    }

    @Override
    public void onStepSelected(Step step) {
        if(this.mListener != null) {
            this.mListener.onStepSelected(step);
        }
    }

    private void setElements() {
        this.mRvSteps.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mRvSteps.addItemDecoration(new ItemDivider(getActivity()));
        this.mRvSteps.setItemAnimator(new DefaultItemAnimator());
        this.mRvSteps.setHasFixedSize(true);
        this.mStepsRVAdapter = new StepsRVAdapter(this);
        this.mRvSteps.setAdapter(this.mStepsRVAdapter);
    }

    public void setContent(Recipe recipe) {
        getActivity().setTitle(recipe.getName());
        this.mTvIngredients.setText(recipe.getIngredientsAsText());
        this.mStepsRVAdapter.updateStepList(recipe.getSteps());
    }
}