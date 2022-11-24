package com.example.bobstomatoes;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class MealPlanCalendarAdapter extends RecyclerView.Adapter<MealPlanCalendarAdapter.MealPlanCalendarViewHolder>
{
    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;


    public MealPlanCalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener)
    {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public MealPlanCalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.158);
        return new MealPlanCalendarViewHolder(view, onItemListener);
    }



    @Override
    public int getItemCount()
    {
        return daysOfMonth.size();
    }


    public interface  OnItemListener
    {
        void onItemClick(int position, String dayText, TextView day);


    }


    @Override
    public void onBindViewHolder(@NonNull MealPlanCalendarViewHolder holder, @SuppressLint("RecyclerView") int position)
    {

        holder.dayOfMonth.setText(daysOfMonth.get(position));

    }


    public class MealPlanCalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public final TextView dayOfMonth;
        public Boolean hasMealPlan = false;
        private MealPlanCalendarAdapter.OnItemListener onItemListener;
        public MealPlanCalendarViewHolder(@NonNull View itemView, MealPlanCalendarAdapter.OnItemListener onItemListener) {
            super(itemView);
            dayOfMonth = itemView.findViewById(R.id.cellDayText);
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            onItemListener.onItemClick(getAbsoluteAdapterPosition(), (String) dayOfMonth.getText(), dayOfMonth);




        }
    }
}
