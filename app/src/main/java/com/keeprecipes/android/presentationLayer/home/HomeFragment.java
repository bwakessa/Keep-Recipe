package com.keeprecipes.android.presentationLayer.home;

import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.keeprecipes.android.R;
import com.keeprecipes.android.databinding.FragmentHomeBinding;

import java.util.List;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {

    final String TAG = "HomeFragment";
    HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private List<String> categories;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel =
                new ViewModelProvider(getActivity()).get(HomeViewModel.class);

        homeViewModel.getCollections().observe(getViewLifecycleOwner(), categoriesList -> {
            List<String> collectionNames = categoriesList.stream().map(collection -> collection.name).collect(Collectors.toList());
            binding.categoriesChipGroup.removeAllViews();
            for (String categories : collectionNames) {
                Chip chip = new Chip(getContext());
                chip.setText(categories);
//                    chip.setCloseIconVisible(true);
                // necessary to get single selection working
                chip.setClickable(true);
                chip.setCheckable(true);
                chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        chip.setCloseIconVisible(b);
                        Log.d(TAG, "onCheckedChanged: " + b);
                    }
                });
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view12) {
                        chip.setChecked(false);
                    }
                });
                binding.categoriesChipGroup.addView(chip);
            }
        });

        RecipeAdapter recipeAdapter = new RecipeAdapter();
        binding.recipeListView.setAdapter(recipeAdapter);
        homeViewModel.getRecipes().observe(getViewLifecycleOwner(), recipeAdapter::submitList);

        binding.searchBar.inflateMenu(R.menu.top_menu);
        binding.searchBar.setOnMenuItemClickListener(item -> {
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
            return NavigationUI.onNavDestinationSelected(item, navController);
        });

//        binding.categoriesChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
//            Log.d(TAG, "onCheckedChanged: " + checkedIds);
//            if (!checkedIds.isEmpty()){
//                homeViewModel.collectionFetchByName(categories.get(checkedIds.get(0)-1)).observe(getViewLifecycleOwner(), recipeAdapter::submitList);
//            } else {
//                homeViewModel.getRecipe().observe(getViewLifecycleOwner(), recipeAdapter::submitList);
//            }
//        });

        binding.searchView.getEditText().setOnEditorActionListener(
                (v, actionId, event) -> {
                    Log.d(TAG, "onViewCreated: actionId " + actionId);
                    if (actionId == EditorInfo.IME_NULL
                            && event.getAction() == KeyEvent.ACTION_DOWN) {
                        binding.searchBar.setText(binding.searchView.getText());
//                        binding.categoriesChipGroup.setVisibility(View.GONE);
                        homeViewModel.searchRecipe(String.valueOf(binding.searchView.getText())).observe(getViewLifecycleOwner(), recipeAdapter::submitList);
                        binding.searchView.hide();
                    }
                    Log.d(TAG, "setOnEditorActionListener: " + binding.searchView.getText());
//                    binding.searchView.hide();
                    return false;
                });

        binding.searchView.getToolbar().setNavigationOnClickListener(view1 -> {
            Log.d(TAG, "onClick: back button is pressed");
            binding.searchBar.clearText();
            binding.searchView.hide();
            homeViewModel.getRecipes().observe(getViewLifecycleOwner(), recipeAdapter::submitList);
        });

        // Floating Action Button to create new recipe
        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_navigation_home_to_addRecipeFragment);
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: HomeFragment is stopped");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}