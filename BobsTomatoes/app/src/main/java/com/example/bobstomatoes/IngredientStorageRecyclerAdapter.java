package com.example.bobstomatoes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class IngredientStorageRecyclerAdapter extends RecyclerView.Adapter<IngredientStorageRecyclerAdapter.ViewHolder> {
    private ArrayList<Ingredient> ingredientList = new ArrayList<>();
    private Context context;

    private final RecyclerViewInterface recyclerViewInterface;

    public IngredientStorageRecyclerAdapter(Context context, ArrayList<Ingredient> ingredientList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.ingredientList = ingredientList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public IngredientStorageRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_recycler_ingredient_list, viewGroup, false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(IngredientStorageRecyclerAdapter.ViewHolder viewHolder, int position) {
        viewHolder.ingredientView.setText(ingredientList.get(position).getIngredientDesc());
        viewHolder.locationView.setText(ingredientList.get(position).getIngredientLocation());
        viewHolder.amountView.setText("Amount: " + ingredientList.get(position).getIngredientAmount());
        viewHolder.dateView.setText(ingredientList.get(position).getIngredientDate());
        viewHolder.unitView.setText("Unit: $" + ingredientList.get(position).getIngredientUnit());
        viewHolder.categoryView.setText(ingredientList.get(position).getIngredientCategory());

    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView ingredientView;
        TextView locationView;
        TextView amountView;
        TextView dateView;
        TextView unitView;
        TextView categoryView;
        public ViewHolder(View itemView, RecyclerViewInterface recyclerViewInterface) {
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
