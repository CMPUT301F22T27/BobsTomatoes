package com.example.bobstomatoes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
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
    private Button takePhotoButton;
    private Button choosePhotoButton;
    private ImageView recipeImageView;

    private Bitmap finalPhoto;
    private String encodedImage;

    private RecipeFragment.OnRecipeFragmentListener listener;

    private ActivityResultLauncher<Intent> activityResultLauncher;

    // Database
    IngredientDB ingredientDB;
    ArrayList<Ingredient> ingredientList;
    CollectionReference ingredientReference;
    ArrayAdapter<Ingredient> ingredientAdapter;

    // For ingredient selection
    ArrayList<Ingredient> selectedIngredients;

    Recipe selectedRecipe;
    Recipe editRecipe;
    int oldRecipePos;



    public interface OnRecipeFragmentListener{

        public void onAddOkPressed(Recipe recipe);
        public void onEditOkPressed(Recipe newRecipe, Recipe oldRecipe);
        public void onDeleteOkPressed(Recipe recipe);

    }

    /**
     * Attaches context fragment to RecipeFragment
     * @param context   fragment object that will be attached
     */
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
     * @param savedInstanceState    interface container containing saveInstanceState
     * @return                      returns dialog
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

        //Image View
        recipeImageView = view.findViewById(R.id.recipeImageView);

        //Set up button to launch camera app
        takePhotoButton = view.findViewById(R.id.takePhotoButton);

        //Button to choose gallery image
        choosePhotoButton = view.findViewById(R.id.choosePhotoButton);

        initializeCamera(view);

        // Ingredients List
        initIngredientList();


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Bundle bundle = this.getArguments();


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

        // If bundle != null, then a recipe has been passed in to the fragment -> edit/delete
        if (bundle != null) {


            selectedRecipe = bundle.getParcelable("selectedRecipe");
            oldRecipePos = bundle.getInt("oldRecipePos");

            titleText.setText(selectedRecipe.getRecipeTitle());
            timeText.setText(String.valueOf(selectedRecipe.getRecipeTime()));
            servingsText.setText(String.valueOf(selectedRecipe.getRecipeServings()));
            categoryText.setText(selectedRecipe.getRecipeCategory());
            commentsText.setText(selectedRecipe.getRecipeComments());

            // Populate selectedIngredients
            selectedIngredients = selectedRecipe.getRecipeIngredients();
            //updateHighlights();

            //Populate ImageView
            encodedImage = selectedRecipe.getRecipeImage();
            finalPhoto = selectedRecipe.getDecodedImage();

            recipeImageView.setImageBitmap(finalPhoto);


            // Builder for Edit/delete
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
                                    newCategory, newComments, selectedIngredients, encodedImage);

                            listener.onEditOkPressed(newRecipe, selectedRecipe);

                        }
                    })
                    .create();


        } else {  // If bundle = null, then a recipe has not been passed in to the fragment -> add

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
                                    newCategory, newComments, selectedIngredients, encodedImage);

                            listener.onAddOkPressed(newRecipe);

                        }
                    })
                    .create();

        }
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

        readData(new IngredientFireStoreCallback() {
            @Override
            public void onCallBack(ArrayList<Ingredient> IngredientList) {
                ingredientAdapter.notifyDataSetChanged();
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


                //Add or remove items to selectedingredients
                Ingredient selectedIngredient = ingredientList.get(pos);

                boolean ingredientFound = false;

                for (int i = 0; i < selectedIngredients.size(); i ++){

                    // Check if ingredient already selected
                    if (selectedIngredient.getIngredientDesc().equals(selectedIngredients.get(i).getIngredientDesc())){

                        //Unselect ingredient
                        selectedIngredients.remove(i);

                        ingredientFound = true;

                        view.setActivated(false);

                    }

                }


                if (!ingredientFound){

                    selectedIngredients.add(selectedIngredient);

                    view.setActivated(true);

                    int ingredientPos = -1;
                    for (int i = 0; i < selectedIngredients.size(); i++){
                        if (selectedIngredients.get(i).getIngredientDesc() == selectedIngredient.getIngredientDesc()) {
                            ingredientPos = i;
                        }
                    }

                    //Open 2nd fragment here
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("ingredientList", selectedIngredients);
                    bundle.putParcelable("selectedIngredient", selectedIngredient);
                    bundle.putInt("selectedIngredientPos", ingredientPos);
                    SpecifyIngredientAmountFragment fragment = new SpecifyIngredientAmountFragment();
                    fragment.setArguments(bundle);
                    fragment.setCancelable(false);
                    fragment.show(getChildFragmentManager(), "INGREDIENT");
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

    private void initializeCamera(View view){


        //Retrieve info from camera activity
        //Defines an activity result launcher which launches an intent as an activity
        //and returns the results of the activity into OnActivityResult, where it can be handled
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        Intent data = result.getData();

                        if (data != null){

                            Bundle bundle = data.getExtras();

                            //Indicates photo was taken
                            if (bundle != null){

                                finalPhoto = (Bitmap) bundle.get("data");


                            //Photo is from camera roll, comes back as Uri
                            } else {

                                Uri imageUri = data.getData();

                                try {

                                    finalPhoto = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);

                                } catch (Exception e) {

                                    finalPhoto = null;

                                }

                            }

                            recipeImageView.setImageBitmap(finalPhoto);

                            //Encode bitmap to Base64
                            encodedImage = encodeImage(finalPhoto);

                        }

                    }
                });

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                activityResultLauncher.launch(intent);

            }
        });

        choosePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                activityResultLauncher.launch(intent);

            }
        });

    }

    /**
     * encodeImage
     * Takes in a bitmap and encodes it to Base64.
     * @param imageBitmap
     * @return
     */
    private String encodeImage(Bitmap imageBitmap){

        ByteArrayOutputStream baos  = new ByteArrayOutputStream();

        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

        String encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

        return encodedImage;

    }
}