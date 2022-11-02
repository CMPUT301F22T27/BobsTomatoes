package com.example.bobstomatoes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class IngredientStorageActivity extends AbstractNavigationBar {

    ListView ingredientsList;
    ArrayList<Ingredient> dataList;
    int ingredientPos;
    Bundle bundle;
    IngredientStorageFragment fragment = new IngredientStorageFragment();

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

        dataList = new ArrayList<>();
        ingredientsList = findViewById(R.id.ingredients_list);
        ArrayAdapter<Ingredient> ingredientArrayAdapter = new
        Ingredient testIngredient1 = new Ingredient("sauce", "date", "pantry", 5, 6, "category");
        dataList.add(testIngredient1);

        // Creates fragment to allow editing and deletion of an ingredient
        ingredientsList.setOnItemClickListener((adapterView, view, i, l) -> {
            ingredientPos = ingredientsList.getCheckedItemPosition();
            String currentDescription = dataList.get(ingredientPos).getIngredientDesc();
            String currentDate = dataList.get(ingredientPos).getIngredientDate();
            String currentLocation = dataList.get(ingredientPos).getIngredientLocation();
            int currentAmount = dataList.get(ingredientPos).getIngredientAmount();
            int currentUnit = dataList.get(ingredientPos).getIngredientUnit();
            String currentCategory = dataList.get(ingredientPos).getIngredientCategory();

            Ingredient tempIngredient = new Ingredient(currentDescription, currentDate, currentLocation, currentAmount, currentUnit, currentCategory);

            bundle.putParcelable("tempIngredient", (Parcelable) tempIngredient);
            bundle.putParcelable("dataList", (Parcelable) dataList);
            bundle.putInt("ingredientPosition", ingredientPos);

            fragment.setArguments(bundle);
            fragment.show(getSupportFragmentManager(), "EDIT OR DELETE INGREDIENT");
        });

    }

}