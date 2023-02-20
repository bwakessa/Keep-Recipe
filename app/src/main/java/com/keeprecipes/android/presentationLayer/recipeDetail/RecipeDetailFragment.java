package com.keeprecipes.android.presentationLayer.recipeDetail;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.keeprecipes.android.dataLayer.entities.Recipe;
import com.keeprecipes.android.databinding.FragmentRecipeDetailBinding;
import com.keeprecipes.android.presentationLayer.addRecipe.PhotoDTO;
import com.keeprecipes.android.presentationLayer.home.HomeViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipeDetailFragment extends Fragment implements PhotoAdapter.Photo {

    private static final String TAG = "RecipeDetailFragment";
    private FragmentRecipeDetailBinding binding;
    private HomeViewModel homeViewModel;

    PhotoAdapter photoAdapter;

    ArrayList<Uri> recipePhotos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        photoAdapter = new PhotoAdapter(this);
        binding.photoListView.setAdapter(photoAdapter);
        assert getArguments() != null;
        int recipeId = RecipeDetailFragmentArgs.fromBundle(getArguments()).getRecipeId();
        Log.d(TAG, "onViewCreated: recipeId " + recipeId);
        homeViewModel.getRecipeById(recipeId).observe(getViewLifecycleOwner(), new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                binding.setRecipe(recipe);
                List<PhotoDTO> photoDTOList = new ArrayList<>();
                for (int a = 0; a < Objects.requireNonNull(recipe).photos.size(); a++) {
                    photoDTOList.add(new PhotoDTO(a, Uri.fromFile(new File(String.valueOf(new File(getContext().getFilesDir() + "/" + recipe.photos.get(a)))))));
                }
                photoAdapter.submitList(photoDTOList);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void removeItem(int position) {

    }
}