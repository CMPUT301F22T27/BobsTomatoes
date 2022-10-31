package com.example.bobstomatoes;

import java.util.ArrayList;
import java.util.Date;

public class ShoppingList {

    private ArrayList<Ingredient> ingredients;
    private ArrayList<MealPlan> mealPlans;

    public ShoppingList(ArrayList<Ingredient> ingredients, ArrayList<MealPlan> mealPlans){
        this.ingredients = ingredients;
        this.mealPlans = mealPlans;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<MealPlan> getMealPlans() {
        return mealPlans;
    }

}
