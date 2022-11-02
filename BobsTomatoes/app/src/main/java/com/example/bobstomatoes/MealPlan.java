package com.example.bobstomatoes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

public class MealPlan implements Parcelable {
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

    protected MealPlan(Parcel in) {
        recipes = in.createTypedArrayList(Recipe.CREATOR);
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
    }

    public static final Creator<MealPlan> CREATOR = new Creator<MealPlan>() {
        @Override
        public MealPlan createFromParcel(Parcel in) {
            return new MealPlan(in);
        }

        @Override
        public MealPlan[] newArray(int size) {
            return new MealPlan[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(recipes);
        parcel.writeTypedList(ingredients);
    }
}
