package com.example.mycocktails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AllClassicActivity extends AppCompatActivity {

    ArrayList<Cocktail> list;
    RecyclerView rv;
    AllClassicAdapter allClassicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_classic);


        list = new ArrayList<>();

        rv = findViewById(R.id.cocktail_RV);
        rv.setHasFixedSize(false);
        rv.setLayoutManager(new LinearLayoutManager(this));

        showCocktail();
    }

    public void showCocktail() {
        list = new ArrayList<>();
        allClassicAdapter = new AllClassicAdapter(this, list);
        rv.setAdapter(allClassicAdapter);

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
                allClassicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}