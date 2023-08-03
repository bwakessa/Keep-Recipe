package com.keeprecipes.android.data.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.keeprecipes.android.LiveDataHelper;
import com.keeprecipes.android.data.AppDatabase;
import com.keeprecipes.android.data.entities.Ingredient;
import com.keeprecipes.android.data.entities.Recipe;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

@HiltAndroidTest
public class RecipeDaoTest {

    @Rule
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Inject
    AppDatabase appDatabase;

    RecipeDao recipeDao;

    @Before
    public void setUp() {
        hiltRule.inject();
        recipeDao = appDatabase.recipeDao();
        assertNotNull(appDatabase);
        assertNotNull(recipeDao);
    }

    @After
    public void tearDown() {
        appDatabase.clearAllTables();
        appDatabase.close();
    }

    @Test
    public void insert() throws InterruptedException {
        Recipe recipe = new Recipe();
        recipeDao.insert(recipe);
        int recipeCount = LiveDataHelper.getOrAwaitValue(recipeDao.getAll()).size();

        assertEquals(recipeCount, 1);

        recipe.title = "Cardamon Coffee";
        recipeDao.insert(recipe);
        String recipeTitle = LiveDataHelper.getOrAwaitValue(recipeDao.searchRecipe(recipe.title)).get(0).title;

        assertEquals(recipeTitle, recipe.title);
    }

    @Test
    public void delete() throws InterruptedException {
        Recipe recipe = new Recipe();
        recipeDao.insert(recipe);
        int recipeCount = LiveDataHelper.getOrAwaitValue(recipeDao.getAll()).size();
        assertEquals(recipeCount, 1);

        recipe = LiveDataHelper.getOrAwaitValue(recipeDao.getAll()).get(0);
        recipeDao.delete(recipe);
        recipeCount = LiveDataHelper.getOrAwaitValue(recipeDao.getAll()).size();

        assertEquals(recipeCount, 0);
    }

    @Test
    public void fetchById() throws InterruptedException {
        Recipe recipe = new Recipe();
        recipe.title = "Cardamon Coffee";
        recipeDao.insert(recipe);

        recipe = LiveDataHelper.getOrAwaitValue(recipeDao.getAll()).get(0);

        Recipe recipe1 = LiveDataHelper.getOrAwaitValue(recipeDao.fetchById((int) recipe.recipeId));

        assertEquals(recipe1.title, recipe.title);
    }

    @Test
    public void fetchByTitle() throws InterruptedException {
        Recipe recipe = new Recipe();
        recipe.title = "Cardamon Coffee";
        recipeDao.insert(recipe);

        Recipe recipe1 = LiveDataHelper.getOrAwaitValue(recipeDao.fetchByTitle(recipe.title));

        assertEquals(recipe1.title, recipe.title);
    }

    @Test
    public void getAll() throws InterruptedException {
        Recipe recipe = new Recipe();
        recipeDao.insert(recipe);
        int recipeCount = LiveDataHelper.getOrAwaitValue(recipeDao.getAll()).size();
        assertEquals(recipeCount, 1);
    }

    @Test
    public void searchRecipe() throws InterruptedException {
        Recipe recipe = new Recipe();
        recipe.title = "Cardmon Coffee";
        recipe.dateCreated = Instant.now();
        recipe.instructions = "Grind cardamom seeds and coffee together and store in an airtight container.\n" +
                "\n" +
                "Brew the coffee using your regular method, or make traditional cooked coffee: Grind beans and cardamom very finely; bring 1 1/2 cups water to boil in a small pan (traditionally a long-handled cup-like pot called a finjan is used) and stir in 1/4 cup coffee-cardamom mixture and sugar to taste.\n" +
                "\n" +
                "Cook over medium-low heat until liquid simmers around the edges and coffee grinds begin to sink, 2 to 3 minutes.\n" +
                "\n" +
                "Remove from heat and let set 1 minute.\n" +
                "\n" +
                "Pour coffee carefully into small cups, leaving most of the grounds behind in the pan.";
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("whole cardamom seeds", "1 1/2", "teaspoon"));
        ingredients.add(new Ingredient("dark-roast whole coffee beans", "1", "cup"));
        recipe.ingredients = ingredients;
        recipeDao.insert(recipe);

        assertEquals(LiveDataHelper.getOrAwaitValue(recipeDao.searchRecipe("KitKat")).size(), 0);
        assertEquals(LiveDataHelper.getOrAwaitValue(recipeDao.searchRecipe("Coffee")).size(), 1);
        assertEquals(LiveDataHelper.getOrAwaitValue(recipeDao.searchRecipe("Cardmon")).size(), 1);
        assertEquals(LiveDataHelper.getOrAwaitValue(recipeDao.searchRecipe("Brew the coffee")).size(), 1);
    }

    @Test
    public void updateRecipe() throws InterruptedException {
        Recipe recipe = new Recipe();
        recipe.title = "Spicy Coffee";
        recipe.dateCreated = Instant.now();
        recipe.instructions = "Grind cardamom seeds and coffee together and store in an airtight container.\n" +
                "\n" +
                "Brew the coffee using your regular method, or make traditional cooked coffee: Grind beans and cardamom very finely; bring 1 1/2 cups water to boil in a small pan (traditionally a long-handled cup-like pot called a finjan is used) and stir in 1/4 cup coffee-cardamom mixture and sugar to taste.\n" +
                "\n" +
                "Cook over medium-low heat until liquid simmers around the edges and coffee grinds begin to sink, 2 to 3 minutes.\n" +
                "\n" +
                "Remove from heat and let set 1 minute.\n" +
                "\n" +
                "Pour coffee carefully into small cups, leaving most of the grounds behind in the pan.";
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("whole cardamom seeds", "1 1/2", "teaspoon"));
        ingredients.add(new Ingredient("dark-roast whole coffee beans", "1", "cup"));
        recipe.ingredients = ingredients;
        recipeDao.insert(recipe);

        recipe = LiveDataHelper.getOrAwaitValue(recipeDao.searchRecipe("Spicy")).get(0);
        assertEquals(recipe.title, "Spicy Coffee");

        recipe.title = "Americano";
        recipeDao.updateRecipe(recipe);

        recipe = LiveDataHelper.getOrAwaitValue(recipeDao.searchRecipe("Americano")).get(0);
        assertEquals(recipe.title, "Americano");

        assertEquals(LiveDataHelper.getOrAwaitValue(recipeDao.searchRecipe("Spicy")).size(), 0);
    }

    @Test
    public void drop() throws InterruptedException {
        Recipe recipe = new Recipe();
        recipeDao.insert(recipe);
        int recipeCount = LiveDataHelper.getOrAwaitValue(recipeDao.getAll()).size();
        assertEquals(recipeCount, 1);

        recipeDao.drop();
        recipeCount = LiveDataHelper.getOrAwaitValue(recipeDao.getAll()).size();
        assertEquals(recipeCount, 0);
    }
}