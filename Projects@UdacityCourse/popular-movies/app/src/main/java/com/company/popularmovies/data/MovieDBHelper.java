package com.company.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "movies_db";
    private static final int DB_VERSION = 1;

    public MovieDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE_STATEMENT =
                "CREATE TABLE " + MovieDBEntry.TABLE_NAME + " (" +
                MovieDBEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieDBEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                MovieDBEntry.COLUMN_THUMBNAIL + " BLOB NOT NULL, " +
                MovieDBEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieDBEntry.COLUMN_RATING + " DOUBLE NOT NULL, " +
                MovieDBEntry.COLUMN_TIMESTAMP + " TIMESTAMP NOT NULL);";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieDBEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}