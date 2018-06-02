package com.company.bakingapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class DBContentProvider extends ContentProvider {

    private static final int RECIPES = 100;
    private static final int RECIPES_ID = 101;
    private static final int INGREDIENTS = 200;
    private static final int INGREDIENTS_ID = 201;
    private static final int STEPS = 300;
    private static final int STEPS_ID = 301;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DBHelper sDBHelper;

    @Override
    public boolean onCreate() {
        this.sDBHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selCriteria, @Nullable String[] selArguments, @Nullable String sortOrder) {
        final SQLiteDatabase db = sDBHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor;
        switch (match) {
            case RECIPES:
                cursor = db.query(RecipeDBEntry.TABLE_NAME, projection, selCriteria,
                        selArguments, null, null, sortOrder);
                break;
            case RECIPES_ID:
                String id = uri.getPathSegments().get(1);
                String criteria = "_id = ?";
                String[] arguments = new String[]{id};
                cursor = db.query(RecipeDBEntry.TABLE_NAME, projection, criteria,
                        arguments, null, null, sortOrder);
                break;
            case INGREDIENTS:
                cursor = db.query(IngredientDBEntry.TABLE_NAME, projection, selCriteria,
                        selArguments, null, null, sortOrder);
                break;
            case INGREDIENTS_ID:
                String recipe_id = uri.getPathSegments().get(1);
                criteria = "recipe_id = ?";
                arguments = new String[]{recipe_id};
                cursor = db.query(IngredientDBEntry.TABLE_NAME, projection, criteria,
                        arguments, null, null, sortOrder);
                break;
            case STEPS:
                cursor = db.query(StepDBEntry.TABLE_NAME, projection, selCriteria,
                        selArguments, null, null, sortOrder);
                break;
            case STEPS_ID:
                recipe_id = uri.getPathSegments().get(1);
                criteria = "recipe_id = ?";
                arguments = new String[]{recipe_id};
                cursor = db.query(StepDBEntry.TABLE_NAME, projection, criteria,
                        arguments, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues cv) {
        final SQLiteDatabase db = sDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        long id;
        switch (match) {
            case RECIPES:
                id = db.insert(RecipeDBEntry.TABLE_NAME, null, cv);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(RecipeDBEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            case INGREDIENTS:
                id = db.insert(IngredientDBEntry.TABLE_NAME, null, cv);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(RecipeDBEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            case STEPS:
                id = db.insert(StepDBEntry.TABLE_NAME, null, cv);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(RecipeDBEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String whereClause, @Nullable String[] whereArgs) {
        final SQLiteDatabase db = sDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int deletedRows;
        switch (match) {
            case RECIPES:
                deletedRows = db.delete(RecipeDBEntry.TABLE_NAME, null, null);
                db.delete(IngredientDBEntry.TABLE_NAME, null, null);
                db.delete(StepDBEntry.TABLE_NAME, null, null);
                break;
            case RECIPES_ID:
                deletedRows = db.delete(RecipeDBEntry.TABLE_NAME, null, null);
                db.delete(IngredientDBEntry.TABLE_NAME, null, null);
                db.delete(StepDBEntry.TABLE_NAME, null, null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case RECIPES:
                return DBContract.PATH_RECIPES;
            case RECIPES_ID:
                return DBContract.PATH_SINGLE_RECIPE;
            case INGREDIENTS:
                return DBContract.PATH_INGREDIENTS;
            case INGREDIENTS_ID:
                return DBContract.PATH_SINGLE_INGREDIENT;
            case STEPS:
                return DBContract.PATH_STEPS;
            case STEPS_ID:
                return DBContract.PATH_SINGLE_STEP;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_RECIPES, RECIPES);
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_SINGLE_RECIPE, RECIPES_ID);
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_INGREDIENTS, INGREDIENTS);
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_SINGLE_INGREDIENT, INGREDIENTS_ID);
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_STEPS, STEPS);
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_SINGLE_STEP, STEPS_ID);
        return uriMatcher;
    }
}