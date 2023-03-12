package com.keeprecipes.android.presentationLayer.home;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.keeprecipes.android.dataLayer.entities.Recipe;

public class RecipeDiff {
    public static final DiffUtil.ItemCallback<Recipe> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull Recipe oldItem, @NonNull Recipe newItem) {
            return oldItem.recipeId == newItem.recipeId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Recipe oldItem, @NonNull Recipe newItem) {
            return oldItem.equals(newItem);
        }
    };
}
