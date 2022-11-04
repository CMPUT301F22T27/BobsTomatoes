package com.example.bobstomatoes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import android.util.Log;

import androidx.annotation.NonNull;

import org.junit.runner.RunWith;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

public class IngredientDBTest {

    IngredientDB ingredientDB;

    @Before
    public void initializeDB(){
        ingredientDB = new IngredientDB();
    }


    private ArrayList<Ingredient> mockIngredientList(){
        ArrayList<Ingredient> ingredientList= new ArrayList<>();
        ingredientList.add(mockIngredient());
        ingredientList.add(new Ingredient("Honeydew", "2022-12-04", "Pantry", 1,1,"Fruit"));
        ingredientList.add(new Ingredient("Cantaloupe", "2022-11-03", "Pantry", 2,2,"Fruit"));
        ingredientList.add(new Ingredient("Egg", "2022-11-05", "Fridge", 3,3,"Protein"));
        ingredientList.add(new Ingredient("Milk", "2021-01-28", "Fridge", 4,4,"Dairy"));
        ingredientList.add(new Ingredient("Bread", "2023-06-30", "Freezer", 5,5,"Grain"));
        return ingredientList;
    }

    private Ingredient mockIngredient(){
        return new Ingredient("Mango", "2022-11-04", "Fridge", 6, 6, "Fruit");
    }

    private IngredientDB mockIngredientDB(){
        return new IngredientDB();
    }

    @Test
    void testCompareToIngredientDesc(){
        ArrayList<Ingredient> ingredientList = mockIngredientList();

        Collections.sort(ingredientList, Ingredient::compareToIngredientDesc);

        assertTrue(ingredientList.get(0).getIngredientDesc().equals("Bread"));
        assertTrue(ingredientList.get(1).getIngredientDesc().equals("Cantaloupe"));
        assertTrue(ingredientList.get(2).getIngredientDesc().equals("Egg"));
        assertTrue(ingredientList.get(3).getIngredientDesc().equals("Honeydew"));
        assertTrue(ingredientList.get(4).getIngredientDesc().equals("Mango"));
        assertTrue(ingredientList.get(5).getIngredientDesc().equals("Milk"));
    }

    @Test
    void testCompareToIngredientLocation(){
        ArrayList<Ingredient> ingredientList = mockIngredientList();

        Collections.sort(ingredientList, Ingredient::compareToIngredientLocation);

        assertTrue(ingredientList.get(0).getIngredientLocation().equals("Freezer"));
        assertTrue(ingredientList.get(1).getIngredientLocation().equals("Fridge"));
        assertTrue(ingredientList.get(2).getIngredientLocation().equals("Fridge"));
        assertTrue(ingredientList.get(3).getIngredientLocation().equals("Fridge"));
        assertTrue(ingredientList.get(4).getIngredientLocation().equals("Pantry"));
        assertTrue(ingredientList.get(5).getIngredientLocation().equals("Pantry"));
    }

    @Test
    void testCompareToIngredientCategory(){
        ArrayList<Ingredient> ingredientList = mockIngredientList();

        Collections.sort(ingredientList, Ingredient::compareToIngredientCategory);

        assertTrue(ingredientList.get(0).getIngredientCategory().equals("Dairy"));
        assertTrue(ingredientList.get(1).getIngredientCategory().equals("Fruit"));
        assertTrue(ingredientList.get(2).getIngredientCategory().equals("Fruit"));
        assertTrue(ingredientList.get(3).getIngredientCategory().equals("Fruit"));
        assertTrue(ingredientList.get(4).getIngredientCategory().equals("Grain"));
        assertTrue(ingredientList.get(5).getIngredientCategory().equals("Protein"));
    }

    @Test
    void testAddIngredientDB(){
        ingredientDB = new IngredientDB();

        Ingredient ingredient = mockIngredient();

        ArrayList<Ingredient> ingredientListDB = ingredientDB.getIngredientList();

        ingredientDB.addIngredient(ingredient);

        int preSize = ingredientListDB.size();

        ingredientDB.addIngredient(ingredient);

        assertEquals(preSize+1, ingredientListDB.size());

    }

    public void readData(IngredientFireStoreCallback callBack, CollectionReference ingredientReference, ArrayList<Ingredient> ingredientList) {
        ingredientReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Ingredient ingredient = document.toObject(Ingredient.class);
                        ingredientList.add(ingredient);
                    }
                    callBack.onCallBack(ingredientList);
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    /**
     * Call back ingredientList
     */
    private interface IngredientFireStoreCallback {
        void onCallBack(ArrayList<Ingredient> ingredientList);
    }

}
