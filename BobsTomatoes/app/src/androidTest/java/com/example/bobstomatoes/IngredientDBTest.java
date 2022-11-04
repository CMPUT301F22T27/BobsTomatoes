package com.example.bobstomatoes;

import static org.junit.Assert.assertEquals;

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

/**
 * Ingredient database test, test adding, deleting, and editing ingredient, tests will execute on an Android device
 */
@RunWith(AndroidJUnit4.class)
public class IngredientDBTest {

    IngredientDB ingredientDB;

    /**
     * Initialize database for ingredients
     */
    @Before
    public void initializeDB(){
        this.ingredientDB = new IngredientDB();

    }

    /**
     * Test adding of ingredient into database
     */
    @Test
    public void testAddIngredientDB() {

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
                    assertEquals(0,1);
                }
            }
        });

        assertEquals(PreSize+1, ingredientList.size());
        ingredientDB.removeIngredient(ingredient);

    }

    /**
     * Testing deletion/removal of ingredient from database
     */
    @Test
    public void testDeleteIngredient() {
        CollectionReference ingredientReference = ingredientDB.getIngredientReference();

        ArrayList<Ingredient> ingredientList = ingredientDB.getIngredientList();
        Ingredient ingredient = new Ingredient("Mango", "2022-11-04", "Fridge", 6, 6, "Fruit");

        DocumentReference ingredientRef = ingredientReference.document(ingredient.getIngredientDesc());

        int PreSize = ingredientList.size();

        ingredientDB.addIngredient(ingredient);
        ingredientDB.removeIngredient(ingredient);

        ingredientRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        assertEquals(0,1);
                    } else {
                        assertEquals(1,1);
                    }
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });

        assertEquals(PreSize, ingredientList.size());
    }

    /**
     * Testing editing of ingredient in database
     */
    @Test
    public void testEditIngredient() {
        CollectionReference ingredientReference = ingredientDB.getIngredientReference();

        ArrayList<Ingredient> ingredientList = ingredientDB.getIngredientList();
        Ingredient ingredient = new Ingredient("Mango", "2022-11-04", "Fridge", 6, 6, "Fruit");
        Ingredient ingredient2 = new Ingredient("Watermelon", "2022-11-04", "Fridge", 6, 6, "Fruit");

        DocumentReference ingredient1Ref = ingredientReference.document(ingredient.getIngredientDesc());

        DocumentReference ingredient2Ref = ingredientReference.document(ingredient2.getIngredientDesc());

        int PreSize = ingredientList.size();

        ingredientDB.addIngredient(ingredient);

        ingredient1Ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

        ingredientDB.editIngredient(0,ingredient2);

        ingredient1Ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        assertEquals(0,1);
                    } else {
                        assertEquals(1,1);
                    }
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });

        ingredient2Ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
        assertEquals(ingredient2, ingredientList.get(0));
        ingredientDB.removeIngredient(ingredient2);
    }
}
