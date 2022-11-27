//package com.example.bobstomatoes;
//
//
//import android.app.Activity;
//
//import static androidx.test.espresso.Espresso.onData;
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.clearText;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.action.ViewActions.typeText;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static androidx.test.espresso.matcher.ViewMatchers.withText;
//
//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import org.junit.After;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static org.hamcrest.CoreMatchers.containsString;
//import static org.hamcrest.CoreMatchers.not;
//
//
//
///**
// * Test class for IngredientStorageActivity. All the UI tests are written here. Espresso test framework is used
// */
//@RunWith(AndroidJUnit4.class)
//public class IngredientStorageActivityTest{
//
//    @Rule
//    public ActivityScenarioRule<IngredientStorageActivity> rule =
//            new ActivityScenarioRule<>(IngredientStorageActivity.class);
//
//
//
//    /**
//     * Check adding an ingredient to storage
//     */
//    @Test
//    public void checkSuccessIngredientAdd(){
//
//        onView(withId(R.id.center_add_imageButton_id))
//                .perform((click()));
//
//        onView(withId(R.id.editTextIngredientDesc))
//                .perform(click(), typeText("TEST INGREDIENT"));
//
//        onView(withId(R.id.editTextIngredientDate))
//                .perform(click(), typeText("2022-07-18"));
//
//        onView(withId(R.id.editTextIngredientLocation))
//                .perform(click(), typeText("Fridge"));
//
//        onView(withId(R.id.editTextIngredientAmount))
//                .perform(click(), typeText("1"));
//
//        onView(withId(R.id.editTextIngredientUnit))
//                .perform(click(), typeText("5"));
//
//        onView(withId(R.id.editTextIngredientCategory))
//                .perform(click(), typeText("Water"));
//
//        onView(withText("ADD"))
//                .perform(click());
//
//        //Ensure item is in list and clickable
//        onView(withText("TEST INGREDIENT"))
//                .perform(click());
//
//        //Check for proper entries
//        onView(withId(R.id.editTextIngredientDesc))
//                .check(matches(withText("TEST INGREDIENT")));
//
//        onView(withId(R.id.editTextIngredientDate))
//                .check(matches(withText("2022-07-18")));
//
//        onView(withId(R.id.editTextIngredientLocation))
//                .check(matches(withText("Fridge")));
//
//        onView(withId(R.id.editTextIngredientAmount))
//                .check(matches(withText("1")));
//
//        onView(withId(R.id.editTextIngredientUnit))
//                .check(matches(withText("5")));
//
//        onView(withId(R.id.editTextIngredientCategory))
//                .check(matches(withText("Water")));
//
//
//        //Delete
//        onView(withText("DELETE"))
//                .perform((click()));
//
//    }
//
//    /**
//     * Check editing an ingredient
//     */
//    @Test
//    public void checkSuccessIngredientEdit(){
//
//        //Add item
//        onView(withId(R.id.center_add_imageButton_id))
//                .perform((click()));
//
//        onView(withId(R.id.editTextIngredientDesc))
//                .perform(click(), typeText("TEST INGREDIENT"));
//
//        onView(withId(R.id.editTextIngredientDate))
//                .perform(click(), typeText("2022-07-18"));
//
//        onView(withId(R.id.editTextIngredientLocation))
//                .perform(click(), typeText("Fridge"));
//
//        onView(withId(R.id.editTextIngredientAmount))
//                .perform(click(), typeText("1"));
//
//        onView(withId(R.id.editTextIngredientUnit))
//                .perform(click(), typeText("5"));
//
//        onView(withId(R.id.editTextIngredientCategory))
//                .perform(click(), typeText("Water"));
//
//        onView(withText("ADD"))
//                .perform(click());
//
//        //Click item
//        onView(withText("TEST INGREDIENT"))
//                .perform(click());
//
//        //Edit
//        onView(withId(R.id.editTextIngredientDesc))
//                .perform(click(), clearText(), typeText("TEST INGREDIENT 2"));
//
//        onView(withId(R.id.editTextIngredientDate))
//                .perform(click(), clearText(), typeText("2023-07-18"));
//
//        onView(withId(R.id.editTextIngredientLocation))
//                .perform(click(), clearText(), typeText("Fridge 2"));
//
//        onView(withId(R.id.editTextIngredientAmount))
//                .perform(click(), clearText(), typeText("2"));
//
//        onView(withId(R.id.editTextIngredientUnit))
//                .perform(click(), clearText(), typeText("6"));
//
//        onView(withId(R.id.editTextIngredientCategory))
//                .perform(click(), clearText(), typeText("Water 2"));
//
//        //Edit item
//        onView(withText("EDIT"))
//                .perform(click());
//
//        //Click item
//        onView(withText("TEST INGREDIENT 2"))
//                .perform(click());
//
//        //Check for proper entries
//        onView(withId(R.id.editTextIngredientDesc))
//                .check(matches(withText("TEST INGREDIENT 2")));
//
//        onView(withId(R.id.editTextIngredientDate))
//                .check(matches(withText("2023-07-18")));
//
//        onView(withId(R.id.editTextIngredientLocation))
//                .check(matches(withText("Fridge 2")));
//
//        onView(withId(R.id.editTextIngredientAmount))
//                .check(matches(withText("2")));
//
//        onView(withId(R.id.editTextIngredientUnit))
//                .check(matches(withText("6")));
//
//        onView(withId(R.id.editTextIngredientCategory))
//                .check(matches(withText("Water 2")));
//
//        //Click item
//        onView(withText("TEST INGREDIENT 2"))
//                .perform(click());
//
//        //Delete
//        onView(withText("DELETE"))
//                .perform((click()));
//
//    }
//
//    /**
//     * Check deleting an item from ingredient list
//     */
//    @Test
//    public void checkIngredientDelete(){
//
//        //Add item
//        onView(withId(R.id.center_add_imageButton_id))
//                .perform((click()));
//
//        onView(withId(R.id.editTextIngredientDesc))
//                .perform(click(), typeText("TEST INGREDIENT"));
//
//        onView(withId(R.id.editTextIngredientDate))
//                .perform(click(), typeText("2022-07-18"));
//
//        onView(withId(R.id.editTextIngredientLocation))
//                .perform(click(), typeText("Fridge"));
//
//        onView(withId(R.id.editTextIngredientAmount))
//                .perform(click(), typeText("1"));
//
//        onView(withId(R.id.editTextIngredientUnit))
//                .perform(click(), typeText("5"));
//
//        onView(withId(R.id.editTextIngredientCategory))
//                .perform(click(), typeText("Water"));
//
//        onView(withText("ADD"))
//                .perform(click());
//
//        //Click item
//        onView(withText("TEST INGREDIENT"))
//                .perform(click());
//
//        //Delete
//        onView(withText("DELETE"))
//                .perform((click()));
//
//        //Not in list anymore
//        onView(withId(R.id.ingredients_list))
//                .check(matches(not(hasDescendant(withText("TEST INGREDIENT")))));
//
//    }
//
//    /**
//     * Check adding an invalid ingredient
//     */
//    @Test (expected = Exception.class)
//    public void checkFailureIngredientAdd(){
//
//        //Add empty item
//        onView(withId(R.id.center_add_imageButton_id))
//                .perform((click()));
//
//        onView(withText("ADD"))
//                .perform(click());
//
//    }
//
//    /**
//     * Check editing an invalid ingredient
//     */
//    @Test (expected = Exception.class)
//    public void checkFailureIngredientEdit(){
//
//        //Add item
//        onView(withId(R.id.center_add_imageButton_id))
//                .perform((click()));
//
//        onView(withId(R.id.editTextIngredientDesc))
//                .perform(click(), typeText("TEST INGREDIENT"));
//
//        onView(withId(R.id.editTextIngredientDate))
//                .perform(click(), typeText("2022-07-18"));
//
//        onView(withId(R.id.editTextIngredientLocation))
//                .perform(click(), typeText("Fridge"));
//
//        onView(withId(R.id.editTextIngredientAmount))
//                .perform(click(), typeText("1"));
//
//        onView(withId(R.id.editTextIngredientUnit))
//                .perform(click(), typeText("5"));
//
//        onView(withId(R.id.editTextIngredientCategory))
//                .perform(click(), typeText("Water"));
//
//        onView(withText("ADD"))
//                .perform(click());
//
//        //Click item
//        onView(withText("TEST INGREDIENT"))
//                .perform(click());
//
//        //Edit
//        onView(withId(R.id.editTextIngredientDesc))
//                .perform(click(), clearText());
//
//        onView(withId(R.id.editTextIngredientDate))
//                .perform(click(), clearText());
//
//        onView(withId(R.id.editTextIngredientLocation))
//                .perform(click(), clearText());
//
//        onView(withId(R.id.editTextIngredientAmount))
//                .perform(click(), clearText());
//
//        onView(withId(R.id.editTextIngredientUnit))
//                .perform(click(), clearText());
//
//        onView(withId(R.id.editTextIngredientCategory))
//                .perform(click(), clearText());
//
//        //Try to save edit
//        onView(withText("EDIT"))
//                .perform(click());
//
//    }
//
//    /**
//     * Clear tested items from database
//     */
//    @After
//    public void clear() throws InterruptedException {
//
//        Thread.sleep(15000);
//
//        try {
//
//            onView(withText(containsString("TEST INGREDIENT")))
//                    .perform(click());
//            onView(withText("DELETE"))
//                    .perform(click());
//
//        } catch (Exception e) {}
//
//
//    }
//
//
//}
