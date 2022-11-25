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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
 * Class for the Ingredient Storage Fragment which allows the user to add, edit, and delete ingredients
 * extends DialogFragment
 */

public class IngredientStorageFragment extends DialogFragment {

    private EditText descriptionText;
    private EditText amountText;
    private EditText unitText;
    Boolean isEdit = false;
    DatePicker datePicker;

    RadioGroup locationRadioGroup;
    RadioButton pantryRadioButton;
    RadioButton fridgeRadioButton;
    RadioButton freezerRadioButton;
    RadioGroup categoryRadioGroup1;
    RadioGroup categoryRadioGroup2;
    RadioButton dairyRadioButton;
    RadioButton fruitRadioButton;
    RadioButton grainRadioButton;
    RadioButton proteinRadioButton;
    RadioButton vegetableRadioButton;
    RadioButton otherRadioButton;

    private OnIngredientFragmentListener listener;

    Ingredient selectedIngredient;
    Ingredient editIngredient;
    Ingredient addIngredient;
    int oldIngredientPos;
    Context context;
    AlertDialog.Builder builder;

    public interface OnIngredientFragmentListener{
        public void onEditOkPressed(Ingredient newIngredient, Ingredient oldIngredient);
        public void onDeleteOkPressed(Ingredient ingredient);
        public void onAddOkPressed(Ingredient ingredient);

    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
        if (context instanceof OnIngredientFragmentListener){
            listener = (OnIngredientFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement the interface method(s)");
        }
    }

