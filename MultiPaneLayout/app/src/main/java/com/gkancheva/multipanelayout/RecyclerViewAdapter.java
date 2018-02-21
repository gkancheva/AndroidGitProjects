package com.gkancheva.multipanelayout;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<Article> mListArticles;
    private ISingleItemClickListener mListener;

    public interface ISingleItemClickListener {
        public void onItemSelected(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.title_single_element);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemSelected(getAdapterPosition());
        }
    }

    public void setOnItemSelectedListener(ISingleItemClickListener itemSelectedListener) {
        this.mListener = itemSelectedListener;
    }

    public RecyclerViewAdapter(ArrayList<Article> data) {
        this.mListArticles = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_element_titles, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder != null) {
            holder.mTitle.setText(mListArticles.get(position).getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return mListArticles.size();
    }

}
