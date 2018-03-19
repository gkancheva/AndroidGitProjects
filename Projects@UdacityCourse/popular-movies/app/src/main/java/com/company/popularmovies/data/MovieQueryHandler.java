package com.company.popularmovies.data;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class MovieQueryHandler extends AsyncQueryHandler {

    private final AsyncQueryListener mListener;

    public interface AsyncQueryListener {
        void onQueryCompleted(Object obj);
    }

    public MovieQueryHandler(ContentResolver cr, AsyncQueryListener listener) {
        super(cr);
        this.mListener = listener;
    }

    @Override
    protected void onInsertComplete(int token, Object movie, Uri uri) {
        this.mListener.onQueryCompleted(movie);
    }

    @Override
    protected void onDeleteComplete(int token, Object movie, int result) {
        this.mListener.onQueryCompleted(movie);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        this.mListener.onQueryCompleted(cursor);
    }
}
