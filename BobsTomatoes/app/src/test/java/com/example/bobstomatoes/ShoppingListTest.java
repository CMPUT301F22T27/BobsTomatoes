package com.example.bobstomatoes;

import static org.junit.Assert.assertFalse;
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
import java.util.HashMap;

/**
 * ShoppingList test, test sorting of ingredients in shopping list by description & category, test will execute on android device
 */
public class ShoppingListTest {

    /**
     * Create an ingredient list
     * @return      return the new created ingredient list
     */
    private ArrayList<Ingredient> mockIngredientList1(){
        ArrayList<Ingredient> ingredientList= new ArrayList<>();
        ingredientList.add(mockIngredient());
        ingredientList.add(new Ingredient("Honeydew", "2022-12-04", "Pantry", 1,1,"Fruit"));
        ingredientList.add(new Ingredient("Cantaloupe", "2022-11-03", "Pantry", 2,2,"Fruit"));
        ingredientList.add(new Ingredient("Egg", "2022-11-05", "Fridge", 3,3,"Protein"));
        ingredientList.add(new Ingredient("Milk", "2021-01-28", "Fridge", 4,4,"Dairy"));
        ingredientList.add(new Ingredient("Bread", "2023-06-30", "Freezer", 5,5,"Grain"));
        ingredientList.add(new Ingredient("Ice Cream", "2025-06-30", "Pantry", 100,3,"Vegetable"));
        return ingredientList;
    }

    /**
     * Create a second ingredient list (with different amounts for the recipes and meal plans)
     * This will be compared to the ingredient list in the shopping list implementation
     * @return      return the new created ingredient list
     */
    private ArrayList<Ingredient> mockIngredientList2(){
        ArrayList<Ingredient> ingredientList= new ArrayList<>();
        ingredientList.add(mockIngredient());
        ingredientList.add(new Ingredient("Honeydew", "2022-12-04", "Pantry", 5,1,"Fruit"));
        ingredientList.add(new Ingredient("Cantaloupe", "2022-11-03", "Pantry", 4,2,"Fruit"));
        ingredientList.add(new Ingredient("Egg", "2022-11-05", "Fridge", 7,3,"Protein"));
        ingredientList.add(new Ingredient("Milk", "2021-01-28", "Fridge", 8,4,"Dairy"));
        ingredientList.add(new Ingredient("Bread", "2023-06-30", "Freezer", 1,5,"Grain"));
        ingredientList.add(new Ingredient("Ice Cream", "2025-06-30", "Pantry", 2,3,"Dairy"));
        return ingredientList;
    }

    private ArrayList<Ingredient> mockIngredientList3() {
        ArrayList<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(new Ingredient("Orange", "2022-07-25", "Pantry", 4, 2, "Dairy"));
        return ingredientList;
    }

    /**
     * Create a recipe list
     * @return      return the new created recipe list
     */
    private ArrayList<Recipe> mockRecipeList(){
        ArrayList<Recipe> recipeList= new ArrayList<>();
        recipeList.add(mockRecipe());
        recipeList.add(new Recipe("Tomato Soup", 20, 5, "Soup","Tasty",mockIngredientList2(), "Image"));
        recipeList.add(new Recipe("Potato Soup", 15, 3, "Soup","Good Soup",mockIngredientList2(), "Image"));
        recipeList.add(new Recipe("Caesar Salad", 17, 4, "Salad","Healthy",mockIngredientList2(), "Image"));
        recipeList.add(new Recipe("Chocolate Fondue", 30, 2, "Fondue","Dessert",mockIngredientList2(), "Image"));
        recipeList.add(new Recipe("Omelette", 10, 1, "Breakfast","Gourmet",mockIngredientList2(), "Image"));
        return recipeList;
    }

    /**
     * Create a meal plan list
     * @return      return the new created meal plan list
     */
    private ArrayList<MealPlan> mockMealPlanList(){
        ArrayList<MealPlan> mealPlanList= new ArrayList<>();
        mealPlanList.add(mockMealPlan());
        mealPlanList.add(new MealPlan("2022-01-05", mockRecipeList(), mockIngredientList3()));
        mealPlanList.add(new MealPlan("2022-03-07", mockRecipeList(), mockIngredientList3()));
        mealPlanList.add(new MealPlan("2020-10-23", mockRecipeList(), mockIngredientList3()));
        mealPlanList.add(new MealPlan("2024-05-16", mockRecipeList(), mockIngredientList3()));
        mealPlanList.add(new MealPlan("2023-11-09", mockRecipeList(), mockIngredientList3()));
        return mealPlanList;
    }

