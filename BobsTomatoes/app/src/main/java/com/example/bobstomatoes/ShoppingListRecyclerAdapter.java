package com.example.bobstomatoes;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Recycler adapter for shopping list
 */
public class ShoppingListRecyclerAdapter extends RecyclerView.Adapter<ShoppingListRecyclerAdapter.ViewHolder> {

    private ArrayList<Ingredient> ingredientList = new ArrayList<>();
    private int currentIngredientAmount;
    private ArrayList<Ingredient> checkedIngredients = new ArrayList<>();
    private ArrayList<Integer> databaseIngredientAmountList = new ArrayList<>();

    private Context context;
    private final RecyclerViewInterface recyclerViewInterface;
    private FragmentManager fragmentManager;
    private int pos;
    private HashMap<String, Integer> currentAmounts = new HashMap<>();
    private HashMap<String, Boolean> checkedItems = new HashMap<>();

    private IngredientDB ingredientDB;


    public ShoppingListRecyclerAdapter(Context context, ArrayList<Ingredient> ingredientList, HashMap<String, Boolean> checkedItems, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.ingredientList = ingredientList;
        this.recyclerViewInterface = recyclerViewInterface;
        this.checkedItems = checkedItems;
        this.fragmentManager = fragmentManager;
    }


    /**
     * Takes an ingredient name and toggles it's checkbox to checked
     * @param ingredientName
     */
    public void setChecked(String ingredientName){

        checkedItems.put(ingredientName, true);

    }

    /**
     * Takes an ingredient name and toggles it's checkbox to unchecked
     * @param ingredientName
     */
    public void setUnchecked(String ingredientName){

        checkedItems.put(ingredientName, false);

    }

    /**
     * Updates the amount of an ingredient that has been bought
     *
     * @param viewHolder
     * @param newInt
     * @param ingredientName
     */
    public void setBoughtAmount(ShoppingListRecyclerAdapter.ViewHolder viewHolder, int newInt, String ingredientName, Ingredient lastChangedIngredient) {
        Integer currentNum = currentAmounts.get(ingredientName);

        if (currentNum != null) {

            //newInt = currentNum + newInt;
            currentAmounts.put(ingredientName, newInt);

        } else {

            currentAmounts.put(ingredientName, newInt);

        }

        if (viewHolder != null) {

            viewHolder.ingredientCurrentAmountView.setText("Current Amount: " + newInt);

        }

    }

    /**
     * Returns amount of an ingredient that has been bought
     *
     * @param ingredientName
     * @return
     */
    public int getBoughtAmount(String ingredientName) {

        Integer currentNum = currentAmounts.get(ingredientName);

        if (currentNum != null) {

            return currentNum;

        } else {

            return 0;
        }

    }

    /**
     *
     * @param viewGroup
     * @param i
     * @return
     */
    @NonNull
    @Override
    public ShoppingListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_recycler_shopping_list_recyclerlist, viewGroup, false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    /**
     *
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(ShoppingListRecyclerAdapter.ViewHolder viewHolder, int position) {
        int ingredientPos = position;

        viewHolder.ingredientNameView.setText(ingredientList.get(position).getIngredientDesc());
        viewHolder.ingredientUnitView.setText("Unit: $" + ingredientList.get(position).getIngredientUnit());

        Integer tempInt = currentAmounts.get(ingredientList.get(position).getIngredientDesc());
        if (tempInt != null) {

            viewHolder.ingredientCurrentAmountView.setText("Current Amount: " + tempInt);


        } else {

            viewHolder.ingredientCurrentAmountView.setText("Current Amount: " + "0");

        }

        Boolean tempBool = checkedItems.get(ingredientList.get(position).getIngredientDesc());

        if(tempBool != null){

            viewHolder.checkBox.setChecked(tempBool);

        }



        viewHolder.ingredientAmountNeededView.setText("Amount Needed: " + ingredientList.get(position).getIngredientAmount());
        viewHolder.ingredientCategoryView.setText("Category: " + ingredientList.get(position).getIngredientCategory());
        viewHolder.checkBox.setClickable(false);



        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ingredient selectedIngredient = ingredientList.get(ingredientPos);
                Bundle bundle = new Bundle();

                bundle.putParcelable("selectedIngredient", selectedIngredient);
                bundle.putInt("oldIngredientPos", ingredientPos);

                ShoppingListActivity shoppingListActivity = (ShoppingListActivity) context;

                ShoppingListFragment fragment = new ShoppingListFragment();
                fragment.setArguments(bundle);
                fragment.show(shoppingListActivity.getSupportFragmentManager(), "UPDATE INGREDIENT FOR SHOPPING LIST");
            }

        });
    }

    /**
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return ingredientList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView ingredientNameView;
        TextView ingredientUnitView;
        TextView ingredientAmountNeededView;
        TextView ingredientCurrentAmountView;
        TextView ingredientCategoryView;
        CheckBox checkBox;

        ShoppingListItemClickInterface itemClick;

        /**
         *
         * @param itemView
         * @param recyclerViewInterface
         */
        public ViewHolder(View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            ingredientNameView = itemView.findViewById(R.id.shoppingListIngredientDescTextView);
            ingredientUnitView = itemView.findViewById(R.id.shoppingListIngredientUnitTextView);
            ingredientAmountNeededView = itemView.findViewById(R.id.shoppingListIngredientAmountNeededTextView);
            ingredientCurrentAmountView = itemView.findViewById(R.id.shoppingListIngredientCurrentAmountTextView);
            ingredientCategoryView = itemView.findViewById(R.id.shoppingListIngredientCategoryTextView);
            checkBox = itemView.findViewById(R.id.checkbox);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        pos = getBindingAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

            checkBox.setOnClickListener(this);
        }

        /**
         *
         * @param view
         */
        @Override
        public void onClick(View view) {
            this.itemClick.OnCheckBoxClick(view, getLayoutPosition());
        }
    }

}
