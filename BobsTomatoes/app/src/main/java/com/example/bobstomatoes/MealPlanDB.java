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

public class MealPlanDB {
    private ArrayList<MealPlan> mealPlanList;

    private FirebaseFirestore mealPlanDatabase = FirebaseFirestore.getInstance();

    private final CollectionReference ingredientReference = mealPlanDatabase.collection("Meal Plan");

    public ArrayList<MealPlan> getMealPlanList() {
        return mealPlanList;
    }

    public MealPlanDB() {
        mealPlanList = new ArrayList<MealPlan>(); // Change String to Ingredient
        //Populate
    }

    public void addMealPlan(MealPlan mealPlan){
        HashMap<String,MealPlan> data = new HashMap<>();
        String mealPlanDate = mealPlan.getDate().toString();
        data.put("Attributes", mealPlan);
        ingredientReference.document(mealPlanDate)
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
        mealPlanList.add(mealPlan);
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


