package com.keeprecipes.android.dataLayer.entities;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class RecipeWithCategories {
    @Embedded
    public Recipe recipe;
    @Relation(parentColumn = "recipeId", entityColumn = "categoriesId", associateBy = @Junction(CategoriesRecipeCrossRef.class))
    public List<Category> categories;
}
