package com.keeprecipes.android.presentationLayer.home;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public record CategoriesDTO(long categoriesId, String name, boolean selected) {
    public static final DiffUtil.ItemCallback<com.keeprecipes.android.dataLayer.entities.Category> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull com.keeprecipes.android.dataLayer.entities.Category oldItem, @NonNull com.keeprecipes.android.dataLayer.entities.Category newItem) {
            return Objects.equals(oldItem.categoriesId, newItem.categoriesId);
        }

        @Override
        public boolean areContentsTheSame(@NonNull com.keeprecipes.android.dataLayer.entities.Category oldItem, @NonNull com.keeprecipes.android.dataLayer.entities.Category newItem) {
            return oldItem.equals(newItem);
        }
    };
}