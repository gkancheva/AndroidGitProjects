package com.company.bakingapp.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.bakingapp.R;
import com.company.bakingapp.models.Step;
import com.company.bakingapp.services.StepClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsRVAdapter extends RecyclerView.Adapter<StepsRVAdapter.StepViewHolder> {

    private List<Step> mSteps;
    private StepClickListener mClickListener;
    private int mSelectedPos;

    public StepsRVAdapter(StepClickListener clickListener) {
        this.mSteps = new ArrayList<>();
        this.mClickListener = clickListener;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_step, parent, false);
        view.setFocusable(true);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        holder.itemView.setSelected(mSelectedPos == position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return this.mSteps.size();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_step_description) TextView mTvStepDescription;

        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            Step step = mSteps.get(position);
            String text = position + 1 + ". " + step.getShortDescription();
            this.mTvStepDescription.setText(text);

        }

        @Override
        public void onClick(View v) {
            Step step = mSteps.get(getAdapterPosition());
            notifyItemChanged(mSelectedPos);
            mSelectedPos = getAdapterPosition();
            notifyItemChanged(mSelectedPos);
            mClickListener.onStepSelected(step);
        }
    }

    public void updateStepList(List<Step> steps) {
        int itemCount = this.getItemCount();
        this.mSteps.clear();
        this.notifyItemRangeRemoved(0, itemCount);
        this.mSteps.addAll(steps);
        this.notifyItemRangeInserted(0, steps.size());
    }

}