package com.keeprecipes.android.presentationLayer.collections;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.keeprecipes.android.CollectionFragment;
import com.keeprecipes.android.CuisineFragment;
import com.keeprecipes.android.databinding.FragmentRecipeGroupsBinding;

public class RecipeGroupsFragment extends Fragment {

    final String TAG = "CollectionsFragment";

    private FragmentRecipeGroupsBinding binding;

    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager2 viewPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private FragmentStateAdapter pagerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRecipeGroupsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = binding.pager;
        viewPager.setAdapter(new ViewPagerAdapter(getActivity()));
        new TabLayoutMediator(binding.tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(((ViewPagerAdapter)(viewPager.getAdapter())).mFragmentNames[position]);
            }
        }).attach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public class ViewPagerAdapter extends FragmentStateAdapter {

        private final Fragment[] mFragments = new Fragment[] {//Initialize fragments views
//Fragment views are initialized like any other fragment (Extending Fragment)
                new CuisineFragment(),//First fragment to be displayed within the pager tab number 1
                new CollectionFragment(),
        };
        public final String[] mFragmentNames = new String[] {//Tabs names array
                "Cuisine",
                "Collection"
        };

        public ViewPagerAdapter(FragmentActivity fa){//Pager constructor receives Activity instance
            super(fa);
        }

        @Override
        public int getItemCount() {
            return mFragments.length;//Number of fragments displayed
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return mFragments[position];
        }
    }
}