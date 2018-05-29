package com.company.bakingapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;

import com.company.bakingapp.models.Ingredient;
import com.company.bakingapp.models.Recipe;
import com.company.bakingapp.models.Step;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private static final String RECIPE_KEY = "RECIPE";
    private static final Recipe RECIPE = new Recipe(1, "Nutella Pie", 8, "",
            new ArrayList<Ingredient>(), new ArrayList<Step>());

    @Rule
    public IntentsTestRule<MainActivity> mActivityTestRule =
           new IntentsTestRule<>(MainActivity.class);

    //this code was taken from the following web blog: http://blog.xebia.com/android-intent-extras-espresso-rules/
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<MainActivity>(MainActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, MainActivity.class);
                    result.putExtra(RECIPE_KEY, RECIPE);
                    return result;
                }
            };

    @Test
    public void clickOnRecipeRVItem_OpensDetailsActivity() {
        onView(ViewMatchers.withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        intended(hasComponent(new ComponentName(getTargetContext(), DetailsActivity.class)));
    }

    @Test
    public void clickOnRecipeRVItem_checksIfDetailsActivityHasExtra() {
        onView(ViewMatchers.withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        intended(hasExtra(RECIPE_KEY, RECIPE));
    }

}