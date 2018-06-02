package com.company.bakingapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class StepDBEntry implements BaseColumns {
    public static final Uri CONTENT_URI = DBContract.BASE_CONTENT_URI
            .buildUpon().appendPath(DBContract.PATH_STEPS).build();

    private StepDBEntry(){}

    public static final String TABLE_NAME = "steps";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SHORT_DESCRIPTION = "short_description";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_VIDEO_URL = "video_url";
    public static final String COLUMN_THUMBNAIL_URL = "thumbnail_url";
    public static final String COLUMN_RECIPE_ID = "recipe_id";

}