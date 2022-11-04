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

public class IngredientDB {

    private final ArrayList<Ingredient> ingredientList;

    private final FirebaseFirestore ingredientDatabase = FirebaseFirestore.getInstance();

    private final CollectionReference ingredientReference = ingredientDatabase.collection("Ingredients");
    
    public IngredientDB() {
        ingredientList = new ArrayList<Ingredient>();
    }

    public void addIngredient(Ingredient ingredient){
        HashMap<String, Object> data = new HashMap<>();
        String ingredientName = ingredient.getIngredientDesc();
        data.put("ingredientDesc", ingredient.getIngredientDesc());
        data.put("ingredientDate", ingredient.getIngredientDate());
        data.put("ingredientLocation", ingredient.getIngredientLocation());
        data.put("ingredientAmount", ingredient.getIngredientAmount());
        data.put("ingredientUnit", ingredient.getIngredientUnit());
        data.put("ingredientCategory", ingredient.getIngredientCategory());
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

    public void removeIngredient(Ingredient ingredient){
        HashMap<String, Object> data = new HashMap<>();
        String ingredientName = ingredient.getIngredientDesc();
        data.put("ingredientDesc", ingredient.getIngredientDesc());
        data.put("ingredientDate", ingredient.getIngredientDate());
        data.put("ingredientLocation", ingredient.getIngredientLocation());
        data.put("ingredientAmount", ingredient.getIngredientAmount());
        data.put("ingredientUnit", ingredient.getIngredientUnit());
        data.put("ingredientCategory", ingredient.getIngredientCategory());
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

    public void editIngredient(int oldIngredientPos, Ingredient updatedIngredient) {
        HashMap<String, Object> data = new HashMap<>();
        String ingredientName = updatedIngredient.getIngredientDesc();
        data.put("ingredientDesc", updatedIngredient.getIngredientDesc());
        data.put("ingredientDate", updatedIngredient.getIngredientDate());
        data.put("ingredientLocation", updatedIngredient.getIngredientLocation());
        data.put("ingredientAmount", updatedIngredient.getIngredientAmount());
        data.put("ingredientUnit", updatedIngredient.getIngredientUnit());
        data.put("ingredientCategory", updatedIngredient.getIngredientCategory());
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
        ingredientList.set(oldIngredientPos, updatedIngredient);
    }


    public ArrayList<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public CollectionReference getIngredientReference(){
        return ingredientReference;
    }


}