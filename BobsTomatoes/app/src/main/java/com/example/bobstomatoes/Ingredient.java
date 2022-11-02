package com.example.bobstomatoes;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Ingredient implements Parcelable {
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

    public Ingredient() {

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

    public void setIngredientDesc(String ingredientDesc) {
        this.ingredientDesc = ingredientDesc;
    }

    public void setIngredientDate(String ingredientDate) {
        this.ingredientDate = ingredientDate;
    }

    public void setIngredientLocation(String ingredientLocation) {
        this.ingredientLocation = ingredientLocation;
    }

    public void setIngredientAmount(int ingredientAmount) {
        this.ingredientAmount = ingredientAmount;
    }

    public void setIngredientUnit(int ingredientUnit) {
        this.ingredientUnit = ingredientUnit;
    }

    public void setIngredientCategory(String ingredientCategory) {
        this.ingredientCategory = ingredientCategory;
    }

    protected Ingredient(Parcel in) {
        ingredientDesc = in.readString();
        ingredientDate = in.readString();
        ingredientLocation = in.readString();
        ingredientAmount = in.readInt();
        ingredientUnit = in.readInt();
        ingredientCategory = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ingredientDesc);
        parcel.writeString(ingredientDate);
        parcel.writeString(ingredientLocation);
        parcel.writeInt(ingredientAmount);
        parcel.writeInt(ingredientUnit);
        parcel.writeString(ingredientCategory);
    }
}