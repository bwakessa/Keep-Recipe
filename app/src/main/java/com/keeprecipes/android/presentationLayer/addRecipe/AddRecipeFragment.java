package com.keeprecipes.android.presentationLayer.addRecipe;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ext.SdkExtensions;
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
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.keeprecipes.android.R;
import com.keeprecipes.android.databinding.FragmentAddRecipeBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddRecipeFragment extends Fragment {

    private FragmentAddRecipeBinding binding;
    String[] cuisine = {"Chinese", "Ethiopian", "French", "Korean", "Italian", "Japanese", "Indian", "Continental"};
    final String TAG = "AddRecipeFragment";
    ArrayList<Uri> recipePhotos;
    IngredientAdapter ingredientAdapter;
    RecipePhotoAdapter recipePhotoAdapter;
    AddRecipeViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(AddRecipeViewModel.class);
//        binding = FragmentAddRecipeBinding.inflate(inflater, container, false);
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_recipe, container, false);
        binding.setViewModel(mViewModel);
        View root = binding.getRoot();
        // AddRecipeFragment has it's toolbar,
        // here we are setting title, back arrow and the menu for toolbar
        binding.toolbar.setTitle("Add Recipe");
        binding.toolbar.inflateMenu(R.menu.add_recipe_menu);
        binding.toolbar.setNavigationIcon(R.drawable.ic_outline_arrow_back_24);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (binding.getRoot().getContext(), android.R.layout.select_dialog_item, cuisine);
        binding.cusineAutoCompleteTextView.setAdapter(adapter);
        ingredientAdapter = new IngredientAdapter();
        binding.ingredientsListView.setAdapter(ingredientAdapter);
        recipePhotos = new ArrayList<>();
        recipePhotoAdapter = new RecipePhotoAdapter(recipePhotos);
        binding.photoListView.setAdapter(recipePhotoAdapter);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel.ingredients.observe(requireActivity(), ingredients -> ingredientAdapter.submitList(ingredients));
        // To go back to previous fragment
        binding.toolbar.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        // menu item click listener
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_save) {// Navigate to settings screen
                // Go back to previous fragment after saving
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
            if (SdkExtensions.getExtensionVersion(Build.VERSION_CODES.R) >= 2) {
                PickVisualMediaRequest request = new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build();
                pickMedia.launch(request);
            }

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

//    ActivityResultLauncher<String[]> galleryActivityLauncher = registerForActivityResult(new ActivityResultContracts.OpenDocument(), new ActivityResultCallback<Uri>() {
//        public void onActivityResult(Uri result) {
//            if (result != null) {
//                // perform desired operations using the result Uri
//                Log.i(TAG, "onActivityResult: " + result);
//            } else {
//                Log.d(TAG, "onActivityResult: the result is null for some reason");
//            }
//        }
//    });

    // Registers a photo picker activity launcher in single-select mode.
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected URI: " + uri);
                    recipePhotos.add(uri);
                    recipePhotoAdapter.notifyItemInserted(recipePhotos.size());
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });
}