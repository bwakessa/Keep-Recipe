package com.keeprecipes.android.presentationLayer.addRecipe;

import androidx.annotation.NonNull;

import com.keeprecipes.android.dataLayer.entities.Recipe;

import java.util.List;

public class RecipeDTO {
    public int id;
    public String title;
    public String instructions;
    public String cuisine;
    public String collection;
    public String portionSize;
    public List photoURI;

    public RecipeDTO() {
    }

    public RecipeDTO(Recipe recipe) {
        this.id = recipe.recipeId;
        this.title = recipe.title;
        this.instructions = recipe.instructions;
        this.cuisine = recipe.cuisine;
        this.collection = recipe.collection;
        this.portionSize = recipe.portionSize;
        this.photoURI = recipe.photos;
    }

    @NonNull
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
