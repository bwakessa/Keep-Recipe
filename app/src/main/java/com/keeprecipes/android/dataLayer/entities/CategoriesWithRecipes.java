package com.keeprecipes.android.dataLayer.entities;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class CategoriesWithRecipes {
    @Embedded
    public Category categories;
    @Relation(parentColumn = "categoriesId", entityColumn = "recipeId", associateBy = @Junction(CategoriesRecipeCrossRef.class))
    public List<Recipe> recipes;
}

