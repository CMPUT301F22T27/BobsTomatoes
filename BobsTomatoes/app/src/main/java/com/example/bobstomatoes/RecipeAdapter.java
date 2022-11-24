package com.example.bobstomatoes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Custom array adapter class designed to work with recipe objects
 */
public class RecipeAdapter extends ArrayAdapter<Recipe> {

    private ArrayList<Recipe> recipes;
    private Context context;

    /**
     * RecipeAdapter constructor. Takes in context of activity and arraylist of recipes
     * @param context   activity context
     * @param recipes   array list of recipes
     */
    public RecipeAdapter(Context context, ArrayList<Recipe> recipes) {
        super(context, 0, recipes);
        this.recipes = recipes;
        this.context = context;
    }

    /**
     * Creates and inflates a new view representation of a recipe item
     * @param position      index of specified recipe
     * @param convertView   new view
     * @param parent        parent view
     * @return              returns new view representation of recipe items
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {

        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.recipe_adapter_content, parent,false);
        }

        Recipe recipe = recipes.get(position);

        TextView recipeName = view.findViewById(R.id.recipe_name_textview_id);

        TextView servingSize = view.findViewById(R.id.recipe_serving_textview_id);

        servingSize.setText("Size: " + String.valueOf(recipe.getRecipeServings()));
        recipeName.setText(recipe.getRecipeTitle());

        return view;
    }

}
