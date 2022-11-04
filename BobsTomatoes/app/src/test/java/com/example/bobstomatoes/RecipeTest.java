package com.example.bobstomatoes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collections;

public class RecipeTest {

    ArrayList<Ingredient> mockIngredientList(){
        ArrayList<Ingredient> ingredientList= new ArrayList<>();
        ingredientList.add(new Ingredient("Honeydew", "2022-12-04", "Pantry", 1,1,"Fruit"));
        ingredientList.add(new Ingredient("Cantaloupe", "2022-11-03", "Pantry", 2,2,"Fruit"));
        ingredientList.add(new Ingredient("Egg", "2022-11-05", "Fridge", 3,3,"Protein"));
        ingredientList.add(new Ingredient("Milk", "2021-01-28", "Fridge", 4,4,"Dairy"));
        ingredientList.add(new Ingredient("Bread", "2023-06-30", "Freezer", 5,5,"Grain"));
        return ingredientList;
    }

    private ArrayList<Recipe> mockRecipeList(){
        ArrayList<Recipe> recipeList= new ArrayList<>();
        recipeList.add(mockRecipe());
        recipeList.add(new Recipe("Tomato Soup", 20, 5, "Soup","Tasty",mockIngredientList()));
        recipeList.add(new Recipe("Potato Soup", 15, 3, "Soup","Good Soup",mockIngredientList()));
        recipeList.add(new Recipe("Caesar Salad", 17, 4, "Salad","Healthy",mockIngredientList()));
        recipeList.add(new Recipe("Chocolate Fondue", 30, 2, "Fondue","Dessert",mockIngredientList()));
        recipeList.add(new Recipe("Omelette", 10, 1, "Breakfast","Gourmet",mockIngredientList()));
        return recipeList;
    }

    private Recipe mockRecipe(){
        return new Recipe("Mushroom Soup", 12, 6, "Soup", "Hot", mockIngredientList());
    }

    private RecipeDB mockRecipeDB(){
        return new RecipeDB();
    }

    @Test
    void testCompareToRecipeTitle(){
        ArrayList<Recipe> recipeList = mockRecipeList();

        Collections.sort(recipeList, Recipe::compareToRecipeTitle);

        assertTrue(recipeList.get(0).getRecipeTitle().equals("Caesar Salad"));
        assertTrue(recipeList.get(1).getRecipeTitle().equals("Chocolate Fondue"));
        assertTrue(recipeList.get(2).getRecipeTitle().equals("Mushroom Soup"));
        assertTrue(recipeList.get(3).getRecipeTitle().equals("Omelette"));
        assertTrue(recipeList.get(4).getRecipeTitle().equals("Potato Soup"));
        assertTrue(recipeList.get(5).getRecipeTitle().equals("Tomato Soup"));
    }

    @Test
    void testCompareToRecipeTime(){
        ArrayList<Recipe> recipeList = mockRecipeList();

        Collections.sort(recipeList, Recipe::compareToRecipeTime);

        assertTrue(recipeList.get(0).getRecipeTime() == 10);
        assertTrue(recipeList.get(1).getRecipeTime() == 12);
        assertTrue(recipeList.get(2).getRecipeTime() == 15);
        assertTrue(recipeList.get(3).getRecipeTime() == 17);
        assertTrue(recipeList.get(4).getRecipeTime() == 20);
        assertTrue(recipeList.get(5).getRecipeTime() == 30);
    }

    @Test
    void testCompareToRecipeServings(){
        ArrayList<Recipe> recipeList = mockRecipeList();

        Collections.sort(recipeList, Recipe::compareToRecipeServings);

        assertTrue(recipeList.get(0).getRecipeServings() == 1);
        assertTrue(recipeList.get(1).getRecipeServings() == 2);
        assertTrue(recipeList.get(2).getRecipeServings() == 3);
        assertTrue(recipeList.get(3).getRecipeServings() == 4);
        assertTrue(recipeList.get(4).getRecipeServings() == 5);
        assertTrue(recipeList.get(5).getRecipeServings() == 6);
    }

    @Test
    void testCompareToRecipeCategory(){
        ArrayList<Recipe> recipeList = mockRecipeList();

        Collections.sort(recipeList, Recipe::compareToRecipeCategory);

        assertTrue(recipeList.get(0).getRecipeCategory().equals("Breakfast"));
        assertTrue(recipeList.get(1).getRecipeCategory().equals("Fondue"));
        assertTrue(recipeList.get(2).getRecipeCategory().equals("Salad"));
        assertTrue(recipeList.get(3).getRecipeCategory().equals("Soup"));
        assertTrue(recipeList.get(4).getRecipeCategory().equals("Soup"));
        assertTrue(recipeList.get(5).getRecipeCategory().equals("Soup"));
    }

}