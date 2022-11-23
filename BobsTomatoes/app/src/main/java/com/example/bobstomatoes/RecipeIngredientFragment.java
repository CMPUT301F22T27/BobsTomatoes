package com.example.bobstomatoes;

import androidx.fragment.app.DialogFragment;

public class RecipeIngredientFragment extends DialogFragment {

    public interface OnRecipeFragmentListener{

        public void onAddOkPressed(Ingredient ingredient);


    }
}
