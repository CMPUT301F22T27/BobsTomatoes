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

import java.util.ArrayList;

public class IngredientStorageFragment extends DialogFragment{
    private EditText descriptionText;
    private EditText dateText;
    private EditText locationText;
    private EditText amountText;
    private EditText unitText;
    private EditText categoryText;
    private Button editButton;
    private Button deleteButton;
    private OnFragmentInteractionListener listener;
    Ingredient tempIngredient;
    Ingredient editIngredient;
    ArrayList<Ingredient> dataList;
    int ingredientPos;

    public interface OnFragmentInteractionListener{
        public void onEditPressed(Ingredient ingredient);
        public void onDeletePressed(Ingredient ingredient);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement the interface method(s)");
        }
    }

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
            tempIngredient = bundle.getParcelable("tempIngredient");
            dataList = bundle.getParcelable("dataList");
            ingredientPos = bundle.getInt("ingredientPosition");
            descriptionText.setText(tempIngredient.getIngredientDesc());
            dateText.setText(tempIngredient.getIngredientDate());
            locationText.setText(tempIngredient.getIngredientLocation());
            amountText.setText(String.valueOf(tempIngredient.getIngredientAmount()));
            unitText.setText(String.valueOf(tempIngredient.getIngredientUnit()));
            categoryText.setText(tempIngredient.getIngredientCategory());
        }

        editButton.setOnClickListener(view1 -> {
            String newDescription = descriptionText.getText().toString();
            String newDate = dateText.getText().toString();
            String newLocation = locationText.getText().toString();
            String tempAmount = amountText.getText().toString();
            int newAmount = Integer.parseInt(tempAmount);
            String tempUnit = unitText.getText().toString();
            int newUnit = Integer.parseInt(tempUnit);
            String newCategory = categoryText.getText().toString();

            editIngredient = new Ingredient(newDescription, newDate, newLocation, newAmount, newUnit, newCategory);
            dataList.set(ingredientPos, editIngredient);

        });

        deleteButton.setOnClickListener(view1 -> {
            dataList.remove(ingredientPos);
        });


        return null;
    }
}
