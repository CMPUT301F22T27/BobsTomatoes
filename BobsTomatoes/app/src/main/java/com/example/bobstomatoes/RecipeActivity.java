package com.example.bobstomatoes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class RecipeActivity extends AbstractNavigationBar implements RecipeFragment.OnRecipeFragmentListener {

    ListView RecipeListView;
    ImageButton addButton;

    Bundle bundle;
    int recipePos;

    RecipeDB recipeDB;
    ArrayAdapter<Recipe> recipeAdapter;
    ArrayList<Recipe> recipeList;
    CollectionReference recipeReference;

    String [] sortChoices = {"Title", "Preparation Time", "Number of servings", "Category"};
    ArrayList <String> spinnerOptions = new ArrayList<>();
    ArrayAdapter <String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Recipes");
        setContentView(R.layout.activity_recipe);

        RecipeListView = findViewById(R.id.recipe_listview_id);
        addButton = findViewById(R.id.center_add_imageButton_id);

        //Initialize recipe database
        recipeDB = new RecipeDB();
        recipeList = recipeDB.getRecipes();
        recipeReference = recipeDB.getRecipeReference();

        //Create bundle
        bundle = new Bundle();

        //Test Values
        Ingredient testIngredient1 = new Ingredient("Red Tomatoes", "2022",
                "Fridge", 1, 1, "Canned");
        ArrayList<Ingredient> testIngredientsList = new ArrayList<>();
        testIngredientsList.add(testIngredient1);
        Recipe testRecipe1 = new Recipe("Tomato Bomb", 10, 2,
                "Soup", "Very Good!", testIngredientsList);
        testRecipes = new ArrayList<>();
        testRecipes.add(testRecipe1);

        //Recipe Adapter
        recipeAdapter = new RecipeAdapter(this, recipeList);

        //Link array and adapter
        RecipeListView.setAdapter(recipeAdapter);

        //Populate recipe list from database
        readData(new RecipeFireStoreCallback() {
            @Override
            public void onCallBack(ArrayList<Recipe> test) {
                recipeAdapter.notifyDataSetChanged();
            }
        });


        //Sets up buttons and onClickListeners for navigation bar
        initializeButtons(RecipeActivity.this);

        //Override recipe nav button to do nothing
        recipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do nothing
            }
        });

        //Add Button Listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new RecipeFragment().show(getSupportFragmentManager(), "RECIPE ADD FRAGMENT");

            }
        });

        //Set listview item click listener for when user clicks item in list
        RecipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                recipePos = pos;
                Recipe selectedRecipe = recipeList.get(pos);

                bundle.putParcelable("selectedRecipe", selectedRecipe);
                bundle.putInt("oldRecipePos", recipePos);

                RecipeFragment fragment = new RecipeFragment();
                fragment.setArguments(bundle);
                fragment.show(getSupportFragmentManager(), "EDIT/DELETE RECIPE");

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
     * @param recipe
     */
    public void onAddOkPressed(Recipe recipe) {

        recipeDB.addRecipe(recipe);
        recipeAdapter.notifyDataSetChanged();

    }

    /**
     * Confirms the edit of a recipe when the edit button is pressed
     * @param recipe
     */
    public void onEditOkPressed(Recipe recipe) {

        recipeDB.editRecipe(recipePos, recipe);
        recipeAdapter.notifyDataSetChanged();

    }

    /**
     * Confirms the deletion of a recipe when the delete button is pressed
     * @param recipe
     */
    public void onDeleteOkPressed(Recipe recipe){
        recipeDB.removeRecipe(recipe);
        recipeAdapter.notifyDataSetChanged();

    }

    /**
     * Allows the user to sort the list of recipes by title, preparation time, number of servings, and category
     * @param choice
     */
    public void sortByChoice(String choice){
        if(choice.equals("Title")){
            Collections.sort(recipeList, Recipe::compareToRecipeTitle);
        }else if(choice.equals("Preparation Time")){
            Collections.sort(recipeList, Recipe::compareToRecipeTime);
        }else if(choice.equals("Number of Servings")){
            Collections.sort(recipeList, Recipe::compareToRecipeServings);
        }else{
            Collections.sort(recipeList, Recipe::compareToRecipeCategory);
        }
        recipeAdapter.notifyDataSetChanged();
    }

    public void readData(RecipeFireStoreCallback callBack) {
        recipeReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Recipe recipe = document.toObject(Recipe.class);
                        recipeList.add(recipe);
                    }
                    callBack.onCallBack(recipeList);
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private interface RecipeFireStoreCallback {
        void onCallBack(ArrayList<Recipe> test);
    }


}