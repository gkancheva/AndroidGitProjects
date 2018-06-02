package com.company.bakingapp.data;

import android.net.Uri;

public class DBContract {
    static final String AUTHORITY = "com.company.bakingapp";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    static final String PATH_RECIPES = "recipes";
    static final String PATH_SINGLE_RECIPE = "recipes/#";
    static final String PATH_INGREDIENTS = "ingredients";
    static final String PATH_SINGLE_INGREDIENT = "ingredients/#";
    static final String PATH_STEPS = "steps";
    static final String PATH_SINGLE_STEP = "steps/#";
}