package com.keeprecipes.android;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import java.util.Objects;
import java.util.function.Consumer;

public final class HiltFragmentScenario {

    public static <T extends Fragment> ActivityScenario<HiltTestActivity> launchFragmentInHiltContainer(
            Class<T> fragmentClass,
            Bundle args,
            Consumer<Fragment> consumer
    ) {
        Intent startActivityIntent = Intent.makeMainActivity(
                new ComponentName(ApplicationProvider.getApplicationContext(), HiltTestActivity.class)
        );

        return ActivityScenario.<HiltTestActivity>launch(startActivityIntent)
                .onActivity(activity -> {
                    Fragment fragment = activity.getSupportFragmentManager().getFragmentFactory().instantiate(
                            Objects.requireNonNull(fragmentClass.getClassLoader()),
                            fragmentClass.getName()
                    );

                    consumer.accept(fragment);

                    fragment.setArguments(args);
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .add(android.R.id.content, fragment, "")
                            .commitNow();


                });

    }

    public static <T extends Fragment> ActivityScenario<HiltTestActivity> launchFragmentInHiltContainer(Class<T> fragmentClass, Bundle args) {
        return launchFragmentInHiltContainer(fragmentClass, args, fragment -> {
        });
    }

    public static <T extends Fragment> ActivityScenario<HiltTestActivity> launchFragmentInHiltContainer(Class<T> fragmentClass, Consumer<Fragment> consumer) {
        return launchFragmentInHiltContainer(fragmentClass, new Bundle(), consumer);
    }

    public static <T extends Fragment> ActivityScenario<HiltTestActivity> launchFragmentInHiltContainer(Class<T> fragmentClass) {
        return launchFragmentInHiltContainer(fragmentClass, new Bundle(), fragment -> {
        });
    }

}