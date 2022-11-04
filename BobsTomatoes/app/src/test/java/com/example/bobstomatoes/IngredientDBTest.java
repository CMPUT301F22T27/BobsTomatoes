package com.example.bobstomatoes;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

public class IngredientDBTest {

    private ArrayList<Ingredient> mockIngredientList(){
        ArrayList<Ingredient> ingredientList= new ArrayList<>();
        ingredientList.add(mockIngredient());
        ingredientList.add(new Ingredient("Honeydew", "2022-12-04", "Pantry", 1,1,"Fruit"));
        ingredientList.add(new Ingredient("Cantaloupe", "2022-11-03", "Pantry", 2,2,"Fruit"));
        ingredientList.add(new Ingredient("Egg", "2022-11-05", "Fridge", 3,3,"Protein"));
        ingredientList.add(new Ingredient("Milk", "2022-01-28", "Fridge", 4,4,"Dairy"));
        ingredientList.add(new Ingredient("Bread", "2022-6-30", "Freezer", 5,5,"Grain"));
        return ingredientList;
    }

    private Ingredient mockIngredient(){
        return new Ingredient("Mango", "2022-11-04", "Fridge", 6, 6, "Fruit");
    }

    @Test
    void testCompareToIngredientDesc(){
        //Ingredient ingredient1 = mockIngredient();
        //        Ingredient ingredient2 = new Ingredient("Honeydew", "2022-12-04", "Pantry", 1,1,"Fruit");
        //        Ingredient ingredient3 = new Ingredient("Cantaloupe", "2022-11-03", "Pantry", 2,2,"Fruit");
        //        Ingredient ingredient4 = new Ingredient("Egg", "2022-11-05", "Fridge", 3,3,"Protein");
        //        Ingredient ingredient5 = new Ingredient("Milk", "2022-01-28", "Fridge", 4,4,"Dairy");
        //        Ingredient ingredient6 = new Ingredient("Bread", "2022-6-30", "Freezer", 5,5,"Grain");

        ArrayList<Ingredient> ingredientList = mockIngredientList();

        Collections.sort(ingredientList, Ingredient::compareToIngredientDesc);

        assertTrue(ingredientList.get(0).getIngredientDesc().equals("Bread"));
        assertTrue(ingredientList.get(1).getIngredientDesc().equals("Cantaloupe"));
        assertTrue(ingredientList.get(2).getIngredientDesc().equals("Egg"));
        assertTrue(ingredientList.get(3).getIngredientDesc().equals("Honeydew"));
        assertTrue(ingredientList.get(4).getIngredientDesc().equals("Mango"));
        assertTrue(ingredientList.get(5).getIngredientDesc().equals("Milk"));
    }
}
