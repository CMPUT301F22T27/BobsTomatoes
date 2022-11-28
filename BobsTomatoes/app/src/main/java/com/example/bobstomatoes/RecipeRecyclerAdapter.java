package com.example.bobstomatoes;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

/**
 * Custom recycler adapter intended for use with RecipeActivity
 */
public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeRecyclerAdapter.RecipeViewHolder> {
    private ArrayList<Recipe> recipeList = new ArrayList<>();
    private Context context;

    private final RecyclerViewInterface recyclerViewInterface;

    /**
     * Constructor for RecipeRecyclerAdapter
     * @param context
     * @param recipeList recipes to display
     * @param recyclerViewInterface
     */
    public RecipeRecyclerAdapter(Context context, ArrayList<Recipe> recipeList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.recipeList = recipeList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    /**
     * Inflates custom ViewHolder to display in recycler view
     * @param viewGroup
     * @param i
     * @return
     */
    @NonNull
    @Override
    public RecipeRecyclerAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_recycler_recipe_list, viewGroup, false);
        return new RecipeViewHolder(view, recyclerViewInterface);
    }

    /**
     * Populates custom viewholder with recipe info
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecipeRecyclerAdapter.RecipeViewHolder viewHolder, int position) {
        viewHolder.recipeNameView.setText(recipeList.get(position).getRecipeTitle());
        viewHolder.recipeCookTimeView.setText("Prep Time: " + recipeList.get(position).getRecipeTime());
        viewHolder.recipeServingSizeView.setText("Serving Size: " + recipeList.get(position).getRecipeServings());
        viewHolder.recipeCategoryView.setText("Category: " + recipeList.get(position).getRecipeCategory());
        viewHolder.recipeCommentViews.setText(recipeList.get(position).getRecipeComments());
        viewHolder.recipeImageView.setImageBitmap(recipeList.get(position).getDecodedImage());

    }

    /**
     * Returns number of items in adapter
     * @return
     */
    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    /**
     * Custom view holder class for use with RecipeRecyclerAdapter
     * Creates viewholder from xml template
     */
    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView recipeNameView;
        TextView recipeCookTimeView;
        TextView recipeServingSizeView;
        TextView recipeCategoryView;
        TextView recipeCommentViews;
        ImageView recipeImageView;

        /**
         * RecipeViewHolder constructor
         * @param itemView
         * @param recyclerViewInterface
         */
        public RecipeViewHolder(View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            recipeNameView = itemView.findViewById(R.id.recipeNameTextView);
            recipeCookTimeView = itemView.findViewById(R.id.recipeTimeTextView);
            recipeServingSizeView = itemView.findViewById(R.id.recipeServingSizeTextView);
            recipeCategoryView = itemView.findViewById(R.id.recipeCategoryTextView);
            recipeCommentViews = itemView.findViewById(R.id.recipeCommentsTextView);
            recipeImageView = itemView.findViewById(R.id.recipeImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null){
                        int pos = getBindingAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }

}
