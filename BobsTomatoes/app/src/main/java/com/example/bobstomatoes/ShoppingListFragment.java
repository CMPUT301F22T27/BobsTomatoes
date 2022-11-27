package com.example.bobstomatoes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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


import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * Class for the Shopping List Fragment which allows the user to edit ingredients
 * extends DialogFragment
 */

public class ShoppingListFragment extends DialogFragment {

    private EditText locationText;
    private EditText amountText;
    private EditText unitText;
    Boolean isEdit = false;

    private OnShoppingListFragmentListener listener;

    Ingredient selectedIngredient;
    Ingredient editIngredient;
    int oldIngredientPos;

    AlertDialog.Builder builder;

    public interface OnShoppingListFragmentListener {
        public void onEditOkPressed(Ingredient newIngredient, int oldIngredientPos, int newAmount);

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
        String title = "";
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_shopping_list, null);

        locationText = view.findViewById(R.id.editTextShoppingListIngredientLocation);
        amountText = view.findViewById(R.id.editTextShoppingListIngredientAmount);
        unitText = view.findViewById(R.id.editTextShoppingListIngredientUnit);

        builder = new AlertDialog.Builder(getContext());
        AlertDialog dialog;
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            selectedIngredient = bundle.getParcelable("selectedIngredient");
            oldIngredientPos = bundle.getInt("oldIngredientPos");
            title = selectedIngredient.getIngredientDesc();

            locationText.setText(selectedIngredient.getIngredientLocation());
            unitText.setText(String.valueOf(selectedIngredient.getIngredientUnit()));

        }
        // If isEdit is true, then the ingredient was clicked on the ListView so populate the fragment text boxes with its details and make the two buttons Delete and Edit
        dialog = builder
                .setView(view)
                .setTitle("Add Details For " + title)
                .setPositiveButton("Add", null)
                .setNeutralButton("Cancel", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newLocation = locationText.getText().toString();
                        String currentAmount = amountText.getText().toString();
                        String tempUnit = unitText.getText().toString();

                        try {

                            String field1 = "";
                            String field2 = "";
                            String field3 = "";

                            if (locationText.getText().toString().equals("")) {
                                field1 = "location ";
                            }

                            if (amountText.getText().toString().equals("")){
                                field2 = "amount ";
                            }

                            if (unitText.getText().toString().equals("")){
                                field3 = "unit ";
                            }

                            if (!field1.equals("") || !field2.equals("") || !field3.equals("")) {
                                throw new Exception("Please fill out the specified fields: \n" + field1 + field2 + field3);
                            }
                            
//                            if (locationText.getText().toString().equals("")){
//                                throw new Exception("Please fill out the required fields");
//                            }
//
//                            if (amountText.getText().toString().equals("")){
//                                throw new Exception("Please fill out the required fields");
//                            }
//
//                            if (unitText.getText().toString().equals("")){
//                                throw new Exception("Please fill out the required fields");
//                            }

                            int newAmount = Integer.parseInt(currentAmount);
                            int newUnit = Integer.parseInt(tempUnit);

//                            String newCategory = categoryText.getText().toString();
                            editIngredient = new Ingredient(selectedIngredient.getIngredientDesc(), selectedIngredient.getIngredientDate(),
                                    newLocation, selectedIngredient.getIngredientAmount(), newUnit, selectedIngredient.getIngredientCategory());
                            listener.onEditOkPressed(editIngredient, oldIngredientPos, newAmount);

                            dialog.dismiss();
                        } catch (Exception e) {
                            Log.d("EXCEPTION HERE", e.toString());

                            Snackbar snackbar = null;
                            snackbar = snackbar.make(view, e.getMessage(), Snackbar.LENGTH_SHORT);
                            snackbar.setDuration(700);
                            snackbar.show();
                        }
                    }
                });


            }
        });


        return dialog;
    }
}
