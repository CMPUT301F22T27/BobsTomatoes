package com.example.bobstomatoes;

import android.app.Activity;
import android.app.Instrumentation;
import android.util.Log;
import android.view.View;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.google.common.base.Preconditions.checkNotNull;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.CoordinatesProvider;
import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Tap;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

import java.util.concurrent.TimeoutException;

/**
 * Test class for ShoppingListActivity. All the UI tests are written here. Espresso test framework is used
 * Assumes IngredientStorageActivity, RecipeActivity, and MealPlanActivity are working
 */
@RunWith(AndroidJUnit4.class)
public class ShoppingListActivityTest {

    private String CAMERA_BUTTON_SHUTTER_ACTION_ID = "com.android.camera2:id/shutter_button";
    private String CAMERA_BUTTON_DONE_ACTION_ID = "com.android.camera2:id/done_button";

    @Rule
    public ActivityScenarioRule<ShoppingListActivity> rule =
            new ActivityScenarioRule<>(ShoppingListActivity.class);

    /**
     * Tests completing a shopping list item
     * @throws InterruptedException
     * @throws UiObjectNotFoundException
     */
    @Test
    public void shoppingListTest() throws InterruptedException, UiObjectNotFoundException {

        try { onView(withId(R.id.progressBar)).perform(WaitUntilGone(R.id.progressBar, 15000)); } catch (Exception e){}

        addTestIngredient();

        addTestRecipe();

        addTestMealPlan();

        //Meal plan has 2 test ingredients, 1 is in storage -> shopping list should have 1 needed testingredient
        addToShoppingList();

        checkIngredientChecked();

        checkStorageAfterAdd();

        deleteTestMealPlan();

        deleteTestRecipe();

        deleteTestIngredient();

    }

    /**
     * Checks storage after ingredient added in shopping list
     */
    private void checkStorageAfterAdd(){

        onView(isRoot()).perform(waitId(R.id.ingredients_item, 15000));

        onView(withId(R.id.ingredients_item))
                .perform(click());

        try { onView(withId(R.id.progressBar)).perform(WaitUntilGone(R.id.progressBar, 20000)); } catch (Exception e){}

        onView(withText("111TEST INGREDIENT"))
                .perform(click());

        onView(withId(R.id.editTextIngredientAmount))
                .check(matches(withText("2")));

        onView(withText("EDIT"))
                .perform(click());

    }

    /**
     * Checks if test ingredient checkbox is checked
     */
    private void checkIngredientChecked(){

        onView(withId(R.id.recyclerView))
                .check(matches(hasDescendant(isChecked())));

    }

    /**
     * Buys item from shopping list
     * @throws InterruptedException
     */
    private void addToShoppingList() throws InterruptedException {

        onView(isRoot()).perform(waitId(R.id.shopping_list_item, 15000));

        onView(withId(R.id.shopping_list_item))
                .perform(click());

        try { onView(withId(R.id.progressBar)).perform(WaitUntilGone(R.id.progressBar, 20000)); } catch (Exception e){}

        onView(withText("111TEST INGREDIENT"))
                .perform(click());

        Thread.sleep(50);

        onView(withId(R.id.editTextShoppingListIngredientUnit))
                .perform(click(), typeText("1"));

        onView(withId(R.id.editTextShoppingListIngredientAmount))
                .perform(click(), typeText("1"));

        onView(isRoot()).perform(ViewActions.closeSoftKeyboard());

        onView(withText("ADD"))
                .perform(click());

    }


    /**
     * Deletes meal plan used for testing
     */
    private void deleteTestMealPlan() throws InterruptedException {

        onView(isRoot()).perform(waitId(R.id.meal_plan_item, 15000));

        onView(withId(R.id.meal_plan_item))
                .perform(click());

        try { onView(withId(R.id.progressBar)).perform(WaitUntilGone(R.id.progressBar, 20000)); } catch (Exception e){}

        onView(withText(">"))
                .perform(click());

        try { onView(withId(R.id.progressBar)).perform(WaitUntilGone(R.id.progressBar, 20000)); } catch (Exception e){}

        onView(withText("20"))
                .perform(click());

        onView(withText("EDIT MEAL PLAN"))
                .perform(click());

        Thread.sleep(100);

        onView(withText("DELETE"))
                .perform(click());

    }

