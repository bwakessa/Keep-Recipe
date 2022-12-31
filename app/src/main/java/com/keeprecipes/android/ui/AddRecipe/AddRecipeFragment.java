package com.keeprecipes.android.ui.AddRecipe;

import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keeprecipes.android.R;
import com.keeprecipes.android.databinding.FragmentAddRecipeBinding;

public class AddRecipeFragment extends Fragment {

    private AddRecipeViewModel mViewModel;

    private FragmentAddRecipeBinding binding;

    public static AddRecipeFragment newInstance() {
        return new AddRecipeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAddRecipeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.toolbar.setTitle("Add Recipe");
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddRecipeViewModel.class);
        binding.toolbar.inflateMenu(R.menu.add_recipe_menu);
        binding.toolbar.setNavigationIcon(R.drawable.ic_outline_arrow_back_24);
        binding.toolbar.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
    }
}