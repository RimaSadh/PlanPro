package com.example.planpro;

import com.example.planpro.project.ViewProject;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class DeleteProjectTest {

    @Rule
    public ActivityTestRule<ViewProject> activityTestRule = new ActivityTestRule<ViewProject>(ViewProject.class);
    private ViewProject viewProject = null;

    @Before
    public void setUp() throws Exception {
        viewProject = activityTestRule.getActivity();
//add tiemr
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check the activity visible
            onView(withId(R.id.ViewProject)).check(matches(isDisplayed()));
            //check text views visible
            //description
            onView(withId(R.id.description)).check(matches(isDisplayed()));
            //cost
            onView(withId(R.id.totalCost)).check(matches(isDisplayed()));
            //check the add button visible
            onView(withId(R.id.fab)).check(matches(isDisplayed()));
            //delete project
            onView(withId(R.id.Delete)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void DeleteAccountCancel () {
        //click the delete button
        onView(withId(R.id.Delete)).perform(click());
        //this line is to check when button clicked this dialog will apear
        onView(withText("Are you sure you want to delete this project ? "))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //click cancel button
        onView(withText("Cancel")).perform(click());
        //nothing will be happen
    }

    @Test
    public void DeleteAccountOK () {
        onView(withId(R.id.Delete)).perform(click());
        //this line is to check when button clicked this dialog will apear
        onView(withText("Are you sure you want to delete this project ? "))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //click ok button
        onView(withText("Delete")).perform(click());
        // check toast visibility
        onView(withText("Project deleted"))
                .inRoot(new ToastMatcher())
                .check(matches(withText("Project deleted")));
    }

    @After
    public void tearDown() throws Exception {
        viewProject = null;
    }
}