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
import com.keeprecipes.android.usecase.DeleteRecipeUseCase;

import java.util.List;
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
    private final MutableLiveData<List<PhotoDTO>> photoDTOList;
    CollectionRepository collectionRepository;
    RecipeRepository recipeRepository;
    CollectionWithRecipesRepository collectionWithRecipesRepository;

    DeleteRecipeUseCase deleteRecipeUseCase;

    @Inject
    public HomeViewModel(CollectionRepository collectionRepository, RecipeRepository recipeRepository, CollectionWithRecipesRepository collectionWithRecipesRepository, DeleteRecipeUseCase deleteRecipeUseCase) {
        this.collectionRepository = collectionRepository;
        this.recipeRepository = recipeRepository;
        this.collectionWithRecipesRepository = collectionWithRecipesRepository;
        this.deleteRecipeUseCase = deleteRecipeUseCase;
        this.recipeId = new MutableLiveData<>();
        this.selectedRecipe = Transformations.switchMap(recipeId, (recipe) -> recipeRepository.fetchById(recipeId.getValue()));
        this.photoDTOList = new MutableLiveData<>();
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

    public MutableLiveData<List<PhotoDTO>> getPhotoDTOList() {
        return this.photoDTOList;
    }

    public void setPhotoDTOList(List<PhotoDTO> photoDTOList) {
        this.photoDTOList.setValue(photoDTOList);
    }

    public void deleteRecipe(Recipe recipe) {
        deleteRecipeUseCase.delete(recipe);
    }

    public void deleteAllRecipes() {
        this.deleteRecipeUseCase.deleteAll();
    }

    public LiveData<List<Recipe>> searchRecipe(String query) {
        return recipeRepository.searchRecipe(query);
    }
}