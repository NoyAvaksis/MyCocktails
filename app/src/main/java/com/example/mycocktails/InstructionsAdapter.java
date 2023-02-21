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
public class InstructionsAdapter extends RecyclerView.Adapter<InstructionsAdapter.MiniPostHolder> implements RecyclerView.OnItemTouchListener{


    public ArrayList<String> mStrList;

    private final Context context;

    public InstructionsAdapter(Context context, ArrayList<String> mList) {
        this.context = context;
        this.mStrList = mList;
    }

    @NonNull
    @Override
    public InstructionsAdapter.MiniPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.instruction_item, parent, false);
        return new InstructionsAdapter.MiniPostHolder(v);
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull InstructionsAdapter.MiniPostHolder holder,@SuppressLint("RecyclerView") int position) {
        holder.TXT_InstructionsItem.setText(String.valueOf(mStrList.get(position).toString()));
    }


    @Override
    public int getItemCount() {
        return mStrList.size();
    }

    public static class MiniPostHolder extends RecyclerView.ViewHolder {
        TextView TXT_InstructionsItem;

        public MiniPostHolder(@NonNull View itemView) {
            super(itemView);
            TXT_InstructionsItem = itemView.findViewById(R.id.TXT_InstructionsItem);
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
