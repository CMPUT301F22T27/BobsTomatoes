package com.example.bobstomatoes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class RecipeActivity extends AbstractNavigationBar {

    ListView RecipeListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        RecipeListView = findViewById(R.id.recipe_listview_id);

        //Test Values
        Ingredient tomato = new Ingredient("tomato", "2020-07-18", "fridge",
                2, 1, "tomato");
        ArrayList<Ingredient> recipeIngredients = new ArrayList<>();
        recipeIngredients.add(tomato);

        Recipe testRecipe = new Recipe("Tomato Soup", 10, 2, "Category",
                "a nice tomato soup", recipeIngredients);

        Recipe testRecipe2 = new Recipe("Tomato Soup 2", 5, 3, "Soup",
                "a bad tomato soup", recipeIngredients);


        RecipeDB recipeDB = new RecipeDB();
        recipeDB.addRecipe(testRecipe);
        recipeDB.addRecipe(testRecipe2);


        ArrayList<Recipe> testRecipes = recipeDB.getRecipes();

        Log.d("TESTINGGGG", testRecipes.get(0).getRecipeTitle());
        Log.d("TESTINGGGG", testRecipes.get(0).getRecipeCategory());
        Log.d("TESTINGGGG", testRecipes.get(0).getRecipeComments());
        Log.d("TESTINGGGG", testRecipes.get(0).getRecipeTime() + "");
        Log.d("TESTINGGGG", testRecipes.get(0).getRecipeIngredients().get(0).getIngredientDesc());
        Log.d("TESTINGGGG", testRecipes.get(0).getRecipeServings() + "");

        Log.d("TESTINGGGG", testRecipes.get(1).getRecipeTitle());
        Log.d("TESTINGGGG", testRecipes.get(1).getRecipeCategory());
        Log.d("TESTINGGGG", testRecipes.get(1).getRecipeComments());
        Log.d("TESTINGGGG", testRecipes.get(1).getRecipeTime() + "");
        Log.d("TESTINGGGG", testRecipes.get(1).getRecipeIngredients().get(0).getIngredientDesc());
        Log.d("TESTINGGGG", testRecipes.get(1).getRecipeServings() + "");

        //Recipe Adapter
        ArrayAdapter<Recipe> recipeAdapter = new RecipeAdapter(this, testRecipes);

        //Link array and adapter
        RecipeListView.setAdapter(recipeAdapter);

        //Sets up buttons and onClickListeners for navigation bar
        initializeButtons(RecipeActivity.this);

        recipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do nothing
            }
        });

    }
}