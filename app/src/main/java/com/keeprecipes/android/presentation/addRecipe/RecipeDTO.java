package com.keeprecipes.android.presentation.addRecipe;

import androidx.annotation.NonNull;

import com.keeprecipes.android.data.entities.Recipe;

import java.util.List;

public class RecipeDTO {
    public long id;
    public String title;
    public String instructions;
    public String cuisine;
    public String categories;
    public String portionSize;
    public List<String> photoURI;

    public RecipeDTO() {
    }

    public RecipeDTO(Recipe recipe) {
        this.id = recipe.recipeId;
        this.title = recipe.title;
        this.instructions = recipe.instructions;
        this.portionSize = recipe.portionSize;
        this.photoURI = recipe.photos;
    }

    @NonNull
    @Override
    public String toString() {
        return "RecipeDTO{" +
                "title='" + title + '\'' +
                ", instructions=" + instructions +
                ", categories='" + categories + '\'' +
                ", portionSize=" + portionSize +
                ", photoURI=" + photoURI +
                '}';
    }
}
