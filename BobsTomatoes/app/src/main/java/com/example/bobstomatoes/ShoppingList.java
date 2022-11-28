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
     *
     * @return
     */
    public HashMap<String, Boolean> getCheckedItems() {
        return checkedItems;
    }

    /**
     *
     * @return
     */
    public HashMap<String, Integer> getIngredientCount() {
        return ingredientCount;
    }

    /**
     *
     * @param ingredientCount
     */
    public void setIngredientCount(HashMap<String, Integer> ingredientCount) {
        this.ingredientCount = ingredientCount;
    }

    /**
     *
     * @param checkedItems
     */
    public void setCheckedItems(HashMap<String, Boolean> checkedItems) {
        this.checkedItems = checkedItems;
    }

    /**
     *
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     *
     * @param parcel
     * @param i
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    /**
     *
     * @param in
     */
    protected ShoppingList(Parcel in) {
    }

    /**
     *
     */
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
}
