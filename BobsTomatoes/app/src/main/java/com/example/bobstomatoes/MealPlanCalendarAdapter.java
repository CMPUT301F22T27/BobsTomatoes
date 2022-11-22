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
    int selected_position = -1; // You have to set this globally in the Adapter class


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
        holder.itemView.setBackgroundColor(selected_position == position ? Color.GREEN : Color.TRANSPARENT);


    }




    public class MealPlanCalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public final TextView dayOfMonth;
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
            if (getAbsoluteAdapterPosition() == RecyclerView.NO_POSITION) return;

            // Updating old as well as new positions
            notifyItemChanged(selected_position);
            selected_position = getAbsoluteAdapterPosition();
            notifyItemChanged(selected_position);
            onItemListener.onItemClick(getAbsoluteAdapterPosition(), (String) dayOfMonth.getText(), dayOfMonth);




        }
    }
}
