package com.keeprecipes.android;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.keeprecipes.android.databinding.FragmentRecipeDetailBinding;
import com.keeprecipes.android.presentationLayer.home.HomeViewModel;

public class RecipeDetailFragment extends Fragment {

    private static final String TAG = "RecipeDetailFragment";

    private FragmentRecipeDetailBinding binding;
    private HomeViewModel homeViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false);
//        binding.setRecipe();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int recipeId = RecipeDetailFragmentArgs.fromBundle(getArguments()).getRecipeId();
        Log.d(TAG, "onViewCreated: recipeId " + recipeId);
        homeViewModel.getRecipeById(recipeId).observe(getViewLifecycleOwner(), binding::setRecipe);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}