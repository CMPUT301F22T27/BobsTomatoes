package com.example.bobstomatoes;

import static android.content.ContentValues.TAG;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

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
        deleteRecipeIngredientTransaction(ingredient);
        deleteMealIngredientTransaction(ingredient);
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

        editRecipeIngredientTransaction(updatedIngredient);
        editMealIngredientTransaction(updatedIngredient);
    }

    /**
     * Edit ingredient
     * Update an old ingredient with new description, date, location, amount, unit, category on firebase database
     * @param updatedIngredient   new ingredient with updated information
     */
    public void editRecipeIngredientTransaction(Ingredient updatedIngredient) {
        RecipeDB recipeDB = new RecipeDB();
        FirebaseFirestore recipeDatabase = recipeDB.getRecipeDatabase();
        CollectionReference recipeColRef = recipeDB.getRecipeReference();
        DocumentReference recipeDocRef = recipeColRef.document();

        ArrayList<Recipe> recipesList = recipeDB.getRecipeList();

        readRecipeData(recipeColRef, recipesList, new RecipeFireStoreCallback() {
            @Override
            public void onCallBack(ArrayList<Recipe> recipeList) {
                recipeDatabase.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        for  (int i = 0; i < recipesList.size(); i++) {
                            DocumentReference recipeDocRef = recipeColRef.document(recipeList.get(i).getRecipeTitle());
                            ArrayList<Ingredient> currentRecipeIngredientList = recipeList.get(i).getRecipeIngredients();
                            for (int j = 0; j < currentRecipeIngredientList.size(); j++) {
                                updatedIngredient.setIngredientAmount(currentRecipeIngredientList.get(j).getIngredientAmount());
                                if (currentRecipeIngredientList.get(j).getIngredientDesc().equals(updatedIngredient.getIngredientDesc())) {
                                    currentRecipeIngredientList.set(j, updatedIngredient);

                                    recipeList.get(i).setRecipeIngredients(currentRecipeIngredientList);
                                    recipeDB.editRecipeMealTransaction(recipeList.get(i));
                                }
                            }

                            transaction.update(recipeDocRef, "recipeIngredients", currentRecipeIngredientList);
                        }
                        return null;
                    }

                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "In IngredientDB: Edit Recipe Transaction success!");
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "In IngredientDB: Edit Recipe Transaction failure.", e);
                    }
                });
            }
        });
    }

    /**
     * Edit ingredient
     * Update an old ingredient with new description, date, location, amount, unit, category on firebase database
     * @param deletedIngredient   new ingredient with updated information
     */
    public void deleteRecipeIngredientTransaction(Ingredient deletedIngredient) {
        RecipeDB recipeDB = new RecipeDB();
        FirebaseFirestore recipeDatabase = recipeDB.getRecipeDatabase();
        CollectionReference recipeColRef = recipeDB.getRecipeReference();
        DocumentReference recipeDocRef = recipeColRef.document();

        ArrayList<Recipe> recipesList = recipeDB.getRecipeList();

        readRecipeData(recipeColRef, recipesList, new RecipeFireStoreCallback() {
            @Override
            public void onCallBack(ArrayList<Recipe> recipeList) {
                recipeDatabase.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        for  (int i = 0; i < recipesList.size(); i++) {
                            DocumentReference recipeDocRef = recipeColRef.document(recipeList.get(i).getRecipeTitle());
                            ArrayList<Ingredient> currentRecipeIngredientList = recipeList.get(i).getRecipeIngredients();
                            for (int j = 0; j < currentRecipeIngredientList.size(); j++) {
                                if (currentRecipeIngredientList.get(j).getIngredientDesc().equals(deletedIngredient.getIngredientDesc())) {
                                    currentRecipeIngredientList.remove(j);

                                    recipeList.get(i).setRecipeIngredients(currentRecipeIngredientList);
                                    recipeDB.editRecipeMealTransaction(recipeList.get(i));
                                }
                            }

                            transaction.update(recipeDocRef, "recipeIngredients", currentRecipeIngredientList);
                        }
                        return null;
                    }

                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "In IngredientDB: Delete Recipe Transaction success!");
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "In IngredientDB: Delete Recipe Transaction failure.", e);
                    }
                });
            }
        });
    }

    /**
     *
     * @param updatedIngredient
     */
    public void editMealIngredientTransaction(Ingredient updatedIngredient) {
        MealPlanDB mealPlanDB = new MealPlanDB();
        FirebaseFirestore mealPlanDatabase = mealPlanDB.getMealPlanDatabase();
        CollectionReference mealPlanColRef = mealPlanDB.getMealPlanReference();
        DocumentReference mealPlanDocRef = mealPlanColRef.document();

        ArrayList<MealPlan> mealPlanList = mealPlanDB.getMealPlanList();

        readMealPlanData(mealPlanColRef, mealPlanList, new MealPlanFireStoreCallback() {
            @Override
            public void onCallBack(ArrayList<MealPlan> mealPlanList) {
                Log.d("MEAL PLAN DATE:", mealPlanList.get(0).getMealPlanDate());
                mealPlanDatabase.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        for  (int i = 0; i < mealPlanList.size(); i++) {
                            Log.d("MEAL PLAN DATE:", mealPlanList.get(i).getMealPlanDate());
                            DocumentReference mealPlanDocRef = mealPlanColRef.document(mealPlanList.get(i).getMealPlanDate());
                            ArrayList<Ingredient> currentMealPlanIngredientList = mealPlanList.get(i).getMealPlanIngredients();
                            for (int j = 0; j < currentMealPlanIngredientList.size(); j++) {
                                updatedIngredient.setIngredientAmount(currentMealPlanIngredientList.get(j).getIngredientAmount());
                                if (currentMealPlanIngredientList.get(j).getIngredientDesc().equals(updatedIngredient.getIngredientDesc())) {
                                    currentMealPlanIngredientList.set(j, updatedIngredient);
                                }
                            }
                            transaction.update(mealPlanDocRef, "mealPlanIngredients", currentMealPlanIngredientList);
                        }
                        return null;
                    }

                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "In IngredientDB: Edit Meal Plan Transaction success!");
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "In IngredientDB: Edit Meal Plan Transaction failure.", e);
                    }
                });
            }
        });
    }

    /**
     *
     * @param deletedIngredient
     */
    public void deleteMealIngredientTransaction(Ingredient deletedIngredient) {
        MealPlanDB mealPlanDB = new MealPlanDB();
        FirebaseFirestore mealPlanDatabase = mealPlanDB.getMealPlanDatabase();
        CollectionReference mealPlanColRef = mealPlanDB.getMealPlanReference();
        DocumentReference mealPlanDocRef = mealPlanColRef.document();

        ArrayList<MealPlan> mealPlanList = mealPlanDB.getMealPlanList();

        readMealPlanData(mealPlanColRef, mealPlanList, new MealPlanFireStoreCallback() {
            @Override
            public void onCallBack(ArrayList<MealPlan> mealPlanList) {
                Log.d("MEAL PLAN DATE:", mealPlanList.get(0).getMealPlanDate());
                mealPlanDatabase.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        for  (int i = 0; i < mealPlanList.size(); i++) {
                            Log.d("MEAL PLAN DATE:", mealPlanList.get(i).getMealPlanDate());
                            DocumentReference mealPlanDocRef = mealPlanColRef.document(mealPlanList.get(i).getMealPlanDate());
                            ArrayList<Ingredient> currentMealPlanIngredientList = mealPlanList.get(i).getMealPlanIngredients();
                            for (int j = 0; j < currentMealPlanIngredientList.size(); j++) {
                                if (currentMealPlanIngredientList.get(j).getIngredientDesc().equals(deletedIngredient.getIngredientDesc())) {
                                    currentMealPlanIngredientList.remove(j);
                                }
                            }
                            transaction.update(mealPlanDocRef, "mealPlanIngredients", currentMealPlanIngredientList);
                        }
                        return null;
                    }

                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "In IngredientDB: Delete Meal Plan Transaction success!");
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "In IngredientDB: Delete Meal Plan Transaction failure.", e);
                    }
                });
            }
        });
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

    /**
     * Populates data base using callBack
     * @param callBack  recipe database
     */
    public void readRecipeData(CollectionReference recipeReference, ArrayList<Recipe> recipeList, RecipeFireStoreCallback callBack) {
        recipeReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Recipe recipe = document.toObject(Recipe.class);
                        recipeList.add(recipe);
                    }
                    callBack.onCallBack(recipeList);
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private interface RecipeFireStoreCallback {
        void onCallBack(ArrayList<Recipe> recipeList);
    }

    /**
     * Populates data base using callBack
     * @param callBack  recipe database
     */
    public void readMealPlanData(CollectionReference mealPlanReference, ArrayList<MealPlan> mealPlanList, MealPlanFireStoreCallback callBack) {
        mealPlanReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        MealPlan mealPlan = document.toObject(MealPlan.class);
                        Log.d("mealPlan DATE: ", mealPlan.getMealPlanDate());
                        mealPlanList.add(mealPlan);
                    }
                    callBack.onCallBack(mealPlanList);
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private interface MealPlanFireStoreCallback {
        void onCallBack(ArrayList<MealPlan> MealPlanList);
    }


}