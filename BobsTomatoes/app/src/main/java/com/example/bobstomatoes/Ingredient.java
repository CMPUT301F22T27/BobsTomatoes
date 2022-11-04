package com.example.bobstomatoes;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Class detailing the information of an ingredient
 * Implements Parcelable
 */
public class Ingredient implements Parcelable {
    private String ingredientDesc;
    private String ingredientDate;
    private String ingredientLocation;
    private int ingredientAmount;
    private int ingredientUnit;
    private String ingredientCategory;

    /**
     * Ingredient constructor, takes in the description, date, location, amount, unit, and category of the ingredient
     * @param ingredientDesc        description of the ingredient
     * @param ingredientDate        best before date of the ingredient
     * @param ingredientLocation    storage location of the ingredient
     * @param ingredientAmount      amount of the ingredient
     * @param ingredientUnit        unit cost of the ingredient
     * @param ingredientCategory    category of the ingredient
     */
    public Ingredient(String ingredientDesc, String ingredientDate, String ingredientLocation, int ingredientAmount, int ingredientUnit, String ingredientCategory) {
        this.ingredientDesc = ingredientDesc;
        this.ingredientDate = ingredientDate;
        this.ingredientLocation = ingredientLocation;
        this.ingredientAmount = ingredientAmount;
        this.ingredientUnit = ingredientUnit;
        this.ingredientCategory = ingredientCategory;
    }

    /**
     * Ingredient constructor, is an empty constructor, takes in nothing
     */
    public Ingredient() {

    }

    /**
     * Ingredient constructor, takes in a parcel
     * @param in    parcel containing an ingredient's information (description, date, etc)
     */
    protected Ingredient(Parcel in) {
        ingredientDesc = in.readString();
        ingredientDate = in.readString();
        ingredientLocation = in.readString();
        ingredientAmount = in.readInt();
        ingredientUnit = in.readInt();
        ingredientCategory = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        /**
         * Create new instance of the Parcelable Class
         * @param in    parcel containing an ingredient's information (description, date, etc)
         * @return      returns a new created Ingredient
         */
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        /**
         * Create a new array of the Parcelable Class
         * @param size
         * @return      returns a new Ingredient Array
         */
        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    /**
     * Description getter
     * Retrieve description of the ingredient, allow for accessibility to other classes
     * @return      returns the description of the ingredient
     */
    public String getIngredientDesc() {
        return ingredientDesc;
    }

    /**
     * Date getter
     * Retrieve best before date of the ingredient, allow for accessibility to other classes
     * @return      returns the best before date of the ingredient
     */
    public String getIngredientDate() {
        return ingredientDate;
    }

    /**
     * Location getter
     * Retrieve location of the ingredient, allow for accessibility to other classes
     * @return      return the location of the ingredient
     */
    public String getIngredientLocation() {
        return ingredientLocation;
    }

    /**
     * Amount getter
     * Retrieve amount of the ingredient, allow for accessibility to other classes
     * @return      return the amount of the ingredient
     */
    public int getIngredientAmount() {
        return ingredientAmount;
    }

    /**
     * Unit getter
     * Retrieve the unit of the ingredient, allow for accessibility to other classes
     * @return      return the unit of the ingredient
     */
    public int getIngredientUnit() {
        return ingredientUnit;
    }

    /**
     * Category getter
     * Retrieve the category of the ingredient, allow for accessibility to other classes
     * @return      return the category of the ingredient
     */
    public String getIngredientCategory() {
        return ingredientCategory;
    }

    /**
     * Description setter
     * Set the ingredient's description to a new given description
     * @param ingredientDesc    new description for ingredient
     */
    public void setIngredientDesc(String ingredientDesc) {
        this.ingredientDesc = ingredientDesc;
    }

    /**
     * Date setter
     * Set the ingredient's best before date to a new given best before date
     * @param ingredientDate    new best before date for ingredient
     */
    public void setIngredientDate(String ingredientDate) {
        this.ingredientDate = ingredientDate;
    }

    /**
     * Location setter
     * Set the ingredient's location to a new given location
     * @param ingredientLocation    new location for ingredient
     */
    public void setIngredientLocation(String ingredientLocation) {
        this.ingredientLocation = ingredientLocation;
    }

    /**
     * Amount setter
     * Set the ingredient's amount to a new given amount
     * @param ingredientAmount      new amount for ingredient
     */
    public void setIngredientAmount(int ingredientAmount) {
        this.ingredientAmount = ingredientAmount;
    }

    /**
     * Unit setter
     * Set the ingredient's unit to a new given unit
     * @param ingredientUnit        new unit for ingredient
     */
    public void setIngredientUnit(int ingredientUnit) {
        this.ingredientUnit = ingredientUnit;
    }

    /**
     * Category setter
     * Set the ingredient's category to a new given category
     * @param ingredientCategory    new category for ingredient
     */
    public void setIngredientCategory(String ingredientCategory) {
        this.ingredientCategory = ingredientCategory;
    }

    /**
     * Description compareTo
     * Compare ingredient descriptions
     * @param ingredient    specified ingredient
     * @return              returns 0 if both equal, >0 if first value is greater, <0 if second value is greater
     */
    public int compareToIngredientDesc(Ingredient ingredient) {
        return (this.getIngredientDesc().compareTo(ingredient.getIngredientDesc()));
    }

    /**
     * Location compareTo
     * Compare ingredient locations
     * @param ingredient    specified ingredient
     * @return              returns 0 if both equal, >0 if first value is greater, <0 if second value is greater
     */
    public int compareToIngredientLocation(Ingredient ingredient) {
        return (this.getIngredientLocation().compareTo(ingredient.getIngredientLocation()));
    }

    /**
     * Date compareTo
     * Compare ingredient Best Before Dates
     * @param ingredient    specified ingredient
     * @return              returns 0 if both equal, >0 if first value is greater, <0 if second value is greater
     */
    public int compareToIngredientDate(Ingredient ingredient){
        return(this.getIngredientDate().compareTo(ingredient.getIngredientDate()));
    }

    /**
     * Category compareTo
     * Compare ingredient categories
     * @param ingredient    specified ingredient
     * @return              returns 0 if both equal, >0 if first value is greater, <0 if second value is greater
     */
    public int compareToIngredientCategory(Ingredient ingredient){
        return(this.getIngredientCategory().compareTo(ingredient.getIngredientCategory()));
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
     * Parcel writer
     * Creates parcel with specified object written in
     * @param parcel    parcel in which object should be written
     * @param i         addition flags of how object should be written
     */
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