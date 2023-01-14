package com.keeprecipes.android.presentationLayer.addRecipe;

import java.util.ArrayList;

public class RecipeDTO {
    public String title;
    public String instructions;
    public String cuisine;
    public String collection;
    public String portionSize;
    public ArrayList<String> photoURI;

    @Override
    public String toString() {
        return "RecipeDTO{" +
                "title='" + title + '\'' +
                ", instructions=" + instructions +
                ", cuisine='" + cuisine + '\'' +
                ", collection='" + collection + '\'' +
                ", portionSize=" + portionSize +
                ", photoURI=" + photoURI +
                '}';
    }
}
