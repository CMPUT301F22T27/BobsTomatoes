package com.example.bobstomatoes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * ShoppingListActivity, displays shoppingList and extends AbstractNavigationBar
 */
public class ShoppingListActivity extends AbstractNavigationBar implements RecyclerViewInterface, ShoppingListFragment.OnShoppingListFragmentListener {

    IngredientDB ingredientDB;
    CollectionReference ingredientReference;
    ArrayList<Ingredient> ingredientList;

    MealPlanDB mealPlanDB;
    CollectionReference mealPlanReference;
    ArrayList<MealPlan> mealPlanList;

    ShoppingList shoppingList;
    ArrayList<Ingredient> neededIngredients = new ArrayList<>();

    private boolean ingredientDataAvailable = false;
    private boolean mealPlanDataAvailable = false;
    boolean ingredientLoadDone = false;
    boolean mealPlanLoadDone = false;

    ShoppingListRecyclerAdapter shoppingListRecyclerAdapter;
    RecyclerView recyclerView;

    Context context = this;


    private RecyclerViewInterface recyclerViewInterface;
    Dialog progressBar;

    /**
     * Create instance
     * Display shopping list activity
     * @param savedInstanceState    interface container containing savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.progress_dialog);
        progressBar = builder.create();

        // Modify ActionBar
        setTitle("Shopping List");
        ActionBar actionBar; // Define ActionBar object
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable =
                new ColorDrawable(Color.parseColor("#9C0902")); // Define ColorDrawable object + parse color
        actionBar.setBackgroundDrawable(colorDrawable); // Set BackgroundDrawable

        setContentView(R.layout.activity_recycler_shopping_list);

        //Sets up buttons and onClickListeners for navigation bar
        initializeButtons(ShoppingListActivity.this);

        //Set up ingredient and meal plan reading
        ingredientDB = new IngredientDB();
        ingredientReference = ingredientDB.getIngredientReference();
        ingredientList = ingredientDB.getIngredientList();

        mealPlanDB = new MealPlanDB();
        mealPlanReference = mealPlanDB.getMealPlanReference();
        mealPlanList = mealPlanDB.getMealPlanList();

        //Create a new bundle
        bundle = new Bundle();

        //RecyclerView
        recyclerView = findViewById(R.id.recyclerView);

        //Spinner
        String [] sortChoices = {"Description", "Category"};
        ArrayList <String> spinnerOptions = new ArrayList<>();
        ArrayAdapter<String> spinnerAdapter;

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
                    createNeededIngredients();

                    shoppingListRecyclerAdapter = new ShoppingListRecyclerAdapter(context, neededIngredients, shoppingList.getCheckedItems(), recyclerViewInterface);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(shoppingListRecyclerAdapter);
                    shoppingListRecyclerAdapter.notifyDataSetChanged();

                    Log.d("GABE STINKY ASS", "STINKY ASS GABE");

                }
            }
        });

        // Create and Populate Spinner
        // Spinner allows users to choose how to sort ingredients
        Spinner choiceSpinner = (Spinner) findViewById(R.id.sortDropDownID);
        // Populate Sort Choice Spinner
        for (int i = 0;  i < sortChoices.length; i++) {
            spinnerOptions.add(sortChoices[i]);
        }
        spinnerAdapter = new ArrayAdapter <> (this, android.R.layout.simple_spinner_dropdown_item, spinnerOptions);
        choiceSpinner.setAdapter(spinnerAdapter);

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
                    createNeededIngredients();

                    shoppingListRecyclerAdapter = new ShoppingListRecyclerAdapter(context, neededIngredients, shoppingList.getCheckedItems(), recyclerViewInterface);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(shoppingListRecyclerAdapter);

                    // Retrieve user sort choice
                    choiceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String sortChoice = (String) choiceSpinner.getSelectedItem();
                            sortByChoice(sortChoice);

                            shoppingListRecyclerAdapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    shoppingListRecyclerAdapter.notifyDataSetChanged();

                }
            }
        });

    }

    /**
     * Needed to implement for interface
     * @param position
     */
    @Override
    public void onItemClick(int position) {

    }

    /**
     * Allows the user to sort the list of ingredients by description or category
     * @param choice    user choice of how to sort ingredients
     */
    public void sortByChoice(String choice){
        if(choice.equals("Description")){
            Collections.sort(neededIngredients, Ingredient::compareToIngredientDesc);
        }else{
            Collections.sort(neededIngredients, Ingredient::compareToIngredientCategory);
        }
    }

    /**
     * Creates a list of needed ingredient objects
     */
    private void createNeededIngredients(){

        neededIngredients = new ArrayList<>();

        //Add ingredient objects present in shopping list
        for(int i = 0; i < ingredientList.size(); i++){

            Ingredient tempIngredient = ingredientList.get(i);

            String tempIngredientName = tempIngredient.getIngredientDesc();

            //If ingredient needed
            if(shoppingList.getIngredientCount().get(tempIngredientName) != null){

                int have = tempIngredient.getIngredientAmount();
                int total = shoppingList.getIngredientCount().get(tempIngredientName);
                int need = total - have;

                if(need > 0){

                    Ingredient neededIngredient = tempIngredient.clone();
                    neededIngredient.setIngredientAmount(need);
                    neededIngredients.add(neededIngredient);

                }

            }
        }

    }

    /**
     * Handles creation of the shopping list using the current meal plan
     * be wary
     */
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

