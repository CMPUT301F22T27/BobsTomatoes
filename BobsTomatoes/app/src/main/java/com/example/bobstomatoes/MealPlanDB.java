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

//    public void updateMealPlan(){
//        IngredientDB ingredientDB = new IngredientDB();
//        ArrayList<Ingredient> allIngredients;
//
//        RecipeDB recipeDB = new RecipeDB();
//        ArrayList<Recipe> allRecipes;
//
//
//        for (int i = 0; i < mealPlanList.size(); i++) {
//        }
//    }

    /**
     * Add a meal plan
     * Inputs a new meal plan's recipes, ingredients, date on firebase database
     * @param mealPlan    specified meal plan to add into database
     */
    public void addMealPlan(MealPlan mealPlan, String mealPlanDate) {
        //Populate map with recipe contents
        HashMap<String, Object> data = new HashMap<>();
        data.put("mealPlanDate", mealPlanDate);
        data.put("mealPlanRecipes", mealPlan.getMealPlanRecipes());
        data.put("mealPlanIngredients", mealPlan.getMealPlanIngredients());

        //Edit recipe in database
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
        mealPlan.setMealPlanDate(mealPlanDate);
        mealPlanList.add(mealPlan);
    }

    /**
     * Remove meal plan
     * Removes a meal plan's recipes, ingredients, date from firebase database
     * @param mealPlan    specified meal plan to remove from the database
     */
    public void removeMealPlan(MealPlan mealPlan) {
        //Populate map with recipe contents
        HashMap<String, Object> data = new HashMap<>();
        String mealPlanDate = mealPlan.getMealPlanDate();
        data.put("mealPlanDate", mealPlanDate);
        data.put("mealPlanRecipes", mealPlan.getMealPlanRecipes());
        data.put("mealPlanIngredients", mealPlan.getMealPlanIngredients());

        //Edit recipe in database
        mealPlanReference.document(mealPlanDate)
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
        mealPlanList.remove(mealPlan);
    }

    /**
     * Edit meal plan
     * Update an old meal plan with new recipes, ingredients, date on firebase database
     * @param oldMealPlan    index of original meal plan
     * @param updatedMealPlan   new meal plan with updated information
     */
    public void editMealPlan(MealPlan oldMealPlan, MealPlan updatedMealPlan) {
        //Populate map with recipe contents
        HashMap<String, Object> data = new HashMap<>();
        String mealPlanDate = updatedMealPlan.getMealPlanDate();
        data.put("mealPlanDate", mealPlanDate);
        data.put("mealPlanRecipes", updatedMealPlan.getMealPlanRecipes());
        data.put("mealPlanIngredients", updatedMealPlan.getMealPlanIngredients());

        //Edit recipe in database
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
        mealPlanList.remove(oldMealPlan);
        mealPlanList.add(updatedMealPlan);
    }

    /**
     * Meal Plan reference getter
     * Retrieve collection path of Meal Plan, allow for accessibility to other classes
     * @return  path of Meal Plan
     */
    public CollectionReference getMealPlanReference() {
        return mealPlanReference;
    }
}


