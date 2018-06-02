package com.company.bakingapp.data;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class DBQueryHandler extends AsyncQueryHandler {

    private final AsyncQueryListener mListener;

    public interface AsyncQueryListener {
        void onQueryCompleted(Object obj);
    }

    public DBQueryHandler(ContentResolver cr, AsyncQueryListener listener) {
        super(cr);
        this.mListener = listener;
    }

    @Override
    protected void onInsertComplete(int token, Object recipe, Uri uri) {
        this.mListener.onQueryCompleted(recipe);
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