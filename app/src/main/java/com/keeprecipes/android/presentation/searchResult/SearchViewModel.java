package com.keeprecipes.android.presentation.searchResult;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.keeprecipes.android.data.entities.Recipe;
import com.keeprecipes.android.data.repository.RecipeRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SearchViewModel extends ViewModel {
    RecipeRepository recipeRepository;

    @Inject
    public SearchViewModel(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public LiveData<List<Recipe>> searchRecipe(String query) {
        return recipeRepository.searchRecipe(query);
    }
}