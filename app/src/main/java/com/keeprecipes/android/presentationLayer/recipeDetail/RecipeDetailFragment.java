package com.keeprecipes.android.presentationLayer.recipeDetail;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.keeprecipes.android.R;
import com.keeprecipes.android.dataLayer.entities.Recipe;
import com.keeprecipes.android.databinding.FragmentRecipeDetailBinding;
import com.keeprecipes.android.presentationLayer.addRecipe.IngredientDTO;
import com.keeprecipes.android.presentationLayer.addRecipe.PhotoDTO;
import com.keeprecipes.android.presentationLayer.home.HomeViewModel;
import com.keeprecipes.android.presentationLayer.recipeDetail.RecipeDetailFragmentDirections.ActionRecipeDetailFragmentToAddRecipeFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class RecipeDetailFragment extends Fragment implements PhotoAdapter.Photo {

    private static final String TAG = "RecipeDetailFragment";
    PhotoAdapter photoAdapter;
    IngredientAdapter ingredientAdapter;
    Recipe mRecipe;
    private FragmentRecipeDetailBinding binding;
    List<PhotoDTO> photoDTOList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HomeViewModel homeViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);

        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        NavigationUI.setupWithNavController(
                toolbar, navController, appBarConfiguration);

        binding.toolbar.inflateMenu(R.menu.recipe_detail_menu);
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_edit) {// Navigate to settings screen
                ActionRecipeDetailFragmentToAddRecipeFragment action = RecipeDetailFragmentDirections.actionRecipeDetailFragmentToAddRecipeFragment();
                action.setRecipeId((int) mRecipe.recipeId);
                Navigation.findNavController(view).navigate(action);
                return true;
            } else if (item.getItemId() == R.id.action_delete) {
                homeViewModel.deleteRecipe(mRecipe);
                deleteFiles(photoDTOList);
                Toast.makeText(getActivity(), "Recipe Deleted", Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
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
                if (recipe.photos != null) {
                    File appDir = getContext().getFilesDir();
                    // TODO: Change to streams
                    for (int a = 0; a < recipe.photos.size(); a++) {
                        photoDTOList.add(new PhotoDTO(a, Uri.fromFile(new File(appDir, recipe.photos.get(a)))));
                    }
                    photoAdapter.submitList(photoDTOList);
                }
                ingredientAdapter = new IngredientAdapter();
                binding.ingredientsListView.setAdapter(ingredientAdapter);
                if (recipe.ingredients != null) {
                    List<IngredientDTO> ingredientDTOList = new ArrayList<>();
                    // TODO: Change to streams
                    for (int a = 0; a < recipe.ingredients.size(); a++) {
                        ingredientDTOList.add(new IngredientDTO(a, recipe.ingredients.get(a).name, String.valueOf(recipe.ingredients.get(a).size), recipe.ingredients.get(a).quantity));
                    }
                    ingredientAdapter.submitList(ingredientDTOList);
                }
            }
        });
    }

    void deleteFiles(List<PhotoDTO> photoDTOList) {
        Executors.newSingleThreadExecutor().execute(() -> {
            photoDTOList.forEach(photoDTO -> {
                String deleteCommand = "rm -rf " + photoDTO.uri.getPath();
                Log.d(TAG, "deleteFiles: "+deleteCommand);
                Runtime runtime = Runtime.getRuntime();
                Process process;
                try {
                    process = runtime.exec(deleteCommand);
                    process.waitFor();
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

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