package com.example.bobstomatoes;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;

public class IngredientDB {
    private ArrayList<Ingredient> ingredientList;

    private FirebaseFirestore ingredientDatabase = FirebaseFirestore.getInstance();

    private final CollectionReference ingredientReference = ingredientDatabase.collection("Ingredients");

    public ArrayList<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public CollectionReference getIngredientReference(){
        return ingredientReference;
    }

    public IngredientDB() {
        ingredientList = new ArrayList<Ingredient>(); // Change String to Ingredient
        //Populate
    }

    public void addIngredient(Ingredient ingredient){
        HashMap<String,Ingredient> data = new HashMap<>();
        String ingredientName = ingredient.getIngredientDesc();
        data.put("Attributes", ingredient);
        ingredientReference.document(ingredientName)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("", "Data could not be added");
                    }
                });
        //add to both arrayList and DB
    }

    //public void removeIngredient(ingredient) {
    //remove from both arrayList and DB

    // Use ingredient to find its position in arrayList
    //ingredientList.remove(ingredientPos)
    //}

    //public void editIngredient(oldIngredient, updatedIngredient) {
    //Find oldIngredientPos
    //ingredientList.set(oldIngredientPos, updatedIngredient)
    //}



}