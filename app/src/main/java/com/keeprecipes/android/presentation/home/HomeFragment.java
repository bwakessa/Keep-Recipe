package com.keeprecipes.android.presentation.home;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.hilt.navigation.HiltViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.keeprecipes.android.R;
import com.keeprecipes.android.databinding.FragmentHomeBinding;

import java.util.List;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment implements ChipClickListener {

    final String TAG = "HomeFragment";
    HomeViewModel homeViewModel;
    RecipeAdapter recipeAdapter;
    CategoriesAdapter categoriesAdapter;
    private FragmentHomeBinding binding;
    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.mobile_navigation);
        homeViewModel = new ViewModelProvider(backStackEntry, HiltViewModelFactory.create(view.getContext(), backStackEntry)).get(HomeViewModel.class);

        recipeAdapter = new RecipeAdapter();
        binding.recipeListView.setAdapter(recipeAdapter);
        homeViewModel.getRecipes().observe(getViewLifecycleOwner(), recipeAdapter::submitList);

        categoriesAdapter = new CategoriesAdapter(this);
        binding.recipeCategoryListView.setAdapter(categoriesAdapter);
        binding.recipeCategoryListView.setItemAnimator(null);
        homeViewModel.getCollections().observe(getViewLifecycleOwner(), categoriesDTOS -> {
            Log.d(TAG, "onChanged: categories changed");
            List<CategoriesDTO> categoryCopy = categoriesDTOS.stream().map(item -> new CategoriesDTO(item.categoriesId, item.name, item.selected)).collect(Collectors.toList());
            categoriesAdapter.submitList(categoryCopy);
        });


        binding.searchBar.inflateMenu(R.menu.top_menu);
        binding.searchBar.setOnMenuItemClickListener(item -> NavigationUI.onNavDestinationSelected(item, navController));

        binding.searchView.getEditText().setOnEditorActionListener(
                (v, actionId, event) -> {
                    Log.d(TAG, "onViewCreated: actionId " + actionId);
                    if (actionId == EditorInfo.IME_NULL
                            && event.getAction() == KeyEvent.ACTION_DOWN) {
                        binding.searchBar.setText(binding.searchView.getText());
                        binding.recipeCategoryListView.setVisibility(View.GONE);
                        homeViewModel.searchRecipe(String.valueOf(binding.searchView.getText())).observe(getViewLifecycleOwner(), recipeAdapter::submitList);
                    }
                    Log.d(TAG, "setOnEditorActionListener: " + binding.searchView.getText());
                    binding.searchView.hide();
                    return true;
                });

        binding.searchView.getToolbar().setNavigationOnClickListener(view1 -> {
            Log.d(TAG, "onClick: back button is pressed");
            binding.searchBar.clearText();
            binding.searchView.hide();
            binding.recipeCategoryListView.setVisibility(View.VISIBLE);
            homeViewModel.getRecipes().observe(getViewLifecycleOwner(), recipeAdapter::submitList);
        });

        // Floating Action Button to create new recipe
        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(v -> navController.navigate(R.id.action_navigation_home_to_addRecipeFragment));
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

    @Override
    public void chipClicked(long categoryId) {
        Log.d(TAG, "chipClicked: " + categoryId);
        homeViewModel.setSelectedCategory(categoryId);
    }
}