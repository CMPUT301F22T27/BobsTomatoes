package com.example.bobstomatoes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;


/**
 * Custom recycler adapter class used in IngredientStorageActivity
 */
public class IngredientStorageRecyclerAdapter extends RecyclerView.Adapter<IngredientStorageRecyclerAdapter.IngredientViewHolder> {
    private ArrayList<Ingredient> ingredientList = new ArrayList<>();
    private Context context;

    private final RecyclerViewInterface recyclerViewInterface;

    /**
     * Creates new instance of the adapter. IngredientList contains ingredients to be displayed
     * @param context
     * @param ingredientList
     * @param recyclerViewInterface
     */
    public IngredientStorageRecyclerAdapter(Context context, ArrayList<Ingredient> ingredientList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.ingredientList = ingredientList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    /**
     * Occurs when a new ViewHolder is created to display an ingredient
     * Inflates viewholder
     * @param viewGroup
     * @param i
     * @return
     */
    @NonNull
    @Override
    public IngredientStorageRecyclerAdapter.IngredientViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_recycler_ingredient_list, viewGroup, false);
        return new IngredientViewHolder(view, recyclerViewInterface);
    }

    /**
     * Sets displayed information within a viewholder
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(IngredientStorageRecyclerAdapter.IngredientViewHolder viewHolder, int position) {
        viewHolder.ingredientView.setText(ingredientList.get(position).getIngredientDesc());
        viewHolder.locationView.setText(ingredientList.get(position).getIngredientLocation());
        viewHolder.amountView.setText("Amount: " + ingredientList.get(position).getIngredientAmount());
        viewHolder.dateView.setText("Date: " + ingredientList.get(position).getIngredientDate());
        viewHolder.unitView.setText("Unit: $" + ingredientList.get(position).getIngredientUnit());
        viewHolder.categoryView.setText("Category: " + ingredientList.get(position).getIngredientCategory());

    }

    /**
     * Returns number of items adapter holds
     * @return
     */
    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    /**
     * Class for creating an IngredientViewHolder
     * Custom ViewHolder intended for use with ingredients
     */
    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientView;
        TextView locationView;
        TextView amountView;
        TextView dateView;
        TextView unitView;
        TextView categoryView;

        /**
         * Constructor for IngredientViewHolder
         * @param itemView
         * @param recyclerViewInterface
         */
        public IngredientViewHolder(View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            ingredientView = itemView.findViewById(R.id.ingredientDescTextView);
            locationView = itemView.findViewById(R.id.ingredientLocationTextView);
            amountView = itemView.findViewById(R.id.ingredientAmountTextView);
            dateView = itemView.findViewById(R.id.ingredientDateTextView);
            unitView = itemView.findViewById(R.id.ingredientUnitTextView);
            categoryView = itemView.findViewById(R.id.ingredientCategoryTextView);

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
