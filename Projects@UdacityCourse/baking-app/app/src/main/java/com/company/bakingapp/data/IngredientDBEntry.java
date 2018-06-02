package com.company.bakingapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class IngredientDBEntry implements BaseColumns {
    public static final Uri CONTENT_URI = DBContract.BASE_CONTENT_URI
            .buildUpon().appendPath(DBContract.PATH_INGREDIENTS).build();

    private IngredientDBEntry(){}

    public static final String TABLE_NAME = "ingredients";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_MEASURE = "measure";
    public static final String COLUMN_RECIPE_ID = "recipe_id";

}