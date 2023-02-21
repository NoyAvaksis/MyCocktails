package com.example.mycocktails;

import java.util.ArrayList;
import java.util.HashMap;

public class Cocktail {
    public String key;
    public String name;
    public ArrayList<Ingredients> ingredients;
    public ArrayList<String> uidLikes;
    public ArrayList<String> recipes;
    public String url;

    public Cocktail() {

    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("key", key);
        map.put("name", name);
        map.put("url", url);

        HashMap<String, Object> mapIngredients = new HashMap<>();
        for (int i = 0; i < ingredients.size(); i++)
            mapIngredients.put("in" + i, ingredients.get(i).toMap());
        map.put("ingredients", mapIngredients);

        HashMap<String, Object> mapUidLikes = new HashMap<>();
        for (int i = 0; i < uidLikes.size(); i++)
            mapUidLikes.put("uid" + i, uidLikes.get(i));
        map.put("uidLikes", mapUidLikes);

        HashMap<String, Object> mapRecipes = new HashMap<>();
        for (int i = 0; i < recipes.size(); i++)
            mapRecipes.put("rp" + i, recipes.get(i));
        map.put("recipes", mapRecipes);

        return map;
    }

    public Cocktail(HashMap<String, Object> map) {
        key = map.get("key").toString();
        name = map.get("name").toString();
        url = map.get("url").toString();

        int i = 0;
        HashMap<String, Object> mapIngredients = (HashMap<String, Object>) map.get("ingredients");
        ingredients = new ArrayList<>();
        while (mapIngredients.get("in"+i) !=null) {
            ingredients.add(new Ingredients((HashMap<String, Object>) mapIngredients.get("in"+i)));
            i++;
        }

        i = 0;
        HashMap<String, Object> mapUidLikes = (HashMap<String, Object>) map.get("uidLikes");
        uidLikes = new ArrayList<>();
        while (mapUidLikes.get("uid"+i) !=null) {
            uidLikes.add(mapUidLikes.get("uid"+i).toString());
            i++;
        }

        i = 0;
        HashMap<String, Object> mapRecipes = (HashMap<String, Object>) map.get("recipes");
        recipes = new ArrayList<>();
        while (mapRecipes.get("rp"+i) !=null) {
            recipes.add(mapRecipes.get("rp"+i).toString());
            i++;
        }
    }
}
