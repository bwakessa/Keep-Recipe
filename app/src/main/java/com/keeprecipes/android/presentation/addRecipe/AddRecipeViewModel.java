package com.keeprecipes.android.presentation.addRecipe;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.keeprecipes.android.KeepRecipeApplication;
import com.keeprecipes.android.data.entities.Recipe;
import com.keeprecipes.android.data.entities.RecipeWithCategories;
import com.keeprecipes.android.data.repository.CollectionRepository;
import com.keeprecipes.android.data.repository.CollectionWithRecipesRepository;
import com.keeprecipes.android.data.repository.RecipeRepository;
import com.keeprecipes.android.usecase.AddOrEditRecipeUseCase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddRecipeViewModel extends ViewModel {
    public final MutableLiveData<RecipeDTO> recipe;
    public final MutableLiveData<List<String>> collections;
    public final MutableLiveData<List<IngredientDTO>> ingredients;
    public final MutableLiveData<List<PhotoDTO>> photos;
    public final MutableLiveData<Boolean> updateRecipe;
    private final String TAG = "AddRecipeViewModel";
    CollectionRepository collectionRepository;
    RecipeRepository recipeRepository;
    CollectionWithRecipesRepository collectionWithRecipesRepository;
    KeepRecipeApplication application;

    AddOrEditRecipeUseCase addOrEditRecipeUseCase;

    @Inject
    public AddRecipeViewModel(KeepRecipeApplication application, CollectionRepository collectionRepository, CollectionWithRecipesRepository collectionWithRecipesRepository, RecipeRepository recipeRepository, AddOrEditRecipeUseCase addOrEditRecipeUseCase) {
        this.application = application;
        this.recipeRepository = recipeRepository;
        this.collectionRepository = collectionRepository;
        this.collectionWithRecipesRepository = collectionWithRecipesRepository;

        this.recipe = new MutableLiveData<>(new RecipeDTO());
        this.collections = new MutableLiveData<>(new ArrayList<>());
        this.ingredients = new MutableLiveData<>(new ArrayList<>());
        this.photos = new MutableLiveData<>(new ArrayList<>());
        this.updateRecipe = new MutableLiveData<>(false);
        this.addOrEditRecipeUseCase = addOrEditRecipeUseCase;
        Log.d(TAG, "AddRecipeViewModel: recipe" + recipe.getValue().toString());
    }

    public void setRecipe(Recipe recipe) {
        RecipeDTO recipeDTO = new RecipeDTO(recipe);
        this.recipe.setValue(recipeDTO);
        this.updateRecipe.setValue(true);
        if (recipe.ingredients != null) {
            List<IngredientDTO> ingredientList = new ArrayList<>();
            for (int a = 0; a < recipe.ingredients.size(); a++) {
                ingredientList.add(new IngredientDTO(a, recipe.ingredients.get(a).name, String.valueOf(recipe.ingredients.get(a).size), recipe.ingredients.get(a).quantity));
            }
            this.ingredients.setValue(ingredientList);
        }
        if (recipe.photos != null) {
            List<PhotoDTO> photoList = new ArrayList<>();
            for (int a = 0; a < recipe.photos.size(); a++) {
                photoList.add(new PhotoDTO(a, Uri.fromFile(new File(this.application.getFilesDir(), recipe.photos.get(a)))));
            }
            this.photos.setValue(photoList);
        }
        Log.d(TAG, "AddRecipeViewModel: setRecipe" + this.recipe.getValue().toString());
    }

    public void addIngredient() {
        List<IngredientDTO> ingredientList = ingredients.getValue() == null ? new ArrayList<>() : new ArrayList<>(ingredients.getValue());
        IngredientDTO ingredient = new IngredientDTO(ingredientList.size(), "", "1", "g");
        ingredientList.add(ingredient);
        ingredients.postValue(ingredientList);
    }

    public void removeIngredient() {
        List<IngredientDTO> ingredientList = ingredients.getValue() == null ? new ArrayList<>() : new ArrayList<>(ingredients.getValue());
        if (ingredientList.size() > 0) {
            ingredientList.remove(ingredientList.size() - 1);
            ingredients.postValue(ingredientList);
        }
    }

    public void addPhotos(Uri uri) {
        List<PhotoDTO> photoList = photos.getValue() == null ? new ArrayList<>() : new ArrayList<>(photos.getValue());
        PhotoDTO photo = new PhotoDTO(photoList.size(), uri);
        photoList.add(photo);
        photos.postValue(photoList);
    }

    public void removePhotos(int position) {
        List<PhotoDTO> photoList = photos.getValue() == null ? new ArrayList<>() : new ArrayList<>(photos.getValue());
        if (photoList.size() > position) {
            photoList.remove(position);
            photos.postValue(photoList);
        }
    }

    public void setCollection(List<String> collectionsList) {
        Log.d(TAG, "setCollectionList: " + collectionsList.toString());
        List<String> categoriesList = collectionsList.stream().distinct().collect(Collectors.toList());
        Log.d(TAG, "setCollectionList: " + categoriesList.size());
        collections.postValue(categoriesList);
    }

    public void removeCollection() {
        List<String> categoriesList = collections.getValue() == null ? new ArrayList<>() : new ArrayList<>(collections.getValue());
        if (categoriesList.size() >= 1) {
            categoriesList.remove(categoriesList.size() - 1);
            collections.postValue(categoriesList);
        }
    }

    public void saveRecipe() {
        addOrEditRecipeUseCase.save(application, recipe.getValue(), collections.getValue(), photos.getValue(), ingredients.getValue(), updateRecipe.getValue());
        this.updateRecipe.setValue(false);
    }

    public LiveData<List<RecipeWithCategories>> getRecipeCollections(int recipeId) {
        return collectionWithRecipesRepository.getRecipeWithCategories(recipeId);
    }
}