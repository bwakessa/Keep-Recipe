package com.keeprecipes.android.presentationLayer.addRecipe;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.FileUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.keeprecipes.android.dataLayer.entities.Collection;
import com.keeprecipes.android.dataLayer.entities.CollectionRecipeCrossRef;
import com.keeprecipes.android.dataLayer.entities.Ingredient;
import com.keeprecipes.android.dataLayer.entities.Recipe;
import com.keeprecipes.android.dataLayer.entities.RecipeWithCollections;
import com.keeprecipes.android.dataLayer.repository.CollectionRepository;
import com.keeprecipes.android.dataLayer.repository.CollectionWithRecipesRepository;
import com.keeprecipes.android.dataLayer.repository.RecipeRepository;

import org.checkerframework.checker.units.qual.C;

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
    private final CollectionRepository collectionRepository;
    private final RecipeRepository recipeRepository;
    private final CollectionWithRecipesRepository collectionWithRecipesRepository;

    private final Application application;
    public MutableLiveData<RecipeDTO> recipe = new MutableLiveData<>(new RecipeDTO());
    public MutableLiveData<List<Collection>> collections = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<List<IngredientDTO>> ingredients = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<List<PhotoDTO>> photos = new MutableLiveData<>(new ArrayList<>());
    private boolean updateRecipe = false;

    public AddRecipeViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        recipeRepository = new RecipeRepository(application);
        collectionRepository = new CollectionRepository(application);
        collectionWithRecipesRepository = new CollectionWithRecipesRepository(application);
        Log.d(TAG, "AddRecipeViewModel: recipe" + recipe.getValue().toString());
    }

    public void setRecipe(Recipe recipe) {
        RecipeDTO recipeDTO = new RecipeDTO(recipe);
        this.recipe.setValue(recipeDTO);
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

    public void addIngredientList(List<Ingredient> ingredientList) {
        if (ingredientList != null) {
            List<IngredientDTO> ingredientDTOList = new ArrayList<>();
            for (int i = 0; i < ingredientList.size(); i++) {
                ingredientDTOList.add(new IngredientDTO(i, ingredientList.get(i).name, String.valueOf(ingredientList.get(i).size), ingredientList.get(i).quantity));
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

    public void addCollection(String name) {
        List<Collection> collectionList = collections.getValue() == null ? new ArrayList<>() : new ArrayList<>(collections.getValue());
        collectionList.add(new Collection(name));
        setCollectionList(collectionList);
    }

    public void setCollectionList(List<Collection> collectionsList){
        collections.postValue(collectionsList);
    }

    public void removeCollection() {
        List<Collection> collectionList = collections.getValue() == null ? new ArrayList<>() : new ArrayList<>(collections.getValue());
        if (collectionList.size() >= 1) {
            collectionList.remove(collectionList.size() - 1);
            collections.postValue(collectionList);
        }
    }

    public LiveData<List<String>> getAllCuisine() {
        return recipeRepository.getAllCuisine();
    }

    public void saveRecipe() throws IOException {
        Recipe recipeToSave = new Recipe();
        recipeToSave.recipeId = Objects.requireNonNull(recipe.getValue()).id;
        recipeToSave.title = Objects.requireNonNull(recipe.getValue()).title;
        recipeToSave.instructions = recipe.getValue().instructions;
        ArrayList<Long> collectionId = new ArrayList<>();
        if (collections.getValue() != null) {
            for (Collection c : collections.getValue()) {
//                Collection collection = new Collection(c);
//                collectionId.add(collectionRepository.insert(collection));
                if (!collectionRepository.isRowExist(c)) {
                    collectionId.add(collectionRepository.insert(c));
                } else {
                    collectionRepository.fetchByName(c.name).observe((LifecycleOwner) application, collection -> collectionId.add(collection.collectionId));
                }
            }
        }
        recipeToSave.portionSize = recipe.getValue().portionSize;
        recipeToSave.dateCreated = Instant.now();
        List<PhotoDTO> photoDTOList = photos.getValue();
        List<String> photoFiles = new ArrayList<>();
        for (PhotoDTO photo : photoDTOList) {
            try (InputStream inputStream = application.getContentResolver().openInputStream(photo.uri)) {
                // If we are adding a new image then the scheme will of type content
                if (Objects.equals(photo.uri.getScheme(), "content")) {
                    Log.d(TAG, "saveRecipe: " + photo.uri.getAuthority());
                    String fileName;
                    if (Objects.equals(photo.uri.getAuthority(), "media")) {
                        fileName = photo.uri.getLastPathSegment() + "." + application.getContentResolver().getType(photo.uri).split("/")[1];
                    } else {
                        fileName = DocumentFile.fromSingleUri(this.application, photo.uri).getName();
                    }
                    Log.d(TAG, "saveRecipe: filename" + fileName);
                    try (FileOutputStream outputStream = application.openFileOutput(fileName, Context.MODE_PRIVATE)) {
                        File file = new File(photo.uri.getPath());
                        Log.d(TAG, "saveRecipe: app file path " + application.getFilesDir().getAbsolutePath());
                        Log.d(TAG, "saveRecipe: filePath" + file.getAbsolutePath());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            FileUtils.copy(inputStream, outputStream);
                        } else {
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = inputStream.read(buffer)) > 0) {
                                outputStream.write(buffer, 0, length);
                            }
                        }
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
        recipeToSave.photos = photoFiles;
        List<Ingredient> ingredientList = new ArrayList<>();
        for (IngredientDTO ingredientDTO : Objects.requireNonNull(ingredients.getValue())) {
            ingredientList.add(ingredientDTO.id, new Ingredient(ingredientDTO.name, Integer.parseInt(ingredientDTO.size), ingredientDTO.quantity));
        }
        recipeToSave.ingredients = ingredientList;
        long recipeId;
        if (updateRecipe) {
            recipeId = recipeRepository.update(recipeToSave);
        } else {
            recipeId = recipeRepository.insert(recipeToSave);
        }
        for (Long c : collectionId) {
            CollectionRecipeCrossRef collectionRecipeCrossRef = new CollectionRecipeCrossRef(c, recipeId);
            collectionWithRecipesRepository.insert(collectionRecipeCrossRef);
        }
    }

    public LiveData<Recipe> getRecipeById(int recipeId) {
        updateRecipe = true;
        return recipeRepository.fetchById(recipeId);
    }

    public LiveData<List<RecipeWithCollections>> getRecipeCollections(int recipeId) {
        return collectionWithRecipesRepository.getRecipeWithCollections(recipeId);
    }
}