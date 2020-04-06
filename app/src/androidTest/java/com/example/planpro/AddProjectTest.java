package com.example.planpro;

import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.planpro.project.AddProject;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class AddProjectTest {

    @Rule
    public ActivityTestRule<AddProject> activityTestRule = new ActivityTestRule<AddProject>(AddProject.class);
    private AddProject addprj = null;

    @Before
    public void setUp() throws Exception {
        //add timer for all tests
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            addprj = activityTestRule.getActivity();
            //check the activity is visible
            onView(withId(R.id.AddProject)).check(matches(isDisplayed()));
            //check edit texts visible
            //name
            onView(withId(R.id.nameET)).check(matches(isDisplayed()));
            //description
            onView(withId(R.id.description)).check(matches(isDisplayed()));
            //check buttons visible
            //Date
            onView(withId(R.id.start)).check(matches(isDisplayed()));
            onView(withId(R.id.end)).check(matches(isDisplayed()));
            //confirm
            onView(withId(R.id.button)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void EmptyFields () {
        //leave all fields empty and Date buttons not clicked
        //name
        onView(withId(R.id.nameET)).perform(typeText(""));
        //description
        onView(withId(R.id.description)).perform(typeText(""));
        //date button
        onView(withId(R.id.start)).check(matches(withText("Set Date")));
        onView(withId(R.id.end)).check(matches(withText("Set Date")));
        //click confirm button
        onView(withId(R.id.button)).perform(click());
        // check toast visibility
        //for resources fields
        onView(withText("All Fields Required"))
                .inRoot(new ToastMatcher())
                .check(matches(withText("All Fields Required")));
    }

    @Test
    public void EndBeforeStart () {
        //enter name of project
        onView(withId(R.id.nameET)).perform(typeText("GP"));
        //close the keyboard
        closeSoftKeyboard();
        //description
        onView(withId(R.id.description)).perform(typeText("This Project Of Graduation Project For Second Term"));
        closeSoftKeyboard();
        // check when click the date and time button
        onView(withId(R.id.start)).perform(click());
        //choose date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 8, 2));
        //click ok dialog button
        onView(withText("OK")).perform(click());
        // button clicked
        onView(withId(R.id.end)).perform(click());
        //choose date before start date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 7, 2));
        //click ok button
        onView(withText("OK")).perform(click());
        //click confirm button
        onView(withId(R.id.button)).perform(click());
        // check toast visibility
        onView(withText("The End Day must be after or same as Start Day"))
                .inRoot(new ToastMatcher())
                .check(matches(withText("The End Day must be after or same as Start Day")));
    }

    @Test
    public void AdddPrjSucess () {
        //enter name of project
        onView(withId(R.id.nameET)).perform(typeText("GP"));
        //close the keyboard
        closeSoftKeyboard();
        //description
        onView(withId(R.id.description)).perform(typeText("\nThis Project Of Graduation Project For Second Term\n"));
        closeSoftKeyboard();
        // check when click the date and time button
        onView(withId(R.id.start)).perform(click());
        //choose date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 8, 2));
        //click ok dialog button
        onView(withText("OK")).perform(click());
        // button clicked
        onView(withId(R.id.end)).perform(click());
        //choose date before start date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 10, 2));
        //click ok button
        onView(withText("OK")).perform(click());
        //click confirm button
        onView(withId(R.id.button)).perform(click());
        // check toast visibility
        onView(withText("Project added successfully"))
                .inRoot(new ToastMatcher())
                .check(matches(withText("Project added successfully")));
    }

    @After
    public void tearDown() throws Exception {
        addprj = null;
    }
}