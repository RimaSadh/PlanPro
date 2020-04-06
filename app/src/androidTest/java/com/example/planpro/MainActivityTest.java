package com.example.planpro;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static com.example.planpro.TestUtils.withRecyclerView;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mainActivity = null;

    @Before
    public void setUp() throws Exception {
        //add timer
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(10000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            mainActivity = activityTestRule.getActivity();

            //check the activity is visible
            onView(withId(R.id.MainActivity)).check(matches(isDisplayed()));

            //check the button add project visible
            onView(withId(R.id.fab)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }


    //TODO make test for recycler view
    @Test
    public void testItemClick() {

        onView(withRecyclerView(R.id.ProjectRV).atPosition(1)).perform(click());
        //add tiemr
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(3000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(3000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(3000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //activity
            onView(withId(R.id.ViewProject)).check(matches(isDisplayed()));
            //proj name
            onView(withId(R.id.projectName)).check(matches(isDisplayed()));
            //prj des
            onView(withId(R.id.description)).check(matches(isDisplayed()));
            //cost
            onView(withId(R.id.totalCost)).check(matches(isDisplayed()));
            //buttons
            onView(withId(R.id.Delete)).check(matches(isDisplayed()));
            onView(withId(R.id.fab)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

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