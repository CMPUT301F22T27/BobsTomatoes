package com.example.bobstomatoes;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecipeDB {
    private ArrayList<Recipe> recipeList;

    private FirebaseFirestore recipeDatabase = FirebaseFirestore.getInstance();

    private final CollectionReference recipeReference = recipeDatabase.collection("Recipes");

    public ArrayList<Recipe> getRecipeList() {
        return recipeList;
    }

    public RecipeDB() {
        recipeList = new ArrayList<Recipe>(); // Change String to Recipe
        updateRecipeList();
    }

    public void addRecipe(Recipe recipe){

        HashMap<String, Recipe> data = new HashMap<>();
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

    public void removeRecipe(Recipe recipe) {
        HashMap<String, Recipe> data = new HashMap<>();
        String recipeName = recipe.getRecipeTitle();
        data.put("Attributes", recipe);
        recipeReference.document(recipeName)
                .delete()
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
        recipeList.remove(recipe);
    }

    //public void editRecipe(oldRecipe, updatedRecipe) {
    //Find oldRecipePos
    //recipeList.set(oldRecipePos, updatedRecipe)
    //}

    public ArrayList<Recipe> getRecipes(){
        return this.recipeList;
    }

    public void updateRecipeList(){
        recipeReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                recipeList.clear();
                for (QueryDocumentSnapshot doc: value) {
                    recipeList.add(doc.toObject(Recipe.class));
                }
            }
        });
    }

}