package com.example.bobstomatoes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Class detailing the information of a ShoppingList
 * implements Parcelable
 */
public class ShoppingList implements Parcelable {

    private ArrayList<Ingredient> ingredients;
    private ArrayList<MealPlan> mealPlans;

    /**
     * ShoppingList Constructor, takes in ingredients, and meal plans
     * @param ingredients   ingredients to buy
     * @param mealPlans     meal plans to guide shopping list
     */
    public ShoppingList(ArrayList<Ingredient> ingredients, ArrayList<MealPlan> mealPlans){
        this.ingredients = ingredients;
        this.mealPlans = mealPlans;
    }

    /**
     * Ingredients list getter
     * Retrieve list of ingredients to buy, allow for accessibility to other classes
     * @return      returns the list of ingredients
     */
    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * Meal plan list getter
     * Retrieve the meal plans that will guide the shopping, allow for accessibility to other classes
     * @return      returns the list of meal plans
     */
    public ArrayList<MealPlan> getMealPlans() {
        return mealPlans;
    }

    /**
     * Create bitmask return value
     * @return      return value
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * ShoppingList constructor, takes in a parcel
     * @param in    parcel containing shopping list information (list of ingredients, list of meal plans)
     */
    protected ShoppingList(Parcel in) {
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        mealPlans = in.createTypedArrayList(MealPlan.CREATOR);
    }

    public static final Creator<ShoppingList> CREATOR = new Creator<ShoppingList>() {
        /**
         * Create new instance of the Parcelable Class
         * @param in    parcel containing shopping list information (list of ingredients, list of meal plans)
         * @return      returns a new created ShoppingList
         */
        @Override
        public ShoppingList createFromParcel(Parcel in) {
            return new ShoppingList(in);
        }

        /**
         * Create a new array of the Parcelable Class
         * @param size  size of new array
         * @return      returns a new ShoppingList Array
         */
        @Override
        public ShoppingList[] newArray(int size) {
            return new ShoppingList[size];
        }
    };

    /**
     * Parcel writer
     * Creates parcel with specified object written in
     * @param parcel    parcel in which object should be written
     * @param i         addition flags of how object should be written
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(ingredients);
        parcel.writeTypedList(mealPlans);
    }
}
