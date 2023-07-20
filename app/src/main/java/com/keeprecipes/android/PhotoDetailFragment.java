package com.keeprecipes.android;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.keeprecipes.android.databinding.FragmentPhotoDetailBinding;


public class PhotoDetailFragment extends Fragment {

    private FragmentPhotoDetailBinding binding;
    private ScreenSlidePagerAdapter pagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPhotoDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager(), getLifecycle());
//        pagerAdapter.addFragment(new PhotoDetailImageFragment(new Uri("df")));
        binding.imageViewpager.setAdapter(pagerAdapter);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (binding.imageViewpager.getCurrentItem() == 0) {
                    requireActivity().getOnBackPressedDispatcher().onBackPressed();
                } else {
                    binding.imageViewpager.setCurrentItem(binding.imageViewpager.getCurrentItem() - 1);
                }
            }
        });
    }
}