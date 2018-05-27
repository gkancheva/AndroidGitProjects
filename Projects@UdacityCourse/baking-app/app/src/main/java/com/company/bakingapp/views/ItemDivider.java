package com.company.bakingapp.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.company.bakingapp.R;

public class ItemDivider extends RecyclerView.ItemDecoration{
    private Drawable mDrawable;

    public ItemDivider(Context ctx) {
        this.mDrawable = ctx.getResources().getDrawable(R.drawable.item_rv_divider);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + this.mDrawable.getIntrinsicHeight();
            this.mDrawable.setBounds(left, top, right, bottom);
            this.mDrawable.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = this.mDrawable.getIntrinsicHeight();
    }
}