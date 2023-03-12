package com.keeprecipes.android.dataLayer.entities;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class CollectionWithRecipes {
    @Embedded
    public Collection collection;
    @Relation(parentColumn = "collectionId", entityColumn = "recipeId", associateBy = @Junction(CollectionRecipeCrossRef.class))
    public List<Recipe> recipes;
}
