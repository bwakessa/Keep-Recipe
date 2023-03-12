package com.keeprecipes.android.dataLayer.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.keeprecipes.android.dataLayer.AppDatabase;
import com.keeprecipes.android.dataLayer.dao.CollectionDao;
import com.keeprecipes.android.dataLayer.entities.Collection;

import java.util.List;
import java.util.concurrent.Executors;

public class CollectionRepository {

    private CollectionDao mCollectionDao;
    private LiveData<List<Collection>> allCollections;

    public CollectionRepository(Application application) {
        mCollectionDao = AppDatabase.getDatabase(application).recipeCollectionDao();
        allCollections = mCollectionDao.getAll();
    }

    public LiveData<List<Collection>> getAllCollections() {
        return allCollections;
    }

    public LiveData<Collection> fetchByName(String title) {
        return mCollectionDao.fetchByName(title);
    }

    public void insert(Collection collection) {
        Executors.newSingleThreadExecutor().execute(() -> mCollectionDao.insert(collection));
    }

    public void delete(Collection collection) {
        Executors.newSingleThreadExecutor().execute(() -> mCollectionDao.delete(collection));
    }

    public void update(Collection collection) {
        Executors.newSingleThreadExecutor().execute(() -> mCollectionDao.updateCollection(collection));
    }
}
