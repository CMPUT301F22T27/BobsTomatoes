package com.example.bobstomatoes;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe implements Parcelable {
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
    protected Recipe(Parcel in) {
        recipeTitle = in.readString();
        recipeTime = in.readInt();
        recipeServings = in.readInt();
        recipeCategory = in.readString();
        recipeComments = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(recipeTitle);
        parcel.writeInt(recipeTime);
        parcel.writeInt(recipeServings);
        parcel.writeString(recipeCategory);
        parcel.writeString(recipeComments);
    }
}