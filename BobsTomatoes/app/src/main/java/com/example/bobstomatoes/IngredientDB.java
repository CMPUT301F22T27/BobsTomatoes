package com.example.bobstomatoes;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class IngredientDB {
    private ArrayList<String> ingredientList;

    private FirebaseFirestore ingredientDatabase = FirebaseFirestore.getInstance();

    private final CollectionReference ingredientReference = ingredientDatabase.collection("Ingredients");

    public ArrayList<String> getIngredientList() {
        return ingredientList;
    }

    private IngredientDB() {
        ingredientList = new ArrayList<String>(); // Change String to Ingredient
        //Populate
    }

    //public void addIngredient(ingredient){
        //add to both arrayList and DB
    //}

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
