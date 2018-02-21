package com.udacity.sandwichclub.utils;

import android.content.Context;

import com.udacity.sandwichclub.R;
import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json, Context ctx) throws JSONException {
        JSONObject mainJsonObj = new JSONObject(json);
        JSONObject nameObject = mainJsonObj.getJSONObject(ctx.getString(R.string.json_name));
        String mainName = nameObject.getString(ctx.getString(R.string.json_main_name));

        JSONArray alsoKnowArr = nameObject.getJSONArray(ctx.getString(R.string.json_also_known_as));
        List<String> alsoKnowNames = new ArrayList<>();
        for (int i = 0; i < alsoKnowArr.length(); i++) {
            String currentAlsoKnowStr = alsoKnowArr.getString(i);
            alsoKnowNames.add(currentAlsoKnowStr);
        }

        String placeOfOrigin = mainJsonObj.getString(ctx.getString(R.string.json_place_of_origin));
        String description = mainJsonObj.getString(ctx.getString(R.string.json_description));
        String imageUri = mainJsonObj.getString(ctx.getString(R.string.json_image));

        JSONArray ingredientsArr = mainJsonObj.getJSONArray(ctx.getString(R.string.json_ingredients));
        List<String> ingredients = new ArrayList<>();
        for (int i = 0; i < ingredientsArr.length(); i++) {
            String ingredient = ingredientsArr.getString(i);
            ingredients.add(ingredient);
        }
        return new Sandwich(mainName, alsoKnowNames, placeOfOrigin, description, imageUri, ingredients);
    }
}
