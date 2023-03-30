package com.keeprecipes.android.dataLayer.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.keeprecipes.android.dataLayer.AppDatabase;
import com.keeprecipes.android.dataLayer.dao.RecipeDao;
import com.keeprecipes.android.dataLayer.entities.Recipe;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RecipeRepository {

    private static final String TAG = "RecipeRepository";

    private RecipeDao mRecipeDao;
    private LiveData<List<Recipe>> allRecipes;

    private ExecutorService executorService;

    public RecipeRepository() {
    }

    public RecipeRepository(Application application) {
        mRecipeDao = AppDatabase.getDatabase(application).recipeDao();
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

    public LiveData<List<String>> getAllCuisine() {
        return mRecipeDao.getAllCuisine();
    }

    public long insert(Recipe recipe) {
        executorService = Executors.newSingleThreadExecutor();
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

    public void deleteAllRecipes(Application application) {
        Executors.newSingleThreadExecutor().execute(() -> {
            Executors.newSingleThreadExecutor().execute(() ->
                    AppDatabase.getDatabase(application).clearAllTables());
        });
    }

    public void clearPrimaryKey() {
        Executors.newSingleThreadExecutor().execute(() -> {
            Executors.newSingleThreadExecutor().execute(() ->
                    mRecipeDao.clearPrimaryKey());
        });
    }

    public void update(Recipe recipe) {
        Executors.newSingleThreadExecutor().execute(() -> {
            mRecipeDao.updateRecipe(recipe);
        });
    }

    public LiveData<List<Recipe>> searchRecipe(String query) {
        return mRecipeDao.searchRecipe(query);
    }
}
