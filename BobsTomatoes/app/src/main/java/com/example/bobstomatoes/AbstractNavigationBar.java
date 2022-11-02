package com.example.bobstomatoes;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


/**
 * Abstract class specifying logic for navigation bar buttons at bottom of app
 * extends AppCompatActivity
 * @author Jack
 * @see AppCompatActivity
 */
public abstract class AbstractNavigationBar extends AppCompatActivity {

    public Button storageButton;
    public Button recipeButton;
    public Button mealPlanButton;
    public Button shoppingListButton;
    public Button addButton;

    /**
     * Initialize buttons from layout by id
     * Set onClickListeners to navigate between activities
     * @poram AbstractNavigationBar, instance of current activity
     */
    public void initializeButtons(AbstractNavigationBar activity){

        storageButton = findViewById(R.id.storage_nav_button_id);
        recipeButton = findViewById(R.id.recipes_nav_button_id);
        mealPlanButton = findViewById(R.id.mealplan_nav_button_id);
        shoppingListButton = findViewById(R.id.shoplist_nav_button_id);

        //Storage Button OnCLickListener
        storageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, IngredientStorageActivity.class);
                activity.startActivity(intent);

            }
        });

        //Recipe Button OnCLickListener
        recipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, RecipeActivity.class);
                activity.startActivity(intent);

            }
        });

        //Meal Plan Button OnCLickListener
        mealPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, MealPlanActivity.class);
                activity.startActivity(intent);

            }
        });

        //Shopping List Button OnCLickListener
        shoppingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, ShoppingListActivity.class);
                activity.startActivity(intent);

            }
        });







    }


}
