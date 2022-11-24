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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Class for Meal Plan which displays a calendar showing meal plans
 * extends AbstractNavigator
 */
public class MealPlanActivity extends AbstractNavigationBar implements MealPlanFragment.OnMealPlanFragmentListener, MealPlanCalendarAdapter.OnItemListener {

    /**
     * Create instance
     * Display meal plan activity
     * @param savedInstanceState    interface container containing savedInstanceState
     */

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    TextView descTitle;
    TextView ingredientTitle;
    TextView recipeTitle;
    ListView recipesList;
    ListView ingredientsList;
    Button openEdit;
    String globalDayText;
    MealPlanDB mealPlanDB;
    Boolean planFound = false;

    ArrayList<MealPlan> mealPlanList;
    ArrayList<Recipe> recipeList;
    ArrayList<Ingredient> ingredientList;
    ArrayAdapter<Recipe> recipeAdapter;
    ArrayAdapter<Ingredient> ingredientAdapter;

    CollectionReference mealPlanReference;
    int mealPlanPos;
    Bundle bundle;
    MealPlan currentMealPlan;
    String globalDate;
    TextView dayOfMonth;
    int oldPosition;
    Boolean planExist = false;

    LinearLayout layout;

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

        mealPlanDB = new MealPlanDB();
        mealPlanList = mealPlanDB.getMealPlanList();
        mealPlanReference = mealPlanDB.getMealPlanReference();
        descTitle = findViewById(R.id.title_ID);
        ingredientTitle = findViewById(R.id.meal_plan_desc_ingredient_ID);
        recipeTitle = findViewById(R.id.meal_plan_desc_recipe_ID);
        recipesList = findViewById(R.id.meal_plan_recipe_list_ID);
        ingredientsList = findViewById(R.id.meal_plan_ingredient_list_ID);
        openEdit = findViewById(R.id.meal_plan_edit_ID);
        layout = findViewById(R.id.mealPlanDetailLayout);

        // Populate meal plan list from database, by calling this, we can safely assume the list has been populated from the DataBase
        readData(new MealPlanFireStoreCallBack() {
            /**
             * Notify data change for ingredientList
             * @param mealPlanList    array list of ingredients
             */
            @Override
            public void onCallBack(ArrayList<MealPlan> mealPlanList) {

                for (int i = 0; i < mealPlanList.size(); i++){
                    Log.d("TESTING:", mealPlanList + "");
                    if (mealPlanList.get(i).getMealPlanDate().equals(globalDate)) {
                        currentMealPlan = mealPlanList.get(i);
                    }
                }

                for (int i = 0; i <= 40; i++) {
                    planExist = false;
                    for (int j = 0; j < mealPlanList.size(); j++) {
                        View check = calendarRecyclerView.getChildAt(i);
                        if(check != null) {
                            dayOfMonth = check.findViewById(R.id.cellDayText);
                            String day = (String) dayOfMonth.getText();

                            String date = selectedDate.toString();
                            date = date.substring(0, 8).concat(day);

                            if (mealPlanList.get(j).getMealPlanDate().equals(date)) {

                                //check.setBackgroundColor(Color.LTGRAY);
                                check.setActivated(true);
                            }
                        }
                    }
                }
            }
        });

        openEdit.setOnClickListener(view -> {
             MealPlanFragment fragment = new MealPlanFragment();
                fragment.setArguments(bundle);
                fragment.show(getSupportFragmentManager(), "EDIT/DELETE MEAL PLAN");
                planFound = false;
        });

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

        layout.setVisibility(View.INVISIBLE);
        openEdit.setVisibility(View.GONE);

        setMonthView();

