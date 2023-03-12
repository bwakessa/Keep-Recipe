package com.keeprecipes.android.dataLayer.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.keeprecipes.android.dataLayer.AppDatabase;
import com.keeprecipes.android.dataLayer.dao.CollectionWithRecipesDao;
import com.keeprecipes.android.dataLayer.entities.CollectionWithRecipes;

import java.util.List;

public class CollectionWithRecipesRepository {

    private LiveData<List<CollectionWithRecipes>> collectionWithRecipesList;

    public CollectionWithRecipesRepository(Application application) {
        CollectionWithRecipesDao collectionWithRecipesDao = AppDatabase.getDatabase(application).collectionWithRecipesDao();
        collectionWithRecipesList = collectionWithRecipesDao.getCollectionWithRecipes();
    }

    public LiveData<List<CollectionWithRecipes>> getCollectionWithRecipesList() {
        return collectionWithRecipesList;
    }
}
