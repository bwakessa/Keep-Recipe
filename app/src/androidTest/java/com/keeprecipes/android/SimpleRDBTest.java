package com.keeprecipes.android;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.keeprecipes.android.dataLayer.AppDatabase;
import com.keeprecipes.android.dataLayer.dao.RecipeDao;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
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
        recipeDao.drop();
        db.close();
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

    @Test
    public void testExistsInDb() throws InterruptedException {
//        Recipe alfredo = new Recipe();
//        alfredo.setDateCreated(Instant.now());
//
//        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
//        ingredients.add(new Ingredient("pasta", 3));
//        ingredients.add(new Ingredient("chicken", 2));
//        ingredients.add(new Ingredient("cheese", 1));
//        alfredo.setIngredients(ingredients);
//
//        alfredo.setInstructions("Cook the damn food");
//
//        alfredo.setTitle("Chicken");
//        alfredo.setCuisine("Italian");
//
//        recipeDao.insert(alfredo);
//
//        Recipe gotten_alfredo = getOrAwaitValue(recipeDao.fetchByTitle("Chicken"));
//        assert (gotten_alfredo.getTitle().equals("Chicken"));
    }
}


