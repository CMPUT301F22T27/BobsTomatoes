package com.example.bobstomatoes;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class adding, removing, and editing ingredient firebase database
 */
public class IngredientDB {

    private ArrayList<Ingredient> ingredientList;

    private final FirebaseFirestore ingredientDatabase = FirebaseFirestore.getInstance();

    private final CollectionReference ingredientReference = ingredientDatabase.collection("Ingredients");

    /**
     * IngredientDB constructor, is an empty constructor, initialize ingredientList
     */
    public IngredientDB() {
        this.ingredientList = new ArrayList<Ingredient>();
    }

    /**
     * Add ingredient
     * Inputs new ingredient description, date, location, amount, unit, category into firebase database
     * @param ingredient    specified Ingredient to add into database
     */
    public void addIngredient(Ingredient ingredient){
        // Create HashMap for FireStore with Ingredient Attributes
        HashMap<String, Object> data = new HashMap<>();
        String ingredientName = ingredient.getIngredientDesc();
        data.put("ingredientDesc", ingredient.getIngredientDesc());
        data.put("ingredientDate", ingredient.getIngredientDate());
        data.put("ingredientLocation", ingredient.getIngredientLocation());
        data.put("ingredientAmount", ingredient.getIngredientAmount());
        data.put("ingredientUnit", ingredient.getIngredientUnit());
        data.put("ingredientCategory", ingredient.getIngredientCategory());

        // Insert the data into the FireStore database
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
        ingredientList.add(ingredient);
    }
    /**
     * Remove ingredient
     * Removes an ingredient description, date, location, amount, unit, category from firebase database
     * @param ingredient    specified Ingredient to remove from the database
     */
    public void removeIngredient(Ingredient ingredient){
        // Create HashMap for FireStore with Ingredient Attributes
        HashMap<String, Object> data = new HashMap<>();
        String ingredientName = ingredient.getIngredientDesc();
        data.put("ingredientDesc", ingredient.getIngredientDesc());
        data.put("ingredientDate", ingredient.getIngredientDate());
        data.put("ingredientLocation", ingredient.getIngredientLocation());
        data.put("ingredientAmount", ingredient.getIngredientAmount());
        data.put("ingredientUnit", ingredient.getIngredientUnit());
        data.put("ingredientCategory", ingredient.getIngredientCategory());

        // Remove the data from the FireStore Database
        ingredientReference.document(ingredientName)
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
        ingredientList.remove(ingredient);
    }

    /**
     * Edit ingredient
     * Update an old ingredient with new description, date, location, amount, unit, category on firebase database
     * @param oldIngredientPos    index of original ingredient
     * @param updatedIngredient   new ingredient with updated information
     */
    public void editIngredient(int oldIngredientPos, Ingredient updatedIngredient, Ingredient oldIngredient) {
        // Create HashMap for FireStore with Ingredient Attributes

        // Delete the ingredient in case they change the name of the ingredient
        HashMap<String, Object> data = new HashMap<>();
        String ingredientName = oldIngredient.getIngredientDesc();
        data.put("ingredientDesc", oldIngredient.getIngredientDesc());
        data.put("ingredientDate", oldIngredient.getIngredientDate());
        data.put("ingredientLocation", oldIngredient.getIngredientLocation());
        data.put("ingredientAmount", oldIngredient.getIngredientAmount());
        data.put("ingredientUnit", oldIngredient.getIngredientUnit());
        data.put("ingredientCategory", oldIngredient.getIngredientCategory());

        // Remove the data from the FireStore Database
        ingredientReference.document(ingredientName)
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

        // Add/Update the current ingredient
        HashMap<String, Object> data2 = new HashMap<>();
        String ingredientName2 = updatedIngredient.getIngredientDesc();
        data2.put("ingredientDesc", updatedIngredient.getIngredientDesc());
        data2.put("ingredientDate", updatedIngredient.getIngredientDate());
        data2.put("ingredientLocation", updatedIngredient.getIngredientLocation());
        data2.put("ingredientAmount", updatedIngredient.getIngredientAmount());
        data2.put("ingredientUnit", updatedIngredient.getIngredientUnit());
        data2.put("ingredientCategory", updatedIngredient.getIngredientCategory());

        // Overwrite the data in the FireStore Database
        ingredientReference.document(ingredientName2)
                .set(data2)
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

        ingredientList.set(oldIngredientPos, updatedIngredient);
    }

    /**
     * Ingredient list getter
     * Retrieve array list of ingredients, allow for accessibility to other classes
     * @return  list of ingredients
     */
    public ArrayList<Ingredient> getIngredientList() {
        return ingredientList;
    }

    /**
     * Ingredient reference getter
     * Retrieve collection path of Ingredients, allow for accessibility to other classes
     * @return  path of Ingredients
     */
    public CollectionReference getIngredientReference(){
        return ingredientReference;
    }


}