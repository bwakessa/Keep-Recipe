package com.keeprecipes.android.presentationLayer.addRecipe;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.chip.ChipDrawable;
import com.keeprecipes.android.R;
import com.keeprecipes.android.databinding.FragmentAddRecipeBinding;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class AddRecipeFragment extends Fragment implements RecipePhotoAdapter.Photo {

    final String TAG = "AddRecipeFragment";
    final LinkedList<Integer> wordBreakPoints = new LinkedList<>(List.of(0));
    IngredientAdapter ingredientAdapter;
    RecipePhotoAdapter recipePhotoAdapter;
    AddRecipeViewModel mViewModel;
    // Callback is invoked after the user selects a media item or closes the
    // photo picker.
    // Registers a photo picker activity launcher in single-select mode.
    final ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), this::handleMediaUri);
    // Callback is invoked after the user selects a media item or closes the
    // document picker.
    private FragmentAddRecipeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_recipe, container, false);
        binding.setLifecycleOwner(this);
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
//                mViewModel.setCategoryList(recipeWithCollections.get(0).categories);
                Log.d(TAG, "onViewCreated: " + recipeWithCollections.get(0).categories);
                List<String> collectionNames = recipeWithCollections.get(0).categories.stream().map(collection -> collection.name).collect(Collectors.toList());
                mViewModel.setCollection(collectionNames);
                ArrayAdapter<String> collectionAdapter = new ArrayAdapter<>(binding.getRoot().getContext(), android.R.layout.select_dialog_item, collectionNames);
                binding.cusineAutoCompleteTextView.setAdapter(collectionAdapter);
                binding.cusineAutoCompleteTextView.setTokenizer(new SpaceTokenizer());
                binding.cusineAutoCompleteTextView.setThreshold(2);
                Log.d(TAG, "onViewCreated: #" + collectionNames);
                if (collectionNames.size() > 0) {
                    setChip(collectionNames);
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
//        binding.toolbar.setTitle("Add Recipe");
        binding.toolbar.inflateMenu(R.menu.add_recipe_menu);


        ingredientAdapter = new IngredientAdapter();
        binding.ingredientsListView.setAdapter(ingredientAdapter);

        recipePhotoAdapter = new RecipePhotoAdapter(this);
        binding.photoListView.setAdapter(recipePhotoAdapter);

        mViewModel.recipe.observe(getViewLifecycleOwner(), recipeDTO -> Log.d(TAG, "onChanged: recipe" + recipeDTO));

        mViewModel.ingredients.observe(getViewLifecycleOwner(), ingredientAdapter::submitList);
        mViewModel.photos.observe(getViewLifecycleOwner(), recipePhotoAdapter::submitList);
        // menu item click listener
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_save) {// Navigate to settings screen
                // Go back to previous fragment after saving
                Log.d(TAG, "onViewCreated: recipe - " + mViewModel.recipe.getValue().toString());
                Log.d(TAG, "onViewCreated: ingredients - " + mViewModel.ingredients.getValue().size());
                Log.d(TAG, "onViewCreated: photos - " + mViewModel.photos.getValue().size());
                Log.d(TAG, "onViewCreated: categories - " + mViewModel.collections.getValue());
                try {
                    mViewModel.saveRecipe();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(getActivity(), "Recipe Saved", Toast.LENGTH_SHORT).show();
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
                return true;
            } else if (item.getItemId() == R.id.action_delete) {
                Toast.makeText(getActivity(), "Recipe Deleted", Toast.LENGTH_SHORT).show();
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
                return true;
            }
            return false;
        });

        binding.cusineAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged: " + charSequence + i + i1 + i2);
                if (i1 == 1) {
                    // When deleting the text or backspace
                    Log.d(TAG, "onTextChanged:" + i + i1 + i2);
                    if (wordBreakPoints.size() > 1 && wordBreakPoints.getLast() >= i) {
                        wordBreakPoints.removeLast();
                        mViewModel.removeCollection();
                        ImageSpan[] spans = binding.cusineAutoCompleteTextView.getEditableText().getSpans(0, charSequence.length(), ImageSpan.class);
                        if (spans.length > 0) {
                            binding.cusineAutoCompleteTextView.getEditableText().removeSpan(spans[spans.length - 1]);
                        }
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // adding a new character and the new character is space
                Log.d(TAG, "onTextChanged: " + charSequence + i);
                if (i1 == 0 && i != 0 && charSequence.charAt(i) == ' ') {
                    Log.d(TAG, "onTextChanged: " + charSequence + i + i1 + i2);
                    ChipDrawable chip = ChipDrawable.createFromResource(getContext(), R.xml.chip);
                    Log.d(TAG, "onTextChanged:" + charSequence.subSequence(wordBreakPoints.getLast(), i) + "**");
                    chip.setText(charSequence.subSequence(wordBreakPoints.getLast(), i));
                    ImageSpan span = new ImageSpan(chip);
                    chip.setBounds(0, 0, chip.getIntrinsicWidth(), chip.getIntrinsicHeight());
                    binding.cusineAutoCompleteTextView.getText().setSpan(span, wordBreakPoints.getLast(), i, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//                    mViewModel.addCollection(charSequence.subSequence(wordBreakPoints.getLast(), i).toString());
                    wordBreakPoints.add(i + 1);
                } else {
                    Log.d(TAG, "onTextChanged: not in if " + charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged: " + editable.toString());
                List<String> collections = Arrays.asList(editable.toString().split("\\s+"));
                Log.d(TAG, "afterTextChanged: " + collections.size());
                if (collections.size() > 0) {
                    mViewModel.setCollection(collections);
                }
            }
        });

        binding.addIngredientButton.setOnClickListener(v -> mViewModel.addIngredient());

        binding.removeIngredientButton.setOnClickListener(v -> mViewModel.removeIngredient());

        binding.addPhotoButton.setOnClickListener(v -> {
            // Launch the photo picker and allow the user to choose only images.
            PickVisualMediaRequest request = new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build();
            pickMedia.launch(request);
        });
    }


    private void setChip(List<String> collectionNames) {
        String chipText = String.join(" ", collectionNames) + " ";
        Log.d(TAG, "setChip: chipTextLength:" + chipText.length());
        int lastPoint = 0;
        SpannableString spannableString = new SpannableString(chipText);
        for (int a = 0; a < collectionNames.size(); a++) {
            ChipDrawable chip = ChipDrawable.createFromResource(getContext(), R.xml.chip);
            chip.setText(collectionNames.get(a));
            ImageSpan span = new ImageSpan(chip);
            chip.setBounds(0, 0, chip.getIntrinsicWidth(), chip.getIntrinsicHeight());
            Log.d(TAG, "setChip: spanStart:" + lastPoint);
            Log.d(TAG, "setChip: spanEnd:" + (lastPoint + collectionNames.get(a).length()));
            spannableString.setSpan(span,
                    lastPoint,
                    lastPoint + collectionNames.get(a).length(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            lastPoint += collectionNames.get(a).length() + 1;
            wordBreakPoints.add(lastPoint);
        }
        binding.cusineAutoCompleteTextView.setText(spannableString);
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
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