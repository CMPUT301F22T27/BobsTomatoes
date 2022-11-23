package com.example.bobstomatoes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class ShoppingListRecyclerAdapter extends RecyclerView.Adapter<ShoppingListRecyclerAdapter.ViewHolder>{

    private ArrayList<Ingredient> ingredientList = new ArrayList<>();
    private Context context;
    private final RecyclerViewInterface recyclerViewInterface;

    public ShoppingListRecyclerAdapter(Context context, ArrayList<Ingredient> ingredientList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.ingredientList = ingredientList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ShoppingListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_recycler_shopping_list_recyclerlist, viewGroup, false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(ShoppingListRecyclerAdapter.ViewHolder viewHolder, int position) {
        viewHolder.ingredientNameView.setText(ingredientList.get(position).getIngredientDesc());
        viewHolder.ingredientUnitView.setText("Cost: " + ingredientList.get(position).getIngredientUnit());
        viewHolder.ingredientAmountView.setText("Amount: " + ingredientList.get(position).getIngredientAmount());
        viewHolder.ingredientCategoryView.setText("Catg: " + ingredientList.get(position).getIngredientCategory());
        //viewHolder.checkBox.setChecked(false);

    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientNameView;
        TextView ingredientUnitView;
        TextView ingredientAmountView;
        TextView ingredientCategoryView;
        CheckBox checkBox;

        public ViewHolder(View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            ingredientNameView = itemView.findViewById(R.id.shoppingListIngredientDescTextView);
            ingredientUnitView = itemView.findViewById(R.id.shoppingListIngredientUnitTextView);
            ingredientAmountView = itemView.findViewById(R.id.shoppingListIngredientAmountTextView);
            ingredientCategoryView = itemView.findViewById(R.id.shoppingListIngredientCategoryTextView);
            checkBox = itemView.findViewById(R.id.checkbox);

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
