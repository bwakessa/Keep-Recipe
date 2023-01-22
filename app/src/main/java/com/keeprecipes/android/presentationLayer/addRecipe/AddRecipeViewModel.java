package com.keeprecipes.android.presentationLayer.addRecipe;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.FileUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.keeprecipes.android.dataLayer.entities.Recipe;
import com.keeprecipes.android.dataLayer.repository.RecipeRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class AddRecipeViewModel extends AndroidViewModel {

    private final String TAG = "AddRecipeViewModel";
    public MutableLiveData<RecipeDTO> recipe = new MutableLiveData<>(new RecipeDTO());
    public MutableLiveData<List<IngredientDTO>> ingredients = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<List<PhotoDTO>> photos = new MutableLiveData<>(new ArrayList<>());
    private RecipeRepository recipeRepository;
    private Application application;

    public AddRecipeViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
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

    public void saveRecipe() throws IOException {
        Recipe recipeToSave = new Recipe();
        recipeToSave.setTitle(recipe.getValue().title);
        recipeToSave.setInstructions(recipe.getValue().instructions);
        recipeToSave.setCuisine(recipe.getValue().cuisine);
        recipeToSave.setCollection(recipe.getValue().collection);
        recipeToSave.setPortionSize(recipe.getValue().portionSize);
        recipeToSave.setDateCreated(Instant.now());
        List<PhotoDTO> photoDTOList = photos.getValue();
        List<String> photoFiles = new ArrayList<>();
        for (PhotoDTO photo : photoDTOList) {
            try (InputStream inputStream = application.getContentResolver().openInputStream(photo.uri)) {
                String fileName = photo.uri.getLastPathSegment() + "." + application.getContentResolver().getType(photo.uri).split("/")[1];
                try (FileOutputStream outputStream = application.openFileOutput(fileName, Context.MODE_PRIVATE)) {
                    File file = new File(photo.uri.getPath());
                    Log.d(TAG, "saveRecipe: app file path " + application.getFilesDir().getAbsolutePath());
                    Log.d(TAG, "saveRecipe: filePath" + file.getAbsolutePath());
                    FileUtils.copy(inputStream, outputStream);
                    Log.d(TAG, "saveRecipe: fileName " + fileName);
                    photoFiles.add(fileName);
                }
            }
        }
        recipeToSave.setPhotos(photoFiles);
        recipeRepository.insert(recipeToSave);
    }
}