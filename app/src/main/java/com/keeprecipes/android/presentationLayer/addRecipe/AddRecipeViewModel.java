package com.keeprecipes.android.presentationLayer.addRecipe;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.keeprecipes.android.dataLayer.entities.Ingredient;
import com.keeprecipes.android.dataLayer.entities.Recipe;
import com.keeprecipes.android.dataLayer.repository.RecipeRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddRecipeViewModel extends AndroidViewModel {

    public MutableLiveData<RecipeDTO> recipe = new MutableLiveData<>(new RecipeDTO());
    public MutableLiveData<List<IngredientDTO>> ingredients = new MutableLiveData<>(new ArrayList<>());

    public MutableLiveData<List<PhotoDTO>> photos = new MutableLiveData<>(new ArrayList<>());

    private RecipeRepository recipeRepository;

    public AddRecipeViewModel(@NonNull Application application) {
        super(application);
        recipeRepository = new RecipeRepository(application);
    }

    public void addIngredient() {
        List<IngredientDTO> ingredientList = ingredients.getValue() == null ? new ArrayList<>() : new ArrayList<>(ingredients.getValue());
        IngredientDTO ingredient = new IngredientDTO(ingredientList.size(), "", 1);
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

    public void saveRecipe() {
        Recipe recipeToSave = new Recipe();
        recipeToSave.setTitle(recipe.getValue().title);
        recipeToSave.setInstructions(recipe.getValue().instructions);
        recipeToSave.setCuisine(recipe.getValue().cuisine);
        recipeToSave.setCollection(recipe.getValue().collection);
        recipeToSave.setPortionSize(recipe.getValue().portionSize);
        recipeToSave.setDateCreated(Instant.now());
//        recipeToSave.setIngredients(new Ingredient(ing));
        recipeRepository.insert(recipeToSave);
    }
}