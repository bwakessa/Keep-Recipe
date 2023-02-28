package com.keeprecipes.android.presentationLayer.searchResult;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.keeprecipes.android.dataLayer.entities.Recipe;
import com.keeprecipes.android.databinding.FragmentSearchBinding;
import com.keeprecipes.android.presentationLayer.home.RecipeAdapter;

import java.util.List;

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
            SearchViewModel searchViewModel =
                    new ViewModelProvider(this).get(SearchViewModel.class);
            searchViewModel.searchRecipe(searchArg).observe(getViewLifecycleOwner(), recipeAdapter::submitList);
        }
    }
}