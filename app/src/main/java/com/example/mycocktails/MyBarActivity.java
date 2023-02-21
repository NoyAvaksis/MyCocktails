package com.example.mycocktails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class MyBarActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView cocktailPage_RC_Ingredients;

    ArrayList<MyBarModel> list;

    Button btnPass;

    MyBarAdapter myBarAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bar);

        list = new ArrayList<>();

        cocktailPage_RC_Ingredients = findViewById(R.id.cocktailPage_RC_Ingredients);
        cocktailPage_RC_Ingredients.setHasFixedSize(false);
        cocktailPage_RC_Ingredients.setLayoutManager(new GridLayoutManager(this,2));

        list.add(new MyBarModel("Agave Nectar",false ));
        list.add(new MyBarModel("Aged Rum",false));
        list.add(new MyBarModel("Angostura Bitters",false));
        list.add(new MyBarModel("Appelton 8",false));
        list.add(new MyBarModel("Citrus Vodka",false));
        list.add(new MyBarModel("Campari",false));
        list.add(new MyBarModel("Coconut Cream",false));
        list.add(new MyBarModel("Coffee Liquor",false));
        list.add(new MyBarModel("Cognac",false));
        list.add(new MyBarModel("Cointreau",false));
        list.add(new MyBarModel("Dark Rum",false));
        list.add(new MyBarModel("Dry Curacao",false));
        list.add(new MyBarModel("Dry Vermouth",false));
        list.add(new MyBarModel("Egg white",false));
        list.add(new MyBarModel("Espresso",false));
        list.add(new MyBarModel("Gin",false));
        list.add(new MyBarModel("Ginger Beer",false));
        list.add(new MyBarModel("Ginger Syrup",false));
        list.add(new MyBarModel("Grenadine",false)); //
        list.add(new MyBarModel("Honey",false));
        list.add(new MyBarModel("Honey Syrup",false));
        list.add(new MyBarModel("Lemon",false));
        list.add(new MyBarModel("Light Rum",false));
        list.add(new MyBarModel("Lilet Blanc",false));
        list.add(new MyBarModel("Lime",false)); //
        list.add(new MyBarModel("Lime Slices",false));
        list.add(new MyBarModel("Luxardo Maraschino",false));
        list.add(new MyBarModel("Mezcal",false));
        list.add(new MyBarModel("Mint Leaves",false));
        list.add(new MyBarModel("Orange Bitters",false));
        list.add(new MyBarModel("Orange Liquor",false)); //
        list.add(new MyBarModel("orange juice",false)); //
        list.add(new MyBarModel("Orgeat",false));
        list.add(new MyBarModel("Pineapple Juice",false));
        list.add(new MyBarModel("Rum",false));
        list.add(new MyBarModel("Simple Syrup",false));
        list.add(new MyBarModel("Sparkling Red Grapefruit",false));
        list.add(new MyBarModel("Sugar",false));
        list.add(new MyBarModel("Sweet Vermouth",false));
        list.add(new MyBarModel("Tequila",false)); //
        list.add(new MyBarModel("Tomato Juice",false));
        list.add(new MyBarModel("Triple Sec",false));
        list.add(new MyBarModel("Vanilla Vodka",false));
        list.add(new MyBarModel("Vodka",false));
        list.add(new MyBarModel("White Rum",false));
        list.add(new MyBarModel("Worcestershire sauce",false));

        myBarAdapter = new MyBarAdapter(this, list);
        cocktailPage_RC_Ingredients.setAdapter(myBarAdapter);
        myBarAdapter.notifyDataSetChanged();

        btnPass = findViewById(R.id.myBar_BTN_pass);
        btnPass.setOnClickListener(this);

        // Save state
        Parcelable recyclerViewState;
        recyclerViewState = cocktailPage_RC_Ingredients.getLayoutManager().onSaveInstanceState();

// Restore state
        cocktailPage_RC_Ingredients.getLayoutManager().onRestoreInstanceState(recyclerViewState);
    }

    public void passFilter() {
        if(myBarAdapter.ingredientsList.size() > 0) {
            ArrayList<String> object = myBarAdapter.ingredientsList;
            Intent intent = new Intent(MyBarActivity.this, ShowByFilterActivity.class);
            Bundle args = new Bundle();
            args.putSerializable("ARRAYLIST_FILTER", (Serializable) object);
            intent.putExtra("BUNDLE", args);
            startActivity(intent);
        } else {
            Toast.makeText(this,"chose",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnPass.getId()) {
            passFilter();
        }
    }
}