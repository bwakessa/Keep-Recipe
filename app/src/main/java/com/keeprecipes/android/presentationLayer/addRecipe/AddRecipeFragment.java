package com.keeprecipes.android.presentationLayer.addRecipe;

import static android.os.ext.SdkExtensions.getExtensionVersion;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.keeprecipes.android.R;
import com.keeprecipes.android.dataLayer.entities.Recipe;
import com.keeprecipes.android.databinding.FragmentAddRecipeBinding;

import java.io.IOException;
import java.util.List;

public class AddRecipeFragment extends Fragment implements RecipePhotoAdapter.Photo {

    final String TAG = "AddRecipeFragment";
    IngredientAdapter ingredientAdapter;
    RecipePhotoAdapter recipePhotoAdapter;
    AddRecipeViewModel mViewModel;

    private FragmentAddRecipeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_recipe, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddRecipeViewModel.class);
        assert getArguments() != null;
        int recipeId = AddRecipeFragmentArgs.fromBundle(getArguments()).getRecipeId();
        if (recipeId != -1) {
            mViewModel.getRecipeById(recipeId).observe(getViewLifecycleOwner(), new Observer<Recipe>() {
                @Override
                public void onChanged(Recipe recipe) {
                    mViewModel.setRecipe(recipe);
                    mViewModel.addIngredientList(recipe.ingredients);
                }
            });
        }

        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

        binding.setViewModel(mViewModel);
        binding.setLifecycleOwner(this);
        // AddRecipeFragment has it's toolbar,
        // here we are setting title, back arrow and the menu for toolbar
        binding.toolbar.setTitle("Add Recipe");
        binding.toolbar.inflateMenu(R.menu.add_recipe_menu);

        mViewModel.getAllCuisine().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> cuisines) {
                Log.d(TAG, "onChanged: cuisines" + cuisines);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(binding.getRoot().getContext(), android.R.layout.select_dialog_item, cuisines);
                binding.cusineAutoCompleteTextView.setAdapter(adapter);
            }
        });

        mViewModel.getAllCollection().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> collection) {
                binding.collectionAutoCompleteTextView.setAdapter(new ArrayAdapter<>(binding.getRoot().getContext(), android.R.layout.select_dialog_item, collection));
            }
        });

        ingredientAdapter = new IngredientAdapter();
        binding.ingredientsListView.setAdapter(ingredientAdapter);

        mViewModel.ingredients.observe(getViewLifecycleOwner(), ingredientAdapter::submitList);

        recipePhotoAdapter = new RecipePhotoAdapter(this);
        binding.photoListView.setAdapter(recipePhotoAdapter);

        mViewModel.recipe.observe(getViewLifecycleOwner(), new Observer<RecipeDTO>() {
            @Override
            public void onChanged(RecipeDTO recipeDTO) {
                Log.d(TAG, "onChanged: recipe" + recipeDTO);
            }
        });

        mViewModel.ingredients.observe(getViewLifecycleOwner(), ingredientAdapter::submitList);
        mViewModel.photos.observe(getViewLifecycleOwner(), recipePhotoAdapter::submitList);
        // menu item click listener
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_save) {// Navigate to settings screen
                // Go back to previous fragment after saving
                Log.d(TAG, "onViewCreated: recipe - " + mViewModel.recipe.getValue().toString());
                Log.d(TAG, "onViewCreated: ingredients - " + mViewModel.ingredients.getValue().size());
                Log.d(TAG, "onViewCreated: photos - " + mViewModel.photos.getValue().size());
                try {
                    mViewModel.saveRecipe();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(getActivity(), "Recipe Saved", Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
                return true;
            }
            return false;
        });

        binding.addIngredientButton.setOnClickListener(v -> {
            mViewModel.addIngredient();
        });

        binding.removeIngredientButton.setOnClickListener(v -> {
            mViewModel.removeIngredient();
        });

        binding.addPhotoButton.setOnClickListener(v -> {
            // Launch the photo picker and allow the user to choose only images.
            // You will see red line under setMediaType() ignore that.
            // It's an bug with kotlin code used by Google to create contract class

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                Log.d(TAG, "onViewCreated: api " + true);
//            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                Log.d(TAG, "onViewCreated: extension " + getExtensionVersion(Build.VERSION_CODES.R));
//            } else {
//                Log.d(TAG, "onViewCreated: " + false);
//            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && getExtensionVersion(Build.VERSION_CODES.R) >= 2) {
                PickVisualMediaRequest request = new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build();
                pickMedia.launch(request);
            } else {
                galleryActivityLauncher.launch(new String[]{"image/*"});
            }
        });
    }

    // Callback is invoked after the user selects a media item or closes the
    // photo picker.
    // Registers a photo picker activity launcher in single-select mode.
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), this::handleMediaUri);

    // Callback is invoked after the user selects a media item or closes the
    // document picker.
    // Registers a document picker activity launcher in single-select mode.
    // Used on Build.VERSION.SDK_INT < Build.VERSION_CODES.R || getExtensionVersion(Build.VERSION_CODES.R) < 2
    ActivityResultLauncher<String[]> galleryActivityLauncher = registerForActivityResult(new ActivityResultContracts.OpenDocument(), this::handleMediaUri);

    private void handleMediaUri(Uri uri) {
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: " + uri);
            Log.d(TAG, "File type: " + getContext().getContentResolver().getType(uri).split("/")[1]);
            mViewModel.addPhotos(uri);
        } else {
            Log.d("PhotoPicker", "No media selected");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void removeItem(int position) {
        Log.d(TAG, "removeItem: " + position);
        mViewModel.removePhotos(position);
    }
}