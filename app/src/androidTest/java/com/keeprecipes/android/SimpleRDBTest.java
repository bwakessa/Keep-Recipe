package com.keeprecipes.android;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import android.content.Context;

import androidx.lifecycle.LiveData;
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

@RunWith(AndroidJUnit4.class)
public class SimpleRDBTest {
    private static RecipeDao recipeDao;
    private static AppDatabase db;

    @BeforeClass
    public static void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = AppDatabase.getDatabase(context);
        recipeDao = db.recipeDao();
    }

    @AfterClass
    public static void closeDb() throws IOException {
        //In the dao, do delete all values from database
        db.close();
    }

    @Test
    public void testExistsInDb() {
        Recipe alfredo = new Recipe();
        alfredo.setDateCreated(Instant.now());

        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(new Ingredient("pasta", 3));
        ingredients.add(new Ingredient("chicken", 2));
        ingredients.add(new Ingredient("cheese", 1));
        alfredo.setIngredients(ingredients);

        alfredo.setInstructions( "Cook the damn food");

        alfredo.setTitle("Chicken");
        alfredo.setCuisine("Italian");
//        alfredo.setId(1);

        recipeDao.insert(alfredo);

        LiveData<Recipe> gotten_alfredo = recipeDao.fetchByTitle("Chicken");

        Recipe value = gotten_alfredo.getValue();
        assert (value.getTitle() == "Chicken Alfredo");
    }

}


