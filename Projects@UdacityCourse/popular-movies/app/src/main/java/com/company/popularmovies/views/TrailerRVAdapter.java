package com.company.popularmovies.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.popularmovies.R;
import com.company.popularmovies.models.Trailer;
import com.company.popularmovies.services.TrailerClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailerRVAdapter extends RecyclerView.Adapter<TrailerRVAdapter.TrailerVH> {

    private List<Trailer> mTrailers;
    private TrailerClickListener mClickListener;

    public TrailerRVAdapter(TrailerClickListener listener) {
        this.mTrailers = new ArrayList<>();
        this.mClickListener = listener;
    }

    @Override
    public TrailerVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_trailer_item, parent, false);
        return new TrailerVH(view);
    }

    @Override
    public void onBindViewHolder(TrailerVH holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return this.mTrailers.size();
    }

    public void updateListItems(List<Trailer> trailers) {
        this.mTrailers = trailers;
        this.notifyDataSetChanged();
    }

    public class TrailerVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_trailer_name) TextView mName;

        TrailerVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Trailer trailer = mTrailers.get(getAdapterPosition());
            mClickListener.onTrailerClicked(trailer);
        }

        void bind(int position) {
            Trailer trailer = mTrailers.get(position);
            this.mName.setText(trailer.getName());
        }
    }
}