package com.keeprecipes.android.dataLayer.entities;

public class Ingredient {
    public final String name;
    public final int size;
    public final String quantity;

    public Ingredient(String name, int size, String quantity) {
        this.name = name;
        this.size = size;
        this.quantity = quantity;
    }
}
