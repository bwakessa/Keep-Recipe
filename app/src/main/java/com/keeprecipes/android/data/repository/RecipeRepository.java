package com.keeprecipes.android.data.repository;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.keeprecipes.android.data.dao.RecipeDao;
import com.keeprecipes.android.data.entities.Recipe;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.inject.Inject;

public class RecipeRepository {

    private final RecipeDao mRecipeDao;
    private final LiveData<List<Recipe>> allRecipes;

    @Inject
    public RecipeRepository(RecipeDao recipeDao) {
        this.mRecipeDao = recipeDao;
        allRecipes = mRecipeDao.getAll();
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return allRecipes;
    }

    public LiveData<Recipe> fetchByTitle(String title) {
        return mRecipeDao.fetchByTitle(title);
    }

    public LiveData<Recipe> fetchById(int id) {
        return mRecipeDao.fetchById(id);
    }

    public long insert(Recipe recipe) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Callable<Long> insertCallable = () -> mRecipeDao.insert(recipe);
        long rowId = 0;
        Future<Long> future = executorService.submit(insertCallable);
        try {
            rowId = future.get();
        } catch (InterruptedException | ExecutionException e1) {
            e1.printStackTrace();
        }
        return rowId;
    }

    public void delete(Recipe recipe) {
        Executors.newSingleThreadExecutor().execute(() -> {
            mRecipeDao.delete(recipe);
            mRecipeDao.vacuumDb(new SimpleSQLiteQuery("VACUUM"));
        });
    }

    public void clearPrimaryKey() {
        Executors.newSingleThreadExecutor().execute(() -> Executors.newSingleThreadExecutor().execute(mRecipeDao::clearPrimaryKey));
    }

    public void update(Recipe recipe) {
        Executors.newSingleThreadExecutor().execute(() -> mRecipeDao.updateRecipe(recipe));
    }

    public LiveData<List<Recipe>> searchRecipe(String query) {
        return mRecipeDao.searchRecipe(query);
    }

    public void drop() {
        Executors.newSingleThreadExecutor().execute(() -> Executors.newSingleThreadExecutor().execute(mRecipeDao::drop));
    }
}