                //Iterate over ingredients in that recipe
                for(int k = 0; k < ingredientsInTempRecipe.size(); k++){

                    //Get ingredient
                    Ingredient tempIngredient = ingredientsInTempRecipe.get(k);

                    //Get ingredient name
                    String ingredientName = tempIngredient.getIngredientDesc();

                    //Get number of that ingredient in recipe
                    int numIngredient = tempIngredient.getIngredientAmount();

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

                //Get number of that ingredient in recipe
                int numIngredient = tempIngredient.getIngredientAmount();
                //int numIngredient = 1;

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

    /**
     * Populates from data base using callBack
     * @param callBack  meal plan database
     */
    public void readMealPlanData(MealPlanFireStoreCallBack callBack) {
        showDialog(true, ingredientLoadDone, mealPlanLoadDone);
        mealPlanReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    mealPlanList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        MealPlan mealPlan = document.toObject(MealPlan.class);
                        mealPlanList.add(mealPlan);
                    }
                    mealPlanLoadDone = true;
                    showDialog(false, ingredientLoadDone, true);
                    callBack.onCallBack(mealPlanList);
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                    showDialog(false, ingredientLoadDone, true);
                }
            }
        });
    }


    /**
     * Populates from data base using callBack
     * @param callBack  ingredient database
     */
    public void readIngredientData(IngredientFireStoreCallback callBack) {
        showDialog(true, ingredientLoadDone, mealPlanLoadDone);
        ingredientReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Ingredient ingredient = document.toObject(Ingredient.class);
                        ingredientList.add(ingredient);
                    }
                    ingredientLoadDone = true;
                    showDialog(false, true, mealPlanLoadDone);
                    callBack.onCallBack(ingredientList);
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                    showDialog(false, true, mealPlanLoadDone);
                }
            }
        });
    }

    /**
     * Adds necessary details in accordance to user edit
     * @param newIngredient
     * @param oldIngredientPos
     * @param newAmount
     */
    @Override
    public void onEditOkPressed(Ingredient newIngredient, int oldIngredientPos, int newAmount){

        ShoppingListRecyclerAdapter.ViewHolder viewHolder = (ShoppingListRecyclerAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(oldIngredientPos);

        Ingredient oldIngredient = neededIngredients.get(oldIngredientPos);

        //Save ingredient info from storage, and ingredient info from list
        String ingredientName = oldIngredient.getIngredientDesc();
        Ingredient ingredientFromStorage = null;
        Ingredient ingredientFromShopping = oldIngredient;
        int storedIngredientPosition = -1;

        for(int i = 0; i < ingredientList.size(); i++){

            if(ingredientList.get(i).getIngredientDesc().equals(ingredientName)){

                ingredientFromStorage = ingredientList.get(i);
                storedIngredientPosition = i;

            }

        }

        //Set checkbox
        if (newAmount >= ingredientFromShopping.getIngredientAmount()){

            //Check off ingredient
            shoppingListRecyclerAdapter.setChecked(ingredientName);

        } else {

            //Ingredient not checked
            shoppingListRecyclerAdapter.setUnchecked(ingredientName);

        }


        //If number bought increased
        if(newAmount > shoppingListRecyclerAdapter.getBoughtAmount(ingredientName)) {

            //Update DB to reflect bought ingredient
            if (ingredientFromStorage != null) {

                int amountStored = ingredientFromStorage.getIngredientAmount();

                int amountBought = newAmount;

                int amountHave = amountStored + amountBought;

                Ingredient editedIngredient = ingredientFromShopping.clone();

                editedIngredient.setIngredientAmount(amountHave);

                ingredientDB.editIngredient(storedIngredientPosition, editedIngredient, ingredientFromStorage);

            } else {

                ingredientDB.addIngredient(ingredientFromShopping);

            }

        //If number decreased
        } else {

            //Update DB to reflect removed ingredients (If changed amount bought from 10 to 7, DB should lower ingredient amount by 3)
            if (ingredientFromStorage != null) {

                int amountStored = ingredientFromStorage.getIngredientAmount();

                int amountLost = shoppingListRecyclerAdapter.getBoughtAmount(ingredientName) - newAmount;

                int amountHave = amountStored - amountLost;


                Ingredient editedIngredient = ingredientFromShopping.clone();

                editedIngredient.setIngredientAmount(amountHave);

                ingredientDB.editIngredient(storedIngredientPosition, editedIngredient, ingredientFromStorage);

            } else {

                ingredientDB.addIngredient(ingredientFromShopping);

            }

        }

        shoppingListRecyclerAdapter.setBoughtAmount(viewHolder, newAmount, newIngredient.getIngredientDesc(), oldIngredient);
        shoppingListRecyclerAdapter.notifyDataSetChanged();

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

    /**
     * Determines if progress bar is shown
     * @param isShown
     * @param ingredientLoadDone
     * @param mealPlanLoadDone
     */
    private void showDialog(boolean isShown, boolean ingredientLoadDone, boolean mealPlanLoadDone){
        if (isShown == true && (ingredientLoadDone == false || mealPlanLoadDone == false)) {
            progressBar.setCancelable(false);
            progressBar.setCanceledOnTouchOutside(false);
            progressBar.show();
        } else if (isShown == false && ingredientLoadDone == true && mealPlanLoadDone == true) {
            progressBar.setCancelable(true);
            progressBar.setCanceledOnTouchOutside(true);
            progressBar.dismiss();
        }
    }

}