package com.keeprecipes.android.presentationLayer.searchResult;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.keeprecipes.android.dataLayer.entities.Recipe;
import com.keeprecipes.android.dataLayer.repository.RecipeRepository;

import java.util.List;

public class SearchViewModel extends AndroidViewModel {
    private final RecipeRepository recipeRepository;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        this.recipeRepository = new RecipeRepository(application);
    }

    public LiveData<List<Recipe>> searchRecipe(String query) {
        return recipeRepository.searchRecipe(query);
    }
}