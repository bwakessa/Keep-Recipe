package com.keeprecipes.android.usecase;

import android.util.Log;

import com.keeprecipes.android.data.entities.Recipe;
import com.keeprecipes.android.data.repository.CollectionRepository;
import com.keeprecipes.android.data.repository.CollectionWithRecipesRepository;
import com.keeprecipes.android.data.repository.RecipeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.inject.Inject;

public class DeleteRecipeUseCase {

    private final String TAG = "DeleteRecipeUseCase";
    RecipeRepository recipeRepository;
    CollectionRepository collectionRepository;
    CollectionWithRecipesRepository collectionWithRecipesRepository;

    @Inject
    public DeleteRecipeUseCase(RecipeRepository recipeRepository, CollectionRepository collectionRepository, CollectionWithRecipesRepository collectionWithRecipesRepository) {
        this.recipeRepository = recipeRepository;
        this.collectionRepository = collectionRepository;
        this.collectionWithRecipesRepository = collectionWithRecipesRepository;
    }

    public void deleteAll() {
        this.recipeRepository.drop();
        this.collectionRepository.drop();
        this.collectionWithRecipesRepository.drop();
        this.recipeRepository.clearPrimaryKey();
        this.collectionRepository.clearPrimaryKey();
        this.collectionWithRecipesRepository.clearPrimaryKey();
    }

    public void delete(Recipe recipe) {
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
}
