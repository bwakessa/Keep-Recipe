package com.keeprecipes.android.data.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.keeprecipes.android.LiveDataHelper;
import com.keeprecipes.android.data.AppDatabase;
import com.keeprecipes.android.data.entities.Category;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

@HiltAndroidTest
public class CollectionDaoTest {

    @Rule
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Inject
    AppDatabase appDatabase;

    RecipeDao recipeDao;
    CollectionDao collectionDao;

    @Before
    public void setUp() {
        hiltRule.inject();
        recipeDao = appDatabase.recipeDao();
        collectionDao = appDatabase.recipeCollectionDao();
        assertNotNull(appDatabase);
        assertNotNull(collectionDao);
    }

    @After
    public void tearDown() {
        appDatabase.clearAllTables();
        appDatabase.close();
    }

    @Test
    public void insert() throws InterruptedException {
        Category category = new Category("Coffee");
        collectionDao.insert(category);
        assertEquals(LiveDataHelper.getOrAwaitValue(collectionDao.getAll()).size(), 1);
    }

    @Test
    public void delete() throws InterruptedException {
        Category category = new Category("Coffee");
        collectionDao.insert(category);
        category = LiveDataHelper.getOrAwaitValue(collectionDao.getAll()).get(0);
        assertEquals(category.name, "Coffee");
        collectionDao.delete(category);
        assertEquals(LiveDataHelper.getOrAwaitValue(collectionDao.getAll()).size(), 0);
    }

    @Test
    public void deleteById() throws InterruptedException {
        Category category = new Category("Coffee");
        long categoryId = collectionDao.insert(category);
        category = LiveDataHelper.getOrAwaitValue(collectionDao.getAll()).get(0);
        assertEquals(category.name, "Coffee");
        collectionDao.deleteById(categoryId);
        assertEquals(LiveDataHelper.getOrAwaitValue(collectionDao.getAll()).size(), 0);
    }

    @Test
    public void updateCollection() throws InterruptedException {
        Category category = new Category("Coffee");
        collectionDao.insert(category);
        assertEquals(LiveDataHelper.getOrAwaitValue(collectionDao.getAll()).get(0).name, "Coffee");
        category = LiveDataHelper.getOrAwaitValue(collectionDao.getAll()).get(0);
        category.name = "Tea";
        collectionDao.updateCollection(category);
        assertEquals(LiveDataHelper.getOrAwaitValue(collectionDao.getAll()).get(0).name, "Tea");
    }

    @Test
    public void getAll() throws InterruptedException {
        collectionDao.insert(new Category("Coffee"));
        collectionDao.insert(new Category("Tea"));
        collectionDao.insert(new Category("Milk"));
        collectionDao.insert(new Category("Cookie"));
        assertEquals(LiveDataHelper.getOrAwaitValue(collectionDao.getAll()).size(), 4);
    }

    @Test
    public void fetchIdByName() throws InterruptedException {
        long id = collectionDao.insert(new Category("Coffee"));
        assertEquals(collectionDao.fetchIdByName("Coffee"), id);
    }

    @Test
    public void fetchByName() throws InterruptedException {
        collectionDao.insert(new Category("Coffee"));
        collectionDao.insert(new Category("Tea"));
        collectionDao.insert(new Category("Milk"));
        collectionDao.insert(new Category("Cookie"));
        Category category = LiveDataHelper.getOrAwaitValue(collectionDao.fetchByName("Coffee"));
        assertEquals(category.name, "Coffee");
        category = LiveDataHelper.getOrAwaitValue(collectionDao.fetchByName("Tea"));
        assertEquals(category.name, "Tea");
        category = LiveDataHelper.getOrAwaitValue(collectionDao.fetchByName("Milk"));
        assertEquals(category.name, "Milk");
        category = LiveDataHelper.getOrAwaitValue(collectionDao.fetchByName("Cookie"));
        assertEquals(category.name, "Cookie");
    }

    @Test
    public void isRowExist() {
        collectionDao.insert(new Category("Coffee"));
        collectionDao.insert(new Category("Tea"));
        collectionDao.insert(new Category("Milk"));
        collectionDao.insert(new Category("Cookie"));

        assertEquals(collectionDao.isRowExist("Coffee"), true);
        assertEquals(collectionDao.isRowExist("Tea"), true);
        assertEquals(collectionDao.isRowExist("Milk"), true);
        assertEquals(collectionDao.isRowExist("Cookie"), true);
    }
}