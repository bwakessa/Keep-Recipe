package com.keeprecipes.android.presentationLayer.addRecipe;

import static android.os.ext.SdkExtensions.getExtensionVersion;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
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

import com.google.android.material.chip.ChipDrawable;
import com.keeprecipes.android.R;
import com.keeprecipes.android.databinding.FragmentAddRecipeBinding;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class AddRecipeFragment extends Fragment implements RecipePhotoAdapter.Photo {

    final String TAG = "AddRecipeFragment";
    IngredientAdapter ingredientAdapter;
    RecipePhotoAdapter recipePhotoAdapter;
    AddRecipeViewModel mViewModel;
    // Callback is invoked after the user selects a media item or closes the
    // photo picker.
    // Registers a photo picker activity launcher in single-select mode.
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), this::handleMediaUri);
    // Callback is invoked after the user selects a media item or closes the
    // document picker.
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
            mViewModel.getRecipeCollections(recipeId).observe(getViewLifecycleOwner(), recipeWithCollections -> {
                mViewModel.setRecipe(recipeWithCollections.get(0).recipe);
                Log.d(TAG, "onViewCreated: " + recipeWithCollections.get(0).collections);
                List<String> collectionNames = recipeWithCollections.get(0).collections.stream().map(collection -> collection.name).collect(Collectors.toList());
                ArrayAdapter<String> collectionAdapter = new ArrayAdapter<>(binding.getRoot().getContext(), android.R.layout.select_dialog_item, collectionNames);
                binding.cusineAutoCompleteTextView.setAdapter(collectionAdapter);
                binding.cusineAutoCompleteTextView.setTokenizer(new SpaceTokenizer());
                binding.cusineAutoCompleteTextView.setThreshold(2);
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
//        binding.toolbar.setTitle("Add Recipe");
        binding.toolbar.inflateMenu(R.menu.add_recipe_menu);


        ingredientAdapter = new IngredientAdapter();
        binding.ingredientsListView.setAdapter(ingredientAdapter);

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
            } else if (item.getItemId() == R.id.action_delete) {
                Toast.makeText(getActivity(), "Recipe Deleted", Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
                return true;
            }
            return false;
        });

        binding.cusineAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            final LinkedList<Integer> wordBreakPoints = new LinkedList<>(List.of(0));
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // adding a new character and the new character is space
                if (i1 == 0 && charSequence.charAt(i) == ' ') {
                    Log.d(TAG, "onTextChanged: " + charSequence + i + i1 + i2);
                    ChipDrawable chip = ChipDrawable.createFromResource(getContext(), R.xml.chip);
                    Log.d(TAG, "onTextChanged:" + charSequence.subSequence(wordBreakPoints.getLast(), i) + "**");
                    chip.setText(charSequence.subSequence(wordBreakPoints.getLast(), i));
                    ImageSpan span = new ImageSpan(chip);
                    chip.setBounds(0, 0, chip.getIntrinsicWidth(), chip.getIntrinsicHeight());
                    binding.cusineAutoCompleteTextView.getText().setSpan(span, wordBreakPoints.getLast(), i, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mViewModel.addCollection(charSequence.subSequence(wordBreakPoints.getLast(), i).toString());
                    wordBreakPoints.add(i + 1);
                } else if (i1 == 1) {
                    // When deleting the text or backspace
                    Log.d(TAG, "onTextChanged:" + i + i1 + i2);
                    if (wordBreakPoints.size() > 1 && wordBreakPoints.getLast() >= i) {
                        wordBreakPoints.removeLast();
                        mViewModel.removeCollection();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
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
            PickVisualMediaRequest request = new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build();
            pickMedia.launch(request);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && getExtensionVersion(Build.VERSION_CODES.R) >= 2) {
//                PickVisualMediaRequest request = new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build();
//                pickMedia.launch(request);
//            } else {
//                galleryActivityLauncher.launch(new String[]{"image/*"});
//            }
        });
    }

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