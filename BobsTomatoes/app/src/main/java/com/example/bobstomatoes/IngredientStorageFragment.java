package com.example.bobstomatoes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class IngredientStorageFragment extends DialogFragment{
    private EditText descriptionText;
    private EditText dateText;
    private EditText locationText;
    private EditText amountText;
    private EditText unitText;
    private EditText categoryText;
    private Button editButton;
    private Button deleteButton;
    Ingredient ingredient;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_ingredient_storage, null);
        descriptionText = view.findViewById(R.id.editTextIngredientDesc);
        dateText = view.findViewById(R.id.editTextIngredientDate);
        locationText = view.findViewById(R.id.editTextIngredientLocation);
        amountText = view.findViewById(R.id.editTextIngredientAmount);
        unitText = view.findViewById(R.id.editTextIngredientUnit);
        categoryText = view.findViewById(R.id.editTextIngredientCategory);
        editButton = view.findViewById(R.id.editButton);
        deleteButton = view.findViewById(R.id.deleteButton);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            ingredient = bundle.getParcelable("tempIngredient");
            descriptionText.setText(ingredient.getIngredientDesc());
            dateText.setText(ingredient.getIngredientDate());
            locationText.setText(ingredient.getIngredientLocation());
            amountText.setText(String.valueOf(ingredient.getIngredientAmount()));
            unitText.setText(String.valueOf(ingredient.getIngredientUnit()));
            categoryText.setText(ingredient.getIngredientCategory());
        }


        return null;
    }
}
