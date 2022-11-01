package com.example.bobstomatoes;

import java.util.ArrayList;
import java.util.Date;

public class MealPlan {
    private ArrayList<Recipe> recipes;
    private ArrayList<Ingredient> ingredients;
    private Date date;

    public MealPlan(ArrayList<Recipe> recipes, ArrayList<Ingredient> ingredients, Date date){
        this.recipes = recipes;
        this.ingredients = ingredients;
        this.date = date;
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public Date getDate() {
        return date;
    }
}
