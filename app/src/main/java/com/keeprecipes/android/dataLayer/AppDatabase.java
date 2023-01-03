package com.keeprecipes.android.dataLayer;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.keeprecipes.android.dataLayer.entities.Recipe;
import com.keeprecipes.android.dataLayer.dao.RecipeDao;
import com.keeprecipes.android.dataLayer.entities.RecipeCollection;
import com.keeprecipes.android.dataLayer.dao.RecipeCollectionDao;

@Database(entities = {Recipe.class, RecipeCollection.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
//  SEPERATE INTO TWO DIFFERENT APP DATABASE CLASSES OR NOT?
    public abstract RecipeDao recipeDao();
    public abstract RecipeCollectionDao recipeCollectionDao();

    private static volatile AppDatabase instanceRecipe = null;
    private static volatile AppDatabase instanceCollection = null;

    public static AppDatabase getRecipeDatabase(Context context) {
        if (instanceRecipe == null) {
            synchronized (AppDatabase.class) {
                if (instanceRecipe == null) {
                    instanceRecipe = Room.databaseBuilder(context, AppDatabase.class, "recipe-db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instanceRecipe;
    }

    public static AppDatabase getCollectionDatabase(Context context) {
        if (instanceCollection == null) {
            synchronized (AppDatabase.class) {
                if (instanceCollection == null) {
                    instanceCollection = Room.databaseBuilder(context, AppDatabase.class, "collection-db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instanceCollection;
    }
}
