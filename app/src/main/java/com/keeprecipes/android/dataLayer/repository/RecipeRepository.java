package com.keeprecipes.android.dataLayer.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executors;

import com.keeprecipes.android.dataLayer.AppDatabase;
import com.keeprecipes.android.dataLayer.dao.RecipeDao;
import com.keeprecipes.android.dataLayer.entities.Recipe;

public class RecipeRepository {

    private RecipeDao mRecipeDao;
    private LiveData<List<Recipe>> allRecipes;

    public RecipeRepository(Application application) {
        mRecipeDao = AppDatabase.getDatabase(application).recipeDao();
        allRecipes = mRecipeDao.getAll();
    }

    public LiveData<List<Recipe>> getAllRecipes() {return allRecipes;}

    public LiveData<Recipe> fetchByTitle(String title) {
        return mRecipeDao.fetchByTitle(title);
    }

    public void insert(Recipe recipe) {
        Executors.newSingleThreadExecutor().execute(() -> mRecipeDao.insert(recipe));
    }

    public void delete(Recipe recipe) {
        Executors.newSingleThreadExecutor().execute(() -> mRecipeDao.delete(recipe));
    }

    public void update(Recipe recipe) {
        Executors.newSingleThreadExecutor().execute(() -> mRecipeDao.updateRecipe(recipe));
    }
}