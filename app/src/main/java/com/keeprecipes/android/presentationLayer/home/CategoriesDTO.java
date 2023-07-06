package com.keeprecipes.android.presentationLayer.home;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class CategoriesDTO {
    public long categoriesId;
    public String name;
    public boolean selected;

    public CategoriesDTO(long categoriesId, String name, boolean selected) {
        this.categoriesId = categoriesId;
        this.name = name;
        this.selected = selected;
    }

    public static final DiffUtil.ItemCallback<CategoriesDTO> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull CategoriesDTO oldItem, @NonNull CategoriesDTO newItem) {
            return Objects.equals(oldItem.categoriesId, newItem.categoriesId);
        }

        @Override
        public boolean areContentsTheSame(@NonNull CategoriesDTO oldItem, @NonNull CategoriesDTO newItem) {
            return oldItem.equals(newItem);
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoriesDTO that = (CategoriesDTO) o;
        return categoriesId == that.categoriesId && selected == that.selected && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoriesId, name, selected);
    }
}