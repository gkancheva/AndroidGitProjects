package com.company.bakingapp.utils;

import com.company.bakingapp.models.Ingredient;
import com.company.bakingapp.models.Recipe;
import com.company.bakingapp.models.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String JSON_KEY_ID = "id";
    private static final String JSON_KEY_NAME = "name";
    private static final String JSON_KEY_INGREDIENTS = "ingredients";
    private static final String JSON_KEY_QUANTITY = "quantity";
    private static final String JSON_KEY_MEASURE = "measure";
    private static final String JSON_KEY_INGREDIENT = "ingredient";
    private static final String JSON_KEY_STEPS = "steps";
    private static final String JSON_SHORT_DESCRIPTION = "shortDescription";
    private static final String JSON_KEY_DESCRIPTION = "description";
    private static final String JSON_KEY_VIDEO_URL = "videoURL";
    private static final String JSON_KEY_THUMBNAIL_URL = "thumbnailURL";
    private static final String JSON_KEY_SERVINGS = "servings";
    private static final String JSON_KEY_IMAGE = "image";

    private JsonUtils(){}

    public static List<Recipe> convertToRecipeListObject(String result) throws JSONException {
        List<Recipe> recipes = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(result);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject currentRecipe = jsonArray.getJSONObject(i);
            long recipeId = currentRecipe.getLong(JSON_KEY_ID);
            String recipeName = currentRecipe.getString(JSON_KEY_NAME);
            JSONArray ingredientsArr = currentRecipe.getJSONArray(JSON_KEY_INGREDIENTS);
            List<Ingredient> ingredients = new ArrayList<>();
            for (int j = 0; j < ingredientsArr.length(); j++) {
                JSONObject currentIngredient = ingredientsArr.getJSONObject(j);
                double quantity = currentIngredient.getDouble(JSON_KEY_QUANTITY);
                String measure = currentIngredient.getString(JSON_KEY_MEASURE);
                String ingredientName = currentIngredient.getString(JSON_KEY_INGREDIENT);
                ingredients.add(new Ingredient(quantity, measure, ingredientName));
            }
            JSONArray stepsArr = currentRecipe.getJSONArray(JSON_KEY_STEPS);
            List<Step> steps = new ArrayList<>();
            for (int j = 0; j < stepsArr.length(); j++) {
                JSONObject currentStep = stepsArr.getJSONObject(j);
                long id = currentStep.getLong(JSON_KEY_ID);
                String shortDescription = currentStep.getString(JSON_SHORT_DESCRIPTION);
                String description = currentStep.getString(JSON_KEY_DESCRIPTION);
                String videoUrl = currentStep.getString(JSON_KEY_VIDEO_URL);
                String thumbnailURL = currentStep.getString(JSON_KEY_THUMBNAIL_URL);
                steps.add(new Step(id, shortDescription, description, videoUrl, thumbnailURL));
            }
            int servings = currentRecipe.getInt(JSON_KEY_SERVINGS);
            String image = currentRecipe.getString(JSON_KEY_IMAGE);
            recipes.add(new Recipe(recipeId, recipeName, servings, image, ingredients, steps));
        }
        return recipes;
    }

}