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
 * Class adding, removing, and editing meal plans in firebase database
 */
public class MealPlanDB {
    private ArrayList<MealPlan> mealPlanList;

    private FirebaseFirestore mealPlanDatabase = FirebaseFirestore.getInstance();

    private final CollectionReference mealPlanReference = mealPlanDatabase.collection("Meal Plan");

    /**
     * MealPlanList getter
     * Retrieve list of meal plans, allow accessibility to other classes
     * @return      returns the list of meal plans
     */
    public ArrayList<MealPlan> getMealPlanList() {
        return mealPlanList;
    }

    /**
     * MealPlanDP constructor, is an empty constructor, initialize mealPlanList
     */
    public MealPlanDB() {
        mealPlanList = new ArrayList<MealPlan>(); // Change String to Ingredient
        //Populate
    }

    /**
     * Add a meal plan
     * Inputs a new meal plan's recipes, ingredients, date on firebase database
     * @param mealPlan    specified meal plan to add into database
     */
    public void addMealPlan(MealPlan mealPlan){
        HashMap<String,MealPlan> data = new HashMap<>();
        String mealPlanDate = mealPlan.getDate().toString();
        data.put("Attributes", mealPlan);
        mealPlanReference.document(mealPlanDate)
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

    /**
     * Remove meal plan
     * Removes a meal plan's recipes, ingredients, date from firebase database
     * @param mealPlan    specified meal plan to remove from the database
     */
    //public void removeIngredient(ingredient) {
    //remove from both arrayList and DB

    // Use ingredient to find its position in arrayList
    //ingredientList.remove(ingredientPos)
    //}

    /**
     * Edit meal plan
     * Update an old meal plan with new recipes, ingredients, date on firebase database
     * @param oldMealPlanPos    index of original meal plan
     * @param updatedMealPlan   new meal plan with updated information
     */
    //public void editIngredient(oldIngredient, updatedIngredient) {
    //Find oldIngredientPos
    //ingredientList.set(oldIngredientPos, updatedIngredient)
    //}

}


