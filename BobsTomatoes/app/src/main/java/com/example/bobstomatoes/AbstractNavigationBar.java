package com.example.bobstomatoes;

import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.bobstomatoes.databinding.ActivityRecyclerIngredientBinding;
import com.example.bobstomatoes.databinding.ActivityRecyclerRecipeBinding;
import com.example.bobstomatoes.databinding.ActivityShoppingListBinding;
import com.example.bobstomatoes.databinding.MainMealPlanBinding;
import com.example.bobstomatoes.databinding.NavigationButtonsLayoutBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;


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
                }

                return false;
            });
        } else if (activity instanceof ShoppingListActivity){
            ActivityShoppingListBinding binding;
            binding = ActivityShoppingListBinding.inflate(getLayoutInflater());
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
                }

                return false;
            });
        }
    }

}
