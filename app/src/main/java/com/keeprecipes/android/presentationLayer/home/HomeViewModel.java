package com.keeprecipes.android.presentationLayer.home;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.keeprecipes.android.dataLayer.entities.CategoriesWithRecipes;
import com.keeprecipes.android.dataLayer.entities.Category;
import com.keeprecipes.android.dataLayer.entities.Recipe;
import com.keeprecipes.android.dataLayer.repository.CollectionRepository;
import com.keeprecipes.android.dataLayer.repository.CollectionWithRecipesRepository;
import com.keeprecipes.android.dataLayer.repository.RecipeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class HomeViewModel extends AndroidViewModel {
    final String TAG = "AndroidViewModel";
    public final LiveData<Recipe> selectedRecipe;
    private final LiveData<List<Recipe>> recipe;
    private final MutableLiveData<Integer> recipeId;
    private final MutableLiveData<List<String>> selectedCollection;
    private final CollectionRepository collectionRepository;
    private final RecipeRepository recipeRepository;
    private final CollectionWithRecipesRepository collectionWithRecipesRepository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.recipeRepository = new RecipeRepository(application);
        this.collectionRepository = new CollectionRepository(application);
        this.collectionWithRecipesRepository = new CollectionWithRecipesRepository(application);
        this.recipe = recipeRepository.getAllRecipes();
        this.recipeId = new MutableLiveData<>();
        this.selectedRecipe = Transformations.switchMap(recipeId, (recipe) -> recipeRepository.fetchById(recipeId.getValue()));
        this.selectedCollection = new MutableLiveData<>();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipe;
    }

    public LiveData<List<Category>> getCollections() {
        return collectionRepository.getAllCollections();
    }

    public LiveData<List<CategoriesWithRecipes>> getCollectionWithRecipesById(long id) {
        return collectionWithRecipesRepository.getCollectionWithRecipesById(id);
    }

    public void setRecipeId(int id) {
        recipeId.setValue(id);
    }

    public void deleteRecipe(Recipe recipe) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Callable<List<Long>> uniqueCallable = () -> collectionWithRecipesRepository.getUniqueCategories(recipe.recipeId);
        List<Long> uniqueCategories = new ArrayList<>();
        Future<List<Long>> future = executorService.submit(uniqueCallable);
        try {
            uniqueCategories = future.get();
        } catch (InterruptedException | ExecutionException e1) {
            e1.printStackTrace();
        }
        executorService.shutdown();
        Log.d(TAG, "deleteRecipe: " + uniqueCategories);
        collectionWithRecipesRepository.deleteByRecipe(recipe.recipeId);
        uniqueCategories.forEach(collectionRepository::deleteById);
        recipeRepository.delete(recipe);
    }

    public void deleteAllRecipes(Application application) {
        recipeRepository.deleteAllRecipes(application);
        recipeRepository.clearPrimaryKey();
        collectionRepository.clearPrimaryKey();
        collectionWithRecipesRepository.clearPrimaryKey();
    }

    public LiveData<List<Recipe>> searchRecipe(String query) {
        return recipeRepository.searchRecipe(query);
    }
}