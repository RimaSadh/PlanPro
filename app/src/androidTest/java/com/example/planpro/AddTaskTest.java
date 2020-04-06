package com.example.planpro;

import android.content.Intent;
import android.widget.DatePicker;

import com.example.planpro.project.task.AddTask;

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
import androidx.test.runner.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.doubleClick;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AddTaskTest {

    @Rule
    public ActivityTestRule<AddTask> activityTestRule = new ActivityTestRule<AddTask>(AddTask.class);
    private AddTask addTask = null;

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
           // addTask = activityTestRule.getActivity();
            activityTestRule.launchActivity(new Intent());
            //check the activity is visible
            onView(withId(R.id.AddTask)).check(matches(isDisplayed()));
            //check edit texts visible
            //name
            onView(withId(R.id.TaskName)).check(matches(isDisplayed()));
            //Resourses Info
            onView(withId(R.id.Resources)).check(matches(isDisplayed()));
            onView(withId(R.id.ResourceCost)).check(matches(isDisplayed()));
            //cost
            onView(withId(R.id.TaskCost)).check(matches(isDisplayed()));
            //check buttons visible
            //Date
            onView(withId(R.id.StartButton)).check(matches(isDisplayed()));
            onView(withId(R.id.EndButton)).check(matches(isDisplayed()));
            //Resources
            onView(withId(R.id.AddMoreRe)).check(matches(isDisplayed()));
            onView(withId(R.id.DeleteLR)).check(matches(isDisplayed()));
            //confirm
            onView(withId(R.id.Create)).check(matches(isDisplayed()));
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
        onView(withId(R.id.TaskName)).perform(typeText(""));
        //Resourses Info
        onView(withId(R.id.Resources)).perform(typeText(""));
        onView(withId(R.id.ResourceCost)).perform(typeText(""));
        //cost
        onView(withId(R.id.TaskCost)).perform(typeText(""));
        //click confirm button
        onView(withId(R.id.Create)).perform(click());
        // check toast visibility
        //for resources fields
        onView(withText("All Resources Fields Required"))
                .inRoot(new ToastMatcher())
                .check(matches(withText("All Resources Fields Required")));
        //for task fields
        /*onView(withText("All Fields Required"))
                .inRoot(new ToastMatcher())
                .check(matches(withText("All Fields Required")));*/
    }

    @Test
    public void ResEmptyFields () {
        //leave all fields empty and Date buttons not clicked
        //name
        onView(withId(R.id.TaskName)).perform(typeText("Controlling"));
        //close keyboard
        closeSoftKeyboard();
        //select date for start and end
        //click the start button
        onView(withId(R.id.StartButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 9, 24));
        onView(withText("OK")).perform(click());
        //click the end button
        onView(withId(R.id.EndButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2021, 4, 24));
        onView(withText("OK")).perform(click());
        //Resourses Info
        onView(withId(R.id.Resources)).perform(typeText(""));
        onView(withId(R.id.ResourceCost)).perform(typeText(""));
        //cost
        onView(withId(R.id.TaskCost)).perform(typeText("200"));
        //close keyboard
        closeSoftKeyboard();
        //click confirm button
        onView(withId(R.id.Create)).perform(click());
        // check toast visibility
        //for resources fields
        onView(withText("All Resources Fields Required"))
                .inRoot(new ToastMatcher())
                .check(matches(withText("All Resources Fields Required")));
    }

    @Test
    public void StartTaskBeofreStartPRJ () {
        //leave all fields empty and Date buttons not clicked
        //name
        onView(withId(R.id.TaskName)).perform(typeText("Controlling"));
        //close keyboard
        closeSoftKeyboard();
        //select date for start and end
        //click the start button
        onView(withId(R.id.StartButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 7, 21));
        onView(withText("OK")).perform(click());
        //click the end button
        onView(withId(R.id.EndButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 9, 13));
        onView(withText("OK")).perform(click());
        //Resourses Info
        onView(withId(R.id.Resources)).perform(typeText("Member"));
        //close keyboard
        closeSoftKeyboard();
        onView(withId(R.id.ResourceCost)).perform(typeText("30"));
        //close keyboard
        closeSoftKeyboard();
        //cost
        onView(withId(R.id.TaskCost)).perform(typeText("200"));
        //close keyboard
        closeSoftKeyboard();
            //click confirm button
            onView(withId(R.id.Create)).perform(click());
            // check toast visibility
            //for resources fields
            onView(withText("The Chosen Start Date Must Not Before Project Date"))
                    .inRoot(new ToastMatcher())
                    .check(matches(withText("The Chosen Start Date Must Not Before Project Date")));
    }


    @Test
    public void EndBeofreStart () {
        //leave all fields empty and Date buttons not clicked
        //name
        onView(withId(R.id.TaskName)).perform(typeText("Controlling"));
        //close keyboard
        closeSoftKeyboard();
        //select date for start and end
        //click the start button
        onView(withId(R.id.StartButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 8, 24));
        onView(withText("OK")).perform(click());
        //click the end button
        onView(withId(R.id.EndButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 8, 14));
        onView(withText("OK")).perform(click());
        //Resourses Info
        onView(withId(R.id.Resources)).perform(typeText("Member"));
        //close keyboard
        closeSoftKeyboard();
        onView(withId(R.id.ResourceCost)).perform(typeText("30"));
        //close keyboard
        closeSoftKeyboard();
        //cost
        onView(withId(R.id.TaskCost)).perform(typeText("200"));
        //close keyboard
        closeSoftKeyboard();
        //click confirm button
        onView(withId(R.id.Create)).perform(click());
            // check toast visibility
            //for resources fields
            onView(withText("The End Day must be after or same as Start Day"))
                    .inRoot(new ToastMatcher())
                    .check(matches(withText("The End Day must be after or same as Start Day")));
    }

    @Test
    public void MoreRes () {
        //leave all fields empty and Date buttons not clicked
        //name
        onView(withId(R.id.TaskName)).perform(typeText("Controlling"));
        //close keyboard
        closeSoftKeyboard();
        //select date for start and end
        //click the start button
        onView(withId(R.id.StartButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 8, 24));
        onView(withText("OK")).perform(click());
        //click the end button
        onView(withId(R.id.EndButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2021, 4, 24));
        onView(withText("OK")).perform(click());
        //Resourses Info
        onView(withId(R.id.Resources)).perform(typeText("Member"));
        //close keyboard
        closeSoftKeyboard();
        onView(withId(R.id.ResourceCost)).perform(typeText("30"));
        //close keyboard
        closeSoftKeyboard();
        //click add more
        onView(withId(R.id.AddMoreRe)).perform(click());
        //leave the resources edit text empty

        //cost
        onView(withId(R.id.TaskCost)).perform(typeText("200"));
        //close keyboard
        closeSoftKeyboard();
        //click confirm button
        onView(withId(R.id.Create)).perform(click());
        // check toast visibility
        //for resources fields
        onView(withText("All Resources Fields Required"))
                .inRoot(new ToastMatcher())
                .check(matches(withText("All Resources Fields Required")));
    }

    @Test
    public void DeleteLastRes () {
        //leave all fields empty and Date buttons not clicked
        //name
        onView(withId(R.id.TaskName)).perform(typeText("Controlling"));
        //close keyboard
        closeSoftKeyboard();
        //select date for start and end
        //click the start button
        onView(withId(R.id.StartButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2021, 3, 24));
        onView(withText("OK")).perform(click());
        //click the end button
        onView(withId(R.id.EndButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2021, 4, 24));
        onView(withText("OK")).perform(click());
        //Resourses Info
        onView(withId(R.id.Resources)).perform(typeText("Member"));
        //close keyboard
        closeSoftKeyboard();
        onView(withId(R.id.ResourceCost)).perform(typeText("30"));
        //close keyboard
        closeSoftKeyboard();
        //click add more
        onView(withId(R.id.AddMoreRe)).perform(click());
        //leave the resources edit text empty
        //click delete button to remove the last one
        onView(withId(R.id.DeleteLR)).perform(click());
        //check the root linear layout contain one children
        onView(withId(R.id.Rlinear)).check(matches(hasChildCount(1)));
    }

    @Test
    public void FinshTaskAfterPRJ () {
        //leave all fields empty and Date buttons not clicked
        //name
        onView(withId(R.id.TaskName)).perform(typeText("Controlling"));
        //close keyboard
        closeSoftKeyboard();
        //select date for start and end
        //click the start button
        onView(withId(R.id.StartButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 8, 21));
        onView(withText("OK")).perform(click());
        //click the end button
        onView(withId(R.id.EndButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2021, 9, 13));
        onView(withText("OK")).perform(click());
        //Resourses Info
        onView(withId(R.id.Resources)).perform(typeText("Member"));
        //close keyboard
        closeSoftKeyboard();
        onView(withId(R.id.ResourceCost)).perform(typeText("30"));
        //close keyboard
        closeSoftKeyboard();
        //cost
        onView(withId(R.id.TaskCost)).perform(typeText("200"));
        //close keyboard
        closeSoftKeyboard();
        //click confirm button
        onView(withId(R.id.Create)).perform(click());
            // check toast visibility
            //for resources fields
            onView(withText("Task Added Successfully"))
                    .inRoot(new ToastMatcher())
                    .check(matches(withText("Task Added Successfully")));
    }

    @Test
    public void AddTaskSuccessOneRes () {
        //leave all fields empty and Date buttons not clicked
        //name
        onView(withId(R.id.TaskName)).perform(typeText("Controlling"));
        //close keyboard
        closeSoftKeyboard();
        //select date for start and end
        //click the start button
        onView(withId(R.id.StartButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 8, 24));
        onView(withText("OK")).perform(click());
        //click the end button
        onView(withId(R.id.EndButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 9, 24));
        onView(withText("OK")).perform(click());
        //Resourses Info
        onView(withId(R.id.Resources)).perform(typeText("Member"));
        //close keyboard
        closeSoftKeyboard();
        onView(withId(R.id.ResourceCost)).perform(typeText("30"));
        //close keyboard
        closeSoftKeyboard();
        //cost
        onView(withId(R.id.TaskCost)).perform(typeText("200"));
        //close keyboard
        closeSoftKeyboard();
        //click confirm button
        onView(withId(R.id.Create)).perform(click());
            // check toast visibility
            //for resources fields
            onView(withText("Task Added Successfully"))
                    .inRoot(new ToastMatcher())
                    .check(matches(withText("Task Added Successfully")));
    }

    @Test
    public void AddTaskSuccessMoreRes () {
        //leave all fields empty and Date buttons not clicked
        //name
        onView(withId(R.id.TaskName)).perform(typeText("Controlling"));
        //close keyboard
        closeSoftKeyboard();
        //select date for start and end
        //click the start button
        onView(withId(R.id.StartButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 8, 11));
        onView(withText("OK")).perform(click());
        //click the end button
        onView(withId(R.id.EndButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 8, 29));
        onView(withText("OK")).perform(click());
        //Resourses Info
        onView(withId(R.id.Resources)).perform(typeText("Member"));
        //close keyboard
        closeSoftKeyboard();
        onView(withId(R.id.ResourceCost)).perform(typeText("30"));
        //close keyboard
        closeSoftKeyboard();
        //click add more
        onView(withId(R.id.AddMoreRe)).perform(click());
        //Resourses Info
        onView(withHint("Resource Name2")).perform(typeText("PC"));
        //close keyboard
        closeSoftKeyboard();
        onView(withHint("Resource Cost2")).perform(typeText("3000"));
        //close keyboard
        closeSoftKeyboard();
        //click add more
        onView(withId(R.id.AddMoreRe)).perform(click());
        //Resourses Info
        onView(withHint("Resource Name3")).perform(typeText("Server"));
        //close keyboard
        closeSoftKeyboard();
        onView(withHint("Resource Cost3")).perform(typeText("10000"));
        //close keyboard
        closeSoftKeyboard();
        //cost
        onView(withId(R.id.TaskCost)).perform(typeText("200"));
        //close keyboard
        closeSoftKeyboard();
        //click confirm button
        onView(withId(R.id.Create)).perform(click());
            // check toast visibility
            //for resources fields
            onView(withText("Task Added Successfully"))
                    .inRoot(new ToastMatcher())
                    .check(matches(withText("Task Added Successfully")));
    }

    @After
    public void tearDown() throws Exception {
        addTask = null;
    }
}