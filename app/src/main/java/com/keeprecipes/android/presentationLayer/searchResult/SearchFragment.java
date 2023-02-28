package com.keeprecipes.android.presentationLayer.searchResult;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.keeprecipes.android.R;

public class SearchFragment extends Fragment {

    final String TAG = "SearchFragment";

    private SearchViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;
        String searchArg = SearchFragmentArgs.fromBundle(getArguments()).getSearchArg();
        Log.d(TAG, "onViewCreated: " + searchArg);
    }
}