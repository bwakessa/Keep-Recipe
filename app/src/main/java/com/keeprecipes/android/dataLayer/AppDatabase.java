package com.keeprecipes.android.dataLayer;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.keeprecipes.android.dataLayer.dao.RecipeCollectionDao;
import com.keeprecipes.android.dataLayer.dao.RecipeDao;
import com.keeprecipes.android.dataLayer.entities.Recipe;
import com.keeprecipes.android.dataLayer.entities.RecipeCollection;

@Database(entities = {Recipe.class, RecipeCollection.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance = null;

    public static AppDatabase getDatabase(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context, AppDatabase.class, "recipe-db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }

    public abstract RecipeDao recipeDao();

    public abstract RecipeCollectionDao recipeCollectionDao();
}
