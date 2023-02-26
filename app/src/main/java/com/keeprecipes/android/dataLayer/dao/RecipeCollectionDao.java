package com.keeprecipes.android.dataLayer.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.keeprecipes.android.dataLayer.entities.RecipeCollection;

import java.util.List;

@Dao
public interface RecipeCollectionDao {
    @Insert
    void insert(RecipeCollection collection);

    @Delete
    void delete(RecipeCollection collection);

    @Update
    void updateCollection(RecipeCollection collection);

    @Query("SELECT * FROM collections ORDER BY id DESC")
    LiveData<List<RecipeCollection>> getAll();

    @Query("SELECT * FROM collections WHERE name = :collectionName")
    LiveData<RecipeCollection> fetchByName(String collectionName);

    // Resets the the primary key after deleting entity
    @RawQuery
    int vacuumDb(SupportSQLiteQuery supportSQLiteQuery);
}
