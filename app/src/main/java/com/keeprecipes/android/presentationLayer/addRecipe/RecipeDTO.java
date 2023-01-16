package com.keeprecipes.android.presentationLayer.addRecipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeDTO {
    public String title;
    public String instructions;
    public String cuisine;
    public String collection;
    public String portionSize;
    public List photoURI;

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
