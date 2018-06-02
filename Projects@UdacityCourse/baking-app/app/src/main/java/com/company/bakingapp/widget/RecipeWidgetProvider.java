package com.company.bakingapp.widget;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.widget.RemoteViews;

import com.company.bakingapp.DetailsActivity;
import com.company.bakingapp.MainActivity;
import com.company.bakingapp.R;
import com.company.bakingapp.data.IngredientDBEntry;
import com.company.bakingapp.data.RecipeDBEntry;
import com.company.bakingapp.data.StepDBEntry;
import com.company.bakingapp.models.Ingredient;
import com.company.bakingapp.models.Recipe;
import com.company.bakingapp.models.Step;
import com.company.bakingapp.utils.CursorUtils;

import java.util.List;

public class RecipeWidgetProvider extends AppWidgetProvider {

    private static final String RECIPE = "RECIPE";
    private static final String WHERE_CLAUSE = "recipe_id = ?";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Cursor cursor = context.getContentResolver()
                .query(RecipeDBEntry.CONTENT_URI, null, null, null, null);
        Recipe recipe = CursorUtils.getRecipeFromCursor(cursor);
        if(recipe != null) {
            cursor = context.getContentResolver()
                    .query(IngredientDBEntry.CONTENT_URI, null,
                            WHERE_CLAUSE,
                            new String[]{String.valueOf(recipe.getId())},
                            null);
            List<Ingredient> ingredients = CursorUtils.getIngredientsFromCursor(cursor);
            recipe.setIngredients(ingredients);
            cursor = context.getContentResolver()
                    .query(StepDBEntry.CONTENT_URI, null,
                            WHERE_CLAUSE,
                            new String[]{String.valueOf(recipe.getId())},
                            null);
            List<Step> steps = CursorUtils.getStepsFromCursor(cursor);
            recipe.setSteps(steps);
            WidgetUpdateService.startUpdateWidgetService(context, recipe);
        }
        updateRecipeWidgets(context, appWidgetManager, recipe, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager, Recipe recipe, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Intent intent;
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
            if(recipe == null) {
                intent = new Intent(context, MainActivity.class);
                rv.setTextViewText(R.id.recipe_title, context.getString(R.string.no_info_to_show));
            } else {
                intent = new Intent(context, DetailsActivity.class);
                intent.putExtra(RECIPE, recipe);
                rv.setTextViewText(R.id.recipe_title, recipe.getName());
                rv.setTextViewText(R.id.recipe_ingredients, recipe.getIngredientsAsText());
            }
            intent.setAction(WidgetUpdateService.ACTION_UPDATE_INGREDIENTS);
            PendingIntent piDetails = PendingIntent
                    .getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            rv.setOnClickPendingIntent(R.id.recipe_ingredients, piDetails);
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
    }
}