package com.example.bobstomatoes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;


import java.util.ArrayList;

/**
 * Class for the Shopping List Fragment which allows the user to edit ingredients
 * extends DialogFragment
 */

public class ShoppingListFragment extends DialogFragment {

//    private EditText descriptionText;
//    private EditText dateText;
    private EditText locationText;
    private EditText amountText;
    private EditText unitText;
    //    private EditText categoryText;
    Boolean isEdit = false;

    private OnShoppingListFragmentListener listener;

    Ingredient selectedIngredient;
    Ingredient editIngredient;
    //    Ingredient addIngredient;
    int oldIngredientPos;

    public interface OnShoppingListFragmentListener {
        public void onEditOkPressed(Ingredient newIngredient, Ingredient oldIngredient);
//        public void onDeleteOkPressed(Ingredient ingredient);
//        public void onAddOkPressed(Ingredient ingredient);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnShoppingListFragmentListener) {
            listener = (OnShoppingListFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement the interface method(s)");
        }
    }

    /**
     * Provides a dialog for the fragment to manage and display, allows the user to edit an ingredient (can change amount, thus adding ingredients)
     *
     * @param savedInstanceState interface container containing saveInstanceState
     * @return returns dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String title;
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_ingredient_storage, null);
//        descriptionText = view.findViewById(R.id.editTextIngredientDesc);
//        dateText = view.findViewById(R.id.editTextIngredientDate);
        locationText = view.findViewById(R.id.editTextShoppingListIngredientLocation);
        amountText = view.findViewById(R.id.editTextShoppingListIngredientAmount);
        unitText = view.findViewById(R.id.editTextShoppingListIngredientUnit);
//        categoryText = view.findViewById(R.id.editTextIngredientCategory);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            selectedIngredient = bundle.getParcelable("selectedIngredient");
            oldIngredientPos = bundle.getInt("oldIngredientPos");
            isEdit = bundle.getBoolean("isEdit");
//            descriptionText.setText(selectedIngredient.getIngredientDesc());
//            dateText.setText(selectedIngredient.getIngredientDate());
            locationText.setText(selectedIngredient.getIngredientLocation());
            amountText.setText(String.valueOf(selectedIngredient.getIngredientAmount()));
            unitText.setText(String.valueOf(selectedIngredient.getIngredientUnit()));
//            categoryText.setText(selectedIngredient.getIngredientCategory());
        }
        // If isEdit is true, then the ingredient was clicked on the ListView so populate the fragment text boxes with its details and make the two buttons Delete and Edit
        return builder
                .setView(view)
                .setTitle("Edit Ingredient")
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                            String newDescription = descriptionText.getText().toString();
//                            String newDate = dateText.getText().toString();
                        String newLocation = locationText.getText().toString();
                        String tempAmount = amountText.getText().toString();
                        int newAmount = Integer.parseInt(tempAmount);
                        String tempUnit = unitText.getText().toString();
                        int newUnit = Integer.parseInt(tempUnit);
//                            String newCategory = categoryText.getText().toString();
                        editIngredient = new Ingredient(selectedIngredient.getIngredientDesc(), selectedIngredient.getIngredientDate(),
                                newLocation, newAmount, newUnit, selectedIngredient.getIngredientCategory());
                        listener.onEditOkPressed(editIngredient, selectedIngredient);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
    }
}
