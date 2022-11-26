package com.example.bobstomatoes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class for Recipe which displays a list of all the recipes
 * extends AbstractNavigationBar
 * implements RecipeFragment.OnRecipeFragmentListener
 */

public class RecipeActivity extends AbstractNavigationBar implements RecipeFragment.OnRecipeFragmentListener, SpecifyIngredientAmountFragment.OnRecipeIngredientListener, RecyclerViewInterface {

    ListView RecipeListView;
    ImageButton addButton;

    Bundle bundle;
    int recipePos;

    RecipeDB recipeDB;
    ArrayList<Recipe> recipeList;
    CollectionReference recipeReference;

    String [] sortChoices = {"Title", "Preparation Time", "Number of servings", "Category"};
    ArrayList <String> spinnerOptions = new ArrayList<>();
    ArrayAdapter <String> spinnerAdapter;

    RecipeRecyclerAdapter recipeRecyclerAdapter;
    RecyclerView recyclerView;

    ArrayList<Ingredient> globalIngredientList = new ArrayList<>();

    Dialog progressBar;

    boolean updatedIngredients = false;


    /**
     * Create instance
     * Display recipe activity
     * @param savedInstanceState    interface container containing savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.progress_dialog);
        progressBar = builder.create();

        // Modify ActionBar
        setTitle("Recipes");
        ActionBar actionBar; // Define ActionBar object
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable =
                new ColorDrawable(Color.parseColor("#00C034")); // Define ColorDrawable object + parse color
        actionBar.setBackgroundDrawable(colorDrawable); // Set BackgroundDrawable

        setContentView(R.layout.activity_recycler_recipe);

        // Sets up buttons and onClickListeners for navigation bar
        initializeButtons(RecipeActivity.this);

        recyclerView = findViewById(R.id.recyclerView);
        //addButton = findViewById(R.id.center_add_imageButton_id);

        // Initialize recipe database
        recipeDB = new RecipeDB();
        recipeList = recipeDB.getRecipes();
        recipeReference = recipeDB.getRecipeReference();

        // Create bundle
        bundle = new Bundle();

        // Recipe Adapter
        recipeRecyclerAdapter = new RecipeRecyclerAdapter(this, recipeList,this);

        // Link array and adapter
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recipeRecyclerAdapter);


        // Populate recipe list from database, by calling this, we can safely assume the list has been populated from the DataBase
        readData(new RecipeFireStoreCallback() {
            /**
             * Notify data change for ingredientList
             * @param recipeList    array list of ingredients
             */
            @Override
            public void onCallBack(ArrayList<Recipe> recipeList) {
                recipeRecyclerAdapter.notifyDataSetChanged();
                Log.d("ARRAYLIST", recipeList + "");
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

        // Retrieve user sort choice
        choiceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String sortChoice = (String) choiceSpinner.getSelectedItem();
                sortByChoice(sortChoice);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    /**
     * Confirms the addition of a new recipe when the add button is pressed
     * @param recipe    specified recipe
     */
    public void onAddOkPressed(Recipe recipe) {
        recipe.setRecipeIngredients(globalIngredientList);
        recipeDB.addRecipe(recipe);
        recipeRecyclerAdapter.notifyDataSetChanged();

    }

    /**
     * Confirms the edit of a recipe when the edit button is pressed
     *
     * @param newRecipe     new updated recipe to be added
     * @param oldRecipe     old recipe to be removed
     */
    public void onEditOkPressed(Recipe newRecipe, Recipe oldRecipe) {
        if(updatedIngredients) {
            newRecipe.setRecipeIngredients(globalIngredientList);
        }
        Log.d("New Recipe:", newRecipe.getRecipeIngredients().toString());
        Log.d("Old Recipe:", oldRecipe.getRecipeIngredients().toString());

        recipeDB.editRecipe(recipePos, newRecipe, oldRecipe);
        recipeRecyclerAdapter.notifyDataSetChanged();
        updatedIngredients = false;
    }

    /**
     * Confirms the deletion of a recipe when the delete button is pressed
     * @param recipe    specified recipe
     */
    public void onDeleteOkPressed(Recipe recipe){
        recipeDB.removeRecipe(recipe);
        recipeRecyclerAdapter.notifyDataSetChanged();

    }

    /**
     * For specifying the ingredient amount and adding it to the ingredient list
     * @param ingredientList    ingredient list for the recipe
     */
    @Override
    public void onAddIngredientOkPressed(ArrayList<Ingredient> ingredientList) {
        globalIngredientList = ingredientList;
        updatedIngredients = true;
    }

    /**
     * Allows the user to sort the list of recipes by title, preparation time, number of servings, and category
     * @param choice    user choice of how to sort recipes
     */
    public void sortByChoice(String choice){
        if(choice.equals("Title")){
            Collections.sort(recipeList, Recipe::compareToRecipeTitle);
        }else if(choice.equals("Preparation Time")){
            Collections.sort(recipeList, Recipe::compareToRecipeTime);
        }else if(choice.equals("Number of servings")){
            Collections.sort(recipeList, Recipe::compareToRecipeServings);
        }else{
            Collections.sort(recipeList, Recipe::compareToRecipeCategory);
        }
        recipeRecyclerAdapter.notifyDataSetChanged();
    }

    /**
     * Populates data base using callBack
     * @param callBack  recipe database
     */
    public void readData(RecipeFireStoreCallback callBack) {
        recipeReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    showDialog(true);
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Recipe recipe = document.toObject(Recipe.class);
                        recipeList.add(recipe);
                    }
                    showDialog(false);
                    callBack.onCallBack(recipeList);
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    /**
     * ItemClick for RecyclerView to open the fragment
     * @param position      position of the recipe in the array list
     */
    @Override
    public void onItemClick(int position) {
        recipePos = position;
        Recipe selectedRecipe = recipeList.get(position);

        bundle.putParcelable("selectedRecipe", selectedRecipe);
        bundle.putInt("oldRecipePos", recipePos);

        RecipeFragment fragment = new RecipeFragment();
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "EDIT/DELETE RECIPE");
    }


    /**
     * Interface
     * Call back recipeList
     * Basically allows us to access the recipeList outside of the onComplete and it ensures that the onComplete has fully populated our list
     */
    private interface RecipeFireStoreCallback {
        void onCallBack(ArrayList<Recipe> recipeList);
    }

    private void showDialog(boolean isShown){
        if (isShown) {
            progressBar.setCancelable(false);
            progressBar.setCanceledOnTouchOutside(false);
            progressBar.show();
        } else {
            progressBar.setCancelable(true);
            progressBar.setCanceledOnTouchOutside(true);
            progressBar.dismiss();
        }
    }


}