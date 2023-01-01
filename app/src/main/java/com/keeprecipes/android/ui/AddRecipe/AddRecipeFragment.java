package com.keeprecipes.android.ui.AddRecipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.keeprecipes.android.R;
import com.keeprecipes.android.databinding.FragmentAddRecipeBinding;

public class AddRecipeFragment extends Fragment {

    private FragmentAddRecipeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddRecipeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // AddRecipeFragment has it's toolbar,
        // here we are setting title, back arrow and the menu for toolbar
        binding.toolbar.setTitle("Add Recipe");
        binding.toolbar.inflateMenu(R.menu.add_recipe_menu);
        binding.toolbar.setNavigationIcon(R.drawable.ic_outline_arrow_back_24);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AddRecipeViewModel mViewModel = new ViewModelProvider(this).get(AddRecipeViewModel.class);
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
    }
}