package com.company.bakingapp.utils;

import com.company.bakingapp.R;

public class RecipeImageUtils {

    private RecipeImageUtils() {
    }

    public static int getImageResource(String name) {
        // TODO: 5/13/2018
        switch (name) {
            case "Nutella Pie": return R.drawable.dessert_vector;
            case "Brownies": return R.drawable.dessert_vector;
            case "Yellow Cake": return R.drawable.dessert_vector;
            case "Cheesecake": return R.drawable.dessert_vector;
        }
        return R.drawable.dessert_vector;
    }
}