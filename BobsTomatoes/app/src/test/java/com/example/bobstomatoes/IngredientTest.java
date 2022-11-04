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

/**
 * Ingredient test, test sorting of ingredients by description, date, location, category, test will execute on android device
 */
public class IngredientTest {

    /**
     * Create an ingredient list
     * @return      return the new created ingredient list
     */
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

    /**
     * Create a new ingredient
     * @return      return new ingredient
     */
    private Ingredient mockIngredient(){
        return new Ingredient("Mango", "2022-11-04", "Fridge", 6, 6, "Fruit");
    }

    /**
     * Create a new ingredient database
     * @return      return new ingredient database
     */
    private IngredientDB mockIngredientDB(){
        return new IngredientDB();
    }

    /**
     * Test the comparing of ingredient descriptions
     * Allow for sorting of ingredients by description
     */
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
    /**
     * Test the comparing of ingredient dates
     * Allow for sorting of ingredients by date
     */
    @Test
    void testCompareToIngredientDate(){
        ArrayList<Ingredient> ingredientList = mockIngredientList();

        Collections.sort(ingredientList, Ingredient::compareToIngredientDate);

        assertTrue(ingredientList.get(0).getIngredientDate().equals("2021-01-28"));
        assertTrue(ingredientList.get(1).getIngredientDate().equals("2022-11-03"));
        assertTrue(ingredientList.get(2).getIngredientDate().equals("2022-11-04"));
        assertTrue(ingredientList.get(3).getIngredientDate().equals("2022-11-05"));
        assertTrue(ingredientList.get(4).getIngredientDate().equals("2022-12-04"));
        assertTrue(ingredientList.get(5).getIngredientDate().equals("2023-06-30"));
    }

    /**
     * Test the comparing of ingredient locations
     * Allow for sorting of ingredients by location
     */
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

    /**
     * Test the comparing of ingredient category
     * Allow for sorting of ingredients by category
     */
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
}
