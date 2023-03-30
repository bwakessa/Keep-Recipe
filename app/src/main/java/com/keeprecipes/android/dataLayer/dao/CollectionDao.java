package com.keeprecipes.android.dataLayer.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.keeprecipes.android.dataLayer.entities.Collection;

import java.util.List;

@Dao
public interface CollectionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Collection collection);

    @Delete
    void delete(Collection collection);

    @Update
    void updateCollection(Collection collection);

    @Query("SELECT * FROM collections ORDER BY collectionId DESC")
    LiveData<List<Collection>> getAll();

    @Query("SELECT collectionId FROM collections WHERE name = :collectionName")
    long fetchIdByName(String collectionName);

    @Query("SELECT * FROM collections WHERE name = :collectionName")
    LiveData<Collection> fetchByName(String collectionName);

    @Query("SELECT EXISTS(SELECT * FROM collections WHERE name = :name)")
    Boolean isRowExist(String name);

    @Query("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'collections'")
    void clearPrimaryKey();

    // Resets the the primary key after deleting entity
    @RawQuery
    int vacuumDb(SupportSQLiteQuery supportSQLiteQuery);
}
