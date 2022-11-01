package com.example.bobstomatoes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MealPlanActivity extends AbstractNavigationBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_meal_plan);

        //Sets up buttons and onClickListeners for navigation bar
        initializeButtons(MealPlanActivity.this);

        mealPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do nothing
            }
        });


    }

}