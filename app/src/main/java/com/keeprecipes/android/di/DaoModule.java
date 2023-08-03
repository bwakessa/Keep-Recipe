package com.keeprecipes.android.di;

import com.keeprecipes.android.data.AppDatabase;
import com.keeprecipes.android.data.dao.CollectionDao;
import com.keeprecipes.android.data.dao.CollectionWithRecipesDao;
import com.keeprecipes.android.data.dao.RecipeDao;
import com.keeprecipes.android.data.dao.RecipeWithCollectionsDao;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(ViewModelComponent.class)
public class DaoModule {
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
}
