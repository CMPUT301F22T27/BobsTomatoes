package com.example.bobstomatoes;

import java.io.Serializable;

public class Ingredient implements Serializable {
    private String ingredientDesc;
    private String ingredientDate;
    private String ingredientLocation;
    private int ingredientAmount;
    private int ingredientUnit;
    private String ingredientCategory;

    public Ingredient(String ingredientDesc, String ingredientDate, String ingredientLocation, int ingredientAmount, int ingredientUnit, String ingredientCategory) {
        this.ingredientDesc = ingredientDesc;
        this.ingredientDate = ingredientDate;
        this.ingredientLocation = ingredientLocation;
        this.ingredientAmount = ingredientAmount;
        this.ingredientUnit = ingredientUnit;
        this.ingredientCategory = ingredientCategory;
    }

    public String getIngredientDesc() {
        return ingredientDesc;
    }

    public String getIngredientDate() {
        return ingredientDate;
    }

    public String getIngredientLocation() {
        return ingredientLocation;
    }

    public int getIngredientAmount() {
        return ingredientAmount;
    }

    public int getIngredientUnit() {
        return ingredientUnit;
    }

    public String getIngredientCategory() {
        return ingredientCategory;
    }
}