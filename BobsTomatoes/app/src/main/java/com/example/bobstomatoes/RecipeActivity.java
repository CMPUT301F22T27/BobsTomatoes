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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RecipeActivity extends AbstractNavigationBar implements RecipeFragment.OnRecipeFragmentListener {

    ListView RecipeListView;
    ImageButton addButton;

    Bundle bundle;
    int recipePos;

    RecipeDB recipeDB;
    ArrayAdapter<Recipe> recipeAdapter;
    ArrayList<Recipe> testRecipes;
    CollectionReference recipeReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        RecipeListView = findViewById(R.id.recipe_listview_id);
        addButton = findViewById(R.id.center_add_imageButton_id);

        //Initialize recipe database
        recipeDB = new RecipeDB();
        testRecipes = recipeDB.getRecipes();
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
        recipeAdapter = new RecipeAdapter(this, testRecipes);

        //Link array and adapter
        RecipeListView.setAdapter(recipeAdapter);

        //Populate recipe list from database
        readData(new FireStoreCallback() {
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
                Recipe selectedRecipe = testRecipes.get(pos);

                bundle.putParcelable("selectedRecipe", selectedRecipe);
                bundle.putInt("oldRecipePos", recipePos);

                RecipeFragment fragment = new RecipeFragment();
                fragment.setArguments(bundle);
                fragment.show(getSupportFragmentManager(), "EDIT/DELETE RECIPE");

            }
        });

    }

    public void onAddOkPressed(Recipe recipe) {

        recipeDB.addRecipe(recipe);
        recipeAdapter.notifyDataSetChanged();

    }

    public void onEditOkPressed(Recipe recipe) {

        recipeDB.editRecipe(recipePos, recipe);
        recipeAdapter.notifyDataSetChanged();

    }

    public void onDeleteOkPressed(Recipe recipe){

        recipeDB.removeRecipe(recipe);
        recipeAdapter.notifyDataSetChanged();

    }

    public void readData(FireStoreCallback callBack) {
        recipeReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Recipe recipe = document.toObject(Recipe.class);
                        testRecipes.add(recipe);
                    }
                    callBack.onCallBack(testRecipes);
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private interface FireStoreCallback {
        void onCallBack(ArrayList<Recipe> test);
    }


}