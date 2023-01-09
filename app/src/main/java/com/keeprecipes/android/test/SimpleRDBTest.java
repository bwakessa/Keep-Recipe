package com.keeprecipes.android.test;

import static org.junit.jupiter.api.Assertions.*;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.test.core.app.ApplicationProvider;

import com.keeprecipes.android.dataLayer.AppDatabase;
import com.keeprecipes.android.dataLayer.dao.RecipeDao;
import com.keeprecipes.android.dataLayer.entities.Recipe;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class SimpleRDBTest {
    private static RecipeDao recipeDao;
    private static AppDatabase db;

    @BeforeAll
    public static void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = AppDatabase.getDatabase(context);
        recipeDao = db.recipeDao();
    }

    @AfterAll
    public static void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testCreateDb() {
        LiveData<Recipe> r = recipeDao.fetchByTitle("Pasta Carbonara");
    }
    // PROBLEM:
}


