package com.example.bobstomatoes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeRecyclerAdapter.ViewHolder> {
    private ArrayList<Recipe> recipeList = new ArrayList<>();
    private Context context;

    private final RecyclerViewInterface recyclerViewInterface;

    public RecipeRecyclerAdapter(Context context, ArrayList<Recipe> recipeList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.recipeList = recipeList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public RecipeRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_recycler_recipe_list, viewGroup, false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(RecipeRecyclerAdapter.ViewHolder viewHolder, int position) {
        viewHolder.recipeNameView.setText(recipeList.get(position).getRecipeTitle());
        viewHolder.recipeCookTimeView.setText("Cook Time: " + recipeList.get(position).getRecipeTime());
        viewHolder.recipeServingSizeView.setText("Serving Size: " + recipeList.get(position).getRecipeServings());
        viewHolder.recipeCategoryView.setText("Category: " + recipeList.get(position).getRecipeCategory());
        viewHolder.recipeCommentViews.setText("Comments: " + recipeList.get(position).getRecipeComments());

    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView recipeNameView;
        TextView recipeCookTimeView;
        TextView recipeServingSizeView;
        TextView recipeCategoryView;
        TextView recipeCommentViews;
        public ViewHolder(View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            recipeNameView = itemView.findViewById(R.id.recipeNameTextView);
            recipeCookTimeView = itemView.findViewById(R.id.recipeTimeTextView);
            recipeServingSizeView = itemView.findViewById(R.id.recipeServingSizeTextView);
            recipeCategoryView = itemView.findViewById(R.id.recipeCategoryTextView);
            recipeCommentViews = itemView.findViewById(R.id.recipeCommentsTextView);

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