    /**
     * Create a new ingredient
     * @return      return new ingredient
     */
    private Ingredient mockIngredient(){
        return new Ingredient("Mango", "2022-11-04", "Fridge", 6, 6, "Fruit");
    }

    /**
     * Create a new recipe
     * @return      return new recipe
     */
    private Recipe mockRecipe(){
        return new Recipe("Mushroom Soup", 12, 6, "Soup", "Hot", mockIngredientList2(), "Image");
    }

    /**
     * Create a new meal plan
     * @return      return new meal plan
     */
    private MealPlan mockMealPlan(){
        return new MealPlan("2022-11-27", mockRecipeList(), mockIngredientList2());
    }

    /**
     * Create a new ingredient database
     * @return      return new ingredient database
     */
    private IngredientDB mockIngredientDB(){
        return new IngredientDB();
    }

    /**
     * Create a new recipe database
     * @return      return new recipe
     */
    private RecipeDB mockRecipeDB(){
        return new RecipeDB();
    }

    /**
     * Create a new meal plan database
     * @return      return new meal plan database
     */
    private MealPlanDB mockMealPlanDB(){
        return new MealPlanDB();
    }

    ShoppingList shoppingList;
    ArrayList<Ingredient> neededIngredients = new ArrayList<>();

    /**
     * Creates a list of needed ingredient objects (needed to create list
     * of differing ingredients in storage and meal plans)
     */
    private void mockCreateNeededIngredients(){

        neededIngredients = new ArrayList<>();

        // Add ingredient objects present in shopping list
        for(int i = 0; i < mockIngredientList1().size(); i++){

            Ingredient tempIngredient = mockIngredientList1().get(i);

            String tempIngredientName = tempIngredient.getIngredientDesc();

            // If ingredient needed
            if(shoppingList.getIngredientCount().get(tempIngredientName) != null){

                int have = tempIngredient.getIngredientAmount();
                int total = shoppingList.getIngredientCount().get(tempIngredientName);
                int need = total - have;

                if(need > 0){

                    Ingredient neededIngredient = tempIngredient.clone();
                    neededIngredient.setIngredientAmount(need);
                    neededIngredients.add(neededIngredient);

                }
            }
        }
    }

