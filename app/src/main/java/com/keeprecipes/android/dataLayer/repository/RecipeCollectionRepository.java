package com.keeprecipes.android.dataLayer.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executors;

import com.keeprecipes.android.dataLayer.AppDatabase;
import com.keeprecipes.android.dataLayer.dao.RecipeCollectionDao;
import com.keeprecipes.android.dataLayer.entities.Recipe;
import com.keeprecipes.android.dataLayer.entities.RecipeCollection;

public class RecipeCollectionRepository {

    private RecipeCollectionDao mCollectionDao;
    private LiveData<List<RecipeCollection>> allCollections;

    public RecipeCollectionRepository(Application application) {
        mCollectionDao = AppDatabase.getCollectionDatabase(application).recipeCollectionDao();
        allCollections = mCollectionDao.getAll();
    }

    public LiveData<List<RecipeCollection>> getAllCollections() {return allCollections;}

//    public LiveData<RecipeCollection> fetchByName(String title) {
//        Executors.newSingleThreadExecutor().execute(() -> mCollectionDao.fetchByName(title));
//    }
//  HOW TO WRITE THIS METHOD

    public void insert(RecipeCollection collection) {
        Executors.newSingleThreadExecutor().execute(() -> mCollectionDao.insert(collection));
    }

    public void delete(RecipeCollection collection) {
        Executors.newSingleThreadExecutor().execute(() -> mCollectionDao.delete(collection));
    }

    public void update(RecipeCollection collection) {
        Executors.newSingleThreadExecutor().execute(() -> mCollectionDao.updateCollection(collection));
    }
}
