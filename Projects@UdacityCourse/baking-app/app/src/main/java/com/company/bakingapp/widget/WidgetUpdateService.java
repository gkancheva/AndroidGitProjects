package com.company.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.company.bakingapp.models.Recipe;

public class WidgetUpdateService extends IntentService {

    public static final String ACTION_UPDATE_INGREDIENTS = "com.company.bakingapp.widget.action.update_ingredients";
    private static final String RECIPE = "RECIPE";

    public WidgetUpdateService() {
        super("WidgetUpdateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null) {
            String action = intent.getAction();
            if(ACTION_UPDATE_INGREDIENTS.equals(action)) {
                Recipe recipe = intent.getParcelableExtra(RECIPE);
                handleUpdateIngredients(recipe);
            }
        }
    }

    private void handleUpdateIngredients(Recipe recipe) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        RecipeWidgetProvider.updateRecipeWidgets(this, appWidgetManager, recipe, appWidgetIds);
    }

    public static void startUpdateWidgetService(Context context, Recipe recipe) {
        Intent intent = new Intent(context, WidgetUpdateService.class);
        intent.setAction(ACTION_UPDATE_INGREDIENTS);
        intent.putExtra(RECIPE, recipe);
        context.startService(intent);
    }
}
