package com.example.bobstomatoes;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Database for shoppingList
 */
public class ShoppingListDB {
    private ArrayList<ShoppingList> shoppingList;

    private FirebaseFirestore shoppingListDatabase = FirebaseFirestore.getInstance();

    private final CollectionReference shoppingListReference = shoppingListDatabase.collection("Shopping List");

    public ArrayList<ShoppingList> getShoppingList() {
        return shoppingList;
    }

    public ShoppingListDB() {
        shoppingList = new ArrayList<ShoppingList>(); // Change String to Ingredient
        //Populate
    }

    /**
     * Add shopping list
     * Inputs a new recipe's title, time, servings, category, comments, ingredients to firebase database
     * @param shoppingList    specified recipe to add into recipe database
     */
    public void addShoppingList(ShoppingList shoppingList){
        HashMap<String,ShoppingList> data = new HashMap<>();

        data.put("Attributes", shoppingList);
        shoppingListReference.document("Shopping List")
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
        this.shoppingList.add(shoppingList);
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


