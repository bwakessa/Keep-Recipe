package com.keeprecipes.android.dataLayer.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.keeprecipes.android.dataLayer.entities.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {
    @Insert
    long insert(Recipe recipe);

    @Delete
    void delete(Recipe recipe);

    @Query("SELECT * FROM recipes WHERE recipeId LIKE :recipeId")
    LiveData<Recipe> fetchById(int recipeId);

    @Query("SELECT * FROM recipes WHERE title LIKE :recipeName")
    LiveData<Recipe> fetchByTitle(String recipeName);

    @Query("SELECT * FROM recipes ORDER BY recipeId DESC")
    LiveData<List<Recipe>> getAll();

    @Query("SELECT DISTINCT cuisine FROM recipes WHERE cuisine IS NOT NULL")
    LiveData<List<String>> getAllCuisine();

    // TODO add ignore unused fields collection, portion-size, photos, data_created
    @Transaction
    @Query(
            "SELECT recipes.recipeId, recipes.title, recipes.instructions, recipes.ingredients FROM recipes "
                    + "JOIN recipeFts ON (recipes.recipeId = recipeFts.docid) WHERE recipeFts MATCH :query")
    LiveData<List<Recipe>> searchRecipe(String query);

    @Update
    int updateRecipe(Recipe recipe);

    @Query("DELETE FROM recipes")
    void drop();

    // Resets the the primary key after deleting entity
    // https://www.sqlite.org/lang_vacuum.html
    @RawQuery
    int vacuumDb(SupportSQLiteQuery supportSQLiteQuery);
}
