package com.company.bakingapp.utils;

import android.content.Context;

import com.company.bakingapp.models.Ingredient;
import com.company.bakingapp.models.Recipe;
import com.company.bakingapp.models.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private JsonUtils(){}

    public static List<Recipe> convertToRecipeListObject(String result, Context ctx) throws JSONException, ParseException {
        List<Recipe> recipes = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(result);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject currentRecipe = jsonArray.getJSONObject(i);
            long recipeId = currentRecipe.getLong("id");
            String recipeName = currentRecipe.getString("name");
            JSONArray ingredientsArr = currentRecipe.getJSONArray("ingredients");
            List<Ingredient> ingredients = new ArrayList<>();
            for (int j = 0; j < ingredientsArr.length(); j++) {
                JSONObject currentIngredient = ingredientsArr.getJSONObject(j);
                double quantity = currentIngredient.getDouble("quantity");
                String measure = currentIngredient.getString("measure");
                String ingredientName = currentIngredient.getString("ingredient");
                ingredients.add(new Ingredient(quantity, measure, ingredientName));
            }
            JSONArray stepsArr = currentRecipe.getJSONArray("steps");
            List<Step> steps = new ArrayList<>();
            for (int j = 0; j < stepsArr.length(); j++) {
                JSONObject currentStep = stepsArr.getJSONObject(j);
                long id = currentStep.getLong("id");
                String shortDescription = currentStep.getString("shortDescription");
                String description = currentStep.getString("description");
                String videoUrl = currentStep.getString("videoURL");
                String thumbnailURL = currentStep.getString("thumbnailURL");
                steps.add(new Step(id, shortDescription, description, videoUrl, thumbnailURL));
            }
            int servings = currentRecipe.getInt("servings");
            String image = currentRecipe.getString("image");
            recipes.add(new Recipe(recipeId, recipeName, servings, image, ingredients, steps));
        }
        return recipes;
    }

}