    /**
     * Adds a meal plan used for testing (December 20th)
     * @throws InterruptedException
     */
    private void addTestMealPlan() throws InterruptedException {

        onView(isRoot()).perform(waitId(R.id.meal_plan_item, 15000));

        onView(withId(R.id.meal_plan_item))
                .perform(click());

        try { onView(withId(R.id.progressBar)).perform(WaitUntilGone(R.id.progressBar, 20000)); } catch (Exception e){}

        onView(withText(">"))
                .perform(click());

        try { onView(withId(R.id.progressBar)).perform(WaitUntilGone(R.id.progressBar, 20000)); } catch (Exception e){}

        onView(withText("20"))
                .perform(click());

        onView(withId(R.id.add_item))
                .perform(click());

        onView(isRoot()).perform(waitId(R.id.ingredient_name_textview_id, 15000));

        Thread.sleep(500);

        onView(withText("111TEST INGREDIENT"))
                .check(matches(withText(containsString("111TEST INGREDIENT"))))
                .perform(click());

        onView((withId(R.id.ingredientAmount)))
                .perform(click(), typeText("1"));

        onView(withText("ADD"))
                .perform(click());


        onView(isRoot()).perform(waitId(R.id.recipe_name_textview_id, 15000));

        Thread.sleep(50);

        onView(withText("111TEST RECIPE"))
                .check(matches(withText(containsString("111TEST RECIPE"))))
                .perform(click());

        onView((withText("ADD")))
                .perform(click());

    }

    /**
     * Adds ingredient to DB from any activity
     * Expects current activity not running tasks
     * @throws InterruptedException
     */
    private void addTestIngredient() throws InterruptedException {

        onView(isRoot()).perform(waitId(R.id.ingredients_item, 15000));

        onView(withId(R.id.ingredients_item))
                .perform(click());

        try { onView(withId(R.id.progressBar)).perform(WaitUntilGone(R.id.progressBar, 15000)); } catch (Exception e){}

        onView(withId(R.id.add_item))
                .perform(click());

        onView(withId(R.id.editTextIngredientDesc))
                .perform(click(), typeText("111TEST INGREDIENT"));

        onView(isRoot()).perform(ViewActions.closeSoftKeyboard());

        onView(withId(R.id.radioButtonFreezer))
                .perform(click());

        onView(withId(R.id.editTextIngredientAmount))
                .perform(click(), typeText("1"));

        onView(isRoot()).perform(ViewActions.closeSoftKeyboard());

        onView(withId(R.id.editTextIngredientUnit))
                .perform(click(), typeText("5"));

        onView(isRoot()).perform(ViewActions.closeSoftKeyboard());

        onView(withId(R.id.radioButtonDairy))
                .perform(click());

        onView(withText("ADD"))
                .perform(click());

    }

    /**
     * Deletes test ingredient from DB from any activity
     * Expects current activity not running tasks
     * @throws InterruptedException
     */
    private void deleteTestIngredient() throws InterruptedException {

        onView(isRoot()).perform(waitId(R.id.ingredients_item, 15000));

        onView(withId(R.id.ingredients_item))
                .perform(click());

        //Thread.sleep(1000);
        try { onView(withId(R.id.progressBar)).perform(WaitUntilGone(R.id.progressBar, 15000)); } catch (Exception e){}

        onView(withText("111TEST INGREDIENT"))
                .perform(click());

        Thread.sleep(100);

        onView(withText("DELETE"))
                .perform(click());

    }

