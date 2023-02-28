package com.keeprecipes.android.presentationLayer.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.keeprecipes.android.R;
import com.keeprecipes.android.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    final String TAG = "HomeFragment";

    private FragmentHomeBinding binding;

    HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        RecipeAdapter recipeAdapter = new RecipeAdapter();
        binding.recipeListView.setAdapter(recipeAdapter);
        homeViewModel.getRecipe().observe(getViewLifecycleOwner(), recipeAdapter::submitList);

        binding.searchBar.inflateMenu(R.menu.top_menu);
        binding.searchBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d(TAG, "onMenuItemClick: " + item);
                return false;
            }
        });

        binding.searchView.getEditText().setOnEditorActionListener(
                (v, actionId, event) -> {
                    Log.d(TAG, "setOnEditorActionListener: " + binding.searchView.getText());
                    binding.searchBar.setText(binding.searchView.getText());
                    homeViewModel.searchRecipe(String.valueOf(binding.searchView.getText())).observe(getViewLifecycleOwner(), recipeAdapter::submitList);
                    binding.searchView.hide();
                    return false;
                });


//        binding.toolbar.setTitle("Recipes");
//        binding.toolbar.inflateMenu(R.menu.top_menu);

//        MenuItem item = binding.toolbar.getMenu().getItem(R.id.action_search);
//        SearchView searchView = (SearchView) item.getActionView();

//        binding.toolbar.setOnMenuItemClickListener(item -> {
//            if (item.getItemId() == R.id.action_search) {
//                Log.d(TAG, "onViewCreated: search is being called");
//                homeViewModel.searchRecipe().observe(getViewLifecycleOwner(), recipeAdapter::submitList);
//                return true;
//            }
//            return false;
//        });

        // Floating Action Button to create new recipe
        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_navigation_home_to_addRecipeFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}