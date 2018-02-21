package com.gkancheva.tripmanager.view;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gkancheva.tripmanager.R;

import java.util.List;

public class RVCategoryAdapter extends RecyclerView.Adapter<RVCategoryAdapter.CategoryVHolder> {

    private List<String> mCategories;
    private IOnCategoryClickListener mListener;

    public RVCategoryAdapter(List<String> categories, IOnCategoryClickListener listener) {
        this.mCategories = categories;
        this.mListener = listener;
    }

    public interface IOnCategoryClickListener {
        public void onCategorySelected(View v, int i);
    }

    @Override
    public CategoryVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_row_category, parent, false);
        return new CategoryVHolder(view);
    }

    @Override
    public void onBindViewHolder(RVCategoryAdapter.CategoryVHolder holder, int position) {
        String category = mCategories.get(position);
        holder.mImageView.setImageResource(getIconByTypeCategory(category));
        holder.mTxtDescription.setText(category);
    }

    private int getIconByTypeCategory(String category) {
        switch (category) {
            case "Transportation":
                return R.mipmap.ic_direction_24dp;
            case "Accommodation":
                return R.mipmap.ic_hotel_black_24dp;
            case "Rent a car":
                return R.mipmap.ic_road_24dp;
            case "Expense":
                return R.mipmap.ic_dining;
            default:
                break;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return this.mCategories.size();
    }

    public class CategoryVHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;
        private TextView mTxtDescription;

        public CategoryVHolder(View v) {
            super(v);
            mImageView = (ImageView)v.findViewById(R.id.icon_category_exp);
            mTxtDescription = (TextView)v.findViewById(R.id.category_exp_desc);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onCategorySelected(v, this.getLayoutPosition());
        }
    }
}
