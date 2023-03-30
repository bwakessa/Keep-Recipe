package com.keeprecipes.android.presentationLayer.home;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.keeprecipes.android.dataLayer.entities.Collection;
import com.keeprecipes.android.dataLayer.entities.Recipe;
import com.keeprecipes.android.dataLayer.repository.CollectionRepository;
import com.keeprecipes.android.dataLayer.repository.CollectionWithRecipesRepository;
import com.keeprecipes.android.dataLayer.repository.RecipeRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    final String TAG = "AndroidViewModel";

    private final LiveData<List<Recipe>> recipe;
    private final MutableLiveData<Integer> recipeId;
    private CollectionRepository collectionRepository;
    private RecipeRepository recipeRepository;
    public final LiveData<Recipe> selectedRecipe;

    private final CollectionWithRecipesRepository collectionWithRecipesRepository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.recipeRepository = new RecipeRepository(application);
        this.collectionRepository = new CollectionRepository(application);
        this.collectionWithRecipesRepository = new CollectionWithRecipesRepository(application);
        this.recipe = recipeRepository.getAllRecipes();
        this.recipeId = new MutableLiveData<>();
        this.selectedRecipe = Transformations.switchMap(recipeId, (recipe) -> recipeRepository.fetchById(recipeId.getValue()));
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipe;
    }

    public LiveData<List<Collection>> getCollections() {
        return collectionRepository.getAllCollections();
    }

    public void setRecipeId(int id) {
        recipeId.setValue(id);
    }

    public void deleteRecipe(Recipe recipe) {
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

    public LiveData<Collection> collectionFetchByName(String query) {
        Log.d(TAG, "collectionFetchByName: " + query);
        return collectionRepository.fetchByNameObserver(query);
    }
}