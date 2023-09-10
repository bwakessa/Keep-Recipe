package com.keeprecipes.android.presentation.photoDetail;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.keeprecipes.android.databinding.PhotoDetailImageSlideBinding;
import com.squareup.picasso.Picasso;

import java.io.File;

public class PhotoDetailImageFragment extends Fragment {

    private final String TAG = "PhotoDetailImageFragment";
    private PhotoDetailImageSlideBinding binding;
    private Uri photoUri;

    public static PhotoDetailImageFragment newInstance(Uri photoUri) {

        Bundle args = new Bundle();
        args.putString("Photo Uri", photoUri.getPath());

        PhotoDetailImageFragment fragment = new PhotoDetailImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PhotoDetailImageSlideBinding.inflate(inflater, container, false);
        assert getArguments() != null;
        this.photoUri = Uri.parse(getArguments().getString("Photo Uri"));
        Log.d(TAG, "onCreateView: " + getArguments().getString("Photo Uri"));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        File file = new File(String.valueOf(this.photoUri));
        Picasso.get().load(file)
                .into(binding.recipeFullImage);
    }
}
