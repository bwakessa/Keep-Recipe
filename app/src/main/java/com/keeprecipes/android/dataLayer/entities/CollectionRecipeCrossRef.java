package com.keeprecipes.android.dataLayer.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"recipeId", "collectionId"})
public class CollectionRecipeCrossRef {
    @NonNull
    public int recipeId;
    @NonNull
    @ColumnInfo(index = true)
    public int collectionId;
}
