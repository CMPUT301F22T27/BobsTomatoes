package com.example.bobstomatoes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.common.data.SingleRefDataBufferIterator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.annotation.Nullable;

/**
 * Class for the Meal Plan Scale Fragment which allows the user to adjust the number of servings for the meal plan
 * extends DialogFragment
 */

public class MealPlanScaleFragment extends DialogFragment {

    private MealPlanScaleFragment.OnMealPlanScaleFragmentListener listener;
    private EditText scaleText;
    MealPlan oldMealPlan;

    public interface OnMealPlanScaleFragmentListener {
        public void onScaleOkPressed(MealPlan oldMealPlan, MealPlan scaledMealPlan);
    }


    /**
     * Attaches context fragment to RecipeFragment
     * @param context   fragment object that will be attached
     */
    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        if (context instanceof MealPlanScaleFragment.OnMealPlanScaleFragmentListener){
            listener = (MealPlanScaleFragment.OnMealPlanScaleFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement the interface method(s)");
        }
    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_meal_plan_scale, null);
        scaleText = view.findViewById(R.id.editTextScale);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Bundle bundle = this.getArguments();

        oldMealPlan = bundle.getParcelable("selectedMealPlan");
        ArrayList<Recipe> recipesList = oldMealPlan.getMealPlanRecipes();
        ArrayList<Ingredient> originalIngredientsList = oldMealPlan.getMealPlanIngredients();
        String originalDate = oldMealPlan.getMealPlanDate();

        //Builder for add
        return builder.setView(view)
                .setTitle("How Many Servings?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        double recipeScale = Double.parseDouble(scaleText.getText().toString());

                        for (int index = 0; index < recipesList.size(); index++) {

                            recipesList.get(index).setRecipeServings((int) Math.round(recipesList.get(index).getRecipeServings() * recipeScale));
                            ArrayList<Ingredient> ingredientsList = recipesList.get(index).getRecipeIngredients();

                            for (int j = 0; j < ingredientsList.size(); j++) {
                                ingredientsList.get(j).setIngredientAmount((int) Math.round(ingredientsList.get(j).getIngredientAmount() * recipeScale));
                            }

                            recipesList.get(index).setRecipeIngredients(ingredientsList);
                        }

                        MealPlan scaledMealPlan = new MealPlan(originalDate, recipesList, originalIngredientsList);

                        listener.onScaleOkPressed(oldMealPlan, scaledMealPlan);
                    }
                })
                .create();

    }
}
