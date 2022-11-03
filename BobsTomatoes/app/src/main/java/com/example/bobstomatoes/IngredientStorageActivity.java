package com.example.bobstomatoes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class IngredientStorageActivity extends AbstractNavigationBar implements IngredientStorageFragment.OnIngredientFragmentListener {

    ListView ingredientsListView;
    int ingredientPos;
    Bundle bundle;
    IngredientStorageFragment fragment = new IngredientStorageFragment();
    ImageButton addButton;
    ArrayAdapter<Ingredient> ingredientAdapter;

    IngredientDB ingredientDB;
    ArrayList<Ingredient> testIngredients;
    CollectionReference ingredientReference;
    String [] sortChoices = {"Description", "Location", "Best Before Date", "Category"};
    ArrayList <String> spinnerOptions = new ArrayList<>();
    ArrayAdapter <String> spinnerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_storage);

        bundle = new Bundle();

        //Sets up buttons and onClickListeners for navigation bar
        initializeButtons(IngredientStorageActivity.this);

        storageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do nothing
            }
        });


        ingredientsListView = findViewById(R.id.ingredients_list);

        ingredientDB = new IngredientDB();

        testIngredients = ingredientDB.getIngredientList();

        ingredientReference = ingredientDB.getIngredientReference();

        ingredientAdapter = new IngredientStorageAdapter(this, testIngredients);
        ingredientsListView.setAdapter(ingredientAdapter);

        readData(new FireStoreCallback() {
            @Override
            public void onCallBack(ArrayList<Ingredient> test) {
                ingredientAdapter.notifyDataSetChanged();
            }
        });

        //testIngredients = ingredientDB.getIngredientList();

        //Ingredient testIngredient1 = new Ingredient("Red Tomatoes", "2022-11-02", "Fridge", 1, 4, "Canned");

        //ingredientDB.addIngredient(testIngredient1);

        // Creates fragment to allow editing and deletion of an ingredient
        ingredientsListView.setOnItemClickListener((adapterView, view, i, l) -> {
            ingredientPos = ingredientsListView.getCheckedItemPosition();
            Ingredient selectedIngredient = testIngredients.get(ingredientPos);


            bundle.putParcelable("selectedIngredient", selectedIngredient);
            bundle.putInt("oldIngredientPos", ingredientPos);
            bundle.putBoolean("isEdit", true);

            fragment.setArguments(bundle);

            fragment.show(getSupportFragmentManager(), "EDIT OR DELETE INGREDIENT");
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


        addButton = findViewById(R.id.center_add_imageButton_id);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putBoolean("isEdit", false);
                new IngredientStorageFragment().show(getSupportFragmentManager(), "ADD INGREDIENT FRAGMENT");
            }
        });

    }
    public void onAddOkPressed(Ingredient ingredient) {

        ingredientDB.addIngredient(ingredient);
        ingredientAdapter.notifyDataSetChanged();

    }

    public void onEditOkPressed(Ingredient ingredient) {
        ingredientDB.editIngredient(ingredientPos, ingredient);
        ingredientAdapter.notifyDataSetChanged();
    }

    public void onDeleteOkPressed(Ingredient ingredient){
        ingredientDB.removeIngredient(ingredient);
        ingredientAdapter.notifyDataSetChanged();
    }

    public void sortByChoice(String choice){
        if(choice.equals("Description")){
            Collections.sort(testIngredients, Ingredient::compareToIngredientDesc);
        }else if(choice.equals("Location")){
            Collections.sort(testIngredients, Ingredient::compareToIngredientLocation);
        }else if(choice.equals("Best Before Date")){
            Collections.sort(testIngredients, Ingredient::compareToIngredientDate);
        }else{
            Collections.sort(testIngredients, Ingredient::compareToIngredientCategory);
        }
        for (int i = 0; i < testIngredients.size(); i++) {
            System.out.println(testIngredients.get(i).getIngredientDesc());
        }
        ingredientAdapter.notifyDataSetChanged();
    }

    public void readData(FireStoreCallback callBack) {
        ingredientReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                      Ingredient ingredient = document.toObject(Ingredient.class);
                      testIngredients.add(ingredient);
                    }
                    callBack.onCallBack(testIngredients);
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });
    }
    private interface FireStoreCallback {
        void onCallBack(ArrayList<Ingredient> test);
    }
}