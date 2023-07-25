package com.keeprecipes.android.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.keeprecipes.android.data.entities.Category;

import java.util.List;

@Dao
public interface CollectionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Category categories);

    @Delete
    void delete(Category categories);

    @Query("DELETE FROM Category WHERE categoriesId = :categoriesId")
    void deleteById(long categoriesId);

    @Update
    void updateCollection(Category categories);

    @Query("SELECT * FROM Category ORDER BY categoriesId DESC")
    LiveData<List<Category>> getAll();

    @Query("SELECT categoriesId FROM Category WHERE name = :collectionName")
    long fetchIdByName(String collectionName);

    @Query("SELECT * FROM Category WHERE name = :collectionName")
    LiveData<Category> fetchByName(String collectionName);

    @Query("SELECT EXISTS(SELECT * FROM Category WHERE name = :name)")
    Boolean isRowExist(String name);

    @Query("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'categories'")
    void clearPrimaryKey();

    // Resets the the primary key after deleting entity
    @RawQuery
    int vacuumDb(SupportSQLiteQuery supportSQLiteQuery);
}
