package com.keeprecipes.android.presentation.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.keeprecipes.android.data.entities.Recipe;
import com.keeprecipes.android.data.repository.CollectionRepository;
import com.keeprecipes.android.data.repository.CollectionWithRecipesRepository;
import com.keeprecipes.android.data.repository.RecipeRepository;
import com.keeprecipes.android.presentation.addRecipe.PhotoDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeViewModel extends ViewModel {
    public final LiveData<Recipe> selectedRecipe;
    final String TAG = "AndroidViewModel";
    private final LiveData<List<Recipe>> recipe;
    private final MutableLiveData<Integer> recipeId;
    private final MutableLiveData<Long> selectedCategory;
    private final MediatorLiveData<List<CategoriesDTO>> filteredCategoryRecipe;
    private final MutableLiveData<List<PhotoDTO>> photoDTOlist;
    CollectionRepository collectionRepository;
    RecipeRepository recipeRepository;
    CollectionWithRecipesRepository collectionWithRecipesRepository;

    @Inject
    public HomeViewModel(CollectionRepository collectionRepository, RecipeRepository recipeRepository, CollectionWithRecipesRepository collectionWithRecipesRepository) {
        this.collectionRepository = collectionRepository;
        this.recipeRepository = recipeRepository;
        this.collectionWithRecipesRepository = collectionWithRecipesRepository;
        this.recipeId = new MutableLiveData<>();
        this.selectedRecipe = Transformations.switchMap(recipeId, (recipe) -> recipeRepository.fetchById(recipeId.getValue()));
        this.photoDTOlist = new MutableLiveData<>();
        this.selectedCategory = new MutableLiveData<>((long) -2);
        LiveData<List<CategoriesDTO>> categories = Transformations.map(collectionRepository.getAllCollections(), collections -> collections.stream().map(e -> new CategoriesDTO(e.categoriesId, e.name, false)).collect(Collectors.toList()));
        this.filteredCategoryRecipe = new MediatorLiveData<>();
        this.filteredCategoryRecipe.addSource(categories, filteredCategoryRecipe::setValue);
        this.filteredCategoryRecipe.addSource(this.selectedCategory, selectedCategoryId -> {
            List<CategoriesDTO> c = filteredCategoryRecipe.getValue();
            if (c != null && selectedCategoryId != -2) {
                c.forEach(categoriesDTO -> {
                    Log.d(TAG, "HomeViewModel: selectedCategoryId outside " + selectedCategoryId + categoriesDTO.name + categoriesDTO.categoriesId);
                    if (categoriesDTO.categoriesId != selectedCategoryId) {
                        Log.d(TAG, "HomeViewModel: selectedCategoryId inside " + selectedCategoryId + categoriesDTO.name + categoriesDTO.categoriesId);
                        categoriesDTO.selected = false;
                    } else {
                        categoriesDTO.selected = true;
                    }
                });
                filteredCategoryRecipe.setValue(c);
            }
        });
        this.recipe = Transformations.switchMap(selectedCategory, selectedCategoryId -> {
            Log.d(TAG, "HomeViewModel: " + selectedCategoryId);
            if (selectedCategoryId == -1 || selectedCategoryId == -2) {
                return recipeRepository.getAllRecipes();
            } else {
                return collectionWithRecipesRepository.getCollectionWithRecipesById(selectedCategoryId);
            }
        });
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipe;
    }

    public MediatorLiveData<List<CategoriesDTO>> getCollections() {
        return this.filteredCategoryRecipe;
    }

    public void setSelectedCategory(long categoryId) {
        if (this.selectedCategory.getValue() == categoryId) {
            this.selectedCategory.setValue((long) -1);
        } else {
            this.selectedCategory.setValue(categoryId);
        }
    }

    public void setRecipeId(int id) {
        recipeId.setValue(id);
    }

    public MutableLiveData<List<PhotoDTO>> getPhotoDTOlist() {
        return this.photoDTOlist;
    }

    public void setPhotoDTOlist(List<PhotoDTO> photoDTOlist) {
        this.photoDTOlist.setValue(photoDTOlist);
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

    public void deleteAllRecipes() {
        recipeRepository.drop();
        collectionRepository.drop();
        collectionWithRecipesRepository.drop();
        recipeRepository.clearPrimaryKey();
        collectionRepository.clearPrimaryKey();
        collectionWithRecipesRepository.clearPrimaryKey();
    }

    public LiveData<List<Recipe>> searchRecipe(String query) {
        return recipeRepository.searchRecipe(query);
    }
}