package com.keeprecipes.android;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.keeprecipes.android.dataLayer.AppDatabase;
import com.keeprecipes.android.dataLayer.dao.RecipeDao;
import com.keeprecipes.android.dataLayer.entities.Recipe;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class SimpleRDBTest {
    private static RecipeDao recipeDao;
    private static AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = AppDatabase.getDatabase(context);
        recipeDao = db.recipeDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testCreateDb() {
        LiveData<Recipe> r = recipeDao.fetchByTitle("Pasta Carbonara");
    }
    // PROBLEM:
}


