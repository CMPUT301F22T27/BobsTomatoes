package com.example.bobstomatoes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

/**
 * ShoppingListActivity, displays shoppingList and extends AbstractNavigationBar
 */
public class ShoppingListActivity extends AbstractNavigationBar {

    /**
     * Create instance
     * Display shopping list activity
     * @param savedInstanceState    interface container containing savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Shopping List");
        setContentView(R.layout.activity_shopping_list);

        //Sets up buttons and onClickListeners for navigation bar
        initializeButtons(ShoppingListActivity.this);

    }
}