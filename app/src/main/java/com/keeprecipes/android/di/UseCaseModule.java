package com.keeprecipes.android.di;

import com.keeprecipes.android.usecase.DeleteFilesUseCase;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;

@Module
@InstallIn(ViewModelComponent.class)
public class UseCaseModule {

    @Provides
    public DeleteFilesUseCase provideDeleteFilesUseCase() {
        return new DeleteFilesUseCase();
    }
}
