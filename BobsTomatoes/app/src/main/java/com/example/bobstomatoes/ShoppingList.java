package com.example.bobstomatoes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

public class ShoppingList implements Parcelable {

    private ArrayList<Ingredient> ingredients;
    private ArrayList<MealPlan> mealPlans;

    public ShoppingList(ArrayList<Ingredient> ingredients, ArrayList<MealPlan> mealPlans){
        this.ingredients = ingredients;
        this.mealPlans = mealPlans;
    }


    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<MealPlan> getMealPlans() {
        return mealPlans;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected ShoppingList(Parcel in) {
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        mealPlans = in.createTypedArrayList(MealPlan.CREATOR);
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

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(ingredients);
        parcel.writeTypedList(mealPlans);
    }
}
