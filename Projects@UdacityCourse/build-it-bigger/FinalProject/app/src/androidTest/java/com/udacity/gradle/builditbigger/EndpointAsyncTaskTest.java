package com.udacity.gradle.builditbigger;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class EndpointAsyncTaskTest {

    private static final String JOKE = "This should be a very funny joke!";

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void onClickBtnProvideJokeFromGCE_displaysJokeFromJokeProviderLib() {
        onView(withId(R.id.btn_provide_joke_from_gce)).perform(click());
        onView(withId(R.id.tv_joke_text)).check(matches(withText(JOKE)));
    }

}