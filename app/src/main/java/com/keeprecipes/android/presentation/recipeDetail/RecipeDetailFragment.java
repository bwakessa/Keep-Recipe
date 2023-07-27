package com.keeprecipes.android.presentation.recipeDetail;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.hilt.navigation.HiltViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.keeprecipes.android.R;
import com.keeprecipes.android.data.entities.Recipe;
import com.keeprecipes.android.databinding.FragmentRecipeDetailBinding;
import com.keeprecipes.android.presentation.addRecipe.IngredientDTO;
import com.keeprecipes.android.presentation.addRecipe.PhotoDTO;
import com.keeprecipes.android.presentation.home.HomeViewModel;
import com.keeprecipes.android.presentation.photoDetail.PhotoClickListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RecipeDetailFragment extends Fragment implements PhotoClickListener {

    private static final String TAG = "RecipeDetailFragment";
    PhotoAdapter photoAdapter;
    IngredientAdapter ingredientAdapter;
    Recipe mRecipe;
    private List<PhotoDTO> photoDTOList;
    private FragmentRecipeDetailBinding binding;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.navController = Navigation.findNavController(view);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.mobile_navigation);
        HomeViewModel homeViewModel = new ViewModelProvider(backStackEntry, HiltViewModelFactory.create(view.getContext(), backStackEntry)).get(HomeViewModel.class);

        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        NavigationUI.setupWithNavController(
                toolbar, navController, appBarConfiguration);

        binding.toolbar.inflateMenu(R.menu.recipe_detail_menu);
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_edit) {// Navigate to settings screen
                RecipeDetailFragmentDirections.ActionRecipeDetailFragmentToAddRecipeFragment action = RecipeDetailFragmentDirections.actionRecipeDetailFragmentToAddRecipeFragment();
                action.setRecipeId((int) mRecipe.recipeId);
                Navigation.findNavController(view).navigate(action);
                return true;
            } else if (item.getItemId() == R.id.action_delete) {
                homeViewModel.deleteRecipe(mRecipe);
                deleteFiles(photoDTOList);
                Toast.makeText(getActivity(), "Recipe Deleted", Toast.LENGTH_SHORT).show();
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
                return true;
            }
            return false;
        });

        assert getArguments() != null;
        int recipeId = RecipeDetailFragmentArgs.fromBundle(getArguments()).getRecipeId();
        Log.d(TAG, "onViewCreated: recipeId " + recipeId);
        homeViewModel.setRecipeId(recipeId);
        homeViewModel.selectedRecipe.observe(getViewLifecycleOwner(), recipe -> {
            if (recipe != null) {
                mRecipe = recipe;
                binding.setRecipe(recipe);
                photoAdapter = new PhotoAdapter(this);
                binding.photoListView.setAdapter(photoAdapter);
                photoDTOList = new ArrayList<>();
                if (recipe.photos != null) {
                    File appDir = getContext().getFilesDir();
                    for (int a = 0; a < recipe.photos.size(); a++) {
                        photoDTOList.add(new PhotoDTO(a, Uri.fromFile(new File(appDir, recipe.photos.get(a)))));
                    }
                    photoAdapter.submitList(photoDTOList);
                    homeViewModel.setPhotoDTOlist(photoDTOList);
                }
                ingredientAdapter = new IngredientAdapter();
                binding.ingredientsListView.setAdapter(ingredientAdapter);
                if (recipe.ingredients != null) {
                    List<IngredientDTO> ingredientDTOList = new ArrayList<>();
                    for (int a = 0; a < recipe.ingredients.size(); a++) {
                        ingredientDTOList.add(new IngredientDTO(a, recipe.ingredients.get(a).name, String.valueOf(recipe.ingredients.get(a).size), recipe.ingredients.get(a).quantity));
                    }
                    binding.ingredientsTitle.setVisibility(View.VISIBLE);
                    ingredientAdapter.submitList(ingredientDTOList);
                } else {
                    binding.ingredientsTitle.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    void deleteFiles(List<PhotoDTO> photoDTOList) {
        Executors.newSingleThreadExecutor().execute(() -> photoDTOList.forEach(photoDTO -> {
            String deleteCommand = "rm -rf " + photoDTO.uri.getPath();
            Log.d(TAG, "deleteFiles: " + deleteCommand);
            Runtime runtime = Runtime.getRuntime();
            Process process;
            try {
                process = runtime.exec(deleteCommand);
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            process.destroy();
        }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void photoClicked(View view, int photoId) {
        Log.d(TAG, "photoClicked: " + photoDTOList.get(photoId).uri);
        RecipeDetailFragmentDirections.ActionRecipeDetailFragmentToPhotoDetailFragment action = RecipeDetailFragmentDirections.actionRecipeDetailFragmentToPhotoDetailFragment();
        action.setPhotoId(photoId);
        this.navController.navigate(action);
    }
}