    /**
     * Handles creation of the shopping list using the current meal plan
     */
    private void mockCreateShoppingList(){

        // Get all recipes and ingredients from all meal plans

        // Stores ingredient paired to the amount needed
        HashMap<String, Integer> numberOfIngredients = new HashMap<>();

        // Stores ingredient name paired to boolean value
        HashMap<String, Boolean> checkedIngredients = new HashMap<>();

        // Stores recipes, might not need this
        ArrayList<Recipe> allRecipes = new ArrayList<>();

        for(int i = 0; i < mockMealPlanList().size(); i++){

            // Grab one meal plan from database
            MealPlan tempMealPlan = mockMealPlanList().get(i);

            // Grab list of recipes from that meal plan
            ArrayList<Recipe> recipesInTempMealPlan = tempMealPlan.getMealPlanRecipes();

            // Iterate over recipes in that meal plan
            for(int j = 0; j < recipesInTempMealPlan.size(); j++){

                // Add recipe to allRecipes
                allRecipes.add(recipesInTempMealPlan.get(j));

                // Get recipe
                Recipe tempRecipe = recipesInTempMealPlan.get(j);

                // Get list of ingredients in recipe
                ArrayList<Ingredient> ingredientsInTempRecipe = tempRecipe.getRecipeIngredients();

                // Iterate over ingredients in that recipe
                for(int k = 0; k < ingredientsInTempRecipe.size(); k++){

                    // Get ingredient
                    Ingredient tempIngredient = ingredientsInTempRecipe.get(k);

                    // Get ingredient name
                    String ingredientName = tempIngredient.getIngredientDesc();

                    // Get number of that ingredient in recipe
                    int numIngredient = tempIngredient.getIngredientAmount();

                    // Check if we have seen this ingredient before in meal plans
                    if (checkedIngredients.get(ingredientName) == null){

                        // add it to checkedIngredients
                        checkedIngredients.put(ingredientName, false);

                        // add it to num of ingredients
                        numberOfIngredients.put(ingredientName, numIngredient);

                    } else {

                        // If we have seen it before, we need to add number of it to num ingredients
                        int currentNum = numberOfIngredients.get(ingredientName);

                        currentNum = currentNum + numIngredient;

                        numberOfIngredients.put(ingredientName, currentNum);

                    }
                }
            }

            // Add ingredients in mealplan


            // Get list of ingredients in meal plan
            ArrayList<Ingredient> ingredientsInTempMealPlan = tempMealPlan.getMealPlanIngredients();

            // Get hashmap of number of ingredients in meal plan
            // HashMap<String, Integer> ingredientCountsMP = tempMealPlan.getIngredientCounts();
            // not implemented

            // Iterate over ingredients in that recipe
            for(int k = 0; k < ingredientsInTempMealPlan.size(); k++){

                // Get ingredient
                Ingredient tempIngredient = ingredientsInTempMealPlan.get(k);

                // Get ingredient name
                String ingredientName = tempIngredient.getIngredientDesc();

                // Get number of that ingredient in recipe
                int numIngredient = tempIngredient.getIngredientAmount();
                // int numIngredient = 1;

                // Check if we have seen this ingredient before in meal plans
                if (checkedIngredients.get(ingredientName) == null){

                    // add it to checkedIngredients
                    checkedIngredients.put(ingredientName, false);

                    // add it to num of ingredients
                    numberOfIngredients.put(ingredientName, numIngredient);

                } else {

                    // If we have seen it before, we need to add number of it to num ingredients
                    int currentNum = numberOfIngredients.get(ingredientName);

                    currentNum = currentNum + numIngredient;

                    numberOfIngredients.put(ingredientName, currentNum);

                }
            }
        }

        shoppingList = new ShoppingList(checkedIngredients, numberOfIngredients);

    }


    /**
     * Test the comparing of ingredient descriptions
     * Allow for sorting of ingredients by description
     */
    @Test
    void testCompareToIngredientInShoppingListDesc(){
        mockCreateShoppingList();
        mockCreateNeededIngredients();

        Collections.sort(neededIngredients, Ingredient::compareToIngredientDesc);

        assertTrue(neededIngredients.get(0).getIngredientDesc().equals("Bread"));
        assertTrue(neededIngredients.get(1).getIngredientDesc().equals("Cantaloupe"));
        assertTrue(neededIngredients.get(2).getIngredientDesc().equals("Egg"));
        assertTrue(neededIngredients.get(3).getIngredientDesc().equals("Honeydew"));
        assertFalse(neededIngredients.get(4).getIngredientDesc().equals("Ice Cream"));
        assertTrue(neededIngredients.get(4).getIngredientDesc().equals("Mango"));
        assertTrue(neededIngredients.get(5).getIngredientDesc().equals("Milk"));
    }

    /**
     * Test the comparing of ingredient category
     * Allow for sorting of ingredients by category
     */
    @Test
    void testCompareToIngredientInShoppingListCategory(){
        mockCreateShoppingList();
        mockCreateNeededIngredients();

        Collections.sort(neededIngredients, Ingredient::compareToIngredientCategory);

        assertTrue(neededIngredients.get(0).getIngredientCategory().equals("Dairy"));
        assertFalse(neededIngredients.get(1).getIngredientCategory().equals("Dairy"));
        assertTrue(neededIngredients.get(1).getIngredientCategory().equals("Fruit"));
        assertTrue(neededIngredients.get(2).getIngredientCategory().equals("Fruit"));
        assertTrue(neededIngredients.get(3).getIngredientCategory().equals("Fruit"));
        assertTrue(neededIngredients.get(4).getIngredientCategory().equals("Grain"));
        assertTrue(neededIngredients.get(5).getIngredientCategory().equals("Protein"));
    }
}
