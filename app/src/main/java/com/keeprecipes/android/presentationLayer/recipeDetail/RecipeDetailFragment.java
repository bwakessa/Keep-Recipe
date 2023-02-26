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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.keeprecipes.android.R;
import com.keeprecipes.android.dataLayer.entities.Recipe;
import com.keeprecipes.android.databinding.FragmentRecipeDetailBinding;
import com.keeprecipes.android.presentationLayer.addRecipe.AddRecipeViewModel;
import com.keeprecipes.android.presentationLayer.addRecipe.IngredientDTO;
import com.keeprecipes.android.presentationLayer.addRecipe.PhotoDTO;
import com.keeprecipes.android.presentationLayer.addRecipe.RecipeDTO;
import com.keeprecipes.android.presentationLayer.home.HomeFragmentDirections;
import com.keeprecipes.android.presentationLayer.home.HomeViewModel;
import com.keeprecipes.android.presentationLayer.recipeDetail.RecipeDetailFragmentDirections.ActionRecipeDetailFragmentToAddRecipeFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecipeDetailFragment extends Fragment implements PhotoAdapter.Photo {

    private static final String TAG = "RecipeDetailFragment";
    private FragmentRecipeDetailBinding binding;

    PhotoAdapter photoAdapter;

    Recipe mRecipe;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding.toolbar.inflateMenu(R.menu.recipe_detail_menu);
        binding.toolbar.setNavigationIcon(R.drawable.ic_outline_arrow_back_24);
        binding.toolbar.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_edit) {// Navigate to settings screen
                ActionRecipeDetailFragmentToAddRecipeFragment action = RecipeDetailFragmentDirections.actionRecipeDetailFragmentToAddRecipeFragment();
                action.setRecipeId(mRecipe.getId());
                Navigation.findNavController(view).navigate(action);
                return true;
            } else if (item.getItemId() == R.id.action_delete) {
                homeViewModel.deleteRecipe(mRecipe);
                Toast.makeText(getActivity(), "Recipe Deleted", Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
                return true;
            }
            return false;
        });

        photoAdapter = new PhotoAdapter(this);
        binding.photoListView.setAdapter(photoAdapter);
        assert getArguments() != null;
        int recipeId = RecipeDetailFragmentArgs.fromBundle(getArguments()).getRecipeId();
        Log.d(TAG, "onViewCreated: recipeId " + recipeId);
        homeViewModel.setRecipeId(recipeId);
        homeViewModel.selectedRecipe.observe(getViewLifecycleOwner(), new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                mRecipe = recipe;
                binding.setRecipe(recipe);
                assert recipe != null;
                if (recipe.photos != null) {
                    List<PhotoDTO> photoDTOList = new ArrayList<>();
                    for (int a = 0; a < recipe.photos.size(); a++) {
                        photoDTOList.add(new PhotoDTO(a, Uri.fromFile(new File(getContext().getFilesDir() + "/" + recipe.photos.get(a)))));
                    }
                    photoAdapter.submitList(photoDTOList);
                }
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