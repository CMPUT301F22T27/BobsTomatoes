package com.example.bobstomatoes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.example.bobstomatoes.IngredientDBTest;
import com.example.bobstomatoes.Ingredient;

import android.util.Log;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import androidx.annotation.NonNull;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class IngredientDBTest {

    IngredientDB ingredientDB;

    @Before
    public void initializeDB(){
        this.ingredientDB = new IngredientDB();

    }

    @Test
    public void testAddIngredientDB() {
        boolean isInDocument;

        CollectionReference ingredientReference = ingredientDB.getIngredientReference();

        ArrayList<Ingredient> ingredientList = ingredientDB.getIngredientList();
        Ingredient ingredient = new Ingredient("Mango", "2022-11-04", "Fridge", 6, 6, "Fruit");

        DocumentReference ingredientRef = ingredientReference.document(ingredient.getIngredientDesc());

        int PreSize = ingredientList.size();

        ingredientDB.addIngredient(ingredient);

        ingredientRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

        assertEquals(PreSize+1, ingredientList.size());

        //Remove the ingredient after the test
        ingredientDB.removeIngredient(ingredient);
    }


    @Test
    public void testDeleteIngredient() {

        CollectionReference ingredientReference = ingredientDB.getIngredientReference();

        ArrayList<Ingredient> ingredientList = ingredientDB.getIngredientList();
        Ingredient ingredient = new Ingredient("Mango", "2022-11-04", "Fridge", 6, 6, "Fruit");

        DocumentReference ingredientRef = ingredientReference.document(ingredient.getIngredientDesc());

        int PreSize = ingredientList.size();

        ingredientDB.addIngredient(ingredient);

        ingredientRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

        assertEquals(PreSize+1, ingredientList.size());

        //Remove the ingredient after the test
        ingredientDB.removeIngredient(ingredient);
    }

}