    /**
     * Provides a dialog for the fragment to manage and display, allows the user to add, edit, and delete an ingredient
     * @param savedInstanceState    interface container containing saveInstanceState
     * @return                      returns dialog 
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String title;
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_ingredient_storage, null);
        descriptionText = view.findViewById(R.id.editTextIngredientDesc);
        datePicker = view.findViewById(R.id.datePicker);
        locationRadioGroup = view.findViewById(R.id.radioGroupLocation);
        pantryRadioButton = view.findViewById(R.id.radioButtonPantry);
        fridgeRadioButton = view.findViewById(R.id.radioButtonFridge);
        freezerRadioButton = view.findViewById(R.id.radioButtonFreezer);

        amountText = view.findViewById(R.id.editTextIngredientAmount);
        unitText = view.findViewById(R.id.editTextIngredientUnit);

        categoryRadioGroup1 = view.findViewById(R.id.radioGroupCategory1);
        categoryRadioGroup2 = view.findViewById(R.id.radioGroupCategory2);
        dairyRadioButton = view.findViewById(R.id.radioButtonDairy);
        fruitRadioButton = view.findViewById(R.id.radioButtonFruit);
        grainRadioButton = view.findViewById(R.id.radioButtonGrain);
        proteinRadioButton = view.findViewById(R.id.radioButtonProtein);
        vegetableRadioButton = view.findViewById(R.id.radioButtonVegetable);
        otherRadioButton = view.findViewById(R.id.radioButtonOther);

        builder = new AlertDialog.Builder(getContext());
        AlertDialog dialog;
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            selectedIngredient = bundle.getParcelable("selectedIngredient");
            oldIngredientPos = bundle.getInt("oldIngredientPos");
            isEdit = bundle.getBoolean("isEdit");
            descriptionText.setText(selectedIngredient.getIngredientDesc());

            // Date
            int year = Integer.parseInt(selectedIngredient.getIngredientDate().toString().substring(0, 4));
            int month;
            int day;
            if (selectedIngredient.getIngredientDate().toString().substring(6, 7).equals("-")) {
                month = Integer.parseInt(selectedIngredient.getIngredientDate().toString().substring(5, 6)) - 1;
                day = Integer.parseInt(selectedIngredient.getIngredientDate().toString().substring(7, 9));
            } else {
                month = Integer.parseInt(selectedIngredient.getIngredientDate().toString().substring(5, 7)) - 1;
                day = Integer.parseInt(selectedIngredient.getIngredientDate().toString().substring(8, 10));
            }
            datePicker.updateDate(year, month, day);

            // Location
            if (selectedIngredient.getIngredientLocation().toString().equals("Pantry")) {
                locationRadioGroup.check(locationRadioGroup.getChildAt(0).getId());
            } else if (selectedIngredient.getIngredientLocation().toString().equals("Fridge")) {
                locationRadioGroup.check(locationRadioGroup.getChildAt(1).getId());
            } else if (selectedIngredient.getIngredientLocation().toString().equals("Freezer")) {
                locationRadioGroup.check(locationRadioGroup.getChildAt(2).getId());
            } else {

            }

            amountText.setText(String.valueOf(selectedIngredient.getIngredientAmount()));
            unitText.setText(String.valueOf(selectedIngredient.getIngredientUnit()));

            // Category
            if (selectedIngredient.getIngredientCategory().toString().equals("Dairy")) {
                categoryRadioGroup1.check(R.id.radioButtonDairy);
            } else if (selectedIngredient.getIngredientCategory().toString().equals("Fruit")) {
                categoryRadioGroup1.check(R.id.radioButtonFruit);
            } else if (selectedIngredient.getIngredientCategory().toString().equals("Grain")) {
                categoryRadioGroup1.check(R.id.radioButtonGrain);
            } else if (selectedIngredient.getIngredientCategory().toString().equals("Protein")) {
                categoryRadioGroup2.check(R.id.radioButtonProtein);
            } else if (selectedIngredient.getIngredientCategory().toString().equals("Vegetable")) {
                categoryRadioGroup2.check(R.id.radioButtonVegetable);
            } else if (selectedIngredient.getIngredientCategory().toString().equals("Other")) {
                categoryRadioGroup2.check(R.id.radioButtonOther);
            } else {

            }
        }

        categoryRadioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {
                if (radioButtonID == dairyRadioButton.getId()){
                    if(dairyRadioButton.isChecked() == true) {

                        proteinRadioButton.setChecked(false);
                        vegetableRadioButton.setChecked(false);
                        otherRadioButton.setChecked(false);

                        categoryRadioGroup2.clearCheck();
                    }
                }
                if (radioButtonID == fruitRadioButton.getId()){
                    if(fruitRadioButton.isChecked() == true) {
                        
                        proteinRadioButton.setChecked(false);
                        vegetableRadioButton.setChecked(false);
                        otherRadioButton.setChecked(false);

                        categoryRadioGroup2.clearCheck();

                    }
                }
                if (radioButtonID == grainRadioButton.getId()){
                    if (grainRadioButton.isChecked() == true) {

                        proteinRadioButton.setChecked(false);
                        vegetableRadioButton.setChecked(false);
                        otherRadioButton.setChecked(false);

                        categoryRadioGroup2.clearCheck();
                    }
                }
            }
        });

        categoryRadioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {
                if (radioButtonID == otherRadioButton.getId()) {
                    if (otherRadioButton.isChecked() == true) {

                        dairyRadioButton.setChecked(false);
                        fruitRadioButton.setChecked(false);
                        grainRadioButton.setChecked(false);

                        categoryRadioGroup1.clearCheck();
                    }
                }
                if (radioButtonID == vegetableRadioButton.getId()) {
                    if (vegetableRadioButton.isChecked() == true) {

                        dairyRadioButton.setChecked(false);
                        fruitRadioButton.setChecked(false);
                        grainRadioButton.setChecked(false);

                        categoryRadioGroup1.clearCheck();
                    }
                }
                if (radioButtonID == proteinRadioButton.getId()) {
                    if (proteinRadioButton.isChecked() == true) {

                        dairyRadioButton.setChecked(false);
                        fruitRadioButton.setChecked(false);
                        grainRadioButton.setChecked(false);

                        categoryRadioGroup1.clearCheck();
                    }
                }
            }
        });

        // If isEdit is true, then the ingredient was clicked on the ListView so populate the fragment text boxes with its details and make the two buttons Delete and Edit
        if (isEdit == true) {
            return builder
                    .setView(view)
                    .setTitle("Edit Ingredient")
                    .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String newDescription = descriptionText.getText().toString();

                            // Date
                            int year = datePicker.getYear();
                            int month = datePicker.getMonth() + 1;
                            int day = datePicker.getDayOfMonth();
                            String newDate = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);

                            // Location
                            String newLocation;
                            if(pantryRadioButton.isChecked()){
                                newLocation = "Pantry";
                            }else if(fridgeRadioButton.isChecked()){
                                newLocation = "Fridge";
                            }else if(freezerRadioButton.isChecked()){
                                newLocation = "Freezer";
                            }else{
                                newLocation = "";
                            }

                            String tempAmount = amountText.getText().toString();
                            int newAmount;
                            if (tempAmount.toString().equals("")) {
                                newAmount = 0;
                            } else {
                                newAmount = Integer.parseInt(tempAmount);
                            }
                            String tempUnit = unitText.getText().toString();
                            int newUnit;
                            if (tempUnit.toString().equals("")) {
                                newUnit = 1;
                            } else {
                                newUnit = Integer.parseInt(tempUnit);
                            }

                            // Category
                            String newCategory;
                            if(dairyRadioButton.isChecked()){
                                newCategory = "Dairy";
                            }else if(fruitRadioButton.isChecked()){
                                newCategory = "Fruit";
                            }else if(grainRadioButton.isChecked()){
                                newCategory = "Grain";
                            }else if(proteinRadioButton.isChecked()){
                                newCategory = "Protein";
                            }else if(vegetableRadioButton.isChecked()) {
                                newCategory = "Vegetable";
                            }else if(otherRadioButton.isChecked()){
                                newCategory = "Other";
                            }else{
                                newCategory = "";
                            }

                            if (newDescription.equals("")) {
                                newDescription = selectedIngredient.getIngredientDesc();
                            }

                            editIngredient = new Ingredient(newDescription, newDate, newLocation, newAmount, newUnit, newCategory);
                            listener.onEditOkPressed(editIngredient, selectedIngredient);
                        }
                    })
                    .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            listener.onDeleteOkPressed(selectedIngredient);
                        }
                    })
                    .create();
        }
        // If its false, then the add button was pressed, so open a fragment with its text boxes empty and make the two buttons Cancel and Add
        else {
             dialog = builder
                    .setView(view)
                    .setTitle("Add Ingredient")
                    .setPositiveButton("Add", null)
                    .setNegativeButton("Cancel", null)
                    .create();

             dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                 @Override
                 public void onShow(DialogInterface dialogInterface) {
                     Button button = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                     button.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View view) {
                             try {
                                 String newDescription = descriptionText.getText().toString();

                                 // Date
                                 int year = datePicker.getYear();
                                 int month = datePicker.getMonth() + 1;
                                 int day = datePicker.getDayOfMonth();
                                 String newDate = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);

                                 // Location
                                 String newLocation;
                                 if(pantryRadioButton.isChecked()){
                                     newLocation = "Pantry";
                                 }else if(fridgeRadioButton.isChecked()){
                                     newLocation = "Fridge";
                                 }else if(freezerRadioButton.isChecked()){
                                     newLocation = "Freezer";
                                 }else{
                                     newLocation = "";
                                 }

                                 String tempAmount = amountText.getText().toString();
                                 int newAmount;
                                 if (tempAmount.toString().equals("")) {
                                     newAmount = 0;
                                 } else {
                                     newAmount = Integer.parseInt(tempAmount);
                                 }
                                 String tempUnit = unitText.getText().toString();
                                 int newUnit;
                                 if (tempUnit.toString().equals("")) {
                                     newUnit = 0;
                                 } else {
                                     newUnit = Integer.parseInt(tempUnit);
                                 }

                                 // Category
                                 String newCategory;
                                 if(dairyRadioButton.isChecked()){
                                     newCategory = "Dairy";
                                 }else if(fruitRadioButton.isChecked()){
                                     newCategory = "Fruit";
                                 }else if(grainRadioButton.isChecked()){
                                     newCategory = "Grain";
                                 }else if(proteinRadioButton.isChecked()){
                                     newCategory = "Protein";
                                 }else if(vegetableRadioButton.isChecked()) {
                                     newCategory = "Vegetable";
                                 }else if(otherRadioButton.isChecked()){
                                     newCategory = "Other";
                                 }else{
                                     newCategory = "";
                                 }
                                 addIngredient = new Ingredient(newDescription, newDate, newLocation, newAmount, newUnit, newCategory);

                                 listener.onAddOkPressed(addIngredient);

                                 dialog.dismiss();
                             } catch (Exception e) {
                                 Log.d("EXCEPTION HERE", e.toString());
                                 Toast.makeText(context.getApplicationContext(),"Fill out all fields", Toast.LENGTH_SHORT).show();
                             }
                         }
                     });
                 }
             });

             return dialog;
        }

    }




}
