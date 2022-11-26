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

public class ShoppingListRecyclerAdapter extends RecyclerView.Adapter<ShoppingListRecyclerAdapter.ViewHolder> {

    private ArrayList<Ingredient> ingredientList = new ArrayList<>();
    private int currentIngredientAmount;
    private ArrayList<Ingredient> checkedIngredients = new ArrayList<>();
    private Context context;
    private final RecyclerViewInterface recyclerViewInterface;
    private FragmentManager fragmentManager;
    int pos;
    private HashMap<String, Integer> currentAmounts = new HashMap<>();
    Ingredient databaseIngredient;
    private IngredientDB ingredientDB;
    boolean isDocument = false;


    public ShoppingListRecyclerAdapter(Context context, ArrayList<Ingredient> ingredientList, int currentIngredientAmount, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.ingredientList = ingredientList;
        this.recyclerViewInterface = recyclerViewInterface;
        this.currentIngredientAmount = currentIngredientAmount;
        this.fragmentManager = fragmentManager;
    }

    /**
     * Updates the amount of an ingredient that has been bought
     *
     * @param viewHolder
     * @param newInt
     * @param ingredientName
     */
    public void setBoughtAmount(ShoppingListRecyclerAdapter.ViewHolder viewHolder, int newInt, String ingredientName) {

        Integer currentNum = currentAmounts.get(ingredientName);

        if (currentNum != null) {

            //newInt = currentNum + newInt;
            newInt = newInt;
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

    @NonNull
    @Override
    public ShoppingListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_recycler_shopping_list_recyclerlist, viewGroup, false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    public void updateInt(ShoppingListRecyclerAdapter.ViewHolder viewHolder, int newInt) {
        viewHolder.ingredientCurrentAmountView.setText("Current Amount: " + newInt);
    }

    @Override
    public void onBindViewHolder(ShoppingListRecyclerAdapter.ViewHolder viewHolder, int position) {
        viewHolder.ingredientNameView.setText(ingredientList.get(position).getIngredientDesc());
        viewHolder.ingredientUnitView.setText("Unit: $" + ingredientList.get(position).getIngredientUnit());

        Integer tempInt = currentAmounts.get(ingredientList.get(position).getIngredientDesc());

        if (tempInt != null) {

            viewHolder.ingredientCurrentAmountView.setText("Current Amount: " + tempInt);

            if (tempInt >= ingredientList.get(position).getIngredientAmount()) {

                viewHolder.checkBox.setChecked(true);
                ingredientDB = new IngredientDB();
                CollectionReference ingredientRef = ingredientDB.getIngredientReference();
                DocumentReference ingredientDocumentRef = ingredientDB.getIngredientReference().document(ingredientList.get(position).getIngredientDesc());



                readIngredientData(ingredientDocumentRef, new IngredientFireStoreCallback() {
                    @Override
                    public void onCallBack(Ingredient databaseIngredient) {
                        Ingredient newIngredient = null;

                        if (isDocument) { // If ingredient does exist in the database, add the amount you bought + the amount in the ingredient storage
                            newIngredient = new Ingredient(ingredientList.get(position).getIngredientDesc(), ingredientList.get(position).getIngredientDate(),
                                    ingredientList.get(position).getIngredientLocation(), databaseIngredient.getIngredientAmount() + tempInt,
                                    ingredientList.get(position).getIngredientUnit(), ingredientList.get(position).getIngredientCategory());

                        } else { // If the ingredient does not exist in the database, then simply add the amount you bought, can not test at the moment however.
                            newIngredient = new Ingredient(ingredientList.get(position).getIngredientDesc(), ingredientList.get(position).getIngredientDate(),
                                    ingredientList.get(position).getIngredientLocation(), tempInt,
                                    ingredientList.get(position).getIngredientUnit(), ingredientList.get(position).getIngredientCategory());
                        }

                        isDocument = false;
                        ingredientDB.addIngredient(newIngredient);
                    }
                });





            } else {
                viewHolder.checkBox.setChecked(false);
            }

        } else {

            viewHolder.ingredientCurrentAmountView.setText("Current Amount: " + "0");

        }

        viewHolder.ingredientAmountNeededView.setText("Amount Needed: " + ingredientList.get(position).getIngredientAmount());
        viewHolder.ingredientCategoryView.setText("Category: " + ingredientList.get(position).getIngredientCategory());

        viewHolder.checkBox.setClickable(false);

        int ingredientPos = position;

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

        public void setItemClickListener(ShoppingListItemClickInterface itemClick) {
            this.itemClick = itemClick;
        }

        @Override
        public void onClick(View view) {
            this.itemClick.OnCheckBoxClick(view, getLayoutPosition());
        }
    }

    /**
     * Interface
     * Call back ingredientList
     * Allows us to access the ingredientList outside of the onComplete and it
     * ensures that the onComplete has fully populated our list
     */
    private interface IngredientFireStoreCallback {
        void onCallBack(Ingredient ingredient);
    }

    /**
     * Populates from data base using callBack
     *
     * @param callBack ingredient database
     */
    public void readIngredientData(DocumentReference ingredientReference, ShoppingListRecyclerAdapter.IngredientFireStoreCallback callBack) {
        ingredientReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        databaseIngredient = document.toObject(Ingredient.class);
                        isDocument = true;
                    } else {
                        isDocument = false;
                    }

                    callBack.onCallBack(databaseIngredient);
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });

    }
}
