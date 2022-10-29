package com.example.bobstomatoes;

public class Ingredient {
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
}
