package com.example.mycocktails;

import static java.util.Objects.requireNonNull;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TopRatedAdapter extends RecyclerView.Adapter<TopRatedAdapter.MiniPostHolder> implements RecyclerView.OnItemTouchListener {

    public ArrayList<Cocktail> mList;
    private final Context context;

    public TopRatedAdapter(Context context, ArrayList<Cocktail> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MiniPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.top_rated_item, parent, false);
        return new MiniPostHolder(v);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull MiniPostHolder holder, @SuppressLint("RecyclerView") int position) {
        Picasso.get().load(mList.get(position).url).into(holder.ivImage);
        holder.tvName.setText(mList.get(position).name);

        holder.lBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCocktails(position);
            }
        });
    }

    public void showCocktails(int index) {
        Dialog d = new Dialog(context);
        d.setContentView(R.layout.cocktail_page);

        d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        TextView tvName = d.findViewById(R.id.tvName);
        tvName.setText(String.valueOf(mList.get(index).name));
        ImageView ivImage = d.findViewById(R.id.ivImage);
        Picasso.get().load(mList.get(index).url).into(ivImage);

        RecyclerView cocktailPage_RC_Ingredients = d.findViewById(R.id.cocktailPage_RC_Ingredients);
        cocktailPage_RC_Ingredients.setHasFixedSize(false);
        cocktailPage_RC_Ingredients.setLayoutManager(new LinearLayoutManager(context));

        IngredientsAdapter IngredientsAdapter = new IngredientsAdapter(context, mList.get(index).ingredients);
        cocktailPage_RC_Ingredients.setAdapter(IngredientsAdapter);
        IngredientsAdapter.notifyDataSetChanged();


        RecyclerView cocktailPage_RC_Instructions = d.findViewById(R.id.cocktailPage_RC_Instructions);
        cocktailPage_RC_Instructions.setHasFixedSize(false);
        cocktailPage_RC_Instructions.setLayoutManager(new LinearLayoutManager(context));

        InstructionsAdapter instructionsAdapter = new InstructionsAdapter(context, mList.get(index).recipes);
        cocktailPage_RC_Instructions.setAdapter(instructionsAdapter);
        instructionsAdapter.notifyDataSetChanged();


        ImageButton ibLike = d.findViewById(R.id.ibLike);
        if(isUserLikedIt(index)) ibLike.setBackground(context.getDrawable(R.drawable.heart_full));
        else  ibLike.setBackground(context.getDrawable(R.drawable.heart));

        ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isUserLikedIt(index)) {
                    ibLike.setBackground(context.getDrawable(R.drawable.heart));
                    setCocktailLike(index,false);
                } else {
                    ibLike.setBackground(context.getDrawable(R.drawable.heart_full));
                    setCocktailLike(index,true);
                }
            }
        });

        d.show();
    }

    public boolean isUserLikedIt(int position) {
        ArrayList<String> uidLikes = new ArrayList<>(mList.get(position).uidLikes);
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        for(int i=0; i<uidLikes.size(); i++) {
            if(currentUid.equals(uidLikes.get(i))) return true;
        }

        return false;
    }
    public void setCocktailLike(int position, boolean isLiked) {
        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("/Cocktails/" + mList.get(position).key);
        if (isLiked) {
            mList.get(position).uidLikes.add(requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        } else {
            removeUidWhere(position);
        }
        postRef.setValue(mList.get(position).toMap());
    }

    public void removeUidWhere(int position) {
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        for(int i=0; i<mList.get(position).uidLikes.size(); i++) {
            if(currentUid.equals(mList.get(position).uidLikes.get(i))) {
                mList.get(position).uidLikes.remove(i);
                return;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MiniPostHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivImage;
        LinearLayout lBackground;

        public MiniPostHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            lBackground = itemView.findViewById(R.id.lBackground);
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
