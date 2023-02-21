package com.example.mycocktails;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcelable;
import android.view.CollapsibleActionView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class MyBarAdapter extends RecyclerView.Adapter<MyBarAdapter.MiniPostHolder> implements RecyclerView.OnItemTouchListener {

    public ArrayList<MyBarModel> mList;
    private final Context context;
    ArrayList<String> ingredientsList;
    final boolean[] check;


    public MyBarAdapter(Context context, ArrayList<MyBarModel> mList) {
        this.context = context;
        this.mList = mList;
        this.ingredientsList = new ArrayList<>();
        check = new boolean[mList.size()];

        for(int i=0; i<check.length; i++)
            check[i] = false;

    }

    @NonNull
    @Override
    public MiniPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.spirit_item, parent, false);
        return new MiniPostHolder(v);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull MiniPostHolder holder, @SuppressLint("RecyclerView") int position) {

        MyBarModel myBarModel = mList.get(position);
        holder.ingredientsCheckBox.setText(String.valueOf(myBarModel.name));

        holder.ingredientsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    ingredientsList.add(myBarModel.name);

                }
                else {
                    removeItemByName(myBarModel.name);

                }
                mList.get(position).checked = isChecked;
            }

        });

        holder.ingredientsCheckBox.setChecked(myBarModel.checked);

    }

    public void removeItemByName(String str) {
        for(int i=0; i<ingredientsList.size(); i++) {
            if(ingredientsList.get(i).equals(str)) {
                ingredientsList.remove(i);
                return;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MiniPostHolder extends RecyclerView.ViewHolder {

        CheckBox ingredientsCheckBox;
        public MiniPostHolder(@NonNull View itemView) {
            super(itemView);
            ingredientsCheckBox = itemView.findViewById(R.id.ingredientsCheckBox);
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