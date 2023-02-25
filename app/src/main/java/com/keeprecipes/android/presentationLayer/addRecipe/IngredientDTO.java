package com.keeprecipes.android.presentationLayer.addRecipe;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.keeprecipes.android.dataLayer.entities.Ingredient;

import java.util.List;

public class IngredientDTO {
    public static final DiffUtil.ItemCallback<IngredientDTO> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull IngredientDTO oldItem, @NonNull IngredientDTO newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull IngredientDTO oldItem, @NonNull IngredientDTO newItem) {
            return oldItem.equals(newItem);
        }
    };
    public int id;
    public String name;
    public int size;

    public IngredientDTO(int id, String name, int size) {
        this.id = id;
        this.name = name;
        this.size = size;
    }

    @Override
    public String toString() {
        return "IngredientDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", size=" + size +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientDTO that = (IngredientDTO) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}
