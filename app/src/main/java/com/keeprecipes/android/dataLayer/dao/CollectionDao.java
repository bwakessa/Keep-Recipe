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

import com.keeprecipes.android.dataLayer.entities.Categories;

import java.util.List;

@Dao
public interface CollectionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Categories categories);

    @Delete
    void delete(Categories categories);

    @Update
    void updateCollection(Categories categories);

    @Query("SELECT * FROM Categories ORDER BY categoriesId DESC")
    LiveData<List<Categories>> getAll();

    @Query("SELECT categoriesId FROM Categories WHERE name = :collectionName")
    long fetchIdByName(String collectionName);

    @Query("SELECT * FROM Categories WHERE name = :collectionName")
    LiveData<Categories> fetchByName(String collectionName);

    @Query("SELECT EXISTS(SELECT * FROM Categories WHERE name = :name)")
    Boolean isRowExist(String name);

    @Query("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'categories'")
    void clearPrimaryKey();

    // Resets the the primary key after deleting entity
    @RawQuery
    int vacuumDb(SupportSQLiteQuery supportSQLiteQuery);
}
