package com.example.bobstomatoes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class IngredientStorageRecyclerAdapter extends RecyclerView.Adapter<IngredientStorageRecyclerAdapter.ViewHolder> {
    private ArrayList<Ingredient> ingredientList = new ArrayList<>();
    private Context context;


    public IngredientStorageRecyclerAdapter(Context context, ArrayList<Ingredient> ingredientList) {
        this.context = context;
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public IngredientStorageRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_recycler_ingredient_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientStorageRecyclerAdapter.ViewHolder viewHolder, int position) {
        viewHolder.ingredientView.setText(ingredientList.get(position).getIngredientDesc());
        viewHolder.locationView.setText(ingredientList.get(position).getIngredientLocation());
        viewHolder.amountView.setText(String.valueOf(ingredientList.get(position).getIngredientAmount()));
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView ingredientView;
        TextView locationView;
        TextView amountView;
        public ViewHolder(View itemView) {
            super(itemView);
            ingredientView = itemView.findViewById(R.id.ingredientDescTextView);
            locationView = itemView.findViewById(R.id.ingredientLocationTextView);
            amountView = itemView.findViewById(R.id.ingredientAmountTextView);

        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getBindingAdapterPosition();
        }
    }
}
