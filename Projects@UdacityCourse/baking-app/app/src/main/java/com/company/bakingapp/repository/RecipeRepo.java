package com.company.bakingapp.repository;

import com.company.bakingapp.models.Recipe;

public interface RecipeRepo {
    void getRecipes();
    void saveRecipeLocally(Recipe recipe);
    boolean isSavedLocally(long id);
}