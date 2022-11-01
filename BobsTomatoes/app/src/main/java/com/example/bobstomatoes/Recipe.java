package com.example.bobstomatoes;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe implements Serializable {
    private String recipeTitle;
    private int recipeTime;
    private int recipeServings;
    private String recipeCategory;
    private String recipeComments;
    private ArrayList<Ingredient> recipeIngredients;

    public Recipe(String recipeTitle, int recipeTime, int recipeServings, String recipeCategory, String recipeComments, ArrayList<Ingredient> recipeIngredients) {
        this.recipeTitle = recipeTitle;
        this.recipeTime = recipeTime;
        this.recipeServings = recipeServings;
        this.recipeCategory = recipeCategory;
        this.recipeComments = recipeComments;
        this.recipeIngredients = recipeIngredients;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public int getRecipeTime() {
        return recipeTime;
    }

    public int getRecipeServings() { return recipeServings; }

    public String getRecipeCategory() {
        return recipeCategory;
    }

    public String getRecipeComments() {
        return recipeComments;
    }

    public ArrayList<Ingredient> getRecipeIngredients() {
        return recipeIngredients;
    }
}