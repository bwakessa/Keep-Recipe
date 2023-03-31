package com.keeprecipes.android.dataLayer.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.keeprecipes.android.dataLayer.AppDatabase;
import com.keeprecipes.android.dataLayer.dao.CollectionDao;
import com.keeprecipes.android.dataLayer.entities.Collection;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CollectionRepository {

    private CollectionDao mCollectionDao;
    private LiveData<List<Collection>> allCollections;

    private ExecutorService executorService;

    public CollectionRepository(Application application) {
        mCollectionDao = AppDatabase.getDatabase(application).recipeCollectionDao();
        allCollections = mCollectionDao.getAll();
    }

    public LiveData<List<Collection>> getAllCollections() {
        return allCollections;
    }

    public Long fetchByName(String collectionName) {
        executorService = Executors.newSingleThreadExecutor();
        Callable<Long> fetchIdByNameCallable = () -> mCollectionDao.fetchIdByName(collectionName);
        Long id = -1L;
        Future<Long> future = executorService.submit(fetchIdByNameCallable);
        try {
            id = future.get();
        } catch (InterruptedException | ExecutionException e1) {
            e1.printStackTrace();
        }
        executorService.shutdown();
        return id;
    }

    public LiveData<Collection> fetchByNameObserver(String title) {
        return mCollectionDao.fetchByName(title);
    }

    public Boolean isRowExist(Collection collection) {
        executorService = Executors.newSingleThreadExecutor();
        Callable<Boolean> isRowExistCallable = () -> mCollectionDao.isRowExist(collection.name);
        Boolean exist = false;
        Future<Boolean> future = executorService.submit(isRowExistCallable);
        try {
            exist = future.get();
        } catch (InterruptedException | ExecutionException e1) {
            e1.printStackTrace();
        }
        executorService.shutdown();
        return exist;
    }

    public long insert(Collection collection) {
        executorService = Executors.newSingleThreadExecutor();
        Callable<Long> insertCallable = () -> mCollectionDao.insert(collection);
        long rowId = 0;
        Future<Long> future = executorService.submit(insertCallable);
        try {
            rowId = future.get();
        } catch (InterruptedException | ExecutionException e1) {
            e1.printStackTrace();
        }
        executorService.shutdown();
        return rowId;
    }

    public void delete(Collection collection) {
        Executors.newSingleThreadExecutor().execute(() -> mCollectionDao.delete(collection));
    }

    public void clearPrimaryKey() {
        Executors.newSingleThreadExecutor().execute(() ->
                mCollectionDao.clearPrimaryKey());
    }

    public void update(Collection collection) {
        Executors.newSingleThreadExecutor().execute(() -> mCollectionDao.updateCollection(collection));
    }
}
