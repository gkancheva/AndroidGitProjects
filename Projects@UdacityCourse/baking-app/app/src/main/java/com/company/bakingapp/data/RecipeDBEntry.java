package com.company.bakingapp.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class RecipeDBEntry implements BaseColumns {
    public static final Uri CONTENT_URI = DBContract.BASE_CONTENT_URI
            .buildUpon().appendPath(DBContract.PATH_RECIPES).build();

    private RecipeDBEntry(){}

    public static final String TABLE_NAME = "recipes";
    public static final String COLUMN_NAME = "recipe_name";
    public static final String COLUMN_SERVINGS = "servings";
    public static final String COLUMN_IMAGE = "image";

    public static Uri getContentUriSingleRow(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}