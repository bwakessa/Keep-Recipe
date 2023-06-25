package com.keeprecipes.android.dataLayer.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.keeprecipes.android.dataLayer.entities.RecipeWithCategories;

import java.util.List;

@Dao
public interface RecipeWithCollectionsDao {
    @Transaction
    @Query("SELECT * FROM recipes WHERE recipeId LIKE :id")
    LiveData<List<RecipeWithCategories>> getRecipeWithCategories(long id);
}
