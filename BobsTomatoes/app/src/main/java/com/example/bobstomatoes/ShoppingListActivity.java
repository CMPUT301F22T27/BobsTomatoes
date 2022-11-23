package com.example.bobstomatoes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * ShoppingListActivity, displays shoppingList and extends AbstractNavigationBar
 */
public class ShoppingListActivity extends AbstractNavigationBar {

    IngredientDB ingredientDB;
    CollectionReference ingredientReference;
    ArrayList<Ingredient> ingredientList;

    MealPlanDB mealPlanDB;
    CollectionReference mealPlanReference;
    ArrayList<MealPlan> mealPlanList;

    ShoppingList shoppingList;

    private boolean ingredientDataAvailable = false;
    private boolean mealPlanDataAvailable = false;


    /**
     * Create instance
     * Display shopping list activity
     * @param savedInstanceState    interface container containing savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Modify ActionBar
        setTitle("Shopping List");
        ActionBar actionBar; // Define ActionBar object
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable =
                new ColorDrawable(Color.parseColor("#9C0902")); // Define ColorDrawable object + parse color
        actionBar.setBackgroundDrawable(colorDrawable); // Set BackgroundDrawable

        setContentView(R.layout.activity_shopping_list);

        //Sets up buttons and onClickListeners for navigation bar
        initializeButtons(ShoppingListActivity.this);

        //Set up ingredient and meal plan reading
        ingredientDB = new IngredientDB();
        ingredientReference = ingredientDB.getIngredientReference();
        ingredientList = ingredientDB.getIngredientList();

        mealPlanDB = new MealPlanDB();
        mealPlanReference = mealPlanDB.getMealPlanReference();
        mealPlanList = mealPlanDB.getMealPlanList();

        // Populate ingredient list from database
        readIngredientData(new IngredientFireStoreCallback() {
            /**
             * Notify data change for ingredientList
             * @param ingredientList    array list of ingredients
             */
            @Override
            public void onCallBack(ArrayList<Ingredient> ingredientList) {

                ingredientDataAvailable = true;

                if (mealPlanDataAvailable) {
                    createShoppingList();

                    //TESTING
                    HashMap<String, Boolean> checkedItems = shoppingList.getCheckedItems();
                    HashMap<String, Integer> ingredientCount = shoppingList.getIngredientCount();

                    Set ingredients1 = checkedItems.keySet();
                    Set ingredients2 = ingredientCount.keySet();
                    Collection values1 = checkedItems.values();
                    Collection values2 = checkedItems.values();

                    Log.d("TESTING SHOPPING", ingredients1 + "");
                    Log.d("TESTING SHOPPING", values1 + "");
                    Log.d("TESTING SHOPPING", ingredients2 + "");
                    Log.d("TESTING SHOPPING", values2 + "");

                }
            }
        });

        // Populate ingredient list from database
        readMealPlanData(new MealPlanFireStoreCallBack() {
            /**
             * Notify data change for mealPlanList
             * @param mealPlanList    array list of meal plans
             */
            @Override
            public void onCallBack(ArrayList<MealPlan> mealPlanList) {

                mealPlanDataAvailable = true;

                if (ingredientDataAvailable) {
                    createShoppingList();

                    //TESTING
                    HashMap<String, Boolean> checkedItems = shoppingList.getCheckedItems();
                    HashMap<String, Integer> ingredientCount = shoppingList.getIngredientCount();

                    Set ingredients1 = checkedItems.keySet();
                    Set ingredients2 = ingredientCount.keySet();
                    Collection values1 = checkedItems.values();
                    Collection values2 = checkedItems.values();

                    Log.d("TESTING SHOPPING", ingredients1 + "");
                    Log.d("TESTING SHOPPING", values1 + "");
                    Log.d("TESTING SHOPPING", ingredients2 + "");
                    Log.d("TESTING SHOPPING", values2 + "");

                }

            }
        });
    }


    private void createShoppingList(){

        //Get all recipes and ingredients from all mealplans

        //Stores ingredient paired to the amount needed
        HashMap<String, Integer> numberOfIngredients = new HashMap<>();

        //Stores ingredient name paired to boolean value
        HashMap<String, Boolean> checkedIngredients = new HashMap<>();

        //Stores recipes, might not need this
        ArrayList<Recipe> allRecipes = new ArrayList<>();

        for(int i = 0; i < mealPlanList.size(); i++){

            //Grab one mealplan from database
            MealPlan tempMealPlan = mealPlanList.get(i);

            //Grab list of recipes from that mealplan
            ArrayList<Recipe> recipesInTempMealPlan = tempMealPlan.getMealPlanRecipes();

            //Iterate over recipes in that mealplan
            for(int j = 0; j < recipesInTempMealPlan.size(); j++){

                //Add recipe to allRecipes
                allRecipes.add(recipesInTempMealPlan.get(j));

                //Get recipe
                Recipe tempRecipe = recipesInTempMealPlan.get(j);

                //Get list of ingredients in recipe
                ArrayList<Ingredient> ingredientsInTempRecipe = tempRecipe.getRecipeIngredients();

                //Get hashmap of number of ingredients in recipe
                //HashMap<String, Integer> ingredientCountsR = tempRecipe.getIngredientCounts();
                //not implemented

                //Iterate over ingredients in that recipe
                for(int k = 0; k < ingredientsInTempRecipe.size(); k++){

                    //Get ingredient
                    Ingredient tempIngredient = ingredientsInTempRecipe.get(k);

                    //Get ingredient name
                    String ingredientName = tempIngredient.getIngredientDesc();

                    //Get number of that ingredient in recipe
                    //int numIngredient = ingredientCountsR.get(ingredientName);
                    int numIngredient = 1;

                    //Check if we have seen this ingredient before in mealplans
                    if (checkedIngredients.get(ingredientName) == null){

                        //add it to checkedIngredients
                        checkedIngredients.put(ingredientName, false);

                        //add it to num of ingredients
                        numberOfIngredients.put(ingredientName, numIngredient);

                    } else {

                        //If we have seen it before, we need to add number of it to num ingredients
                        int currentNum = numberOfIngredients.get(ingredientName);

                        currentNum = currentNum + numIngredient;

                        numberOfIngredients.put(ingredientName, currentNum);

                    }
                }
            }

            //Add ingredients in mealplan


            //Get list of ingredients in meal plan
            ArrayList<Ingredient> ingredientsInTempMealPlan = tempMealPlan.getMealPlanIngredients();

            //Get hashmap of number of ingredients in meal plan
            //HashMap<String, Integer> ingredientCountsMP = tempMealPlan.getIngredientCounts();
            //not implemented

            //Iterate over ingredients in that recipe
            for(int k = 0; k < ingredientsInTempMealPlan.size(); k++){

                //Get ingredient
                Ingredient tempIngredient = ingredientsInTempMealPlan.get(k);

                //Get ingredient name
                String ingredientName = tempIngredient.getIngredientDesc();

                //Get number of that ingredient in mealPlan
                //int numIngredient = ingredientCountsMP.get(ingredientName);
                int numIngredient = 1;

                //Check if we have seen this ingredient before in mealplans
                if (checkedIngredients.get(ingredientName) == null){

                    //add it to checkedIngredients
                    checkedIngredients.put(ingredientName, false);

                    //add it to num of ingredients
                    numberOfIngredients.put(ingredientName, numIngredient);

                } else {

                    //If we have seen it before, we need to add number of it to num ingredients
                    int currentNum = numberOfIngredients.get(ingredientName);

                    currentNum = currentNum + numIngredient;

                    numberOfIngredients.put(ingredientName, currentNum);

                }
            }
        }

        shoppingList = new ShoppingList(checkedIngredients, numberOfIngredients);

    }

    public void readMealPlanData(MealPlanFireStoreCallBack callBack) {
        mealPlanReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    mealPlanList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        MealPlan mealPlan = document.toObject(MealPlan.class);
                        mealPlanList.add(mealPlan);
                    }
                    callBack.onCallBack(mealPlanList);
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });
    }


    /**
     * Populates data base using callBack
     * @param callBack  ingredient database
     */
    public void readIngredientData(IngredientFireStoreCallback callBack) {
        ingredientReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Ingredient ingredient = document.toObject(Ingredient.class);
                        ingredientList.add(ingredient);
                    }
                    callBack.onCallBack(ingredientList);
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    /**
     * Interface
     * Call back ingredientList
     * Allows us to access the ingredientList outside of the onComplete and it
     * ensures that the onComplete has fully populated our list
     */
    private interface IngredientFireStoreCallback {
        void onCallBack(ArrayList<Ingredient> ingredientList);
    }

    /**
     * Interface
     * Call back MealPlanList
     * Allows us to access the MealPlanList outside of the onComplete and it
     * ensures that the onComplete has fully populated our list
     */
    private interface MealPlanFireStoreCallBack {
        void onCallBack(ArrayList<MealPlan> mealPlanList);
    }

}