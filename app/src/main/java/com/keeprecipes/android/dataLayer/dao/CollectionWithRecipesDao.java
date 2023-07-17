package com.keeprecipes.android.dataLayer.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.keeprecipes.android.dataLayer.entities.CategoriesRecipeCrossRef;
import com.keeprecipes.android.dataLayer.entities.CategoriesWithRecipes;
import com.keeprecipes.android.dataLayer.entities.Recipe;

import java.util.List;

@Dao
public interface CollectionWithRecipesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CategoriesRecipeCrossRef collectionWithRecipes);

    @Transaction
    @Query("SELECT * FROM Category")
    LiveData<List<CategoriesWithRecipes>> getCollectionWithRecipes();

    @Transaction
    @Query("SELECT * FROM recipes LEFT JOIN CategoriesRecipeCrossRef WHERE recipes.recipeId = CategoriesRecipeCrossRef.recipeId AND categoriesId = :collectionId")
    LiveData<List<Recipe>> getCategoriesWithRecipesById(long collectionId);

    @Transaction
    @Query("""
            SELECT categoriesId
            FROM CategoriesRecipeCrossRef\s
            GROUP BY categoriesId
            HAVING COUNT(categoriesId) == 1
            AND recipeId == :recipeId;""")
    List<Long> getUniqueCategories(long recipeId);

    @Delete
    void delete(CategoriesRecipeCrossRef collectionWithRecipes);

    @Query("DELETE FROM CategoriesRecipeCrossRef WHERE recipeId = :recipeId;")
    void deleteByRecipe(long recipeId);

    @Query("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'CategoriesRecipeCrossRef'")
    void clearPrimaryKey();
}
