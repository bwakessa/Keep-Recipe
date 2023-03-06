package com.keeprecipes.android.presentationLayer.addRecipe;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.FileUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.keeprecipes.android.dataLayer.entities.Ingredient;
import com.keeprecipes.android.dataLayer.entities.Recipe;
import com.keeprecipes.android.dataLayer.repository.RecipeRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddRecipeViewModel extends AndroidViewModel {
    private final String TAG = "AddRecipeViewModel";
    private final RecipeRepository recipeRepository;
    private final Application application;
    public MutableLiveData<RecipeDTO> recipe = new MutableLiveData<>(new RecipeDTO());
    public MutableLiveData<List<IngredientDTO>> ingredients = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<List<PhotoDTO>> photos = new MutableLiveData<>(new ArrayList<>());
    private boolean updateRecipe = false;

    public AddRecipeViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        recipeRepository = new RecipeRepository(application);
        Log.d(TAG, "AddRecipeViewModel: recipe" + recipe.getValue().toString());
    }

    public void setRecipe(Recipe recipe) {
        RecipeDTO recipeDTO = new RecipeDTO(recipe);
        this.recipe.setValue(recipeDTO);
        if (recipe.ingredients != null) {
            List<IngredientDTO> ingredientList = new ArrayList<>();
            for (int a = 0; a < recipe.ingredients.size(); a++) {
                ingredientList.add(new IngredientDTO(a, recipe.ingredients.get(a).name, String.valueOf(recipe.ingredients.get(a).size)));
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
        IngredientDTO ingredient = new IngredientDTO(ingredientList.size(), "", "1");
        ingredientList.add(ingredient);
        ingredients.postValue(ingredientList);
    }

    public void addIngredientList(List<Ingredient> ingredientList) {
        if (ingredientList != null) {
            List<IngredientDTO> ingredientDTOList = new ArrayList<>();
            for (int i = 0; i < ingredientList.size(); i++) {
                ingredientDTOList.add(new IngredientDTO(i, ingredientList.get(i).name, String.valueOf(ingredientList.get(i).size)));
            }
            ingredients.postValue(ingredientDTOList);
        }
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

    public LiveData<List<String>> getAllCuisine() {
        return recipeRepository.getAllCuisine();
    }

    public LiveData<List<String>> getAllCollection() {
        return recipeRepository.getAllCollection();
    }

    public void saveRecipe() throws IOException {
        Recipe recipeToSave = new Recipe();
        recipeToSave.setId(Objects.requireNonNull(recipe.getValue()).id);
        recipeToSave.setTitle(Objects.requireNonNull(recipe.getValue()).title);
        recipeToSave.setInstructions(recipe.getValue().instructions);
        recipeToSave.setCuisine(recipe.getValue().cuisine);
        recipeToSave.setCollection(recipe.getValue().collection);
        recipeToSave.setPortionSize(recipe.getValue().portionSize);
        recipeToSave.setDateCreated(Instant.now());
        List<PhotoDTO> photoDTOList = photos.getValue();
        List<String> photoFiles = new ArrayList<>();
        for (PhotoDTO photo : photoDTOList) {
            try (InputStream inputStream = application.getContentResolver().openInputStream(photo.uri)) {
                // If we are adding a new image then the scheme will of type content
                if (Objects.equals(photo.uri.getScheme(), "content")) {
                    Log.d(TAG, "saveRecipe: "+photo.uri.getAuthority());
                    String fileName;
                    if (Objects.equals(photo.uri.getAuthority(), "media")){
                        fileName = photo.uri.getLastPathSegment() + "." + application.getContentResolver().getType(photo.uri).split("/")[1];
                    } else {
                        fileName = DocumentFile.fromSingleUri(this.application, photo.uri).getName();;
                    }
                    Log.d(TAG, "saveRecipe: filename"+fileName);
                    try (FileOutputStream outputStream = application.openFileOutput(fileName, Context.MODE_PRIVATE)) {
                        File file = new File(photo.uri.getPath());
                        Log.d(TAG, "saveRecipe: app file path " + application.getFilesDir().getAbsolutePath());
                        Log.d(TAG, "saveRecipe: filePath" + file.getAbsolutePath());
                        FileUtils.copy(inputStream, outputStream);
                        Log.d(TAG, "saveRecipe: fileName " + fileName);
                        photoFiles.add(fileName);
                    }
                } else {
                    // When scheme of Uri is file, that means the file has been already copied before,
                    // in that case we only need to store the file name
                    photoFiles.add(photo.uri.getLastPathSegment());
                }
            }
        }
        recipeToSave.setPhotos(photoFiles);
        List<Ingredient> ingredientList = new ArrayList<>();
        for (IngredientDTO ingredientDTO : Objects.requireNonNull(ingredients.getValue())) {
            ingredientList.add(ingredientDTO.id, new Ingredient(ingredientDTO.name, Integer.parseInt(ingredientDTO.size)));
        }
        recipeToSave.setIngredients(ingredientList);
        if (updateRecipe) {
            recipeRepository.update(recipeToSave);
        } else {
            recipeRepository.insert(recipeToSave);
        }
    }

    public LiveData<Recipe> getRecipeById(int recipeId) {
        updateRecipe = true;
        return recipeRepository.fetchById(recipeId);
    }
}