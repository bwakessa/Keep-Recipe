package com.keeprecipes.android;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.keeprecipes.android.databinding.FragmentPhotoDetailBinding;
import com.keeprecipes.android.presentationLayer.home.HomeViewModel;


public class PhotoDetailFragment extends Fragment {
    private FragmentPhotoDetailBinding binding;
    private ScreenSlidePagerAdapter pagerAdapter;
    private final String TAG = "PhotoDetailFragment";

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
        HomeViewModel homeViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);

        NavController navController = Navigation.findNavController(view);
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