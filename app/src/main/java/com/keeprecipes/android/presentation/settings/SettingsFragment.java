package com.keeprecipes.android.presentation.settings;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.hilt.navigation.HiltViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.keeprecipes.android.R;
import com.keeprecipes.android.databinding.FragmentSettingsBinding;
import com.keeprecipes.android.presentation.home.HomeViewModel;
import com.keeprecipes.android.usecase.DeleteFilesUseCase;

import java.io.IOException;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsFragment extends Fragment {

    FragmentSettingsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
//        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
//        version = pInfo.versionName.toString()
        String packageVersion = "1.0.0";
        try {
            packageVersion = view.getContext().getPackageManager().getPackageInfo(view.getContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        binding.versionTextView.setText(packageVersion);
        binding.deleteTextView.setOnClickListener(view1 -> new MaterialAlertDialogBuilder(view1.getContext())
                // Use String resources
                .setTitle(R.string.title_alert_dialog)
                .setMessage(R.string.message_alert_dialog)
                .setNegativeButton(R.string.negative_alert_dialog, (dialogInterface, i) -> {

                })
                .setPositiveButton(R.string.positive_alert_dialog, (dialogInterface, i) -> {
                    NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.mobile_navigation);
                    HomeViewModel homeViewModel = new ViewModelProvider(backStackEntry, HiltViewModelFactory.create(view.getContext(), backStackEntry)).get(HomeViewModel.class);
                    homeViewModel.deleteAllRecipes();
                    try {
                        DeleteFilesUseCase deleteFilesUseCase = new DeleteFilesUseCase();
                        deleteFilesUseCase.delete(getActivity().getFilesDir().getPath());
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .show());

        binding.privacyTextView.setOnClickListener(view2 -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://roney.me/"))));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}