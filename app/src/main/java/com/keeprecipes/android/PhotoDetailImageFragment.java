package com.keeprecipes.android;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.keeprecipes.android.databinding.PhotoDetailImageSlideBinding;
import com.squareup.picasso.Picasso;

public class PhotoDetailImageFragment extends Fragment {

    private PhotoDetailImageSlideBinding binding;

    private Uri photoUri;

    public PhotoDetailImageFragment(Uri photoUri) {
        this.photoUri = photoUri;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PhotoDetailImageSlideBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Picasso.get()
                .load(this.photoUri)
                .into(binding.recipeFullImage);
    }
}
