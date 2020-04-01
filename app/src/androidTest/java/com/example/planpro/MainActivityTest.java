package com.example.planpro;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.runner.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mainActivity = null;

    @Before
    public void setUp() throws Exception {
        mainActivity = activityTestRule.getActivity();

        //check the activity is visible
        onView(withId(R.id.MainActivity)).check(matches(isDisplayed()));

        //check the button add project visible
        onView(withId(R.id.fab)).check(matches(isDisplayed()));
    }

    //TODO make test for recycler view

    @Test
    public void AddProject () {
        //check when click to add button
        onView(withId(R.id.fab)).perform(click());
        //check the add project page appear
        onView(withId(R.id.AddProject)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        mainActivity = null;
    }
}