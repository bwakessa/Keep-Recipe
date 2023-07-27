package com.keeprecipes.android.presentation.searchResult;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.hilt.navigation.HiltViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.keeprecipes.android.R;
import com.keeprecipes.android.databinding.FragmentSearchBinding;
import com.keeprecipes.android.presentation.home.RecipeAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchFragment extends Fragment {

    final String TAG = "SearchFragment";

    private SearchViewModel mViewModel;

    private FragmentSearchBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            RecipeAdapter recipeAdapter = new RecipeAdapter();
            binding.recipeListView.setAdapter(recipeAdapter);
            binding.recipeListView.setAdapter(recipeAdapter);
            String searchArg = SearchFragmentArgs.fromBundle(getArguments()).getSearchArg();
            Log.d(TAG, "onViewCreated: " + searchArg);

            NavController navController = Navigation.findNavController(view);
            NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.mobile_navigation);
            SearchViewModel searchViewModel = new ViewModelProvider(backStackEntry, HiltViewModelFactory.create(view.getContext(), backStackEntry)).get(SearchViewModel.class);
            searchViewModel.searchRecipe(searchArg).observe(getViewLifecycleOwner(), recipeAdapter::submitList);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}