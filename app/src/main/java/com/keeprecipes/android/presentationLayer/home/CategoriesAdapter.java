package com.keeprecipes.android.presentationLayer.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.keeprecipes.android.dataLayer.entities.Category;
import com.keeprecipes.android.databinding.CategoryItemBinding;

public class CategoriesAdapter extends ListAdapter<Category, CategoriesAdapter.ViewHolder> {

    private final ChipClickListener chipClickListener;

    public CategoriesAdapter(ChipClickListener chipClickListener) {
        super(Category.DIFF_CALLBACK);
        this.chipClickListener = chipClickListener;
    }

    @NonNull
    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new CategoriesAdapter.ViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.ViewHolder holder, int position) {
        Category categories = getItem(position);
        holder.bind(categories, chipClickListener);
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CategoryItemBinding categoryItemBinding;

        public ViewHolder(CategoryItemBinding binding) {
            super(binding.getRoot());
            // Define click listener for the ViewHolder's View
            categoryItemBinding = binding;
        }

        public void bind(Category category, ChipClickListener chipClickListener) {
            categoryItemBinding.setCategory(category);
            categoryItemBinding.setItemClickListener(chipClickListener);
        }

    }
}
