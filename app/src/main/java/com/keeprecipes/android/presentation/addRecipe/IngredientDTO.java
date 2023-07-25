package com.keeprecipes.android.presentation.addRecipe;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

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
    public String size;
    public String quantity;

    public IngredientDTO(int id, String name, String size, String quantity) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.quantity = quantity;
    }

    @NonNull
    @Override
    public String toString() {
        return "IngredientDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", quantity='" + quantity + '\'' +
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
