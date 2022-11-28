package com.example.bobstomatoes;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.bobstomatoes.databinding.ActivityRecyclerIngredientBinding;
import com.example.bobstomatoes.databinding.ActivityRecyclerRecipeBinding;
import com.example.bobstomatoes.databinding.ActivityRecyclerShoppingListBinding;
import com.example.bobstomatoes.databinding.ActivityRecyclerShoppingListBinding;
import com.example.bobstomatoes.databinding.MainMealPlanBinding;
import com.example.bobstomatoes.databinding.NavigationButtonsLayoutBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;


/**
 * Abstract class specifying logic for navigation bar buttons at bottom of app
 * extends AppCompatActivity
 * @author Jack
 * @see AppCompatActivity
 */
public abstract class AbstractNavigationBar extends AppCompatActivity {
    /**
     * Initialize buttons from layout by id
     * Set onClickListeners to navigate between activities
     * @param activity, instance of current activity
     */

    private Bundle bundle;
    public void initializeButtons(AbstractNavigationBar activity){
        if (activity instanceof MealPlanActivity){
            MainMealPlanBinding binding;
            binding = MainMealPlanBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            View view = findViewById(R.id.meal_plan_item);
            view.setActivated(true);

            binding.navigationButtonsLayoutId.bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemID = item.getItemId();

                if (itemID == R.id.ingredients_item){
                    Intent intent = new Intent(activity, IngredientStorageActivity.class);
                    activity.startActivity(intent);
                } else if (itemID == R.id.recipes_item){
                    Intent intent = new Intent(activity, RecipeActivity.class);
                    activity.startActivity(intent);
                } else if (itemID == R.id.meal_plan_item){
                    // Leave Empty
                } else if (itemID == R.id.shopping_list_item){
                    Intent intent = new Intent(activity, ShoppingListActivity.class);
                    activity.startActivity(intent);
                } else if (itemID == R.id.add_item) {
                    String date = ((MealPlanActivity) activity).getGlobalDate();

                    if (!date.equals("")) {
                        new MealPlanFragment().show(getSupportFragmentManager(), "MEAL PLAN ADD FRAGMENT");
                    } else {
                        Snackbar snackbar = null;
                        View view1 = findViewById(R.id.mealPlanDetailLayout);
                        snackbar = snackbar.make(view1, "Please select a date first!", Snackbar.LENGTH_SHORT);
                        snackbar.setDuration(800);
                        snackbar.show();

                    }
                }
               return false;
            });

        } else if (activity instanceof IngredientStorageActivity){
            ActivityRecyclerIngredientBinding binding;
            binding = ActivityRecyclerIngredientBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            View view = findViewById(R.id.ingredients_item);
            view.setActivated(true);

            binding.navigationButtonsLayoutId.bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemID = item.getItemId();
                if (itemID == R.id.ingredients_item){
                    // Leave Empty
                } else if (itemID == R.id.recipes_item){
                    Intent intent = new Intent(activity, RecipeActivity.class);
                    activity.startActivity(intent);
                } else if (itemID == R.id.meal_plan_item){
                    Intent intent = new Intent(activity, MealPlanActivity.class);
                    activity.startActivity(intent);
                } else if (itemID == R.id.shopping_list_item){
                    Intent intent = new Intent(activity, ShoppingListActivity.class);
                    activity.startActivity(intent);
                } else if (itemID == R.id.add_item){
                    bundle = new Bundle();
                    bundle.putBoolean("isEdit", false);
                    new IngredientStorageFragment().show(getSupportFragmentManager(), "ADD INGREDIENT FRAGMENT");
                }

                return false;
            });
        } else if (activity instanceof RecipeActivity) {
            ActivityRecyclerRecipeBinding binding;
            binding = ActivityRecyclerRecipeBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            View view = findViewById(R.id.recipes_item);
            view.setActivated(true);

            binding.navigationButtonsLayoutId.bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemID = item.getItemId();
                if (itemID == R.id.ingredients_item){
                    Intent intent = new Intent(activity, IngredientStorageActivity.class);
                    activity.startActivity(intent);
                } else if (itemID == R.id.recipes_item){
                    // Leave Empty
                } else if (itemID == R.id.meal_plan_item){
                    Intent intent = new Intent(activity, MealPlanActivity.class);
                    activity.startActivity(intent);
                } else if (itemID == R.id.shopping_list_item){
                    Intent intent = new Intent(activity, ShoppingListActivity.class);
                    activity.startActivity(intent);
                } else if (itemID == R.id.add_item){
                    new RecipeFragment().show(getSupportFragmentManager(), "RECIPE ADD FRAGMENT");
                }

                return false;
            });
        } else if (activity instanceof ShoppingListActivity){
            ActivityRecyclerShoppingListBinding binding;
            binding = ActivityRecyclerShoppingListBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            View view = findViewById(R.id.shopping_list_item);
            view.setActivated(true);

            binding.navigationButtonsLayoutId.bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemID = item.getItemId();
                if (itemID == R.id.ingredients_item){
                    Intent intent = new Intent(activity, IngredientStorageActivity.class);
                    activity.startActivity(intent);
                } else if (itemID == R.id.recipes_item){
                    Intent intent = new Intent(activity, RecipeActivity.class);
                    activity.startActivity(intent);
                } else if (itemID == R.id.meal_plan_item){
                    Intent intent = new Intent(activity, MealPlanActivity.class);
                    activity.startActivity(intent);
                } else if (itemID == R.id.shopping_list_item){
                    // Leave Empty
                } else if (itemID == R.id.add_item) {
                    // Leave Empty
                }

                return false;
            });
        }
    }

}
