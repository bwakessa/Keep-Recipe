package com.keeprecipes.android.presentation.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.keeprecipes.android.databinding.CategoryItemBinding;

public class CategoriesAdapter extends ListAdapter<CategoriesDTO, CategoriesAdapter.ViewHolder> {

    private static ChipClickListener chipClickListener;
    private final String TAG = "CategoriesAdapter";

    public CategoriesAdapter(ChipClickListener chipClickListener) {
        super(CategoriesDTO.DIFF_CALLBACK);
        CategoriesAdapter.chipClickListener = chipClickListener;
    }

    @NonNull
    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        ViewHolder viewHolder = new CategoriesAdapter.ViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
        viewHolder.categoryItemBinding.chip.setOnClickListener(v -> {
            Log.d(TAG, "onClick: " + viewHolder.getBindingAdapterPosition());
            chipClickListener.chipClicked(getCurrentList().get(viewHolder.getBindingAdapterPosition()).categoriesId);
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.ViewHolder holder, int position) {
        CategoriesDTO categories = getItem(position);
        holder.bind(categories);
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

        public void bind(CategoriesDTO category) {
            categoryItemBinding.setCategory(category);
        }
    }
}
