package com.example.bobstomatoes;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class detailing the information of a Recipe
 * implements Parcelable
 */

public class Recipe implements Parcelable {
    private String recipeTitle;
    private int recipeTime;
    private int recipeServings;
    private String recipeCategory;
    private String recipeComments;
    private ArrayList<Ingredient> recipeIngredients;

    /**
     * Recipe constructor, takes in the title, time, servings, category, comments, and the ingredients of the recipe
     * @param recipeTitle       title of recipe
     * @param recipeTime        time is takes to make recipe
     * @param recipeServings    number of servings from recipe
     * @param recipeCategory    category of recipe
     * @param recipeComments    comments about recipe
     * @param recipeIngredients ingredients needed to make recipe
     */
    public Recipe(String recipeTitle, int recipeTime, int recipeServings, String recipeCategory, String recipeComments, ArrayList<Ingredient> recipeIngredients) {
        this.recipeTitle = recipeTitle;
        this.recipeTime = recipeTime;
        this.recipeServings = recipeServings;
        this.recipeCategory = recipeCategory;
        this.recipeComments = recipeComments;
        this.recipeIngredients = recipeIngredients;
    }

    /**
     * Recipe constructor, is an empty constructor, takes in nothing
     */
    //Need this and setters to pull from database
    public Recipe(){

    }

    /**
     * Title getter
     * Retrieve title of the recipe, allow for accessibility to other classes
     * @return      returns the title of the recipe
     */
    public String getRecipeTitle() {
        return recipeTitle;
    }

    /**
     * Time getter
     * Retrieve time needed to make recipe, allow for accessibility to other classes
     * @return      returns the time of the recipe
     */
    public int getRecipeTime() {
        return recipeTime;
    }

    /**
     * Servings getter
     * Retrieve serving size of the recipe, allow for accessibility to other classes
     * @return      returns the serving size of the recipe
     */
    public int getRecipeServings() { return recipeServings; }

    /**
     * Category getter
     * Retrieve category of the recipe, allow for accessibility to other classes
     * @return      returns the category of the recipe
     */
    public String getRecipeCategory() {
        return recipeCategory;
    }

    /**
     * Comments getter
     * Retrieve comments of the recipe, allow for accessibility to other classes
     * @return      returns the comments of the recipe
     */
    public String getRecipeComments() {
        return recipeComments;
    }

    /**
     * Ingredients getter
     * Retrieve ingredients of the recipe, allow for accessibility to other classes
     * @return      returns the ingredients of the recipe
     */
    public ArrayList<Ingredient> getRecipeIngredients() {
        return recipeIngredients;
    }

    /**
     * Title setter
     * Set the recipe's title to a new given title
     * @param recipeTitle    new title for recipe
     */
    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    /**
     * Time setter
     * Set the recipe's time to a new given time
     * @param recipeTime    new time for recipe
     */
    public void setRecipeTime(int recipeTime) {
        this.recipeTime = recipeTime;
    }

    /**
     * Servings setter
     * Set the recipe's serving size to a new given serving size
     * @param recipeServings    new serving size for recipe
     */
    public void setRecipeServings(int recipeServings) {
        this.recipeServings = recipeServings;
    }

    /**
     * Category setter
     * Set the recipe's category to a new given category
     * @param recipeCategory    new category for recipe
     */
    public void setRecipeCategory(String recipeCategory) {
        this.recipeCategory = recipeCategory;
    }

    /**
     * Comments setter
     * Set the recipe's comments to new given comments
     * @param recipeComments    new comments for recipe
     */
    public void setRecipeComments(String recipeComments) {
        this.recipeComments = recipeComments;
    }

    /**
     * Ingredient setter
     * Set the recipe's ingredients list to new given ingredients list
     * @param recipeIngredients    new list of ingredients for recipe
     */
    public void setRecipeIngredients(ArrayList<Ingredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    /**
     * Title compareTo
     * Compare recipe titles for sorting
     * @param recipe    specified recipe
     * @return          returns 0 if both equal, >0 if first value is greater, <0 if second value is greater
     */
    public int compareToRecipeTitle(Recipe recipe) {
        return (this.getRecipeTitle().compareTo(recipe.getRecipeTitle()));
    }

    /**
     * Time compareTo
     * Compare recipe make time for sorting
     * @param recipe    specified recipe
     * @return          returns 0 if both equal, >0 if first value is greater, <0 if second value is greater
     */
    public int compareToRecipeTime(Recipe recipe) {
        return (String.valueOf(this.getRecipeTime()).compareTo(String.valueOf(recipe.getRecipeTime())));
    }

    /**
     * Servings compareTo
     * Compare recipe serving size for sorting
     * @param recipe    specified recipe
     * @return          returns 0 if both equal, >0 if first value is greater, <0 if second value is greater
     */
    public int compareToRecipeServings(Recipe recipe){
        return(String.valueOf(this.getRecipeServings()).compareTo(String.valueOf(recipe.getRecipeServings())));
    }

    /**
     * Category compareTo
     * Compare recipe category for sorting
     * @param recipe    specified recipe
     * @return          returns 0 if both equal, >0 if first value is greater, <0 if second value is greater
     */
    public int compareToRecipeCategory(Recipe recipe){
        return(this.getRecipeCategory().compareTo(recipe.getRecipeCategory()));
    }

    /**
     * Recipe constructor, takes in a parcel
     * @param in    parcel containing a recipe information (title, time, servings, category, comments, ingredients)
     */
    protected Recipe(Parcel in) {
        recipeTitle = in.readString();
        recipeTime = in.readInt();
        recipeServings = in.readInt();
        recipeCategory = in.readString();
        recipeComments = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        /**
         * Create new instance of the Parcelable Class
         * @param in    parcel containing a recipe's information (title, time, servings, category, comments, ingredients)
         * @return      returns a new created Recipe
         */
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        /**
         * Create a new array of the Parcelable Class
         * @param size  size of new array
         * @return      returns a new Recipe Array
         */
        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
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
        parcel.writeString(recipeTitle);
        parcel.writeInt(recipeTime);
        parcel.writeInt(recipeServings);
        parcel.writeString(recipeCategory);
        parcel.writeString(recipeComments);
    }
}