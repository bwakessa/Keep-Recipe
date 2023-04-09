package com.keeprecipes.android.dataLayer.dao;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.keeprecipes.android.dataLayer.entities.CollectionRecipeCrossRef;
import com.keeprecipes.android.dataLayer.entities.CollectionWithRecipes;

import java.util.List;

@Dao
public interface CollectionWithRecipesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CollectionRecipeCrossRef collectionWithRecipes);

    @Transaction
    @Query("SELECT * FROM collections")
    LiveData<List<CollectionWithRecipes>> getCollectionWithRecipes();

    @Transaction
    @Query("SELECT * FROM collectionrecipecrossref WHERE collectionId = :collectionId")
    LiveData<List<CollectionWithRecipes>> getCollectionWithRecipesById(long collectionId);

    @Delete
    void delete(CollectionRecipeCrossRef collectionWithRecipes);

    @Query("DELETE FROM CollectionRecipeCrossRef WHERE recipeId = :recipeId;")
    void deleteByRecipe(long recipeId);

    @Query("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'CollectionRecipeCrossRef'")
    void clearPrimaryKey();
}
