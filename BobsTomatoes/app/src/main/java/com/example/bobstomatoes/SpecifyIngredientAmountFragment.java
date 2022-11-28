package com.example.bobstomatoes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

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
        AlertDialog dialog;
        Bundle bundle = this.getArguments();


        Ingredient ingredient = bundle.getParcelable("selectedIngredient");
        ArrayList<Ingredient> ingredientList = bundle.getParcelableArrayList("ingredientList");
        int ingredientPos = bundle.getInt("selectedIngredientPos");

        //Builder for add
        dialog = builder.setView(view)
                .setTitle("Input Specified Ingredient Amount")
                .setPositiveButton("Add", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {

                            if (ingredientAmount.getText().toString().equals("")) {
                                throw new Exception("Please fill in the ingredient amount");
                            }

                            if(Integer.parseInt(ingredientAmount.getText().toString()) == 0) {
                                throw new Exception("0 ingredient amount not allowed");
                            }


                            ingredient.setIngredientAmount(Integer.parseInt(ingredientAmount.getText().toString()));
                            ingredientList.set(ingredientPos, ingredient);
                            listener.onAddIngredientOkPressed(ingredientList);
                            dialog.dismiss();
                        } catch (Exception e) {
                            Log.d("Exception", e.toString());

                            Snackbar snackbar = null;
                            snackbar = snackbar.make(view, e.getMessage(), Snackbar.LENGTH_SHORT);
                            snackbar.setDuration(600);
                            snackbar.show();
                        }

                    }
                });
            }
        });

        return dialog;
    }



}
