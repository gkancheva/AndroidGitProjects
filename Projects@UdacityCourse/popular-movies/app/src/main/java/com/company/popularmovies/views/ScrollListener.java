package com.company.popularmovies.views;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class ScrollListener extends RecyclerView.OnScrollListener {

    private boolean isLoading = true;
    private int prevTotalItemCount = 0;

    @Override
    public void onScrolled(RecyclerView rv, int dx, int dy) {
        super.onScrolled(rv, dx, dy);

        if(dy >= 0) {
            int visibleItemCount = rv.getLayoutManager().getChildCount();
            int totalItemCount = rv.getLayoutManager().getItemCount();
            int firstVisibleItemPos = ((GridLayoutManager)rv.getLayoutManager()).findFirstVisibleItemPosition();

            if(this.isLoading) {
                if(totalItemCount >= this.prevTotalItemCount) {
                    this.isLoading = false;
                    this.prevTotalItemCount = totalItemCount;
                }
            }

            if(!this.isLoading) {
                int threshold = 4;
                if ((totalItemCount - threshold) <= (visibleItemCount + firstVisibleItemPos)) {
                    this.onScrollRequested();
                    this.isLoading = true;
                }
            }
        }
    }

    public abstract void onScrollRequested();

    protected void setTotal(int total) {
        this.prevTotalItemCount = total;
    }
}