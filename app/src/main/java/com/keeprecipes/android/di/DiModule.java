package com.keeprecipes.android.di;

import android.content.Context;

import androidx.room.Room;

import com.keeprecipes.android.KeepRecipeApplication;
import com.keeprecipes.android.data.AppDatabase;
import com.keeprecipes.android.data.dao.CollectionDao;
import com.keeprecipes.android.data.dao.CollectionWithRecipesDao;
import com.keeprecipes.android.data.dao.RecipeDao;
import com.keeprecipes.android.data.dao.RecipeWithCollectionsDao;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DiModule {

    @Provides
    @Singleton
    @Inject
    public AppDatabase provideDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "recipe-db").fallbackToDestructiveMigration().build();
    }

    @Provides
    @Inject
    public RecipeDao provideRecipeDao(AppDatabase db) {
        return db.recipeDao();
    }

    @Provides
    @Inject
    public CollectionDao provideCollectionDao(AppDatabase db) {
        return db.recipeCollectionDao();
    }

    @Provides
    @Inject
    public RecipeWithCollectionsDao provideRecipeWithCollectionsDao(AppDatabase db) {
        return db.recipeWithCollectionsDao();
    }

    @Provides
    @Inject
    public CollectionWithRecipesDao provideCollectionWithRecipesDao(AppDatabase db) {
        return db.collectionWithRecipesDao();
    }

    @Singleton
    @Provides
    public KeepRecipeApplication provideApplication(@ApplicationContext Context context) {
        return (KeepRecipeApplication) context;
    }
}
