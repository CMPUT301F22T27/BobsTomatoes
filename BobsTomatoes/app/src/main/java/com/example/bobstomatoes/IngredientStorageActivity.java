package com.example.bobstomatoes;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

/**
 * Class for Ingredient Storage which displays a list of all ingredients
 * extends AbstractNavigator
 * implements IngredientStorageFragment.OnIngredientFragmentListener
 */

public class IngredientStorageActivity extends AbstractNavigationBar implements IngredientStorageFragment.OnIngredientFragmentListener, RecyclerViewInterface {
    ListView ingredientsListView;
    int ingredientPos;
    Bundle bundle;
    IngredientStorageFragment fragment = new IngredientStorageFragment();
    ImageButton addButton;
    ArrayAdapter<Ingredient> ingredientAdapter;
    IngredientStorageRecyclerAdapter ingredientRecyclerAdapter;
    IngredientDB ingredientDB;
    RecyclerView recyclerView;
    IngredientStorageRecyclerAdapter recyclerAdapter;
    ArrayList<Ingredient> ingredientList;
    CollectionReference ingredientReference;
    String [] sortChoices = {"Description", "Location", "Best Before Date", "Category"};
    ArrayList <String> spinnerOptions = new ArrayList<>();
    ArrayAdapter <String> spinnerAdapter;

    /**
     * Create instance
     * Display ingredient storage activity
     * @param savedInstanceState    interface container containing savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("Ingredient Storage");
        setContentView(R.layout.activity_recycler_ingredient);

        bundle = new Bundle();

        // Sets up buttons and onClickListeners for navigation bar
        initializeButtons(IngredientStorageActivity.this);

        storageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do nothing
            }
        });

        //ingredientsListView = findViewById(R.id.ingredients_list);
        recyclerView = findViewById(R.id.recyclerView);
       //recyclerView = new RecyclerView(IngredientStorageActivity.this);

        ingredientDB = new IngredientDB();

        ingredientList = ingredientDB.getIngredientList();

        ingredientReference = ingredientDB.getIngredientReference();

        ingredientRecyclerAdapter = new IngredientStorageRecyclerAdapter(this, ingredientList, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ingredientRecyclerAdapter);

        // Populate ingredient list from database, by calling this, we can safely assume the list has been populated from the DataBase
        readData(new IngredientFireStoreCallback() {
            /**
             * Notify data change for ingredientList
             * @param ingredientList    array list of ingredients
             */
            @Override
            public void onCallBack(ArrayList<Ingredient> ingredientList) {
                ingredientRecyclerAdapter.notifyDataSetChanged();
            }
        });

//        // Creates fragment to allow editing and deletion of an ingredient
//        ingredientsListView.setOnItemClickListener((adapterView, view, i, l) -> {
//            ingredientPos = ingredientsListView.getCheckedItemPosition();
//            Ingredient selectedIngredient = ingredientList.get(ingredientPos);
//            bundle.putParcelable("selectedIngredient", selectedIngredient);
//            bundle.putInt("oldIngredientPos", ingredientPos);
//            bundle.putBoolean("isEdit", true);
//            fragment.setArguments(bundle);
//            fragment.show(getSupportFragmentManager(), "EDIT OR DELETE INGREDIENT");
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

        // Opens the fragment with blank editTexts to add a completely new Recipe
        addButton = findViewById(R.id.center_add_imageButton_id);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putBoolean("isEdit", false);
                new IngredientStorageFragment().show(getSupportFragmentManager(), "ADD INGREDIENT FRAGMENT");
            }
        });
    }

    /**
     * Confirms the addition of a new ingredient when the add button is pressed
     * @param ingredient    specified ingredient
     */
    public void onAddOkPressed(Ingredient ingredient) {
        ingredientDB.addIngredient(ingredient);
        ingredientRecyclerAdapter.notifyDataSetChanged();

    }

    /**
     * Confirms the edit of an ingredient when the edit button is pressed
     * @param ingredient    specified ingredient
     */
    public void onEditOkPressed(Ingredient ingredient) {
        ingredientDB.editIngredient(ingredientPos, ingredient);
        ingredientRecyclerAdapter.notifyDataSetChanged();
    }

    /**
     * Confirms the deletion of an ingredient when the delete button is pressed
     * @param ingredient    specified ingredient
     */
    public void onDeleteOkPressed(Ingredient ingredient){
        ingredientDB.removeIngredient(ingredient);
        ingredientRecyclerAdapter.notifyDataSetChanged();
    }

    /**
     * Allows the user to sort the list of ingredients by description, location, or best before date
     * @param choice    user choice of how to sort ingredients
     */
    public void sortByChoice(String choice){
        if(choice.equals("Description")){
            Collections.sort(ingredientList, Ingredient::compareToIngredientDesc);
        }else if(choice.equals("Location")){
            Collections.sort(ingredientList, Ingredient::compareToIngredientLocation);
        }else if(choice.equals("Best Before Date")){
            Collections.sort(ingredientList, Ingredient::compareToIngredientDate);
        }else{
            Collections.sort(ingredientList, Ingredient::compareToIngredientCategory);
        }
        ingredientRecyclerAdapter.notifyDataSetChanged();
    }

    /**
     * Populates data base using callBack
     * @param callBack  ingredient database
     */
    public void readData(IngredientFireStoreCallback callBack) {
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

    @Override
    public void onItemClick(int position) {
        Ingredient selectedIngredient = ingredientList.get(position);
        bundle.putParcelable("selectedIngredient", selectedIngredient);
        bundle.putInt("oldIngredientPos", position);
        bundle.putBoolean("isEdit", true);
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "EDIT OR DELETE INGREDIENT");
    }

    /**
     * Interface
     * Call back ingredientList
     * Basically allows us to access the ingredientList outside of the onComplete and it ensures that the onComplete has fully populated our list
     */
    private interface IngredientFireStoreCallback {
        void onCallBack(ArrayList<Ingredient> ingredientList);
    }

}