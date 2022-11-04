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
 * Custom array adapter class designed to work with ingredient objects
 */
public class IngredientStorageAdapter extends ArrayAdapter<Ingredient> {

    private ArrayList<Ingredient> ingredients;
    private Context context;

    /**
     * IngredientStorageAdapter constructor. Takes in context of activity and arraylist of recipes
     * @param context       activity context
     * @param ingredients   array list of ingredients
     */
    public IngredientStorageAdapter(Context context, ArrayList<Ingredient> ingredients) {
        super(context, 0, ingredients);
        this.ingredients = ingredients;
        this.context = context;
    }

    /**
     * Creates and inflates a new view representation of a ingredient item
     * @param position      index of specified ingredients
     * @param convertView   new view
     * @param parent        parent view
     * @return              returns new view representation
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {

        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.ingredient_storage_adapter_content, parent,false);
        }

        Ingredient ingredient = ingredients.get(position);

        TextView ingredientName = view.findViewById(R.id.ingredient_name_textview_id);

        ingredientName.setText(ingredient.getIngredientDesc());

        return view;
    }

}
