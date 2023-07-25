package com.keeprecipes.android.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.keeprecipes.android.data.dao.CollectionWithRecipesDao;
import com.keeprecipes.android.data.dao.RecipeWithCollectionsDao;
import com.keeprecipes.android.data.AppDatabase;
import com.keeprecipes.android.data.entities.CategoriesRecipeCrossRef;
import com.keeprecipes.android.data.entities.CategoriesWithRecipes;
import com.keeprecipes.android.data.entities.Recipe;
import com.keeprecipes.android.data.entities.RecipeWithCategories;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class CollectionWithRecipesRepository {

    CollectionWithRecipesDao collectionWithRecipesDao;
    RecipeWithCollectionsDao recipeWithCollectionsDao;

    private LiveData<List<CategoriesWithRecipes>> collectionWithRecipesList;

    public CollectionWithRecipesRepository(Application application) {
        collectionWithRecipesDao = AppDatabase.getDatabase(application).collectionWithRecipesDao();
        recipeWithCollectionsDao = AppDatabase.getDatabase(application).recipeWithCollectionsDao();
    }

    public LiveData<List<Recipe>> getCollectionWithRecipesById(long categoriesId) {
        return collectionWithRecipesDao.getCategoriesWithRecipesById(categoriesId);
    }

    public List<Long> getUniqueCategories(long recipeId) {
        return collectionWithRecipesDao.getUniqueCategories(recipeId);
    }

    public LiveData<List<RecipeWithCategories>> getRecipeWithCategories(long id) {
        LiveData<List<RecipeWithCategories>> recipeWithCollections = recipeWithCollectionsDao.getRecipeWithCategories(id);
        return recipeWithCollections;
    }

    public void insert(ArrayList<Long> collectionId, long recipeId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            collectionWithRecipesDao.deleteByRecipe(recipeId);
            for (Long c : collectionId) {
                CategoriesRecipeCrossRef categoriesRecipeCrossRef = new CategoriesRecipeCrossRef(c, recipeId);
                collectionWithRecipesDao.insert(categoriesRecipeCrossRef);
            }
        });
    }

    public void deleteByRecipe(long recipeId) {
        Executors.newSingleThreadExecutor().execute(() -> collectionWithRecipesDao.deleteByRecipe(recipeId));
    }

    public void clearPrimaryKey() {
        Executors.newSingleThreadExecutor().execute(() -> Executors.newSingleThreadExecutor().execute(() ->
                collectionWithRecipesDao.clearPrimaryKey()));
    }
}