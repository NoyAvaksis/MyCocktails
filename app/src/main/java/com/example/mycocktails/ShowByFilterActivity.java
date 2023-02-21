package com.example.mycocktails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ShowByFilterActivity extends AppCompatActivity {

    ArrayList<String> listFilter;
    ArrayList<Cocktail> listCocktail;
    RecyclerView rv;
    AllClassicAdapter allClassicAdapter;
    TextView tvMessage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_by_filter);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        listFilter = (ArrayList<String>) args.getSerializable("ARRAYLIST_FILTER");


        tvMessage = findViewById(R.id.tvMessage);
        listCocktail = new ArrayList<>();

        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(false);
        rv.setLayoutManager(new LinearLayoutManager(this));

        showCocktail();

    }

    public void showCocktail() {
        listCocktail = new ArrayList<>();
        allClassicAdapter = new AllClassicAdapter(this, listCocktail);
        rv.setAdapter(allClassicAdapter);

        setList();
    }

    public boolean isGoodCocktailByFilter(ArrayList<Ingredients> ingredientsList) {
        for (int i = 0; i < ingredientsList.size(); i++) {
            if(!containIn(ingredientsList.get(i).name)) return false;
        }
        return true;
    }

    public boolean containIn(String ingredientName) {
        for (int i = 0; i < listFilter.size(); i++) {
            if (ingredientName.equals(listFilter.get(i))) return true;
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
                if(listCocktail.size() ==0) {
                    tvMessage.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.GONE);
                }
                allClassicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}