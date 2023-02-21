package com.example.mycocktails;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.MiniPostHolder> implements RecyclerView.OnItemTouchListener {

    public ArrayList<Ingredients> mList;
    private final Context context;

    public IngredientsAdapter(Context context, ArrayList<Ingredients> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MiniPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.ingredients_item, parent, false);
        return new MiniPostHolder(v);
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull MiniPostHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.TXT_IngredientsItemName.setText(String.valueOf(mList.get(position).name));
        holder.TXT_IngredientsItemCount.setText(String.valueOf(mList.get(position).count));


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MiniPostHolder extends RecyclerView.ViewHolder {
        TextView TXT_IngredientsItemCount, TXT_IngredientsItemName;

        public MiniPostHolder(@NonNull View itemView) {
            super(itemView);
            TXT_IngredientsItemCount = itemView.findViewById(R.id.TXT_IngredientsItemCount);
            TXT_IngredientsItemName = itemView.findViewById(R.id.TXT_IngredientsItemName);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

}
