package com.example.bobstomatoes;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Custom recycler view adapter
 * Intended for use with the MealPlanActivity calendar
 */
class MealPlanCalendarAdapter extends RecyclerView.Adapter<MealPlanCalendarAdapter.MealPlanCalendarViewHolder>
{
    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;


    /**
     * Create new adapter from list of days in the month
     * @param daysOfMonth
     * @param onItemListener
     */
    public MealPlanCalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener)
    {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
    }

    /**
     * Inflate ViewHolder for calendar cell
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public MealPlanCalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.16);
        return new MealPlanCalendarViewHolder(view, onItemListener);
    }

    /**
     * Returns number of items in adapter
     * @return
     */
    @Override
    public int getItemCount()
    {
        return daysOfMonth.size();
    }


    /**
     * Interface to be implemented by class using adapter
     */
    public interface OnItemListener
    {
        void onItemClick(int position, String dayText, TextView day);
    }


    /**
     * Populate view holder with calendar cell day
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MealPlanCalendarViewHolder holder, @SuppressLint("RecyclerView") int position)
    {

        holder.dayOfMonth.setText(daysOfMonth.get(position));

    }


    /**
     * Declare custom viewholder to represent a calendar cell
     */
    public class MealPlanCalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public final TextView dayOfMonth;
        public Boolean hasMealPlan = false;
        private MealPlanCalendarAdapter.OnItemListener onItemListener;

        /**
         * MealPlanCalendarViewHolder constructor
         * @param itemView
         * @param onItemListener
         */
        public MealPlanCalendarViewHolder(@NonNull View itemView, MealPlanCalendarAdapter.OnItemListener onItemListener) {
            super(itemView);
            dayOfMonth = itemView.findViewById(R.id.cellDayText);
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        /**
         * onClick method which occurs when calendar cell is clicked
         * @param view
         */
        @Override
        public void onClick(View view)
        {
            onItemListener.onItemClick(getAbsoluteAdapterPosition(), (String) dayOfMonth.getText(), dayOfMonth);

        }
    }
}
