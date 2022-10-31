package com.example.bobstomatoes;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;

public class RecipeDB {
    private ArrayList<Recipe> recipeList;

    private FirebaseFirestore recipeDatabase = FirebaseFirestore.getInstance();

    private final CollectionReference recipeReference = recipeDatabase.collection("Recipes");

    public ArrayList<Recipe> getRecipeList() {
        return recipeList;
    }

    public RecipeDB() {
        recipeList = new ArrayList<Recipe>(); // Change String to Recipe
        //Populate
    }

    public void addRecipe(Recipe recipe){
        HashMap<String,Recipe> data = new HashMap<>();
        String recipeName = recipe.getRecipeTitle();
        data.put("Attributes", recipe);
        recipeReference.document(recipeName)
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
        recipeList.add(recipe);
    }

    //public void removeRecipe(recipe) {
    //remove from both arrayList and DB

    // Use recipe to find its position in arrayList
    //recipeList.remove(recipePos)
    //}

    //public void editRecipe(oldRecipe, updatedRecipe) {
    //Find oldRecipePos
    //recipeList.set(oldRecipePos, updatedRecipe)
    //}



}