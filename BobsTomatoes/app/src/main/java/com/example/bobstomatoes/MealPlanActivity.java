package com.example.bobstomatoes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Class for Meal Plan which displays a calendar showing meal plans
 * extends AbstractNavigator
 */
public class MealPlanActivity extends AbstractNavigationBar implements MealPlanCalendarAdapter.OnItemListener {

    /**
     * Create instance
     * Display meal plan activity
     * @param savedInstanceState    interface container containing savedInstanceState
     */

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    MealPlanDB mealPlanDB;

    ArrayList<MealPlan> mealPlanList;
    CollectionReference mealPlanReference;
    int mealPlanPos;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Modify ActionBar
        setTitle("Meal Plan");
        ActionBar actionBar; // Define ActionBar object
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable =
                new ColorDrawable(Color.parseColor("#4260F5")); // Define ColorDrawable object + parse color
        actionBar.setBackgroundDrawable(colorDrawable); // Set BackgroundDrawable

        setContentView(R.layout.main_meal_plan);

        //Sets up buttons and onClickListeners for navigation bar
        initializeButtons(MealPlanActivity.this);
        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();

    }

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        MealPlanCalendarAdapter calendarAdapter = new MealPlanCalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date)
    {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        int j = 0;
        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek|| i > daysInMonth + dayOfWeek)
            {
                j = j + 1;
                if (dayOfWeek == 7 && j <= 7)
                    continue;
                daysInMonthArray.add("");
            }
            else
            {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return  daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public void previousMonthAction(View view)
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view)
    {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, String dayText, TextView day)
    {
        if(!dayText.equals(""))
        {
            calendarRecyclerView.getChildAt(position).setSelected(true);
            String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            calendarRecyclerView.getChildAt(position).setBackgroundColor(Color.LTGRAY);

            for(int i = 0; i <= 40; i++) {
                View check = calendarRecyclerView.getChildAt(i);
                if (Objects.nonNull(check))
                    if (i != position)
                        check.setBackgroundColor(Color.WHITE);
            }

        }
    }


    public void onAddOkPressed(MealPlan mealPlan) {
        mealPlanDB.addMealPlan(mealPlan);
    }


    public void onEditOkPressed(MealPlan mealPlan) {
        mealPlanDB.editMealPlan(mealPlanPos, mealPlan);
    }


    public void onDeleteOkPressed(MealPlan mealPlan) {
        mealPlanDB.removeMealPlan(mealPlan);
    }

    public void readData(MealPlanFireStoreCallBack callBack) {
        mealPlanReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        MealPlan mealPlan = document.toObject(MealPlan.class);
                        mealPlanList.add(mealPlan);
                    }
                    callBack.onCallBack(mealPlanList);
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    /**
     * Interface
     * Call back recipeList
     * Basically allows us to access the recipeList outside of the onComplete and it ensures that the onComplete has fully populated our list
     */
    private interface MealPlanFireStoreCallBack {
        void onCallBack(ArrayList<MealPlan> mealPlanList);
    }

}
