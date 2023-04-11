package com.keeprecipes.android.dataLayer.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.keeprecipes.android.dataLayer.AppDatabase;
import com.keeprecipes.android.dataLayer.dao.CollectionDao;
import com.keeprecipes.android.dataLayer.entities.Categories;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CollectionRepository {

    private CollectionDao mCollectionDao;
    private LiveData<List<Categories>> allCollections;

    private ExecutorService executorService;

    public CollectionRepository(Application application) {
        mCollectionDao = AppDatabase.getDatabase(application).recipeCollectionDao();
        allCollections = mCollectionDao.getAll();
    }

    public LiveData<List<Categories>> getAllCollections() {
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

    public LiveData<Categories> fetchByNameObserver(String title) {
        return mCollectionDao.fetchByName(title);
    }

    public Boolean isRowExist(Categories categories) {
        executorService = Executors.newSingleThreadExecutor();
        Callable<Boolean> isRowExistCallable = () -> mCollectionDao.isRowExist(categories.name);
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

    public long insert(Categories categories) {
        executorService = Executors.newSingleThreadExecutor();
        Callable<Long> insertCallable = () -> mCollectionDao.insert(categories);
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

    public void delete(Categories categories) {
        Executors.newSingleThreadExecutor().execute(() -> mCollectionDao.delete(categories));
    }

    public void clearPrimaryKey() {
        Executors.newSingleThreadExecutor().execute(() ->
                mCollectionDao.clearPrimaryKey());
    }

    public void update(Categories categories) {
        Executors.newSingleThreadExecutor().execute(() -> mCollectionDao.updateCollection(categories));
    }
}
