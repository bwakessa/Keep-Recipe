package com.keeprecipes.android.dataLayer.entities;

public class Ingredient {
    public String name;
    public int size;
    public String quantity;

    public Ingredient(String name, int size, String quantity) {
        this.name = name;
        this.size = size;
        this.quantity = quantity;
    }
}
