package com.keeprecipes.android.dataLayer.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.keeprecipes.android.dataLayer.entities.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {
    @Insert
    void insert(Recipe recipe);

    @Delete
    void delete(Recipe recipe);

    @Query("SELECT * FROM recipes WHERE id LIKE :recipeId")
    LiveData<Recipe> fetchById(int recipeId);

    @Query("SELECT * FROM recipes WHERE title LIKE :recipeName")
    LiveData<Recipe> fetchByTitle(String recipeName);

    @Query("SELECT * FROM recipes ORDER BY id DESC")
    LiveData<List<Recipe>> getAll();

    @Query("SELECT DISTINCT cuisine FROM recipes")
    LiveData<List<String>> getAllCusine();

    @Update
    void updateRecipe(Recipe recipe);

    @Query("DELETE FROM recipes")
    void drop();

    // Resets the the primary key after deleting entity
    // https://www.sqlite.org/lang_vacuum.html
    @RawQuery
    int vacuumDb(SupportSQLiteQuery supportSQLiteQuery);
}
