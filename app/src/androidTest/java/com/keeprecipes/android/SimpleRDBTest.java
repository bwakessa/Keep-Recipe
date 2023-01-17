package com.keeprecipes.android;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import com.keeprecipes.android.dataLayer.AppDatabase;
import com.keeprecipes.android.dataLayer.dao.RecipeDao;
import com.keeprecipes.android.dataLayer.entities.Ingredient;
import com.keeprecipes.android.dataLayer.entities.Recipe;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class SimpleRDBTest {
    private static RecipeDao recipeDao;
    private static AppDatabase db;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @BeforeClass
    public static void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = AppDatabase.getDatabase(context);
        recipeDao = db.recipeDao();
    }

    @AfterClass
    public static void closeDb() throws IOException {
        //In the dao, do delete all values from database
        recipeDao.
        db.close();
    }

    @Test
    public void testExistsInDb() throws InterruptedException {
        Recipe alfredo = new Recipe();
        alfredo.setDateCreated(Instant.now());

        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(new Ingredient("pasta", 3));
        ingredients.add(new Ingredient("chicken", 2));
        ingredients.add(new Ingredient("cheese", 1));
        alfredo.setIngredients(ingredients);

        alfredo.setInstructions("Cook the damn food");

        alfredo.setTitle("Chicken");
        alfredo.setCuisine("Italian");
//        alfredo.setId(1);

        recipeDao.insert(alfredo);

        LiveData<Recipe> gotten_alfredo = recipeDao.fetchByTitle("Chicken");
//        LiveData<List<Recipe>> gotten_alfredo = recipeDao.getAll();
//        List<Recipe> values = gotten_alfredo.getValue();
        Recipe value = getOrAwaitValue(gotten_alfredo);

        assert (value.getTitle() == "Chicken");
    }

    public static <T> T getOrAwaitValue(final LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(@Nullable T o) {
                data[0] = o;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(2, TimeUnit.SECONDS)) {
            throw new RuntimeException("LiveData value was never set.");
        }
        //noinspection unchecked
        return (T) data[0];
    }
}
