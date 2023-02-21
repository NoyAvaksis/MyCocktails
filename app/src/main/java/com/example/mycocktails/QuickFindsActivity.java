package com.example.mycocktails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class QuickFindsActivity extends AppCompatActivity {

    String filter;
    ArrayList<Cocktail> listCocktail;
    RecyclerView cocktailPage_RC_Instructions;
    AllClassicAdapter allClassicAdapter;
    TextView tvTitle;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_finds);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        filter = args.getString("FILTER");


        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(filter);
        listCocktail = new ArrayList<>();

        cocktailPage_RC_Instructions = findViewById(R.id.cocktailPage_RC_Instructions);
        cocktailPage_RC_Instructions.setHasFixedSize(false);
        cocktailPage_RC_Instructions.setLayoutManager(new LinearLayoutManager(this));

        showCocktail();

    }

    public void showCocktail() {
        listCocktail = new ArrayList<>();
        allClassicAdapter = new AllClassicAdapter(this, listCocktail);
        cocktailPage_RC_Instructions.setAdapter(allClassicAdapter);

        setList();
    }

    public boolean isGoodCocktailByFilter(ArrayList<Ingredients> list) {
        for(int i=0; i<list.size(); i++) {
            if(list.get(i).name.contains(filter)) return true;
        }
        return false;
    }

    public void setList() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference("Cocktails");
        root.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listCocktail.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Cocktail model = new Cocktail((HashMap<String, Object>) (Objects.requireNonNull(dataSnapshot.getValue())));
                    if(isGoodCocktailByFilter(model.ingredients))
                        listCocktail.add(model);
                }
                allClassicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}