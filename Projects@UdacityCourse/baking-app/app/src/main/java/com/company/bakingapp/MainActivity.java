package com.company.bakingapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.company.bakingapp.models.Recipe;
import com.company.bakingapp.repository.RecipeRepo;
import com.company.bakingapp.repository.RecipeRepoImpl;
import com.company.bakingapp.services.RecipeClickListener;
import com.company.bakingapp.services.RecipeRepoListener;
import com.company.bakingapp.views.RecipesRVAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements RecipeRepoListener, RecipeClickListener {

    private static final String RECIPE = "RECIPE";
    private static final int NB_COLUMNS_LAND_MODE = 3;
    private static final int NB_COLUMNS_PORTRAIT = 1;
    private RecipeRepo mRecipeRepo;
    private RecipesRVAdapter mRecipesAdapter;
    @BindView(R.id.rv_recipes) RecyclerView mRVRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        this.setRecyclerView();
        this.mRecipeRepo = new RecipeRepoImpl(this, getSupportLoaderManager(), this);
        this.mRecipeRepo.getRecipes();
    }

    @Override
    public void onRecipeSuccess(List<Recipe> recipes) {
        this.mRecipesAdapter.updateRecipeList(recipes);
    }

    @Override
    public void onRecipeFailure(String message) {
        Toast.makeText(this, R.string.recipe_load_failure, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecipeSelected(Recipe recipe) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(RECIPE, recipe);
        startActivity(intent);
    }

    private void setRecyclerView() {
        if(getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            this.mRVRecipes.setLayoutManager(new GridLayoutManager(this, NB_COLUMNS_LAND_MODE));
        } else {
            this.mRVRecipes.setLayoutManager(new GridLayoutManager(this, NB_COLUMNS_PORTRAIT));
        }
        this.mRecipesAdapter = new RecipesRVAdapter(this, this);
        this.mRVRecipes.setAdapter(this.mRecipesAdapter);
    }
}
