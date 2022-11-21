package com.example.bobstomatoes;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class MealPlanCalendarAdapter extends RecyclerView.Adapter<MealPlanCalendarViewHolder>
{
    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;
    public int selectedPosition = -1;


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
        layoutParams.height = (int) (parent.getHeight() * 0.1);
        return new MealPlanCalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MealPlanCalendarViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        holder.dayOfMonth.setText(daysOfMonth.get(position));
        if(selectedPosition==position)
            holder.itemView.setBackgroundColor(Color.parseColor("#F8C8DC"));
        else
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition=position;
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount()
    {
        return daysOfMonth.size();
    }

    public interface  OnItemListener
    {
        void onItemClick(int position, String dayText);
    }
}
