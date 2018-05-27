package com.company.bakingapp.services;

import com.company.bakingapp.models.Recipe;

public interface RecipeClickListener {
    void onRecipeSelected(Recipe recipe);
}