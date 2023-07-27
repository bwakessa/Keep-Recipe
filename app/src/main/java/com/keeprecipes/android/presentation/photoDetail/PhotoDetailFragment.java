package com.keeprecipes.android.presentation.photoDetail;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.hilt.navigation.HiltViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.keeprecipes.android.R;
import com.keeprecipes.android.databinding.FragmentPhotoDetailBinding;
import com.keeprecipes.android.presentation.home.HomeViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PhotoDetailFragment extends Fragment {
    private final String TAG = "PhotoDetailFragment";
    private FragmentPhotoDetailBinding binding;
    private ScreenSlidePagerAdapter pagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPhotoDetailBinding.inflate(inflater, container, false);
        getActivity().getWindow().setNavigationBarColor(Color.BLACK);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.mobile_navigation);
        HomeViewModel homeViewModel = new ViewModelProvider(backStackEntry, HiltViewModelFactory.create(view.getContext(), backStackEntry)).get(HomeViewModel.class);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        NavigationUI.setupWithNavController(
                toolbar, navController, appBarConfiguration);

        int photoId = PhotoDetailFragmentArgs.fromBundle(getArguments()).getPhotoId();
        Log.d(TAG, "onViewCreated: " + photoId);

        homeViewModel.getPhotoDTOlist().observe(getViewLifecycleOwner(), photoDTOS -> {
                    pagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager(), getLifecycle(), photoDTOS);
                    binding.imageViewpager.setAdapter(pagerAdapter);
                    binding.imageViewpager.setCurrentItem(photoId, false);
                }
        );
    }
}