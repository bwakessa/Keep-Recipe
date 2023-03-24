package com.keeprecipes.android.dataLayer.entities;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class RecipeWithCollections {
    @Embedded
    public Recipe recipe;
    @Relation(parentColumn = "recipeId", entityColumn = "collectionId", associateBy = @Junction(CollectionRecipeCrossRef.class))
    public List<Collection> collections;
}
