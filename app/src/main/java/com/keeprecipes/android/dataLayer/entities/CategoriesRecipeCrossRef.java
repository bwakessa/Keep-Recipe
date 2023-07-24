package com.keeprecipes.android.dataLayer.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"categoriesId", "recipeId"})
public class CategoriesRecipeCrossRef {
    public long categoriesId;
    @ColumnInfo(index = true)
    public long recipeId;

    public CategoriesRecipeCrossRef(long categoriesId, long recipeId) {
        this.categoriesId = categoriesId;
        this.recipeId = recipeId;
    }
}
