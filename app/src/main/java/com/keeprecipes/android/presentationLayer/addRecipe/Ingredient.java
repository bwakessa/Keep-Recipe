package com.keeprecipes.android.presentationLayer.addRecipe;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class Ingredient {
    public int id;
    public String name;
    public int size;

    public Ingredient(int id, String name, int size) {
        this.id = id;
        this.name = name;
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    public static final DiffUtil.ItemCallback<Ingredient> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull Ingredient oldItem, @NonNull Ingredient newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Ingredient oldItem, @NonNull Ingredient newItem) {
            return oldItem.equals(newItem);
        }
    };
}
