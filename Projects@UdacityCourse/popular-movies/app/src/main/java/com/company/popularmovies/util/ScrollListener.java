package com.company.popularmovies.util;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class ScrollListener extends RecyclerView.OnScrollListener {

    @Override
    public void onScrolled(RecyclerView rv, int dx, int dy) {
        super.onScrolled(rv, dx, dy);

        if(dy >= 0) {
            int visibleItemCount = rv.getLayoutManager().getChildCount();
            int totalItemCount = rv.getLayoutManager().getItemCount();
            int firstVisibleItemPos = ((GridLayoutManager)rv.getLayoutManager()).findFirstVisibleItemPosition();
            int threshold = 4;
            if((visibleItemCount + firstVisibleItemPos) >= totalItemCount - threshold) {
                this.onScrollRequested();
            }
        }
    }

    public abstract void onScrollRequested();
}