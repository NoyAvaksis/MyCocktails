package com.example.mycocktails;

import java.util.HashMap;

public class Ingredients {
    public String name;
    public int count;

    public Ingredients(String name, int count){
        this.name = name;
        this.count = count;
    }

    public Ingredients(HashMap<String, Object> map ) {
        this.name = String.valueOf(map.get("name"));
        this.count = Integer.parseInt(String.valueOf(map.get("count")));
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name",name);
        map.put("count",count);
        return map;
    }
}
