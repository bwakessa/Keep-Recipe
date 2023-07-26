package com.keeprecipes.android;

import android.app.Application;

import com.google.android.material.color.DynamicColors;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class KeepRecipeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DynamicColors.applyToActivitiesIfAvailable(this);
    }
}
