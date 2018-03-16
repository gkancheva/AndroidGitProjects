package com.company.popularmovies.views;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.company.popularmovies.R;
import com.company.popularmovies.models.Review;
import com.company.popularmovies.services.ReviewClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewRVAdapter extends RecyclerView.Adapter<ReviewRVAdapter.ReviewVH> {

    private List<Review> mReviews;
    private ReviewClickListener mClickListener;

    public ReviewRVAdapter(ReviewClickListener listener) {
        this.mReviews = new ArrayList<>();
        this.mClickListener = listener;
    }

    @Override
    public ReviewVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_review_item, parent, false);
        return new ReviewVH(view);
    }

    @Override
    public void onBindViewHolder(ReviewVH holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return this.mReviews.size();
    }

    public void updateListItems(List<Review> reviews) {
        this.mReviews = reviews;
        this.notifyDataSetChanged();
    }

    public class ReviewVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_author) TextView mAuthor;
        @BindView(R.id.tv_content) TextView mContent;

        public ReviewVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Review review = mReviews.get(getAdapterPosition());
            mClickListener.onReviewClicked(review);
        }

        public void bind(int position) {
            Review review = mReviews.get(position);
            this.mAuthor.setText(review.getAuthor());
            this.mContent.setText(review.getContent());
            ViewTreeObserver vto = this.mContent.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ViewTreeObserver obs = mContent.getViewTreeObserver();
                    obs.removeGlobalOnLayoutListener(this);
                    int lineCount = mContent.getLineCount();
                    if(lineCount > 3) {
                        int endInd = mContent.getLayout().getLineEnd(2);
                        String text = mContent.getText().toString().substring(0, endInd - 14).trim() + "...";
                        SpannableString str = new SpannableString(text + mContent.getContext().getString(R.string.show_more_message));
                        str.setSpan(new ForegroundColorSpan(Color.BLUE), str.length() - 10, str.length(), 0);
                        mContent.setText(str);
                    }
                }
            });
        }
    }
}