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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * Class adding, removing, and editing recipe firebase database
 */
public class RecipeDB implements Parcelable {
    private ArrayList<Recipe> recipeList;

    private FirebaseFirestore recipeDatabase = FirebaseFirestore.getInstance();

    private final CollectionReference recipeReference = recipeDatabase.collection("Recipes");

    /**
     * recipeList getter
     * Retrieve list of recipes, allow accessibility to other classes
     * @return      returns the list of recipes
     */
    public ArrayList<Recipe> getRecipeList() {
        return recipeList;
    }

    /**
     * RecipeDP constructor, is an empty constructor, initialize recipeList
     */
    public RecipeDB() {
        recipeList = new ArrayList<Recipe>(); // Change String to Recipe
    }

    /**
     * Add recipe
     * Inputs a new recipe's title, time, servings, category, comments, ingredients to firebase database
     * @param recipe    specified recipe to add into recipe database
     */
    public void addRecipe(Recipe recipe){

        // Populate map with recipe contents
        HashMap<String, Object> data = new HashMap<>();
        String recipeName = recipe.getRecipeTitle();
        data.put("recipeTitle", recipeName);
        data.put("recipeTime", recipe.getRecipeTime());
        data.put("recipeServings", recipe.getRecipeServings());
        data.put("recipeCategory", recipe.getRecipeCategory());
        data.put("recipeComments", recipe.getRecipeComments());
        data.put("recipeIngredients", recipe.getRecipeIngredients());
        data.put("recipeImage", recipe.getRecipeImage());

        // Add recipe to database
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

    /**
     * Remove recipe
     * Removes an old recipe's title, time, servings, category, comments, ingredients from firebase database
     * @param recipe    specified recipe to remove from recipe database
     */
    public void removeRecipe(Recipe recipe) {

        // Populate map with recipe contents
        HashMap<String, Object> data = new HashMap<>();
        String recipeName = recipe.getRecipeTitle();
        data.put("recipeTitle", recipeName);
        data.put("recipeTime", recipe.getRecipeTime());
        data.put("recipeServings", recipe.getRecipeServings());
        data.put("recipeCategory", recipe.getRecipeCategory());
        data.put("recipeComments", recipe.getRecipeComments());
        data.put("recipeIngredients", recipe.getRecipeIngredients());
        data.put("recipeImage", recipe.getRecipeImage());


        //Remove recipe from database
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
        deleteRecipeMealTransaction(recipe);
    }

    /**
     * Edit recipe
     * Update an old recipe with new title, time, servings, category, comments, ingredients on firebase database
     * @param oldRecipePos    index of old recipe
     * @param updatedRecipe   new recipe with updated information
     */
    public void editRecipe(int oldRecipePos, Recipe updatedRecipe, Recipe oldRecipe) {

        // Populate map with recipe contents

        // Delete the recipe incase they change the name of the recipe
        HashMap<String, Object> data = new HashMap<>();
        String recipeName = oldRecipe.getRecipeTitle();
        data.put("recipeTitle", recipeName);
        data.put("recipeTime", oldRecipe.getRecipeTime());
        data.put("recipeServings", oldRecipe.getRecipeServings());
        data.put("recipeCategory", oldRecipe.getRecipeCategory());
        data.put("recipeComments", oldRecipe.getRecipeComments());
        data.put("recipeIngredients", oldRecipe.getRecipeIngredients());
        data.put("recipeImage", oldRecipe.getRecipeImage());


        //Remove recipe in database
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

        //Add/Update the current recipe
        HashMap<String, Object> data2 = new HashMap<>();
        String recipeName2 = updatedRecipe.getRecipeTitle();
        data2.put("recipeTitle", updatedRecipe.getRecipeTitle());
        data2.put("recipeTime", updatedRecipe.getRecipeTime());
        data2.put("recipeServings", updatedRecipe.getRecipeServings());
        data2.put("recipeCategory", updatedRecipe.getRecipeCategory());
        data2.put("recipeComments", updatedRecipe.getRecipeComments());
        data2.put("recipeIngredients", updatedRecipe.getRecipeIngredients());
        data2.put("recipeImage", updatedRecipe.getRecipeImage());

        // Overwrite the data in the FireStore Database
        recipeReference.document(recipeName2)
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

        recipeList.set(oldRecipePos, updatedRecipe);

        if(updatedRecipe.getRecipeTitle().equals(oldRecipe.getRecipeTitle())){
            editRecipeMealTransaction(updatedRecipe);
        } else if (!updatedRecipe.getRecipeTitle().equals(oldRecipe.getRecipeImage())) {
            editDiffNameMealTransaction(updatedRecipe, oldRecipe);
        }

    }

    /**
     * Handles syncing all databases when a recipe is edited
     * @param updatedRecipe
     */
    public void editRecipeMealTransaction(Recipe updatedRecipe) {
        MealPlanDB mealPlanDB = new MealPlanDB();
        FirebaseFirestore mealPlanDatabase = mealPlanDB.getMealPlanDatabase();
        CollectionReference mealPlanColRef = mealPlanDB.getMealPlanReference();
        DocumentReference mealPlanDocRef = mealPlanColRef.document();

        ArrayList<MealPlan> mealPlanList = mealPlanDB.getMealPlanList();


        readMealPlanData(mealPlanColRef, mealPlanList, new RecipeDB.MealPlanFireStoreCallback() {
            @Override
            public void onCallBack(ArrayList<MealPlan> mealPlanList) {
                Log.d("MEAL PLAN DATE:", mealPlanList.get(0).getMealPlanDate());
                mealPlanDatabase.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        for  (int i = 0; i < mealPlanList.size(); i++) {
                            Log.d("MEAL PLAN DATE:", mealPlanList.get(i).getMealPlanDate());
                            DocumentReference mealPlanDocRef = mealPlanColRef.document(mealPlanList.get(i).getMealPlanDate());
                            ArrayList<Recipe> currentMealPlanRecipeList = mealPlanList.get(i).getMealPlanRecipes();
                            for (int j = 0; j < currentMealPlanRecipeList.size(); j++) {
                                if (currentMealPlanRecipeList.get(j).getRecipeTitle().equals(updatedRecipe.getRecipeTitle())) {
                                    currentMealPlanRecipeList.set(j, updatedRecipe);
                                    mealPlanList.get(i).setMealPlanRecipes(currentMealPlanRecipeList);
                                }
                            }

                            transaction.update(mealPlanDocRef, "mealPlanRecipes", currentMealPlanRecipeList);
                        }
                        return null;
                    }

                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "In RecipeDB: Meal Plan Transaction success!");
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "In RecipeDB: Transaction failure.", e);
                    }
                });
            }
        });
    }

    /**
     * Handles syncing all databases when a recipe name is edited
     * @param updatedRecipe
     */
    public void editDiffNameMealTransaction(Recipe updatedRecipe, Recipe oldRecipe) {
        MealPlanDB mealPlanDB = new MealPlanDB();
        FirebaseFirestore mealPlanDatabase = mealPlanDB.getMealPlanDatabase();
        CollectionReference mealPlanColRef = mealPlanDB.getMealPlanReference();
        DocumentReference mealPlanDocRef = mealPlanColRef.document();

        ArrayList<MealPlan> mealPlanList = mealPlanDB.getMealPlanList();


        readMealPlanData(mealPlanColRef, mealPlanList, new RecipeDB.MealPlanFireStoreCallback() {
            @Override
            public void onCallBack(ArrayList<MealPlan> mealPlanList) {
                Log.d("MEAL PLAN DATE:", mealPlanList.get(0).getMealPlanDate());
                mealPlanDatabase.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        for  (int i = 0; i < mealPlanList.size(); i++) {
                            Log.d("MEAL PLAN DATE:", mealPlanList.get(i).getMealPlanDate());
                            DocumentReference mealPlanDocRef = mealPlanColRef.document(mealPlanList.get(i).getMealPlanDate());
                            ArrayList<Recipe> currentMealPlanRecipeList = mealPlanList.get(i).getMealPlanRecipes();
                            for (int j = 0; j < currentMealPlanRecipeList.size(); j++) {
                                if (currentMealPlanRecipeList.get(j).getRecipeTitle().equals(oldRecipe.getRecipeTitle())) {
                                    currentMealPlanRecipeList.set(j, updatedRecipe);
                                    mealPlanList.get(i).setMealPlanRecipes(currentMealPlanRecipeList);
                                }
                            }

                            transaction.update(mealPlanDocRef, "mealPlanRecipes", currentMealPlanRecipeList);
                        }
                        return null;
                    }

                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "In RecipeDB: Meal Plan Transaction success!");
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "In RecipeDB: Transaction failure.", e);
                    }
                });
            }
        });
    }

    /**
     * Handles syncing all databases when a recipe is deleted
     * @param deletedRecipe
     */
    public void deleteRecipeMealTransaction(Recipe deletedRecipe) {
        MealPlanDB mealPlanDB = new MealPlanDB();
        FirebaseFirestore mealPlanDatabase = mealPlanDB.getMealPlanDatabase();
        CollectionReference mealPlanColRef = mealPlanDB.getMealPlanReference();
        DocumentReference mealPlanDocRef = mealPlanColRef.document();

        ArrayList<MealPlan> mealPlanList = mealPlanDB.getMealPlanList();

        readMealPlanData(mealPlanColRef, mealPlanList, new RecipeDB.MealPlanFireStoreCallback() {
            @Override
            public void onCallBack(ArrayList<MealPlan> mealPlanList) {
                Log.d("MEAL PLAN DATE:", mealPlanList.get(0).getMealPlanDate());
                mealPlanDatabase.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        for  (int i = 0; i < mealPlanList.size(); i++) {
                            Log.d("MEAL PLAN DATE:", mealPlanList.get(i).getMealPlanDate());
                            DocumentReference mealPlanDocRef = mealPlanColRef.document(mealPlanList.get(i).getMealPlanDate());
                            ArrayList<Recipe> currentMealPlanRecipeList = mealPlanList.get(i).getMealPlanRecipes();
                            for (int j = 0; j < currentMealPlanRecipeList.size(); j++) {
                                if (currentMealPlanRecipeList.get(j).getRecipeTitle().equals(deletedRecipe.getRecipeTitle())) {
                                    currentMealPlanRecipeList.remove(j);
                                }
                            }

                            transaction.update(mealPlanDocRef, "mealPlanRecipes", currentMealPlanRecipeList);
                        }
                        return null;
                    }

                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "In RecipeDB: Meal Plan Transaction success!");
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "In RecipeDB: Transaction failure.", e);
                    }
                });
            }
        });
    }

    /**
     * Populates data base using callBack
     * @param callBack  recipe database
     */
    public void readMealPlanData(CollectionReference mealPlanReference, ArrayList<MealPlan> mealPlanList, RecipeDB.MealPlanFireStoreCallback callBack) {
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

    /**
     * Interface containing method called when meal plan database read returns its callback
     */
    private interface MealPlanFireStoreCallback {
        void onCallBack(ArrayList<MealPlan> MealPlanList);
    }

    /**
     * recipeList getter
     * Retrieve list of recipes, allow accessibility to other classes
     * @return      returns the list of recipes
     */
    public ArrayList<Recipe> getRecipes(){
        return this.recipeList;
    }

    /**
     * Recipe reference getter
     * Retrieve collection path of Recipes, allow for accessibility to other classes
     * @return  path of Recipes
     */
    public CollectionReference getRecipeReference(){
        return this.recipeReference;
    }

    /**
     * Recipe database getter
     * @return
     */
    public FirebaseFirestore getRecipeDatabase() {
        return recipeDatabase;
    }


    /**
     * RecipeDP constructor, takes in a parcel
     * @param in    parcel containing a database of recipes
     */
    protected RecipeDB(Parcel in) {
        recipeList = in.createTypedArrayList(Recipe.CREATOR);
    }


    /**
     * CREATOR used for parcelable implementation
     */
    public static final Creator<RecipeDB> CREATOR = new Creator<RecipeDB>() {
        /**
         * Create new instance of the Parcelable Class
         * @param in    parcel containing a database of recipes
         * @return      returns a new created recipe database
         */
        @Override
        public RecipeDB createFromParcel(Parcel in) {
            return new RecipeDB(in);
        }

        /**
         * Create a new array of the Parcelable Class
         * @param size  size of new array
         * @return      returns a new recipe database
         */
        @Override
        public RecipeDB[] newArray(int size) {
            return new RecipeDB[size];
        }
    };

    /**
     * Create bitmask return value
     * @return      return value
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Parcel writer
     * Creates parcel with specified object written in
     * @param parcel    parcel in which object should be written
     * @param i         addition flags of how object should be written
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(recipeList);
    }
}