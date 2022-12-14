package com.example.bobstomatoes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Recipe test, test sorting of recipes by title, time, servings, category, test will execute on an android device
 */
public class RecipeTest {

    /**
     * Create an ingredient list
     * @return      return the new created ingredient list
     */
    ArrayList<Ingredient> mockIngredientList(){
        ArrayList<Ingredient> ingredientList= new ArrayList<>();
        ingredientList.add(new Ingredient("Honeydew", "2022-12-04", "Pantry", 1,1,"Fruit"));
        ingredientList.add(new Ingredient("Cantaloupe", "2022-11-03", "Pantry", 2,2,"Fruit"));
        ingredientList.add(new Ingredient("Egg", "2022-11-05", "Fridge", 3,3,"Protein"));
        ingredientList.add(new Ingredient("Milk", "2021-01-28", "Fridge", 4,4,"Dairy"));
        ingredientList.add(new Ingredient("Bread", "2023-06-30", "Freezer", 5,5,"Grain"));
        return ingredientList;
    }

    /**
     * Create a recipe list
     * @return      return the new created recipe list
     */
    private ArrayList<Recipe> mockRecipeList(){
        ArrayList<Recipe> recipeList= new ArrayList<>();
        recipeList.add(mockRecipe());
        recipeList.add(new Recipe("Tomato Soup", 20, 5, "Soup","Tasty",mockIngredientList(), "Image"));
        recipeList.add(new Recipe("Potato Soup", 15, 3, "Soup","Good Soup",mockIngredientList(), "Image"));
        recipeList.add(new Recipe("Caesar Salad", 17, 4, "Salad","Healthy",mockIngredientList(), "Image"));
        recipeList.add(new Recipe("Chocolate Fondue", 30, 2, "Fondue","Dessert",mockIngredientList(), "Image"));
        recipeList.add(new Recipe("Omelette", 10, 1, "Breakfast","Gourmet",mockIngredientList(), "Image"));
        return recipeList;
    }

    /**
     * Create a new recipe
     * @return      return new recipe
     */
    private Recipe mockRecipe(){
        return new Recipe("Mushroom Soup", 12, 6, "Soup", "Hot", mockIngredientList(), "Image");
    }

    /**
     * Create a new recipe database
     * @return      return new recipe
     */
    private RecipeDB mockRecipeDB(){
        return new RecipeDB();
    }

    /**
     * Test the comparing of recipe title
     * Allow for sorting of recipes by titles
     */
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

    /**
     * Test the comparing of recipe time
     * Allow for sorting of recipe by time
     */
    @Test
    void testCompareToRecipeTime(){
        ArrayList<Recipe> recipeList = mockRecipeList();

        Collections.sort(recipeList, Recipe::compareToRecipeTime);

        assertTrue(recipeList.get(0).getRecipeTime() == 30);
        assertTrue(recipeList.get(1).getRecipeTime() == 20);
        assertTrue(recipeList.get(2).getRecipeTime() == 17);
        assertTrue(recipeList.get(3).getRecipeTime() == 15);
        assertTrue(recipeList.get(4).getRecipeTime() == 12);
        assertTrue(recipeList.get(5).getRecipeTime() == 10);
    }

    /**
     * Test the comparing of recipe serving size
     * Allow for sorting of recipes by serving size
     */
    @Test
    void testCompareToRecipeServings(){
        ArrayList<Recipe> recipeList = mockRecipeList();

        Collections.sort(recipeList, Recipe::compareToRecipeServings);

        assertTrue(recipeList.get(0).getRecipeServings() == 6);
        assertTrue(recipeList.get(1).getRecipeServings() == 5);
        assertTrue(recipeList.get(2).getRecipeServings() == 4);
        assertTrue(recipeList.get(3).getRecipeServings() == 3);
        assertTrue(recipeList.get(4).getRecipeServings() == 2);
        assertTrue(recipeList.get(5).getRecipeServings() == 1);
    }

    /**
     * Test the comparing of recipe category
     * Allow for sorting of recipes by categories
     */
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