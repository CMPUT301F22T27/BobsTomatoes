package com.example.bobstomatoes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * DialogFragment for adding, editing, and deleting a recipe
 * extends DialogFragment
 */
public class RecipeFragment extends DialogFragment {


    private EditText titleText;
    private EditText timeText;
    private EditText servingsText;
    private EditText categoryText;
    private EditText commentsText;
    private ListView ingredientsList;

    private RecipeFragment.OnRecipeFragmentListener listener;

    //Database
    IngredientDB ingredientDB;
    ArrayList<Ingredient> testIngredients;
    CollectionReference ingredientReference;
    ArrayAdapter<Ingredient> ingredientAdapter;

    //For ingredient selection
    ArrayList<Ingredient> selectedIngredients;

    Recipe selectedRecipe;
    Recipe editRecipe;
    int oldRecipePos;

    public interface OnRecipeFragmentListener{

        public void onAddOkPressed(Recipe recipe);
        public void onEditOkPressed(Recipe recipe);
        public void onDeleteOkPressed(Recipe recipe);

    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        if (context instanceof RecipeFragment.OnRecipeFragmentListener){
            listener = (RecipeFragment.OnRecipeFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement the interface method(s)");
        }
    }

    /**
     * Provides a dialog for the fragment to manage and display, allows the user to add, edit, and delete a recipe
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_recipe, null);

        titleText = view.findViewById(R.id.editTextRecipeName);
        timeText = view.findViewById(R.id.editTextRecipeCookTime);
        servingsText = view.findViewById(R.id.editTextRecipeServingSize);
        categoryText = view.findViewById(R.id.editTextRecipeCategory);
        commentsText = view.findViewById(R.id.editTextRecipeComment);
        ingredientsList = view.findViewById(R.id.ingredients_list);

        selectedIngredients = new ArrayList<>();

        //Ingredients List
        initIngredientList();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Bundle bundle = this.getArguments();

        //If bundle != null, then a recipe has been passed in to the fragment -> edit/delete
        if (bundle != null) {


            selectedRecipe = bundle.getParcelable("selectedRecipe");
            oldRecipePos = bundle.getInt("oldRecipePos");

            Log.d("TESTESTESTEST", selectedRecipe.getRecipeTitle() + "");

            titleText.setText(selectedRecipe.getRecipeTitle());
            timeText.setText(String.valueOf(selectedRecipe.getRecipeTime()));
            servingsText.setText(String.valueOf(selectedRecipe.getRecipeServings()));
            categoryText.setText(selectedRecipe.getRecipeCategory());
            commentsText.setText(selectedRecipe.getRecipeComments());

            //Populate selectedIngredients
            selectedIngredients = selectedRecipe.getRecipeIngredients();


            //Builder for Edit/delete
            return builder.setView(view)
                    .setTitle("Edit Recipe")
                    .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            listener.onDeleteOkPressed(selectedRecipe);

                        }
                    })
                    .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            String newTitle = titleText.getText().toString();
                            int newTime = Integer.parseInt(timeText.getText().toString());
                            int newServings = Integer.parseInt(servingsText.getText().toString());
                            String newCategory = categoryText.getText().toString();
                            String newComments = commentsText.getText().toString();

                            Recipe newRecipe = new Recipe(newTitle, newTime, newServings,
                                    newCategory, newComments, selectedIngredients);

                            listener.onEditOkPressed(newRecipe);

                        }
                    })
                    .create();


        } else {  //If bundle = null, then a recipe has not been passed in to the fragment -> add

            //Builder for add
            return builder.setView(view)
                    .setTitle("Add Recipe")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            String newTitle = titleText.getText().toString();
                            int newTime = Integer.parseInt(timeText.getText().toString());
                            int newServings = Integer.parseInt(servingsText.getText().toString());
                            String newCategory = categoryText.getText().toString();
                            String newComments = commentsText.getText().toString();

                            Recipe newRecipe = new Recipe(newTitle, newTime, newServings,
                                    newCategory, newComments, selectedIngredients);

                            listener.onAddOkPressed(newRecipe);

                        }
                    })
                    .create();

        }
    }

    public void initIngredientList(){

        ingredientDB = new IngredientDB();

        testIngredients = ingredientDB.getIngredientList();

        ingredientReference = ingredientDB.getIngredientReference();


        //For no database testing
//        ArrayList<Ingredient> ingredients = new ArrayList<>();
//
//        ingredients.add(new Ingredient("Red Tomatoes", "2022",
//                "Fridge", 1, 1, "Canned"));
//        ingredients.add(new Ingredient("Tomato Sauce", "2022",
//                "Fridge", 1, 1, "Canned"));
//
//        ingredientAdapter = new IngredientStorageAdapter(getContext(), ingredients);


        ingredientAdapter = new IngredientStorageAdapter(getContext(), testIngredients);

        ingredientsList.setAdapter(ingredientAdapter);

        readData(new FireStoreCallback() {
            @Override
            public void onCallBack(ArrayList<Ingredient> test) {
                ingredientAdapter.notifyDataSetChanged();
            }
        });


        //Handle selection and unselection of items
        ingredientsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                //For no database testing
                //Ingredient selectedIngredient = ingredients.get(pos);

                Ingredient selectedIngredient = testIngredients.get(pos);

                boolean found = false;

                for (int i = 0; i < selectedIngredients.size(); i ++){
                    //Check if ingredient already selected
                    if (selectedIngredient.getIngredientDesc().equals(selectedIngredients.get(i).getIngredientDesc())){

                        //Unselect ingredient
                        selectedIngredients.remove(i);

                        found = true;

                        //Messing around with highlighting
                        ingredientsList.setSelector(R.color.white);

                        Log.d("TESTESTESTESTES", "REMOVED FROM LIST");

                    }
                }

                if (!found){

                    selectedIngredients.add(selectedIngredient);

                    //Messing around with highlighting
                    ingredientsList.setSelector(R.color.teal_200);

                    Log.d("TESTESTESTESTES", "ADDED TO LIST");

                }

            }
        });

    }

    public void readData(FireStoreCallback callBack) {
        ingredientReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Ingredient ingredient = document.toObject(Ingredient.class);
                        testIngredients.add(ingredient);
                    }
                    callBack.onCallBack(testIngredients);
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private interface FireStoreCallback {
        void onCallBack(ArrayList<Ingredient> test);
    }

}