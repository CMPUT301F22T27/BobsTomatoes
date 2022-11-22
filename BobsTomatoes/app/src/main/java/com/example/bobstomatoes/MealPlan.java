package com.example.bobstomatoes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Class detailing the information of a meal plan
 * implements Parcelable
 */
public class MealPlan implements Parcelable {


    private ArrayList<Recipe> mealPlanRecipes;
    private ArrayList<Ingredient> mealPlanIngredients;
    private String mealPlanDate;

    /**
     * MealPlan constructor, takes in recipes, ingredients, date
     * @param date          the date specifying the time span of the meal plan
     * @param recipes       array list of recipes that the meal plan uses
     * @param ingredients   array list of ingredients that the meal plan uses
     */
    public MealPlan(String date, ArrayList<Recipe> recipes, ArrayList<Ingredient> ingredients){
        this.mealPlanDate = date;
        this.mealPlanRecipes = recipes;
        this.mealPlanIngredients = ingredients;

    }

    public MealPlan(){
        // For callback
    }


    public ArrayList<Recipe> getMealPlanRecipes() {
        return mealPlanRecipes;
    }

    public void setMealPlanRecipes(ArrayList<Recipe> mealPlanRecipes) {
        this.mealPlanRecipes = mealPlanRecipes;
    }

    public ArrayList<Ingredient> getMealPlanIngredients() {
        return mealPlanIngredients;
    }

    public void setMealPlanIngredients(ArrayList<Ingredient> mealPlanIngredients) {
        this.mealPlanIngredients = mealPlanIngredients;
    }

    public String getMealPlanDate() {
        return mealPlanDate;
    }

    public void setMealPlanDate(String mealPlanDate) {
        this.mealPlanDate = mealPlanDate;
    }

//    /**
//     * Recipes getter
//     * Retrieve list of recipes used in meal plan, allow accessibility to other classes
//     * @return      returns the list of recipes
//     */
//    public ArrayList<Recipe> getRecipes() {
//        return recipes;
//    }
//
//    /**
//     * Ingredients getter
//     * Retrieve list of ingredients used in meal plan, allow accessibility to other classes
//     * @return      returns the list of ingredients
//     */
//    public ArrayList<Ingredient> getIngredients() {
//        return ingredients;
//    }
//
//    /**
//     * Date getter
//     * Retrieve date of meal plan, allow accessibility to other classes
//     * @return      returns the meal plan date
//     */
//    public String getDate() {
//        return date;
//    }
//
//    public void setRecipes(ArrayList<Recipe> recipes) {
//        this.recipes = recipes;
//    }
//
//    public void setIngredients(ArrayList<Ingredient> ingredients) {
//        this.ingredients = ingredients;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }

    /**
     * MealPlan constructor, takes in a parcel
     * @param in    parcel containing an meal plan's information (recipes, ingredients, etc)
     */
    protected MealPlan(Parcel in) {
        mealPlanRecipes = in.createTypedArrayList(Recipe.CREATOR);
        mealPlanIngredients = in.createTypedArrayList(Ingredient.CREATOR);
    }

    public static final Creator<MealPlan> CREATOR = new Creator<MealPlan>() {
        /**
         * Create new instance of the Parcelable Class
         * @param in    parcel containing an meal plan's information (recipes, ingredients, etc)
         * @return      returns a new created MealPlan
         */
        @Override
        public MealPlan createFromParcel(Parcel in) {
            return new MealPlan(in);
        }

        /**
         * Create a new array of the Parcelable Class
         * @param size  size of new array
         * @return      returns a new MealPlan Array
         */
        @Override
        public MealPlan[] newArray(int size) {
            return new MealPlan[size];
        }
    };

    /**
     * Create bitmask return value
     * @return      return value
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Parcel writer
     * Creates parcel with specified object written in
     * @param parcel    parcel in which object should be written
     * @param i         addition flags of how object should be written
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(mealPlanRecipes);
        parcel.writeTypedList(mealPlanIngredients);
    }
}
