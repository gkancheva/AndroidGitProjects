package com.company.bakingapp.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.company.bakingapp.R;
import com.company.bakingapp.models.Recipe;
import com.company.bakingapp.services.RecipeRepoListener;
import com.company.bakingapp.services.RecipeLoaderTask;
import com.company.bakingapp.utils.JsonUtils;

import org.json.JSONException;

import java.text.ParseException;
import java.util.List;

public class RecipeRepoImpl implements RecipeRepo,
    LoaderManager.LoaderCallbacks<String> {

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

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String path = args.getString(PATH);
        return new RecipeLoaderTask(this.mContext, path);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try {
            List<Recipe> recipes = JsonUtils.convertToRecipeListObject(data, this.mContext);
            this.mRecipeListener.onRecipeSuccess(recipes);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
            // TODO: 5/2/2018
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
}