package com.keeprecipes.android.data.entities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "recipes")
public class Recipe {
    public static final DiffUtil.ItemCallback<Recipe> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull Recipe oldItem, @NonNull Recipe newItem) {
            return Objects.equals(oldItem.recipeId, newItem.recipeId);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Recipe oldItem, @NonNull Recipe newItem) {
            return oldItem.equals(newItem);
        }
    };
    @PrimaryKey(autoGenerate = true)
    public long recipeId;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "instructions")
    public String instructions;
    @ColumnInfo(name = "cuisine")
    public String cuisine;
    @ColumnInfo(name = "portion-size")
    public String portionSize;
    @ColumnInfo(name = "ingredients")
    public List<Ingredient> ingredients;
    @ColumnInfo(name = "photos")
    public List<String> photos;
    @ColumnInfo(name = "data_created")
    public Instant dateCreated;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(recipeId, recipe.recipeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId);
    }
}
