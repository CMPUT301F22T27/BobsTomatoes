package com.example.bobstomatoes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class RecipeActivity extends AbstractNavigationBar implements RecipeFragment.OnRecipeFragmentListener, RecyclerViewInterface {

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

    /**
     * Create instance
     * Display recipe activity
     * @param savedInstanceState    interface container containing savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Recipes");
        setContentView(R.layout.activity_recycler_recipe);

        recyclerView = findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.center_add_imageButton_id);

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
            }
        });


        // Sets up buttons and onClickListeners for navigation bar
        initializeButtons(RecipeActivity.this);

        //Override recipe nav button to do nothing
        recipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do nothing
            }
        });

        // Add Button Listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new RecipeFragment().show(getSupportFragmentManager(), "RECIPE ADD FRAGMENT");

            }
        });

//        // Set listview item click listener for when user clicks item in list
//        RecipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
//
//                recipePos = pos;
//                Recipe selectedRecipe = recipeList.get(pos);
//
//                bundle.putParcelable("selectedRecipe", selectedRecipe);
//                bundle.putInt("oldRecipePos", recipePos);
//
//                RecipeFragment fragment = new RecipeFragment();
//                fragment.setArguments(bundle);
//                fragment.show(getSupportFragmentManager(), "EDIT/DELETE RECIPE");
//
//            }
//        });

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

        recipeDB.addRecipe(recipe);
        recipeRecyclerAdapter.notifyDataSetChanged();

    }

    /**
     * Confirms the edit of a recipe when the edit button is pressed
     * @param recipe    specified recipe
     */
    public void onEditOkPressed(Recipe recipe) {

        recipeDB.editRecipe(recipePos, recipe);
        recipeRecyclerAdapter.notifyDataSetChanged();

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


}