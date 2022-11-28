package com.example.bobstomatoes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Class for Meal Plan which displays a calendar showing meal plans
 * extends AbstractNavigator
 */
public class MealPlanActivity extends AbstractNavigationBar implements MealPlanFragment.OnMealPlanFragmentListener,SpecifyIngredientAmountFragment.OnRecipeIngredientListener, MealPlanScaleFragment.OnMealPlanScaleFragmentListener, MealPlanCalendarAdapter.OnItemListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    private TextView descTitle;
    private TextView noMealPlanText;
    private ListView recipesList;
    private ListView ingredientsList;
    private Button openEdit;
    private Button scaleRecipeButton;
    private String globalDayText;
    private MealPlanDB mealPlanDB;
    private Boolean planFound = false;

    private ArrayList<MealPlan> mealPlanList;
    private ArrayList<Recipe> recipeList = new ArrayList<>();
    private ArrayList<Ingredient> ingredientList = new ArrayList<>();
    private ArrayAdapter<Recipe> recipeAdapter;
    private ArrayAdapter<Ingredient> ingredientAdapter;

    private CollectionReference mealPlanReference;
    private int mealPlanPos;
    private Bundle bundle;
    private Bundle scaleBundle;
    private MealPlan currentMealPlan;
    private String globalDate = "";
    private TextView dayOfMonth;

    private MealPlanFragment editFragment;

    private ArrayList<Ingredient> globalIngredientList = new ArrayList<>();

    private LinearLayout mealPlanDetailsLinearLayout;
    private LinearLayout mealPlanDetailSubTitles;
    private LinearLayout mealPlanDetailDesc;
    private LinearLayout mealPlanButtonsLinearLayout;


    private Dialog progressBar;


    /**
     * Create instance
     * Display meal plan activity
     * @param savedInstanceState    interface container containing savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.progress_dialog);
        progressBar = builder.create();

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
        noMealPlanText = findViewById(R.id.no_meal_plan_ID);
        noMealPlanText.setText("Please Select a Date");
        recipesList = findViewById(R.id.meal_plan_recipe_list_ID);
        ingredientsList = findViewById(R.id.meal_plan_ingredient_list_ID);
        openEdit = findViewById(R.id.meal_plan_edit_ID);
        scaleRecipeButton = findViewById(R.id.scale_meal_plan_ID);

        mealPlanButtonsLinearLayout = findViewById(R.id.meal_plan_bottom_button_layout);
        mealPlanDetailsLinearLayout = findViewById(R.id.mealPlanDetailLayout);
        mealPlanDetailSubTitles = findViewById(R.id.meal_plan_subtitles_layout);
        mealPlanDetailDesc = findViewById(R.id.meal_plan_lists_layout);

        // Populate meal plan list from database, by calling this, we can safely assume the list has been populated from the DataBase
        readData(new MealPlanFireStoreCallBack() {
            /**
             * Notify data change for ingredientList
             * @param mealPlanList    array list of ingredients
             */
            @Override
            public void onCallBack(ArrayList<MealPlan> mealPlanList) {

                for (int i = 0; i < mealPlanList.size(); i++){
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

        openEdit.setOnClickListener(view -> {
            editFragment = new MealPlanFragment();
            editFragment.setArguments(bundle);
            editFragment.show(getSupportFragmentManager(), "EDIT/DELETE MEAL PLAN");
            planFound = false;
        });

        scaleRecipeButton.setOnClickListener(view -> {
            MealPlanScaleFragment fragment = new MealPlanScaleFragment();
            fragment.setArguments(scaleBundle);
            fragment.show(getSupportFragmentManager(), "SCALE RECIPES IN MEAL PLAN");
        });

    }

    /**
     *
     */
    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    /**
     *
     */
    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        MealPlanCalendarAdapter calendarAdapter = new MealPlanCalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);


        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    /**
     *
     * @param date
     * @return
     */
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

    /**
     *
     * @param date
     * @return
     */
    private String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    /**
     *
     * @param view
     */
    public void previousMonthAction(View view)
    {
        selectedDate = selectedDate.minusMonths(1);

        noMealPlanText.setText("Please Select a Date");
        noMealPlanText.setVisibility(View.VISIBLE);
        mealPlanDetailSubTitles.setVisibility(View.GONE);
        mealPlanDetailDesc.setVisibility(View.GONE);
        descTitle.setVisibility(View.GONE);
        mealPlanButtonsLinearLayout.setVisibility(View.GONE);
        globalDate = "";

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

    /**
     *
     * @param view
     */
    public void nextMonthAction(View view)
    {
        selectedDate = selectedDate.plusMonths(1);

        noMealPlanText.setText("Please Select a Date");
        noMealPlanText.setVisibility(View.VISIBLE);
        mealPlanDetailSubTitles.setVisibility(View.GONE);
        mealPlanDetailDesc.setVisibility(View.GONE);
        descTitle.setVisibility(View.GONE);
        mealPlanButtonsLinearLayout.setVisibility(View.GONE);
        globalDate = "";

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

    /**
     *
     * @param position
     * @param dayText
     * @param day
     */
    @Override
    public void onItemClick(int position, String dayText, TextView day)
    {
        if(!dayText.equals(""))
        {
            mealPlanPos = position;
            globalDayText = dayText;

            calendarRecyclerView.getChildAt(position).setActivated(true);

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

            for (int i = 0; i < mealPlanList.size(); i++) {
                if (mealPlanList.get(i).getMealPlanDate().equals(globalDate)) {
                    currentMealPlan = mealPlanList.get(i);
                    planFound = true;
                }
            }

            if (planFound) {
                bundle = new Bundle();
                bundle.putString("selectedDate", globalDate);
                bundle.putParcelable("selectedMealPlan", currentMealPlan);

                scaleBundle = new Bundle();
                scaleBundle.putParcelable("selectedMealPlan", currentMealPlan);

                noMealPlanText.setVisibility(View.GONE);
                descTitle.setVisibility(View.VISIBLE);
                mealPlanDetailSubTitles.setVisibility(View.VISIBLE);
                mealPlanDetailDesc.setVisibility(View.VISIBLE);
                mealPlanButtonsLinearLayout.setVisibility(View.VISIBLE);

                descTitle.setText(globalDate + " Meal Plan: ");

                recipeList = currentMealPlan.getMealPlanRecipes();
                ingredientList = currentMealPlan.getMealPlanIngredients();

                ingredientAdapter = new IngredientStorageMealPlanAdapter(this, ingredientList);
                recipeAdapter = new RecipeAdapter(this, recipeList);

                recipesList.setAdapter(recipeAdapter);
                ingredientsList.setAdapter(ingredientAdapter);

                recipeAdapter.notifyDataSetChanged();
                ingredientAdapter.notifyDataSetChanged();
                planFound = false;

            }else {

                noMealPlanText.setText("No Meal Plan On This Date");
                noMealPlanText.setVisibility(View.VISIBLE);
                mealPlanDetailSubTitles.setVisibility(View.GONE);
                mealPlanDetailDesc.setVisibility(View.GONE);
                descTitle.setVisibility(View.GONE);
                mealPlanButtonsLinearLayout.setVisibility(View.GONE);

            }
        }
    }


    /**
     *
     * @param mealPlan
     */
    public void onAddOkPressed(MealPlan mealPlan) {
        View check = calendarRecyclerView.getChildAt(mealPlanPos);
        check.setActivated(true);

        String date = selectedDate.toString();
        date = date.substring(0,8).concat(globalDayText);

        mealPlanDB.addMealPlan(mealPlan, date);

        descTitle.setVisibility(View.VISIBLE);
        mealPlanDetailSubTitles.setVisibility(View.VISIBLE);
        mealPlanDetailDesc.setVisibility(View.VISIBLE);
        //mealPlanDetailsLinearLayout.setVisibility(View.VISIBLE);

        mealPlanButtonsLinearLayout.setVisibility(View.VISIBLE);

        recipeList = mealPlan.getMealPlanRecipes();
        ingredientList = mealPlan.getMealPlanIngredients();

        ingredientAdapter = new IngredientStorageMealPlanAdapter(this, ingredientList);
        recipeAdapter = new RecipeAdapter(this, recipeList);

        recipesList.setAdapter(recipeAdapter);
        ingredientsList.setAdapter(ingredientAdapter);

        recipeAdapter.notifyDataSetChanged();
        ingredientAdapter.notifyDataSetChanged();

    }


    /**
     *
     * @param oldMealPlan
     * @param updatedMealPlan
     */
    public void onEditOkPressed(MealPlan oldMealPlan, MealPlan updatedMealPlan) {

        recipeList.clear();
        ingredientList.clear();

        for (int i = 0; i < updatedMealPlan.getMealPlanIngredients().size(); i++){
            ingredientList.add(updatedMealPlan.getMealPlanIngredients().get(i));
        }

        for (int i = 0; i < updatedMealPlan.getMealPlanRecipes().size(); i++){
            recipeList.add(updatedMealPlan.getMealPlanRecipes().get(i));
        }


        mealPlanDB.editMealPlan(oldMealPlan, updatedMealPlan);
        recipeAdapter.notifyDataSetChanged();
        ingredientAdapter.notifyDataSetChanged();
    }


    /**
     *
     * @param mealPlan
     */
    public void onDeleteOkPressed(MealPlan mealPlan) {
        View check = calendarRecyclerView.getChildAt(mealPlanPos);
        check.setActivated(false);
        check.setSelected(false);

        mealPlanDB.removeMealPlan(mealPlan);

        mealPlanButtonsLinearLayout.setVisibility(View.GONE);
        descTitle.setVisibility(View.GONE);
        mealPlanDetailSubTitles.setVisibility(View.GONE);
        mealPlanDetailDesc.setVisibility(View.GONE);
        //mealPlanDetailsLinearLayout.setVisibility(View.INVISIBLE);

        recipeAdapter.notifyDataSetChanged();
        ingredientAdapter.notifyDataSetChanged();
    }

    /**
     *
     * @param callBack
     */
    public void readData(MealPlanFireStoreCallBack callBack) {
        showDialog(true);
        mealPlanReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //showDialog(true);
                    mealPlanList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        MealPlan mealPlan = document.toObject(MealPlan.class);
                        mealPlanList.add(mealPlan);
                    }
                    showDialog(false);
                    callBack.onCallBack(mealPlanList);
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                    showDialog(false);
                }
            }
        });
    }

    /**
     *
     * @param ingredientList
     */
    @Override
    public void onAddIngredientOkPressed(ArrayList<Ingredient> ingredientList) {
        // Do nothing
    }

    /**
     *
     * @param oldMealPlan
     * @param scaledMealPlan
     */
    @Override
    public void onScaleOkPressed(MealPlan oldMealPlan, MealPlan scaledMealPlan) {
        mealPlanDB.editMealPlan(oldMealPlan, scaledMealPlan);
        recipeAdapter.notifyDataSetChanged();
        ingredientAdapter.notifyDataSetChanged();
    }

    /**
     * Interface
     * Call back MealPlanList
     * Basically allows us to access the MealPlanList outside of the onComplete and it ensures that the onComplete has fully populated our list
     */
    private interface MealPlanFireStoreCallBack {
        void onCallBack(ArrayList<MealPlan> mealPlanList);
    }

    /**
     *
     * @param isShown
     */
    private void showDialog(boolean isShown){
        if (isShown) {
            progressBar.setCancelable(false);
            progressBar.setCanceledOnTouchOutside(false);
            progressBar.show();
        } else {
            progressBar.setCancelable(true);
            progressBar.setCanceledOnTouchOutside(true);
            progressBar.dismiss();
        }
    }

    /**
     *
     * @return
     */
    public String getGlobalDate() {
        return globalDate;
    }


}