    /**
     * Adds a recipe used for testing
     * If not on emulator, Robot CANNOT take picture. Taking picture manually will allow it to continue
     * @throws InterruptedException
     * @throws UiObjectNotFoundException
     */
    private void addTestRecipe() throws InterruptedException, UiObjectNotFoundException {

        onView(isRoot()).perform(waitId(R.id.recipes_item, 15000));

        onView((withId(R.id.recipes_item)))
                .perform(click());

        //Thread.sleep(1000);
        try { onView(withId(R.id.progressBar)).perform(WaitUntilGone(R.id.progressBar, 15000)); } catch (Exception e){}

        onView(withId(R.id.add_item))
                .perform(click());

        onView(withId(R.id.editTextRecipeName))
                .perform(click(), typeText("111TEST RECIPE"));

        //onView(isRoot()).perform(ViewActions.closeSoftKeyboard());
        Thread.sleep(100);

        onView(withId(R.id.editTextRecipeCookTime))
                .perform(click(), typeText("10"));

        //onView(isRoot()).perform(ViewActions.closeSoftKeyboard());
        Thread.sleep(100);

        onView(withId(R.id.editTextRecipeServingSize))
                .perform(click(), typeText("10"));

        //onView(isRoot()).perform(ViewActions.closeSoftKeyboard());
        Thread.sleep(100);

        onView(withId(R.id.editTextRecipeCategory))
                .perform(click(), typeText("TEST"));

        //onView(isRoot()).perform(ViewActions.closeSoftKeyboard());
        Thread.sleep(100);

        onView(withId(R.id.editTextRecipeComment))
                .perform(click(), typeText("TEST"));

        //onView(isRoot()).perform(ViewActions.closeSoftKeyboard());
        Thread.sleep(100);


        onView(isRoot()).perform(waitId(R.id.ingredient_name_textview_id, 15000));


        onView(withText("111TEST INGREDIENT"))
                .perform(click());

        onView(isRoot()).perform(waitId(R.id.ingredientAmount, 15000));

        onView((withId(R.id.ingredientAmount)))
                .perform(click(), typeText("1"));

        onView(withText("ADD"))
                .perform(click());

        onView(withId(R.id.takePhotoButton))
                .perform(click());

        //Take a photo
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice device = UiDevice.getInstance(instrumentation);

        UiObject value = device.findObject(new UiSelector().resourceId(CAMERA_BUTTON_SHUTTER_ACTION_ID));
        if(value.waitForExists(5000)){
            value.click();
        }

        value = device.findObject(new UiSelector().resourceId(CAMERA_BUTTON_DONE_ACTION_ID));
        if(value.waitForExists(5000)){
            value.click();
        }

        Thread.sleep(500);

        onView(withText("ADD"))
                .perform(click());

    }


    /**
     * Deletes test recipe
     * @throws InterruptedException
     */
    private void deleteTestRecipe() throws InterruptedException {

        onView(isRoot()).perform(waitId(R.id.recipes_item, 15000));

        onView((withId(R.id.recipes_item)))
                .perform(click());

        //Thread.sleep(1000);
        try { onView(withId(R.id.progressBar)).perform(WaitUntilGone(R.id.progressBar, 15000)); } catch (Exception e){}

        onView(withText("111TEST RECIPE"))
                .perform(click());

        Thread.sleep(100);

        onView(withText("DELETE"))
                .perform(click());

    }

    /**
     * Waits until a view is gone to proceed
     * @param viewId The id of the view to wait for.
     * @param millis The timeout of until when to wait for.
     * @return
     */
    public static ViewAction WaitUntilGone(final int viewId, final long millis){

        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return any(View.class);
            }

            @Override
            public String getDescription() {
                return "wait for a specific view with id <" + viewId + "> to disappear during " + millis + " millis.";
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
                            Log.d("TESTING", view.isShown()+"");
                            if(!view.isShown()){
                                return;
                            }
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

    /**
     * Perform action of clicking specific coordinates on view
     * @param x x coordinate of view
     * @param y y coordinate of view
     * @return
     */
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