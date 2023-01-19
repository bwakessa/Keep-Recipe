package com.keeprecipes.android.presentationLayer.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.keeprecipes.android.dataLayer.entities.Recipe;
import com.keeprecipes.android.dataLayer.repository.RecipeRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private LiveData<List<Recipe>> recipe;

    private RecipeRepository recipeRepository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        recipeRepository = new RecipeRepository(application);
        recipe = recipeRepository.getAllRecipes();
    }

    public LiveData<List<Recipe>> getRecipe() {
        return recipe;
    }
}