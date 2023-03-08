package com.keeprecipes.android.presentationLayer.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.keeprecipes.android.dataLayer.entities.Recipe;
import com.keeprecipes.android.dataLayer.repository.RecipeRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private final LiveData<List<Recipe>> recipe;
    private final MutableLiveData<Integer> recipeId = new MutableLiveData();
    private RecipeRepository recipeRepository;
    public final LiveData<Recipe> selectedRecipe = Transformations.switchMap(recipeId, (recipe) -> recipeRepository.fetchById(recipeId.getValue()));

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.recipeRepository = new RecipeRepository(application);
        this.recipe = recipeRepository.getAllRecipes();
    }

    public LiveData<List<Recipe>> getRecipe() {
        return recipe;
    }

    public void setRecipeId(int id) {
        recipeId.setValue(id);
    }

    public void deleteRecipe(Recipe recipe) {
        recipeRepository.delete(recipe);
    }

    public void deleteAllRecipes() {
        recipeRepository.deleteAllRecipes();
    }

    public LiveData<List<String>> getAllCuisineCollection() {
        return recipeRepository.getAllCuisineCollection();
    }

    public LiveData<List<Recipe>> searchRecipe(String query) {
        return recipeRepository.searchRecipe(query);
    }
}