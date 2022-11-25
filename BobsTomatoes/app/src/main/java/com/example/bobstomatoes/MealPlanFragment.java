package com.example.bobstomatoes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MealPlanFragment extends DialogFragment {

    private ListView recipesList;
    private ListView ingredientsList;

    private ActivityResultLauncher<Intent> activityResultLauncher;

    private MealPlanFragment.OnMealPlanFragmentListener listener;
    //private ActivityResultLauncher<Intent> activityResultLauncher;

    // Database
    IngredientDB ingredientDB;
    ArrayList<Ingredient> ingredientList;
    CollectionReference ingredientReference;
    ArrayAdapter<Ingredient> ingredientAdapter;
    RecipeDB recipeDB;
    ArrayList<Recipe> recipeList;
    ArrayList<String> testList;
    CollectionReference recipeReference;
    ArrayAdapter<Recipe> recipeAdapter;

    // For ingredient selection
    ArrayList<Ingredient> selectedIngredients;
    ArrayList<Recipe> selectedRecipes;

    Recipe selectedRecipe;
    MealPlan selectedMealPlan;
    String selectedDate;
    int oldRecipePos;
    int oldIngredientPos;
    String date = "0-0-0";

    public interface OnMealPlanFragmentListener{

        public void onAddOkPressed(MealPlan mealPlan);
        public void onEditOkPressed(MealPlan oldMealPlan, MealPlan updatedMealPlan);
        public void onDeleteOkPressed(MealPlan mealPlan);

    }

    /**
     * Attaches context fragment to RecipeFragment
     * @param context   fragment object that will be attached
     */
    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        if (context instanceof MealPlanFragment.OnMealPlanFragmentListener){
            listener = (MealPlanFragment.OnMealPlanFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement the interface method(s)");
        }
    }

    /**
     * Provides a dialog for the fragment to manage and display, allows the user to add, edit, and delete a recipe
     * @param savedInstanceState    interface container containing saveInstanceState
     * @return                      returns dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_meal_plan, null);

        recipesList = view.findViewById(R.id.recipe_listview_id);
        ingredientsList = view.findViewById(R.id.ingredient_listview_id);

        selectedIngredients = new ArrayList<>();
        selectedRecipes = new ArrayList<>();

        // Recipes List
        initRecipeList();
        initIngredientList();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Bundle bundle = this.getArguments();

        // If bundle != null, then a recipe has been passed in to the fragment -> edit/delete
        if (bundle != null) {

            selectedMealPlan = bundle.getParcelable("selectedMealPlan");
            selectedDate = bundle.getString("selectedDate");

            // Populate selectedIngredients and selectedRecipes
            selectedRecipes = selectedMealPlan.getMealPlanRecipes();
            selectedIngredients = selectedMealPlan.getMealPlanIngredients();

            // Builder for Edit/delete
            return builder.setView(view)
                    .setTitle("Edit Meal Plan")
                    .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            listener.onDeleteOkPressed(selectedMealPlan);

                        }
                    })
                    .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            MealPlan newMealPlan = new MealPlan(selectedDate, selectedRecipes, selectedIngredients);

                            listener.onEditOkPressed(selectedMealPlan, newMealPlan);

                        }
                    })
                    .create();


        } else {  // If bundle = null, then a recipe has not been passed in to the fragment -> add

            //Builder for add
            return builder.setView(view)
                    .setTitle("Add MealPlan")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            MealPlan newMealPlan = new MealPlan("", selectedRecipes, selectedIngredients);

                            listener.onAddOkPressed(newMealPlan);

                        }
                    })
                    .create();

        }
    }


    /**
     * Initialize and update ingredient list and database
     */
    public void initRecipeList(){

        recipeDB = new RecipeDB();

        recipeList = recipeDB.getRecipeList();

        recipeReference = recipeDB.getRecipeReference();

        recipeAdapter = new RecipeAdapter(getContext(), recipeList);

        recipesList.setAdapter(recipeAdapter);

        recipesList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // Populate recipe list from database, by calling this, we can safely assume the list has been populated from the DataBase
        readData(new RecipeFireStoreCallback() {
            /**
             * Notify data change for recipeList
             * @param recipeList    array list of recipes
             */
            @Override
            public void onCallBack(ArrayList<Recipe> recipeList) {
                recipeAdapter.notifyDataSetChanged();
                Log.d("ARRAYLIST", recipeList + "");
            }
        });


        // Handle selection and unselection of items
        recipesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                //Highlighting
                for(int i = 0; i < recipeList.size(); i++) {

                    View check = recipesList.getChildAt(i);

                    if (check != null) {

                        check.setActivated(false);

                        for (int j = 0; j < selectedRecipes.size(); j++) {

                            TextView tempView = check.findViewById(R.id.recipe_name_textview_id);

                            String name = tempView.getText().toString();

                            if (name.equals(selectedRecipes.get(j).getRecipeTitle())) {

                                check.setActivated(true);

                            }

                        }

                    }

                }


                Recipe selectedRecipe = recipeList.get(pos);

                boolean found = false;

                for (int i = 0; i < selectedRecipes.size(); i ++){
                    // Check if ingredient already selected
                    if (selectedRecipe.getRecipeTitle().equals(selectedRecipes.get(i).getRecipeTitle())){

                        //Unselect ingredient
                        selectedRecipes.remove(i);

                        found = true;

                        view.setActivated(false);

                    }
                }

                if (!found){

                    selectedRecipes.add(selectedRecipe);

                    view.setActivated(true);

                }
            }
        });

        //Need to re-highlight items when views are drawn from offscreen
        recipesList.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                for (int i = 0; i < recipeAdapter.getCount(); i++) {

                    View check = recipesList.getChildAt(i);

                    if(check != null) {

                        check.setActivated(false);

                        for(int j = 0; j < selectedRecipes.size(); j++){

                            TextView tempView = check.findViewById(R.id.recipe_name_textview_id);

                            String name = tempView.getText().toString();

                            if (name.equals(selectedRecipes.get(j).getRecipeTitle())){

                                check.setActivated(true);

                            }

                        }

                    }

                }

            }
        });

    }

    /**
     * Populates data base using callBack
     * @param callBack  ingredient database
     */
    public void readData(RecipeFireStoreCallback callBack) {
        recipeReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Recipe recipe = document.toObject(Recipe.class);
                        recipeList.add(recipe);
                    }
                    callBack.onCallBack(recipeList);
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    /**
     * Initialize and update ingredient list and database
     */
    public void initIngredientList(){

        ingredientDB = new IngredientDB();

        ingredientList = ingredientDB.getIngredientList();

        ingredientReference = ingredientDB.getIngredientReference();

        ingredientAdapter = new IngredientStorageAdapter(getContext(), ingredientList);

        ingredientsList.setAdapter(ingredientAdapter);

        ingredientsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // Populate recipe list from database, by calling this, we can safely assume the list has been populated from the DataBase
        readData(new IngredientFireStoreCallback() {
            /**
             * Notify data change for recipeList
             * @param ingredientList    array list of recipes
             */
            @Override
            public void onCallBack(ArrayList<Ingredient> ingredientList) {
                ingredientAdapter.notifyDataSetChanged();
                Log.d("ARRAYLIST", ingredientList + "");
            }
        });


        // Handle selection and unselection of items
        ingredientsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                //Highlighting
                for(int i = 0; i < ingredientList.size(); i++) {

                    View check = ingredientsList.getChildAt(i);

                    if (check != null) {

                        check.setActivated(false);

                        for (int j = 0; j < selectedIngredients.size(); j++) {

                            TextView tempView = check.findViewById(R.id.ingredient_name_textview_id);

                            String name = tempView.getText().toString();

                            if (name.equals(selectedIngredients.get(j).getIngredientDesc())) {

                                check.setActivated(true);

                            }

                        }

                    }

                }


                Ingredient selectedIngredient = ingredientList.get(pos);

                boolean found = false;

                for (int i = 0; i < selectedIngredients.size(); i ++){
                    // Check if ingredient already selected
                    if (selectedIngredient.getIngredientDesc().equals(selectedIngredients.get(i).getIngredientDesc())){

                        //Unselect ingredient
                        selectedIngredients.remove(i);

                        found = true;

                        view.setActivated(false);

                    }
                }

                if (!found){

                    //selectedIngredients.add(selectedIngredient);
                    view.setActivated(true);

                    //Open 2nd fragment here
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("ingredientList", selectedIngredients);
                    bundle.putParcelable("selectedIngredient", selectedIngredient);
                    SpecifyIngredientAmountFragment fragment = new SpecifyIngredientAmountFragment();
                    fragment.setArguments(bundle);
                    fragment.setCancelable(false);
                    fragment.show(getChildFragmentManager(), "INGREDIENT");
                }
            }
        });

        //Need to re-highlight items when views are drawn from offscreen
        ingredientsList.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                for (int i = 0; i < ingredientAdapter.getCount(); i++) {

                    View check = ingredientsList.getChildAt(i);

                    if(check != null) {

                        check.setActivated(false);

                        for(int j = 0; j < selectedIngredients.size(); j++){

                            TextView tempView = check.findViewById(R.id.ingredient_name_textview_id);

                            String name = tempView.getText().toString();

                            if (name.equals(selectedIngredients.get(j).getIngredientDesc())){

                                check.setActivated(true);

                            }

                        }

                    }

                }

            }
        });


    }

    /**
     * Populates data base using callBack
     * @param callBack  ingredient database
     */
    public void readData(IngredientFireStoreCallback callBack) {
        ingredientReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Ingredient ingredient = document.toObject(Ingredient.class);
                        ingredientList.add(ingredient);
                    }
                    callBack.onCallBack(ingredientList);
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private interface IngredientFireStoreCallback {
        void onCallBack(ArrayList<Ingredient> IngredientList);
    }

    private interface RecipeFireStoreCallback{
        void onCallBack(ArrayList<Recipe> recipeList);
    }

}
