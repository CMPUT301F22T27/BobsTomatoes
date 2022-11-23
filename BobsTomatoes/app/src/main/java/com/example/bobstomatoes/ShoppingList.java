package com.example.bobstomatoes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Class detailing the information of a ShoppingList
 * implements Parcelable
 */
public class ShoppingList implements Parcelable {

    private HashMap<String, Boolean> checkedItems;
    private HashMap<String, Integer> ingredientCount;


    /**
     * ShoppingList Constructor, takes in ingredients, and meal plans
     * @param checkedItems   tracks if an ingredient is checked
     * @param ingredientCount     ingredients needed for mealplans
     */
    public ShoppingList(HashMap<String, Boolean> checkedItems, HashMap<String, Integer> ingredientCount){
        this.checkedItems = checkedItems;
        this.ingredientCount = ingredientCount;
    }



    public HashMap<String, Boolean> getCheckedItems() {
        return checkedItems;
    }

    public HashMap<String, Integer> getIngredientCount() {
        return ingredientCount;
    }

    public void setIngredientCount(HashMap<String, Integer> ingredientCount) {
        this.ingredientCount = ingredientCount;
    }

    public void setCheckedItems(HashMap<String, Boolean> checkedItems) {
        this.checkedItems = checkedItems;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    protected ShoppingList(Parcel in) {
    }

    public static final Creator<ShoppingList> CREATOR = new Creator<ShoppingList>() {
        @Override
        public ShoppingList createFromParcel(Parcel in) {
            return new ShoppingList(in);
        }

        @Override
        public ShoppingList[] newArray(int size) {
            return new ShoppingList[size];
        }
    };

//    /**
//     * Create bitmask return value
//     * @return      return value
//     */
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    /**
//     * ShoppingList constructor, takes in a parcel
//     * @param in    parcel containing shopping list information (list of ingredients, list of meal plans)
//     */
//    protected ShoppingList(Parcel in) {
//        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
//        mealPlans = in.createTypedArrayList(MealPlan.CREATOR);
//    }
//
//    public static final Creator<ShoppingList> CREATOR = new Creator<ShoppingList>() {
//        /**
//         * Create new instance of the Parcelable Class
//         * @param in    parcel containing shopping list information (list of ingredients, list of meal plans)
//         * @return      returns a new created ShoppingList
//         */
//        @Override
//        public ShoppingList createFromParcel(Parcel in) {
//            return new ShoppingList(in);
//        }
//
//        /**
//         * Create a new array of the Parcelable Class
//         * @param size  size of new array
//         * @return      returns a new ShoppingList Array
//         */
//        @Override
//        public ShoppingList[] newArray(int size) {
//            return new ShoppingList[size];
//        }
//    };
//
//    /**
//     * Parcel writer
//     * Creates parcel with specified object written in
//     * @param parcel    parcel in which object should be written
//     * @param i         addition flags of how object should be written
//     */
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeTypedList(ingredients);
//        parcel.writeTypedList(mealPlans);
//    }
}
