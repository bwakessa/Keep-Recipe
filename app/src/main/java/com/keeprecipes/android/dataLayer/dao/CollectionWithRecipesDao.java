package com.keeprecipes.android.dataLayer.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.keeprecipes.android.dataLayer.entities.CollectionWithRecipes;

import java.util.List;

@Dao
public interface CollectionWithRecipesDao {
    @Transaction
    @Query("SELECT * FROM collections")
    public LiveData<List<CollectionWithRecipes>> getCollectionWithRecipes();
}
