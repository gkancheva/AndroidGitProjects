package com.company.bakingapp.utils;

import android.database.Cursor;

import com.company.bakingapp.data.IngredientDBEntry;
import com.company.bakingapp.data.RecipeDBEntry;
import com.company.bakingapp.data.StepDBEntry;
import com.company.bakingapp.models.Ingredient;
import com.company.bakingapp.models.Recipe;
import com.company.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

public class CursorUtils {

    private CursorUtils() {
    }

    public static Recipe getRecipeFromCursor(Cursor c) {
        Recipe recipe = null;
        if(c != null && c.moveToFirst()) {
            long id = c.getLong(c.getColumnIndex(RecipeDBEntry._ID));
            String name = c.getString(c.getColumnIndex(RecipeDBEntry.COLUMN_NAME));
            String image = c.getString(c.getColumnIndex(RecipeDBEntry.COLUMN_IMAGE));
            int servings = c.getInt(c.getColumnIndex(RecipeDBEntry.COLUMN_SERVINGS));
            recipe = new Recipe(id, image, servings, name, null, null);
            recipe.setId(id);
            recipe.setName(name);
            recipe.setImage(image);
            recipe.setServings(servings);
        }
        return recipe;
    }

    public static List<Ingredient> getIngredientsFromCursor(Cursor c) {
        List<Ingredient> ingredients = new ArrayList<>();
        while (c.moveToNext()) {
            long id = c.getLong(c.getColumnIndex(IngredientDBEntry._ID));
            String name = c.getString(c.getColumnIndex(IngredientDBEntry.COLUMN_NAME));
            String measure = c.getString(c.getColumnIndex(IngredientDBEntry.COLUMN_MEASURE));
            double quantity = c.getDouble(c.getColumnIndex(IngredientDBEntry.COLUMN_QUANTITY));
            ingredients.add(new Ingredient(id, quantity, measure, name));
        }
        return ingredients;
    }

    public static List<Step> getStepsFromCursor(Cursor c) {
        List<Step> steps = new ArrayList<>();
        while (c.moveToNext()) {
            long id = c.getLong(c.getColumnIndex(StepDBEntry.COLUMN_ID));
            String shortDescription = c.getString(c.getColumnIndex(StepDBEntry.COLUMN_SHORT_DESCRIPTION));
            String description = c.getString(c.getColumnIndex(StepDBEntry.COLUMN_DESCRIPTION));
            String thumbnailUrl = c.getString(c.getColumnIndex(StepDBEntry.COLUMN_THUMBNAIL_URL));
            String videoUrl = c.getString(c.getColumnIndex(StepDBEntry.COLUMN_VIDEO_URL));
            steps.add(new Step(id, shortDescription, description, videoUrl, thumbnailUrl));
        }
        return steps;
    }
}