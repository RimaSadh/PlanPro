package com.example.planpro;

import com.example.planpro.project.ViewProject;

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
public class ViewProjectTest {

    @Rule
    public ActivityTestRule<ViewProject> activityTestRule = new ActivityTestRule<ViewProject>(ViewProject.class);
    private ViewProject viewProject = null;

    @Before
    public void setUp() throws Exception {
        viewProject = activityTestRule.getActivity();

        //check the activity visible
        onView(withId(R.id.ViewProject)).check(matches(isDisplayed()));
        //check text views visible
        //description
        onView(withId(R.id.description)).check(matches(isDisplayed()));
        //date
        onView(withId(R.id.startDate)).check(matches(isDisplayed()));
        onView(withId(R.id.endDate)).check(matches(isDisplayed()));
        //cost
        onView(withId(R.id.totalCost)).check(matches(isDisplayed()));
        //check the add button visible
        onView(withId(R.id.fab)).check(matches(isDisplayed()));
    }

    //TODO check for recycler view

    @Test
    public void AddTask () {
        //check when click on add button
        onView(withId(R.id.fab)).perform(click());
        //the add task page appear
        onView(withId(R.id.AddTask)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        viewProject = null;
    }
}