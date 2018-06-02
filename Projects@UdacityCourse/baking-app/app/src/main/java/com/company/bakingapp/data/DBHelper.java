package com.company.bakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "recipes_db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE_RECIPES =
                "CREATE TABLE " + RecipeDBEntry.TABLE_NAME + " (" +
                        RecipeDBEntry._ID + " INTEGER PRIMARY KEY, " +
                        RecipeDBEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                        RecipeDBEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +
                        RecipeDBEntry.COLUMN_SERVINGS + " INTEGER NOT NULL);";
        db.execSQL(SQL_CREATE_TABLE_RECIPES);
        final String SQL_CREATE_TABLE_INGREDIENTS =
                "CREATE TABLE " + IngredientDBEntry.TABLE_NAME + " (" +
                        IngredientDBEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        IngredientDBEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                        IngredientDBEntry.COLUMN_QUANTITY + " DECIMAL NOT NULL, " +
                        IngredientDBEntry.COLUMN_MEASURE + " TEXT NOT NULL, " +
                        IngredientDBEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                        "FOREIGN KEY (" + IngredientDBEntry.COLUMN_RECIPE_ID + ") " +
                        "REFERENCES " + RecipeDBEntry.TABLE_NAME + " (" + RecipeDBEntry._ID + "));";
        db.execSQL(SQL_CREATE_TABLE_INGREDIENTS);
        final String SQL_CREATE_TABLE_STEPS =
                "CREATE TABLE " + StepDBEntry.TABLE_NAME + " (" +
                        StepDBEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        StepDBEntry.COLUMN_ID + "  INTEGER NOT NULL, " +
                        StepDBEntry.COLUMN_SHORT_DESCRIPTION + " TEXT NOT NULL, " +
                        StepDBEntry.COLUMN_DESCRIPTION + " DECIMAL NOT NULL, " +
                        StepDBEntry.COLUMN_VIDEO_URL + " TEXT NOT NULL, " +
                        StepDBEntry.COLUMN_THUMBNAIL_URL + " TEXT NOT NULL, " +
                        StepDBEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                        "FOREIGN KEY (" + StepDBEntry.COLUMN_RECIPE_ID + ") " +
                        "REFERENCES " + RecipeDBEntry.TABLE_NAME + " (" + RecipeDBEntry._ID + "));";
        db.execSQL(SQL_CREATE_TABLE_STEPS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < newVersion) {
            // alter table accordingly without loosing data
            db.execSQL("ALTER TABLE " + RecipeDBEntry.TABLE_NAME);
            db.execSQL("ALTER TABLE " + IngredientDBEntry.TABLE_NAME);
            db.execSQL("ALTER TABLE " + StepDBEntry.TABLE_NAME);
        }
    }
}