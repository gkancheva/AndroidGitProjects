package com.company.bakingapp.repository;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.company.bakingapp.R;
import com.company.bakingapp.data.DBQueryHandler;
import com.company.bakingapp.data.IngredientDBEntry;
import com.company.bakingapp.data.RecipeDBEntry;
import com.company.bakingapp.data.StepDBEntry;
import com.company.bakingapp.models.Ingredient;
import com.company.bakingapp.models.Recipe;
import com.company.bakingapp.models.Step;
import com.company.bakingapp.services.RecipeRepoListener;
import com.company.bakingapp.services.RecipeLoaderTask;
import com.company.bakingapp.utils.CursorUtils;
import com.company.bakingapp.utils.JsonUtils;

import org.json.JSONException;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

public class RecipeRepoImpl implements RecipeRepo,
    LoaderManager.LoaderCallbacks<String>, DBQueryHandler.AsyncQueryListener {

    private static final String PATH = "path";
    private static final int LOADER_ID_GET_RECIPES = 101;
    private final Context mContext;
    private final LoaderManager mLoaderManager;
    private RecipeRepoListener mRecipeListener;

    public RecipeRepoImpl(Context ctx, LoaderManager loaderManager, RecipeRepoListener recipeListener) {
        this.mContext = ctx;
        this.mLoaderManager = loaderManager;
        this.mRecipeListener = recipeListener;
    }

    @Override
    public void getRecipes() {
        String path = this.mContext.getString(R.string.recipes_url);
        Bundle args = new Bundle();
        args.putString(PATH, path);
        if(this.hasNetworkConnectivity()) {
            this.mLoaderManager.initLoader(LOADER_ID_GET_RECIPES, args, this);
        } else {
            this.mRecipeListener.onRecipeFailure(this.mContext.getString(R.string.no_internet_connction));
        }
    }

    @Override
    public void saveRecipeLocally(Recipe recipe) {
        //Saving the new recipe, deletes the previous one.
        this.deleteRecipeFromLocalStorage(recipe);
        ContentValues cvRecipe = new ContentValues();
        cvRecipe.put(RecipeDBEntry._ID, recipe.getId());
        cvRecipe.put(RecipeDBEntry.COLUMN_NAME, recipe.getName());
        cvRecipe.put(RecipeDBEntry.COLUMN_SERVINGS, recipe.getServings());
        cvRecipe.put(RecipeDBEntry.COLUMN_IMAGE, recipe.getImage());
        AsyncQueryHandler handler = new DBQueryHandler(this.mContext.getContentResolver(), this);
        handler.startInsert(0, recipe, RecipeDBEntry.CONTENT_URI, cvRecipe);
        for (Ingredient i : recipe.getIngredients()) {
            ContentValues cvIngredients = new ContentValues();
            cvIngredients.put(IngredientDBEntry.COLUMN_NAME, i.getName());
            cvIngredients.put(IngredientDBEntry.COLUMN_MEASURE, i.getMeasure());
            cvIngredients.put(IngredientDBEntry.COLUMN_QUANTITY, i.getQuantity());
            cvIngredients.put(IngredientDBEntry.COLUMN_RECIPE_ID, recipe.getId());
            handler.startInsert(0, null, IngredientDBEntry.CONTENT_URI, cvIngredients);
        }
        for (Step s : recipe.getSteps()) {
            ContentValues cvSteps = new ContentValues();
            cvSteps.put(StepDBEntry.COLUMN_ID, s.getId());
            cvSteps.put(StepDBEntry.COLUMN_SHORT_DESCRIPTION, s.getShortDescription());
            cvSteps.put(StepDBEntry.COLUMN_DESCRIPTION, s.getDescription());
            cvSteps.put(StepDBEntry.COLUMN_VIDEO_URL, s.getVideoUrl());
            cvSteps.put(StepDBEntry.COLUMN_THUMBNAIL_URL, s.getThumbnailUrl());
            cvSteps.put(StepDBEntry.COLUMN_RECIPE_ID, recipe.getId());
            handler.startInsert(0, null, StepDBEntry.CONTENT_URI, cvSteps);
        }
    }

    @Override
    public boolean isSavedLocally(long id) {
        Cursor cursor = this.mContext.getContentResolver()
                .query(RecipeDBEntry.getContentUriSingleRow(id),
                        null, null, null, null);
        Recipe result = null;
        if(cursor != null && cursor.moveToFirst()) {
            result = this.getOneRowFromCursor(cursor);
            cursor.close();
        }
        return result != null;
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String path = args.getString(PATH);
        return new RecipeLoaderTask(this.mContext, path);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try {
            List<Recipe> recipes = JsonUtils.convertToRecipeListObject(data);
            this.mRecipeListener.onRecipeSuccess(recipes);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    private boolean hasNetworkConnectivity() {
        ConnectivityManager cm =
                (ConnectivityManager)this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onQueryCompleted(Object object) {
        if(object != null) {
            if(object instanceof Recipe) {
                //insert, delete
                this.mRecipeListener.onRecipeSuccess(Collections.singletonList((Recipe) object));
                return;
            }
            if(object instanceof Cursor) {
                // query for one row
                Cursor cursor = (Cursor) object;
                Recipe recipe = null;
                if(cursor.moveToFirst()) {
                    recipe = this.getOneRowFromCursor(cursor);
                    cursor.close();
                }
                this.mRecipeListener.onRecipeSuccess(Collections.singletonList(recipe));
            }
        }
    }

    private void deleteRecipeFromLocalStorage(Recipe recipe) {
        AsyncQueryHandler handler = new DBQueryHandler(this.mContext.getContentResolver(), this);
        handler.startDelete(0, recipe, RecipeDBEntry.getContentUriSingleRow(recipe.getId()),
                null, null);
    }

    private Recipe getOneRowFromCursor(Cursor cursor) {
        Recipe recipe = CursorUtils.getRecipeFromCursor(cursor);
        List<Ingredient> ingredients = CursorUtils.getIngredientsFromCursor(cursor);
        List<Step> steps = CursorUtils.getStepsFromCursor(cursor);
        recipe.setIngredients(ingredients);
        recipe.setSteps(steps);
        return recipe;
    }
}