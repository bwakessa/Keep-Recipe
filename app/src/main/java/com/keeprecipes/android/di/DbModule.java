package com.keeprecipes.android.di;

import android.content.Context;

import androidx.room.Room;

import com.keeprecipes.android.KeepRecipeApplication;
import com.keeprecipes.android.data.AppDatabase;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DbModule {

    @Provides
    @Singleton
    @Inject
    public AppDatabase provideDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "recipe-db").fallbackToDestructiveMigration().build();
    }

    @Provides
    @Singleton
    public KeepRecipeApplication provideApplication(@ApplicationContext Context context) {
        return (KeepRecipeApplication) context;
    }
}
