package com.example.mycocktails;

import static java.util.Objects.requireNonNull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    androidx.cardview.widget.CardView cvAllClassic,cvFind, cvRandom;
    ImageView menu_BTN_quickFindVodka,menu_BTN_quickFindTequila,menu_BTN_quickFindRum,menu_BTN_quickFindWhisky;

    RecyclerView top_rated_list;
    ArrayList<Cocktail> list;
    TopRatedAdapter topRatedAdapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button logoutBtn = findViewById(R.id.logoutBtn);
        cvAllClassic = findViewById(R.id.cvAllClassic);
        cvAllClassic.setOnClickListener(this);
        cvFind = findViewById(R.id.cvFind);
        cvFind.setOnClickListener(this);
        cvRandom = findViewById(R.id.cvRandom);
        cvRandom.setOnClickListener(this);

        menu_BTN_quickFindVodka = findViewById(R.id.menu_BTN_quickFindVodka);
        menu_BTN_quickFindVodka.setOnClickListener(this);
        menu_BTN_quickFindTequila = findViewById(R.id.menu_BTN_quickFindTequila);
        menu_BTN_quickFindTequila.setOnClickListener(this);
        menu_BTN_quickFindRum = findViewById(R.id.menu_BTN_quickFindRum);
        menu_BTN_quickFindRum.setOnClickListener(this);
        menu_BTN_quickFindWhisky = findViewById(R.id.menu_BTN_quickFindWhisky);
        menu_BTN_quickFindWhisky.setOnClickListener(this);

        top_rated_list = findViewById(R.id.top_rated_list);
        top_rated_list.setHasFixedSize(false);
        top_rated_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        showTopRatedCocktail();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AuthUI.getInstance()
                        .signOut(MenuActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {

                                Toast.makeText(MenuActivity.this, "User Signed Out", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(MenuActivity.this, LoginActivity.class);
                                startActivity(i);
                            }
                        });
            }
        });
    }

    public void showTopRatedCocktail() {
        list = new ArrayList<>();
        topRatedAdapter = new TopRatedAdapter(this, list);
        top_rated_list.setAdapter(topRatedAdapter);

        setList();
    }

    public void setList() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference("Cocktails");
        root.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Cocktail model = new Cocktail((HashMap<String, Object>) (Objects.requireNonNull(dataSnapshot.getValue())));
                    list.add(model);
                }
                if(list.size() > 5) {
                    while (list.size() > 5) {
                        list.remove(list.size()-1);
                    }
                }
                Collections.sort(list, (d1, d2) -> d2.uidLikes.size() - d1.uidLikes.size());

                topRatedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvAllClassic:
                startActivity(new Intent(this,AllClassicActivity.class));
                break;

            case R.id.cvFind:
                startActivity(new Intent(this,MyBarActivity.class));
                break;

            case R.id.cvRandom:
                getRandom();
                break;

            case R.id.menu_BTN_quickFindVodka:
                passFilter("Vodka");
                break;
            case R.id.menu_BTN_quickFindTequila:
                passFilter("Tequila");
                break;
             case R.id.menu_BTN_quickFindRum:
                passFilter("Rum");
                break;
             case R.id.menu_BTN_quickFindWhisky:
                passFilter("Gin");
                break;

        }
    }

    public void passFilter(String name) {
            Intent intent = new Intent(MenuActivity.this, QuickFindsActivity.class);
            Bundle args = new Bundle();
            args.putSerializable("FILTER", name);
            intent.putExtra("BUNDLE", args);
            startActivity(intent);
    }


    public void showCocktails(Cocktail cocktail) {
        Dialog d = new Dialog(this);
        d.setContentView(R.layout.cocktail_page);

        d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        TextView tvName = d.findViewById(R.id.tvName);
        tvName.setText(String.valueOf(cocktail.name));
        ImageView ivImage = d.findViewById(R.id.ivImage);
        Picasso.get().load(cocktail.url).into(ivImage);

        RecyclerView cocktailPage_RC_Ingredients = d.findViewById(R.id.cocktailPage_RC_Ingredients);
        cocktailPage_RC_Ingredients.setHasFixedSize(false);
        cocktailPage_RC_Ingredients.setLayoutManager(new LinearLayoutManager(this));

        IngredientsAdapter IngredientsAdapter = new IngredientsAdapter(this, cocktail.ingredients);
        cocktailPage_RC_Ingredients.setAdapter(IngredientsAdapter);
        IngredientsAdapter.notifyDataSetChanged();


        RecyclerView cocktailPage_RC_Instructions = d.findViewById(R.id.cocktailPage_RC_Instructions);
        cocktailPage_RC_Instructions.setHasFixedSize(false);
        cocktailPage_RC_Instructions.setLayoutManager(new LinearLayoutManager(this));

        InstructionsAdapter instructionsAdapter = new InstructionsAdapter(this, cocktail.recipes);
        cocktailPage_RC_Instructions.setAdapter(instructionsAdapter);
        instructionsAdapter.notifyDataSetChanged();


        ImageButton ibLike = d.findViewById(R.id.ibLike);
        if(isUserLikedIt(cocktail)) ibLike.setBackground(getDrawable(R.drawable.heart_full));
        else  ibLike.setBackground(getDrawable(R.drawable.heart));

        ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isUserLikedIt(cocktail)) {
                    ibLike.setBackground(getDrawable(R.drawable.heart));
                    setCocktailLike(cocktail,false);
                } else {
                    ibLike.setBackground(getDrawable(R.drawable.heart_full));
                    setCocktailLike(cocktail,true);
                }
            }
        });
        //   TextView TXT_InstructionsItem = d.findViewById(R.id.TXT_InstructionsItem);
        // todo TXT_InstructionsItem.setText(String.valueOf());
        d.show();
    }

    public boolean isUserLikedIt(Cocktail cocktail) {
        ArrayList<String> uidLikes = new ArrayList<>(cocktail.uidLikes);
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        for(int i=0; i<uidLikes.size(); i++) {
            if(currentUid.equals(uidLikes.get(i))) return true;
        }

        return false;
    }
    public void setCocktailLike(Cocktail cocktail, boolean isLiked) {
        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("/Cocktails/" + cocktail.key);
        if (isLiked) {
            cocktail.uidLikes.add(requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        } else {
            cocktail = removeUidWhere(cocktail);
        }
        postRef.setValue(cocktail.toMap());
    }

    public Cocktail removeUidWhere(Cocktail cocktail) {
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        for(int i=0; i<cocktail.uidLikes.size(); i++) {
            if(currentUid.equals(cocktail.uidLikes.get(i))) {
                cocktail.uidLikes.remove(i);
                return cocktail;
            }
        }
        return cocktail;
    }
    public void getRandom() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference("Cocktails");
        ArrayList<Cocktail> list = new ArrayList<>();
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Cocktail model = new Cocktail((HashMap<String, Object>) (Objects.requireNonNull(dataSnapshot.getValue())));
                    list.add(model);
                }

                int num = new Random().nextInt(list.size());
                showCocktails(list.get(num));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}