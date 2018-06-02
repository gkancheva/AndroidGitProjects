package com.company.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.company.bakingapp.models.Recipe;
import com.company.bakingapp.models.Step;
import com.company.bakingapp.repository.RecipeRepo;
import com.company.bakingapp.repository.RecipeRepoImpl;
import com.company.bakingapp.services.RecipeRepoListener;
import com.company.bakingapp.widget.WidgetUpdateService;

import java.util.List;

public class DetailsActivity extends AppCompatActivity
    implements FragmentDetailsList.OnStepSelectedListener, RecipeRepoListener {

    private static final String RECIPE = "RECIPE";
    private static final String STEP_INDEX = "STEP_INDEX";
    private Recipe mRecipe;
    private RecipeRepo mRecipeRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        this.mRecipeRepo = new RecipeRepoImpl(this, getSupportLoaderManager(), this);
        if(getIntent() == null) {
            this.closeOnError();
            return;
        }

        this.mRecipe = getIntent().getParcelableExtra(RECIPE);
        if(this.mRecipe == null){
            this.closeOnError();
            return;
        }
        this.populateUi(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_show_cook_today) {
            if(this.mRecipeRepo.isSavedLocally(this.mRecipe.getId())) {
                Toast.makeText(this, R.string.error_adding_recipe_to_cooking_list, Toast.LENGTH_SHORT).show();
                return true;
            }
            this.mRecipeRepo.saveRecipeLocally(this.mRecipe);
            WidgetUpdateService.startUpdateWidgetService(this, this.mRecipe);
        } else if(item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStepSelected(Step step) {
        FragmentDetailView fragmentDetailView = new FragmentDetailView();
        if(findViewById(R.id.fragment_details_view) == null) {
            Bundle args = new Bundle();
            args.putParcelable(RECIPE, this.mRecipe);
            args.putInt(STEP_INDEX, this.getStepIndex(step));
            fragmentDetailView.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragmentDetailView)
                    .addToBackStack(null)
                    .commit();
        } else {
            fragmentDetailView = (FragmentDetailView) getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_details_view);
            fragmentDetailView.setContent(step);
        }
    }

    private int getStepIndex(Step step) {
        for (int i = 0; i < this.mRecipe.getSteps().size(); i++) {
            if(step.getId() == this.mRecipe.getSteps().get(i).getId()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onRecipeSuccess(List<Recipe> recipes) {
        Toast.makeText(this, R.string.ingredients_added_widget, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecipeFailure(String message) {
        Toast.makeText(this, R.string.error_recipe_details, Toast.LENGTH_SHORT).show();
    }

    private void populateUi(Bundle savedInstanceState) {
        setTitle(this.mRecipe.getName());
        if(savedInstanceState == null) {
            FragmentDetailsList fragmentDetailsList = new FragmentDetailsList();
            if(findViewById(R.id.fragment_details_view) == null) {
                Bundle args = new Bundle();
                args.putParcelable(RECIPE, this.mRecipe);
                fragmentDetailsList.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragmentDetailsList)
                        .commit();
            } else {
                fragmentDetailsList = (FragmentDetailsList) getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_details_list);
                fragmentDetailsList.setContent(this.mRecipe);
                FragmentDetailView frDetail = (FragmentDetailView) getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_details_view);
                frDetail.setContent(this.mRecipe.getSteps().get(0));
            }
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.error_recipe_details, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        FragmentDetailView fdv = (FragmentDetailView) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_details_view);
        if(!this.getResources().getBoolean(R.bool.isTablet)) {
            getSupportFragmentManager().popBackStackImmediate();
            return;
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
