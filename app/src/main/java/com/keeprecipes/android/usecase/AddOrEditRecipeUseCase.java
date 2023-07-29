package com.keeprecipes.android.usecase;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.FileUtils;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

import com.keeprecipes.android.data.entities.Category;
import com.keeprecipes.android.data.entities.Ingredient;
import com.keeprecipes.android.data.entities.Recipe;
import com.keeprecipes.android.data.repository.CollectionRepository;
import com.keeprecipes.android.data.repository.CollectionWithRecipesRepository;
import com.keeprecipes.android.data.repository.RecipeRepository;
import com.keeprecipes.android.presentation.addRecipe.IngredientDTO;
import com.keeprecipes.android.presentation.addRecipe.PhotoDTO;
import com.keeprecipes.android.presentation.addRecipe.RecipeDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class AddOrEditRecipeUseCase {

    private final String TAG = "AddRecipeUseCase";
    CollectionRepository collectionRepository;
    RecipeRepository recipeRepository;
    CollectionWithRecipesRepository collectionWithRecipesRepository;

    DeleteFilesUseCase deleteFilesUseCase;

    @Inject
    public AddOrEditRecipeUseCase(CollectionRepository collectionRepository, RecipeRepository recipeRepository, CollectionWithRecipesRepository collectionWithRecipesRepository, DeleteFilesUseCase deleteFilesUseCase) {
        this.collectionRepository = collectionRepository;
        this.recipeRepository = recipeRepository;
        this.collectionWithRecipesRepository = collectionWithRecipesRepository;
        this.deleteFilesUseCase = deleteFilesUseCase;
    }

    public void save(Application application, RecipeDTO recipe, List<String> categories, List<PhotoDTO> photoDTOList, List<IngredientDTO> ingredientDTOList, boolean updateRecipe) {
        if (recipe != null) {
            Recipe recipeToSave = new Recipe();
            recipeToSave.recipeId = recipe.id;
            recipeToSave.title = recipe.title;
            recipeToSave.instructions = recipe.instructions;
            recipeToSave.portionSize = recipe.portionSize;
            recipeToSave.dateCreated = Instant.now();

            ArrayList<Long> collectionId = new ArrayList<>();
            if (categories != null) {
                for (String c : categories) {
                    Category category = new Category(c);
                    if (!collectionRepository.isRowExist(category)) {
                        collectionId.add(collectionRepository.insert(category));
                    } else {
                        long id = collectionRepository.fetchByName(category.name);
                        if (id != -1L) {
                            collectionId.add(id);
                        } else {
                            throw new RuntimeException("Couldn't find id");
                        }
                    }
                }
            }

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
                            fileName = DocumentFile.fromSingleUri(application, photo.uri).getName();
                        }
                        Log.d(TAG, "saveRecipe: filename" + fileName);
                        assert fileName != null;
                        File photoFile = new File(application.getFilesDir(), fileName);
                        int fileCount = 0;
                        while (photoFile.exists()) {
                            fileCount++;
                            String[] nameSplits = fileName.split("-|\\.");
                            String fileNameWithoutExtension = nameSplits[0];
                            String extension = nameSplits[nameSplits.length - 1];
                            fileName = fileNameWithoutExtension + "-" + fileCount + "." + extension;
                            photoFile = new File(application.getFilesDir(), fileName);
                        }
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
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            recipeToSave.photos = photoFiles;

            List<Ingredient> ingredientList = new ArrayList<>();
            for (IngredientDTO ingredientDTO : ingredientDTOList) {
                try {
                    ingredientList.add(ingredientDTO.id, new Ingredient(ingredientDTO.name, Integer.parseInt(ingredientDTO.size), ingredientDTO.quantity));
                } catch (NumberFormatException e) {
                    Log.d(TAG, "saveRecipe: " + e);
                }
            }
            recipeToSave.ingredients = ingredientList;
            long recipeId;
            if (updateRecipe) {
                List<String> currentlySavedPhotos = recipe.photoURI;
                if (currentlySavedPhotos != null) {
                    currentlySavedPhotos.removeAll(recipeToSave.photos);
                    currentlySavedPhotos.forEach(s -> {
                        try {
                            deleteFilesUseCase.delete(application.getFilesDir() + "/" + s);
                        } catch (IOException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                recipeRepository.update(recipeToSave);
                recipeId = recipeToSave.recipeId;
            } else {
                recipeId = recipeRepository.insert(recipeToSave);
            }
            collectionWithRecipesRepository.insert(collectionId, recipeId);
        }
    }
}
