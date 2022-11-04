package com.example.bobstomatoes;

import android.app.Activity;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

/**
 * Test class for RecipeActivity. All the UI tests are written here. Espresso test framework is used
 */
@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {

    @Rule
    public ActivityScenarioRule<RecipeActivity> rule =
            new ActivityScenarioRule<>(RecipeActivity.class);

    /**
     * Add test ingredients to storage before tests
     */
    @Before
    public void SetUp(){

        onView(withId(R.id.storage_nav_button_id))
                .perform(click());

        onView(withId(R.id.center_add_imageButton_id))
                .perform((click()));

        onView(withId(R.id.editTextIngredientDesc))
                .perform(click(), typeText("TEST INGREDIENT"));

        onView(withId(R.id.editTextIngredientDate))
                .perform(click(), typeText("2022-07-18"));

        onView(withId(R.id.editTextIngredientLocation))
                .perform(click(), typeText("Fridge"));

        onView(withId(R.id.editTextIngredientAmount))
                .perform(click(), typeText("1"));

        onView(withId(R.id.editTextIngredientUnit))
                .perform(click(), typeText("5"));

        onView(withId(R.id.editTextIngredientCategory))
                .perform(click(), typeText("Water"));

        onView(withText("ADD"))
                .perform(click());

        onView(withId(R.id.center_add_imageButton_id))
                .perform((click()));

        onView(withId(R.id.editTextIngredientDesc))
                .perform(click(), typeText("TEST INGREDIENT 2"));

        onView(withId(R.id.editTextIngredientDate))
                .perform(click(), typeText("2022-07-18"));

        onView(withId(R.id.editTextIngredientLocation))
                .perform(click(), typeText("Fridge"));

        onView(withId(R.id.editTextIngredientAmount))
                .perform(click(), typeText("1"));

        onView(withId(R.id.editTextIngredientUnit))
                .perform(click(), typeText("5"));

        onView(withId(R.id.editTextIngredientCategory))
                .perform(click(), typeText("Water"));

        onView(withText("ADD"))
                .perform(click());

        onView(withText("RECIPES"))
                .perform(click());

    }

    /**
     * Check adding a recipe
     */
    @Test
    public void checkSuccessRecipeAdd() throws InterruptedException {

        onView(withId(R.id.center_add_imageButton_id))
                .perform((click()));

        onView(withId(R.id.editTextRecipeName))
                .perform(click(), typeText("TEST RECIPE"));

        onView(withId(R.id.editTextRecipeCookTime))
                .perform(click(), typeText("5"));

        onView(withId(R.id.editTextRecipeServingSize))
                .perform(click(), typeText("5"));

        onView(withId(R.id.editTextRecipeCategory))
                .perform(click(), typeText("TEST CATEGORY"));

        onView(withId(R.id.editTextRecipeComment))
                .perform(click(), typeText("TEST COMMENT"));

        //Wait for ingredients to populate
        Thread.sleep(5000);

        //Add ingredient
        onView(withText("TEST INGREDIENT"))
                .perform(click());


        onView(withText("TEST INGREDIENT 2"))
                .perform(click());

        onView(withText("ADD"))
                .perform(click());


        //Ensure item is in list and clickable
        onView(withText("TEST RECIPE"))
                .perform(click());

        //Check for proper entries
        onView(withId(R.id.editTextRecipeName))
                .check(matches(withText("TEST RECIPE")));

        onView(withId(R.id.editTextRecipeCookTime))
                .check(matches(withText("5")));

        onView(withId(R.id.editTextRecipeServingSize))
                .check(matches(withText("5")));

        onView(withId(R.id.editTextRecipeCategory))
                .check(matches(withText("TEST CATEGORY")));

        onView(withId(R.id.editTextRecipeComment))
                .check(matches(withText("TEST COMMENT")));

        //Delete recipe
        onView(withText("DELETE"))
                .perform((click()));

    }

    /**
     * Check editing a recipe
     */
    @Test
    public void checkSuccessRecipeEdit() throws InterruptedException {

        //Add recipe
        onView(withId(R.id.center_add_imageButton_id))
                .perform((click()));

        onView(withId(R.id.editTextRecipeName))
                .perform(click(), typeText("TEST RECIPE"));

        onView(withId(R.id.editTextRecipeCookTime))
                .perform(click(), typeText("5"));

        onView(withId(R.id.editTextRecipeServingSize))
                .perform(click(), typeText("5"));

        onView(withId(R.id.editTextRecipeCategory))
                .perform(click(), typeText("TEST CATEGORY"));

        onView(withId(R.id.editTextRecipeComment))
                .perform(click(), typeText("TEST COMMENT"));

        //Wait for ingredients to populate
        Thread.sleep(5000);

        //Add ingredient
        onView(withText("TEST INGREDIENT"))
                .perform(click());

        onView(withText("ADD"))
                .perform(click());


        //Ensure item is in list and clickable
        onView(withText("TEST RECIPE"))
                .perform(click());

        onView(withId(R.id.editTextRecipeName))
                .perform(click(), clearText(), typeText("TEST RECIPE"));

        onView(withId(R.id.editTextRecipeCookTime))
                .perform(click(), clearText(), typeText("6"));

        onView(withId(R.id.editTextRecipeServingSize))
                .perform(click(), clearText(), typeText("6"));

        onView(withId(R.id.editTextRecipeCategory))
                .perform(click(), clearText(), typeText("TEST CATEGORY 2"));

        onView(withId(R.id.editTextRecipeComment))
                .perform(click(), clearText(), typeText("TEST COMMENT 2"));

        //Wait for ingredients to populate
        Thread.sleep(5000);

        //Remove ingredient
        onView(withText("TEST INGREDIENT"))
                .perform(click());

        //Add ingredient
        onView(withText("TEST INGREDIENT 2"))
                .perform(click());

        //This sometimes will not press the dialog edit button
        onView(withText("EDIT"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());

        Espresso.onIdle();

        //Ensure item is in list and clickable
        onView(withText("TEST RECIPE"))
                .perform(click());

        //Check for proper entries
        onView(withId(R.id.editTextRecipeName))
                .check(matches(withText("TEST RECIPE")));

        onView(withId(R.id.editTextRecipeCookTime))
                .check(matches(withText("6")));

        onView(withId(R.id.editTextRecipeServingSize))
                .check(matches(withText("6")));

        onView(withId(R.id.editTextRecipeCategory))
                .check(matches(withText("TEST CATEGORY 2")));

        onView(withId(R.id.editTextRecipeComment))
                .check(matches(withText("TEST COMMENT 2")));


        //Delete recipe
        onView(withText("DELETE"))
                .perform((click()));

    }

    /**
     * Check deleting a recipe
     */
    @Test
    public void checkRecipeDelete() throws InterruptedException {

        //Add recipe
        onView(withId(R.id.center_add_imageButton_id))
                .perform((click()));

        onView(withId(R.id.editTextRecipeName))
                .perform(click(), typeText("TEST RECIPE"));

        onView(withId(R.id.editTextRecipeCookTime))
                .perform(click(), typeText("5"));

        onView(withId(R.id.editTextRecipeServingSize))
                .perform(click(), typeText("5"));

        onView(withId(R.id.editTextRecipeCategory))
                .perform(click(), typeText("TEST CATEGORY"));

        onView(withId(R.id.editTextRecipeComment))
                .perform(click(), typeText("TEST COMMENT"));

        //Wait for ingredients to populate
        Thread.sleep(5000);

        //Add ingredient
        onView(withText("TEST INGREDIENT"))
                .perform(click());

        onView(withText("ADD"))
                .perform(click());

        //Delete item
        onView(withText("TEST RECIPE"))
                .perform(click());

        onView(withText("DELETE"))
                .perform(click());

        //Not in list anymore
        onView(withId(R.id.recipe_listview_id))
                .check(matches(not(hasDescendant(withText("TEST RECIPE")))));

    }

    /**
     * Check adding an invalid recipe
     */
    @Test (expected = Exception.class)
    public void checkFailureRecipeAdd() throws InterruptedException {

        //Add recipe
        onView(withId(R.id.center_add_imageButton_id))
                .perform((click()));

        onView(withText("ADD"))
                .perform(click());

    }

    /**
     * Check editing an invalid recipe
     */
    @Test (expected = Exception.class)
    public void checkFailureRecipeEdit() throws InterruptedException {

        //Add recipe
        onView(withId(R.id.center_add_imageButton_id))
                .perform((click()));

        onView(withId(R.id.editTextRecipeName))
                .perform(click(), typeText("TEST RECIPE"));

        onView(withId(R.id.editTextRecipeCookTime))
                .perform(click(), typeText("5"));

        onView(withId(R.id.editTextRecipeServingSize))
                .perform(click(), typeText("5"));

        onView(withId(R.id.editTextRecipeCategory))
                .perform(click(), typeText("TEST CATEGORY"));

        onView(withId(R.id.editTextRecipeComment))
                .perform(click(), typeText("TEST COMMENT"));

        //Wait for ingredients to populate
        Thread.sleep(5000);

        //Add ingredient
        onView(withText("TEST INGREDIENT"))
                .perform(click());

        onView(withText("ADD"))
                .perform(click());

        //Ensure item is in list and clickable
        onView(withText("TEST RECIPE"))
                .perform(click());

        //Edit entries
        onView(withId(R.id.editTextRecipeName))
                .perform(click(), clearText());

        onView(withId(R.id.editTextRecipeCookTime))
                .perform(click(), clearText());

        onView(withId(R.id.editTextRecipeServingSize))
                .perform(click(), clearText());

        onView(withId(R.id.editTextRecipeCategory))
                .perform(click(), clearText());

        onView(withId(R.id.editTextRecipeComment))
                .perform(click(), clearText());


        //This sometimes will not press the dialog edit button
        onView(withText("EDIT"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());

        Espresso.onIdle();

    }

    /**
     * Clear test values
     */
    @After
    public void clear() throws InterruptedException {

        Thread.sleep(5000);

        //Issue with onView(withText("EDIT")) not exiting dialog, this is backup attempt to exit dialog if first fails
        try {
            onView(withText(containsString("EDIT")))
                    .perform(click());
        } catch (Exception e){ }

        onView(withId(R.id.storage_nav_button_id))
                .perform(click());

        Thread.sleep(15000);

        onView(withText("TEST INGREDIENT")).
                perform(click());
        onView(withText("DELETE")).
                perform(click());

        onView(withText("TEST INGREDIENT 2")).
                perform(click());
        onView(withText("DELETE")).
                perform(click());

        onView(withId(R.id.recipes_nav_button_id))
                .perform(click());

        Thread.sleep(15000);

        try {
            onView(withText(containsString("TEST")))
                    .perform(click());
            onView(withText("DELETE"))
                    .perform(click());
        } catch (Exception e){ }


    }

}
