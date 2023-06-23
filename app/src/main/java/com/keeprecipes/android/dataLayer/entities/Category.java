package com.keeprecipes.android.dataLayer.entities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "category")
public class Category {
    public static final DiffUtil.ItemCallback<Category> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
            return Objects.equals(oldItem.categoriesId, newItem.categoriesId);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
            return oldItem.equals(newItem);
        }
    };

    @PrimaryKey(autoGenerate = true)
    public long categoriesId;

    @ColumnInfo(name = "name")
    public String name;

    public Category(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category categories = (Category) o;
        return Objects.equals(categoriesId, categories.categoriesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoriesId);
    }
}
