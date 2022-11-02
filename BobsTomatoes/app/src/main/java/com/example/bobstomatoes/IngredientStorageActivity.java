package com.example.bobstomatoes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class IngredientStorageActivity extends AbstractNavigationBar implements IngredientStorageFragment.OnIngredientFragmentListener {

    ListView ingredientsListView;
    int ingredientPos;
    Bundle bundle;
    IngredientStorageFragment fragment = new IngredientStorageFragment();

    ArrayAdapter<Ingredient> ingredientAdapter;

    IngredientDB ingredientDB;
    ArrayList<Ingredient> testIngredients;
    CollectionReference ingredientReference;


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

            fragment.setArguments(bundle);

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