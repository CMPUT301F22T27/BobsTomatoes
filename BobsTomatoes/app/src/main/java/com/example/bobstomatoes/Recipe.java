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

    //Need this and setters to pull from database
    public Recipe(){

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

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public void setRecipeTime(int recipeTime) {
        this.recipeTime = recipeTime;
    }

    public void setRecipeServings(int recipeServings) {
        this.recipeServings = recipeServings;
    }

    public void setRecipeCategory(String recipeCategory) {
        this.recipeCategory = recipeCategory;
    }

    public void setRecipeComments(String recipeComments) {
        this.recipeComments = recipeComments;
    }

    public void setRecipeIngredients(ArrayList<Ingredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

}