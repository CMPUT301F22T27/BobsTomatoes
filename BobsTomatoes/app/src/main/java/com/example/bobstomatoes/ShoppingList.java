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

    /**
     * getCheckedItems getter
     * Contains hashmap mapping String ingredient name - boolean isChecked()
     * @return
     */
    public HashMap<String, Boolean> getCheckedItems() {
        return checkedItems;
    }

    /**
     * getIngredientCount getter
     * Contains hashmap mapping String ingredient name - int number of ingredient in meal plans
     * @return
     */
    public HashMap<String, Integer> getIngredientCount() {
        return ingredientCount;
    }

    /**
     * IngredientCount setter
     * @param ingredientCount
     */
    public void setIngredientCount(HashMap<String, Integer> ingredientCount) {
        this.ingredientCount = ingredientCount;
    }

    /**
     * CheckedItems setter
     * @param checkedItems
     */
    public void setCheckedItems(HashMap<String, Boolean> checkedItems) {
        this.checkedItems = checkedItems;
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
     * Write shoppingList to parcel
     * (Not implemented)
     * @param parcel
     * @param i
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    /**
     * Create shopping list from parcel
     * (Not implemented)
     * @param in
     */
    protected ShoppingList(Parcel in) {
    }

    /**
     * CREATOR for parcelable implementation
     */
    public static final Creator<ShoppingList> CREATOR = new Creator<ShoppingList>() {

        /**
         * Create shopping list from parcel
         * @param in
         * @return
         */
        @Override
        public ShoppingList createFromParcel(Parcel in) {
            return new ShoppingList(in);
        }

        /**
         * Create array of shoppingList
         * @param size
         * @return
         */
        @Override
        public ShoppingList[] newArray(int size) {
            return new ShoppingList[size];
        }
    };
}
