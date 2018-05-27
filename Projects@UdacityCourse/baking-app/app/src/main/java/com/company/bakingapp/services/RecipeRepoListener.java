package com.company.bakingapp.services;

import com.company.bakingapp.models.Recipe;

import java.util.List;

public interface RecipeRepoListener {
    void onRecipeSuccess(List<Recipe> recipes);
    void onRecipeFailure(String message);
}