        // Populate meal plan list from database, by calling this, we can safely assume the list has been populated from the DataBase
        readData(new MealPlanFireStoreCallBack() {
            /**
             * Notify data change for ingredientList
             * @param mealPlanList    array list of ingredients
             */
            @Override
            public void onCallBack(ArrayList<MealPlan> mealPlanList) {


                for (int i = 0; i < mealPlanList.size(); i++){
                    Log.d("TESTING:", mealPlanList + "");
                    if (mealPlanList.get(i).getMealPlanDate().equals(globalDate)) {
                        currentMealPlan = mealPlanList.get(i);
                    }
                }

                for (int i = 0; i <= 40; i++) {
                    for (int j = 0; j < mealPlanList.size(); j++) {
                        View check = calendarRecyclerView.getChildAt(i);
                        if (check != null) {
                            dayOfMonth = check.findViewById(R.id.cellDayText);
                            String day = (String) dayOfMonth.getText();

                            String date = selectedDate.toString();
                            date = date.substring(0, 8).concat(day);

                            if (mealPlanList.get(j).getMealPlanDate().equals(date)) {
                                //check.setBackgroundColor(Color.LTGRAY);
                                check.setActivated(true);
                            }
                        }
                    }
                }
            }
        });

    }

    public void nextMonthAction(View view)
    {
        selectedDate = selectedDate.plusMonths(1);

        layout.setVisibility(View.INVISIBLE);
        openEdit.setVisibility(View.GONE);

        setMonthView();

        // Populate meal plan list from database, by calling this, we can safely assume the list has been populated from the DataBase
        readData(new MealPlanFireStoreCallBack() {
            /**
             * Notify data change for ingredientList
             * @param mealPlanList    array list of ingredients
             */
            @Override
            public void onCallBack(ArrayList<MealPlan> mealPlanList) {


                for (int i = 0; i < mealPlanList.size(); i++){
                    Log.d("TESTING:", mealPlanList + "");
                    if (mealPlanList.get(i).getMealPlanDate().equals(globalDate)) {
                        currentMealPlan = mealPlanList.get(i);
                    }
                }

                for (int i = 0; i <= 40; i++) {
                    for (int j = 0; j < mealPlanList.size(); j++) {
                        View check = calendarRecyclerView.getChildAt(i);
                        if(check != null) {
                            dayOfMonth = check.findViewById(R.id.cellDayText);
                            String day = (String) dayOfMonth.getText();

                            String date = selectedDate.toString();
                            date = date.substring(0, 8).concat(day);

                            if (mealPlanList.get(j).getMealPlanDate().equals(date)) {
                                //check.setBackgroundColor(Color.LTGRAY);
                                check.setActivated(true);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(int position, String dayText, TextView day)
    {
        if(!dayText.equals(""))
        {
            mealPlanPos = position;
            globalDayText = dayText;

//            String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
//            Toast.makeText(this, message, Toast.LENGTH_LONG).show();

            calendarRecyclerView.getChildAt(position).setActivated(true);

            Log.d("PRINTING CHILD POSITION:", dayText + "");

//            String date = selectedDate.toString();
//            date = date.substring(0,8).concat(globalDayText);
//            Log.d("TESTING:", date);

            Log.d("MEAL PLAN POSITION:", position + "");


            for (int i = 0; i <= 40; i++) {

                View check = calendarRecyclerView.getChildAt(i);
                if(check != null) {
                    dayOfMonth = check.findViewById(R.id.cellDayText);
                    String tempDay = (String) dayOfMonth.getText();

                    String date = selectedDate.toString();
                    date = date.substring(0, 8).concat(tempDay);

                    boolean found = false;

                    for (int j = 0; j < mealPlanList.size(); j++) {
                        if (mealPlanList.get(j).getMealPlanDate().equals(date)) {
                            found = true;
                        }
                    }
                    Log.d("FOUND:", found + " " + i);
                    if (i != position) {

                        check.setSelected(false);

                        if (found) {
                            check.setActivated(true);

                        } else {
                            check.setActivated(false);
                        }


                    } else {

                        check.setSelected(true);

                    }
                }

            }

            String date = selectedDate.toString();
            globalDate = date.substring(0,8).concat(globalDayText);
            //Log.d("TESTING", date);

            for (int i = 0; i < mealPlanList.size(); i++) {
                Log.d("DATES:", mealPlanList.get(i).getMealPlanDate() + "");
                if (mealPlanList.get(i).getMealPlanDate().equals(globalDate)) {
                    currentMealPlan = mealPlanList.get(i);
                    planFound = true;
                }
            }

            if (planFound) {
                bundle = new Bundle();
                bundle.putString("selectedDate", globalDate);
                bundle.putParcelable("selectedMealPlan", currentMealPlan);

                descTitle.setVisibility(View.VISIBLE);
                ingredientTitle.setVisibility(View.VISIBLE);
                recipeTitle.setVisibility(View.VISIBLE);
                recipesList.setVisibility(View.VISIBLE);
                ingredientsList.setVisibility(View.VISIBLE);
                openEdit.setVisibility(View.VISIBLE);
                layout.setVisibility(View.VISIBLE);

                descTitle.setText(globalDate + " Meal Plan: ");
                recipeList = currentMealPlan.getMealPlanRecipes();
                ingredientList = currentMealPlan.getMealPlanIngredients();
                ingredientAdapter = new IngredientStorageAdapter(this, ingredientList);
                recipeAdapter = new RecipeAdapter(this, recipeList);

                recipesList.setAdapter(recipeAdapter);
                ingredientsList.setAdapter(ingredientAdapter);
                recipeAdapter.notifyDataSetChanged();
                ingredientAdapter.notifyDataSetChanged();
                planFound = false;

            }else{
                layout.setVisibility(View.INVISIBLE);
                descTitle.setVisibility(View.GONE);
                ingredientTitle.setVisibility(View.GONE);
                recipeTitle.setVisibility(View.GONE);
                recipesList.setVisibility(View.GONE);
                ingredientsList.setVisibility(View.GONE);
                openEdit.setVisibility(View.GONE);

            }
        }
    }




    public void onAddOkPressed(MealPlan mealPlan) {
        View check = calendarRecyclerView.getChildAt(mealPlanPos);
        check.setActivated(true);

        String date = selectedDate.toString();
        date = date.substring(0,8).concat(globalDayText);
        mealPlanDB.addMealPlan(mealPlan, date);

    }


    public void onEditOkPressed(MealPlan oldMealPlan, MealPlan updatedMealPlan) {
        mealPlanDB.editMealPlan(oldMealPlan, updatedMealPlan);
        recipeAdapter.notifyDataSetChanged();
        ingredientAdapter.notifyDataSetChanged();
    }


    public void onDeleteOkPressed(MealPlan mealPlan) {
        View check = calendarRecyclerView.getChildAt(mealPlanPos);
        check.setActivated(false);
        check.setSelected(false);

        mealPlanDB.removeMealPlan(mealPlan);


    }

//
//    public void openEditPressed(){
//        MealPlanFragment fragment = new MealPlanFragment();
//        fragment.setArguments(bundle);
//        fragment.show(getSupportFragmentManager(), "EDIT/DELETE MEAL PLAN");
//        planFound = false;
//
//        recipeAdapter.notifyDataSetChanged();
//        ingredientAdapter.notifyDataSetChanged();
//    }

    public void onCancelPressed(){

    }

    public void readData(MealPlanFireStoreCallBack callBack) {
        mealPlanReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    mealPlanList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        MealPlan mealPlan = document.toObject(MealPlan.class);
                        Log.d("IN READ DATA:", mealPlan.getMealPlanDate() + "");
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
     * Call back MealPlanList
     * Basically allows us to access the MealPlanList outside of the onComplete and it ensures that the onComplete has fully populated our list
     */
    private interface MealPlanFireStoreCallBack {
        void onCallBack(ArrayList<MealPlan> mealPlanList);
    }

}
