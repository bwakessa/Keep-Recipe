package com.keeprecipes.android.dataLayer.entities;

import androidx.room.Entity;

@Entity(primaryKeys = {"collectionId", "recipeId"})
public class CollectionRecipeCrossRef {
    public long collectionId;
    public long recipeId;

    public CollectionRecipeCrossRef(long collectionId, long recipeId) {
        this.collectionId = collectionId;
        this.recipeId = recipeId;
    }
}
