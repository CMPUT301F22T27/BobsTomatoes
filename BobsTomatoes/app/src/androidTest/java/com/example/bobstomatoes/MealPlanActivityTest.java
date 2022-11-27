package com.example.bobstomatoes;

import android.app.Activity;
import android.view.View;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.CoordinatesProvider;
import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Tap;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.hasToString;

import java.util.concurrent.TimeoutException;


/**
 * Test class for IngredientStorageActivity. All the UI tests are written here. Espresso test framework is used
 */
@RunWith(AndroidJUnit4.class)
public class MealPlanActivityTest {

    @Rule
    public ActivityScenarioRule<MealPlanActivity> rule =
            new ActivityScenarioRule<>(MealPlanActivity.class);


    @Test
    public void testAddMealPlan() throws InterruptedException {

        //Wait for data to load
        Thread.sleep(15000);

        //Add ingredient to db
        addTestIngredient();

        //Return to mealplan
        onView(withId(R.id.meal_plan_item))
                .perform(click());

        Thread.sleep(15000);

        deleteTestIngredient();

        onView(withId(R.id.meal_plan_item))
                .perform(click());

    }


    /**
     * Adds ingredient to DB from any activity
     * Expects current activity not running tasks
     * @throws InterruptedException
     */
    private void addTestIngredient() throws InterruptedException {

        onView(withId(R.id.ingredients_item))
                .perform(click());

        onView(withId(R.id.add_item))
                .perform(click());

        onView(withId(R.id.editTextIngredientDesc))
                .perform(click(), typeText("111TEST INGREDIENT"));

        onView(withId(R.id.radioButtonFreezer))
                .perform(click());

        onView(withId(R.id.editTextIngredientAmount))
                .perform(click(), typeText("1"));

        onView(withId(R.id.editTextIngredientUnit))
                .perform(click(), typeText("5"));

        onView(withId(R.id.radioButtonDairy))
                .perform(click());

        onView(withText("ADD"))
                .perform(click());

    }

    /**
     * Deletes ingredient from DB from any activity
     * Expects current activity not running tasks
     * @throws InterruptedException
     */
    private void deleteTestIngredient() throws InterruptedException {

        onView(withId(R.id.ingredients_item))
                .perform(click());

        Thread.sleep(8000);

        onView(withText("111TEST INGREDIENT"))
                .perform(click());

        onView(withText("DELETE"))
                .perform(click());

    }


    private void addTestRecipe() {


    }


    private void deleteTestRecipe() {

    }

    public static ViewAction clickXY(final int x, final int y){
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {

                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);

                        final float screenX = screenPos[0] + x;
                        final float screenY = screenPos[1] + y;
                        float[] coordinates = {screenX, screenY};

                        return coordinates;
                    }
                },
                Press.FINGER);
    }

    /**
     * Perform action of waiting for a specific view id.
     *
     * @param viewId The id of the view to wait for.
     * @param millis The timeout of until when to wait for.
     */
    public static ViewAction waitId(final int viewId, final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for a specific view with id <" + viewId + "> during " + millis + " millis.";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;
                final Matcher<View> viewMatcher = withId(viewId);

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            return;
                        }
                    }

                    uiController.loopMainThreadForAtLeast(50);
                }
                while (System.currentTimeMillis() < endTime);

                // timeout happens
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }
}
