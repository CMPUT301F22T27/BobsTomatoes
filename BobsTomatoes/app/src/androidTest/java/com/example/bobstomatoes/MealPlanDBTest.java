package com.example.bobstomatoes;

import static org.junit.Assert.assertEquals;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

public class MealPlanDBTest {

    MealPlanDB mealPlanDB;

    @Before
    public void initializeDB(){
        this.mealPlanDB = new MealPlanDB();

    }

    @Test
    public void testAddMealPlanDB() {
        boolean isInDocument;

        CollectionReference mealPlanReference = mealPlanDB.getMealPlanReference();
        ArrayList<MealPlan> mealPlanList = mealPlanDB.getMealPlanList();

        ArrayList<Ingredient> ingredientList = new ArrayList<>();
        ArrayList<Recipe> recipeList = new ArrayList<>();

        Ingredient ingredient = new Ingredient("Mango", "2022-11-04", "Fridge", 6, 6, "Fruit");

        ingredientList.add(ingredient);
        recipeList.add(new Recipe("Tomato Soup", 20, 5, "Soup","Tasty", ingredientList));

        MealPlan mealPlan = new MealPlan(recipeList, ingredientList, "2022-11-04");

        DocumentReference mealPlanRef = mealPlanReference.document(mealPlan.getDate());

        int PreSize = mealPlanList.size();

        mealPlanDB.addMealPlan(mealPlan);

        mealPlanRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        assertEquals(1,1);
                    } else {
                        assertEquals(0,1);
                    }
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });

        assertEquals(PreSize+1, mealPlanList.size());

        //Remove the ingredient after the test
        mealPlanDB.removeMealPlan(mealPlan);
    }


    @Test
    public void testDeleteMealPlan() {
        boolean isInDocument;

        CollectionReference mealPlanReference = mealPlanDB.getMealPlanReference();
        ArrayList<MealPlan> mealPlanList = mealPlanDB.getMealPlanList();
        ArrayList<Ingredient> ingredientList = new ArrayList<>();
        ArrayList<Recipe> recipeList = new ArrayList<>();

        Ingredient ingredient = new Ingredient("Mango", "2022-11-04", "Fridge", 6, 6, "Fruit");

        ingredientList.add(ingredient);
        recipeList.add(new Recipe("Tomato Soup", 20, 5, "Soup","Tasty", ingredientList));

        MealPlan mealPlan = new MealPlan(recipeList, ingredientList, "2022-11-04");

        DocumentReference mealPlanRef = mealPlanReference.document(mealPlan.getDate());

        int PreSize = mealPlanList.size();

        mealPlanDB.addMealPlan(mealPlan);
        mealPlanDB.removeMealPlan(mealPlan);

        mealPlanRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {

                    } else {

                    }
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });

        assertEquals(PreSize, mealPlanList.size());


    }

//    @Test
//    public void testEditIngredient() {
//        CollectionReference ingredientReference = ingredientDB.getIngredientReference();
//
//        ArrayList<Ingredient> ingredientList = ingredientDB.getIngredientList();
//        Ingredient ingredient = new Ingredient("Mango", "2022-11-04", "Fridge", 6, 6, "Fruit");
//        Ingredient ingredient2 = new Ingredient("Watermelon", "2022-11-04", "Fridge", 6, 6, "Fruit");
//
//        DocumentReference ingredient1Ref = ingredientReference.document(ingredient.getIngredientDesc());
//
//        DocumentReference ingredient2Ref = ingredientReference.document(ingredient2.getIngredientDesc());
//
//        int PreSize = ingredientList.size();
//
//        ingredientDB.addIngredient(ingredient);
//
//        ingredient1Ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if(document.exists()) {
//                        assertEquals(1,1);
//                    } else {
//                        assertEquals(0,1);
//                    }
//                } else {
//                    Log.d("", "Error getting documents: ", task.getException());
//                }
//            }
//        });
//
//        ingredientDB.editIngredient(0,ingredient2);
//
//        ingredient1Ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if(document.exists()) {
//                        assertEquals(0,1);
//                    } else {
//                        assertEquals(1,1);
//                    }
//                } else {
//                    Log.d("", "Error getting documents: ", task.getException());
//                }
//            }
//        });
//
//        ingredient2Ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if(document.exists()) {
//                        assertEquals(1,1);
//                    } else {
//                        assertEquals(0,1);
//                    }
//                } else {
//                    Log.d("", "Error getting documents: ", task.getException());
//                }
//            }
//        });
//
//
//        assertEquals(PreSize+1, ingredientList.size());
//
//        ingredientDB.removeIngredient(ingredient2);
//    }
}
