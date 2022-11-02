package com.example.bobstomatoes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class IngredientStorageActivity extends AbstractNavigationBar {

    ListView ingredientsListView;
    int ingredientPos;
    Bundle bundle;
    IngredientStorageFragment fragment = new IngredientStorageFragment();
    ArrayAdapter<Ingredient> ingredientAdapter;
    IngredientDB ingredientDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_storage);

        //Sets up buttons and onClickListeners for navigation bar
        initializeButtons(IngredientStorageActivity.this);

        storageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do nothing
            }
        });

        ingredientsListView = findViewById(R.id.ingredients_list);

        //ingredientDB = new IngredientDB();

        ArrayList<Ingredient> testIngredients = ingredientDB.getIngredientList();

        //Ingredient testIngredient1 = new Ingredient("Tomato Sauce", "2022-11-02", "Pantry", 1, 6, "Canned");

        //ingredientDB.addIngredient(testIngredient1);

        ingredientAdapter = new IngredientStorageAdapter(this, testIngredients);

        Log.d("HELLLLLLL", testIngredients.toString());

        ingredientsListView.setAdapter(ingredientAdapter);
        ingredientAdapter.notifyDataSetChanged();

        //Log.d("", testIngredients.get(0).getIngredientLocation());
        // Creates fragment to allow editing and deletion of an ingredient
        ingredientsListView.setOnItemClickListener((adapterView, view, i, l) -> {
            ingredientPos = ingredientsListView.getCheckedItemPosition();
            Ingredient selectedIngredient = testIngredients.get(ingredientPos);

            bundle.putParcelable("selectedIngredient", selectedIngredient);
            bundle.putInt("oldIngredientPos", ingredientPos);

            //fragment.setArguments(bundle);

            fragment.show(getSupportFragmentManager(), "EDIT OR DELETE INGREDIENT");
        });
    }

    public void onAddOkPressed(Ingredient ingredient) {

    }

    public void onEditOkPressed(Ingredient ingredient) {
        ingredientDB.editIngredient(ingredientPos, ingredient);
        ingredientAdapter.notifyDataSetChanged();
    }

    public void onDeleteOkPressed(Ingredient ingredient){
        ingredientDB.removeIngredient(ingredient);
    }

}