package com.example.bobstomatoes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class SpecifyIngredientAmountFragment extends DialogFragment {

    private SpecifyIngredientAmountFragment.OnRecipeIngredientListener listener;


    /**
     * Interface for the onAddIngredientOkPressed
     */
    public interface OnRecipeIngredientListener{
        public void onAddIngredientOkPressed(ArrayList<Ingredient> ingredientsList);
    }

    /**
     * Attaches context fragment to RecipeIngredientFragment
     * @param context   fragment object that will be attached
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SpecifyIngredientAmountFragment.OnRecipeIngredientListener){
            listener = (SpecifyIngredientAmountFragment.OnRecipeIngredientListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement the interface method(s)");
        }
    }

    /**
     * Provides a dialog for specifying the ingredient amount
     * @param savedInstancedState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstancedState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.child_fragment_recipe, null);
        EditText ingredientAmount = view.findViewById(R.id.ingredientAmount);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Bundle bundle = this.getArguments();


        Ingredient ingredient = bundle.getParcelable("selectedIngredient");
        ArrayList<Ingredient> ingredientList = bundle.getParcelableArrayList("ingredientList");
        int ingredientPos = bundle.getInt("selectedIngredientPos");

        //Builder for add
        return builder.setView(view)
                .setTitle("Input Specified Ingredient Amount")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ingredient.setIngredientAmount(Integer.parseInt(ingredientAmount.getText().toString()));
                        ingredientList.set(ingredientPos, ingredient);
                        listener.onAddIngredientOkPressed(ingredientList);

                    }
                })
                .create();
    }



}
